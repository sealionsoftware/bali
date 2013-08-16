package bali.number;

import bali.Number;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class NumberFactoryUnitTest {

	private NumberFactory numberFactory = NumberFactory.NUMBER_FACTORY;

	private void testForByte(byte in){
		bali.Number number = numberFactory.forByte(in);
		Assert.assertTrue(in + " is not a Byte", number instanceof Byte);
		Assert.assertEquals(in + " is incorrect value", in, ((Byte) number).value);
		testForString(java.lang.Byte.toString(in).toCharArray(), Byte.class);
	}

	private void testForShort(short in){
		Number number = numberFactory.forShort(in);
		Assert.assertTrue(in + " is not a Short", number instanceof Short);
		Assert.assertEquals(in + " is incorrect value", in, ((Short) number).value);
		testForString(java.lang.Short.toString(in).toCharArray(), Short.class);
	}

	private void testForInteger(int in){
		Number number = numberFactory.forInt(in);
		Assert.assertTrue(in + " is not an Integer", number instanceof Int);
		Assert.assertEquals(in + " is incorrect value", in, ((Int) number).value);
		testForString(java.lang.Integer.toString(in).toCharArray(), Integer.class);
	}

	private void testForLong(long in){
		Number number = numberFactory.forLong(in);
		Assert.assertTrue(in + " is not a Long", number instanceof Long);
		Assert.assertEquals(in + " is incorrect value", in, ((Long) number).value);
		testForString(java.lang.Long.toString(in).toCharArray(), Long.class);
	}

	private void testForString(char[] in, Class<? extends Number> expectedType){
		Number number = numberFactory.forDecimalString(in);
		Assert.assertEquals("Number is of wrong type",  expectedType, number.getClass());
	}

	@Test
	public void testForZero(){
		testForByte((byte) 0);
	}

	@Test
	public void testForByteHigh(){
		testForByte(java.lang.Byte.MAX_VALUE);
	}

	@Test
	public void testForByteLow(){
		testForByte(java.lang.Byte.MIN_VALUE);
	}

	@Test
	public void testForShortLowPositive(){
		testForShort((short) (java.lang.Byte.MAX_VALUE + 1));
	}

	@Test
	public void testForShortLowNegative(){
		testForShort((short) (java.lang.Byte.MIN_VALUE - 1));
	}

	@Test
	public void testForShortHighPositive(){
		testForShort(java.lang.Short.MAX_VALUE);
	}

	@Test
	public void testForShortHighNegative(){
		testForShort(java.lang.Short.MIN_VALUE);
	}

	@Test
	public void testForIntegerLowPositive(){
		testForInteger(java.lang.Short.MAX_VALUE + 1);
	}

	@Test
	public void testForIntegerLowNegative(){
		testForInteger(java.lang.Short.MIN_VALUE - 1);
	}

	@Test
	public void testForIntegerHighPositive(){
		testForInteger(java.lang.Integer.MAX_VALUE);
	}

	@Test
	public void testForIntegerHighNegative(){
		testForInteger(java.lang.Integer.MIN_VALUE);
	}

	@Test
	public void testForLongLowPositive(){
		testForLong((long) java.lang.Integer.MAX_VALUE + 1);
	}

	@Test
	public void testForLongLowNegative(){
		testForLong((long) java.lang.Integer.MIN_VALUE - 1);
	}

	@Test
	public void testForLongHighPositive(){
		testForLong(java.lang.Long.MAX_VALUE);
	}

	@Test
	public void testForLongHighNegative(){
		testForLong(java.lang.Long.MIN_VALUE);
	}

	@Test
	public void testForBigIntegerPositive(){
		char[] chars = java.lang.Long.toString(java.lang.Long.MAX_VALUE).toCharArray();
		chars[chars.length - 1] = (char) (chars[chars.length - 1] + 1); // This works because the last digit != 9
		testForString(chars, BigInteger.class);
	}

	@Test
	public void testForBigIntegerNegative(){
		char[] chars = java.lang.Long.toString(java.lang.Long.MAX_VALUE).toCharArray();
		chars[chars.length - 1] = (char) (chars[chars.length - 1] + 1); // This works because the last digit != 9
		testForString(chars, BigInteger.class);
	}

}
