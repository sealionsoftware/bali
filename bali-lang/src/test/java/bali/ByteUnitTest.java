package bali;

import com.sealionsoftware.bali.IdentityBoolean;
import com.sealionsoftware.bali.number.*;
import com.sealionsoftware.bali.number.Byte;
import com.sealionsoftware.bali.number.Integer;
import com.sealionsoftware.bali.number.Short;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 13/06/13
 */
public class ByteUnitTest {

	@Test
	public void testConstruction() {
		com.sealionsoftware.bali.number.Byte b = new Byte((byte) 0);
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
		Assert.assertEquals("result is not a Short", com.sealionsoftware.bali.number.Short.class, result.getClass());
		Assert.assertEquals("result is not correct value", java.lang.Byte.MAX_VALUE + 1, ((Short) result).value);
	}

	@Test
	public void testAddToInteger() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Short(java.lang.Short.MAX_VALUE));
		Assert.assertEquals("result is not a Integer", com.sealionsoftware.bali.number.Integer.class, result.getClass());
		Assert.assertEquals("result is not correct value", java.lang.Short.MAX_VALUE + 1, ((Integer) result).value);
	}

	@Test
	public void testAddToLong() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new Integer(java.lang.Integer.MAX_VALUE));
		Assert.assertEquals("result is not a Long", com.sealionsoftware.bali.number.Long.class, result.getClass());
		Assert.assertEquals("result is not correct value", (long) java.lang.Integer.MAX_VALUE + 1, ((com.sealionsoftware.bali.number.Long) result).value);
	}

	@Test
	public void testAddToBigInteger() {
		Byte b = new Byte((byte) 1);
		Number result = b.add(new com.sealionsoftware.bali.number.Long(java.lang.Long.MAX_VALUE));
		Assert.assertEquals("result is not a BigInteger", BigInteger.class, result.getClass());
		Assert.assertTrue("result is not correct value", com.sealionsoftware.bali.number.Long.POSITIVE_HORIZON.equalTo(result) == IdentityBoolean.TRUE);
	}


}
