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

package nikoladasm.commons.configuration.properties;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;

import static nikoladasm.commons.configuration.properties.annotations.Resource.Type.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.PropertyLoader;
import nikoladasm.commons.configuration.properties.PropertyLoaderException;
import nikoladasm.commons.configuration.properties.annotations.*;
import nikoladasm.commons.configuration.properties.converters.Converter;

public class PropertyLoaderUnitTest {

	public static class ConfigClass1 {
		
		@Property("value1")
		public String value1;
		
		@Required()
		@Property("value2")
		public String value2;
		
		@Property("value3")
		@DefaultValue("def")
		public String value3;
		
		@Property("value4")
		@Context("test")
		public String value4;
		
		@Property("value5")
		@Resource("resources/res1.properties")
		public String value5;

		@Property("value6")
		@Resource(source=FILE, value="resources/res2.properties")
		public String value6;

		@Property("value7")
		@Resource(priority=2, source=FILE, value="resources/res2.properties")
		@Resource(priority=1, source=CLASSPATH, value="resources/res1.properties")
		public String value7;
	
		@Property("value8")
		@Resource(priority=2, source=FILE, value={"res3.properties", "resources/res2.properties"})
		@Resource(priority=1, source=CLASSPATH, value={"","resources/res1.properties"})
		public String value8;
	
		@Property("value9")
		@Resource("res3.properties")
		@DefaultValue("def2")
		public String value9;
		
		@Child(prefix="sub")
		public ChildConfigClass1 value10;

		public String nonPropertyValue;
		
		private String value11;
		
		@Property("value11")
		public void value11(String value) {
			value11 = value;
		}
		
		public String value11() {
			return value11;
		}
		
		@Property("value1")
		public void incorrectPropertyMethod(String a, String b) {
			throw new RuntimeException();
		}
		
		@Property("value20")
		@UseConverter(CustomConverter.class)
		public CustomClass value20;
		
	}
	
	public static class ChildConfigClass1 {
		@Property("value1")
		@Resource("resources/res1.properties")
		public String value1;
	}
	
	@Context("dev")
	public static class ConfigClass2 {
		@Property("value11")
		@Context("")
		public String value11;
		
		@Property("value12")
		public String value12;

		@Child(prefix="sub")
		@Context("")
		public ChildConfigClass1 value13;
	}
	
	public static class ConfigClass3 {
		
		@Child(prefix="sub")
		public int value13;
	}
	
	public static class ConfigClass4 {
		
		@Property("value1")
		public static final String value1 = "";
	}
	
	@ComponentsDelimiter(";")
	@KeyValueDelimiter("/")
	public static class ConfigClass5 {

		@Property("value13")
		@ComponentsDelimiter(",")
		public Integer[] value13;
		
		@Property("value14")
		public String[] value14;
		
		@Property("value15")
		@KeyValueDelimiter("\\?")
		@ComponentsDelimiter(",")
		public Map<String,Integer> value15;
		
		@Property("value16")
		public HashMap<String,Boolean> value16;
	}

	@ContextPrefix("--")
	public static class ConfigClass6 {

		@Property("value17")
		@ContextPrefix("%")
		public String value17;
		
		@Property("value18")
		public String value18;
	}

	@Required()
	public static class ConfigClass7 {

		@Property("value19")
		public String value19;
		
		@Property("value20")
		@Required(false)
		public String value20;
	}
	
	@PropertiesEntry(
		property="value8",
		alias="avalue30",
		resources={
			@Resource(priority=2, source=FILE, value={"res3.properties", "resources/res2.properties"}),
			@Resource(priority=1, source=CLASSPATH, value={"","resources/res1.properties"})
		}
	)
	@PropertiesEntry(property="value31")
	@PropertiesEntry(property="value32", defaultValue="defval")
	public static class ConfigClass8 extends Properties {
		private static final long serialVersionUID = 1L;
	}

	public static class ConfigClass9 {
		
		@Property("value41")
		public CustomClass value41;
	}
	
	public static class ConfigClass10 {
		
		@Property("value42")
		public Integer value41;
	}
	
	@PropertiesEntry(property="value33", required="true")
	public static class ConfigClass11 extends Properties {
		private static final long serialVersionUID = 1L;
	}

	@Resource("resources/top.properties")
	@IncludeKey("include")
	@PropertiesEntry(property="parent.key1")
	@PropertiesEntry(property="parent.key2")
	@PropertiesEntry(property="child1.key1")
	@PropertiesEntry(property="child1.key2")
	@PropertiesEntry(property="child2.key1")
	@PropertiesEntry(property="child2.key2")
	@PropertiesEntry(property="child3.key1")
	@PropertiesEntry(property="child3.key2")
	public static class ConfigClass12 extends Properties {
		private static final long serialVersionUID = 1L;
	}

