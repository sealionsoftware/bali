package bali;

import bali.collection.Array;
import bali.collection.ValueCollection;
import org.junit.Assert;
import org.junit.Test;

import static bali.Primitive.convert;
import static bali.logic.True.TRUE;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class StringUnitTest {

	private String helloWorld = convert("Hello World");

	@Test
	public void testEqualTo(){
		Assert.assertEquals(
				"Hello World",
				convert(helloWorld)
		);
		Assert.assertEquals(
				"\"Hello World\".equalTo(\"Hello World\") -> TRUE",
				convert(helloWorld),
				convert(helloWorld)
		);
	}

	@Test
	public void testUppercase(){
		Assert.assertEquals(
				"\"Hello World\".uppercase() -> \"HELLO WORLD\"",
				convert(helloWorld.uppercase()),
				"HELLO WORLD"
		);
	}

	@Test
	public void testJoin(){

		String hello = convert("Hello ");
		String world = convert("World");
		String joined = (String) hello.join(world);

		Assert.assertEquals(
				"\"Hello \" + \"World\" -> \"Hello World\"",
				convert(helloWorld),
				convert(joined)
		);
	}

	@Test
	public void testSplit(){

		String toSplit = convert("Hellosep sepWorldsep");
		String splitter = convert("sep");
		ValueCollection<String> split = toSplit.split(splitter);

		Assert.assertEquals("\"Hellosep sepWorldsep\".split(\"sep\") - > [\"Hello\", \"World\", \"\"]",
				TRUE,
				split.equalTo(new Array<>(new String[]{
					convert("Hello"),
						convert(" "),
						convert("World"),
						convert("")
				}))
		);
	}

	@Test
	public void testHead(){
		String head = (String) helloWorld.head(convert(5));
		Assert.assertEquals(
				"\"Hello World\".head(5) -> \"Hello\"",
				"Hello",
				convert(head)
		);
	}

	@Test
	public void testTail(){
		String tail = (String) helloWorld.tail(convert(6));
		Assert.assertEquals(
				"\"Hello World\".tail(6) -> \"World\"",
				"World",
				convert(tail)
		);
	}

	@Test
	public void testContains(){
		Assert.assertEquals(
				"\"Hello World\".contains('W') -> TRUE",
				TRUE,
				helloWorld.contains(convert('W'))
		);
	}

	@Test
	public void testSize(){
		Assert.assertEquals(
				"\"Hello World\".size() -> 11",
				TRUE,
				helloWorld.size().equalTo(convert(11))
		);
	}

	@Test
	 public void testIsEmpty(){
		Assert.assertFalse(
				"\"Hello World\".isEmpty() -> FALSE",
				convert(helloWorld.isEmpty())
		);
		Assert.assertTrue(
				"\"\".isEmpty() -> TRUE",
				convert(convert("").isEmpty())
		);
	}

	@Test
	public void testGet(){
		Assert.assertEquals(
				"\"Hello World\".get(6) -> 'W'",
				'W',
				convert(helloWorld.get(convert(6)))
		);
	}

	@Test
	public void testIterator(){
		Iterator<Character> i = helloWorld.iterator();
		testNext('H', i);
		testNext('e', i);
		testNext('l', i);
		testNext('l', i);
		testNext('o', i);
		testNext(' ', i);
		testNext('W', i);
		testNext('o', i);
		testNext('r', i);
		testNext('l', i);
		testNext('d', i);
		Assert.assertFalse(convert(i.hasNext()));
		Assert.assertNull(i.next());
	}

	private void testNext(char expectation, Iterator<Character> i){
		Assert.assertTrue(convert(i.hasNext()));
		Assert.assertEquals(expectation, convert(i.next()));
	}


}
