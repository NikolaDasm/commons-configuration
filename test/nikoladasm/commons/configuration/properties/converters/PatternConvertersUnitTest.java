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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.Converters;

public class PatternConvertersUnitTest {

	private Converters converters = new Converters();
	
	@Test
	public void shouldBeGetCorrectPatternConverter() throws Exception {
		Converter<Pattern> converter = converters.converter(Pattern.class, null);
		assertThat(converter.convert("^ab*c?d$").pattern(), is(equalTo(Pattern.compile("^ab*c?d$").pattern())));
	}
}
