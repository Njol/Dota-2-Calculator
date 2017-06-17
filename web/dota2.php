<!--
for cloudflare purge:

http://njol.ch/dota2/Env_additions.js
http://njol.ch/dota2/Env.js
http://njol.ch/dota2/items.js
http://njol.ch/dota2/heroes.js
http://njol.ch/dota2/abilities.js
http://njol.ch/dota2/FileSaver.min.js
http://njol.ch/dota2/dota2.js
http://njol.ch/dota2/dota2.css

-->
<script src="./Env_additions.js"></script>
<script src="./Env.js"></script>
<script src="./items.js"></script>
<script src="./heroes.js"></script>
<script src="./abilities.js"></script>
<script src="./FileSaver.min.js"></script>
<script src="./dota2.js"></script>
<style type="text/css">
	#page {
		padding-left: 5px;
		padding-right: 5px;
	}
	#header_content, #page, #footer_content {
		min-width: 420px;
	}
</style>
<noscript><div style="font-size: 250%; color: #b00; border: 4px solid #b00; width: 80%; padding: 10px; margin: 40px auto; text-align: center;">This calculator requires JavaScript to work.</div></noscript>
<div style="width: 100%;">
	<div style="float: right; width: 390px;">
		<div class="box right">
			<div class="title">About</div>
			Create an item build and get detailed information on which items are most effective next.<br/>
			Calculations are based on the chosen hero, levelled abilities, and items in the inventory.<br/>
			For Dota 2 version <b>6.88</b>.
		</div>
		<div class="box right">
			<div class="title">Instructions</div>
			Use the <b>+</b> on the very left of an item row to add it to the inventory.<br/>
			Items marked [upgrade] will upgrade existing items in your inventory, and are calculated accordingly with reduced effects and reduced cost.<br/>
			Press ctrl+F to find an item by its name.<br/>
			Hover over an abbreviation to learn its meaning.<br/>
		</div>
		
		<div class="box right">
			<div class="title">Please Note</div>
			Most items and abilities with cooldown have their effects averaged over the cooldown, e.g. Phase Boots are calculated as having a small constant movement speed bonus, unless their duration is long enough like Mask of Madness or Orchid Malevolence.<br/>
			Most abilities have no effect except for their mana cost, or might even have wrong effects (<a href="mailto:njol@gmx.net">send me an e-mail</a> if you would like a certain hero&apos;s abilities to be added properly).<br/>
			Most incompatible items are not checked, be careful with those! For example, unique attack modifiers, incompatible buffs and auras, and multiple Maelstroms/Mjollnirs are handled as if they would all work together and stack additively.<br/>
			A few items and abilities are approximated. For example, Echo Sabre's second attack is calculated as an additional instantaneous attack together with a maximum attack speed reduction, Bounty Hunter's Jinada is calculated differently from other crits and e.g. does not increase cleave damage, and Batrider's Sticky Napalm currently only increases attack damage.<br/>
			<br/>
			Damage block, illusions, and creeps are not implemented (yet), except for Necronomicon.<br/>
			May still contain some bugs.
		</div>
	</div>
	<div class="box right" style="float: left; width: 390px;">
		<div class="title">Hero: <select id="option_hero" onchange="update()"></select></div>
		<table id="env">
			<tbody style="text-align: right">
				<tr><th rowspan="6"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 100% 100% 0px;">Settings</div></th>
					<th>Level</th><td><input id="option_level" type="number" step="1" min="1" max="25" value="1" onchange="update()" style="width: 50px" /></td></tr>
					<tr><th>Enemy armor / evasion</th><td><input id="option_enemy_armor" type="number" step="any" value="5" onchange="update()" style="width: 50px" /> / <input id="option_hit_chance_all*%" type="number" min="0" max="100" step="5" value="0" onchange="update()" style="width: 50px" /> %</td></tr>
					<tr><th>Enemy magic resistance</th><td><input id="option_enemy_vulnerability_mag*%" type="number" step="5" min="0" max="100" value="25" onchange="update()" style="width: 50px" /> %<!-- / <input id="option_enemy_spell_immune" type="checkbox" onchange="update()" /> Immune</td>--></tr>
					<tr><th title="Used for effects like Quelling Blade and crits. Also sets the enemy's armor type (for example, buildings take half damage from heroes)">Enemy is a</th><td id="enemy_radio_td">
						<label><input name="enemy_radio" id="option_enemy_is_hero" type="radio" onchange="update()" checked="checked"/>hero</label><!--
						--><label><input name="enemy_radio" id="option_enemy_is_creep" type="radio" onchange="update()"/>creep</label><!--
						--><label><input name="enemy_radio" id="option_enemy_is_building" type="radio" onchange="update()"/>building</label>
					</td></tr>
					<tr><th title="Used for AOE-effects like Radiance, Mjollnir and cleave">Additional enemies in the area</th><td><input id="option_num_additional_enemies" type="number" step="1" min="0" max="1000" value="0" onchange="update()" style="width: 50px" /></td></tr>
					<tr><th title="Used for effects like Axe's Counterhelix and the active of Mjollnir">Incoming attacks per second</th><td><input id="option_enemy_attacks_per_second" type="number" step="0.5" min="0" max="1000" value="1" onchange="update()" style="width: 50px" /></td></tr>
				<tr><th rowspan="3"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 80% 80% 0px;">Attribs</div></th>
					<th>Strength</th><td id="env_str"></td></tr>
					<tr><th>Agility</th><td id="env_agi"></td></tr>
					<tr><th>Intelligence</th><td id="env_int"></td></tr>
				<tr><th rowspan="4"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 80% 80% 0px;">HP/MP</div></th>
					<th title="Health">HP</th><td id="env_hp"></td></tr>
					<tr><th title="Health regeneration per second">HP regen</th><td id="env_hp_regen"></td></tr>
					<tr><th title="Mana">MP</th><td id="env_mp"></td></tr>
					<tr><th title="Mana regeneration per second">MP regen</th><td id="env_mp_regen"></td></tr>
				<tr><th rowspan="5"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 90% 90% 0px;">Defense</div></th>
					<th>Armor / Evasion</th><td><span id="env_armor"></span> / <span id="env_enemy_hit_chance_hero*%"></span>%</td></tr>
					<tr><th>Physical resistance</th><td><span id="env_vulnerability_phy*%"></span>%</td></tr>
					<tr><th title="Effective Health vs. physical attacks">EHP vs. physical</th><td id="env_eff_hp_phy"></td></tr>
					<tr><th>Magical resistance</th><td><span id="env_vulnerability_mag*%"></span>%</td></tr>
					<tr><th title="Effective Health vs. magical attacks">EHP vs. magical</th><td id="env_eff_hp_mag"></td></tr>
				<tr><th rowspan="2"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 60% 60% 0px;">Misc</div></th>
					<th>Movement speed</th><td id="env_movement_speed"></td></tr>
					<tr><th>Spell damage amplification</th><td><span id="env_spell_damage_bonus_mult%"></span>%</td></tr>
				<tr><th rowspan="7"><div style="transform: rotate(-90deg); width: 1.5em; transform-origin: 100% 100% 0px;">Damage</div></th>
					<th>Displayed damage</th><td><span id="displayed_attack_damage"></span></td></tr>
					<tr><th>Attack speed</th><td><span id="env_attack_speed_hero"></span> (<span id="env_attacks_per_second"></span> attacks/s)</td></tr>
					<tr><th>Enemy physical resistance</th><td><span id="env_enemy_vulnerability_phy*%"></span>%</td></tr>
					<tr><th title="Physical damage per second">Physical DPS</th><td id="env_dps_phy_total_after_reductions"></td></tr>
					<tr><th title="Magical damage per second">Magical DPS</th><td id="env_dps_mag_total_after_reductions"></td></tr>
					<tr><th title="Pure damage per second">Pure DPS</th><td id="env_dps_pure_total_after_reductions"></td></tr>
					<tr><th title="Total damage per second">Total DPS</th><td id="env_dps_total_after_reductions"></td></tr>
			</tbody>
		</table>
	</div>
	<div style="float: left; width: 400px; text-align: center;">
		<div class="box right">
			<div class="title">Abilities</div>
			<table id="abilities" style="display: inline-block;">
				<thead style="text-align: center;"><th></th><th>Level</th><th colspan="2">Options</th></thead>
				<tbody style="text-align: left;"></tbody>
			</table>
		</div>
		
		<div class="box right">
			<div class="title">Inventory</div>
			<table id="inventory" style="display: inline-block;">
				<tbody style="text-align: left;"></tbody>
			</table>
			<br/>
			Total Cost: <span id="env_cost"></span>
		</div>
		
		<div class="box right">
			<div class="title">Reset</div>
			<button onclick="reset_hero_build()" class="btn btn-default btn-xs">Reset current hero build</button>
			<button onclick="reset_options()" class="btn btn-default btn-xs">Reset options</button>
		</div>
		
		<div class="box right">
			<div class="title">Hero Builds</div>
			<span>Current hero build name:</span> <input type="text" value="1" id="hero_build_name" /><br/>
			<button onclick="save_changed_hero_build()" id="save_changed_hero_build_button" class="btn btn-default btn-xs">Save changed hero build</button> <button onclick="save_new_hero_build()" class="btn btn-default btn-xs">Save as new hero build</button>
			<div style="margin: 2px 0px;">
				<table id="hero_builds" style="display: inline-block; max-height: 300px; overflow-y: auto;">
					<thead style="text-align: center;"><th>Hero</th><th>Build Name</th><th></th><th></th></thead>
					<tbody style="text-align: left;"></tbody>
				</table>
			</div>
			<button onclick="export_hero_builds()" class="btn btn-default btn-xs">Save all hero builds to file</button> <button onclick="document.getElementById('hero_build_import_file').dispatchEvent(new MouseEvent('click'));" class="btn btn-default btn-xs">Restore hero builds from file</button><input type="file" id="hero_build_import_file" style="visibility: hidden; position: absolute;" accept=".json" onchange="import_hero_builds()"/>
			<div style="font-size: 85%; text-align: center; margin: 3px;">Note: Hero builds are saved to local storage,<br/>which should save them persistently on a single computer.<br/>You can use these buttons to save to a different location<br/>or to create a manual backup though.</div>
		</div>
	</div>
