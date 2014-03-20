package bali.type;

import bali.collection.Array;
import bali.collection.Collection;
import bali.collection.LinkedList;
import bali.collection.List;
import bali.collection._;
import org.junit.Assert;
import org.junit.Test;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class LazyReflectedTypeUnitTest {

	@Test
	public void testSimple(){
		String typeName = "bali.type.A";
		Type t = new LazyReflectedType<>(convert(typeName), _.EMPTY);
		Assert.assertNotNull(t);
		Assert.assertEquals(typeName, convert(t.getClassName()));
		Assert.assertTrue(convert(t.getTypeArguments().isEmpty()));

		Collection<Declaration> parameters = t.getParameters();
		Assert.assertNotNull(parameters);
		Assert.assertFalse(convert(parameters.isEmpty()));
		Assert.assertEquals(1, convert(parameters.size()));

		Declaration parameter = parameters.get(convert(1));
		Assert.assertNotNull(parameter);
		Assert.assertEquals("aString", convert(parameter.name));
		Type parameterType = parameter.type;
		Assert.assertNotNull(parameterType);
		Assert.assertEquals("bali.String", convert(parameterType.getClassName()));
		Assert.assertEquals(0, convert(parameterType.getTypeArguments().size()));
	}

	@Test
	public void testParameterised(){
		List<Type> arguments = new LinkedList<>(null, null);
		arguments.add(new LazyReflectedType<>(convert("bali.type.A"), _.EMPTY));
		Type t = new LazyReflectedType<>(convert("bali.type.B"), arguments);
		Assert.assertNotNull(t);
		Assert.assertEquals("bali.type.B", convert(t.getClassName()));
		Assert.assertFalse(convert(t.getTypeArguments().isEmpty()));

		Collection<Declaration> parameters = t.getParameters();
		Assert.assertNotNull(parameters);
		Assert.assertFalse(convert(parameters.isEmpty()));
		Assert.assertEquals(1, convert(parameters.size()));

		Declaration parameter = parameters.get(convert(1));
		Assert.assertNotNull(parameter);
		Assert.assertEquals("T", convert(parameter.name));
		Type parameterType = parameter.type;
		Assert.assertNotNull(parameterType);
		Assert.assertEquals("bali.type.A", convert(parameterType.getClassName()));
		Assert.assertEquals(0, convert(parameterType.getTypeArguments().size()));
	}

}
