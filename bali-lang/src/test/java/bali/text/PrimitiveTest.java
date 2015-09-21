package bali.text;

import org.junit.Test;

import static bali.text.Primitive.convert;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PrimitiveTest {

    @Test
    public void testConvertToCharacterObject() throws Exception {
        assertThat(convert('a'), equalTo(CharCharacter.CHARS['a']));
    }

    @Test
    public void testConvertToCharacterPrimitive() throws Exception {
        assertThat(convert(CharCharacter.CHARS['a']), equalTo('a'));
    }

    @Test
    public void testConvertToTextObject() throws Exception {
        assertThat(convert("Hello World"), equalTo(new CharArrayString("Hello World".toCharArray())));
    }

    @Test
    public void testConvertToString() throws Exception {
        assertThat(convert(new CharArrayString("Hello World".toCharArray())), equalTo("Hello World"));
    }
}