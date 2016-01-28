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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import nikoladasm.commons.configuration.properties.extproperties.ConProperties;

@RunWith(Parameterized.class)
public class ConPropertiesUnitTest {

	private ConProperties properties;
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{"par1=25\n"
			+ "%test.par1=38",	"test",	"par1",	"38",	"par1",	"38"},
			{"par1=25\n"
			+ "%test.par1=38",	"prod",	"par1",	"25",	"par1",	"25"},
			{"par1=25\n"
			+ "%test.par1=38",	null,	"par1",	"25",	"par1",	"25"},
			{"par1=25\n"
			+ "%test.par1=38",	null,	"par1",	"25",	"par2",	"44"}
		});
	}
	
	@Parameter(0)
	public String inputStr;
	
	@Parameter(1)
	public String context;
	
	@Parameter(2)
	public String key;
	
	@Parameter(3)
	public String value;
	
	@Parameter(4)
	public String keyD;
	
	@Parameter(5)
	public String valueD;
	
	@Test
	public void shouldBeGetValidPropertyValue() throws IOException {
		Reader reader = new StringReader(inputStr);
		properties = new ConProperties(context);
		properties.load(reader);
		assertThat(properties.getProperty(key), is(equalTo(value)));
		assertThat(properties.getProperty(keyD,"44"), is(equalTo(valueD)));
	}

}
