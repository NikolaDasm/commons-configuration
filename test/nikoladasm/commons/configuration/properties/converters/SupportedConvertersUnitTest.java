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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.Converters;

import static java.nio.charset.StandardCharsets.UTF_8;

@RunWith(Parameterized.class)
public class SupportedConvertersUnitTest {

	private static enum TestEnum {
		ONE,
		TWO,
		THREE
	}

	@Parameter(0)
	public Class<?> clazz;
	
	@Parameter(1)
	public Class<?>[] genericParameters;
	
	@Parameter(2)
	public String source;
	
	@Parameter(3)
	public Object expect;
	
	private Converters converters = new Converters();
	
	@Parameters
	public static Collection<Object[]> data() throws Exception {
		return Arrays.asList(new Object[][] {
			{Byte.class,			null,							"83",							(byte)83},
			{Short.class,			null,							"583",							(short)583},
			{Integer.class,			null,							"10583",						10583},
			{Long.class,			null,							"9999910583",					9999910583L},
			{Float.class,			null,							"538.46",						538.46F},
			{Double.class,			null,							"538.46743298",					538.46743298},
			{Character.class,		null,							"g",							'g'},
			{Boolean.class,			null,							"true",							true},
			{String.class,			null,							"aBc",							"aBc"},
			{BigDecimal.class,		null,							"100000000000000000000",		new BigDecimal("100000000000000000000")},
			{BigInteger.class,		null,							"100000000000000000000",		new BigInteger("100000000000000000000")},
			{Charset.class,			null,							"utf-8",						UTF_8},
			{File.class,			null,							"resources/res1.properties",	new File("resources/res1.properties")},
			{Locale.class,			null,							"en",							Locale.forLanguageTag("en")},
			{Path.class,			null,							"resources/res1.properties",	Paths.get("resources/res1.properties")},
			{URI.class,				null,							"http:/example.com/",			new URI("http:/example.com/")},
			{URL.class,				null,							"http:/example.com/",			new URL("http:/example.com/")},
			{Integer[].class,		null,							"25,48, 54",					new Integer[]{25,48,54}},
			{TestEnum.class,		null,							"two",							TestEnum.TWO},
		});
	}

	@Test
	public void shouldBeGetCorrectConverter() throws Exception {
		Converter<?> converter = converters.converter(clazz, genericParameters);
		assertThat(converter.convert(source), is(equalTo(expect)));
	}

}