	public static class ConfigClass13 {
		
		public String result="";
		
		@Property("value51")
		@MethodPriority(MethodPriority.DEFAULT_PRIORITY)
		public void value51(String value){
			result += value;
		};

		@Property("value52")
		@MethodPriority(MethodPriority.DEFAULT_PRIORITY+1)
		public void value52(String value){
			result += value;
		};

		@Property("value53")
		@MethodPriority(MethodPriority.DEFAULT_PRIORITY-1)
		public void value53(String value){
			result += value;
		};
	}
	
	public static class CustomClass {
		private int value;
		public CustomClass(int value) {
			this.value = value;
		}
		public int value() {
			return value;
		}
		
		public boolean equeals(Object object) {
			if (object == null || !(object instanceof CustomClass)) return false;
			CustomClass cl = (CustomClass) object;
			return cl.value() == value;
		}
	}
	
	public static class CustomConverter implements Converter<CustomClass> {

		@Override
		public CustomClass convert(String source) throws Exception {
			return new CustomClass(Integer.valueOf(source.trim()));
		}
	}
	
	
	public static interface ConfigInterface1 {
		
		@Property("value1")
		String value1();
		
		@Required()
		@Property("value2")
		String value2();
		
		@Property("value3")
		@DefaultValue("def")
		String value3();
		
		@Property("value4")
		@Context("test")
		String value4();
		
		@Property("value5")
		@Resource("resources/res1.properties")
		String value5();

		@Property("value6")
		@Resource(source=FILE, value="resources/res2.properties")
		String value6();

		@Property("value7")
		@Resource(priority=2, source=FILE, value="resources/res2.properties")
		@Resource(priority=1, source=CLASSPATH, value="resources/res1.properties")
		String value7();
	
		@Property("value8")
		@Resource(priority=2, source=FILE, value={"res3.properties", "resources/res2.properties"})
		@Resource(priority=1, source=CLASSPATH, value={"","resources/res1.properties"})
		String value8();
	
		@Property("value9")
		@Resource("res3.properties")
		@DefaultValue("def2")
		String value9();
		
		@Child(prefix="sub")
		ChildInterfaceClass1 value10();
		
		String nonPropertyValue();
	}
	
	public static interface ChildInterfaceClass1 {
		@Property("value1")
		@Resource("resources/res1.properties")
		String value1();
	}
	
	@Context("dev")
	public static interface ConfigInterface2 {
		@Property("value11")
		@Context("")
		String value11();
		
		@Property("value12")
		String value12();

		@Child(prefix="sub")
		@Context("")
		ChildInterfaceClass1 value13();
	}
	
	public static interface ConfigInterface3 {
		
		@Child(prefix="sub")
		int value13();
	}
	
	private ConfigClass1 conf1;
	private ConfigClass2 conf2;
	private ConfigClass3 conf3;
	private ConfigClass4 conf4;
	private ConfigClass5 conf5;
	private ConfigClass6 conf6;
	private ConfigClass7 conf7;
	private ConfigClass8 conf8;
	private ConfigClass9 conf9;
	private ConfigClass10 conf10;
	private ConfigClass12 conf12;
	private ConfigClass13 conf13;
	
	@Before
	public void init() {
		System.getProperties().setProperty("value2", "testvalue2");
		conf1 = new ConfigClass1();
		conf2 = new ConfigClass2();
		conf3 = new ConfigClass3();
		conf4 = new ConfigClass4();
		conf5 = new ConfigClass5();
		conf6 = new ConfigClass6();
		conf7 = new ConfigClass7();
		conf8 = new ConfigClass8();
		conf9 = new ConfigClass9();
		conf10 = new ConfigClass10();
		conf12 = new ConfigClass12();
		conf13 = new ConfigClass13();
	}
	
