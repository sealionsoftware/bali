package bali.number;

import bali.Number;
import bali.True;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 13/06/13
 */
public class ByteUnitTest {

	@Test
	public void testConstruction() {
		Byte b = new Byte((byte) 0);
		Assert.assertEquals(b.value, 0);
	}

	@Test
	public void testAddSelf() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(b);
		Assert.assertEquals("result is not a Byte", Byte.class, result.getClass());
		Assert.assertEquals("result is not correct value", 2, ((Byte) result).value);
	}

	@Test
	public void testAddAnother() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Byte((byte) 1));
		Assert.assertEquals("result is not a Byte", Byte.class, result.getClass());
		Assert.assertEquals("result is not correct value", 2, ((Byte) result).value);
	}

	@Test
	public void testAddToShort() {
		Byte b = new Byte((byte) 127);
		Number result = b.add(new Byte((byte) 1));
		Assert.assertEquals("result is not a Short", Short.class, result.getClass());
		Assert.assertEquals("result is not correct value", java.lang.Byte.MAX_VALUE + 1, ((Short) result).value);
	}

	@Test
	public void testAddToInt() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Short(Short.MAX_VALUE));
		Assert.assertEquals("result is not a Int", Int.class, result.getClass());
		Assert.assertEquals("result is not correct value", java.lang.Short.MAX_VALUE + 1, ((Int) result).value);
	}

	@Test
	public void testAddToLong() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Int(Int.MAX_VALUE));
		Assert.assertEquals("result is not a Long", Long.class, result.getClass());
		Assert.assertEquals("result is not correct value", (long) Integer.MAX_VALUE + 1, ((Long) result).value);
	}

	@Test
	public void testAddToBigInteger() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Long(Long.MAX_VALUE));
		Assert.assertEquals("result is not a BigInteger", BigInteger.class, result.getClass());
		Assert.assertTrue("result is not correct value", Long.POSITIVE_HORIZON.equalTo(result) == True.TRUE);
	}


}
