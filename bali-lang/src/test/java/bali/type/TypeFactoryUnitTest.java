package bali.type;

import org.junit.Assert;
import org.junit.Test;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class TypeFactoryUnitTest {

	@Test
	public void testSimple(){
		String typeName = "bali.type.A";
		Type t = TypeFactory.getType(typeName);
		Assert.assertNotNull(t);
		Assert.assertEquals(typeName, convert(t.getClassName()));
		Assert.assertTrue(convert(t.getTypeArguments().isEmpty()));
		Type t2 = TypeFactory.getType(typeName);
		Assert.assertSame(t, t2);
	}

	@Test
	public <P> void testParameterised(){
		String typeName = "bali.type.B[bali.type.A]";
		Type t = TypeFactory.getType(typeName);
		Assert.assertNotNull(t);
		Assert.assertEquals("bali.type.B", convert(t.getClassName()));
		Assert.assertFalse(convert(t.getTypeArguments().isEmpty()));
		Assert.assertEquals(1, convert(t.getTypeArguments().size()));
		Type parameterType = t.getTypeArguments().get(convert(1));
		Assert.assertNotNull(parameterType);
		Assert.assertEquals("bali.type.A", convert(parameterType.getClassName()));
		Assert.assertTrue(convert(parameterType.getTypeArguments().isEmpty()));
	}

	@Test
	public void testSelfParameterised(){
		String typeName = "bali.type.B[bali.type.B[bali.type.A]]";
		Type t = TypeFactory.getType(typeName);
		Assert.assertNotNull(t);
		Assert.assertEquals("bali.type.B", convert(t.getClassName()));
		Assert.assertFalse(convert(t.getTypeArguments().isEmpty()));
		Assert.assertEquals(1, convert(t.getTypeArguments().size()));
		Type parameterType = t.getTypeArguments().get(convert(1));
		Assert.assertNotNull(parameterType);
		Assert.assertEquals("bali.type.B", convert(parameterType.getClassName()));
		Assert.assertFalse(convert(parameterType.getTypeArguments().isEmpty()));
		Type subParameterType = parameterType.getTypeArguments().get(convert(1));
		Assert.assertNotNull(subParameterType);
		Assert.assertEquals("bali.type.A", convert(subParameterType.getClassName()));
	}

	@Test
	public void testSelfParameterisedMultiple(){
		String typeName = "bali.type.B[bali.type.C[bali.type.A,bali.type.A]]";
		Type t = TypeFactory.getType(typeName);
		Assert.assertNotNull(t);
		Assert.assertEquals("bali.type.B", convert(t.getClassName()));
		Type parameterType = t.getTypeArguments().get(convert(1));
		Assert.assertEquals("bali.type.C", convert(parameterType.getClassName()));
		Assert.assertFalse(convert(parameterType.getTypeArguments().isEmpty()));
		Assert.assertEquals(2, convert(parameterType.getTypeArguments().size()));
		Type parameterTypeOne = parameterType.getTypeArguments().get(convert(1));
		Assert.assertNotNull(parameterTypeOne);
		Assert.assertEquals("bali.type.A", convert(parameterTypeOne.getClassName()));
		Type parameterTypeTwo = parameterType.getTypeArguments().get(convert(1));
		Assert.assertNotNull(parameterTypeTwo);
		Assert.assertEquals("bali.type.A", convert(parameterTypeTwo.getClassName()));
	}



}
