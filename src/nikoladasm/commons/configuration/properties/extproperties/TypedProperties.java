/*
 *  Common Configuration
 *  Copyright (C) 2016  Nikolay Platov
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nikoladasm.commons.configuration.properties.extproperties;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.ConverterException;
import nikoladasm.commons.configuration.properties.converters.Converters;

public class TypedProperties extends PrepProperties {
	private static final long serialVersionUID = 1L;

	protected Converters converters = new Converters();
	protected String componentsDelimiter;
	protected String keyValueDelimiter;
	
	protected <T> T convert (Class<?> clazz, Class<?>[] genericParameters, String value) {
		Converter<T> converter = converters.converter(clazz, genericParameters, componentsDelimiter, keyValueDelimiter);
		try {
			return converter.convert(value);
		} catch (Exception e) {
			throw new ConverterException("Conversion error");
		}
	}
	
	public Byte getByte(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Byte.class, null, value);
	}

	public Short getShort(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Short.class, null, value);
	}

	public Integer getInteger(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Integer.class, null, value);
	}

	public Long getLong(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Long.class, null, value);
	}

	public Float getFloat(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Float.class, null, value);
	}

	public Double getDouble(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Double.class, null, value);
	}

	public Character getCharacter(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Character.class, null, value);
	}

	public Boolean getBoolean(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Boolean.class, null, value);
	}

	public BigDecimal getBigDecimal(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(BigDecimal.class, null, value);
	}

	public BigInteger getBigInteger(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(BigInteger.class, null, value);
	}

	public Charset getCharset(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Charset.class, null, value);
	}

	public File getFile(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(File.class, null, value);
	}

	public Locale getLocale(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Locale.class, null, value);
	}

	public Path getPath(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Path.class, null, value);
	}

	public URI getURI(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(URI.class, null, value);
	}

	public URL getURL(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(URL.class, null, value);
	}

	public <T> T getArray(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(clazz, null, value);
	}

	public <T> T getEnum(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(clazz, null, value);
	}

	public AtomicBoolean getAtomicBoolean(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(AtomicBoolean.class, null, value);
	}

	public AtomicInteger getAtomicInteger(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(AtomicInteger.class, null, value);
	}

	public AtomicLong getAtomicLong(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(AtomicLong.class, null, value);
	}

	public Pattern getPattern(String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Pattern.class, null, value);
	}

	public <T> AtomicReference<T> getAtomicReference(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(AtomicReference.class, new Class<?>[]{clazz}, value);
	}

	public <T> List<T> getList(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(List.class, new Class<?>[]{clazz}, value);
	}

	public <T> Collection<T> getCollection(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Collection.class, new Class<?>[]{clazz}, value);
	}

	public <T> Set<T> getSet(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Set.class, new Class<?>[]{clazz}, value);
	}

	public <T> ArrayList<T> getArrayList(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(ArrayList.class, new Class<?>[]{clazz}, value);
	}

	public <T> TreeSet<T> getTreeSet(Class<T> clazz, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(TreeSet.class, new Class<?>[]{clazz}, value);
	}

	public <K,V> Map<K,V> getMap(Class<K> clazz1, Class<V> clazz2, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(Map.class, new Class<?>[]{clazz1,clazz2}, value);
	}

	public <K,V> HashMap<K,V> getHashMap(Class<K> clazz1, Class<V> clazz2, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(HashMap.class, new Class<?>[]{clazz1,clazz2}, value);
	}

	public <K,V> TreeMap<K,V> getTreeMap(Class<K> clazz1, Class<V> clazz2, String key) {
		String value = this.getProperty(key);
		if (value == null) return null;
		return convert(TreeMap.class, new Class<?>[]{clazz1,clazz2}, value);
	}
}
