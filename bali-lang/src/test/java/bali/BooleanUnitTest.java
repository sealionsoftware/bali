package bali;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class BooleanUnitTest {

	private static final Boolean FALSE = Boolean.FALSE;
	private static final Boolean TRUE = Boolean.TRUE;

	@Test
	public void testEquality(){
		Assert.assertEquals("FALSE equalTo FALSE", FALSE.equalTo(FALSE), TRUE);
		Assert.assertEquals("TRUE equalTo TRUE", TRUE.equalTo(TRUE), TRUE);
		Assert.assertEquals("TRUE equalTo FALSE", TRUE.equalTo(FALSE), FALSE);
		Assert.assertEquals("FALSE equalTo TRUE", FALSE.equalTo(TRUE), FALSE);
	}

	@Test
	public void testInversion(){
		Assert.assertEquals("not FALSE", FALSE.not(), TRUE);
		Assert.assertEquals("not TRUE", TRUE.not(), FALSE);
	}

	@Test
	public void testConjunction(){
		Assert.assertEquals("TRUE and TRUE", TRUE.and(TRUE), TRUE);
		Assert.assertEquals("TRUE and FALSE", TRUE.and(FALSE), FALSE);
		Assert.assertEquals("FALSE and TRUE", FALSE.and(TRUE), FALSE);
		Assert.assertEquals("FALSE and FALSE", FALSE.and(FALSE), FALSE);
	}

	@Test
	public void testDisjunction(){
		Assert.assertEquals("TRUE or TRUE", TRUE.or(TRUE), TRUE);
		Assert.assertEquals("TRUE or FALSE", TRUE.or(FALSE), TRUE);
		Assert.assertEquals("FALSE or TRUE", FALSE.or(TRUE), TRUE);
		Assert.assertEquals("FALSE or FALSE", FALSE.or(FALSE), FALSE);
	}

	@Test
	public void testExclusiveDisjunction(){
		Assert.assertEquals("TRUE xor TRUE", TRUE.xor(TRUE), FALSE);
		Assert.assertEquals("TRUE xor FALSE", TRUE.xor(FALSE), TRUE);
		Assert.assertEquals("FALSE xor TRUE", FALSE.xor(TRUE), TRUE);
		Assert.assertEquals("FALSE xor FALSE", FALSE.xor(FALSE), FALSE);
	}


}
