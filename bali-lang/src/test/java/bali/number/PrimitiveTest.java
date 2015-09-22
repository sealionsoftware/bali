package bali.number;

import org.junit.Test;

import static bali.number.Primitive.convert;

public class PrimitiveTest {

    @Test
    public void testConstructor() throws Exception {
        new Primitive();
    }

    @Test
    public void testConvertInt() throws Exception {
        convert(3);
    }

    @Test
    public void testConvertLong() throws Exception {
        convert(3l);
    }

    @Test
    public void testConvertToPrimitive() throws Exception {
        convert(null);
    }
}