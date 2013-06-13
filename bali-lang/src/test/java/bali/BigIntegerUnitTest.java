package bali;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class BigIntegerUnitTest {

	private static final Number FOUR = new BigInteger(new byte[]{4});
	private static final Number TWO = new BigInteger(new byte[]{2});
	private static final Number ONE = new BigInteger(new byte[]{1});
	private static final Number ZERO = new BigInteger(new byte[]{0});
	private static final Number MINUS_ONE = new BigInteger(new byte[]{-1});

	private static final Number ONE_TWENTY_EIGHT = new BigInteger(new byte[]{0,1});

	private static final Number LARGE_POSITIVE = new BigInteger(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,1}); // 128^12 = 1180591620717411303424
	private static final Number LARGE_POSITIVE_LESS_ONE = new BigInteger(new byte[]{127,127,127,127,127,127,127,127,127,127,127,127}); // 128^12 - 1
	private static final Number LARGE_NEGATIVE = new BigInteger(new byte[]{-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-1}); // -128^12 = -1180591620717411303424
	private static final Number LARGE_NEGATIVE_PLUS_ONE = new BigInteger(new byte[]{-127,-127,-127,-127,-127,-127,-127,-127,-127,-127,-127,-127}); // -128^12 + 1

	private static final Boolean TRUE = Boolean.TRUE;

	@Test
	public void testEquality(){
		Assert.assertTrue(ONE.equalTo(new BigInteger(new byte[]{1})) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE.equalTo(new BigInteger(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,1})) == TRUE);
	}

	@Test
	public void testInversion(){
		Assert.assertTrue(ONE.equalTo(MINUS_ONE.inverse()) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE.equalTo(LARGE_NEGATIVE.inverse()) == TRUE);
	}

	@Test
	public void testMagnitude(){
		Assert.assertTrue(ONE.equalTo(MINUS_ONE.magnitude()) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE.equalTo(LARGE_NEGATIVE.magnitude()) == TRUE);
	}

	@Test
	public void testGreaterThan(){
		Assert.assertTrue(TWO.greaterThan(ONE) == TRUE);
		Assert.assertTrue(TWO.greaterThan(ZERO) == TRUE);
		Assert.assertTrue(TWO.greaterThan(MINUS_ONE) == TRUE);
	}

	@Test
	public void testAddPositive(){
		Assert.assertTrue(ONE.add(ONE).equalTo(TWO) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE_LESS_ONE.add(ONE).equalTo(LARGE_POSITIVE) == TRUE);
	}

	@Test
	public void testAddZero(){
		Assert.assertTrue(ONE.add(ZERO).equalTo(ONE) == TRUE);
	}

	@Test
	public void testAddNegative(){
		Assert.assertTrue(TWO.add(MINUS_ONE).equalTo(ONE) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE.add(MINUS_ONE).equalTo(LARGE_POSITIVE_LESS_ONE) == TRUE);
	}

	@Test
	public void testSubtractPositive(){
		Assert.assertTrue(TWO.subtract(ONE).equalTo(ONE) == TRUE);
		Assert.assertTrue(LARGE_POSITIVE.subtract(ONE).equalTo(LARGE_POSITIVE_LESS_ONE) == TRUE);
	}

	@Test
	public void testSubtractToNegative(){
		Assert.assertTrue(ONE.subtract(TWO).equalTo(MINUS_ONE) == TRUE);
	}

	@Test
	public void testAddToPositive(){
		Assert.assertTrue(MINUS_ONE.add(TWO).equalTo(ONE) == TRUE);
	}

	@Test
	public void testSubtractZero(){
		Assert.assertTrue(ONE.subtract(ZERO).equalTo(ONE) == TRUE);
	}

	@Test
	public void testSubtractNegative(){
		Assert.assertTrue(ONE.subtract(MINUS_ONE).equalTo(TWO) == TRUE);
	}

	@Test
	public void testSubtractFromLargeNegative(){
		Assert.assertTrue(LARGE_NEGATIVE.subtract(MINUS_ONE).equalTo(LARGE_NEGATIVE_PLUS_ONE) == TRUE);
	}

	@Test
	public void testMultiplyPositive(){
		Assert.assertTrue("2 x 2 = 4", TWO.multiply(TWO).equalTo(FOUR) == TRUE);
	}

	@Test
	public void testDividePositive(){
		Assert.assertTrue("4 / 2 = 2", FOUR.divide(TWO).equalTo(TWO) == TRUE);
	}

}
