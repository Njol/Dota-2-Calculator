package ch.njol.scripts.dota;

import static ch.njol.scripts.dota.Attribute.*;
import static ch.njol.scripts.dota.Main.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.njol.scripts.dota.Attribute.Constants;

public class Abilities {
	
	private final static JSONObject baseAbility = new JSONObject();
	
	private final static Map<String, Map<String, String>> abilityAttributes = new HashMap<>();
	
	public final static double getAbilityAttribute(final String ability, final String attibute, final int level) {
		return getLevelled(abilityAttributes.get(ability).get(attibute), level);
	}
	
	public final static void execute() throws IOException {
		
		final JSONObject abilities = new JSONObject();
		
		abilitiesData.forEach((final String id, final Object value) -> {
			if (!(value instanceof DotaData)) // version
				return;
			final DotaData abilityData = (DotaData) value;
			final JSONObject ability = id.equals("ability_base") ? baseAbility : cloneJO(baseAbility);
			
			NAME.set(ability, names.getString("DOTA_Tooltip_ability_" + id));
			if (id.equals("invoker_attribute_bonus"))
				NAME.set(ability, names.getString("DOTA_Tooltip_ability_attribute_bonus"));
				
			final boolean isUlt = "DOTA_ABILITY_TYPE_ULTIMATE".equals(abilityData.getString("AbilityType"));
			IS_ULTIMATE.set(ability, isUlt ? 1 : 0);
			int maxLevel = (int) abilityData.getDouble(isUlt ? 3 : 4, "MaxLevel");
			if (id.equals("attribute_bonus"))
				maxLevel = 25 - 4 - 4 - 4 - 3;
			LEVEL_MAX.set(ability, maxLevel);
			LEVEL.set(ability, 0);
			
			abilities.put(id, ability);
			LEVELS.set(ability, new JSONArray().put(new JSONObject())); // first element is empty (ability not levelled, no effects)
			
			double maxStacks = 0;
			if (!id.equals("ability_base")) {
				for (int level = 1; level <= maxLevel; level++) {
					final int level_ = level;
					final JSONObject levelData = new JSONObject();
					
					final double cooldown = getLevelled(abilityData.getString("AbilityCooldown"), level), manacost = getLevelled(abilityData.getString("AbilityManaCost"), level);
					COOLDOWN.set(levelData, cooldown);
					MANA_COST.set(levelData, manacost);
					
					if (abilityData.getString("AbilityBehavior").contains("DOTA_ABILITY_BEHAVIOR_ATTACK") && cooldown == 0) {
						MP_REGEN_PER_ATTACK.add(levelData, -manacost);
					}
					
					final Map<String, Double> attrs = new HashMap<>();
					final Map<String, String> attrs2 = new HashMap<>();
					final DotaData attributesData = abilityData.getSection("AbilitySpecial");
					if (attributesData != null) {
						attributesData.forEach((final String _ignore, final Object attrib) -> {
							final DotaData attr = (DotaData) attrib;
							attr.forEach((final String a, final Object v) -> {
								if (a.equals("var_type") || a.equals("levelkey"))
									return;
								attrs2.put(a, (String) v);
								attrs.put(a, getLevelled((String) v, level_));
							});
						});
					}
					abilityAttributes.put(id, new HashMap<>(attrs2));
					
					final double damage = getLevelled(abilityData.getString("AbilityDamage"), level);
					final double duration = getLevelled(abilityData.getString("AbilityDuration"), level);
					
					handleAbility(id, abilities, ability, levelData, attrs, damage, duration, level, cooldown, manacost);
					
					// far too many to do all
					//			if (attrs.size() > 0)
					//				System.out.println(id + ": " + attrs);
					
					LEVELS.addToList(ability, levelData);
					
					maxStacks = Math.max(maxStacks, STACKS_MAX.get(levelData, 0));
				}
			}
			if (maxStacks > 0)
				STACKS_MAX.set(ability, maxStacks);
		});
		
		final JSONObject silencerStolenInt = new JSONObject();
		INT.multipliedBy(STACKS).set(silencerStolenInt, 2);
		STACKS_MAX.set(silencerStolenInt, 1000);
		LEVEL.set(silencerStolenInt, 0);
		LEVEL_MAX.set(silencerStolenInt, 0);
		LEVELS.addToList(silencerStolenInt, new JSONObject());
		IS_ULTIMATE.set(silencerStolenInt, 0);
		NAME.set(silencerStolenInt, "Stolen Intelligence");
		abilities.put("silencer_stolen_intelligence", silencerStolenInt);
		
		writeEnvs(abilities, "abilities", "abilities.js");
		
	}
	
