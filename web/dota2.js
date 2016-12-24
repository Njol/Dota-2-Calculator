"use strict";

function toString(x) {
	return "" + (Math.round(x * 10) / 10);
}

function escape(s) {
	return s.replace(/&/, "&amp;").replace(/'/, "&apos;").replace(/"/, "&quot;");
}

function hero_icon(hero_id) {
	return "http://cdn.dota2.com/apps/dota2/images/heroes/" + hero_id + "_hphover.png"
}

function ability_icon(ability_id) {
	if (ability_id == "invoker_attribute_bonus")
		ability_id = "attribute_bonus";
	else if (ability_id == "silencer_stolen_intelligence")
		ability_id = "silencer_glaives_of_wisdom";
	return "http://cdn.dota2.com/apps/dota2/images/abilities/" + ability_id + "_hp1.png"
}

function item_icon(item_id) {
	if (item_id == "__unknown__")
		item_id = "winter_greevil_garbage";
	return "http://cdn.dota2.com/apps/dota2/images/items/" + item_id + "_lg.png";
}

var combined_env = {
	dps_total_after_reductions: {
		value: 100,
		name: "DPS",
		desc: "Damage per second"
	},
	hp: {
		value: 0,
		name: "HP",
		desc: "Health"
	},
	eff_hp_phy: {
		value: 1.5,
		name: "EHPP",
		desc: "Effective HP vs. physical attacks"
	},
	eff_hp_mag: {
		value: 2,
		name: "EHPM",
		desc: "Effective HP vs. magical attacks"
	},
	hp_regen: {
		value: 100,
		name: "HP regen",
		desc: "Health regeneration per second"
	},
	mp: {
		value: 5,
		name: "MP",
		desc: "Mana"
	},
	mp_regen: {
		value: 300,
		name: "MP regen",
		desc: "Mana regeneration per second"
	},
	movement_speed: {
		value: 10,
		name: "Movement speed",
		desc: ""
	},
}
var combined_env_defaults = cloneObject(combined_env);

function update_combined(c, v) {
	combined_env[c].value = v;
	save_combined_env();
	update();
}

var items_table = {
	"Cost": {
		enabled: true,
		desc: "Cost of the item (or if [upgrade] of the remaining components)",
		func: function(env, cost) {
			return env === global_env ? 0 : cost;
		}
	},
	"Str": {
		enabled: true,
		desc: "Strength",
		func: function(env, cost) {
			return env.str;
		}
	},
	"Agi": {
		enabled: true,
		desc: "Agility",
		func: function(env, cost) {
			return env.agi;
		}
	},
	"Int": {
		enabled: true,
		desc: "Intelligence",
		func: function(env, cost) {
			return env.int;
		}
	},
	"Attack Speed": {
		enabled: false,
		func: function(env, cost) {
			return env.attack_speed;
		}
	},
	"Attack Speed/1000 gold": {
		enabled: false,
		desc: "Attack speed per 1000 gold",
		func: function(env, cost) {
			return env.attack_speed / cost * 1000;
		}
	},
	"DPS": {
		enabled: true,
		desc: "Damage per second",
		func: function(env, cost) {
			return env.dps_total_after_reductions;
		}
	},
	"DPS/1000 gold": {
		enabled: true,
		desc: "Damage per second per 1000 gold",
		func: function(env, cost) {
			return env.dps_total_after_reductions / cost * 1000;
		}
	},
	"Health": {
		enabled: true,
		func: function(env, cost) {
			return env.hp;
		}
	},
	"Health/1000 gold": {
		enabled: true,
		desc: "Health per 1000 gold",
		func: function(env, cost) {
			return env.hp / cost * 1000;
		}
	},
	"EHPP": {
		enabled: false,
		desc: "Effective HP vs. physical attacks",
		func: function(env, cost) {
			return env.eff_hp_phy;
		}
	},
	"EHPP/1000 gold": {
		enabled: false,
		desc: "Effective HP vs. physical attacks per 1000 gold",
		func: function(env, cost) {
			return env.eff_hp_phy / cost * 1000;
		}
	},
	"EHPM": {
		enabled: false,
		desc: "Effective HP vs. magical attacks",
		func: function(env, cost) {
			return env.eff_hp_mag;
		}
	},
	"EHPM/1000 gold": {
		enabled: false,
		desc: "Effective HP vs. magical attacks per 1000 gold",
		func: function(env, cost) {
			return env.eff_hp_mag / cost * 1000;
		}
	},
	"EHP 50-50": {
		enabled: true,
		desc: "Effective HP vs. 50% physical and 50% magical attacks",
		func: function(env, cost) {
			return env.hp / (0.5 * env.vulnerability_phy + 0.5 * env.vulnerability_mag);
		}
	},
	"EHP 50-50/1000 gold": {
		enabled: true,
		desc: "Effective HP vs. 50% physical and 50% magical attacks per 1000 gold",
		func: function(env, cost) {
			return (env.hp / (0.5 * env.vulnerability_phy + 0.5 * env.vulnerability_mag)) / cost * 1000;
		}
	},
	"HP regen": {
		enabled: true,
		desc: "Health regeneration per second",
		func: function(env, cost) {
			return env.hp_regen;
		}
	},
	"HP regen/1000 gold": {
		enabled: true,
		desc: "Health regeneration per second per 1000 gold",
		func: function(env, cost) {
			return env.hp_regen / cost * 1000;
		}
	},
	"EHP regen 50-50": {
		enabled: false,
		desc: "Health regeneration of EHP 50-50",
		func: function(env, cost) {
			return env.hp_regen / (0.5 * env.vulnerability_phy + 0.5 * env.vulnerability_mag);
		}
	},
	"EHP regen 50-50/1000 gold": {
		enabled: false,
		desc: "Health regeneration of EHP 50-50 per 1000 gold",
		func: function(env, cost) {
			return env.hp_regen / (0.5 * env.vulnerability_phy + 0.5 * env.vulnerability_mag) / cost * 1000;
		}
	},
	"Mana": {
		enabled: true,
		func: function(env, cost) {
			return env.mp;
		}
	},
	"Mana/1000 gold": {
		enabled: true,
		desc: "Mana per 1000 gold",
		func: function(env, cost) {
			return env.mp / cost * 1000;
		}
	},
	"MP regen": {
		enabled: true,
		desc: "Mana regeneration per second",
		func: function(env, cost) {
			return env.mp_regen;
		}
	},
	"MP regen/1000 gold": {
		enabled: true,
		desc: "Mana regeneration per second per 1000 gold",
		func: function(env, cost) {
			return env.mp_regen / cost * 1000;
		}
	},
	"Armor": {
		enabled: false,
		func: function(env, cost) {
			return env.armor;
		}
	},
	"Armor/1000 gold": {
		enabled: false,
		desc: "Armor per 1000 gold",
		func: function(env, cost) {
			return env.armor / cost * 1000;
		}
	},
	"Movement Speed": {
		enabled: false,
		func: function(env, cost) {
			return env.movement_speed;
		}
	},
	"Movement Speed/1000 gold": {
		enabled: false,
		desc: "Movement speed per 1000 gold",
		func: function(env, cost) {
			return env.movement_speed / cost * 1000;
		}
	},
	"Combined": {
		enabled: false,
		desc: "Combined value of the item per the values set above the table",
		func: function(env, cost) {
			var v = 0;
			for ( var c in combined_env) {
				v += env[c] * combined_env[c].value;
			}
			return v;
		}
	},
	"Combined (% of cost)": {
		enabled: true,
		desc: "Combined value of the item per the values set above the table, in percent of the item&apos;s actual cost in gold",
		func: function(env, cost) {
			var v = 0;
			for ( var c in combined_env) {
				v += env[c] * combined_env[c].value / cost * 100;
			}
			return v;
		}
	},
};
var items_table_defaults = cloneObject(items_table);

function setup() {
	
	if (window.localStorage.options) {
		var opts = JSON.parse(window.localStorage.options);
		for ( var i in opts) { // loop makes sure new options aren't deleted
			options[i] = opts[i];
		}
	}
	if (window.localStorage.combined_env) {
		var c_e = JSON.parse(window.localStorage.combined_env);
		for ( var i in c_e) {
			combined_env[i].value = c_e[i];
		}
	}
	if (window.localStorage.items_table) {
		var i_t = JSON.parse(window.localStorage.items_table);
		for ( var i in i_t) {
			items_table[i].enabled = i_t[i];
		}
	}
	
	var heroSel = ""
	for ( var hero_id in heroes) {
		heroSel += "<option value='" + hero_id + "' style='background-image: url(" + hero_icon(hero_id) + ");'>" + heroes[hero_id].name + "</option>";
	}
	document.getElementById("option_hero").innerHTML = heroSel;
	
	setup_items_table_options();
	
	setup_combined_env();
	
	update_hero_builds();
	
	updateAllOptions(false);
	
	update();
	
}
function setup_combined_env() {
	var itemsCombined = "";
	var i = 0;
	for ( var c in combined_env) {
		itemsCombined += "<label title='" + (combined_env[c].desc || "") + "'>" + combined_env[c].name + ":<input type='text' value='" + combined_env[c].value
				+ "' onchange='update_combined(\"" + escape(c) + "\", this.value)'/></label>";
		i++;
	}
	document.getElementById("items_combined").innerHTML = itemsCombined;
}
function save_combined_env() {
	var c_e = {};
	for ( var i in combined_env) {
		c_e[i] = combined_env[i].value;
	}
	window.localStorage.combined_env = JSON.stringify(c_e);
}
function setup_items_table_options() {
	var itemsColumns = "";
	var i = 0;
	for ( var c in items_table) {
		itemsColumns += "<label title='" + (items_table[c].desc || "") + "'><input type='checkbox' onchange='toggle_column(\"" + escape(c) + "\", this.checked)' "
				+ (items_table[c].enabled ? "checked='checked' " : "") + "/>" + c + "</label>";
		i++;
	}
	document.getElementById("items_columns").innerHTML = itemsColumns;
}
function save_items_table_options() {
	var i_t = {};
	for ( var i in items_table) {
		i_t[i] = items_table[i].enabled;
	}
	window.localStorage.items_table = JSON.stringify(i_t);
}

var oldOnLoad = window.onload;
window.onload = function() {
	if (oldOnLoad)
		oldOnLoad();
	setup();
};

var inventory = ["boots"];
var inventory_defaults = cloneArray(inventory);

function add_item(id) {
	if (inventory.length >= 10)
		return;
	var built_from = items[id].built_from.slice();
	for (var i = 0; i < inventory.length; i++) {
		var j = built_from.indexOf(items[inventory[i]].base_id);
		if (j >= 0) {
			inventory.splice(i, 1);
			built_from.splice(j, 1);
		}
	}
	inventory.push(id);
	update();
}
function remove_item(index) {
	if (index < 0 || index >= inventory.length)
		return;
	inventory.splice(index, 1);
	update();
}

var currentSort = "name";
var sortNatural = true; // ascending for names, descending for numbers
function reset_sort() {
	currentSort = "name";
	sortNatural = true;
}

function sort(column) {
	if (column == currentSort) {
		sortNatural = !sortNatural;
	} else {
		currentSort = column;
		sortNatural = true;
	}
	update();
}
function sorting_function(a, b) {
	if (currentSort == "name") {
		return (sortNatural ? 1 : -1) * a.name.localeCompare(b.name);
	} else {
		return (sortNatural ? 1 : -1) * (b[currentSort] - a[currentSort]);
	}
}

function toggle_column(c, v) {
	items_table[c].enabled = v;
	if (currentSort == c)
		reset_sort();
	save_items_table_options();
	update();
}

function set_ability_option(ability_id, option, value) {
	var ability = abilities[ability_id];
	var min, max;
	if (option == "level") {
		min = 0;
		max = ability.level_max;
	} else if (option == "stacks") {
		min = 0;
		max = ability.stacks_max;
		if (ability.levels[ability.level].stacks_max)
			max = ability.levels[ability.level].stacks_max;
	} else {
		console.log("unhandled ability option " + option);
	}
	if (value < min)
		value = min;
	else if (value > max)
		value = max;
	ability[option] = value;
	//if (option == "level" && ability.is_ultimate && env_options.level < 1 + 5 * value)
	//	document.getElementById("option_level").value = 1 + 5 * value;
	update();
}

var env_options = {
	hero: "abaddon",
	level: 1,
	enemy_vulnerability_mag: 0.75,
	enemy_armor: 5,
	hit_chance_all: 1,
	num_additional_enemies: 0,
	enemy_attacks_per_second: 1,
	enemy_is_hero: true,
	enemy_is_creep: false,
	enemy_is_building: false,
	treat_evasion_as_phy_resist: true,
};
var env_options_defaults = cloneObject(env_options);
var env_options_meta = {
	level: {
		min: 1,
		step: 1,
		max: 25
	},
	enemy_vulnerability_mag: {
		min: 0,
		step: 0,
		max: 1
	},
	enemy_armor: {
		min: -1000,
		step: 0,
		max: 1000
	},
	hit_chance_all: {
		min: 0,
		step: 0,
		max: 1
	},
	num_additional_enemies: {
		min: 0,
		step: 1,
		max: 1000
	},
	enemy_attacks_per_second: {
		min: 0,
		step: 0,
		max: 1000
	},
};
var options = {
	show_consumables: true,
	show_images: true,
};
var options_defaults = cloneObject(options);
// fromHTML = set options from HTML, or if false set HTML from options
function updateAllOptions(fromHTML) {
	for (var i = 0; i < 2; i++) {
		var opts = i == 0 ? env_options : options;
		for ( var option in opts) {
			var pct = false, special_pct = false;
			var elem = document.getElementById("option_" + option);
			if (elem == null) {
				elem = document.getElementById("option_" + option + "%");
				if (elem != null)
					pct = true;
			}
			if (elem == null) {
				elem = document.getElementById("option_" + option + "*%");
				if (elem != null)
					special_pct = true;
			}
			if (elem == null) {
				console.log("missing option element " + option);
				continue;
			}
			
			if (typeof (opts[option]) == "boolean") {
				if (fromHTML)
					opts[option] = elem.checked;
				else
					elem.checked = opts[option];
			} else if (typeof (opts[option]) == "string") {
				if (fromHTML)
					opts[option] = elem.value;
				else
					elem.value = opts[option];
			} else if (typeof (opts[option]) == "number") {
				if (fromHTML) {
					var value = pct ? elem.value / 100 : special_pct ? 1 - elem.value / 100 : +elem.value;
					if (value < env_options_meta[option].min) {
						value = env_options_meta[option].min;
						elem.value = pct ? value * 100 : special_pct ? 100 * (1 - value) : value;
					} else if (value > env_options_meta[option].max) {
						value = env_options_meta[option].max;
						elem.value = pct ? value * 100 : special_pct ? 100 * (1 - value) : value;
					} else if (env_options_meta[option].step > 0 && value % env_options_meta[option].step != 0) {
						value = value - value % env_options_meta[option].step;
						elem.value = pct ? value * 100 : special_pct ? 100 * (1 - value) : value;
					}
					opts[option] = value;
				} else {
					var value = opts[option];
					elem.value = pct ? value * 100 : special_pct ? 100 * (1 - value) : value;
				}
			} else {
				console.log("invalid option element " + option);
			}
		}
	}
	if (fromHTML) {
		save_options();
	}
}
function save_options() {
	window.localStorage.options = JSON.stringify(options);
}

var global_env = new Env();

function reset_hero_build() {
	
	env_options = cloneObject(env_options_defaults);
	updateAllOptions(false);
	
	inventory = cloneArray(inventory_defaults);
	
	for ( var ability_id in abilities) {
		var ability = abilities[ability_id];
		if (ability.level)
			ability.level = 0;
		if (ability.stacks)
			ability.stacks = 0;
	}
	
	current_hero_build_id = null;
	document.getElementById("hero_build_name").value = "1";
	update_hero_builds();
	
	update();
}

function reset_options() {
	
	options = cloneObject(options_defaults);
	updateAllOptions(false);
	save_options(); // update() does this already, but let's not depend on that...
	
	items_table = cloneObject(items_table_defaults);
	setup_items_table_options();
	save_items_table_options();
	
	combined_env = cloneObject(combined_env_defaults);
	setup_combined_env();
	save_combined_env();
	
	reset_sort();
	
	update();
	
}

function update() {
	
	// options
	updateAllOptions(true);
	
	//	var oldLevel = global_env.level;
	var old_hero = global_env.hero;
	
	var heroSelect = document.getElementById("option_hero");
	heroSelect.style.backgroundImage = "url(" + hero_icon(env_options.hero) + ")";
	
	var hero = heroes[env_options.hero];
	global_env = new Env(env_options);
	global_env.addToThis(hero);
	
	//	if (oldLevel != global_env.level) {
	//		global_env.abilities["Attribute Bonus"].level = (global_env.level >= 15 ? 1 : 0) + (global_env.level >= 17 ? global_env.level - 16 : 0);
	//		update_env(global_env);
	//	}
	
	for (var i = 0; i < inventory.length; i++)
		global_env.addToThis(items[inventory[i]]);
	for (var i = 0; i < hero.abilities.length; i++)
		global_env.addToThis(abilities[hero.abilities[i]]);
	//	var global_env_uncalculated = global_env.clone();
	global_env.calculate();
	
	// Stats
	for ( var attr in global_env) {
		var elem = document.getElementById("env_" + attr);
		if (elem)
			elem.innerHTML = toString(global_env[attr]);
		var elem_pct = document.getElementById("env_" + attr + "%");
		if (elem_pct)
			elem_pct.innerHTML = toString(global_env[attr] * 100);
		var elem_special_pct = document.getElementById("env_" + attr + "*%");
		if (elem_special_pct)
			elem_special_pct.innerHTML = toString(100 - global_env[attr] * 100);
		var displayed_attack_damage = document.getElementById("displayed_attack_damage");
		var add_damage = global_env.attack_damage_total - global_env.attack_damage_base;
		displayed_attack_damage.innerHTML = toString(global_env.attack_damage_base) + " " + (add_damage >= 0 ? "+" : "-") + " " + toString(Math.abs(add_damage));
	}
	
	// Abilities
	if (old_hero != env_options.hero) {
		var abiltiesTable = "";
		for (var i = 0; i < hero.abilities.length; i++) {
			var ability_id = hero.abilities[i];
			var ability = abilities[ability_id];
			abiltiesTable += "<tr><th class='image' style='background-image: url(" + ability_icon(ability_id) + ")'>" // <input type='checkbox' onchange='toggle_ability(\"" + escape(ability_id) + "\")' " + (true ? "checked='checked' " : "") + "/>
					+ ability.name + "</th>" + "<td><input type='number' min='0' max='" + ability.level_max + "' step='1' value='" + ability.level
					+ "' onchange='set_ability_option(\"" + escape(ability_id) + "\", \"level\", +this.value)' style='width: 100px;' /></td>";
			var max_stacks = Math.max(ability.stacks_max, ability.levels[ability.level].stacks_max || 0);
			if (max_stacks > 0) {
				abiltiesTable += "<td>" + ability.stacks_name + ":</td><td><input type='number' min='0' max='" + max_stacks + "' step='1' value='" + ability.stacks
						+ "' onchange='set_ability_option(\"" + escape(ability_id) + "\", \"stacks\", +this.value)' style='width: 100px;' /></td>";
			} else {
				abiltiesTable += "<td></td><td></td>";
			}
			abiltiesTable += "</tr>";
		}
		document.getElementById("abilities").getElementsByTagName("tbody")[0].innerHTML = abiltiesTable;
	}
	
	// Inventory
	var invi = "";
	var cost = 0;
	for (var i = 0; i < inventory.length; i++) {
		var item = items[inventory[i]];
		invi += "<tr><td onclick='remove_item(\"" + i + "\")' style='cursor: pointer'><b>×</b></td><td><img src='" + item_icon(item.base_id)
				+ "' style='height: 1.3em; vertical-align: top;'/> " + item.name + "</td>"
		cost += item.cost;
	}
	document.getElementById("inventory").getElementsByTagName("tbody")[0].innerHTML = invi;
	
	// Items table
	var percent_width = 100;
	var header = "<tr><th></th><th style='width: " + percent_width + "px; padding: 0px;'>%</th><th onclick='sort(\"name\")' style='cursor: pointer'>Item " + (currentSort == "name" ? sortNatural ? "↓" : "↑" : "↕") + "</th>";
	for ( var stat in items_table) {
		if (!items_table[stat].enabled)
			continue;
		header += "<th onclick='sort(\"" + escape(stat) + "\")' style='padding: 2px 5px; cursor: pointer' title='" + (items_table[stat].desc || "") + "'>" + stat + " "
				+ (currentSort == stat ? sortNatural ? "↑" : "↓" : "↕") + "</th>";
	}
	document.getElementById("items").getElementsByTagName("thead")[0].innerHTML = header + "</tr>";
	
	var tableItems = [];
	var maxs = {};
	for ( var id in items) {
		var item = items[id];
		if (!options.show_consumables && item.is_consumable || id == "__unknown__")
			continue;
		//		var env = global_env_uncalculated.clone();
		var env = new Env(env_options);
		env.addToThis(hero);
		for (var i = 0; i < hero.abilities.length; i++)
			env.addToThis(abilities[hero.abilities[i]]);
		var name = item.name;
		var cost = item.cost;
		var built_from = item.built_from.slice();
		for (var i = 0; i < inventory.length; i++) {
			var j = built_from.indexOf(items[inventory[i]].base_id);
			if (j >= 0) {
				built_from.splice(j, 1);
				env.cost -= items[inventory[i]].cost;
				cost -= items[inventory[i]].cost;
				if (name.indexOf("[upgrade]") < 0)
					name += " [upgrade]";
			} else {
				env.addToThis(items[inventory[i]]);
			}
		}
		env.addToThis(item);
		env.calculate();
		var tableItem = {
			name: name,
			id: id,
			base_id: item.base_id
		};
		for ( var stat in items_table) {
			var s = items_table[stat];
			if (!s.enabled)
				continue;
			tableItem[stat] = s.func(env, cost) - s.func(global_env, cost);
			maxs[stat] = Math.max(maxs[stat] || 0, tableItem[stat]);
		}
		tableItems.push(tableItem);
	}
	tableItems.sort(sorting_function);
	
	var red = 0xd7, blue = 0x5a, green = 0x28;
	var body = "";
	for (var i = 0; i < tableItems.length; i++) {
		var item = tableItems[i];
		var percent = currentSort == "name" ? "" : (100 * item[currentSort] / maxs[currentSort]).toFixed(1);
		// alternative to box-shadow: "<div style="position: absolute; width: "+percent+"%; right: 0px; height: 100%; top: 0px; background: rgb(" + red + "," + blue + "," + green + "); z-index: -1;"></div>"
		// requires <tr style="position: relative;">
		body += "<tr><td onclick='add_item(\"" + escape(item.id) + "\")' style='cursor: pointer'><b>+</b></td><td style='box-shadow: inset -" + percent / 100 * percent_width
				+ "px 0px rgb(" + red + "," + blue + "," + green + ");'>" + percent + "</td><td style='text-align: left;"
				+ (options.show_images ? "background: url(" + item_icon(item.base_id) + ") no-repeat 4px center/28px auto; padding-left: 36px;" : "") + "'>" + item.name + "</td>";
		for ( var stat in items_table) {
			if (!items_table[stat].enabled)
				continue;
			var r = function(x) {
				return (255 - (255 - x) * item[stat] / maxs[stat]).toFixed(0);
			};
			body += "<td style='background-color: rgb(" + r(red) + "," + r(blue) + "," + r(green) + ")'>" + toString(item[stat]) + "</td>";
		}
		body += "</tr>";
	}
	document.getElementById("items").getElementsByTagName("tbody")[0].innerHTML = body;
	
}

// hero builds

var current_hero_build_id = null;

if (!window.localStorage.nextHeroBuildNum)
	window.localStorage.nextHeroBuildNum = 0;

function make_hero_build_id(num) {
	return "heroBuild_" + num;
}

function is_hero_build_id(id) {
	return id.indexOf("heroBuild_") == 0;
}

function export_hero_builds() {
	var blob = new Blob([JSON.stringify(window.localStorage)], {
		type: "text/plain;charset=utf-8"
	});
	saveAs(blob, "Dota 2 hero builds of njol.ch.json");
}

function import_hero_builds() {
	var reader = new FileReader();
	var file = document.getElementById("hero_build_import_file");
	if (file.files.length == 0)
		return;
	reader.readAsText(file.files[0]);
	reader.onloadend = function(evt) {
		if (evt.target.readyState == FileReader.DONE) {
			var nextNum = +window.localStorage.nextHeroBuildNum;
			var storage = JSON.parse(evt.target.result);
			for ( var id in storage) {
				if (is_hero_build_id(id)) {
					window.localStorage[make_hero_build_id(nextNum)] = storage[id];
					nextNum++;
				}
			}
			window.localStorage.nextHeroBuildNum = nextNum;
			update_hero_builds();
		}
	};
}

function save_new_hero_build() {
	current_hero_build_id = make_hero_build_id(+window.localStorage.nextHeroBuildNum);
	window.localStorage.nextHeroBuildNum = +window.localStorage.nextHeroBuildNum + 1;
	save_changed_hero_build();
}

function save_changed_hero_build() {
	if (current_hero_build_id == null) // i.e. no build loaded yet (or saved)
		return;
	var hero_build = {
		name: document.getElementById("hero_build_name").value,
		options: cloneObject(env_options),
		inventory: cloneArray(inventory),
		ability_options: {}
	};
	var abis = heroes[env_options.hero].abilities;
	for (var i = 0; i < abis.length; i++) {
		var abi = abis[i];
		hero_build.ability_options[abi] = {
			level: abilities[abi].level,
			stacks: abilities[abi].stacks
		};
	}
	hero_build.ability_options;
	window.localStorage[current_hero_build_id] = JSON.stringify(hero_build);
	
	update_hero_builds();
}

function load_hero_build(build_id) {
	var hero_build = JSON.parse(window.localStorage[build_id]);
	current_hero_build_id = build_id;
	
	// name
	document.getElementById("hero_build_name").value = hero_build.name;
	
	// options
	delete hero_build.options.treat_evasion_as_phy_resist; // more like a preference option (also displayed along with the other options on the website)
	env_options = hero_build.options;
	updateAllOptions(false);
	
	// inventory
	inventory = hero_build.inventory;
	for (var i = 0; i < inventory.length; i++) {
		if (!items[inventory[i]])
			inventory[i] = "__unknown__";
	}
	
	// ability options
	for ( var id in hero_build.ability_options) {
		var opt = hero_build.ability_options[id];
		abilities[id].level = opt.level;
		abilities[id].stacks = opt.stacks;
	}
	
	update_hero_builds();
	update();
}

function delete_hero_build(build_id) {
	localStorage.removeItem(build_id);
	update_hero_builds();
}

function update_hero_builds() {
	var hero_builds = [];
	for ( var id in window.localStorage) {
		if (is_hero_build_id(id)) {
			var build = JSON.parse(window.localStorage[id]);
			build.id = id;
			hero_builds.push(build);
		}
	}
	hero_builds.sort(function(a, b) {
		if (a.options.hero == b.options.hero)
			return a.name.localeCompare(b.name);
		else
			return heroes[a.options.hero].name.localeCompare(heroes[b.options.hero].name);
	});
	var html = "";
	for (var i = 0; i < hero_builds.length; i++) {
		var build = hero_builds[i];
		html += "<tr><td><img src='" + hero_icon(build.options.hero) + "' height='20' style='vertical-align: text-top;' /> " + heroes[build.options.hero].name + "</td><td"
				+ (build.id == current_hero_build_id ? " style='font-weight: bold;'" : "") + ">" + build.name + "</td><td><button onclick='load_hero_build(\"" + build.id
				+ "\")'>Load</button></td><td><button onclick='delete_hero_build(\"" + build.id + "\")'>Delete</button></td></tr>";
	}
	if (html == "") {
		html = "<tr><td colspan='4'>You don't have any saved hero builds.</td></tr>";
	}
	document.getElementById("hero_builds").getElementsByTagName("tbody")[0].innerHTML = html;
	document.getElementById("save_changed_hero_build_button").disabled = (current_hero_build_id == null);
}
