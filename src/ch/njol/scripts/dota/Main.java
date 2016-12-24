package ch.njol.scripts.dota;

import static ch.njol.scripts.dota.Attribute.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	
	public final static File outputDir = new File("../njol.ch v2.0/dota2/");
	
	public static DotaData names, itemsData, heroesData, abilitiesData, unitsData;
	
	public static void main(final String[] args) throws IOException {
		
		System.out.println("Working...");
		
		names = new DotaData("dota_english.txt", StandardCharsets.UTF_16LE).getSection("Tokens");
		
		itemsData = new DotaData("items.txt", StandardCharsets.UTF_8).getSection("DOTAAbilities");
		heroesData = new DotaData("npc_heroes.txt", StandardCharsets.UTF_8).getSection("DOTAHeroes");
		abilitiesData = new DotaData("npc_abilities.txt", StandardCharsets.UTF_8).getSection("DOTAAbilities");
		unitsData = new DotaData("npc_units.txt", StandardCharsets.UTF_8).getSection("DOTAUnits");
		
		Abilities.execute(); // fills Abilities.abilityAttributes
		Items.execute();
		Heroes.execute();
		
		Attribute.writeJS("Env", new File(outputDir, "Env.js"));
		
		System.out.println("Done!");
		
	}
	
	public final static double getLevelled(final String s, final int level) {
		if (s == null || s.isEmpty())
			return 0;
		final String[] vs = s.split("\\s+");
		return Double.parseDouble(vs[Math.min(level - 1, vs.length - 1)]);
	}
	
	public final static Object cloneJSON(final Object o) {
		if (o instanceof JSONObject)
			return cloneJO((JSONObject) o);
		if (o instanceof JSONArray)
			return cloneJA((JSONArray) o);
		return o;
	}
	
	public final static JSONObject cloneJO(final JSONObject o) {
		final JSONObject r = new JSONObject();
		for (final String key : o.keySet()) {
			r.put(key, cloneJSON(o.get(key)));
		}
		return r;
	}
	
	public final static JSONArray cloneJA(final JSONArray a) {
		final JSONArray r = new JSONArray();
		for (final Object o : a)
			r.put(cloneJSON(o));
		return r;
	}
	
	public final static void writeEnvs(final JSONObject env, final String name, final String fileName) throws IOException {
		final Writer out = new FileWriterWithEncoding(new File(outputDir, fileName), StandardCharsets.UTF_8);
		out.write("\"use strict\";\n");
		out.write("\n");
		out.write("// generated on " + new Date().toString() + "\n");
		out.write("\n");
		out.write("var " + name + " = {\n");
		final String[] keys = JSONObject.getNames(env);
		Arrays.sort(keys, (Comparator<String>) (final String a, final String b) -> NAME.get(env.getJSONObject(a), "").compareTo(NAME.get(env.getJSONObject(b), "")));
		for (final String key : keys) {
			out.write("    " + key + ": new Env(");
			env.getJSONObject(key).write(out, 4, 4);
			out.write(")" + (key == keys[keys.length - 1] ? "" : ",") + "\n");
		}
		out.write("};");
		out.close();
		
	}
	
}
