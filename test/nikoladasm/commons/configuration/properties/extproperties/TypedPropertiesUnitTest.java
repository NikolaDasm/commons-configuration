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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import nikoladasm.commons.configuration.properties.extproperties.TypedProperties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TypedPropertiesUnitTest {

	private static enum TestEnum {
		ONE,
		TWO,
		THREE
	}

	TypedProperties properties = new TypedProperties();
	
	@Test
	public void shouldBeGetCorrectByteValue() {
		properties.setProperty("key", "25");
		assertThat(properties.getByte("key"), is(equalTo((byte)25)));
	}

	@Test
	public void shouldBeGetCorrectShortValue() {
		properties.setProperty("key", "525");
		assertThat(properties.getShort("key"), is(equalTo((short)525)));
	}

	@Test
	public void shouldBeGetCorrectIntegerValue() {
		properties.setProperty("key", "12345525");
		assertThat(properties.getInteger("key"), is(equalTo(12345525)));
	}

	@Test
	public void shouldBeGetCorrectLongValue() {
		properties.setProperty("key", "100000012345525");
		assertThat(properties.getLong("key"), is(equalTo(100000012345525L)));
	}

	@Test
	public void shouldBeGetCorrectFloatValue() {
		properties.setProperty("key", "338.25");
		assertThat(properties.getFloat("key"), is(equalTo(338.25F)));
	}

	@Test
	public void shouldBeGetCorrectDoubleValue() {
		properties.setProperty("key", "338.2500000444");
		assertThat(properties.getDouble("key"), is(equalTo(338.2500000444)));
	}

	@Test
	public void shouldBeGetCorrectCharacterValue() {
		properties.setProperty("key", "c");
		assertThat(properties.getCharacter("key"), is(equalTo('c')));
	}

	@Test
	public void shouldBeGetCorrectBooleanValue() {
		properties.setProperty("key", "true");
		assertThat(properties.getBoolean("key"), is(equalTo(true)));
	}

	@Test
	public void shouldBeGetCorrectBigDecimalValue() {
		properties.setProperty("key", "100000000000000000000");
		assertThat(properties.getBigDecimal("key"), is(equalTo(new BigDecimal("100000000000000000000"))));
	}

	@Test
	public void shouldBeGetCorrectBigIntegerValue() {
		properties.setProperty("key", "100000000000000000000");
		assertThat(properties.getBigInteger("key"), is(equalTo(new BigInteger("100000000000000000000"))));
	}

	@Test
	public void shouldBeGetCorrectCharsetValue() {
		properties.setProperty("key", "utf-8");
		assertThat(properties.getCharset("key"), is(equalTo(UTF_8)));
	}

	@Test
	public void shouldBeGetCorrectFileValue() {
		properties.setProperty("key", "resources/res1.properties");
		assertThat(properties.getFile("key"), is(equalTo(new File("resources/res1.properties"))));
	}

	@Test
	public void shouldBeGetCorrectLocaleValue() {
		properties.setProperty("key", "en");
		assertThat(properties.getLocale("key"), is(equalTo(Locale.forLanguageTag("en"))));
	}

	@Test
	public void shouldBeGetCorrectPathValue() {
		properties.setProperty("key", "resources/res1.properties");
		assertThat(properties.getPath("key"), is(equalTo(Paths.get("resources/res1.properties"))));
	}

	@Test
	public void shouldBeGetCorrectURIValue() throws URISyntaxException {
		properties.setProperty("key", "http:/example.com/");
		assertThat(properties.getURI("key"), is(equalTo(new URI("http:/example.com/"))));
	}

	@Test
	public void shouldBeGetCorrectURLValue() throws MalformedURLException {
		properties.setProperty("key", "http:/example.com/");
		assertThat(properties.getURL("key"), is(equalTo(new URL("http:/example.com/"))));
	}

	@Test
	public void shouldBeGetCorrectArrayOfIntegerValue() {
		properties.setProperty("key", "25,48, 54");
		assertThat(properties.getArray(Integer[].class, "key"), is(equalTo(new Integer[]{25,48,54})));
	}

	@Test
	public void shouldBeGetCorrectEnumValue() {
		properties.setProperty("key", "two");
		assertThat(properties.getEnum(TestEnum.class, "key"), is(equalTo(TestEnum.TWO)));
	}

	@Test
	public void shouldBeGetCorrectAtomicBooleanValue() {
		properties.setProperty("key", "true");
		assertThat(properties.getAtomicBoolean("key").get(), is(equalTo(true)));
	}

	@Test
	public void shouldBeGetCorrectAtomicIntegerValue() {
		properties.setProperty("key", "10583");
		assertThat(properties.getAtomicInteger("key").get(), is(equalTo(10583)));
	}

	@Test
	public void shouldBeGetCorrectAtomicLongValue() {
		properties.setProperty("key", "9999910583");
		assertThat(properties.getAtomicLong("key").get(), is(equalTo(9999910583L)));
	}

	@Test
	public void shouldBeGetCorrectPatternValue() {
		properties.setProperty("key", "^ab*c?d$");
		assertThat(properties.getPattern("key").pattern(), is(equalTo(Pattern.compile("^ab*c?d$").pattern())));
	}

	@Test
	public void shouldBeGetCorrectAtomicReferenceOfIntegerConverter() throws Exception {
		properties.setProperty("key", "12345525");
		assertThat(properties.getAtomicReference(Integer.class, "key").get(), is(equalTo(12345525)));
	}

	@Test
	public void shouldBeGetCorrectListOfIntegerValue() {
		properties.setProperty("key", "25,48, 54");
		List<Integer> expected = new LinkedList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(properties.getList(Integer.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectCollectionOfIntegerValue() throws Exception {
		properties.setProperty("key", "25,48, 54");
		Collection<Integer> expected = new LinkedList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(properties.getCollection(Integer.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectSetOfIntegerValue() throws Exception {
		properties.setProperty("key", "25,48, 54");
		Set<Integer> expected = new HashSet<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(properties.getSet(Integer.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectArrayListOfIntegerValue() throws Exception {
		properties.setProperty("key", "25,48, 54");
		ArrayList<Integer> expected = new ArrayList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(properties.getArrayList(Integer.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectTreeSetOfIntegerConverter() throws Exception {
		properties.setProperty("key", "25,48, 54");
		TreeSet<Integer> expected = new TreeSet<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(properties.getTreeSet(Integer.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectMapOfIntegerBooleanValue() throws Exception {
		properties.setProperty("key", "25:true,48:true, 54:false");
		Map<Integer,Boolean> expected = new HashMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(properties.getMap(Integer.class, Boolean.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectHashMapOfIntegerBooleanConverter() throws Exception {
		properties.setProperty("key", "25:true,48:true, 54:false");
		HashMap<Integer,Boolean> expected = new HashMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(properties.getHashMap(Integer.class, Boolean.class, "key"), is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectTreeMapOfIntegerBooleanConverter() throws Exception {
		properties.setProperty("key", "25:true,48:true, 54:false");
		TreeMap<Integer,Boolean> expected = new TreeMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(properties.getTreeMap(Integer.class, Boolean.class, "key"), is(equalTo(expected)));
	}
}
