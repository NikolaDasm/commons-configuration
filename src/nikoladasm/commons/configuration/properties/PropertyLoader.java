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

package nikoladasm.commons.configuration.properties;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import nikoladasm.commons.configuration.properties.annotations.*;
import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.Converters;
import nikoladasm.commons.configuration.properties.extproperties.PrepProperties;

public class PropertyLoader {

	protected static enum ValueTypeSource {
		RETURN_VALUE,
		FIRST_PARAMETER
	}
	
	@FunctionalInterface
	protected static interface elementValueAction<K> {
		void doIt(K element, Object value);
	}
	
	protected Converters converters = new Converters();
	protected String componentsDelimiter;
	protected String keyValueDelimiter;
	protected Boolean required;
	protected String contextPrefix;
	protected String includeKey;
	protected String includesDelimiter;
	
	protected PropertyLoader() {}
	
	public static PropertyLoader getInstance() {
		return new PropertyLoader();
	}
	
	public PropertyLoader convertersClass(Converters converters) {
		this.converters = converters;
		return this;
	}
	
	public Converters converters() {
		return converters;
	}
	
	public PropertyLoader componentsDelimiter(String componentsDelimiter) {
		this.componentsDelimiter = componentsDelimiter;
		return this;
	}
	
	public String componentsDelimiter() {
		return componentsDelimiter;
	}
	
	public PropertyLoader keyValueDelimiter(String keyValueDelimiter) {
		this.keyValueDelimiter = keyValueDelimiter;
		return this;
	}
	
	public String keyValueDelimiter() {
		return keyValueDelimiter;
	}
	
	public PropertyLoader required(Boolean required) {
		this.required = required;
		return this;
	}
	
	public Boolean required() {
		return required;
	}
	
	public PropertyLoader contextPrefix(String contextPrefix) {
		this.contextPrefix = contextPrefix;
		return this;
	}
	
	public String contextPrefix() {
		return contextPrefix;
	}
	
	public <T> T populate(Class<T> clazz) {
		return populate(null, clazz);
	}
	
	public <T> T populate(String context, Class<T> clazz) {
		return populate(context, null, clazz);
	}

