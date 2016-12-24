package ch.njol.scripts.dota;

import static ch.njol.scripts.dota.Attribute.*;
import static ch.njol.scripts.dota.Main.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class Heroes {
	
	private final static JSONObject baseHero = new JSONObject();
	
	static {
		ATTACK_SPEED_HERO.set(baseHero, 100);
		MOVEMENT_SPEED_MAX.set(baseHero, 522);
	}
	
	public static void execute() throws IOException {
		
		final Map<String, Attribute> attributes = new HashMap<>();
		attributes.put("AttributeBaseStrength", STR);
		attributes.put("AttributeStrengthGain", STR_GAIN);
		attributes.put("AttributeBaseIntelligence", INT);
		attributes.put("AttributeIntelligenceGain", INT_GAIN);
		attributes.put("AttributeBaseAgility", AGI);
		attributes.put("AttributeAgilityGain", AGI_GAIN);
		attributes.put("MovementSpeed", MOVEMENT_SPEED);
		attributes.put("ArmorPhysical", ARMOR);
		attributes.put("AttackRate", BASE_ATTACK_TIME);
		attributes.put("StatusHealth", HP);
		attributes.put("StatusHealthRegen", HP_REGEN);
		attributes.put("StatusMana", MP);
		attributes.put("StatusManaRegen", MP_REGEN_BASE);
		attributes.put("MagicalResistance", VULNERABILITY_MAG.pct().neg());
		
		final JSONObject heroes = new JSONObject();
		
		heroesData.forEach((String id, final Object value) -> {
			id = id.replaceFirst("^npc_dota_hero_", "");
			if (!(value instanceof DotaData)) // version
				return;
			final DotaData heroData = (DotaData) value;
			final JSONObject hero = id.equals("base") ? baseHero : cloneJO(baseHero);
			
			final String primary = heroData.getString("AttributePrimary");
			if (primary.equals("DOTA_ATTRIBUTE_STRENGTH")) {
				PRIMARY_ATTRIBUTE.set(hero, "str");
			} else if (primary.equals("DOTA_ATTRIBUTE_AGILITY")) {
				PRIMARY_ATTRIBUTE.set(hero, "agi");
			} else {
				assert primary.equals("DOTA_ATTRIBUTE_INTELLECT");
				PRIMARY_ATTRIBUTE.set(hero, "int");
			}
			
			final String attackType = heroData.getString("AttackCapabilities");
			if (attackType.equals("DOTA_UNIT_CAP_RANGED_ATTACK")) {
				IS_MELEE.set(hero, 0);
				IS_RANGED.set(hero, 1);
			} else {
				assert attackType.equals("DOTA_UNIT_CAP_MELEE_ATTACK");
				IS_MELEE.set(hero, 1);
				IS_RANGED.set(hero, 0);
			}
			
			final double damageMin = heroData.getDouble("AttackDamageMin"), damageMax = heroData.getDouble("AttackDamageMax");
			ATTACK_DAMAGE_BASE.set(hero, (damageMin + damageMax) / 2);
			
			if (hero != baseHero) {
				if (id.equals("silencer")) {
					ABILITIES.addToList(hero, "silencer_stolen_intelligence");
				}
				for (int i = 1; i < 20; i++) {
					final String ability = heroData.getString("Ability" + i);
					if (ability == null || ability.isEmpty())
						continue;
					if (!ability.equals("attribute_bonus") && !ability.contains("empty"))
						ABILITIES.addToList(hero, ability);
				}
			}
			if (!id.equals("base") && !id.equals("invoker")) {
				ABILITIES.addToList(hero, "attribute_bonus");
			}
			
			for (final Entry<String, Attribute> attr : attributes.entrySet()) {
				final double val = heroData.getDouble(attr.getKey());
				if (!Double.isNaN(val))
					attr.getValue().reset(hero).add(hero, val);
			}
			
			NAME.set(hero, names.getString("npc_dota_hero_" + id));
			
			if (!id.equals("base"))
				heroes.put(id, hero);
		});
		
		writeEnvs(heroes, "heroes", "heroes.js");
		
	}
	
}