	private final static Map<String, Attribute> abilityDamage = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("juggernaut_blade_fury", SPELL_DPS_MAG_AOE);
			put("pudge_rot", SPELL_DPS_MAG_AOE);
			put("sandking_sand_storm", SPELL_DPS_MAG_AOE);
			put("sniper_headshot", BONUS_DAMAGE_PHY__DAMAGE);
			put("warlock_shadow_word", HP_REGEN);
			put("tiny_craggy_exterior", BONUS_DAMAGE_MAG_ON_ATTACKED__DAMAGE);
		}
	};
	
	private final static Map<String, Attribute> attributes = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("marksmanship_agility_bonus", AGI);
			put("invoker_quas.bonus_strength", STR);
			put("invoker_wex.bonus_agility", AGI);
			put("invoker_exort.bonus_intelligence", INT);
			
			put("damage_bonus", ATTACK_DAMAGE_TOTAL);
			put("bonus_damage", ATTACK_DAMAGE_TOTAL); // also bash(es), and several one-time-only bonus damages 8e.g. invisibility)
			put("hero_bonus_damage", ATTACK_DAMAGE_TOTAL);
			put("spectre_desolate.bonus_damage", BONUS_DAMAGE_PURE__DAMAGE);
			put("ancient_apparition_chilling_touch.bonus_damage", BONUS_DAMAGE_MAG__DAMAGE);
			
			put("trueshot_ranged_damage", ATTACK_DAMAGE_TOTAL.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).pct());
			put("gods_strength_damage", ATTACK_DAMAGE_TOTAL.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).pct());
			put("magnataur_empower.bonus_damage_pct", ATTACK_DAMAGE_TOTAL.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).pct());
			
			put("alpha_wolf_command_aura.bonus_damage_pct", ATTACK_DAMAGE_BASE_BONUS_MULT_ALL.pct());
			put("vengefulspirit_command_aura.bonus_damage_pct", ATTACK_DAMAGE_BASE_BONUS_MULT_ALL.pct());
			put("magnataur_empower.bonus_damage_pct", ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.pct());
			put("lycan_feral_impulse.bonus_damage", ATTACK_DAMAGE_BASE_BONUS_MULT_ALL.pct());
			
			put("damage_per_second", SPELL_DPS_MAG_AOE);
			put("viper_corrosive_skin.damage", SPELL_DPS_MAG_AOE);
			put("alchemist_acid_spray.damage", SPELL_DPS_PHY_AOE);
			
			put("venomancer_poison_sting.damage", SPELL_DPS_MAG_SINGLE);
			put("viper_poison_attack.damage", SPELL_DPS_MAG_SINGLE);
			put("corrosive_breath_damage", SPELL_DPS_MAG_SINGLE);
			
			put("riki_permanent_invisibility.damage_multiplier", BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(PRIMARY_ATTRIBUTE_VALUE));
			put("silencer_glaives_of_wisdom.intellect_damage_pct", BONUS_DAMAGE_PURE__DAMAGE.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).pct());
			put("obsidian_destroyer_arcane_orb.mana_pool_damage_pct", BONUS_DAMAGE_PURE__DAMAGE.multipliedBy(MP).pct());
			put("sniper_headshot.proc_chance", BONUS_DAMAGE_PHY__CHANCE.pct());
			
			put("tiny_craggy_exterior.stun_chance", BONUS_DAMAGE_MAG_ON_ATTACKED__CHANCE.pct());
			put("tiny_craggy_exterior.stun_duration", BONUS_DAMAGE_MAG_ON_ATTACKED__COOLDOWN);
			put("troll_warlord_berserkers_rage.bash_chance", BONUS_DAMAGE_MAG_ON_ATTACKED__CHANCE.pct());
			put("troll_warlord_berserkers_rage.bash_damage", BONUS_DAMAGE_MAG_ON_ATTACKED__DAMAGE);
			put("centaur_return.return_damage", BONUS_DAMAGE_PHY_ON_ATTACKED__DAMAGE);
			put("centaur_return.strength_pct", BONUS_DAMAGE_PHY_ON_ATTACKED__DAMAGE.multipliedBy(PRIMARY_ATTRIBUTE_VALUE).pct());
			
			put("great_cleave_damage", CLEAVE_MULT.pct());
			put("magnataur_empower.cleave_damage_pct", CLEAVE_MULT.pct());
			
			put("crit_chance", CRIT__CHANCE.pct());
			put("blade_dance_crit_chance", CRIT__CHANCE.pct());
			put("crit_mult", CRIT__MULTIPLIER.pct());
			put("crit_multiplier", CRIT__MULTIPLIER.pct());
			put("chaos_knight_chaos_strike.crit_damage", CRIT__MULTIPLIER.pct());
			put("crit_bonus", CRIT__MULTIPLIER.pct());
			put("blade_dance_crit_mult", CRIT__MULTIPLIER.pct());
			
			put("base_attack_time", BASE_ATTACK_TIME);
			
			put("mirana_leap.leap_speedbonus_as", ATTACK_SPEED_ALL);
			put("tiny_grow.bonus_attack_speed", ATTACK_SPEED_HERO);
			put("windrunner_focusfire.bonus_attack_speed", ATTACK_SPEED_HERO);
			put("beastmaster_inner_beast.bonus_attack_speed", ATTACK_SPEED_ALL);
			put("phantom_assassin_phantom_strike.bonus_attack_speed", ATTACK_SPEED_HERO);
			put("life_stealer_rage.attack_speed_bonus", ATTACK_SPEED_HERO);
			put("clinkz_strafe.attack_speed_bonus_pct", ATTACK_SPEED_HERO);
			put("night_stalker_hunter_in_the_night.bonus_attack_speed_night", ATTACK_SPEED_HERO);
			put("invoker_alacrity.bonus_attack_speed", ATTACK_SPEED_HERO);
			put("lycan_feral_impulse.bonus_attack_speed", ATTACK_SPEED_ALL);
			put("lone_druid_rabid.bonus_attack_speed", ATTACK_SPEED_ALL);
			put("ogre_magi_bloodlust.bonus_attack_speed", ATTACK_SPEED_ALL);
			put("visage_grave_chill.attackspeed_bonus", ATTACK_SPEED_HERO);
			put("troll_warlord_battle_trance.attack_speed", ATTACK_SPEED_HERO);
			put("abaddon_frostmourne.attack_speed", ATTACK_SPEED_ALL);
			put("legion_commander_press_the_attack.attack_speed", ATTACK_SPEED_HERO);
			put("ursa_overpower.attack_speed_bonus_pct", ATTACK_SPEED_HERO);
			
			put("tiny_grow.bonus_movement_speed", MOVEMENT_SPEED);
			put("alchemist_chemical_rage.bonus_movespeed", MOVEMENT_SPEED);
			put("dragon_knight_elder_dragon_form.bonus_movement_speed", MOVEMENT_SPEED);
			put("lone_druid_true_form.speed_loss", MOVEMENT_SPEED.neg());
			put("troll_warlord_berserkers_rage.bonus_move_speed", MOVEMENT_SPEED);
			put("terrorblade_metamorphosis.speed_loss", MOVEMENT_SPEED.neg());
			
			put("axe_battle_hunger.speed_bonus", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("axe_culling_blade.speed_bonus", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("kunkka_ghostship.movespeed_bonus", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("mirana_leap.leap_speedbonus", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("warcry_movespeed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("slardar_sprint.bonus_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("windrunner_windrun.movespeed_bonus_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("clinkz_wind_walk.move_speed_bonus_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("night_stalker_hunter_in_the_night.bonus_movement_speed_pct_night", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("broodmother_spin_web.bonus_movespeed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("bounty_hunter_track.bonus_move_speed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("doom_bringer_scorched_earth.bonus_movement_speed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("spirit_breaker_empowering_haste.bonus_movespeed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("lone_druid_rabid.bonus_move_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("ogre_magi_bloodlust.bonus_movement_speed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("visage_grave_chill.movespeed_bonus", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("wisp_tether.movespeed", MOVEMENT_SPEED_BONUS_MULT.pct());
			put("abaddon_frostmourne.move_speed_pct", MOVEMENT_SPEED_BONUS_MULT.pct());
			
			put("vampiric_aura", LIFESTEAL.pct());
			put("lifesteal_pct", LIFESTEAL.pct());
			
			put("lone_druid_true_form.bonus_hp", HP);
			put("troll_warlord_berserkers_rage.bonus_hp", HP);
			put("ember_spirit_flame_guard.absorb_amount", EFF_HP_MAG);
			
			put("bonus_health_regen", HP_REGEN);
			put("witch_doctor_voodoo_restoration.heal", HP_REGEN);
			put("huskar_inner_vitality.heal", HP_REGEN);
			put("broodmother_spin_web.heath_regen", HP_REGEN);
			put("legion_commander_press_the_attack.hp_regen", HP_REGEN);
			put("huskar_inner_vitality.attrib_bonus", HP_REGEN.multipliedBy(PRIMARY_ATTRIBUTE_VALUE));
			put("healing_ward_heal_amount", HP_REGEN.multipliedBy(HP).pct());
			put("wisp_overcharge.drain_pct", HP_REGEN.multipliedBy(HP).neg());
			put("slark_shadow_dance.bonus_regen_pct", HP_REGEN.multipliedBy(HP).pct());
			put("huskar_burning_spear.health_cost", HP_REGEN_PER_ATTACK.neg());
			
			put("lion_mana_drain.mana_per_second", MP_REGEN);
			put("witch_doctor_voodoo_restoration.mana_per_second", MP_REGEN.neg());
			put("mana_regen_self", MP_REGEN);
			put("bonus_mana_regen", MP_REGEN_BASE);
			put("wisp_overcharge.drain_pct_tooltip", MP_REGEN.multipliedBy(MP).pct().neg());
			
			put("tidehunter_kraken_shell.damage_reduction", BLOCK__AMOUNT);
			
			put("bonus_armor", ARMOR);
			put("armor_bonus", ARMOR);
			put("warcry_armor", ARMOR);
			
			put("spell_shield_resistance", VULNERABILITY_MAG.pct().neg());
			put("flesh_heap_magic_resist", VULNERABILITY_MAG.pct().neg());
			put("bonus_magic_resistance", VULNERABILITY_MAG.pct().neg());
			put("rubick_null_field.magic_damage_reduction_pct", VULNERABILITY_MAG.pct().neg());
			put("slardar_sprint.bonus_damage", VULNERABILITY_ALL.pct());
			put("spectre_dispersion.damage_reflection_pct", VULNERABILITY_ALL.pct().neg());
			put("wisp_overcharge.bonus_damage_pct", VULNERABILITY_ALL.pct());
			put("bristleback_bristleback.back_damage_reduction", VULNERABILITY_ALL.pct().neg());
			
			put("witch_doctor_maledict.bonus_damage", ORCHID_MULT.pct());
			
			put("chaos_knight_phantasm.images_count", ILLUSION__COUNT);
			put("chaos_knight_phantasm.extra_phantasm_chance_pct_tooltip", ILLUSION__COUNT.pct());
			put("chaos_knight_phantasm.outgoing_damage", ILLUSION__DAMAGE_DEALT_MULT.pct());
			put("chaos_knight_phantasm.incoming_damage", ILLUSION__DAMAGE_TAKEN_MULT.pct());
			put("naga_siren_mirror_image.images_count", ILLUSION__COUNT);
			put("naga_siren_mirror_image.outgoing_damage", ILLUSION__DAMAGE_DEALT_MULT.pct());
			put("naga_siren_mirror_image.incoming_damage", ILLUSION__DAMAGE_TAKEN_MULT.pct());
			put("terrorblade_conjure_image.illusion_outgoing_damage", ILLUSION__DAMAGE_DEALT_MULT.pct());
			put("terrorblade_conjure_image.illusion_incoming_damage", ILLUSION__DAMAGE_TAKEN_MULT.pct());
			
			put("tidehunter_anchor_smash.damage_reduction", ENEMY_DAMAGE_PHY_BASE_BONUS_MULT.pct());
//			put("enfeeble_attack_reduction", ENEMY_DAMAGE_PHY);
			
			put("viper_poison_attack.bonus_attack_speed", ENEMY_ATTACK_SPEED);
			put("viper_corrosive_skin.bonus_attack_speed", ENEMY_ATTACK_SPEED);
			put("viper_viper_strike.bonus_attack_speed", ENEMY_ATTACK_SPEED);
			put("crush_attack_slow_tooltip", ENEMY_ATTACK_SPEED);
			put("crystal_maiden_crystal_nova.attackspeed_slow", ENEMY_ATTACK_SPEED);
			put("lich_frost_nova.slow_attack_speed", ENEMY_ATTACK_SPEED);
			put("lich_frost_armor.slow_attack_speed", ENEMY_ATTACK_SPEED);
			put("lich_chain_frost.slow_attack_speed", ENEMY_ATTACK_SPEED);
			// stopped, not even displayed
			
			put("lion_voodoo.movespeed", ENEMY_BASE_MOVEMENT_SPEED);
			put("shadow_shaman_voodoo.movespeed", ENEMY_BASE_MOVEMENT_SPEED);
			
			put("enemy_movespeed_bonus_pct", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("kunkka_torrent.movespeed_bonus", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("frost_arrows_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("rot_slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("requiem_reduction_ms", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("death_prophet_spirit_siphon.movement_slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct().neg());// positive
			put("crush_extra_slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("tidehunter_gush.movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("crystal_maiden_crystal_nova.movespeed_slow", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("lich_frost_nova.slow_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("lich_frost_armor.slow_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("lich_chain_frost.slow_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			put("riki_smoke_screen.movement_speed_reduction", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct().neg());// positive
			put("sniper_shrapnel.slow_movement_speed", ENEMY_MOVEMENT_SPEED_BONUS_MULT.pct());
			// stopped, not even displayed
			
			put("miss_chance", ENEMY_HIT_CHANCE_ALL.pct().neg());
			put("miss_rate", ENEMY_HIT_CHANCE_ALL.pct().neg());
			put("bonus_evasion", ENEMY_HIT_CHANCE_HERO.pct().neg());
			put("brewmaster_drunken_brawler.dodge_chance", ENEMY_HIT_CHANCE_HERO.pct().neg());
			put("troll_warlord_whirling_axes_melee.blind_pct", ENEMY_HIT_CHANCE_ALL.pct().neg());
			
			put("pugna_decrepify.bonus_spell_damage_pct", ENEMY_VULNERABILITY_MAG.pct().neg());// negative
			put("ancient_apparition_ice_vortex.spell_resist_pct", ENEMY_VULNERABILITY_MAG.pct().neg());// negative
			put("skywrath_mage_ancient_seal.resist_debuff", ENEMY_VULNERABILITY_MAG.pct().neg());// negative
			put("elder_titan_natural_order.magic_resistance_pct", ENEMY_VULNERABILITY_MAG.pct());// positive
			put("chen_penitence.bonus_damage_taken", ENEMY_VULNERABILITY_ALL.pct());
			put("shadow_demon_soul_catcher.bonus_damage_taken", ENEMY_VULNERABILITY_ALL.pct());
			
			put("armor_reduction", ENEMY_ARMOR.neg());
			put("vengefulspirit_wave_of_terror.armor_reduction", ENEMY_ARMOR);
			put("templar_assassin_meld.bonus_armor", ENEMY_ARMOR);
			put("presence_armor_reduction", ENEMY_ARMOR);
			put("tidehunter_gush.armor_bonus", ENEMY_ARMOR);
			put("elder_titan_natural_order.armor_reduction_pct", ENEMY_ARMOR_MULT.pct().neg());
			
			put("fiery_soul_max_stacks", STACKS_MAX);
			put("necromastery_max_souls", STACKS_MAX);
			put("razor_static_link.drain_length", STACKS_MAX);
			put("visage_gravekeepers_cloak.max_layers", STACKS_MAX);
			put("troll_warlord_fervor.max_stacks", STACKS_MAX);
			put("shredder_reactive_armor.stack_limit", STACKS_MAX);
			put("bristleback_viscous_nasal_goo.stack_limit", STACKS_MAX);
			put("bristleback_warpath.max_stacks", STACKS_MAX);
			
			// per stack
			put("fiery_soul_attack_speed_bonus", ATTACK_SPEED_HERO.multipliedBy(STACKS));
			put("fiery_soul_move_speed_bonus", MOVEMENT_SPEED_BONUS_MULT.multipliedBy(STACKS).pct());
			put("necromastery_damage_per_soul", ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS));
			put("flesh_heap_strength_buff_amount", STR.multipliedBy(STACKS));
			put("razor_static_link.drain_rate", ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS));
			put("razor_eye_of_the_storm.armor_reduction", ENEMY_ARMOR.multipliedBy(STACKS).neg());
			put("necrolyte_sadist.mana_regen", MP_REGEN.multipliedBy(STACKS));
			put("necrolyte_sadist.health_regen", HP_REGEN.multipliedBy(STACKS));
			put("ursa_fury_swipes.damage_per_stack", BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(STACKS));
			put("visage_gravekeepers_cloak.bonus_armor", ARMOR.multipliedBy(STACKS));
//			put("visage_gravekeepers_cloak.bonus_resist", VULNERABILITY_MAG.multipliedBy(STACKS).pct().neg()); // multiplicative mults don't work (yet)
			put("troll_warlord_fervor.attack_speed", ATTACK_SPEED_HERO.multipliedBy(STACKS));
			put("shredder_reactive_armor.bonus_armor", ARMOR.multipliedBy(STACKS));
			put("shredder_reactive_armor.bonus_hp_regen", HP_REGEN.multipliedBy(STACKS));
			put("bristleback_viscous_nasal_goo.armor_per_stack", ENEMY_ARMOR.multipliedBy(STACKS).neg());
			put("bristleback_viscous_nasal_goo.move_slow_per_stack", ENEMY_MOVEMENT_SPEED_BONUS_MULT.multipliedBy(STACKS).pct().neg());
			put("bristleback_warpath.damage_per_stack", ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS));
			put("bristleback_warpath.move_speed_per_stack", MOVEMENT_SPEED_BONUS_MULT.multipliedBy(STACKS).pct());
			put("legion_commander_duel.reward_damage", ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS));
			
			// scepter
			put("bonus_cleave_damage_scepter", CLEAVE_MULT.multipliedBy(HAS_SCEPTER).pct());
			//put("naga_siren_song_of_the_siren, regen_rate_self", HP_REGEN.multipliedBy(HP).multipliedBy(HAS_SCEPTER).pct());
		}
	};
	
	private final static Map<String, Attribute> activeAttributesFactored = new HashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("evasion_pct_tooltip", ENEMY_HIT_CHANCE_HERO.pct().neg());
			
			put("tooltip_brain_sap_heal_amt", HP_REGEN);
			put("necrolyte_death_pulse.heal", HP_REGEN);
			put("omniknight_purification.heal", HP_REGEN);
			put("dazzle_shadow_wave.damage", HP_REGEN);
			
			put("keeper_of_the_light_chakra_magic.mana_restore", MP_REGEN);
			
			put("abaddon_death_coil.self_damage", HP_REGEN.neg());
			
			put("templar_assassin_meld.bonus_damage", SPELL_DPS_PHY_SINGLE.multipliedBy(HIT_CHANCE_HERO));
			put("skywrath_mage_arcane_bolt.int_multiplier", SPELL_DPS_MAG_SINGLE.multipliedBy(PRIMARY_ATTRIBUTE_VALUE));
			put("skywrath_mage_arcane_bolt.bolt_damage", SPELL_DPS_MAG_SINGLE);
			put("abaddon_death_coil.target_damage", SPELL_DPS_MAG_SINGLE);
			put("bounty_hunter_shuriken_toss.bonus_damage", SPELL_DPS_MAG_SINGLE);
			put("riki_blink_strike.bonus_damage", SPELL_DPS_MAG_SINGLE);
		}
	};
	
	private final static void handleAbility(final String id, final JSONObject abilities, final JSONObject baseAbility, final JSONObject levelData, final Map<String, Double> attrs, //
	/*		*/ final double damage, final double duration, final int level, final double cooldown, final double manacost) {
		
		// special abilities
		switch (id) {
			case "attribute_bonus":
			case "invoker_attribute_bonus":
				final double bonus = attrs.remove("attribute_bonus_per_level");
				AGI.add(levelData, bonus * level);
				INT.add(levelData, bonus * level);
				STR.add(levelData, bonus * level);
				break;
			case "axe_counter_helix":
				BONUS_DAMAGE_PURE_ON_ATTACKED__DAMAGE.add(levelData, damage);
				BONUS_DAMAGE_PURE_ON_ATTACKED__CHANCE.add(levelData, attrs.remove("trigger_chance"));
				BONUS_DAMAGE_PURE_ON_ATTACKED__COOLDOWN.add(levelData, attrs.remove("cooldown"));
				BONUS_DAMAGE_PURE_ON_ATTACKED__MAX_ADDITIONAL_ENEMIES.add(levelData, 1000000);
				break;
			case "slardar_bash":
				BONUS_DAMAGE_PHY__DAMAGE.add(levelData, attrs.remove("bonus_damage"));
				BONUS_DAMAGE_PHY__CHANCE.add(levelData, attrs.remove("chance") / 100);
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "faceless_void_time_lock":
				BONUS_DAMAGE_MAG__DAMAGE.add(levelData, attrs.remove("bonus_damage"));
				BONUS_DAMAGE_MAG__CHANCE.add(levelData, attrs.remove("chance_pct") / 100);
				BONUS_DAMAGE_MAG__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "sniper_headshot":
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "bloodseeker_bloodrage":
				double damageIncrease = 1 + attrs.remove("damage_increase_pct") / 100;
				VULNERABILITY_ALL.mult(levelData, damageIncrease);
				ENEMY_VULNERABILITY_ALL.mult(levelData, damageIncrease);
				break;
			case "luna_moon_glaive":
				final int bounces = (int) (double) attrs.remove("bounces");
				final double damageReduction = 1 - attrs.remove("damage_reduction_percent") / 100;
				damageIncrease = 0;
				double dmg = 1;
				for (int i = 0; i < bounces; i++)
					damageIncrease += (dmg *= damageReduction);
				BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).index(0).add(levelData, damageIncrease);
				BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).index(1).add(levelData, -damageIncrease);
				BONUS_DAMAGE_PHY__CHANCE.index(0).set(levelData, 1);
				BONUS_DAMAGE_PHY__CHANCE.index(1).set(levelData, -1);
				BONUS_DAMAGE_PHY__MAX_ADDITIONAL_ENEMIES.index(0).set(levelData, 1);
				NAME.set(levelData, NAME.get(baseAbility, "") + " (" + bounces + " bounces)");
				break;
			case "medusa_split_shot":
				ATTACK_DAMAGE_TOTAL_BONUS_MULT.add(levelData, attrs.remove("damage_modifier") / 100);
				// TODO should not be affected by spell damage amp
				BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).index(0).add(levelData, 1);
				BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).index(1).add(levelData, -1);
				BONUS_DAMAGE_PHY__CHANCE.index(0).add(levelData, 1);
				BONUS_DAMAGE_PHY__CHANCE.index(1).add(levelData, 1);
				BONUS_DAMAGE_PHY__MAX_ADDITIONAL_ENEMIES.index(0).add(levelData, attrs.remove("arrow_count"));
				break;
			case "enchantress_natures_attendants":
				HP_REGEN.add(levelData, attrs.remove("wisp_count") * attrs.remove("heal"));
				break;
			case "huskar_burning_spear":
				BONUS_DAMAGE_MAG__DAMAGE.add(levelData, damage * duration);
				BONUS_DAMAGE_MAG__CHANCE.add(levelData, 1);
				break;
			case "weaver_geminate_attack":
				ATTACKS_PER_SECOND.add(levelData, 1 / cooldown);
				break;
			case "jakiro_liquid_fire":
				SPELL_DPS_MAG_AOE.add(levelData, attrs.remove("damage") * Math.min(1, duration / cooldown));
				break;
			case "batrider_sticky_napalm":
				dmg = attrs.remove("damage");
				STACKS_MAX.set(baseAbility, 1000);
				ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS).add(levelData, dmg);
				// TODO needs its own type
				break;
			case "doom_bringer_scorched_earth":
				dmg = attrs.remove("damage_per_second");
				SPELL_DPS_MAG_AOE.add(levelData, dmg);
				HP_REGEN.add(levelData, dmg);
				break;
			case "spirit_breaker_greater_bash":
				BONUS_DAMAGE_MAG__DAMAGE.multipliedBy(MOVEMENT_SPEED).add(levelData, attrs.remove("damage") / 100);
				BONUS_DAMAGE_MAG__CHANCE.add(levelData, attrs.remove("chance_pct") / 100);
				BONUS_DAMAGE_MAG__COOLDOWN.add(levelData, cooldown);
				break;
			case "ursa_fury_swipes":
				STACKS_MAX.set(baseAbility, 1000);
				BONUS_DAMAGE_PHY__CHANCE.add(levelData, 1);
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "treant_living_armor":
				HP_REGEN.add(levelData, attrs.remove("health_regen") * Math.min(1, attrs.remove("duration") / cooldown));
				break;
			case "troll_warlord_berserkers_rage":
				BONUS_DAMAGE_MAG__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "centaur_return":
				BONUS_DAMAGE_PHY_ON_ATTACKED__CHANCE.add(levelData, 1);
				break;
			case "elder_titan_ancestral_spirit":
				final int numCreeps = 5, numHeroes = 2;
				MOVEMENT_SPEED_BONUS_MULT.add(levelData, attrs.remove("move_pct_heroes") / 100 * numHeroes + attrs.remove("move_pct_creeps") / 100 * numCreeps);
				ATTACK_DAMAGE_TOTAL.add(levelData, attrs.remove("damage_heroes") * numHeroes + attrs.remove("damage_creeps") * numCreeps);
				NAME.add(levelData, " (" + numHeroes + " heroes, " + numCreeps + " creeps)");
				break;
			case "legion_commander_moment_of_courage":
				BONUS_DAMAGE_PHY_ON_ATTACKED__CHANCE.add(levelData, attrs.remove("trigger_chance"));
				BONUS_DAMAGE_PHY_ON_ATTACKED__COOLDOWN.add(levelData, cooldown);
				BONUS_DAMAGE_PHY_ON_ATTACKED__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).add(levelData, 1);
				HP_REGEN.multipliedBy(BONUS_DPS_PHY_ON_ATTACKED_SINGLE).add(levelData, attrs.remove("hp_leech_percent") / 100); // assumes no other source of physical bonus damage when attacked
				break;
			case "legion_commander_duel":
				STACKS_MAX.set(baseAbility, 1000);
				STACKS_NAME.set(baseAbility, "duels won");
				break;
			case "pudge_flesh_heap":
				STACKS_MAX.set(baseAbility, 1000);
				break;
			case "razor_eye_of_the_storm":
				STACKS_MAX.set(baseAbility, duration / attrs.remove("strike_interval_scepter"));
//		break;case "viper_nethertoxin":
//			stacks = (int) ability.getDouble(LEVEL.id) * 3;
				break;
			case "terrorblade_conjure_image":
				ILLUSION__COUNT.add(levelData, 1);
				break;
			case "tidehunter_kraken_shell":
				BLOCK__CHANCE.add(levelData, 1);
				break;
			case "skeleton_king_mortal_strike":
				CRIT__MULTIPLIER.multipliedBy(ENEMY_IS_HERO).add(levelData, attrs.remove("crit_mult") / 100);
				CRIT__MULTIPLIER.multipliedBy(ENEMY_IS_CREEP).add(levelData, attrs.remove("crit_mult_creeps") / 100);
				break;
			case "lycan_shapeshift":
				final double speed = attrs.remove("speed");
				MOVEMENT_SPEED.add(levelData, speed);
				MOVEMENT_SPEED_MAX.add(levelData, speed);
				break;
			case "riki_permanent_invisibility":
				BONUS_DAMAGE_PHY__CHANCE.add(levelData, 1);
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(levelData, 1);
				break;
			case "spectre_desolate":
				BONUS_DAMAGE_PURE__CHANCE.add(levelData, 1);
				break;
			case "ancient_apparition_chilling_touch":
				BONUS_DAMAGE_MAG__CHANCE.add(levelData, 1);
				break;
			case "windrunner_focusfire":
				final double baseReduction = attrs.remove("focusfire_damage_reduction") / 100;
				ATTACK_DAMAGE_TOTAL_BONUS_MULT.add(levelData, baseReduction);
				ATTACK_DAMAGE_TOTAL_BONUS_MULT.multipliedBy(HAS_SCEPTER).add(levelData, -baseReduction + attrs.remove("focusfire_damage_reduction_scepter") / 100);
				break;
			case "slark_essence_shift":
				STACKS_MAX.set(baseAbility, 1000);
				AGI.multipliedBy(STACKS).add(levelData, attrs.remove("agi_gain"));
				final double loss = attrs.remove("stat_loss");
				BONUS_DAMAGE_PURE__CHANCE.set(levelData, 1);
				BONUS_DAMAGE_PURE__DAMAGE.set(levelData, loss * Constants.HP_PER_STR / 2);
//				ENEMY_ARMOR.multipliedBy(STACKS).add(levelData, -loss / 0.14); // stacks can be from multiple enemies
				break;
			case "bloodseeker_thirst":
				MOVEMENT_SPEED_MAX.set(levelData, 10000);
				final double min = attrs.remove("min_bonus_pct"), max = attrs.remove("max_bonus_pct");
				final double stacks = min - max; // 1% each
				STACKS_MAX.set(levelData, stacks * 5);// 5 heroes
				STACKS_NAME.set(baseAbility, "% of missing enemy heroes' HP below " + (int) min + "% HP");
				ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS).add(levelData, attrs.remove("bonus_damage") / stacks);
				MOVEMENT_SPEED_BONUS_MULT.multipliedBy(STACKS).add(levelData, attrs.remove("bonus_movement_speed") / 100 / stacks);
				break;
			case "obsidian_destroyer_arcane_orb":
				BONUS_DAMAGE_PURE__CHANCE.add(levelData, 1);
				INT.multipliedBy(STACKS).add(levelData, attrs.remove("int_steal"));
				STACKS_MAX.set(baseAbility, 1000);
				break;
			case "bounty_hunter_wind_walk":
				SPELL_DPS_PHY_SINGLE.multipliedBy(STACKS).add(levelData, attrs.remove("bonus_damage") / cooldown); // .multipliedBy(HIT_CHANCE_HERO) // not very importent, as one can just set the stacks accordingly.
				STACKS_MAX.set(baseAbility, 10);
				STACKS_NAME.set(baseAbility, "attacks while invisible");
				break;
			case "bounty_hunter_jinada":
				final double critBonus = attrs.remove("crit_multiplier") / 100 - 1;
				// this is not completely correct, but almost (other crits and stuff depending on attack damage with crits will be a bit off)
				BONUS_DAMAGE_PHY__DAMAGE.multipliedBy(ATTACK_DAMAGE_TOTAL).add(levelData, critBonus);
				BONUS_DAMAGE_PHY__CHANCE.set(levelData, 1);
				BONUS_DAMAGE_PHY__COOLDOWN.set(levelData, cooldown);
				BONUS_DAMAGE_PHY__LIFESTEAL_MULT.set(levelData, 1);
				break;
			case "phantom_assassin_stifling_dagger":
				// quite a bit off (it's a normal unevadable attack with more base damage and reduced attack damage %)
				ATTACKS_PER_SECOND.add(levelData, 1 / cooldown);
				SPELL_DPS_PHY_SINGLE.multipliedBy(ATTACK_DAMAGE_TOTAL).add(levelData, attrs.remove("attack_factor") / 100 / cooldown);
				SPELL_DPS_PHY_SINGLE.add(levelData, attrs.remove("base_damage") / cooldown);
				break;
			case "invoker_wex":
			case "invoker_exort":
			case "invoker_quas":
				STACKS_MAX.set(baseAbility, 3);
				STACKS_NAME.set(baseAbility, "active instances");
				ATTACK_SPEED_HERO.multipliedBy(STACKS).add(levelData, attrs.getOrDefault("attack_speed_per_instance", 0.0));
				ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS).add(levelData, attrs.getOrDefault("bonus_damage_per_instance", 0.0));
				MOVEMENT_SPEED_BONUS_MULT.multipliedBy(STACKS).pct().add(levelData, attrs.getOrDefault("move_speed_per_instance", 0.0));
				HP_REGEN.multipliedBy(STACKS).add(levelData, attrs.getOrDefault("health_regen_per_instance", 0.0));
				break;
			case "life_stealer_feast":
				STACKS_MAX.set(baseAbility, 100000);
				STACKS_NAME.set(baseAbility, "Enemy HP");
				final double factor = attrs.remove("hp_leech_percent") / 100;
				ATTACK_DAMAGE_TOTAL.multipliedBy(STACKS).add(levelData, factor);
				HP_REGEN_PER_ATTACK_EVADABLE.multipliedBy(STACKS).add(levelData, factor);
				break;
			case "riki_tricks_of_the_trade":
				ATTACK_SPEED_HERO.add(levelData, -100000);
				ATTACKS_PER_SECOND.set(levelData, 1 - 0.2 / 1.7); // to get exactly 1 attack per second (20% min attack speed, 1.7s BAT)
				ATTACKS_PER_SECOND.multipliedBy(NUM_ADDITIONAL_ENEMIES).set(levelData, 1);
				break;
		}
		
		// damage attribute
		final Attribute dmg = abilityDamage.get(id);
		if (dmg != null)
			dmg.add(levelData, damage);
			
		if (attrs.containsKey("damage_per_burn")) {
			BONUS_DAMAGE_PHY__DAMAGE.add(levelData, attrs.remove("damage_per_burn") * attrs.remove("mana_per_hit"));
			BONUS_DAMAGE_PHY__LIFESTEAL_MULT.add(levelData, 1);
		}
		
		if (cooldown > 0)
			MP_REGEN.add(levelData, -MANA_COST.get(levelData, 0) / cooldown);
			
		// default attributes
		final Iterator<Entry<String, Double>> iter = attrs.entrySet().iterator();
		while (iter.hasNext()) {
			final Entry<String, Double> attr = iter.next();
			Attribute a = attributes.get(id + "." + attr.getKey()), af = null;
			if (a == null)
				af = activeAttributesFactored.get(id + "." + attr.getKey());
			if (a == null && af == null)
				a = attributes.get(attr.getKey());
			if (a == null && af == null)
				af = activeAttributesFactored.get(attr.getKey());
			if (a != null) {
				a.add(levelData, attr.getValue());
				iter.remove();
			} else if (af != null) {
				af.add(levelData, attr.getValue() / cooldown);
				iter.remove();
			}
		}
		
	}
	
}
