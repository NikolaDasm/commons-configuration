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

import java.util.Map;
import java.util.Properties;

public class ConProperties extends Properties {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_CONTEXT_PREFIX = "%";
	
	protected String context;
	protected String prefix;
	protected String contextPrefix = DEFAULT_CONTEXT_PREFIX;
	
	public ConProperties() {
		super();
	}
	
	public ConProperties(Properties defaults) {
		super(defaults);
	}
	
	public ConProperties(String context) {
		super();
		context(context);
	}
	
	public ConProperties(String context, Properties defaults) {
		super(defaults);
		context(context);
	}

	public ConProperties context(String context) {
		this.context = (context == null) ? "" : context;
		prefix = (this.context.isEmpty()) ? "" : contextPrefix+this.context+".";
		return this;
	}
	
	public ConProperties context(String contextPrefix, String context) {
		contextPrefix = (contextPrefix == null) ? DEFAULT_CONTEXT_PREFIX : contextPrefix;
		this.context = (context == null) ? "" : context;
		prefix = (this.context.isEmpty()) ? "" : contextPrefix+this.context+".";
		return this;
	}
	
	public String context() {
		return context;
	}
	
	public ConProperties contextPrefix(String prefix) {
		contextPrefix = prefix;
		return this;
	}
	
	public String contextPrefix() {
		return contextPrefix;
	}

	public void load(Properties properties) {
		this.putAll(properties);
	}
	
	public void load(Map<String,String> map) {
		this.putAll(map);
	}
	
	@Override
	public String getProperty(String key) {
		String value = super.getProperty(prefix+key);
		return (value == null) ? super.getProperty(key) : value;
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		String value = super.getProperty(prefix+key);
		return (value == null) ? super.getProperty(key, defaultValue) : value;
	}
}
