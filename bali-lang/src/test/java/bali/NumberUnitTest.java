package bali;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class NumberUnitTest {

	private static final Number TWO = new Number(new byte[]{2});
	private static final Number ONE = new Number(new byte[]{1});
	private static final Number ZERO = new Number(new byte[]{0});
	private static final Number MINUS_ONE = new Number(new byte[]{-1});

	private static final Number ONE_TWENTY_EIGHT = new Number(new byte[]{0,1});

	private static final Number LARGE_POSITIVE = new Number(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,1}); // 128^12 = 1180591620717411303424
	private static final Number LARGE_POSITIVE_LESS_ONE = new Number(new byte[]{127,127,127,127,127,127,127,127,127,127,127,127}); // 128^12 - 1
	private static final Number LARGE_NEGATIVE = new Number(new byte[]{-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-0,-1}); // -128^12 = -1180591620717411303424
	private static final Number LARGE_NEGATIVE_PLUS_ONE = new Number(new byte[]{-127,-127,-127,-127,-127,-127,-127,-127,-127,-127,-127,-127}); // -128^12 + 1

	@Test
	public void testConstructionFromInt(){
		Assert.assertTrue(ONE.equals(new Number(1)).value);
		Assert.assertTrue(ONE_TWENTY_EIGHT.equals(new Number(128)).value);
	}

	@Test
	public void testEquality(){
		Assert.assertTrue(ONE.equals(new Number(new byte[]{1})).value);
		Assert.assertTrue(LARGE_POSITIVE.equals(new Number(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,1})).value);
	}

	@Test
	public void testInversion(){
		Assert.assertTrue(ONE.equals(MINUS_ONE.inverse()).value);
		Assert.assertTrue(LARGE_POSITIVE.equals(LARGE_NEGATIVE.inverse()).value);
	}

	@Test
	public void testMagnitude(){
		Assert.assertTrue(ONE.equals(MINUS_ONE.magnitude()).value);
		Assert.assertTrue(LARGE_POSITIVE.equals(LARGE_NEGATIVE.magnitude()).value);
	}

	@Test
	public void testGreaterThan(){
		Assert.assertTrue(TWO.greaterThan(ONE).value);
		Assert.assertTrue(TWO.greaterThan(ZERO).value);
		Assert.assertTrue(TWO.greaterThan(MINUS_ONE).value);
	}

	@Test
	public void testAddPositive(){
		Assert.assertTrue(ONE.add(ONE).equals(TWO).value);
		Assert.assertTrue(LARGE_POSITIVE_LESS_ONE.add(ONE).equals(LARGE_POSITIVE).value);
	}

	@Test
	public void testAddZero(){
		Assert.assertTrue(ONE.add(ZERO).equals(ONE).value);
	}

	@Test
	public void testAddNegative(){
		Assert.assertTrue(TWO.add(MINUS_ONE).equals(ONE).value);
		Assert.assertTrue(LARGE_POSITIVE.add(MINUS_ONE).equals(LARGE_POSITIVE_LESS_ONE).value);
	}

	@Test
	public void testSubtractPositive(){
		Assert.assertTrue(TWO.subtract(ONE).equals(ONE).value);
		Assert.assertTrue(LARGE_POSITIVE.subtract(ONE).equals(LARGE_POSITIVE_LESS_ONE).value);
	}

	@Test
	public void testSubtractToNegative(){
		Assert.assertTrue(ONE.subtract(TWO).equals(MINUS_ONE).value);
	}

	@Test
	public void testAddToPositive(){
		Assert.assertTrue(MINUS_ONE.add(TWO).equals(ONE).value);
	}

	@Test
	public void testSubtractZero(){
		Assert.assertTrue(ONE.subtract(ZERO).equals(ONE).value);
	}

	@Test
	public void testSubtractNegative(){
		Assert.assertTrue(ONE.subtract(MINUS_ONE).equals(TWO).value);
	}

	@Test
	public void testSubtractFromLargeNegative(){
		Assert.assertTrue(LARGE_NEGATIVE.subtract(MINUS_ONE).equals(LARGE_NEGATIVE_PLUS_ONE).value);
	}

}
