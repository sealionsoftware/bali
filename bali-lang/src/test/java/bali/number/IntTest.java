package bali.number;

import org.junit.Test;

import static bali.Matchers.isFalse;
import static bali.Matchers.isTrue;
import static bali.number.Primitive.convert;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class IntTest {

    private Int positive = new Int(5);
    private Int negative = new Int(-5);
    private Int zero = new Int(0);
    private Int max = new Int(Integer.MAX_VALUE);
    private Int min = new Int(Integer.MIN_VALUE);

    @Test
    public void testIsPositive() throws Exception {
        assertThat(positive.isPositive(), isTrue());
        assertThat(zero.isPositive(), isFalse());
        assertThat(negative.isPositive(), isFalse());
    }

    @Test
    public void testIsNegative() throws Exception {
        assertThat(positive.isNegative(), isFalse());
        assertThat(zero.isNegative(), isFalse());
        assertThat(negative.isNegative(), isTrue());
    }

    @Test
    public void testIsZero() throws Exception {
        assertThat(positive.isZero(), isFalse());
        assertThat(zero.isZero(), isTrue());
        assertThat(negative.isZero(), isFalse());
    }

    @Test
    public void testMagnitude() throws Exception {
        assertThat(positive.magnitude(), equalTo(positive));
        assertThat(zero.magnitude(), equalTo(zero));
        assertThat(negative.magnitude(), equalTo(positive));
    }

    @Test
    public void testNegate() throws Exception {
        assertThat(positive.negate(), equalTo(negative));
        assertThat(zero.negate(), equalTo(zero));
        assertThat(negative.negate(), equalTo(positive));
    }

    @Test(expected = ArithmeticException.class)
    public void testNegateOverflow() throws Exception {
        min.negate();
    }

    @Test
    public void testAdd() throws Exception {
        assertThat(positive.add(negative), equalTo(zero));
        assertThat(positive.add(positive), equalTo(convert(10)));
        assertThat(positive.add(zero), equalTo(positive));
    }

    @Test(expected = ArithmeticException.class)
    public void testAddOverflow() throws Exception {
        max.add(positive);
    }

    @Test(expected = NotImplementedException.class)
    public void testAddUnrecognisedImplementation() throws Exception {
        positive.add(mock(bali.Number.class));
    }

    @Test
    public void testSubtract() throws Exception {
        assertThat(positive.subtract(negative), equalTo(convert(10)));
        assertThat(positive.subtract(positive), equalTo(zero));
        assertThat(positive.subtract(zero), equalTo(positive));
    }

    @Test(expected = ArithmeticException.class)
    public void testSubtractUnderflow() throws Exception {
        min.subtract(positive);
    }

    @Test(expected = NotImplementedException.class)
    public void testSubtractUnrecognisedImplementation() throws Exception {
        positive.subtract(mock(bali.Number.class));
    }

    @Test
    public void testMultiply() throws Exception {
        assertThat(positive.multiply(negative), equalTo(convert(-25)));
        assertThat(positive.multiply(positive), equalTo(convert(25)));
        assertThat(positive.multiply(zero), equalTo(zero));
    }

    @Test(expected = ArithmeticException.class)
    public void testMultiplyOverflow() throws Exception {
        max.multiply(positive);
    }

    @Test(expected = NotImplementedException.class)
    public void testMultiplyUnrecognisedImplementation() throws Exception {
        positive.multiply(mock(bali.Number.class));
    }

    @Test
    public void testDivide() throws Exception {
        assertThat(positive.divide(negative), equalTo(convert(-1)));
        assertThat(positive.divide(positive), equalTo(convert(1)));
    }

    @Test(expected = NotImplementedException.class)
    public void testDivideUnrecognisedImplementation() throws Exception {
        positive.divide(mock(bali.Number.class));
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() throws Exception {
        positive.divide(zero);
    }

    @Test
    public void testGreaterThan() throws Exception {
        assertThat(positive.greaterThan(negative), isTrue());
        assertThat(positive.greaterThan(positive), isFalse());
        assertThat(positive.greaterThan(zero), isTrue());
    }

    @Test(expected = NotImplementedException.class)
    public void testGreaterThanUnrecognisedImplementation() throws Exception {
        positive.greaterThan(mock(bali.Number.class));
    }

    @Test
    public void testLessThan() throws Exception {
        assertThat(negative.lessThan(negative), isFalse());
        assertThat(negative.lessThan(positive), isTrue());
        assertThat(negative.lessThan(zero), isTrue());
    }

    @Test(expected = NotImplementedException.class)
    public void testLessThanUnrecognisedImplementation() throws Exception {
        positive.lessThan(mock(bali.Number.class));
    }

    @Test
    public void testEqualTo() throws Exception {
        assertThat(positive.equalTo(positive), isTrue());
        assertThat(positive.equalTo(negative), isFalse());
    }

    @Test(expected = NotImplementedException.class)
    public void testEqualToUnrecognisedImplementation() throws Exception {
        positive.equalTo(mock(bali.Number.class));
    }

    @Test
    public void testNotEqualTo() throws Exception {
        assertThat(positive.notEqualTo(positive), isFalse());
        assertThat(positive.notEqualTo(negative), isTrue());
    }

    @Test(expected = NotImplementedException.class)
    public void testNotEqualToUnrecognisedImplementation() throws Exception {
        positive.notEqualTo(mock(bali.Number.class));
    }

    @Test
    public void testIncrement() throws Exception {
        assertThat(positive.increment(), equalTo(convert(6)));
    }

    @Test(expected = ArithmeticException.class)
    public void testIncrementOverflow() throws Exception {
        max.increment();
    }

    @Test
    public void testDecrement() throws Exception {
        assertThat(positive.decrement(), equalTo(convert(4)));
    }

    @Test(expected = ArithmeticException.class)
    public void testDecrementUnderflow() throws Exception {
       min.decrement();
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(positive.equals(convert(5)), equalTo(true));
    }

    @Test
    public void testHashcode() throws Exception {
        assertThat(positive.hashCode(), equalTo(5));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(positive.toString(), containsString("5"));
    }

}