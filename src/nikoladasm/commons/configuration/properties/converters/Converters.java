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

package nikoladasm.commons.configuration.properties.converters;

import java.util.List;
import java.util.Locale;
import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class Converters {

	protected static interface genericConverterBuilder {
		Converter<?> build(Converter<?>[] childConverters, String[] delimiters);
	}
	
	public static final String DEFAULT_COMPONENTS_DELIMITER = ",";
	public static final String DEFAULT_KEY_VALUE_DELIMITER = ":";
	
	protected Map<Class<?>,Converter<?>> nonGenericTypeConverters;
	protected String componentsDelimiter = DEFAULT_COMPONENTS_DELIMITER;
	protected Map<Class<?>,genericConverterBuilder> oneTypeParameterGenericConvertersBuilders;
	protected Map<Class<?>,genericConverterBuilder> twoTypeParametersGenericConvertersBuilders;
	protected String keyValueDelimiter = DEFAULT_KEY_VALUE_DELIMITER;
	
	public Converters() {
		nonGenericTypeConverters = new HashMap<>();
		oneTypeParameterGenericConvertersBuilders = new HashMap<>();
		twoTypeParametersGenericConvertersBuilders = new HashMap<>();
		init();
	}
	
	public Converters newInstance() {
		return new Converters();
	}
	
	public Converters componentsDelimiter(String delimiter) {
		componentsDelimiter = delimiter;
		return this;
	}
	
	public String componentsDelimiter() {
		return componentsDelimiter;
	}
	
	public Converters keyValueDelimiter(String delimiter) {
		keyValueDelimiter = delimiter;
		return this;
	}
	
	public String keyValueDelimiter() {
		return keyValueDelimiter;
	}
	
	public <T> Converter<T> converter(Class<?> clazz, Class<?>[] genericParameters) {
		return converter(clazz, genericParameters, componentsDelimiter, keyValueDelimiter);
	}

	@SuppressWarnings("unchecked")
	public <T> Converter<T> converter(Class<?> clazz, Class<?>[] genericParameters, String componentsDelimiter, String keyValueDelimiter) {
		componentsDelimiter = (componentsDelimiter == null) ?
			this.componentsDelimiter : componentsDelimiter;
		keyValueDelimiter = (keyValueDelimiter == null) ?
			this.keyValueDelimiter : keyValueDelimiter;
		if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			Converter<?> childConverter = nonGenericTypeConverters.get(componentType);
			if (childConverter == null) return null;
			return (Converter<T>) arrayConverter(componentType, childConverter, componentsDelimiter);
		}
		if (clazz.isEnum()) {
			return (Converter<T>) enumConverter((Class<? extends Enum<?>>) clazz);
		}
		Converter<T> converter = (Converter<T>) nonGenericTypeConverters.get(clazz);
		if (converter != null) return converter;
		if (genericParameters == null) return null;
		genericConverterBuilder converterBuilder = oneTypeParameterGenericConvertersBuilders.get(clazz);
		if (converterBuilder != null && genericParameters.length == 1) {
			Converter<?> childConverter = nonGenericTypeConverters.get(genericParameters[0]);
			if (childConverter == null) return null;
			return (Converter<T>) converterBuilder.build(new Converter<?>[]{childConverter}, new String[]{componentsDelimiter});
		}
		converterBuilder = twoTypeParametersGenericConvertersBuilders.get(clazz);
		if (converterBuilder != null && genericParameters.length == 2) {
			Converter<?> firstChildConverter = nonGenericTypeConverters.get(genericParameters[0]);
			if (firstChildConverter == null) return null;
			Converter<?> secondChildConverter = nonGenericTypeConverters.get(genericParameters[1]);
			if (secondChildConverter == null) return null;
			return (Converter<T>) converterBuilder.build(new Converter<?>[]{firstChildConverter, secondChildConverter}, new String[]{componentsDelimiter, keyValueDelimiter});
		}
		return converter;
	}

	protected void init() {
		nonGenericTypeConverters.put(Byte.class, byteConverter());
		nonGenericTypeConverters.put(Short.class, shortConverter());
		nonGenericTypeConverters.put(Integer.class, integerConverter());
		nonGenericTypeConverters.put(Long.class, longConverter());
		nonGenericTypeConverters.put(Float.class, floatConverter());
		nonGenericTypeConverters.put(Double.class, doubleConverter());
		nonGenericTypeConverters.put(Character.class, characterConverter());
		nonGenericTypeConverters.put(Boolean.class, booleanConverter());
		nonGenericTypeConverters.put(Byte.TYPE, byteConverter());
		nonGenericTypeConverters.put(Short.TYPE, shortConverter());
		nonGenericTypeConverters.put(Integer.TYPE, integerConverter());
		nonGenericTypeConverters.put(Long.TYPE, longConverter());
		nonGenericTypeConverters.put(Float.TYPE, floatConverter());
		nonGenericTypeConverters.put(Double.TYPE, doubleConverter());
		nonGenericTypeConverters.put(Character.TYPE, characterConverter());
		nonGenericTypeConverters.put(Boolean.TYPE, booleanConverter());
		nonGenericTypeConverters.put(String.class, stringConverter());
		nonGenericTypeConverters.put(BigDecimal.class, bigDecimalConverter());
		nonGenericTypeConverters.put(BigInteger.class, bigIntegerConverter());
		nonGenericTypeConverters.put(AtomicBoolean.class, atomicBooleanConverter());
		nonGenericTypeConverters.put(AtomicInteger.class, atomicIntegerConverter());
		nonGenericTypeConverters.put(AtomicLong.class, atomicLongConverter());
		nonGenericTypeConverters.put(Charset.class, charsetConverter());
		nonGenericTypeConverters.put(File.class, fileConverter());
		nonGenericTypeConverters.put(Locale.class, localeConverter());
		nonGenericTypeConverters.put(Path.class, pathConverter());
		nonGenericTypeConverters.put(Pattern.class, patternConverter());
		nonGenericTypeConverters.put(URI.class, uriConverter());
		nonGenericTypeConverters.put(URL.class, urlConverter());
		
		oneTypeParameterGenericConvertersBuilders.put(AtomicReference.class, genericAtomicReferenceConverterBuilder());		
		oneTypeParameterGenericConvertersBuilders.put(List.class, genericListConverterBuilder());		
		oneTypeParameterGenericConvertersBuilders.put(Collection.class, genericCollectionConverterBuilder());		
		oneTypeParameterGenericConvertersBuilders.put(Set.class, genericSetConverterBuilder());		
		oneTypeParameterGenericConvertersBuilders.put(HashSet.class, genericHashSetConverterBuilder());	
		oneTypeParameterGenericConvertersBuilders.put(LinkedList.class, genericListConverterBuilder());
		oneTypeParameterGenericConvertersBuilders.put(ArrayList.class, genericArrayListConverterBuilder());
		oneTypeParameterGenericConvertersBuilders.put(TreeSet.class, genericTreeSetConverterBuilder());

		twoTypeParametersGenericConvertersBuilders.put(Map.class, genericMapConverterBuilder());
		twoTypeParametersGenericConvertersBuilders.put(HashMap.class, genericHashMapConverterBuilder());
		twoTypeParametersGenericConvertersBuilders.put(TreeMap.class, genericTreeMapConverterBuilder());
	}
	
	protected Converter<Byte> byteConverter() {
		return (source) -> Byte.valueOf(source.trim());
	}

	protected Converter<Short> shortConverter() {
		return (source) -> Short.valueOf(source.trim());
	}

	protected Converter<Integer> integerConverter() {
		return (source) -> Integer.valueOf(source.trim());
	}

	protected Converter<Long> longConverter() {
		return (source) -> Long.valueOf(source.trim());
	}

	protected Converter<Float> floatConverter() {
		return (source) -> Float.valueOf(source.trim());
	}

	protected Converter<Double> doubleConverter() {
		return (source) -> Double.valueOf(source.trim());
	}

	protected Converter<Character> characterConverter() {
		return (source) -> {
			if (source == null || source.length() != 1)
				throw new ConverterException("Incorrect character string");
			return source.charAt(0);
		};
	}

	protected Converter<Boolean> booleanConverter() {
		return (source) -> Boolean.valueOf(source.trim());
	}

	protected Converter<String> stringConverter() {
		return (source) -> source;
	}

	protected Converter<BigDecimal> bigDecimalConverter() {
		return (source) -> {
			DecimalFormat format = new DecimalFormat();
			format.setParseBigDecimal(true);
			return (BigDecimal) format.parse(source.trim());
		};
	}

	protected Converter<BigInteger> bigIntegerConverter() {
		return (source) -> new BigInteger(source.trim());
	}

	protected Converter<AtomicBoolean> atomicBooleanConverter() {
		return (source) -> new AtomicBoolean(Boolean.valueOf(source.trim()));
	}

	protected Converter<AtomicInteger> atomicIntegerConverter() {
		return (source) -> new AtomicInteger(Integer.valueOf(source.trim()));
	}

	protected Converter<AtomicLong> atomicLongConverter() {
		return (source) -> new AtomicLong(Long.valueOf(source.trim()));
	}

	protected Converter<Charset> charsetConverter() {
		return (source) -> Charset.forName(source.trim());
	}

	protected Converter<File> fileConverter() {
		return (source) -> new File(source.trim());
	}

	protected Converter<Locale> localeConverter() {
		return (source) -> Locale.forLanguageTag(source.trim());
	}

	protected Converter<Path> pathConverter() {
		return (source) -> Paths.get(source.trim());
	}

	protected Converter<Pattern> patternConverter() {
		return (source) -> Pattern.compile(source);
	}

	protected Converter<URI> uriConverter() {
		return (source) -> new URI(source.trim());
	}

	protected Converter<URL> urlConverter() {
		return (source) -> new URL(source.trim());
	}

	protected Converter<Object> arrayConverter(Class<?> componentType, Converter<?> converter, String componentsDelimiter) {
		return (source) -> {
			String[] components = StringSplitter.splitComponents(source, componentsDelimiter).components();
			if (components.length == 1 && components[0].trim().isEmpty())
				return Array.newInstance(componentType, 0);
			Object result = Array.newInstance(componentType, components.length);
			for (int i=0; i < components.length; i++)
				Array.set(result, i, converter.convert(components[i]));
			return result;
		};
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Converter<? extends Enum> enumConverter(Class<? extends Enum> clazz) {
		return (source) -> Enum.valueOf(clazz, source.trim().toUpperCase());
	}
	
	protected genericConverterBuilder genericAtomicReferenceConverterBuilder() {
		return (childConverters, splitters) -> genericAtomicReferenceConverter(childConverters[0]);
	}
	
	protected genericConverterBuilder genericListConverterBuilder() {
		return (childConverters, splitters) -> genericListConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericCollectionConverterBuilder() {
		return (childConverters, splitters) -> genericCollectionConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericSetConverterBuilder() {
		return (childConverters, splitters) -> genericSetConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericHashSetConverterBuilder() {
		return (childConverters, splitters) -> genericSetConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericArrayListConverterBuilder() {
		return (childConverters, splitters) -> genericArrayListConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericTreeSetConverterBuilder() {
		return (childConverters, splitters) -> genericTreeSetConverter(childConverters[0], splitters[0]);
	}
	
	protected genericConverterBuilder genericMapConverterBuilder() {
		return (childConverters, splitters) -> genericMapConverter(childConverters[0], childConverters[1], splitters[0], splitters[1]);
	}
	
	protected genericConverterBuilder genericHashMapConverterBuilder() {
		return (childConverters, splitters) -> genericHashMapConverter(childConverters[0], childConverters[1], splitters[0], splitters[1]);
	}
	
	protected genericConverterBuilder genericTreeMapConverterBuilder() {
		return (childConverters, splitters) -> genericTreeMapConverter(childConverters[0], childConverters[1], splitters[0], splitters[1]);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Converter<AtomicReference<T>> genericAtomicReferenceConverter(Converter<?> childConverter) {
		return (source) -> new AtomicReference<>((T) childConverter.convert(source));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Converter<List<T>> genericListConverter(Converter<?> childConverter, String componentsDelimiter) {
		return (source) -> (List<T>) componentsCollection(source, childConverter, componentsDelimiter);
	}
	
	protected <T> Converter<Collection<T>> genericCollectionConverter(Converter<?> childConverter, String componentsDelimiter) {
		return (source) -> componentsCollection(source, childConverter, componentsDelimiter);
	}
	
	protected <T> Converter<Set<T>> genericSetConverter(Converter<?> childConverter, String componentsDelimiter) {
		return (source) -> new HashSet<>(componentsCollection(source, childConverter, componentsDelimiter));
	}
	
	protected <T> Converter<ArrayList<T>> genericArrayListConverter(Converter<?> childConverter, String componentsDelimiter) {
		return (source) -> new ArrayList<>(componentsCollection(source, childConverter, componentsDelimiter));
	}
	
	protected <T> Converter<TreeSet<T>> genericTreeSetConverter(Converter<?> childConverter, String componentsDelimiter) {
		return (source) -> new TreeSet<>(componentsCollection(source, childConverter, componentsDelimiter));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Collection<T> componentsCollection(String source, Converter<?> childConverter, String componentsDelimiter) throws Exception {
		String[] components = StringSplitter.splitComponents(source, componentsDelimiter).components();
		if (components.length == 1 && components[0].trim().isEmpty())
			return new LinkedList<>();
		List<T> result = new LinkedList<>();
		for (String component : components)
			result.add((T) childConverter.convert(component));
		return result;
	}
	
	protected <K,V> Converter<Map<K,V>> genericMapConverter(Converter<?> firstChildConverter, Converter<?> secondChildConverter, String componentsDelimiter, String keyValueDelimiter) {
		return (source) -> componentsMap(source, firstChildConverter, secondChildConverter, componentsDelimiter, keyValueDelimiter);
	}
	
	@SuppressWarnings("unchecked")
	protected <K,V> Converter<HashMap<K,V>> genericHashMapConverter(Converter<?> firstChildConverter, Converter<?> secondChildConverter, String componentsDelimiter, String keyValueDelimiter) {
		return (source) -> (HashMap<K,V>) componentsMap(source, firstChildConverter, secondChildConverter, componentsDelimiter, keyValueDelimiter);
	}
	
	protected <K,V> Converter<TreeMap<K,V>> genericTreeMapConverter(Converter<?> firstChildConverter, Converter<?> secondChildConverter, String componentsDelimiter, String keyValueDelimiter) {
		return (source) -> new TreeMap<>(componentsMap(source, firstChildConverter, secondChildConverter, componentsDelimiter, keyValueDelimiter));
	}
	
	@SuppressWarnings("unchecked")
	protected <K,V> Map<K,V> componentsMap(String source, Converter<?> firstChildConverter,Converter<?> secondChildConverter, String componentsDelimiter, String keyValueDelimiter) throws Exception {
		String[] components = StringSplitter.splitComponents(source, componentsDelimiter).components();
		if (components.length == 1 && components[0].trim().isEmpty())
			return new HashMap<>();
		Map<K,V> result = new HashMap<>();
		for (String component : components) {
			String[] keyValue = StringSplitter.splitKeyValue(component, keyValueDelimiter).keyValue();
			if (keyValue.length == 2) {
				result.put((K) firstChildConverter.convert(keyValue[0]), (V) secondChildConverter.convert(keyValue[1]));
			}
		}
		return result;
	}
}
