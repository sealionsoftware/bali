package bali.number;

import bali.NotImplementedException;
import org.junit.Test;

import static bali.number.Primitive.convert;
import static bali.number.Primitive.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

public class PrimitiveTest {

    @Test
    public void testConstructor() throws Exception {
        new Primitive();
    }

    @Test
    public void testConvertInt() throws Exception {
        assertThat(convert(5), equalTo(new Int(5)));
    }

    @Test
    public void testConvertToPrimitive() throws Exception {
        assertThat(convert(new Int(5)), equalTo(5));
    }

    @Test(expected = NotImplementedException.class)
    public void testConvertUnknownToPrimitive() throws Exception {
        convert(mock(bali.Integer.class));
    }

    @Test
    public void testParse() throws Exception {
        assertThat(parse("123"), equalTo(new Int(123)));
    }

}