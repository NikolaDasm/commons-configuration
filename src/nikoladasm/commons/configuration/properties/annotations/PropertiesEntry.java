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

package nikoladasm.commons.configuration.properties.annotations;

import java.lang.annotation.*;

import nikoladasm.commons.configuration.properties.extproperties.PrepProperties;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(PropertiesEntries.class)
public @interface PropertiesEntry {
	public static String EMPTY_VALUE = "";

	String property();
	String alias() default EMPTY_VALUE;
	String context() default EMPTY_VALUE;
	String contextPrefix() default PrepProperties.DEFAULT_CONTEXT_PREFIX;
	String defaultValue() default EMPTY_VALUE;
	String includeKey() default EMPTY_VALUE;
	String includesDelimiter() default EMPTY_VALUE;
	String required() default EMPTY_VALUE;
	Resource[] resources() default {};
}
