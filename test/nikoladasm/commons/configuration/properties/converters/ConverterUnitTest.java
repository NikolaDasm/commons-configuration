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

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ConverterUnitTest {

	private Converters converters = new Converters();

	@Test(expected=ConverterException.class)
	public void shouldBeGetExceptionWithIncorrectCharacterProperty() throws Exception {
		Converter<Character> converter = converters.converter(Character.class, null);
		converter.convert("ab");
	}

	@Test(expected=ConverterException.class)
	public void shouldBeGetExceptionWithNullCharacterProperty() throws Exception {
		Converter<Character> converter = converters.converter(Character.class, null);
		converter.convert(null);
	}

	@Test
	public void shouldBeGetEmptyArrayOfIntegerFromEmptyProperty() throws Exception {
		Converter<Integer[]> converter = converters.converter(Integer[].class, null);
		assertThat(converter.convert("").length, is(equalTo(0)));
	}

	@Test
	public void shouldBeGetEmptyListOfIntegerFromEmptyProperty() throws Exception {
		Converter<List<Integer>> converter = converters.converter(List.class, new Class<?>[]{Integer.class});
		assertThat(converter.convert("").size(), is(equalTo(0)));
	}

	@Test
	public void shouldBeGetEmptyMapOfStringIntegerFromEmptyProperty() throws Exception {
		Converter<Map<String,Integer>> converter = converters.converter(Map.class, new Class<?>[]{String.class, Integer.class});
		assertThat(converter.convert("").size(), is(equalTo(0)));
	}
}
