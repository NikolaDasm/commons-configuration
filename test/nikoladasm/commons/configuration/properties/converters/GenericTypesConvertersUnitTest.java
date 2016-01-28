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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import nikoladasm.commons.configuration.properties.converters.Converter;
import nikoladasm.commons.configuration.properties.converters.Converters;

public class GenericTypesConvertersUnitTest {

	private Converters converters = new Converters();

	@Test
	public void shouldBeGetCorrectAtomicReferenceOfIntegerConverter() throws Exception {
		Converter<AtomicReference<Integer>> converter = converters.converter(AtomicReference.class, new Class<?>[]{Integer.class});
		AtomicReference<Integer> result = converter.convert("25");
		assertThat(result.get(), is(equalTo(25)));
	}

	@Test
	public void shouldBeGetCorrectListOfIntegerConverter() throws Exception {
		Converter<List<Integer>> converter = converters.converter(List.class, new Class<?>[]{Integer.class});
		List<Integer> result = converter.convert("25,48, 54");
		List<Integer> expected = new LinkedList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectCollectionOfIntegerConverter() throws Exception {
		Converter<Collection<Integer>> converter = converters.converter(Collection.class, new Class<?>[]{Integer.class});
		Collection<Integer> result = converter.convert("25,48, 54");
		Collection<Integer> expected = new LinkedList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectSetOfIntegerConverter() throws Exception {
		Converter<Set<Integer>> converter = converters.converter(Set.class, new Class<?>[]{Integer.class});
		Set<Integer> result = converter.convert("25,48, 54");
		Set<Integer> expected = new HashSet<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectHashSetOfIntegerConverter() throws Exception {
		Converter<HashSet<Integer>> converter = converters.converter(HashSet.class, new Class<?>[]{Integer.class});
		HashSet<Integer> result = converter.convert("25,48, 54");
		HashSet<Integer> expected = new HashSet<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectArrayListOfIntegerConverter() throws Exception {
		Converter<ArrayList<Integer>> converter = converters.converter(ArrayList.class, new Class<?>[]{Integer.class});
		ArrayList<Integer> result = converter.convert("25,48, 54");
		ArrayList<Integer> expected = new ArrayList<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectTreeSetOfIntegerConverter() throws Exception {
		Converter<TreeSet<Integer>> converter = converters.converter(TreeSet.class, new Class<?>[]{Integer.class});
		TreeSet<Integer> result = converter.convert("25,48, 54");
		TreeSet<Integer> expected = new TreeSet<>();
		expected.add(25);
		expected.add(48);
		expected.add(54);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectMapOfIntegerBooleanConverter() throws Exception {
		Converter<Map<Integer,Boolean>> converter = converters.converter(Map.class, new Class<?>[]{Integer.class, Boolean.class});
		Map<Integer,Boolean> result = converter.convert("25:true,48:true, 54:false");
		Map<Integer,Boolean> expected = new HashMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectHashMapOfIntegerBooleanConverter() throws Exception {
		Converter<HashMap<Integer,Boolean>> converter = converters.converter(HashMap.class, new Class<?>[]{Integer.class, Boolean.class});
		HashMap<Integer,Boolean> result = converter.convert("25:true,48:true, 54:false");
		HashMap<Integer,Boolean> expected = new HashMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(result, is(equalTo(expected)));
	}

	@Test
	public void shouldBeGetCorrectTreeMapOfIntegerBooleanConverter() throws Exception {
		Converter<TreeMap<Integer,Boolean>> converter = converters.converter(TreeMap.class, new Class<?>[]{Integer.class, Boolean.class});
		TreeMap<Integer,Boolean> result = converter.convert("25:true,48:true, 54:false");
		TreeMap<Integer,Boolean> expected = new TreeMap<>();
		expected.put(25,true);
		expected.put(48,true);
		expected.put(54,false);
		assertThat(result, is(equalTo(expected)));
	}
}
