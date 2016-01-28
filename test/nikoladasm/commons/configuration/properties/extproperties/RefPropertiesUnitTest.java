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

import java.util.Properties;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.extproperties.RefProperties;

import static org.hamcrest.CoreMatchers.*;

public class RefPropertiesUnitTest {

	@Test
	public void shouldBeGetCorrectEnvValue() {
		String value = RefProperties.resolve(null, "${ENV:JAVA_HOME}");
		assertThat(value, is(equalTo(System.getenv().get("JAVA_HOME"))));
	}
	
	@Test
	public void shouldBeGetCorrectSysPropertyValue() {
		System.setProperty("test.syskey", "sysvalue");
		String value = RefProperties.resolve(null, "${SYS:test.syskey}");
		assertThat(value, is(equalTo("sysvalue")));
	}
	
	@Test
	public void shouldBeGetCorrectPropertyValue() {
		Properties properties = new Properties();
		properties.setProperty("test.key", "value");
		String value = RefProperties.resolve(properties, "${test.key}");
		assertThat(value, is(equalTo("value")));
	}
	
	@Test
	public void shouldBeGetCorrectComplexPropertyValue() {
		Properties properties = new Properties();
		properties.setProperty("path.1", "/resor");
		properties.setProperty("path.2", "${path.1}/tds");
		properties.setProperty("path.3", "/hje/${path.2}");
		properties.setProperty("path.4", "/${path.3}/hnc/${path.2}");
		String path2 = RefProperties.resolve(properties, properties.getProperty("path.2"));
		assertThat(path2, is(equalTo("/resor/tds")));
		String path3 = RefProperties.resolve(properties, properties.getProperty("path.3"));
		assertThat(path3, is(equalTo("/hje//resor/tds")));
		String path4 = RefProperties.resolve(properties, properties.getProperty("path.4"));
		assertThat(path4, is(equalTo("//hje//resor/tds/hnc//resor/tds")));
	}

}
