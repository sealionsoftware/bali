package bali.collection;

import bali.Iterator;
import bali.Text;
import org.junit.Test;

import static bali.Matchers.isFalse;
import static bali.Matchers.isTrue;
import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ArrayTest {

    private Array<Text> subject = new Array<>(convert("Hello"), convert("World"));

    @Test
    public void testSize() throws Exception {
        assertThat(subject.size(), equalTo(convert(2)));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertThat(subject.isEmpty(), isFalse());
    }

    @Test
    public void testGet() throws Exception {
        assertThat(subject.get(convert(1)), equalTo(convert("Hello")));
    }

    @Test
    public void testIterator() throws Exception {

        Iterator<Text> i = subject.iterator();
        assertThat(i.hasNext(), isTrue());
        assertThat(i.next(), equalTo(convert("Hello")));
        assertThat(i.hasNext(), isTrue());
        assertThat(i.next(), equalTo(convert("World")));
        assertThat(i.hasNext(), isFalse());
        assertThat(i.next(), nullValue());

    }

    @Test
    public void testToString(){
        assertThat(subject.toString(), equalTo("[Hello, World]"));
    }

    @Test
    public void testEquals(){
        assertThat(subject.equals(new Array<>(convert("Hello"), convert("World"))), equalTo(true));
    }

}