"use strict";

function zip2(name_a, a, name_b, b) {
	var r = [];
	for (var i = 0; i < Math.min(a.length, b.length); i++) {
		var z = {};
		z[name_a] = a;
		z[name_b] = b;
		r.push(z);
	}
	return r;
}

function crits(cs) {
	//	var cs = cloneArray(cs);
	
	cs.sort(function(a, b) { // sort by crit damage descending
		return a.multiplier - b.multiplier;
	});
	var chance_left = 1, total_mult = 1;
	for (var i = 0; i < cs.length; i++) {
		var c = cs[i];
		var chance = Math.max(0, Math.min(1, c.chance || 1));
		total_mult += (c.multiplier - 1) * chance * chance_left;
		chance_left *= (1 - chance);
	}
	return total_mult;
}

function bonus_damage(env, list, lifesteal, num_additional_enemies, attacks_per_second, hit_chance_hero) {
	var result = {
		single: 0,
		rest: 0
	};
	for (var i = 0; i < list.length; i++) {
		var bd = list[i];
		
		var damage = bd.damage || 0;
		var chance = Math.max(0, Math.min(1, bd.chance || 1));
		var cooldown = bd.cooldown || 0;
		var lifesteal_mult = bd.lifesteal_mult || 0;
		var max_additional_enemies = bd.max_additional_enemies || 0;
		
		var dps = damage / (cooldown + 1 / (attacks_per_second * hit_chance_hero * chance));
		env.hp_regen += dps * lifesteal_mult * lifesteal;
		result.single += dps;
		result.rest += dps * Math.min(num_additional_enemies, max_additional_enemies);
	}
	return result;
}

function summons(list, attack_speed_all, attack_damage_base_bonus_mult_all, hit_chance_all, enemy_vulnerability_phy, enemy_vulnerability_phy_basic, enemy_vulnerability_phy_hero,
		enemy_vulnerability_phy_pierce) {
	var result = 0;
	for (var i = 0; i < list.length; i++) {
		var summon = list[i];
		
		var damage_basic = summon.attack_damage_basic || 0;
		var damage_basic_bonus = summon.attack_damage_basic_bonus || 0;
		var damage_hero = summon.attack_damage_hero || 0;
		var damage_pierce = summon.attack_damage_pierce || 0;
		var base_attack_time = summon.base_attack_time || 1;
		
		result += (1 + attack_speed_all / 100.0)
				/ base_attack_time
				* hit_chance_all
				* enemy_vulnerability_phy
				* (1 + attack_damage_base_bonus_mult_all)
				* ((damage_basic + damage_basic_bonus / (1 + attack_damage_base_bonus_mult_all)) * enemy_vulnerability_phy_basic + damage_hero * enemy_vulnerability_phy_hero + damage_pierce
						* enemy_vulnerability_phy_pierce);
	}
	return result;
}

function addArrayToArray(a, other) {
	var r = [];
	for (var i = 0; i < Math.max(a.length, other.length); i++) {
		r[i] = (a[i] || 0) + (other[i] || 0);
	}
	return a;
}

function multArray(a, mult) {
	var r = [];
	for (var i = 0; i < a.length; i++) {
		r[i] = a[i] * mult;
	}
	return a;
}

function cloneArray(a) {
	var r = [];
	for (var i = 0; i < a.length; i++) {
		r[i] = cloneObject(a[i]);
	}
	return r;
}
function cloneObject(obj) {
	if (obj == null || typeof (obj) != "object")
		return obj;
	var copy = obj.constructor();
	for ( var attr in obj) {
		if (obj.hasOwnProperty(attr))
			copy[attr] = cloneObject(obj[attr]);
	}
	return copy;
}

function addObjects(a, b) {
	if (a == null)
		return b;
	if (b == null)
		return a;
	if (typeof (a) != "object")
		return (a || 0) + (b || 0);
	var copy = a.constructor();
	for ( var attr in a) {
		if (a.hasOwnProperty(attr))
			copy[attr] = addObjects(a[attr], b[attr]);
	}
	for ( var attr in b) {
		if (b.hasOwnProperty(attr) && !a.hasOwnProperty(attr))
			copy[attr] = addObjects(a[attr], b[attr]);
	}
	return copy;
}
