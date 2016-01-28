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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefProperties extends ConProperties {
	private static final long serialVersionUID = 1L;

	public static final String ENV_PREFIX = "ENV:";
	public static final String SYS_PREFIX = "SYS:";
	
	protected static final String REFERENCE_PATTERN = "\\$\\{((?:SYS:|ENV:)?[^\\}]+)\\}";
	protected static final Pattern PATTERN = Pattern.compile(REFERENCE_PATTERN);

	protected boolean doResolving = true;
	
	public static String resolve(Properties properties, String value) {
		ConProperties conProperties;
		if (!(properties instanceof ConProperties))
			conProperties = new ConProperties(properties);
		else
			conProperties = (ConProperties) properties;
		return resolve(conProperties, null, null, value);
	}
	
	public static String resolve(ConProperties properties, String value) {
		return resolve(properties, null, null, value);
	}
	
	public static String resolve(ConProperties properties, String contextPrefix, String context, String value) {
		ConProperties envProperties = new ConProperties();
		envProperties.load(System.getenv());
		ConProperties sysProperties = new ConProperties(System.getProperties());
		return resolve(envProperties, sysProperties, properties, contextPrefix, context, value);
	}
	
	protected static String resolve(ConProperties envProperties, ConProperties sysProperties, ConProperties properties, String contextPrefix, String context, String value) {
		if (value == null) return value;
		Matcher refMatcher = PATTERN.matcher(value);
		Map<String,String> refMap = new HashMap<>();
		while (refMatcher.find())
			refMap.put(refMatcher.group(0), refMatcher.group(1));
		if(refMap.size() < 1) return value;
		Map<String,String> resolvedRefMap = new HashMap<>();
		envProperties.context(contextPrefix, context);
		sysProperties.context(contextPrefix, context);
		refMap.forEach((ref, refName) -> {
			String ucRefName = refName.toUpperCase();
			if (ucRefName.startsWith(ENV_PREFIX)) {
				String envValue = envProperties.getProperty(refName.substring(4));
				if (envValue != null)
					resolvedRefMap.put(ref, resolve(envProperties, sysProperties, properties, contextPrefix, context, envValue));
			} else if (ucRefName.startsWith(SYS_PREFIX)) {
				String sysValue = sysProperties.getProperty(refName.substring(4));
				if (sysValue != null)
					resolvedRefMap.put(ref, resolve(envProperties, sysProperties, properties, contextPrefix, context, sysValue));
			} else if (properties != null) {
				properties.context(contextPrefix, context);
				String propValue = properties.getProperty(refName);
				if (propValue != null)
					resolvedRefMap.put(ref, resolve(envProperties, sysProperties, properties, contextPrefix, context, propValue));
			}
		});
		for (Entry<String,String> entry : resolvedRefMap.entrySet()) {
			value = value.replace(entry.getKey(), entry.getValue());
		}
		return value;
	}
	
	public RefProperties() {
		super();
	}
	
	public RefProperties(Properties defaults) {
		super(defaults);
	}
	
	public RefProperties(String context) {
		super(context);
	}
	
	public RefProperties(String context, Properties defaults) {
		super(context, defaults);
	}
	
	public RefProperties doResolving(boolean doIt) {
		doResolving = doIt;
		return this;
	}
	
	public boolean doResolving() {
		return doResolving;
	}
	
	@Override
	public String getProperty(String key) {
		String value = super.getProperty(key);
		ConProperties envProperties = new ConProperties();
		envProperties.load(System.getenv());
		ConProperties sysProperties = new ConProperties(System.getProperties());
		return doResolving ? resolve(envProperties, sysProperties, this, contextPrefix, context, value) : value;
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		String value = super.getProperty(key, defaultValue);
		ConProperties envProperties = new ConProperties();
		envProperties.load(System.getenv());
		ConProperties sysProperties = new ConProperties(System.getProperties());
		return doResolving ? resolve(envProperties, sysProperties, this, contextPrefix, context, value) : value;
	}
}
