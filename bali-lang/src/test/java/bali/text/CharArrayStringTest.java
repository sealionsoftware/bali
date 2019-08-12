package bali.text;

import bali.Character;
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
import static org.mockito.Mockito.when;

public class CharArrayStringTest {

    private Text subject = new CharArrayString("Hello World".toCharArray());

    @Test
    public void testUppercase() {
        assertThat(subject.uppercase(), equalTo(convert("HELLO WORLD")));
    }

    @Test
    public void testContains() {
        assertThat(subject.contains(convert('W')), equalTo(convert(true)));
    }

    @Test
    public void testContainsNegative() {
        assertThat(subject.contains(convert('u')), equalTo(convert(false)));
    }

    @Test
    public void testSize() {
        assertThat(subject.size(), equalTo(convert(11)));
    }

    @Test
    public void testIsEmpty() {
        assertThat(subject.isEmpty(), equalTo(convert(false)));
    }

    @Test
    public void testGet() {
        assertThat(subject.get(convert(7)), equalTo(convert('W')));
    }

    @Test
    public void testIterator() {
        Iterator<bali.Character> i = subject.iterator();
        assertThat(i, notNullValue());
        char[] expectation = "Hello World".toCharArray();
        for (int j = 0; convert(i.hasNext()) ; j++){
            assertThat(i.next(), equalTo(convert(expectation[j])));
        }
    }

    @Test
    public void testEqualTo() {
        assertThat(subject.equalTo(convert("Hello World")), equalTo(convert(true)));
    }

    @Test
    public void testEqualToOtherImplementationDifferentSize() {

        Text other = mock(Text.class);
        when(other.size()).thenReturn(convert(1));

        assertThat(subject.equalTo(other), equalTo(convert(false)));
    }

    @Test
    public void testEqualToOtherImplementationDifferentChars() {

        Text other = mock(Text.class);
        @SuppressWarnings("unchecked")
        Iterator<Character> iterator = mock(Iterator.class);
        when(other.size()).thenReturn(convert(11));
        when(other.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn(convert('G'));

        assertThat(subject.equalTo(other), equalTo(convert(false)));
    }

    @Test
    public void testEqualToOtherImplementation() {

        Text other = mock(Text.class);
        @SuppressWarnings("unchecked")
        Iterator<Character> iterator = mock(Iterator.class);
        when(other.size()).thenReturn(convert(11));
        when(other.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn(
                convert('H'), convert('e'), convert('l'), convert('l'), convert('o'),
                convert(' '),
                convert('W'), convert('o'), convert('r'), convert('l'), convert('d'));

        assertThat(subject.equalTo(other), equalTo(convert(true)));
    }

    @Test
    public void testNotEqualToOtherImplementationDifferentSize() {

        Text other = mock(Text.class);
        when(other.size()).thenReturn(convert(1));

        assertThat(subject.notEqualTo(other), equalTo(convert(true)));
    }

    @Test
    public void testNotEqualToOtherImplementationDifferentChars() {

        Text other = mock(Text.class);
        @SuppressWarnings("unchecked")
        Iterator<Character> iterator = mock(Iterator.class);
        when(other.size()).thenReturn(convert(11));
        when(other.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn(convert('G'));

        assertThat(subject.notEqualTo(other), equalTo(convert(true)));
    }

    @Test
    public void testNotEqualToOtherImplementation() {

        Text other = mock(Text.class);
        @SuppressWarnings("unchecked")
        Iterator<Character> iterator = mock(Iterator.class);
        when(other.size()).thenReturn(convert(11));
        when(other.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn(
                convert('H'), convert('e'), convert('l'), convert('l'), convert('o'),
                convert(' '),
                convert('W'), convert('o'), convert('r'), convert('l'), convert('d'));

        assertThat(subject.notEqualTo(other), equalTo(convert(false)));
    }

    @Test
    public void testNotEqualTo() {
        assertThat(subject.notEqualTo(convert("Goodbye World")), equalTo(convert(true)));
    }

    @Test
    public void testToString() {
        assertThat(subject.toString(), equalTo("Hello World"));
    }

    @Test
    public void testEquals() {
        assertThat(subject, equalTo(convert("Hello World")));
    }

    @Test
    public void testHashCode() {
        assertThat(subject.hashCode(), not(equalTo(convert("Goodbye World").hashCode())));
    }

    @Test
    public void testConcatenate() {
        assertThat(subject.concatenate(convert("!")), equalTo(convert("Hello World!")));
    }

    @Test
    public void testConcatenateOtherImplementation() {
        Text operand = mock(Text.class);
        @SuppressWarnings("unchecked")
        Iterator<Character> iterator = mock(Iterator.class);
        when(operand.size()).thenReturn(convert(1));
        when(operand.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn(convert('!'));
        assertThat(subject.concatenate(operand), equalTo(convert("Hello World!")));
    }

    @Test
    public void testHead() {
        assertThat(subject.head(convert(5)), equalTo(convert("Hello")));
    }

    @Test
    public void testHeadTakeTooMany() {
        assertThat(subject.head(convert(15)), equalTo(convert("Hello World")));
    }

    @Test
    public void testTail() {
        assertThat(subject.tail(convert(5)), equalTo(convert("World")));
    }

    @Test
    public void testTailTakeTooMany() {
        assertThat(subject.tail(convert(15)), equalTo(convert("Hello World")));
    }

}