package bali.text;

import bali.Iterator;
import bali.Text;
import org.junit.Test;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CharArrayStringTest {

    private Text subject = new CharArrayString("Hello World".toCharArray());

    @Test
    public void testUppercase() throws Exception {
        assertThat(subject.uppercase(), equalTo(convert("HELLO WORLD")));
    }

    @Test
    public void testContains() throws Exception {
        assertThat(subject.contains(convert('W')), equalTo(convert(true)));
    }

    @Test
    public void testContainsNegative() throws Exception {
        assertThat(subject.contains(convert('u')), equalTo(convert(false)));
    }

    @Test
    public void testSize() throws Exception {
        assertThat(subject.size(), equalTo(convert(11)));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertThat(subject.isEmpty(), equalTo(convert(false)));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(subject.get(convert(7)), equalTo(convert('H')));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<bali.Character> i = subject.iterator();
        assertThat(i, notNullValue());
        char[] expectation = "Hello World".toCharArray();
        for (int j = 0; convert(i.hasNext()) ; j++){
            assertThat(i.next(), equalTo(convert(expectation[j])));
        }
    }

    @Test
    public void testEqualTo() throws Exception {
        assertThat(subject.equalTo(convert("Hello World")), equalTo(convert(true)));
    }

    @Test
    public void testEqualToOtherImplementation() throws Exception {
        assertThat(subject.equalTo(mock(Text.class)), equalTo(convert(false)));
    }

    @Test
    public void testNotEqualToOtherImplementation() throws Exception {
        assertThat(subject.notEqualTo(mock(Text.class)), equalTo(convert(true)));
    }

    @Test
    public void testNotEqualTo() throws Exception {
        assertThat(subject.notEqualTo(convert("Goodbye World")), equalTo(convert(true)));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(subject.toString(), equalTo("Hello World"));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(subject, equalTo(convert("Hello World")));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(subject.hashCode(), not(equalTo(convert("Goodbye World").hashCode())));
    }
}