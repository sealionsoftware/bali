package bali;

import org.junit.Assert;
import org.junit.Test;

import java.lang.*;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class BooleanUnitTest {

	private static final Boolean FALSE = Boolean.FALSE;
	private static final Boolean TRUE = Boolean.TRUE;

	@Test
	public void testEquality(){
		Assert.assertTrue(FALSE.equals(FALSE).value);
		Assert.assertTrue(TRUE.equals(TRUE).value);
		Assert.assertFalse(TRUE.equals(FALSE).value);
		Assert.assertFalse(FALSE.equals(TRUE).value);
	}

	@Test
	public void testInversion(){
		Assert.assertTrue(FALSE.not().value);
		Assert.assertFalse(TRUE.not().value);
	}

	@Test
	public void testConjunction(){
		Assert.assertTrue(TRUE.and(TRUE).value);
		Assert.assertFalse(TRUE.and(FALSE).value);
		Assert.assertFalse(FALSE.and(TRUE).value);
		Assert.assertFalse(FALSE.and(FALSE).value);
	}

	@Test
	public void testDisjunction(){
		Assert.assertTrue(TRUE.or(TRUE).value);
		Assert.assertTrue(TRUE.or(FALSE).value);
		Assert.assertTrue(FALSE.or(TRUE).value);
		Assert.assertFalse(FALSE.or(FALSE).value);
	}

	@Test
	public void testExclusiveDisjunction(){
		Assert.assertFalse(TRUE.xor(TRUE).value);
		Assert.assertTrue(TRUE.xor(FALSE).value);
		Assert.assertTrue(FALSE.xor(TRUE).value);
		Assert.assertFalse(FALSE.xor(FALSE).value);
	}


}
