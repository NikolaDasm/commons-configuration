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

import java.util.concurrent.atomic.*;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.Converters;

public class AtomicConvertersUnitTest {

	private Converters converters = new Converters();
	
	@Test
	public void shouldBeGetCorrectAtomicBooleanConverter() throws Exception {
		Converter<AtomicBoolean> converter = converters.converter(AtomicBoolean.class, null);
		assertThat(converter.convert("true").get(), is(equalTo(true)));
	}

	@Test
	public void shouldBeGetCorrectAtomicIntegerConverter() throws Exception {
		Converter<AtomicInteger> converter = converters.converter(AtomicInteger.class, null);
		assertThat(converter.convert("10583").get(), is(equalTo(10583)));
	}

	@Test
	public void shouldBeGetCorrectAtomicLongConverter() throws Exception {
		Converter<AtomicLong> converter = converters.converter(AtomicLong.class, null);
		assertThat(converter.convert("9999910583").get(), is(equalTo(9999910583L)));
	}
}
