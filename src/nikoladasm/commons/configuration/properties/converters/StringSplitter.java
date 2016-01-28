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

public class StringSplitter {
	protected static ThreadLocal<StringSplitter> splitterHolder = new ThreadLocal<>();
	
	protected String[] components;
	protected String[] keyValue;
	
	public static StringSplitter splitComponents(String str, String componentsDelimiter) {
		if (splitterHolder.get() == null) splitterHolder.set(new StringSplitter());
		StringSplitter splitter = splitterHolder.get();
		splitter.splitC(str, componentsDelimiter);
		return splitter;
	}
	
	public static StringSplitter splitKeyValue(String str, String keyValueDelimiter) {
		if (splitterHolder.get() == null) splitterHolder.set(new StringSplitter());
		StringSplitter splitter = splitterHolder.get();
		splitter.splitK(str, keyValueDelimiter);
		return splitter;
	}
	
	protected StringSplitter() {}

	protected String[] split(String str, String delimiter) {
		String regex = "(?<!\\\\)" + delimiter;
		String[] parts = str.split(regex);
		String[] components = new String[parts.length];
		for (int i=0; i < parts.length; i++)
			components[i] = parts[i].replaceAll("\\\\"+delimiter, delimiter);
		return components;
	}
	
	protected void splitC(String str, String componentsDelimiter) {
		components = split(str, componentsDelimiter);
	}
	
	protected void splitK(String str, String keyValueDelimiter) {
		keyValue = split(str, keyValueDelimiter);
	}
	
	public String[] components() {
		return components;
	}
	
	public String[] keyValue() {
		return keyValue;
	}
}
