package ch.njol.scripts.dota;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.njol.util.StringUtils;

public final class DotaData {
	
	private final static Pattern LINE = Pattern.compile("\\s*(.*?)\\s*(//.*)?");
	private final static Pattern SECTION_START = Pattern.compile("\"(.*)\"");
	private final static Pattern SECTION_END = Pattern.compile("\\}");
	private final static Pattern KEY_VALUE_PAIR = Pattern.compile("\"(.*)\"\\s*\"(.*)\"");
	
	private final Map<String, Object> data = new LinkedHashMap<>();
	
	private DotaData() {}
	
	public DotaData(final File file, final Charset charset) throws IOException {
		final Deque<DotaData> currentSections = new ArrayDeque<>();
		currentSections.addLast(this);
		try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
			String line;
			while ((line = r.readLine()) != null) {
				Matcher m = LINE.matcher(line);
				m.matches();// always true, since the regex matches any string with '.*'
				line = m.group(1);
				if ((m = KEY_VALUE_PAIR.matcher(line)).matches()) {
					currentSections.getLast().data.put(m.group(1), m.group(2));
				} else if ((m = SECTION_START.matcher(line)).matches()) {
					final DotaData section = new DotaData();
					currentSections.getLast().data.put(m.group(1), section);
					currentSections.addLast(section);
				} else if ((m = SECTION_END.matcher(line)).matches()) {
					currentSections.removeLast();
				} else {
					// empty line or invalid, ignore
				}
			}
		}
	}
	
	public Object get(final String... path) {
		Object current = this;
		for (int i = 0; i < path.length; i++) {
			if (!(current instanceof DotaData))
				return null;
			current = ((DotaData) current).data.get(path[i]);
		}
		return current;
	}
	
	public DotaData getSection(final String... path) {
		final Object value = get(path);
		if (value instanceof DotaData)
			return (DotaData) value;
		return null;
	}
	
	public String getValue(final String... path) {
		final Object value = get(path);
		if (value instanceof String)
			return (String) value;
		return null;
	}
	
	public String getString(final String... path) {
		final Object value = get(path);
		if (value instanceof String)
			return (String) value;
		return "";
	}
	
	public double getDouble(final String... path) {
		return getDouble(Double.NaN, path);
	}
	
	public double getDouble(final double def, final String... path) {
		try {
			final Object value = get(path);
			if (value instanceof String)
				return Double.parseDouble((String) value);
		} catch (final NumberFormatException e) {
			return def;
		}
		return def;
	}
	
	public void forEach(final BiConsumer<String, Object> function) {
		data.forEach(function);
	}
	
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		toString(b, 0);
		return b.toString();
	}
	
	private void toString(final StringBuilder b, final int indent) {
		data.forEach((final String name, final Object value) -> {
			b.append(StringUtils.multiply('\t', indent));
			if (value instanceof DotaData) {
				b.append('"').append(name).append('"').append('\n');
				b.append(StringUtils.multiply('\t', indent)).append('{').append('\n');
				((DotaData) value).toString(b, indent + 1);
				b.append(StringUtils.multiply('\t', indent)).append('}');
			} else {
				b.append('"').append(name).append('"').append('\t').append('"').append(value).append('"');
			}
			b.append('\n');
		});
	}
	
}
