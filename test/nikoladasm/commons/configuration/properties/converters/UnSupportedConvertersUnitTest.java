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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class UnSupportedConvertersUnitTest {

	public static class CustomClass {
	}
	
	@Parameters
	public static Collection<Object[]> data() throws Exception {
		return Arrays.asList(new Object[][] {
			{CustomClass[].class,	null},
			{CustomClass.class,		null},
			{List.class,			new Class<?>[]{}},
			{List.class,			new Class<?>[]{CustomClass.class}},
			{Map.class,				new Class<?>[]{}},
			{Map.class,				new Class<?>[]{CustomClass.class, CustomClass.class}},
			{Map.class,				new Class<?>[]{Integer.class, CustomClass.class}},
		});
	}

	@Parameter(0)
	public Class<?> clazz;
	
	@Parameter(1)
	public Class<?>[] genericParameters;
	
	private Converters converters = new Converters();
	
	@Test
	public void shouldBeNullConverter() {
		Converter<?> converter = converters.converter(clazz, genericParameters);
		assertThat(converter, is(nullValue()));
	}
}
