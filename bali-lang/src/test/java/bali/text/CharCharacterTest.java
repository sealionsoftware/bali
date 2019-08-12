package bali.text;

import bali.Character;
import org.junit.Test;

import static bali.logic.Primitive.convert;
import static bali.text.Primitive.convert;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CharCharacterTest {

    private Character subject = CharCharacter.CHARS['d'];

    @Test
    public void testToUpperCase() {
        assertThat(subject.toUpperCase(), equalTo(convert('D')));
    }

    @Test
    public void testToLowerCase() {
        assertThat(subject.toLowerCase(), equalTo(convert('d')));
    }

    @Test
    public void testEqualTo() {
        assertThat(subject.equalTo(convert('d')), equalTo(convert(true)));
    }

    @Test
    public void testEqualToOtherCharacterImplementation() {
        assertThat(subject.equalTo(mock(Character.class)), equalTo(convert(false)));
    }

    @Test
    public void testNotEqualTo() {
        assertThat(subject.notEqualTo(convert('d')), equalTo(convert(false)));
    }

    @Test
    public void testToString() {
        assertThat(subject.toString(), equalTo("d"));
    }

    @Test
    public void testHashCode() {
        assertThat(subject.hashCode(), equalTo((int) 'd'));
    }

    @Test
    public void testGetNewLine() {
        assertThat(subject.NEW_LINE.equalTo(convert('\n')), equalTo(convert(true)));
    }
}