	public <T> T populate(String context, String prefix, Class<T> clazz) {
		requireNonNull(clazz,"Class can't be null");
		if (clazz.isInterface()) {
			return proxyInstance(context, prefix, clazz);
		} else {
			try {
				return populate(context, prefix, clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new PropertyLoaderException("Can't create instance of class"+clazz);
			}
		}
	}
	
	public <T> T populate(T object) {
		return populate(null, object);
	}
	
	public <T> T populate(String context, T object) {
		return populate(context, null, object);
	}

	public <T> T populate(String context, String prefix, T object) {
		requireNonNull(object,"Object can't be null");
		PropertyInfo info = new PropertyInfo();
		info.context(context);
		info.required(required);
		info.componentsDelimiter(componentsDelimiter);
		info.keyValueDelimiter(keyValueDelimiter);
		info.contextPrefix(contextPrefix);
		info.includeKey(includeKey);
		info.includesDelimiter(includesDelimiter);
		Class<?> clazz = object.getClass();
		Class<?> resourceClazz = object.getClass();
		PropertyInfo classPropertyInfo = classPropertyInfo(clazz, resourceClazz, info);
		if (object instanceof Properties) {
			Properties properties = (Properties) object;
			populate(
					context,
					prefix,
					propertyEntries(clazz, resourceClazz, classPropertyInfo),
					converters.newInstance(),
					(key, value) -> properties.setProperty(key, (String) value));
		}
		populate(
				context,
				prefix,
				propertyFields(clazz, resourceClazz, classPropertyInfo),
				converters.newInstance(),
				(element, elementValue) -> setValueToField(element, object, elementValue));
		populate(
				context,
				prefix,
				propertyMethods(clazz, resourceClazz, ValueTypeSource.FIRST_PARAMETER, classPropertyInfo),
				converters.newInstance(),
				(element, elementValue) -> callMethod(element, object, elementValue));
		return object;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T proxyInstance(String context, String prefix, Class<T> clazz) {
		Map<Method,Object> propertyValues = new HashMap<>();
		PropertyInfo info = new PropertyInfo();
		info.context(context);
		info.required(required);
		info.componentsDelimiter(componentsDelimiter);
		info.keyValueDelimiter(keyValueDelimiter);
		info.contextPrefix(contextPrefix);
		info.includeKey(includeKey);
		info.includesDelimiter(includesDelimiter);
		Class<?> resourceClazz = this.getClass();
		PropertyInfo classPropertyInfo = classPropertyInfo(clazz, resourceClazz, info);
		populate(
				context,
				prefix,
				propertyMethods(clazz, resourceClazz, ValueTypeSource.RETURN_VALUE, classPropertyInfo),
				converters.newInstance(),
				(element, elementValue) -> propertyValues.put(element, elementValue));
		return (T) Proxy.newProxyInstance(
			clazz.getClassLoader(),
			new Class[]{clazz},
			new PropertiesProxyHandler(propertyValues));
	}
	
	protected <S> void populate(String context, String prefix, Map<S,PropertyInfo> propertyMap, Converters converters, elementValueAction<S> action) {
		prefix = (prefix == null) ? "" : prefix+".";
		PrepProperties sysProperties = 
				new PrepProperties(System.getProperties());
		for (Entry<S, PropertyInfo> propertyElement : propertyMap.entrySet()) {
			PropertyInfo elementPropertyInfo = propertyElement.getValue();
			String elementContext = elementPropertyInfo.context();
			Boolean elementRequired = elementPropertyInfo.required();
			String elementComponentsDelimiter = elementPropertyInfo.componentsDelimiter();
			String elementKeyValueDelimiter = elementPropertyInfo.keyValueDelimiter();
			String elementContextPrefix = elementPropertyInfo.contextPrefix();
			S element = propertyElement.getKey();
			Object elementValue = null;
			if (elementPropertyInfo.isChild()) {
				Class<?> childClazz = elementPropertyInfo.type();
				if (childClazz.isPrimitive()) {
					throw new PropertyLoaderException("Child can't be primitive");
				}
				elementValue = populate(elementContext, elementPropertyInfo.prefix(), childClazz);
				action.doIt(element, elementValue);
			} else {
				sysProperties.context(elementContextPrefix, elementContext);
				String key = prefix+elementPropertyInfo.key();
				String value = sysProperties.getProperty(key);
				value = (value == null) ?
					getValueFromProperties(key, elementContextPrefix, elementContext, elementPropertyInfo.properies()) : value;
				PropertyInfo classPropertyinfo = elementPropertyInfo.parentInfo();
				classPropertyinfo.properies().context(elementContextPrefix, elementContext);
				value = (value == null) ?
					getValueFromProperties(key, elementContextPrefix, elementContext, classPropertyinfo.properies()) : value;
				value = (value == null) ? elementPropertyInfo.defaultValue() : value;
				if (elementRequired != null && elementRequired && value == null)
					throw new PropertyLoaderException("Required property \""+key+"\" not found");
				if (value == null) {
					elementValue = value;
				} else {
					try {
						Converter<?> converter = null;
						if (elementPropertyInfo.converter() != null)
							converter = elementPropertyInfo.converter().newInstance();
						if (converter == null)
							converter = converters.converter(elementPropertyInfo.type(), elementPropertyInfo.genericType(), elementComponentsDelimiter, elementKeyValueDelimiter);
						elementValue = converter.convert(value);
					} catch (Exception e) {
						throw new PropertyLoaderException("Unsupported property type or illegal property value format for proprty \""+key+"\"", e);
					}
				}
				action.doIt(element, elementValue);
			}
		}
	}
	
	protected void setValueToField(Field field, Object object, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		}
		catch (IllegalAccessException e) {
			throw new PropertyLoaderException("Can't set value for field \""+field.getName()+"\"");
		}
	}

	protected void callMethod(Method method, Object object, Object value) {
		method.setAccessible(true);
		try {
			method.invoke(object, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PropertyLoaderException("Can't call method \""+method.getName()+"\"");
		}
	}
	
	protected String getValueFromProperties(String key, String contextPrefix, String context, PrepProperties properties) {
		if (properties == null) return null;
		properties.context(contextPrefix, context);
		return properties.getProperty(key);
	}
	
	protected Map<Field,PropertyInfo> propertyFields(Class<?> clazz, Class<?> resourceClazz, PropertyInfo defaultInfo) {
		Map<Field,PropertyInfo> fieldsMap = new HashMap<>();
		while (clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				PropertyInfo info = fieldAndMethodPropertyInfo(field, resourceClazz, defaultInfo);
				if (info != null) {
					info.type(field.getType());
					info.genericParameters(genericTypeToParameters(field.getGenericType()));
					fieldsMap.put(field, info);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fieldsMap;
	}
	
	protected Map<Method,PropertyInfo> propertyMethods(Class<?> clazz, Class<?> resourceClazz, ValueTypeSource valueTypeSource, PropertyInfo defaultInfo) {
		Map<Method,PropertyInfo> methodsMap = new HashMap<>();
		Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				PropertyInfo info = fieldAndMethodPropertyInfo(method, resourceClazz, defaultInfo);
				if (info == null) continue;
				info = populateMethodType(method, info, valueTypeSource);
				if (info == null) continue;
				methodsMap.put(method, info);
			}
		return methodsMap;
	}

	protected Map<String,PropertyInfo> propertyEntries(Class<?> clazz, Class<?> resourceClazz, PropertyInfo defaultInfo) {
		Map<String,PropertyInfo> entryMap = new HashMap<>();
		PropertiesEntry[] entries = clazz.getAnnotationsByType(PropertiesEntry.class);
		for (PropertiesEntry entry : entries) {
			PropertyInfo info = new PropertyInfo(defaultInfo);
			String key = entry.property();
			info.key(key);
			String alias = entry.alias();
			if (alias.equals(PropertiesEntry.EMPTY_VALUE))
				alias = key;
			String context = entry.context();
			if (!context.equals(PropertiesEntry.EMPTY_VALUE))
				info.context(context);
			String contextPrefix = entry.contextPrefix();
			if (!contextPrefix.equals(PropertiesEntry.EMPTY_VALUE))
				info.contextPrefix(contextPrefix);
			String defaultValue = entry.defaultValue();
			if (!defaultValue.equals(PropertiesEntry.EMPTY_VALUE))
				info.defaultValue(defaultValue);
			String includeKey = entry.includeKey();
			if (!includeKey.equals(PropertiesEntry.EMPTY_VALUE))
				info.includeKey(includeKey);
			String includesDelimiter = entry.includesDelimiter();
			if (!includesDelimiter.equals(PropertiesEntry.EMPTY_VALUE))
				info.includesDelimiter(includesDelimiter);
			String required = entry.required();
			if (!required.equals(PropertiesEntry.EMPTY_VALUE))
				info.required("true".equals(required));
			info.properies(orderedProperies(entry.resources(), resourceClazz, info));
			info.type(String.class);
			entryMap.put(alias, info);
		}
		return entryMap;
	}
	
	protected PropertyInfo populateMethodType(Method method, PropertyInfo info, ValueTypeSource valueTypeSource) {
		switch (valueTypeSource) {
			case RETURN_VALUE : {
				info.type(method.getReturnType());
				info.genericParameters(genericTypeToParameters(method.getGenericReturnType()));
				return info;
			}
			case FIRST_PARAMETER : {
				Class<?>[] types = method.getParameterTypes();
				if (types.length != 1) return null;
				Type[] genericTypes = method.getGenericParameterTypes();
				if (genericTypes.length != 1) return null;
				info.type(types[0]);
				info.genericParameters(genericTypeToParameters(genericTypes[0]));
				return info;
			}
		}
		return null;
	}
	
	protected Class<?>[] genericTypeToParameters(Type type) {
		if (type != null && type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			Type[] genericTypes = paramType.getActualTypeArguments();
			Class<?>[] genericTypeClasses = new Class<?>[genericTypes.length];
			for (int i=0; i < genericTypes.length; i++ )
				genericTypeClasses[i] = (Class<?>) genericTypes[i];
			return genericTypeClasses;
		}
		return null;
	}
	
	protected PropertyInfo fieldAndMethodPropertyInfo(AnnotatedElement element, Class<?> resourceClazz, PropertyInfo defaultInfo) {
		PropertyInfo info = new PropertyInfo(defaultInfo);
		if (element.isAnnotationPresent(Child.class)) {
			info.prefix(element.getAnnotation(Child.class).prefix());
			if (element.isAnnotationPresent(Context.class))
				info.context(element.getAnnotation(Context.class).value());
		} else if (element.isAnnotationPresent(Property.class)) {
			if (element.isAnnotationPresent(Context.class))
				info.context(element.getAnnotation(Context.class).value());
			if (element.isAnnotationPresent(ComponentsDelimiter.class))
				info.componentsDelimiter(element.getAnnotation(ComponentsDelimiter.class).value());
			if (element.isAnnotationPresent(KeyValueDelimiter.class))
				info.keyValueDelimiter(element.getAnnotation(KeyValueDelimiter.class).value());
			if (element.isAnnotationPresent(UseConverter.class))
				info.converter(element.getAnnotation(UseConverter.class).value());
			info.key(element.getAnnotation(Property.class).value());
			if (element.isAnnotationPresent(DefaultValue.class))
				info.defaultValue(element.getAnnotation(DefaultValue.class).value());
			if (element.isAnnotationPresent(Required.class))
				info.required(element.getAnnotation(Required.class).value());
			if (element.isAnnotationPresent(ContextPrefix.class))
				info.contextPrefix(element.getAnnotation(ContextPrefix.class).value());
			if (element.isAnnotationPresent(IncludeKey.class))
				info.includeKey(element.getAnnotation(IncludeKey.class).value());
			if (element.isAnnotationPresent(IncludesDelimiter.class))
				info.includesDelimiter(element.getAnnotation(IncludesDelimiter.class).value());
			info.properies(orderedProperies(element.getAnnotationsByType(Resource.class), resourceClazz, info));
		} else {
			return null;
		}
		return info;
	}
	
	protected PropertyInfo classPropertyInfo(Class<?> clazz, Class<?> resourceClazz, PropertyInfo defaultInfo) {
		PropertyInfo info = new PropertyInfo(defaultInfo);
		if (clazz.isAnnotationPresent(Context.class))
			info.context(clazz.getAnnotation(Context.class).value());
		if (clazz.isAnnotationPresent(ComponentsDelimiter.class))
			info.componentsDelimiter(clazz.getAnnotation(ComponentsDelimiter.class).value());
		if (clazz.isAnnotationPresent(KeyValueDelimiter.class))
			info.keyValueDelimiter(clazz.getAnnotation(KeyValueDelimiter.class).value());
		if (clazz.isAnnotationPresent(Required.class))
			info.required(clazz.getAnnotation(Required.class).value());
		if (clazz.isAnnotationPresent(ContextPrefix.class))
			info.contextPrefix(clazz.getAnnotation(ContextPrefix.class).value());
		if (clazz.isAnnotationPresent(IncludeKey.class))
			info.includeKey(clazz.getAnnotation(IncludeKey.class).value());
		if (clazz.isAnnotationPresent(IncludesDelimiter.class))
			info.includesDelimiter(clazz.getAnnotation(IncludesDelimiter.class).value());
		info.properies(orderedProperies(clazz.getAnnotationsByType(Resource.class), resourceClazz, info));
		return info;
	}
	
	protected PrepProperties orderedProperies(Resource[] resources, Class<?> resourceClazz, PropertyInfo info) {
		Arrays.sort(resources, (r1, r2) -> Integer.compare(r2.priority(), r1.priority()));
		PrepProperties prop = new PrepProperties(System.getProperties());
		PrepProperties fprop = null; 
		for (Resource resource : resources) {
			String includeKey = resource.includeKey();
			if (includeKey.equals(Resource.EMPTY_VALUE))
				includeKey = info.includeKey();
			String includesDelimiter = resource.includeKey();
			if (includesDelimiter.equals(Resource.EMPTY_VALUE))
				includesDelimiter = info.includesDelimiter();
			if (fprop != null) {
				prop = new PrepProperties(fprop);
				fprop = null;
			}
			prop.includeKey(includeKey);
			prop.includesDelimiter(includesDelimiter);
			for (String path : resource.value()) {
				if (path == null || path.trim().isEmpty()) continue;
				String resolvedPath = PrepProperties.resolve(System.getProperties(), path);
				switch(resource.source()) {
					case FILE : {
						fprop = getPropertiesFromFile(resolvedPath, prop);
						break;
					}
					case CLASSPATH : {
						fprop = getPropertiesFromClassPath(resolvedPath, resourceClazz, prop);
						break;
					}
				}
				if (fprop != null) break;
			}
		}
		return (fprop == null) ? prop : fprop;
	}
	
	protected PrepProperties getPropertiesFromClassPath(String path, Class<?> resourceClazz, PrepProperties prop) {
		try (InputStream cis = resourceClazz.getResourceAsStream(path);
			 InputStream clis = resourceClazz.getClassLoader().getResourceAsStream(path)
		) {
			if (cis != null) {
				prop.load(cis);
				return prop;
			} else if (clis != null) {
				prop.load(clis);
				return prop;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	protected PrepProperties getPropertiesFromFile(String path, PrepProperties prop) {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) return null;
		try (InputStream is = new FileInputStream(file)) {
			prop.load(is);
			return prop;
		} catch (IOException e) {
			return null;
		}
	}
}