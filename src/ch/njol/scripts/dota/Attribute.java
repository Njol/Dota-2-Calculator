package ch.njol.scripts.dota;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.njol.util.NonNullPair;
import ch.njol.util.StringUtils;

public final class Attribute {
	
	private final boolean isCopy;
	private final Attribute original;
	
	public Object defaultValue;
	private String[] path;
	private boolean neg = false, pct = false;
	private double offset = 0;
	private Attribute multipliedBy = null;
	private int index = 0;
	
	private boolean onlyInGlobalEnv;
	private boolean isParent = false;
	private Attribute parent;
	
	private Attribute(final Object defaultValue) {
		this(defaultValue, null, false);
	}
	
	private Attribute(final Object defaultValue, final String addBehaviour) {
		this(defaultValue, addBehaviour, false);
	}
	
	private Attribute(final Object defaultValue, final boolean onlyInGlobalEnv) {
		this(defaultValue, null, onlyInGlobalEnv);
	}
	
	private Attribute(final Object defaultValue, final String addBehaviour, final boolean onlyInGlobalEnv) {
		if (onlyInGlobalEnv)
			assert addBehaviour == null;
		this.defaultValue = defaultValue;
		this.addBehaviour = addBehaviour;
		this.onlyInGlobalEnv = onlyInGlobalEnv;
		
		isCopy = false;
		original = this;
	}
	
	private Attribute(final Attribute other) {
		path = other.path;
		defaultValue = other.defaultValue;
		addBehaviour = other.addBehaviour;
		neg = other.neg;
		pct = other.pct;
		parent = other.parent;
		isParent = other.isParent;
		multipliedBy = other.multipliedBy;
		offset = other.offset;
		index = other.index;
		
		isCopy = true;
		original = other.original;
	}
	
	public Attribute pct() {
		if (!(defaultValue instanceof Number))
			throw new IllegalArgumentException();
		final Attribute r = new Attribute(this);
		r.pct = !pct;
		return r;
	}
	
	public Attribute neg() {
		if (!(defaultValue instanceof Number))
			throw new IllegalArgumentException();
		final Attribute r = new Attribute(this);
		r.neg = !neg;
		return r;
	}
	
	public Attribute offset(final double offset) {
		if (!(defaultValue instanceof Number))
			throw new IllegalArgumentException();
		final Attribute r = new Attribute(this);
		r.offset += offset;
		return r;
	}
	
	public Attribute index(final double index) {
		if (path.length == 1)
			throw new IllegalArgumentException();
		final Attribute r = new Attribute(this);
		r.index += index;
		return r;
	}
	
	private final static Set<NonNullPair<Attribute, Attribute>> usedMultiplications = new HashSet<>();
	
	public Attribute multipliedBy(final Attribute mult) {
		if (isCopy || mult.isCopy || mult.path.length > 1 || mult.isParent)
			throw new IllegalArgumentException();
		usedMultiplications.add(new NonNullPair<>(this, mult));
		final Attribute r = new Attribute(this);
		r.multipliedBy = mult;
		return r;
	}
	
	private String pathToFirst() {
		return path.length == 1 ? path[0] : path[0] + "[0]." + path[1];
	}
	