</div>
<div id="most_options" style="clear: both; margin-bottom: 10px;">
	<div class="box right">
		<div class="title">Options</div>
		<div class="dota2_small_section">
			<label><input id="option_show_consumables" type="checkbox" checked="checked" onchange="update()"/>show consumable items</label><!--
			--><label><input id="option_treat_evasion_as_phy_resist" type="checkbox" checked="checked" onchange="update()"/>treat evasion as physical resistance</label><!--
			--><label><input id="option_show_images" type="checkbox" checked="checked" onchange="update()"/>show item icons (may cause some lag)</label>
		</div>
	</div>
	<div class="box right">
		<div class="title">Visible columns</div>
		<div class="dota2_small_section" id="items_columns"></div>
	</div>
	<div class="box right">
		<div class="title">Values for the 'combined' column</div>
		<div style="text-align: center; padding: 3px;">(in gold per unit, e.g. 1 damage per second is worth 100 gold)</div>
		<div class="dota2_small_section" id="items_combined"></div>
	</div>
</div>
<div class="box right">
	<div class="title">Item Table</div>
	<div style="float: left;"><div>
		<table id="dota2_items_header_topleft">
			<thead></thead>
		</table>
	</div><div style="overflow-y: hidden; max-height: 780px; margin-bottom: 20px;">
		<table id="dota2_items_header_left">
			<tbody></tbody>
		</table>
	</div></div><div style="overflow-x: hidden; margin-right: 20px;">
		<table id="dota2_items_header_top">
			<thead></thead>
		</table>
	</div><div style="overflow: auto; max-height: 800px;" onscroll="this.previousSibling.previousSibling.children[1].scrollTop = this.scrollTop; this.previousSibling.scrollLeft = this.scrollLeft;">
		<table id="dota2_items_body" style="text-align: right;">
			<tbody></tbody>
		</table>
	</div><div style="clear: both;"></div>
</div>