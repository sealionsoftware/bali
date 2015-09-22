package bali.logic;

import bali.Boolean;
import org.junit.Test;

import static bali.logic.Primitive.convert;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PrimitiveTest {

    @Test
    public void testConstructor() throws Exception {
        new Primitive();
    }

    @Test
    public void testConvertToPrimitive() throws Exception {
        assertThat(convert(Boolean.TRUE), equalTo(true));
    }

    @Test
    public void testConvertToObject() throws Exception {
        assertThat(convert(true), equalTo(Boolean.TRUE));
    }
}