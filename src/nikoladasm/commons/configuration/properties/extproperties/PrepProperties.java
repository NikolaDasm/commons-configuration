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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

public class PrepProperties extends RefProperties {
	private static final long serialVersionUID = 1L;

	@FunctionalInterface
	public static interface PropertyLoader {
		void load(String path, Class<?> resourceClazz, PrepProperties properties);
	}
	
	public static final String DEFAULT_INCLUDES_DELIMITER = ",";
	public static final PropertyLoader DEFAULT_PROPERTY_LOADER =
		(path, resourceClazz, properties) -> {
			try (InputStream cis = resourceClazz.getResourceAsStream(path);
				 InputStream clis = resourceClazz.getClassLoader().getResourceAsStream(path)
			) {
				if (cis != null) {
					properties.load(cis);
					return;
				} else if (clis != null) {
					properties.load(clis);
					return;
				}
			} catch (IOException e) {}
			File file = new File(path);
			if (!file.exists() || file.isDirectory()) return;
			try (InputStream is = new FileInputStream(file)) {
				properties.load(is);
			} catch (IOException e) {}
		};
	
	protected String includeKey;
	protected String includesDelimiter = DEFAULT_INCLUDES_DELIMITER;
	protected PropertyLoader propertyLoader = DEFAULT_PROPERTY_LOADER;
	
	public PrepProperties() {
		super();
	}
	
	public PrepProperties(Properties defaults) {
		super(defaults);
	}
	
	public PrepProperties(String context) {
		super(context);
	}
	
	public PrepProperties(String context, Properties defaults) {
		super(context, defaults);
	}

	public PrepProperties includeKey(String key) {
		includeKey = (key == null || key.trim().isEmpty()) ? null : key;
		return this;
	}
	
	public String includeKey() {
		return includeKey;
	}
	
	public PrepProperties includesDelimiter(String delimiter) {
		includesDelimiter = (delimiter == null || delimiter.trim().isEmpty()) ? DEFAULT_INCLUDES_DELIMITER : delimiter;
		return this;
	}
	
	public String invludesDelimiter() {
		return includesDelimiter;
	}
	
	public PrepProperties propertyLoader(PropertyLoader loader) {
		propertyLoader = (loader == null) ? DEFAULT_PROPERTY_LOADER : loader;
		return this;
	}
	
	public PropertyLoader propertyLoader() {
		return propertyLoader;
	}
	
	@Override
	public void load(Properties properties) {
		super.load(properties);
		include();
	}
	
	@Override
	public void load(Map<String,String> map) {
		super.load(map);
		include();
	}
	
	@Override
	public void load(InputStream inStream) throws IOException {
		super.load(inStream);
		include();
	}
	
	@Override
	public void load(Reader reader) throws IOException {
		super.load(reader);
		include();
	}
	
	protected void include() {
		if (includeKey == null) return;
		String includeValue = this.getProperty(includeKey);
		if (includeValue == null) return;
		String[] includes = includeValue.split(includesDelimiter);
		this.remove(includeKey);
		Class<?> clazz = this.getClass();
		for (String include : includes) {
			propertyLoader.load(include.trim(), clazz, this);
		}
	}
}
