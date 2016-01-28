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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.extproperties.PrepProperties;

import static org.hamcrest.CoreMatchers.*;

public class PrepPropertiesUnitTest {

	private PrepProperties properties = new PrepProperties();
	
	@Test
	public void shouldBeGetCorrectValueFromIncludedProperties() throws IOException {
		properties.includeKey("include").includesDelimiter(",");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources/top.properties");
		properties.load(is);
		assertThat(properties.getProperty("parent.key1"), is(equalTo("parent.value1")));
		assertThat(properties.getProperty("parent.key2"), is(equalTo("parent.value2")));
		assertThat(properties.getProperty("child1.key1"), is(equalTo("child1.value1")));
		assertThat(properties.getProperty("child1.key2"), is(equalTo("child1.value2")));
		assertThat(properties.getProperty("child2.key1"), is(equalTo("child2.value1")));
		assertThat(properties.getProperty("child2.key2"), is(equalTo("child2.value2")));
		assertThat(properties.getProperty("child3.key1"), is(equalTo("child3.value1")));
		assertThat(properties.getProperty("child3.key2"), is(equalTo("child3.value2")));
	}

}
