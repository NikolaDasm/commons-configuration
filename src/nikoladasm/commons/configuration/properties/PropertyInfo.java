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

import nikoladasm.commons.configuration.properties.annotations.MethodPriority;
import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.extproperties.PrepProperties;

public class PropertyInfo {
	private String key;
	private String defaultValue;
	private Boolean required;
	private String context;
	private String prefix;
	private PrepProperties properies;
	private Class<?> type;
	private Class<?>[] genericParameters;
	private Class<? extends Converter<?>> converter;
	private String componentsDelimiter;
	private String keyValueDelimiter;
	private String contextPrefix;
	private String includeKey;
	private String includesDelimiter;
	private PropertyInfo parentInfo;
	private int priority = MethodPriority.DEFAULT_PRIORITY;
	
	public PropertyInfo() {
	}
	
	public PropertyInfo(PropertyInfo defaultInfo) {
		this.parentInfo = defaultInfo;
	}
	
	public void parentInfo(PropertyInfo defaultInfo) {
		this.parentInfo = defaultInfo;
	}
	
	public PropertyInfo parentInfo() {
		return parentInfo;
	}
	
	public void key(String key) {
		this.key = key;
	}
	
	public String key() {
		return key;
	}
	
	public void defaultValue(String value) {
		defaultValue = value;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
	
	public void required(Boolean required) {
		this.required = required;
	}
	
	public Boolean required() {
		return (required == null && parentInfo != null) ? parentInfo.required() : required;
	}
	
	public void context(String context) {
		this.context = context;
	}
	
	public String context() {
		return (context == null && parentInfo != null) ? parentInfo.context() : context;
	}
	
	public void prefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String prefix() {
		return prefix;
	}
	
	public boolean isChild() {
		return prefix != null;
	}
	
	public void properies(PrepProperties properies) {
		this.properies = properies;
	}
	
	public PrepProperties properies() {
		return properies;
	}
	
	public void type(Class<?> type) {
		this.type = type;
	}
	
	public Class<?> type() {
		return type;
	}
	
	public void genericParameters(Class<?>[] genericParameters) {
		this.genericParameters = genericParameters;
	}
	
	public Class<?>[] genericType() {
		return genericParameters;
	}
	
	public void converter(Class<? extends Converter<?>> converter) {
		this.converter = converter;
	}
	
	public Class<? extends Converter<?>> converter() {
		return converter;
	}

	public void componentsDelimiter(String delimiter) {
		componentsDelimiter = delimiter;
	}
	
	public String componentsDelimiter() {
		return (componentsDelimiter == null && parentInfo != null) ? parentInfo.componentsDelimiter() : componentsDelimiter;
	}

	public void keyValueDelimiter(String delimiter) {
		keyValueDelimiter = delimiter;
	}
	
	public String keyValueDelimiter() {
		return (keyValueDelimiter == null && parentInfo != null) ? parentInfo.keyValueDelimiter() : keyValueDelimiter;
	}
	
	public void contextPrefix(String prefix) {
		contextPrefix = prefix;
	}
	
	public String contextPrefix() {
		return (contextPrefix == null && parentInfo != null) ? parentInfo.contextPrefix() : contextPrefix;
	}
	
	public void includeKey(String includeKey) {
		this.includeKey = includeKey;
	}
	
	public String includeKey() {
		return includeKey;
	}
	
	public void includesDelimiter(String includesDelimiter) {
		this.includesDelimiter = includesDelimiter;
	}
	
	public String includesDelimiter() {
		return includesDelimiter;
	}

	public void priority(int priority) {
		this.priority = priority;
	}
	
	public int priority() {
		return priority;
	}
}
