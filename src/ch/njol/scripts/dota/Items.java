package ch.njol.scripts.dota;

import static ch.njol.scripts.dota.Attribute.*;
import static ch.njol.scripts.dota.Main.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Items {
	
	public static void execute() throws IOException {
		
		// items that should not appear, for example the combined sentry + observer wards, or event items that are not filtered elsewhere
		final Set<String> fakeItems = new HashSet<>();
		fakeItems.addAll(Arrays.asList("ward_dispenser", "cheese", "tango_single", "halloween_rapier"));
		
		final JSONObject items = new JSONObject();
		
		itemsData.forEach((String id, final Object value) -> {
			id = id.replaceFirst("^item_", "");
			if (!(value instanceof DotaData)) // version
				return;
			if (fakeItems.contains(id))
				return;
				
			final DotaData itemData = (DotaData) value;
			if (id.startsWith("recipe_")) {
				final String itemID = id.replaceFirst("^recipe_", "");
				if (fakeItems.contains(itemID))
					return;
					
				final String builtFrom[] = itemData.getString("ItemRequirements", "01").split(";item_");
				if (builtFrom.length > 1 || builtFrom[0].length() > 0) {
					builtFrom[0] = builtFrom[0].substring("item_".length());
					Arrays.sort(builtFrom);
					final JSONObject item = items.has(itemID) ? items.getJSONObject(itemID) : new JSONObject();
					BUILT_FROM.set(item, new JSONArray(builtFrom));
					items.put(itemID, item);
				}
				return;
			}
			
			final JSONObject item = items.has(id) ? items.getJSONObject(id) : new JSONObject();
			items.put(id, item);
			
			final double itemCost = itemData.getDouble(0, "ItemCost");
			if (itemCost == 0) {// event item or just crap
				items.remove(id);
				return;
			}
			COST.set(item, itemCost);
			
			final int level = Math.max(1, (int) itemData.getDouble(1, "ItemBaseLevel"));
			final int maxLevel = Math.max(1, (int) itemData.getDouble(1, "MaxUpgradeLevel"));
			LEVEL.set(item, level);
			
			NAME.set(item, Main.names.getString("DOTA_Tooltip_Ability_item_" + id) + (level == 1 && maxLevel == 1 ? "" : " " + level));
			BASE_ID.set(item, id);
			
			COOLDOWN.set(item, getLevelled(itemData.getString("AbilityCooldown"), level));
			MANA_COST.set(item, getLevelled(itemData.getString("AbilityManaCost"), level));
			
			IS_CONSUMABLE.set(item, "consumable".equals(itemData.getString("ItemQuality")));
			
			final Map<String, Double> attrs = new HashMap<>();
			final DotaData attributesData = itemData.getSection("AbilitySpecial");
			if (attributesData != null) {
				attributesData.forEach((final String _ignore, final Object attrib) -> {
					final DotaData attr = (DotaData) attrib;
					attr.forEach((final String a, final Object v) -> {
						if (a.equals("var_type"))
							return;
						attrs.put(a, getLevelled((String) v, level));
					});
				});
			}
			
			handleItem(id, items, item, attrs);
			
			attrs.keySet().removeIf((final String s) -> {
				for (final String c : new String[] { //
						"duration", "time", "delay", "cooldown", "projectile_speed", //
						"radius", "length", "distance", "blink_range", "true_sight_range", "truesight", "cast_range", "vision_range", "base_attack_range", "charge_range", "heal_on_death_range", //
						"tooltip", "bonus_night_vision", "initial_charges", "maim_chance", "blast_speed", "model_scale", "push_length", "bash_stun", "aura_bonus_threshold", "unholy_health_drain_per_tick", //
						"health_min", "speed_base", "disarm_melee", "disarm_range", "xp_multiplier", "bonus_gold", "xp_bonus", "purge_summoned_damage", "purge_rate", //
						"allied_armor", "allied_evasion", "soul_initial_charge", "soul_heal_interval", "soul_additional_charges", "max_level", "max_attacks", //
						"on_death_removal", "death_gold_reduction", "heal_on_death_base", "heal_on_death_per_charge",})
					if (s.contains(c))
						return true;
				return false;
			});
			switch (id) {
				case "ward_observer":
				case "ward_sentry":
					attrs.remove("health"); // ward health
					break;
				case "tranquil_boots":
					attrs.remove("heal_interval");
					attrs.remove("heal_amount");
					attrs.remove("break_threshold");
					attrs.remove("break_count");
					break;
				case "pipe":
					attrs.remove("barrier_block_creep");
					attrs.remove("magic_resistance_aura"); // creeps aura
					break;
				case "infused_raindrop":
					attrs.remove("min_damage");
					break;
				case "echo_sabre":
					attrs.remove("attack_speed_slow");
					assert attrs.remove("proc_damage") == 0;
					attrs.remove("movement_slow");
			}
			if (attrs.size() > 0)
				System.out.println(id + ": " + attrs);
				
		});
		
		final JSONObject unknownItem = new JSONObject();
		LEVEL.set(unknownItem, 1);
		BASE_ID.set(unknownItem, "__unknown__");
		IS_CONSUMABLE.set(unknownItem, false);
		NAME.set(unknownItem, "unknown item");
		items.put("__unknown__", unknownItem);
		
		writeEnvs(items, "items", "items.js");
		
	}
	
	private final static Map<String, Attribute> attributes = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("bonus_strength", STR);
			put("bonus_str", STR);
			put("bonus_agility", AGI);
			put("bonus_agi", AGI);
			put("bonus_intellect", INT);
			put("bonus_intelligence", INT);
			put("bonus_int", INT);
			
			put("bonus_damage", ATTACK_DAMAGE_TOTAL);
			put("crit_multiplier", CRIT__MULTIPLIER.pct());
			put("crit_chance", CRIT__CHANCE.pct());
			put("cleave_damage_percent", CLEAVE_MULT.pct());
			put("vladmir.damage_aura", ATTACK_DAMAGE_BASE_BONUS_MULT_ALL.pct());
			
			put("quelling_blade.damage_bonus", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_MELEE).pct().offset(-1));
			put("iron_talon.damage_bonus", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_MELEE).pct().offset(-1));
			put("quelling_bonus", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_MELEE).pct().offset(-1));
			put("quelling_blade.damage_bonus_ranged", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_RANGED).pct().offset(-1));
			put("iron_talon.damage_bonus_ranged", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_RANGED).pct().offset(-1));
			put("quelling_bonus_ranged", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.multipliedBy(ENEMY_IS_CREEP_AND_HERO_IS_RANGED).pct().offset(-1));
			
			put("bonus_armor", ARMOR);
			put("armor_aura", ARMOR);
			put("aura_bonus_armor", ARMOR);
			put("aura_positive_armor", ARMOR);
			put("aura_armor", ARMOR);
			// aura_armor_bonus is used by guardian greaves
			put("bonus_magical_armor", VULNERABILITY_MAG.neg().pct());
			put("bonus_spell_resist", VULNERABILITY_MAG.neg().pct());
			put("magic_resistance", VULNERABILITY_MAG.neg().pct());
			put("bonus_evasion", ENEMY_HIT_CHANCE_HERO.neg().pct());
			put("blind_pct", ENEMY_HIT_CHANCE_ALL.neg().pct());
			
			put("bonus_regen", HP_REGEN);
			put("bonus_health_regen", HP_REGEN);
			put("hp_regen", HP_REGEN);
			put("aura_health_regen", HP_REGEN);
			put("health_regen", HP_REGEN);
			// aura_health_regen_bonus is used by guardian greaves
			put("heart.health_regen_rate", HP_REGEN.multipliedBy(HP).pct());
			put("bonus_health", HP);
			put("magic_damage_block", EFF_HP_MAG);
			
			put("mana_regen_aura", MP_REGEN);
			put("aura_mana_regen", MP_REGEN);
			put("infused_raindrop.mana_regen", MP_REGEN);
			put("mana_regen", MP_REGEN_BASE_BONUS_MULT.pct());
			put("bonus_mana_regen", MP_REGEN_BASE_BONUS_MULT.pct());
			put("bonus_mana_regen_pct", MP_REGEN_BASE_BONUS_MULT.pct());
			put("bonus_mana", MP);
			
			put("bonus_movement_speed", MOVEMENT_SPEED);
			put("bonus_movement", MOVEMENT_SPEED);
			put("movement_speed", MOVEMENT_SPEED);
			put("bonus_movement_speed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("movement_speed_percent_bonus", MOVEMENT_SPEED_BONUS_MULT.pct()); // also used by bottle for the courier slow
			put("bonus_aura_movement_speed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("bonus_attack_speed", ATTACK_SPEED_HERO);
			put("quarterstaff.bonus_speed", ATTACK_SPEED_HERO);
			put("bonus_attack_speed_pct", ATTACK_SPEED_HERO);
			put("aura_attack_speed", ATTACK_SPEED_ALL); // shiva uses the same
			put("bonus_aura_attack_speed_pct", ATTACK_SPEED_ALL);
			
			put("spell_amp", SPELL_DAMAGE_BONUS_MULT.pct());
			
			put("block_damage_melee", BLOCK__AMOUNT.multipliedBy(IS_MELEE));
			put("damage_block_melee", BLOCK__AMOUNT.multipliedBy(IS_MELEE));
			put("block_damage_ranged", BLOCK__AMOUNT.multipliedBy(IS_RANGED));
			put("damage_block_ranged", BLOCK__AMOUNT.multipliedBy(IS_RANGED));
			put("block_chance", BLOCK__CHANCE.pct());
			put("block_chance_hero", BLOCK__CHANCE.multipliedBy(ENEMY_IS_HERO).pct());
			
			put("radiance.aura_damage", SPELL_DPS_MAG_AOE);
			put("orb_of_venom.poison_damage", SPELL_DPS_MAG_SINGLE);
			
			put("bonus_chance_damage", BONUS_DAMAGE_MAG__DAMAGE);
			put("bash_damage", BONUS_DAMAGE_MAG__DAMAGE);
			put("chain_damage", BONUS_DAMAGE_MAG__DAMAGE);
			put("bonus_chance", BONUS_DAMAGE_MAG__CHANCE.pct());
			put("bash_chance", BONUS_DAMAGE_MAG__CHANCE.pct());
			put("chain_chance", BONUS_DAMAGE_MAG__CHANCE.pct());
			put("bash_chance_melee", BONUS_DAMAGE_MAG__CHANCE.multipliedBy(IS_MELEE).pct());
			put("bash_chance_ranged", BONUS_DAMAGE_MAG__CHANCE.multipliedBy(IS_RANGED).pct());
			put("bash_cooldown", BONUS_DAMAGE_MAG__COOLDOWN);
			put("chain_cooldown", BONUS_DAMAGE_MAG__COOLDOWN);
			put("chain_strikes", BONUS_DAMAGE_MAG__MAX_ADDITIONAL_ENEMIES);
			
			put("shivas_guard.aura_attack_speed", ENEMY_ATTACK_SPEED);
			put("cold_attack_speed", ENEMY_ATTACK_SPEED);
			put("maim_attack_speed", ENEMY_ATTACK_SPEED);
			put("maim_slow_attack", ENEMY_ATTACK_SPEED.multipliedBy(IS_MELEE));
			put("maim_slow_attack_range", ENEMY_ATTACK_SPEED.multipliedBy(IS_RANGED));
			put("cold_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("maim_slow_movement", ENEMY_MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_MELEE).pct());
			put("maim_slow_movement_range", ENEMY_MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_RANGED).pct());
			put("poison_movement_speed_melee", ENEMY_MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_MELEE).pct());
			put("poison_movement_speed_range", ENEMY_MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_RANGED).pct());
			put("corruption_armor", ENEMY_ARMOR);
			put("aura_negative_armor", ENEMY_ARMOR);
			
			put("lifesteal_percent", LIFESTEAL.pct());
			put("vladmir.vampiric_aura", LIFESTEAL.multipliedBy(IS_MELEE).pct());
			put("vladmir.vampiric_aura_ranged", LIFESTEAL.multipliedBy(IS_RANGED).pct());
			
			put("feedback_mana_burn_illusion_melee", ILLUSIONS_BONUS_DAMAGE_PHY.multipliedBy(IS_MELEE));
			put("feedback_mana_burn_illusion_ranged", ILLUSIONS_BONUS_DAMAGE_PHY.multipliedBy(IS_RANGED));
			
			put("octarine_core.hero_lifesteal", SPELL_LIFESTEAL.multipliedBy(ENEMY_IS_HERO).pct());
			put("octarine_core.creep_lifesteal", SPELL_LIFESTEAL.multipliedBy(ENEMY_IS_CREEP).pct());
			put("octarine_core.bonus_cooldown", SPELL_COOLDOWN_MULT.neg().pct());
		}
	};
	private final static Map<String, Attribute> activeAttributes = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("unholy_bonus_strength", STR);
			
			put("heal_bonus_armor", ARMOR);
			put("bonus_aoe_armor", ARMOR);
			put("unholy_bonus_armor", ARMOR);
			
			put("unholy_bonus_damage", ATTACK_DAMAGE_TOTAL);
			put("unholy_lifesteal_percent", LIFESTEAL.pct());
			
			put("unholy_health_drain_per_second_tooltip", HP_REGEN.neg());
			put("barrier_block", EFF_HP_MAG);
			
			put("miss_chance", ENEMY_HIT_CHANCE_ALL.neg().pct());
			put("active_magical_armor", VULNERABILITY_MAG.neg().pct());
			put("extra_spell_damage_percent", VULNERABILITY_MAG.neg().pct());
			put("berserk_extra_damage", VULNERABILITY_ALL.pct());
			
			put("windwalk_movement_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("bonus_move_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("berserk_bonus_movement_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("ancient_janggo_active_movement_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("ancient_janggo_active_attack_speed", ATTACK_SPEED_ALL);
			put("berserk_bonus_attack_speed", ATTACK_SPEED_HERO);
			put("unholy_bonus_attack_speed", ATTACK_SPEED_HERO);
			//put("archer_attack_speed", ATTACK_SPEED); // also affects movement speed, so handled specially
			
			put("block_damage_melee_active", BLOCK__AMOUNT.multipliedBy(IS_MELEE));
			put("block_damage_ranged_active", BLOCK__AMOUNT.multipliedBy(IS_RANGED));
			put("block_chance_active", BLOCK__CHANCE.pct());
			
			put("static_damage", BONUS_DAMAGE_MAG_ON_ATTACKED__DAMAGE);
			put("static_chance", BONUS_DAMAGE_MAG_ON_ATTACKED__CHANCE.pct());
			put("static_cooldown", BONUS_DAMAGE_MAG_ON_ATTACKED__COOLDOWN);
			put("static_strikes", BONUS_DAMAGE_MAG_ON_ATTACKED__MAX_ADDITIONAL_ENEMIES);
			
			//put("truestrike_chance", TRUE_STRIKE_CHANCE_ALL.pct()); // removed
			put("silence_damage_percent", ORCHID_MULT.pct());
			
			put("backstab_reduction", ENEMY_DAMAGE_PHY_BASE_BONUS_MULT.pct());
			put("armor_reduction", ENEMY_ARMOR);
			put("slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.neg().pct());
			put("movespeed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("blast_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("backstab_slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("sheep_movement_speed", ENEMY_BASE_MOVEMENT_SPEED);
			put("resist_debuff", ENEMY_VULNERABILITY_MAG.neg().pct());
			
			put("images_count", ILLUSION__COUNT);
			put("images_do_damage_percent_melee", ILLUSION__DAMAGE_DEALT_MULT.multipliedBy(IS_MELEE).pct());
			put("images_do_damage_percent_ranged", ILLUSION__DAMAGE_DEALT_MULT.multipliedBy(IS_RANGED).pct());
			put("images_take_damage_percent_melee", ILLUSION__DAMAGE_TAKEN_MULT.multipliedBy(IS_MELEE).pct());
			put("images_take_damage_percent_ranged", ILLUSION__DAMAGE_TAKEN_MULT.multipliedBy(IS_RANGED).pct());
		}
	};
	
	private final static Map<String, Attribute> activeAttributesFactored = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			// TODO these 3 should probably not be divided by the whole cooldown
			put("windwalk_bonus_damage", SPELL_DPS_PHY_SINGLE.multipliedBy(HIT_CHANCE_HERO));
			put("damage", SPELL_DPS_MAG_SINGLE);
			put("blast_damage", SPELL_DPS_MAG_SINGLE);
			
			put("heal_amount", HP_REGEN);
			put("health_sacrifice", HP_REGEN.neg());
			put("replenish_health", HP_REGEN);
			put("hp_restore", HP_REGEN);
			
			put("mana_gain", MP_REGEN);
			put("replenish_amount", MP_REGEN);
			put("replenish_mana", MP_REGEN);
		}
	};
	
	private final static void handleItem(final String id, final JSONObject items, final JSONObject item, final Map<String, Double> attrs) {
		
		// "all stats" pseudo-attribute
		final double bonus_all_stats = attrs.getOrDefault("bonus_all_stats", 0.0) + attrs.getOrDefault("bonus_stats", 0.0);
		if (bonus_all_stats != 0) {
			for (final Attribute s : new Attribute[] {STR, AGI, INT})
				s.add(item, bonus_all_stats);
		}
		attrs.remove("bonus_all_stats");
		attrs.remove("bonus_stats");
		
		// special items
		switch (id) {
			case "monkey_king_bar":
				TRUE_STRIKE_CHANCE_HERO.set(item, 1.0);
				break;
			case "armlet":
				COOLDOWN.set(item, 0.7); // pseudo-cooldown
				break;
			case "wind_lace":
				attrs.remove("bonus_speed"); // WTF?
				break;
			case "phase_boots":
				attrs.remove("block_chance"); // again, WTF?
				break;
			case "ancient_janggo":
				attrs.put("ancient_janggo_active_attack_speed", attrs.remove("bonus_attack_speed_pct"));
				attrs.put("ancient_janggo_active_movement_speed", attrs.remove("bonus_movement_speed_pct"));
				break;
			case "ultimate_scepter":
				HAS_SCEPTER.set(item, 1);
				break;
			case "bottle":
				IS_CONSUMABLE.set(item, false);
				break;
			case "faerie_fire":
				IS_CONSUMABLE.set(item, true);
				break;
			case "hurricane_pike":
				attrs.remove("bonus_attack_speed"); // ignore bonus attack speed for the 4 attacks
				break;
		}
		
		final double cooldown = COOLDOWN.get(item, 0);
		
		// handle default attributes
		final Iterator<Entry<String, Double>> iter = attrs.entrySet().iterator();
		while (iter.hasNext()) {
			final Entry<String, Double> attr = iter.next();
			Attribute a = attributes.get(id + "." + attr.getKey());
			if (a == null)
				a = attributes.get(attr.getKey());
			if (a != null) {
				a.add(item, attr.getValue());
				iter.remove();
			}
		}
		
		// bashes can lifesteal, chain lightning cannot
		if (BONUS_DAMAGE_MAG.has(item))
			BONUS_DAMAGE_MAG__LIFESTEAL_MULT.add(item, id.equals("mjollnir") || id.equals("maelstrom") ? 0 : 1);
			
		if (attrs.size() > 0) {
			
			// special items
			switch (id) {
				case "power_treads": {
					final double val = attrs.remove("bonus_stat");
					final JSONObject str = cloneJO(item), agi = cloneJO(item), intel = cloneJO(item);
					STR.add(str, val);
					AGI.add(agi, val);
					INT.add(intel, val);
					NAME.add(str, " (strength)");
					NAME.add(agi, " (agility)");
					NAME.add(intel, " (intelligence)");
					items.put(id + "_str", str);
					items.put(id + "_agi", agi);
					items.put(id + "_int", intel);
					items.remove(id);
					return;
				}
				case "tranquil_boots": {
					final JSONObject broken = cloneJO(item), mixed = cloneJO(item);
					NAME.add(item, " (intact)");
					NAME.add(broken, " (broken)");
					NAME.add(mixed, " (broken half the time)");
					MOVEMENT_SPEED.set(broken, attrs.remove("broken_movement_speed"));
					HP_REGEN.set(broken, 0.0);
					MOVEMENT_SPEED.set(mixed, MOVEMENT_SPEED.get(item, 0) * 0.5 + MOVEMENT_SPEED.get(broken, 0) * 0.5);
					HP_REGEN.set(mixed, HP_REGEN.get(item, 0) * 0.5 + HP_REGEN.get(broken, 0) * 0.5);
					items.put(id + "_intact", item);
					items.put(id + "_broken", broken);
					items.put(id + "_mixed", mixed);
					items.remove(id);
					return;
				}
				case "guardian_greaves": {
					final JSONObject lowHP = cloneJO(item);
					ARMOR.add(lowHP, attrs.remove("aura_armor_bonus"));
					HP_REGEN.add(lowHP, attrs.remove("aura_health_regen_bonus"));
					NAME.add(lowHP, " (low HP)");
					items.put(id + "_low_hp", lowHP);
					break;
				}
				case "bloodstone": {
					for (final int charges : new int[] {0, 6, 12, 24}) {
						final JSONObject o = cloneJO(item);
						NAME.add(o, " (" + charges + " charges)");
						MP_REGEN.add(o, charges);
						items.put(id + "_" + charges + "_charges", o);
					}
					items.remove(id);
					return;
				}
				case "moon_shard": {
					final JSONObject consumed = cloneJO(item);
					ATTACK_SPEED_HERO.set(consumed, attrs.remove("consumed_bonus"));
					NAME.add(consumed, " (consumed)");
					items.put(id + "_consumed", consumed);
					break;
				}
				case "bottle": {
					final double factor = 3.0 / 120.0;
					NAME.add(item, " (3 uses every 2 minutes)");
					HP_REGEN.add(item, factor * attrs.remove("health_restore"));
					MP_REGEN.add(item, factor * attrs.remove("mana_restore"));
					MOVEMENT_SPEED_BONUS_MULT.reset(item); // courier slow
					return;
				}
				case "magic_stick":
				case "magic_wand": {
					final double maxCharges = attrs.remove("max_charges"), restorePerCharge = attrs.remove("restore_per_charge");
					NAME.add(item, " (used fully charged every 5 minutes)");
					final double regen = 1.0 / 300.0 * maxCharges * restorePerCharge;
					HP_REGEN.add(item, regen);
					MP_REGEN.add(item, regen);
					return;
				}
				case "urn_of_shadows": {
					final JSONObject healing = cloneJO(item), damaging = cloneJO(item);
					HP_REGEN.add(healing, attrs.remove("soul_heal_amount"));
					SPELL_DPS_PURE_SINGLE.add(damaging, attrs.remove("soul_damage_amount"));
					final JSONObject healing2 = cloneJO(healing), damaging2 = cloneJO(damaging);
					
					HP_REGEN.mult(healing, 1.0 / attrs.remove("soul_heal_duration"));
					SPELL_DPS_PURE_SINGLE.mult(damaging, 1.0 / attrs.remove("soul_damage_duration"));
					NAME.add(healing, " (healing)");
					NAME.add(damaging, " (damaging)");
					items.put(id + "_healing", healing);
					items.put(id + "_damaging", damaging);
					
					HP_REGEN.mult(healing2, 1.0 / 60.0);
					SPELL_DPS_PURE_SINGLE.mult(damaging2, 1.0 / 60.0);
					NAME.add(healing2, " (healing once per minute)");
					NAME.add(damaging2, " (damaging once per minute)");
					items.put(id + "_healing2", healing2);
					items.put(id + "_damaging2", damaging2);
					return;
				}
				case "enchanted_mango": {
					NAME.add(item, " (eaten every minute)");
					MP_REGEN.add(item, attrs.remove("replenish_amount") / 60.0);
					break;
				}
				case "echo_sabre": {
					ATTACKS_PER_SECOND.multipliedBy(IS_MELEE).add(item, 1 / cooldown);
					return;
				}
			}
			
			//special attributes
			if (attrs.containsKey("damage_per_burn")) { // diffusal blade
				BONUS_DAMAGE_PHY__DAMAGE.add(item, attrs.remove("feedback_mana_burn") * attrs.remove("damage_per_burn")); // not in percent
				BONUS_DAMAGE_PHY__CHANCE.add(item, 1);
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(item, 1);
				attrs.remove("feedback_mana_burn_ranged"); // ranged illusions
			} else if (attrs.containsKey("buff_duration")) { // tango, clarity, salve
				final double factor = 1 / attrs.remove("buff_duration");
				HP_REGEN.add(item, attrs.getOrDefault("total_heal", attrs.getOrDefault("total_health", 0.0)) * factor);
				MP_REGEN.add(item, attrs.getOrDefault("total_mana", 0.0) * factor);
				attrs.remove("total_heal");
				attrs.remove("total_health");
				attrs.remove("total_mana");
			}
			
			// if the item has a cooldown, create an active variant
			if (cooldown > 0) {
				final JSONObject active = cloneJO(item);
				final String activeID = id + "_active";
				
				MP_REGEN.add(active, -MANA_COST.get(item, 0) / cooldown);
				NAME.add(active, " (active)");
				
				// special items
				boolean keepActive = true, defaultUnused = false;
				switch (id) {
					case "phase_boots": {
						final double factor = attrs.remove("phase_duration") / cooldown;
						MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_MELEE).add(active, attrs.remove("phase_movement_speed") / 100 * factor);
						MOVEMENT_SPEED_BONUS_MULT.multipliedBy(IS_RANGED).add(active, attrs.remove("phase_movement_speed_range") / 100 * factor);
						break;
					}
					case "solar_crest": {
						ENEMY_HIT_CHANCE_HERO.reset(active);
						ARMOR.reset(active);
						break;
					}
					case "medallion_of_courage": {
						ARMOR.reset(active);
						break;
					}
					case "necronomicon":
					case "necronomicon_2":
					case "necronomicon_3": {
						final int level = (int) LEVEL.get(item, 1);
						
						{// warrior:
							final DotaData warrior = unitsData.getSection("npc_dota_necronomicon_warrior_" + level);
							SUMMON__BASE_ATTACK_TIME.index(0).add(active, warrior.getDouble("AttackRate"));
							attrs.remove("warrior_mana_feedback"); // not accurate!
							final double manaBurnWarrior = Abilities.getAbilityAttribute("necronomicon_warrior_mana_burn", "burn_amount", level);
							SUMMON__ATTACK_DAMAGE_BASIC.index(0).add(active, (warrior.getDouble("AttackDamageMin") + warrior.getDouble("AttackDamageMax")) / 2);
							SUMMON__ATTACK_DAMAGE_BASIC_BONUS.multipliedBy(ENEMY_HAS_MANA).index(0).add(active, manaBurnWarrior * Abilities.getAbilityAttribute("necronomicon_warrior_mana_burn", "burn_damage_conversion", level) / 100);
//							DPS_MAG_SINGLE.add(active, attrs.remove("explosion") / 40.0); // 1x last will damage over 40 seconds
							attrs.remove("explosion"); // ignore last will
						}
						
						{// archer:
							final DotaData archer = unitsData.getSection("npc_dota_necronomicon_archer_" + level);
							SUMMON__BASE_ATTACK_TIME.index(1).add(active, archer.getDouble("AttackRate"));
							SUMMON__ATTACK_DAMAGE_PIERCE.index(1).add(active, (archer.getDouble("AttackDamageMin") + archer.getDouble("AttackDamageMax")) / 2);
							attrs.remove("archer_mana_burn");
							final double manaBurnArcher = Abilities.getAbilityAttribute("necronomicon_warrior_mana_burn", "burn_amount", level);
							SPELL_DPS_MAG_SINGLE.multipliedBy(ENEMY_HAS_MANA).add(active, manaBurnArcher * Abilities.getAbilityAttribute("necronomicon_archer_mana_burn", "burn_as_damage_tooltip", level) / 100 / abilitiesData.getDouble("necronomicon_archer_mana_burn", "AbilityCooldown"));
							final double aura = attrs.remove("archer_attack_speed");
							MOVEMENT_SPEED_BONUS_MULT.add(active, aura / 100);
							ATTACK_SPEED_ALL.add(active, aura);
						}
						break;
					}
					case "ghost": {
						final double phyVuln = 1 - 1 / cooldown;
						VULNERABILITY_PHY.set(active, phyVuln);
						ENEMY_VULNERABILITY_PHY.set(active, phyVuln);
						break;
					}
					case "ethereal_blade": {
						final JSONObject self = active, enemy = cloneJO(active);
						final double magVuln = -attrs.remove("ethereal_damage_bonus") / 100;
						final double phyVuln = 1 - 1 / cooldown;
						VULNERABILITY_PHY.set(self, phyVuln);
						ENEMY_VULNERABILITY_PHY.set(self, phyVuln);
						VULNERABILITY_MAG.add(self, magVuln);
						NAME.add(self, " (cast on self)");
						items.put(id + "_self", self);
						VULNERABILITY_PHY.set(enemy, phyVuln);
						ENEMY_VULNERABILITY_PHY.set(enemy, phyVuln);
						ENEMY_VULNERABILITY_MAG.add(enemy, magVuln);
						ENEMY_MOVEMENT_SPEED_BONUS_MULT.set(enemy, attrs.remove("blast_movement_slow") / 100);
						SPELL_DPS_MAG_SINGLE.add(enemy, attrs.remove("blast_damage_base") / cooldown);
						SPELL_DPS_MAG_SINGLE.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).add(enemy, attrs.remove("blast_agility_multiplier") / cooldown); // primary attr, not agi (as of path 6.86 or so)
						NAME.add(enemy, " (cast on enemy)");
						items.put(id + "_enemy", enemy);
						keepActive = false;
						break;
					}
					case "faerie_fire": {
						ATTACK_DAMAGE_TOTAL.reset(active); // no damage any more if eaten
						break;
					}
					case "butterfly": {
						ENEMY_HIT_CHANCE_HERO.reset(active); // no evasion while active
						break;
					}
					case "bloodthorn": {
						TRUE_STRIKE_CHANCE_ALL.set(active, 1);
						CRIT__CHANCE.index(1).add(active, 1);
						CRIT__MULTIPLIER.index(1).add(active, attrs.remove("target_crit_multiplier") / 100);
						break;
					}
					case "iron_talon": {
						NAME.add(active, " (used on 600HP creeps)");
						SPELL_DPS_PURE_SINGLE.add(active, 600.0 * attrs.remove("creep_damage_pct") / 100.0 / cooldown);
						break;
					}
					case "helm_of_the_dominator": {
						final JSONObject wolf = active, hellbear = cloneJO(active), ogre = cloneJO(active), troll = cloneJO(active);
						NAME.add(wolf, " (passive Alpha Wolf)");
						ATTACK_DAMAGE_BASE_BONUS_MULT_ALL.add(wolf, Abilities.getAbilityAttribute("alpha_wolf_command_aura", "bonus_damage_pct", 1) / 100);
						NAME.add(hellbear, " (passive Hellbear Smasher)");
						ATTACK_SPEED_ALL.add(hellbear, Abilities.getAbilityAttribute("centaur_khan_endurance_aura", "bonus_attack_speed", 1)); // yes, this is correct >_>
						NAME.add(ogre, " (casting Ogre Frostmage)");
						ARMOR.add(ogre, Abilities.getAbilityAttribute("ogre_magi_frost_armor", "armor_bonus", 1));
						NAME.add(troll, " (passive Hill Troll Priest)");
						MP_REGEN.add(troll, Abilities.getAbilityAttribute("forest_troll_high_priest_mana_aura", "mana_regen", 1));
						items.put(id + "_wolf", wolf);
						items.put(id + "_hellbear", hellbear);
						items.put(id + "_ogre", ogre);
						items.put(id + "_troll", troll);
						keepActive = false;
						break;
					}
					case "crimson_guard": {
						BLOCK.reset(active);
						break;
					}
					default:
						keepActive = false;
						defaultUnused = true;
						break;
				}
				
				// normal attributes
				for (final Entry<String, Attribute> e : activeAttributes.entrySet()) {
					final Double v = attrs.remove(e.getKey());
					if (v != null) {
						e.getValue().add(active, v);
						keepActive = true;
					}
				}
				// normal attributes, divided by the cooldown
				for (final Entry<String, Attribute> e : activeAttributesFactored.entrySet()) {
					final Double v = attrs.remove(e.getKey());
					if (v != null) {
						e.getValue().add(active, v / cooldown);
						keepActive = true;
					}
				}
				
				if (keepActive)
					items.put(activeID, active);
				else if (defaultUnused)
					assert true;
//					System.out.println("Unused active: " + id);
			
			}
			
		}
		
	}
	
}
