package bali.collection;

import bali.Iterator;
import bali.String;
import org.junit.Test;

import static bali.Primitive.convert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * User: Richard
 * Date: 13/03/14
 */
public class LinkedListUnitTest {

	private LinkedList<String> list = new LinkedList<>(null, new Array<>(new String[]{
			convert("TEST1"),
			convert("TEST2"),
			convert("TEST3")
	}));

	@Test
	public void testIsEmpty(){
		assertFalse(convert(list.isEmpty()));
		assertTrue(convert(new LinkedList<>(null, null).isEmpty()));
	}

	@Test
	public void testSize(){
		assertEquals(3, convert(list.size()));
	}

	@Test
	public void testAdd(){
		list.add(convert("TEST4"));
		assertEquals(4, convert(list.size()));
	}

	@Test
	public void testGet(){
		assertNull(list.get(convert(0)));
		assertEquals("TEST1", convert(list.get(convert(1))));
		assertEquals("TEST2", convert(list.get(convert(2))));
		assertEquals("TEST3", convert(list.get(convert(3))));
		assertNull(list.get(convert(4)));
	}

	@Test
	public void testSet(){
		list.set(convert(2), convert("CHANGED"));
		assertEquals("CHANGED", convert(list.get(convert(2))));
		assertEquals(3, convert(list.size()));
	}

	@Test
	public void testIterator(){
		Iterator<String> i = list.iterator();
		assertNotNull(i);

		assertTrue(convert(i.hasNext()));
		assertEquals("TEST1", convert(i.next()));

		assertTrue(convert(i.hasNext()));
		assertEquals("TEST2", convert(i.next()));

		assertTrue(convert(i.hasNext()));
		assertEquals("TEST3", convert(i.next()));

		assertFalse(convert(i.hasNext()));
		assertNull(i.next());
	}

	@Test
	public void testRemove(){
		list.remove(convert(2));
		assertEquals(2, convert(list.size()));
		assertEquals("TEST3", convert(list.get(convert(2))));

		list.remove(convert(1));
		assertEquals(1, convert(list.size()));
		assertEquals("TEST3", convert(list.get(convert(1))));

		list.remove(convert(1));
		assertTrue(convert(list.isEmpty()));
	}

	@Test
	public void testJoin(){

		LinkedList<String> listTwo = new LinkedList<>(null, new Array<>(new String[]{
				convert("TEST4"),
				convert("TEST5"),
				convert("TEST6")
		}));

		list.join(listTwo);

		assertEquals(6, convert(list.size()));
		assertNull(list.get(convert(0)));
		assertEquals("TEST1", convert(list.get(convert(1))));
		assertEquals("TEST2", convert(list.get(convert(2))));
		assertEquals("TEST3", convert(list.get(convert(3))));
		assertEquals("TEST4", convert(list.get(convert(4))));
		assertEquals("TEST5", convert(list.get(convert(5))));
		assertEquals("TEST6", convert(list.get(convert(6))));
		assertNull(list.get(convert(7)));
	}

	@Test
	public void testHead(){

		list.head(convert(5));
		assertEquals(3, convert(list.size()));

		list.head(convert(3));
		assertEquals(3, convert(list.size()));

		list.head(convert(2));
		assertEquals(2, convert(list.size()));

		list.head(convert(1));
		assertEquals(1, convert(list.size()));

		list.head(convert(-1));
		assertEquals(0, convert(list.size()));

	}

	@Test
	public void testTail(){

		list.tail(convert(-1));
		assertEquals(3, convert(list.size()));

		list.tail(convert(1));
		assertEquals(3, convert(list.size()));

		list.tail(convert(2));
		assertEquals(2, convert(list.size()));

		list.tail(convert(2));
		assertEquals(1, convert(list.size()));

		list.tail(convert(2));
		assertEquals(0, convert(list.size()));

	}


}
