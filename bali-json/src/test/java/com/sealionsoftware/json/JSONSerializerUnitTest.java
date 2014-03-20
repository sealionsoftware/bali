package com.sealionsoftware.json;

import bali.Integer;
import bali.collection.Collection;
import bali.collection.LinkedList;
import bali.collection.List;
import bali.collection._;
import bali.type.LazyReflectedType;
import org.junit.Test;

import java.lang.RuntimeException;

import static bali.Primitive.convert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNotNull;

/**
 * User: Richard
 * Date: 16/03/14
 */
public class JSONSerializerUnitTest {

	@Test
	public void testSerializeString(){
		JSONSerializer<bali.String> serializer = new JSONSerializer<>(null);
		assertEquals("\"TEST\"\n", convert(serializer.format(convert("TEST"))));
	}

	@Test
	public void testSerializeBoolean(){
		JSONSerializer<bali.Boolean> serializer = new JSONSerializer<>(null);
		assertEquals("true\n", convert(serializer.format(convert(true))));
		assertEquals("false\n", convert(serializer.format(convert(false))));
	}

	@Test
	public void testSerializeInteger(){
		JSONSerializer<Integer> serializer = new JSONSerializer<>(null);
		assertEquals("1\n", convert(serializer.format(convert(1))));
//		assertEquals("14562674225", convert(serializer.format(convert(14562674225l)))); //TODO
	}

//	@Test TODO
//	public void testSerializeFloat(){
//		assertEquals("1.01", convert(serializer.format(convert(1.01))));
//	}

	@Test
	public void testSerializeList(){
		JSONSerializer<Collection<bali.String>> serializer = new JSONSerializer<>(null);
		List<bali.String> list = new LinkedList<>(null, null);
		assertEquals("[\n]\n", convert(serializer.format(list)));
		list.add(convert("aString"));
		assertEquals("[\n\t\"aString\"\n]\n", convert(serializer.format(list)));
		list.add(convert("anotherString"));
		assertEquals("[\n\t\"aString\",\n\t\"anotherString\"\n]\n", convert(serializer.format(list)));
	}

	@Test
	public void testSerializeEmptyBean(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		assertEquals("{\n}\n", convert(serializer.format(new A())));
	}

	@Test
	public void testSerializeBeanWithStringProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		A a = new A();
		a.aString = convert("aValue");
		assertEquals("{\n\t\"aString\": \"aValue\"\n}\n", convert(serializer.format(a)));
	}

	@Test
	public void testSerializeBeanWithTwoProperties(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		A a = new A();
		a.aString = convert("aValue");
		a.aBoolean = convert(true);
		assertEquals("{\n\t\"aString\": \"aValue\",\n\t\"aBoolean\": true\n}\n", convert(serializer.format(a)));
	}

	@Test
	public void testSerializeBeanWithBeanProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		A a = new A();
		a.b = new B();
		assertEquals("{\n\t\"b\": {\n\t}\n}\n", convert(serializer.format(a)));
	}

	@Test
	public void testSerializeBeanWithNestedProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		B b = new B();
		b.aNumber = convert(2);
		A a = new A();
		a.b = b;
		assertEquals("{\n\t\"b\": {\n\t\t\"aNumber\": 2\n\t}\n}\n", convert(serializer.format(a)));
	}

	@Test(expected = RuntimeException.class)
	public void testSerializeBeanWithSelfReference(){
		JSONSerializer<A> serializer = new JSONSerializer<>(null);
		B b = new B();
		A a = new A();
		a.b = b;
		b.a = a;
		convert(serializer.format(a));
	}

	@Test
	public void testParseEmptyBean(){
		JSONSerializer<A> serializer = new JSONSerializer<>(new LazyReflectedType(convert(A.class.getName()), _.EMPTY));
		A a = serializer.parse(convert("{}"));
		assertNotNull(a);
	}

	@Test
	public void testParseBeanWithStringProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(new LazyReflectedType(convert(A.class.getName()), _.EMPTY));
		A a = serializer.parse(convert("{\"aString\":\"TEST\"}"));
		assertNotNull(a);
		assertNotNull(a.aString);
		assertEquals("TEST", convert(a.aString));
	}

	@Test
	public void testParseBeanWithBooleanProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(new LazyReflectedType(convert(A.class.getName()), _.EMPTY));
		A a = serializer.parse(convert("{\"aBoolean\": true}"));
		assertNotNull(a);
		assertNotNull(a.aBoolean);
		assertTrue(convert(a.aBoolean));
	}

	@Test
	public void testParseBeanWithBeanProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(new LazyReflectedType(convert(A.class.getName()), _.EMPTY));
		A a = serializer.parse(convert("{\"b\": {}}"));
		assertNotNull(a);
		assertNotNull(a.b);
	}

	@Test
	public void testParseBeanWithNestedProperty(){
		JSONSerializer<A> serializer = new JSONSerializer<>(new LazyReflectedType(convert(A.class.getName()), _.EMPTY));
		A a = serializer.parse(convert("{\"b\": {\"aNumber\":1}}"));
		assertNotNull(a);
		assertNotNull(a.b);
		assertNotNull(a.b.aNumber);
		assertEquals(1, convert(a.b.aNumber));
	}

}