	@Override
	public String toString() {
		return StringUtils.join(path, ":");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(path);
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Attribute))
			return false;
		final Attribute other = (Attribute) obj;
		if (!Arrays.equals(path, other.path))
			return false;
		return true;
	}
	
	private String calculation;
	private final Set<Attribute> dependencies = new HashSet<>();
	
	@SuppressWarnings("null")
	private void dep(String calculation, final Attribute... dependencies) {
		if (dependencies.length > 0)
			assert calculation.matches(".*#" + dependencies.length + "\\b.*");
		if (ArrayUtils.contains(dependencies, HP_REGEN)) // bonus damage lifesteal would have to be made correct first
			throw new IllegalArgumentException("" + this);
		if (calculation.contains("#0") && (!calculation.startsWith("=") || calculation.matches(".*#0.*#0.*")))
			throw new IllegalArgumentException();
		if (isParent)
			assert !calculation.startsWith("+=") && !calculation.startsWith("*=") && !calculation.startsWith("=") && !calculation.contains("#0");
		if (parent != null)
			assert !calculation.contains("#0");
		calculation = StringUtils.replaceAll(calculation, "#([1-9][0-9]*)", (final Matcher m) -> {
			final String[] p = m.group(1).equals("0") ? path : dependencies[Integer.parseInt(m.group(1)) - 1].path;
			return p.length == 1 ? "this." + p[0] : p[1];
		});
		if (this.calculation != null) {
			if (isParent) {
				this.calculation += "; " + calculation;
			} else if (this.calculation.startsWith("+=") && calculation.startsWith("+=") || this.calculation.startsWith("*=") && calculation.startsWith("*=")) {
				if (defaultValue instanceof JSONArray)
					this.calculation = calculation.substring(0, 2) + "addArrayToArray(" + this.calculation.substring(2).trim() + ", " + calculation.substring(2).trim() + ")";
				else
					this.calculation = calculation.substring(0, 2) + " (" + this.calculation.substring(2).trim() + ") " + calculation.substring(0, 1) + " (" + calculation.substring(2).trim() + ")";
//			} else if (this.calculation.startsWith("=") && calculation.startsWith("+=")) { // allow additions all the time
//				if (defaultValue instanceof JSONArray)
//					this.calculation = "= addArrayToArray(" + this.calculation.substring(1).trim() + ", " + calculation.substring(2).trim() + ")";
//				else
//					this.calculation += " + (" + calculation.substring(2).trim() + ")";
			} else if (this.calculation.startsWith("+=") && calculation.startsWith("=") && calculation.contains("#0")) {
				assert !(defaultValue instanceof JSONArray);
				this.calculation = calculation.replace("#0", "(#0 + " + this.calculation.substring(2).trim() + ")");
			} else if (calculation.startsWith("+=") && this.calculation.startsWith("=") && this.calculation.contains("#0")) {
				assert !(defaultValue instanceof JSONArray);
				this.calculation = this.calculation.replace("#0", "(#0 + " + calculation.substring(2).trim() + ")");
			} else {
				throw new IllegalArgumentException(this.calculation + " / " + calculation);
			}
		} else {
			this.calculation = calculation;
		}
		this.dependencies.addAll(Arrays.asList(dependencies));
		for (final Attribute dep : dependencies) { // add all children of a group as dependencies
			if (dep.isParent)
				this.dependencies.addAll(dep.getChildren());
		}
	}
	
	private List<Attribute> getChildren() {
		assert isParent;
		final List<Attribute> r = new ArrayList<>();
		for (final Attribute a : allAttributes) {
			if (a.path.length == 2 && a.path[0].equals(path[0]))
				r.add(a);
		}
		return r;
	}
	
	public final static List<Attribute> allAttributes = new ArrayList<>();
	
	public final static Attribute getAttribute(final String id) {
		for (final Attribute attr : allAttributes) {
			if (attr.path[0].equals(id.toLowerCase(Locale.ENGLISH)))
				return attr;
		}
		throw new IllegalArgumentException(id);
	}
	
	private JSONObject getJO(JSONObject o) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (path.length > 1) {
			if (!o.has(path[0]))
				o.put(path[0], new JSONArray());
			final JSONArray array = o.getJSONArray(path[0]);
			while (array.length() <= index)
				array.put(new JSONObject());
			o = array.getJSONObject(index);
		}
		if (multipliedBy != null) {
			if (!o.has("mults"))
				o.put("mults", new JSONObject());
			o = o.getJSONObject("mults");
			if (!o.has(path[path.length - 1]))
				o.put(path[path.length - 1], new JSONObject());
			o = o.getJSONObject(path[path.length - 1]);
		}
		return o;
	}
	
	private String JOPath() {
		return multipliedBy != null ? multipliedBy.JOPath() : path[path.length - 1];
	}
	
	public void put(final JSONObject o, final Object value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		getJO(o).put(JOPath(), value);
	}
	
	public boolean has(final JSONObject o) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		return getJO(o).has(JOPath());
	}
	
	public double get(final JSONObject o, final double def) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		final Object val = getJO(o).opt(JOPath());
		if (val == null)
			return def;
		if (!(val instanceof Number))
			throw new IllegalArgumentException(path + "; " + val);
		return ((Number) val).doubleValue();
	}
	
	public String get(final JSONObject o, final String def) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		final Object val = getJO(o).opt(JOPath());
		if (val == null)
			return def;
		if (!(val instanceof String))
			throw new IllegalArgumentException(path + "; " + val);
		return (String) val;
	}
	
	public Attribute reset(final JSONObject o) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		put(o, defaultValue);
		return this;
	}
	
	public void addToList(final JSONObject o, final Object value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof JSONArray))
			throw new IllegalArgumentException();
		JSONArray val = getJO(o).optJSONArray(JOPath());
		if (val == null)
			val = new JSONArray();
		put(o, val.put(value));
	}
	
	public void add(final JSONObject o, double value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (neg)
			value = -value;
		if (pct)
			value = value / 100;
		value += offset;
		put(o, get(o, path.length > 1 ? 0 : ((Number) defaultValue).doubleValue()) + value);
	}
	
	public void add(final JSONObject o, final String suffix) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		put(o, get(o, "") + suffix);
	}
	
	public void mult(final JSONObject o, final double factor) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (path.length > 1)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof Number))
			throw new IllegalArgumentException();
		put(o, get(o, ((Number) defaultValue).doubleValue()) * factor);
	}
	
	public void set(final JSONObject o, double value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof Number))
			throw new IllegalArgumentException();
		if (neg)
			value = -value;
		if (pct)
			value = value / 100;
		value += offset;
		put(o, value);
	}
	
	public void set(final JSONObject o, final boolean value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof Boolean))
			throw new IllegalArgumentException();
		put(o, value);
	}
	
	public void set(final JSONObject o, final String value) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof String))
			throw new IllegalArgumentException();
		put(o, value);
	}
	
	public void set(final JSONObject o, final JSONArray values) {
		if (onlyInGlobalEnv)
			throw new IllegalArgumentException();
		if (!(defaultValue instanceof JSONArray))
			throw new IllegalArgumentException();
		put(o, values);
	}
	
	private final String addBehaviour;
	
	private final static String add = "this.# += other.#",
			mult = "this.# *= other.#",
			smaller = "this.# = Math.min(this.#, other.#)",
			larger = "this.# = Math.max(this.#, other.#)",
			setIfEmpty = "this.# = this.# == '' ? other.# : this.#",
			setIfOtherNotEmpty = "this.# = other.# == '' ? this.# : other.#",
			ignore = "// # ignored";
			
	public final static Attribute //
	/*   */ ABILITIES = new Attribute(new JSONArray(), ignore),
			AGI = new Attribute(0, add),
			AGI_GAIN = new Attribute(0, add),
			ARMOR = new Attribute(0, add),
			ATTACK_DAMAGE_BASE = new Attribute(0, add),
			ATTACK_DAMAGE_BASE_BONUS_MULT_ALL = new Attribute(0, add),
			ATTACK_DAMAGE_BASE_BONUS_MULT_HERO = new Attribute(0, add),
			ATTACK_DAMAGE_TOTAL = new Attribute(0, add),
			ATTACK_DAMAGE_TOTAL_BONUS_MULT = new Attribute(0, add),
			ATTACK_DAMAGE_TOTAL_WITH_CRITS = new Attribute(0, true),
			ATTACK_DPS = new Attribute(0, true),
			ATTACK_DPS_AFTER_REDUCTIONS = new Attribute(0, true),
			ATTACK_SPEED_ALL = new Attribute(0, add),
			ATTACK_SPEED_HERO = new Attribute(0, add),
			ATTACKS_PER_SECOND = new Attribute(0, add),
			BASE_ATTACK_TIME = new Attribute(0, "this.# = other.# == 0 ? this.# : this.# == 0 ? other.# : Math.min(this.#, other.#)"),
			BASE_ID = new Attribute("", ignore), // for items (active variants and such)
			BLOCK = new Attribute(new JSONArray()),
			BLOCK__AMOUNT = new Attribute(0),
			BLOCK__CHANCE = new Attribute(1),
			BONUS_DAMAGE_MAG = new Attribute(new JSONArray()),
			BONUS_DAMAGE_MAG__CHANCE = new Attribute(0),
			BONUS_DAMAGE_MAG__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_MAG__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_MAG__LIFESTEAL_MULT = new Attribute(0),
			BONUS_DAMAGE_MAG__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DAMAGE_MAG_ON_ATTACKED = new Attribute(new JSONArray()),
			BONUS_DAMAGE_MAG_ON_ATTACKED__CHANCE = new Attribute(0),
			BONUS_DAMAGE_MAG_ON_ATTACKED__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_MAG_ON_ATTACKED__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_MAG_ON_ATTACKED__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DAMAGE_PHY = new Attribute(new JSONArray()),
			BONUS_DAMAGE_PHY__CHANCE = new Attribute(0),
			BONUS_DAMAGE_PHY__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_PHY__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_PHY__LIFESTEAL_MULT = new Attribute(0),
			BONUS_DAMAGE_PHY__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DAMAGE_PHY_ON_ATTACKED = new Attribute(new JSONArray()),
			BONUS_DAMAGE_PHY_ON_ATTACKED__CHANCE = new Attribute(0),
			BONUS_DAMAGE_PHY_ON_ATTACKED__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_PHY_ON_ATTACKED__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_PHY_ON_ATTACKED__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DAMAGE_PURE = new Attribute(new JSONArray()),
			BONUS_DAMAGE_PURE__CHANCE = new Attribute(0),
			BONUS_DAMAGE_PURE__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_PURE__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_PURE__LIFESTEAL_MULT = new Attribute(0),
			BONUS_DAMAGE_PURE__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DAMAGE_PURE_ON_ATTACKED = new Attribute(new JSONArray()),
			BONUS_DAMAGE_PURE_ON_ATTACKED__CHANCE = new Attribute(0),
			BONUS_DAMAGE_PURE_ON_ATTACKED__COOLDOWN = new Attribute(0),
			BONUS_DAMAGE_PURE_ON_ATTACKED__DAMAGE = new Attribute(0),
			BONUS_DAMAGE_PURE_ON_ATTACKED__MAX_ADDITIONAL_ENEMIES = new Attribute(0),
			BONUS_DPS_MAG_ON_ATTACKED_REST = new Attribute(0, true),
			BONUS_DPS_MAG_ON_ATTACKED_SINGLE = new Attribute(0, true),
			BONUS_DPS_MAG_REST = new Attribute(0, true),
			BONUS_DPS_MAG_SINGLE = new Attribute(0, true),
			BONUS_DPS_PHY_ON_ATTACKED_REST = new Attribute(0, true),
			BONUS_DPS_PHY_ON_ATTACKED_SINGLE = new Attribute(0, true),
			BONUS_DPS_PHY_REST = new Attribute(0, true),
			BONUS_DPS_PHY_SINGLE = new Attribute(0, true),
			BONUS_DPS_PURE_ON_ATTACKED_REST = new Attribute(0, true),
			BONUS_DPS_PURE_ON_ATTACKED_SINGLE = new Attribute(0, true),
			BONUS_DPS_PURE_REST = new Attribute(0, true),
			BONUS_DPS_PURE_SINGLE = new Attribute(0, true),
			BUILT_FROM = new Attribute(new JSONArray(), ignore),
			CLEAVE_MULT = new Attribute(0, add),
			COOLDOWN = new Attribute(0, ignore),
			COST = new Attribute(0, add),
			CRIT = new Attribute(new JSONArray()),
			CRIT__CHANCE = new Attribute(0),
			CRIT__MULTIPLIER = new Attribute(0),
			DPS_AOE_AFTER_REDUCTIONS = new Attribute(0, true),
			DPS_MAG_SINGLE_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_MAG_TOTAL_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_PHY_SINGLE_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_PHY_TOTAL_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_PURE_SINGLE_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_PURE_TOTAL_AFTER_REDUCTIONS = new Attribute(0, add),
			DPS_SINGLE_AFTER_REDUCTIONS = new Attribute(0, true),
			DPS_TOTAL_AFTER_REDUCTIONS = new Attribute(0, true),
			EFF_HP_MAG = new Attribute(0, add),
			EFF_HP_PHY = new Attribute(0, add),
			ENEMY_ARMOR = new Attribute(0, add),
			ENEMY_ARMOR_MULT = new Attribute(1, mult),
			ENEMY_ATTACK_SPEED = new Attribute(0, add),
			ENEMY_ATTACKS_PER_SECOND = new Attribute(0, add),
			ENEMY_BASE_MOVEMENT_SPEED = new Attribute(0, smaller),
			ENEMY_DAMAGE_PHY_BASE_BONUS_MULT = new Attribute(0, add),
			ENEMY_HAS_MANA = new Attribute(1, true),
			ENEMY_HIT_CHANCE_ALL = new Attribute(1, mult),
			ENEMY_HIT_CHANCE_HERO = new Attribute(1, mult),
			ENEMY_IS_BUILDING = new Attribute(0, true),
			ENEMY_IS_CREEP = new Attribute(0, true),
			ENEMY_IS_CREEP_AND_HERO_IS_MELEE = new Attribute(0, true),
			ENEMY_IS_CREEP_AND_HERO_IS_RANGED = new Attribute(0, true),
			ENEMY_IS_HERO = new Attribute(1, true),
			ENEMY_MOVEMENT_SPEED_BONUS_MULT = new Attribute(0, add),
			ENEMY_VULNERABILITY_ALL = new Attribute(1, mult),
			ENEMY_VULNERABILITY_MAG = new Attribute(1, mult),
			ENEMY_VULNERABILITY_PHY = new Attribute(1, mult),
			ENEMY_VULNERABILITY_PHY_BASIC = new Attribute(1, mult),
			ENEMY_VULNERABILITY_PHY_HERO = new Attribute(1, mult),
			ENEMY_VULNERABILITY_PHY_PIERCE = new Attribute(1, mult),
			ENEMY_VULNERABILITY_PURE = new Attribute(1, mult),
			HAS_SCEPTER = new Attribute(0, larger),
			HIT_CHANCE_ALL = new Attribute(1, mult),
			HIT_CHANCE_HERO = new Attribute(1, mult),
			HP = new Attribute(0, add),
			HP_REGEN = new Attribute(0, add),
			HP_REGEN_PER_ATTACK = new Attribute(0, add),
			HP_REGEN_PER_ATTACK_EVADABLE = new Attribute(0, add),
			ILLUSION = new Attribute(new JSONArray()),
			ILLUSION__COUNT = new Attribute(0),
			ILLUSION__DAMAGE_DEALT_MULT = new Attribute(1),
			ILLUSION__DAMAGE_TAKEN_MULT = new Attribute(1),
			ILLUSIONS_BONUS_DAMAGE_PHY = new Attribute(0, ignore),
			INT = new Attribute(0, add),
			INT_GAIN = new Attribute(0, add),
			IS_CONSUMABLE = new Attribute(false, ignore),
			IS_MELEE = new Attribute(1, mult), // only heroes have 0 in one of these two
			IS_RANGED = new Attribute(1, mult), // "
			IS_ULTIMATE = new Attribute(0, ignore),
			LEVEL = new Attribute(0, ignore),
			LEVEL_MAX = new Attribute(0, ignore),
			LEVELS = new Attribute(new JSONArray(), "if (other.#.length > 0) {var e = new Env(other.#[other.level]); e.stacks = other.stacks; this.addToThis(e);}"),
			LIFESTEAL = new Attribute(0, add),
			MANA_COST = new Attribute(0, ignore),
			MOVEMENT_SPEED = new Attribute(0, add),
			MOVEMENT_SPEED_BONUS_MULT = new Attribute(0, add),
			MOVEMENT_SPEED_MAX = new Attribute(0, larger),
			MP = new Attribute(0, add),
			MP_REGEN = new Attribute(0, add),
			MP_REGEN_BASE = new Attribute(0, add),
			MP_REGEN_BASE_BONUS_MULT = new Attribute(0, add),
			MP_REGEN_PER_ATTACK = new Attribute(0, add),
			// MULTS is reserved
			NAME = new Attribute("", setIfOtherNotEmpty),
			NUM_ADDITIONAL_ENEMIES = new Attribute(0, true),
			ORCHID_MULT = new Attribute(0, add),
			PRIMARY_ATTRIBUTE = new Attribute("", setIfEmpty),
			PRIMARY_ATTRIBUTE_VALUE = new Attribute(0, add),
			SPELL_COOLDOWN_MULT = new Attribute(1, mult),
			SPELL_DAMAGE_BONUS_MULT = new Attribute(0, add),
			SPELL_DPS_MAG_AOE = new Attribute(0, add),
			SPELL_DPS_MAG_SINGLE = new Attribute(0, add),
			SPELL_DPS_PHY_AOE = new Attribute(0, add),
			SPELL_DPS_PHY_SINGLE = new Attribute(0, add),
			SPELL_DPS_PURE_AOE = new Attribute(0, add),
			SPELL_DPS_PURE_SINGLE = new Attribute(0, add),
			SPELL_LIFESTEAL = new Attribute(0, add),
			STACKS = new Attribute(0, true),
			STACKS_MAX = new Attribute(0, ignore),
			STACKS_NAME = new Attribute("stacks", ignore),
			STR = new Attribute(0, add),
			STR_GAIN = new Attribute(0, add),
			SUMMON = new Attribute(new JSONArray()),
			SUMMON__ATTACK_DAMAGE_BASIC = new Attribute(0),
			SUMMON__ATTACK_DAMAGE_BASIC_BONUS = new Attribute(0),
			SUMMON__ATTACK_DAMAGE_HERO = new Attribute(0),
			SUMMON__ATTACK_DAMAGE_PIERCE = new Attribute(0),
			SUMMON__BASE_ATTACK_TIME = new Attribute(1),
			TREAT_EVASION_AS_PHY_RESIST = new Attribute(true, true),
			TRUE_STRIKE_CHANCE_ALL = new Attribute(0, "this.# = 1 - (1 - this.#) * (1 - other.#)"),
			TRUE_STRIKE_CHANCE_HERO = new Attribute(0, "this.# = 1 - (1 - this.#) * (1 - other.#)"),
			VULNERABILITY_ALL = new Attribute(1, mult),
			VULNERABILITY_MAG = new Attribute(1, mult),
			VULNERABILITY_PHY = new Attribute(1, mult),
			VULNERABILITY_PURE = new Attribute(1, mult);
			
	static {
		try {
			String lastPath = null;
			Attribute lastObject = null;
			for (final Field f : Attribute.class.getFields()) {
				if (f.getType() != Attribute.class)
					continue;
				final Attribute attr = (Attribute) f.get(null);
				attr.path = f.getName().toLowerCase(Locale.ENGLISH).split("__", 2);
				allAttributes.add(attr);
				if (attr.path.length > 1) {
					if (lastObject == null || !(lastObject.defaultValue instanceof JSONArray) || lastObject.addBehaviour != null || attr.addBehaviour != null)
						throw new IllegalArgumentException();
					attr.parent = lastObject;
					lastObject.isParent = true;
				} else {
					lastObject = attr;
				}
				final String path = StringUtils.join(attr.path, ".");
				if (lastPath != null && lastPath.compareTo(path) > 0)
					System.out.println(lastPath + " > " + path);
				lastPath = path;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		for (final Attribute attr : allAttributes)
			assert (attr.addBehaviour == null) == (attr.isParent || attr.parent != null || attr.onlyInGlobalEnv) : attr;
	}
	
	public final static class Constants {
		public final static double ARMOR_PER_AGI = 0.14,
				MP_PER_INT = 12,
				MP_REGEN_BASE_PER_INT = 0.04,
				HP_PER_STR = 20,
				HP_REGEN_PER_STR = 0.03,
				SPELL_DAMAGE_BONUS_MULT_PER_INT = 0.000625,
				MIN_ATTACK_SPEED = 20,
				MAX_ATTACK_SPEED = 600,
				MIN_MOVEMENT_SPEED = 100,
				DEFAULT_MAX_MOVEMENT_SPEED = 522;
		// armor formula is below
	}
	
	static void calculations() {
		// basic attributes and calculations
		AGI.dep("+= #1 * (#2 - 1)", AGI_GAIN, LEVEL);
		INT.dep("+= #1 * (#2 - 1)", INT_GAIN, LEVEL);
		STR.dep("+= #1 * (#2 - 1)", STR_GAIN, LEVEL);
		PRIMARY_ATTRIBUTE_VALUE.dep("+= this[#1]; // uses #2, #3, #4", PRIMARY_ATTRIBUTE, AGI, INT, STR);
		
		HP.dep("+= " + Constants.HP_PER_STR + " * #1", STR);
		HP_REGEN.dep("+= " + Constants.HP_REGEN_PER_STR + " * #1", STR);
		HP_REGEN.dep("+= #1 * #2", HP_REGEN_PER_ATTACK, ATTACKS_PER_SECOND);
		HP_REGEN.dep("+= #1 * #2 * #3", HP_REGEN_PER_ATTACK_EVADABLE, ATTACKS_PER_SECOND, HIT_CHANCE_HERO);
		
		MP.dep("+= " + Constants.MP_PER_INT + " * #1", INT);
		MP_REGEN_BASE.dep("+= " + Constants.MP_REGEN_BASE_PER_INT + " * #1", INT);
		MP_REGEN.dep("+= #1 * (1 + #2)", MP_REGEN_BASE, MP_REGEN_BASE_BONUS_MULT);
		MP_REGEN.dep("+= #1 * #2", MP_REGEN_PER_ATTACK, ATTACKS_PER_SECOND);
		SPELL_DAMAGE_BONUS_MULT.dep("+= " + Constants.SPELL_DAMAGE_BONUS_MULT_PER_INT + " * #1", INT);
		
		ARMOR.dep("+= " + Constants.ARMOR_PER_AGI + " * #1", AGI);
		VULNERABILITY_PHY.dep("*= 1 - 0.06 * #1 / (1 + Math.abs(0.06 * #1))", ARMOR);
		ENEMY_VULNERABILITY_PHY.dep("*= 1 - 0.06 * #1 / (1 + Math.abs(0.06 * #1))", ENEMY_ARMOR);
		
		ATTACK_SPEED_ALL.dep("= Math.min(" + (Constants.MAX_ATTACK_SPEED - 100) + ", Math.max(" + (Constants.MIN_ATTACK_SPEED - 100) + ", #0))");
		ATTACK_SPEED_HERO.dep("= Math.min(" + Constants.MAX_ATTACK_SPEED + ", Math.max(" + Constants.MIN_ATTACK_SPEED + ", #0 + #1 + #2))", AGI, ATTACK_SPEED_ALL);
		//ENEMY_ATTACK_SPEED.dep("= Math.min(" + Constants.MAX_ATTACK_SPEED + ", Math.max(" + Constants.MIN_ATTACK_SPEED + ", #0))");
		ATTACKS_PER_SECOND.dep("+= #1 / 100 / #2", ATTACK_SPEED_HERO, BASE_ATTACK_TIME);
		
		MOVEMENT_SPEED.dep("= Math.min(#1, Math.max(" + Constants.MIN_MOVEMENT_SPEED + ", #0 * (1 + #2)))", MOVEMENT_SPEED_MAX, MOVEMENT_SPEED_BONUS_MULT);
		
		VULNERABILITY_PHY.dep("*= #1 ? #2 : 1", TREAT_EVASION_AS_PHY_RESIST, ENEMY_HIT_CHANCE_HERO);
//		ENEMY_ARMOR.dep("*= #1", ENEMY_ARMOR_MULT);
		ENEMY_VULNERABILITY_MAG.dep("*= #1", ENEMY_VULNERABILITY_ALL);
		ENEMY_VULNERABILITY_PHY.dep("*= #1", ENEMY_VULNERABILITY_ALL);
		ENEMY_VULNERABILITY_PURE.dep("*= #1", ENEMY_VULNERABILITY_ALL);
		VULNERABILITY_MAG.dep("*= #1", VULNERABILITY_ALL);
		VULNERABILITY_PHY.dep("*= #1", VULNERABILITY_ALL);
		VULNERABILITY_PURE.dep("*= #1", VULNERABILITY_ALL);
		
		// buildings are usually immune to magic and pure damage, and are unaffected by cleave
		ENEMY_VULNERABILITY_MAG.dep("*= 1 - #1", ENEMY_IS_BUILDING);
		ENEMY_VULNERABILITY_PURE.dep("*= 1 - #1", ENEMY_IS_BUILDING);
		CLEAVE_MULT.dep("= #0 * (1 - #1)", ENEMY_IS_BUILDING);
		ENEMY_HAS_MANA.dep("*= 1 - #1", ENEMY_IS_BUILDING);
		
		// normal attack damage
		ATTACK_DAMAGE_BASE.dep("+= #1", PRIMARY_ATTRIBUTE_VALUE);
		ATTACK_DAMAGE_BASE_BONUS_MULT_HERO.dep("+= #1", ATTACK_DAMAGE_BASE_BONUS_MULT_ALL);
		ATTACK_DAMAGE_TOTAL.dep("= (#0 + #1 * (1 + #2)) * (1 + #3)", ATTACK_DAMAGE_BASE, ATTACK_DAMAGE_BASE_BONUS_MULT_HERO, ATTACK_DAMAGE_TOTAL_BONUS_MULT);
		ATTACK_DAMAGE_TOTAL_WITH_CRITS.dep("+= #1 * (1 + (crits(#2) - 1) * (1 - #3))", ATTACK_DAMAGE_TOTAL, CRIT, ENEMY_IS_BUILDING);
		ATTACK_DPS.dep("= #1 * #2 * #3", ATTACK_DAMAGE_TOTAL_WITH_CRITS, ATTACKS_PER_SECOND, HIT_CHANCE_HERO);
		ATTACK_DPS_AFTER_REDUCTIONS.dep("= #1 * #2 * #3", ATTACK_DPS, ENEMY_VULNERABILITY_PHY, ENEMY_VULNERABILITY_PHY_HERO);
		
		// lifesteal
		HP_REGEN.dep("+= #1 * #2", LIFESTEAL, ATTACK_DPS_AFTER_REDUCTIONS);
		
		// cleave damage depends on attack damage and crits, and bypasses enemy armor (but not armor type)
		DPS_PHY_TOTAL_AFTER_REDUCTIONS.dep("+= #1 * #2 * #3 * #4 * (1 + #5) * #6", IS_MELEE, CLEAVE_MULT, ATTACK_DPS, NUM_ADDITIONAL_ENEMIES, SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_PHY_HERO);
		
		// bonus damage
		for (final String damageType : new String[] {"MAG", "PHY", "PURE"}) {
			for (final boolean onAttacked : new boolean[] {false, true}) {
				final String suffix = (onAttacked ? "_ON_ATTACKED" : "");
				final String var = "BONUS_DPS_" + damageType + suffix;
				preCalc("var " + var);
				getAttribute("BONUS_DPS_" + damageType + suffix + "_SINGLE").dep("= (" + var + " = bonus_damage(this, #1, #2, #3, #4, #5)).single", getAttribute("BONUS_DAMAGE_" + damageType + suffix), LIFESTEAL, NUM_ADDITIONAL_ENEMIES, onAttacked ? ENEMY_ATTACKS_PER_SECOND : ATTACKS_PER_SECOND, onAttacked ? ENEMY_HIT_CHANCE_HERO : HIT_CHANCE_HERO);
				getAttribute("BONUS_DPS_" + damageType + suffix + "_REST").dep("= " + var + ".rest; // depends on #1", getAttribute("BONUS_DPS_" + damageType + suffix + "_SINGLE"));
			}
		}
		
		// other DPS
		SPELL_DPS_MAG_SINGLE.dep("+= #1", SPELL_DPS_MAG_AOE);
		SPELL_DPS_PHY_SINGLE.dep("+= #1", SPELL_DPS_PHY_AOE);
		SPELL_DPS_PURE_SINGLE.dep("+= #1", SPELL_DPS_PURE_AOE);
		
		// DPS reductions
		DPS_MAG_SINGLE_AFTER_REDUCTIONS.dep("+= (1 + #1) * #2 * (#3 + (#4 + #5) * (1 - #6))", SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_MAG, SPELL_DPS_MAG_SINGLE, BONUS_DPS_MAG_SINGLE, BONUS_DPS_MAG_ON_ATTACKED_SINGLE, ENEMY_IS_BUILDING);
		DPS_MAG_TOTAL_AFTER_REDUCTIONS.dep("+= #1 + (1 + #2) * #3 * (#4 * #5 + (#6 + #7) * (1 - #8))", DPS_MAG_SINGLE_AFTER_REDUCTIONS, SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_MAG, SPELL_DPS_MAG_AOE, NUM_ADDITIONAL_ENEMIES, BONUS_DPS_MAG_REST, BONUS_DPS_MAG_ON_ATTACKED_REST, ENEMY_IS_BUILDING);
		
		DPS_PHY_SINGLE_AFTER_REDUCTIONS.dep("+= (1 + #1) * #2 * (#3 + (#4 + #5) * (1 - #6))", SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_PHY, SPELL_DPS_PHY_SINGLE, BONUS_DPS_PHY_SINGLE, BONUS_DPS_PHY_ON_ATTACKED_SINGLE, ENEMY_IS_BUILDING);
		DPS_PHY_SINGLE_AFTER_REDUCTIONS.dep("+= #1", ATTACK_DPS_AFTER_REDUCTIONS);
		DPS_PHY_TOTAL_AFTER_REDUCTIONS.dep("+= #1 + (1 + #2) * #3 * (#4 * #5 + (#6 + #7) * (1 - #8))", DPS_PHY_SINGLE_AFTER_REDUCTIONS, SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_PHY, SPELL_DPS_PHY_AOE, NUM_ADDITIONAL_ENEMIES, BONUS_DPS_PHY_REST, BONUS_DPS_PHY_ON_ATTACKED_REST, ENEMY_IS_BUILDING);
		
		DPS_PURE_SINGLE_AFTER_REDUCTIONS.dep("+= (1 + #1) * #2 * (#3 + (#4 + #5) * (1 - #6))", SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_PURE, SPELL_DPS_PURE_SINGLE, BONUS_DPS_PURE_SINGLE, BONUS_DPS_PURE_ON_ATTACKED_SINGLE, ENEMY_IS_BUILDING);
		DPS_PURE_TOTAL_AFTER_REDUCTIONS.dep("+= #1 + (1 + #2) * #3 * (#4 * #5 + (#6 + #7) * (1 - #8))", DPS_PURE_SINGLE_AFTER_REDUCTIONS, SPELL_DAMAGE_BONUS_MULT, ENEMY_VULNERABILITY_PURE, SPELL_DPS_PURE_AOE, NUM_ADDITIONAL_ENEMIES, BONUS_DPS_PURE_REST, BONUS_DPS_PURE_ON_ATTACKED_REST, ENEMY_IS_BUILDING);
		
		// DPS sums
		DPS_SINGLE_AFTER_REDUCTIONS.dep("+= #1 + #2 + #3", DPS_MAG_SINGLE_AFTER_REDUCTIONS, DPS_PHY_SINGLE_AFTER_REDUCTIONS, DPS_PURE_SINGLE_AFTER_REDUCTIONS);
		DPS_TOTAL_AFTER_REDUCTIONS.dep("+= #1 + #2 + #3", DPS_MAG_TOTAL_AFTER_REDUCTIONS, DPS_PHY_TOTAL_AFTER_REDUCTIONS, DPS_PURE_TOTAL_AFTER_REDUCTIONS);
		
		// effective HP
		EFF_HP_MAG.dep("+= #1 / #2", HP, VULNERABILITY_MAG);
		EFF_HP_PHY.dep("+= #1 / #2", HP, VULNERABILITY_PHY);
		
		// true strike
		HIT_CHANCE_ALL.dep("= 1 - (1 - #0) * (1 - #1)", TRUE_STRIKE_CHANCE_ALL);
		HIT_CHANCE_HERO.dep("= 1 - (1 - #0 * #2) * (1 - #1)", TRUE_STRIKE_CHANCE_HERO, HIT_CHANCE_ALL);
		
		// for quell
		ENEMY_IS_CREEP_AND_HERO_IS_MELEE.dep("= #1 * #2", ENEMY_IS_CREEP, IS_MELEE);
		ENEMY_IS_CREEP_AND_HERO_IS_RANGED.dep("= #1 * #2", ENEMY_IS_CREEP, IS_RANGED);
		
		// armor types
		//ENEMY_VULNERABILITY_PHY_HERO.dep("*= 1 - (1 - 1) * #1", ENEMY_IS_HERO);
		ENEMY_VULNERABILITY_PHY_BASIC.dep("*= 1 - (1 - 0.75) * #1", ENEMY_IS_HERO);
		ENEMY_VULNERABILITY_PHY_PIERCE.dep("*= 1 - (1 - 0.5) * #1", ENEMY_IS_HERO);
		//ENEMY_VULNERABILITY_PHY_HERO.dep("*= 1 - (1 - 1) * #1", ENEMY_IS_CREEP);
		//ENEMY_VULNERABILITY_PHY_BASIC.dep("*= 1 - (1 - 1) * #1", ENEMY_IS_CREEP);
		ENEMY_VULNERABILITY_PHY_PIERCE.dep("*= 1 - (1 - 1.5) * #1", ENEMY_IS_CREEP);
		ENEMY_VULNERABILITY_PHY_HERO.dep("*= 1 - (1 - 0.5) * #1", ENEMY_IS_BUILDING);
		ENEMY_VULNERABILITY_PHY_BASIC.dep("*= 1 - (1 - 0.7) * #1", ENEMY_IS_BUILDING);
		ENEMY_VULNERABILITY_PHY_PIERCE.dep("*= 1 - (1 - 0.35) * #1", ENEMY_IS_BUILDING);
		
		// summons
		DPS_PHY_SINGLE_AFTER_REDUCTIONS.dep("+= summons(#1, #2, #3, #4, #5, #6, #7, #8)", SUMMON, ATTACK_SPEED_ALL, ATTACK_DAMAGE_BASE_BONUS_MULT_ALL, HIT_CHANCE_ALL, ENEMY_VULNERABILITY_PHY, ENEMY_VULNERABILITY_PHY_BASIC, ENEMY_VULNERABILITY_PHY_HERO, ENEMY_VULNERABILITY_PHY_PIERCE);
		
		// multiplications
		for (final NonNullPair<Attribute, Attribute> p : usedMultiplications) {
			if (p.getFirst().path.length == 1)
				p.getFirst().dep("+= (((this.mults || {})." + p.getFirst().path[0] + " || {})." + p.getSecond().path[0] + " || 0) * #1", p.getSecond());
			else
				// first[0]: [{mults: {first[1]: {second: 0.5}}}]
				p.getFirst().parent.dep("_.#1 = (_.#1 || " + p.getFirst().defaultValue + ") + (((_.mults || {}).#1 || {})." + p.getSecond().path[0] + " || 0) * #2", p.getFirst(), p.getSecond());
		}
		
		// final stuff
		finalCalc("var orchid_dps = #1 * #2 * #3 * (1 + #4)", ORCHID_MULT, DPS_SINGLE_AFTER_REDUCTIONS, ENEMY_VULNERABILITY_MAG, SPELL_DAMAGE_BONUS_MULT);
		finalCalc("#1 += orchid_dps", DPS_MAG_SINGLE_AFTER_REDUCTIONS);
		finalCalc("#1 += orchid_dps", DPS_MAG_TOTAL_AFTER_REDUCTIONS);
		finalCalc("#1 += orchid_dps", DPS_SINGLE_AFTER_REDUCTIONS);
		finalCalc("#1 += orchid_dps", DPS_TOTAL_AFTER_REDUCTIONS);
		
	}
	
	private final static List<String> finalCalcs = new ArrayList<>();
	
	private final static void finalCalc(final @NonNull String calc, final Attribute... attributes) {
		finalCalcs.add(StringUtils.replaceAll(calc, "#([0-9]+)", (final Matcher m) -> "this." + attributes[Integer.parseInt(m.group(1)) - 1].path[0]));
	}
	
	private final static List<String> preCalcs = new ArrayList<>();
	
	private final static void preCalc(final @NonNull String calc, final Attribute... attributes) {
		preCalcs.add(StringUtils.replaceAll(calc, "#([0-9]+)", (final Matcher m) -> "this." + attributes[Integer.parseInt(m.group(1)) - 1].path[0]));
	}
	
	public final static void writeJS(final String classname, final File file) throws IOException {
		calculations();
		
		try (Writer out = new FileWriterWithEncoding(file, StandardCharsets.UTF_8)) {
			out.write("\"use strict\";\n");
			out.write("\n");
			out.write("// generated on " + new Date().toString() + "\n");
			out.write("\n");
			out.write("function " + classname + "(data) {\n");
			for (final Attribute attr : allAttributes) {
				if (attr.path.length > 1)
					continue;
//					out.write("	this." + attr.path[0] + " = [{}];\n");
				out.write("	this." + attr.pathToFirst() + " = " + (attr.defaultValue instanceof String ? '"' + (String) attr.defaultValue + '"' : attr.defaultValue) + ";\n");
			}
			out.write("	\n");
			out.write("	if (data) {\n");
			out.write("		for (var i in data)\n");
			out.write("			this[i] = data[i];\n");
			out.write("	}\n");
			out.write("};\n");
			out.write("\n");
			out.write(classname + ".prototype.addToThis = function(other) {\n");
			for (final Attribute attr : allAttributes) {
				if (attr.path.length > 1)
					continue;
				if (attr.isParent)
					out.write("	this." + attr.path[0] + " = this." + attr.path[0] + ".concat(cloneArray(other." + attr.path[0] + "));\n");
				else if (attr.addBehaviour != null)
					out.write("	" + attr.addBehaviour.replace("#", attr.path[0]) + ";\n");
			}
			out.write("	this.mults = addObjects(this.mults, other.mults);\n");
			out.write("	for (var attr in other.mults) {\n");
			out.write("		if (other.stacks && other.mults[attr].stacks)\n");
			out.write("			this[attr] += other.stacks * other.mults[attr].stacks;\n");
			out.write("	}\n");
			out.write("};\n");
			out.write("\n");
			out.write(classname + ".prototype.calculate = function() {\n");
			
			final List<Attribute> depSorted = new ArrayList<>(), depUnsorted = new ArrayList<>(allAttributes);
			Iterator<Attribute> iter = depUnsorted.iterator();
			while (iter.hasNext()) { // first remove all attributes that don't depend on anything - makes the resulting ordering much nicer.
				final Attribute attr = iter.next();
				if (attr.dependencies.isEmpty()) {
					depSorted.add(attr);
					iter.remove();
				}
			}
			int unsortedSize;
			do {
				unsortedSize = depUnsorted.size();
				iter = depUnsorted.iterator();
				while (iter.hasNext()) {
					final Attribute attr = iter.next();
					if (depSorted.containsAll(attr.dependencies)) {
						depSorted.add(attr);
						iter.remove();
						break;
					}
				}
			} while (unsortedSize != depUnsorted.size());
			if (!depUnsorted.isEmpty()) {
				System.out.println("Cyclic dependencies: " + depUnsorted);
				for (final Attribute a : depUnsorted) {
					System.out.println(a + ": " + a.dependencies);
				}
			}
			for (final String preCalc : preCalcs)
				out.write("	" + preCalc + ";\n");
			out.write("	\n");
			for (final Attribute attr : depSorted) {
				if (attr.calculation == null)
					continue;
				assert attr.path.length == 1 : attr.path[0] + "." + attr.path[1];
				if (attr.isParent)
					out.write("	this." + attr.path[0] + ".forEach(function(_) {" + attr.calculation + ";}, this);\n");
				else
					out.write("	this." + attr.path[0] + " " + attr.calculation.replace("#0", "this." + attr.path[0]) + ";\n");
			}
			out.write("	\n");
			for (final String finalCalc : finalCalcs)
				out.write("	" + finalCalc + ";\n");
			out.write("};\n");
		}
	}
	
}