	@Test
	public void shoulBeGetPropertyValueFromSystemProperties() {
		System.getProperties().setProperty("value1", "testvalue1");
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value1, is(equalTo("testvalue1")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value1, is(equalTo("testvalue1")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value1(), is(equalTo("testvalue1")));
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForObjectPopulateWithRequiredPropertyWhenPropertyNotFound() {
		System.getProperties().remove("value2");
		PropertyLoader.getInstance().populate(null, conf1);
	}

	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForClassPopulateWithRequiredPropertyWhenPropertyNotFound() {
		System.getProperties().remove("value2");
		PropertyLoader.getInstance().populate(null, ConfigClass1.class);
	}

	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForInterfacePopulateWithRequiredPropertyWhenPropertyNotFound() {
		System.getProperties().remove("value2");
		PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
	}

	@Test
	public void shouldBeGetDefaultValue() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value3, is(equalTo("def")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value3, is(equalTo("def")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value3(), is(equalTo("def")));
	}
	
	@Test
	public void shouldBeGetValueFromContextTest() {
		System.getProperties().setProperty("value4", "nontestcontext");
		System.getProperties().setProperty("%test.value4", "testcontext");
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value4, is(equalTo("testcontext")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value4, is(equalTo("testcontext")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value4(), is(equalTo("testcontext")));
	}
	
	@Test 
	public void shouldBeGetValueFromClasspathPropertyFile() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value5, is(equalTo("res1val5")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value5, is(equalTo("res1val5")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value5(), is(equalTo("res1val5")));
	}
	
	@Test
	public void shouldBeGetValueFromPropertyFile() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value6, is(equalTo("res2val6")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value6, is(equalTo("res2val6")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value6(), is(equalTo("res2val6")));
	}
	
	@Test
	public void shouldBeGetValueFromPropertyFileWithHigherPriority() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value7, is(equalTo("res1val7")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value7, is(equalTo("res1val7")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value7(), is(equalTo("res1val7")));
	}
	
	@Test
	public void shouldBeGetValueFromExistingPropertyFileWithLowerPriority() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value8, is(equalTo("res2val8")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value8, is(equalTo("res2val8")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value8(), is(equalTo("res2val8")));
	}

	@Test
	public void shouldBeGetDefaultValueWhenFileNotFound() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value9, is(equalTo("def2")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value9, is(equalTo("def2")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value9(), is(equalTo("def2")));
	}
	
	@Test
	public void shouldBeGetValueInChildChass() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value10.value1, is(equalTo("res1subval1")));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.value10.value1, is(equalTo("res1subval1")));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.value10().value1(), is(equalTo("res1subval1")));
	}

	@Test
	public void shouldBeGetValueWithMethodPropertyInClass() {
		System.getProperties().setProperty("value11", "propval11");
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value11(), is(equalTo("propval11")));
	}
	
	@Test
	public void shouldBeGetCustomClassValueWithCustomClassConverter() {
		System.getProperties().setProperty("value20", "4943");
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.value20.value(), is(equalTo(4943)));
	}
	
	@Test
	public void shouldBeGetValueFromCorrectContext() {
		System.getProperties().setProperty("value11", "nondevvalue");
		System.getProperties().setProperty("%dev.value11", "devvalue");
		System.getProperties().setProperty("value12", "nondevvalue");
		System.getProperties().setProperty("%dev.value12", "devvalue");
		PropertyLoader.getInstance().populate(null, conf2);
		assertThat(conf2.value11, is(equalTo("nondevvalue")));
		assertThat(conf2.value12, is(equalTo("devvalue")));
		ConfigClass2 conf2 = PropertyLoader.getInstance().populate(null, ConfigClass2.class);
		assertThat(conf2.value11, is(equalTo("nondevvalue")));
		assertThat(conf2.value12, is(equalTo("devvalue")));
		ConfigInterface2 confi2 = PropertyLoader.getInstance().populate(null, ConfigInterface2.class);
		assertThat(confi2.value11(), is(equalTo("nondevvalue")));
		assertThat(confi2.value12(), is(equalTo("devvalue")));
	}
	
	@Test
	public void shouldBeGetValueInChildChassWithContextAnnotation() {
		PropertyLoader.getInstance().populate(null, conf2);
		assertThat(conf2.value13.value1, is(equalTo("res1subval1")));
		ConfigClass2 conf2 = PropertyLoader.getInstance().populate(null, ConfigClass2.class);
		assertThat(conf2.value13.value1, is(equalTo("res1subval1")));
		ConfigInterface2 confi2 = PropertyLoader.getInstance().populate(null, ConfigInterface2.class);
		assertThat(confi2.value13().value1(), is(equalTo("res1subval1")));
	}
	
	@Test
	public void shouldBeGetNullValueWithoutAnnotation() {
		PropertyLoader.getInstance().populate(null, conf1);
		assertThat(conf1.nonPropertyValue, is(nullValue()));
		ConfigClass1 conf1 = PropertyLoader.getInstance().populate(null, ConfigClass1.class);
		assertThat(conf1.nonPropertyValue, is(nullValue()));
		ConfigInterface1 confi1 = PropertyLoader.getInstance().populate(null, ConfigInterface1.class);
		assertThat(confi1.nonPropertyValue(), is(nullValue()));
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForObjectPopulateWithChildAnnotationOnPrimitiveType() {
		PropertyLoader.getInstance().populate(null, conf3);
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForClassPopulateWithChildAnnotationOnPrimitiveType() {
		PropertyLoader.getInstance().populate(null, ConfigClass3.class);
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForInterfacePopulateWithChildAnnotationOnPrimitiveType() {
		PropertyLoader.getInstance().populate(null, ConfigInterface3.class);
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForObjectPopulateWithFinalField() {
		System.getProperties().setProperty("value1", "testvalue1");
		PropertyLoader.getInstance().populate(null, conf4);
	}
	
	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForClassPopulateWithFinalField() {
		System.getProperties().setProperty("value1", "testvalue1");
		PropertyLoader.getInstance().populate(null, ConfigClass4.class);
	}
	
	@Test
	public void shouldBeGetValueWithCorrectDelimiters() {
		System.getProperties().setProperty("value13", "25,46,78");
		System.getProperties().setProperty("value14", "first;second");
		System.getProperties().setProperty("value15", "abc?45,cba?33");
		System.getProperties().setProperty("value16", "aaa/true;bbb/false");
		PropertyLoader.getInstance().populate(null, conf5);
		assertThat(conf5.value13, is(equalTo(new Integer[]{25,46,78})));
		assertThat(conf5.value14, is(equalTo(new String[]{"first","second"})));
		Map<String,Integer> expectedMap = new HashMap<>();
		expectedMap.put("abc",45);
		expectedMap.put("cba",33);
		assertThat(conf5.value15, is(equalTo(expectedMap)));
		HashMap<String,Boolean> expectedHashMap = new HashMap<>();
		expectedHashMap.put("aaa",true);
		expectedHashMap.put("bbb",false);
		assertThat(conf5.value16, is(equalTo(expectedHashMap)));
	}
	
	@Test
	public void shouldBeGetValueWithCorrectContextPrefix() {
		System.getProperties().setProperty("value17", "nonprodvalue");
		System.getProperties().setProperty("%prod.value17", "prodvalue");
		System.getProperties().setProperty("value18", "nonprodvalue");
		System.getProperties().setProperty("--prod.value18", "prodvalue");
		PropertyLoader.getInstance().populate("prod", conf6);
		assertThat(conf6.value17, is(equalTo("prodvalue")));
		assertThat(conf6.value18, is(equalTo("prodvalue")));
	}
	
	@Test
	public void shouldBeGetValueRequiredFromClass() {
		System.getProperties().setProperty("value19", "value19");
		System.getProperties().remove("value20");
		PropertyLoader.getInstance().populate(null, conf7);
		assertThat(conf7.value19, is(equalTo("value19")));
		assertThat(conf7.value20, is(nullValue()));
	}
	
	@Test
	public void shouldBeGetCorrectValueFromPropertiesClass() {
		System.getProperties().setProperty("value31", "value31");
		PropertyLoader.getInstance().populate(null, conf8);
		assertThat(conf8.getProperty("avalue30"), is(equalTo("res2val8")));
		assertThat(conf8.getProperty("value31"), is(equalTo("value31")));
		assertThat(conf8.getProperty("value32"), is(equalTo("defval")));
	}

	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionWithUnsupportedObjectType() {
		System.getProperties().setProperty("value41", "value41");
		PropertyLoader.getInstance().populate(null, conf9);
	}

	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionWithIncorrectPropertyFormat() {
		System.getProperties().setProperty("value42", "value41");
		PropertyLoader.getInstance().populate(null, conf10);
	}

	@Test(expected=PropertyLoaderException.class)
	public void shouldBeGetExceptionForPropertyClassPopulateWithRequiredPropertyWhenPropertyNotFound() {
		System.getProperties().remove("value33");
		PropertyLoader.getInstance().populate(null, ConfigClass11.class);
	}

	@Test
	public void shouldBeGetCorrectValueFromPropertiesClassWithIncludes() {
		PropertyLoader.getInstance().populate(null, conf12);
		assertThat(conf12.getProperty("parent.key1"), is(equalTo("parent.value1")));
		assertThat(conf12.getProperty("parent.key2"), is(equalTo("parent.value2")));
		assertThat(conf12.getProperty("child1.key1"), is(equalTo("child1.value1")));
		assertThat(conf12.getProperty("child1.key2"), is(equalTo("child1.value2")));
		assertThat(conf12.getProperty("child2.key1"), is(equalTo("child2.value1")));
		assertThat(conf12.getProperty("child2.key2"), is(equalTo("child2.value2")));
		assertThat(conf12.getProperty("child3.key1"), is(equalTo("child3.value1")));
		assertThat(conf12.getProperty("child3.key2"), is(equalTo("child3.value2")));
	}

	@Test
	public void shouldBeGetValueWithCorrectOrderindMethodCall() {
		System.getProperties().setProperty("value51", "A");
		System.getProperties().setProperty("value52", "B");
		System.getProperties().setProperty("value53", "C");
		PropertyLoader.getInstance().populate(null, conf13);
		assertThat(conf13.result, is(equalTo("CAB")));
	}
	
	
}