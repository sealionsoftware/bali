package bali.number;

import bali.Boolean;
import bali.IdentityBoolean;
import bali.Number;
import org.junit.Assert;
import org.junit.Test;

import static bali.number.BigInteger.MAX;
import static bali.number.BigInteger.MIN;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class BigIntegerUnitTest {

	private static final bali.Number FOUR = new BigInteger(new byte[]{MIN + 4});
	private static final Number TWO = new BigInteger(new byte[]{MIN + 2});
	private static final Number ONE = new BigInteger(new byte[]{MIN + 1});
	private static final Number ZERO = new BigInteger(new byte[]{MIN});
	private static final Number MINUS_ONE = new BigInteger(new byte[]{MIN + 1}, false);

	private static final Number TWO_FIFTY_SIX = new BigInteger(new byte[]{MIN, 1});

	private static final Number LARGE_POSITIVE = new BigInteger(new byte[]{MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, 1}); // 256^12
	private static final Number LARGE_POSITIVE_LESS_ONE = new BigInteger(new byte[]{MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX}); // 256^12 - 1
	private static final Number LARGE_NEGATIVE = new BigInteger(new byte[]{MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, 1}, false); // -256^12
	private static final Number LARGE_NEGATIVE_PLUS_ONE = new BigInteger(new byte[]{MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX, MAX}); // -128^12 + 1

	private static final Boolean TRUE = IdentityBoolean.TRUE;

	@Test
	public void testEquality() {
		Assert.assertTrue("1 equalTo 1", ONE.equalTo(new BigInteger(new byte[]{MIN + 1})) == TRUE);
		Assert.assertTrue("256^12 equalTo 256^12", LARGE_POSITIVE.equalTo(new BigInteger(new byte[]{MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, MIN, 1})) == TRUE);
	}

	@Test
	public void testInversion() {
		Assert.assertTrue("1 equalTo -(-1)", ONE.equalTo(MINUS_ONE.negative()) == TRUE);
		Assert.assertTrue("256^12 equalTo -(-256^12)", LARGE_POSITIVE.equalTo(LARGE_NEGATIVE.negative()) == TRUE);
	}

	@Test
	public void testMagnitude() {
		Assert.assertTrue("1 equalTo |-1|", ONE.equalTo(MINUS_ONE.magnitude()) == TRUE);
		Assert.assertTrue("256^12 equalTo |-256^12|", LARGE_POSITIVE.equalTo(LARGE_NEGATIVE.magnitude()) == TRUE);
	}

	@Test
	public void testGreaterThan() {
		Assert.assertTrue("2 > 1", TWO.greaterThan(ONE) == TRUE);
		Assert.assertTrue("2 > 0", TWO.greaterThan(ZERO) == TRUE);
		Assert.assertTrue("2 > -1", TWO.greaterThan(MINUS_ONE) == TRUE);
	}

	@Test
	public void testAddPositive() {
		Assert.assertTrue("1 + 1 = 2", ONE.add(ONE).equalTo(TWO) == TRUE);
		Assert.assertTrue("(256^12 - 1) + 1 = 256^12", LARGE_POSITIVE_LESS_ONE.add(ONE).equalTo(LARGE_POSITIVE) == TRUE);
	}

	@Test
	public void testAddZero() {
		Assert.assertTrue("1 + 0 = 1", ONE.add(ZERO).equalTo(ONE) == TRUE);
		Assert.assertTrue("256^12 + 0 = 256^12", LARGE_POSITIVE.add(ZERO).equalTo(LARGE_POSITIVE) == TRUE);
	}

	@Test
	public void testAddNegative() {
		Assert.assertTrue("2 + (-1) = 1", TWO.add(MINUS_ONE).equalTo(ONE) == TRUE);
		Assert.assertTrue("256^12 + (-1) = (256^12 - 1)", LARGE_POSITIVE.add(MINUS_ONE).equalTo(LARGE_POSITIVE_LESS_ONE) == TRUE);
	}

	@Test
	public void testSubtractPositive() {
		Assert.assertTrue("2 - 1 = 1", TWO.subtract(ONE).equalTo(ONE) == TRUE);
		Assert.assertTrue("256^12 - 1 = (256^12 - 1)", LARGE_POSITIVE.subtract(ONE).equalTo(LARGE_POSITIVE_LESS_ONE) == TRUE);
	}

	@Test
	public void testSubtractToNegative() {
		Assert.assertTrue("1 - 2 = -1", ONE.subtract(TWO).equalTo(MINUS_ONE) == TRUE);
	}

	@Test
	public void testAddToPositive() {
		Assert.assertTrue("-1 + 2 = 1", MINUS_ONE.add(TWO).equalTo(ONE) == TRUE);
	}

	@Test
	public void testSubtractZero() {
		Assert.assertTrue("1 - 0 = 1", ONE.subtract(ZERO).equalTo(ONE) == TRUE);
	}

	@Test
	public void testSubtractNegative() {
		Assert.assertTrue("1 - (-1) = 2", ONE.subtract(MINUS_ONE).equalTo(TWO) == TRUE);
	}

	@Test
	public void testSubtractFromLargeNegative() {
		Assert.assertTrue("256^12 - (-1) = 256^12 + 1", LARGE_NEGATIVE.subtract(MINUS_ONE).equalTo(LARGE_NEGATIVE_PLUS_ONE) == TRUE);
	}

	@Test
	public void testMultiplyPositive() {
		Assert.assertTrue("2 x 2 = 4", TWO.multiply(TWO).equalTo(FOUR) == TRUE);
	}

	@Test
	public void testDividePositive() {
		Assert.assertTrue("4 / 2 = 2", FOUR.divide(TWO).equalTo(TWO) == TRUE);
	}

}
