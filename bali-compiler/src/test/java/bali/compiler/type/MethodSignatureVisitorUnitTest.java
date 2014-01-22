package bali.compiler.type;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class MethodSignatureVisitorUnitTest {

	private MethodSignatureVisitor visitor = new MethodSignatureVisitor(new TypeLibrary(), new HashMap<String, Site>(), new TypeData(), Arrays.asList(new TypeData()));

	@Test
	public void testParameterizedReturnMethod() throws Exception {

		new SignatureReader("()TT;").accept(visitor);
		Site returnType = visitor.getReturnType();
		List<Site> parameters = visitor.getParameterTypes();

		Assert.assertNotNull(returnType);
		Assert.assertEquals("T", returnType.getName());
		Assert.assertNotNull(parameters);
		Assert.assertEquals(0, parameters.size());
	}

	@Test
	public void testParameterizedMethodParameter() throws Exception {

		new SignatureReader("(TT;)V").accept(visitor);
		Site returnType = visitor.getReturnType();
		List<Site> parameters = visitor.getParameterTypes();

		Assert.assertNull(returnType);
		Assert.assertNotNull(parameters);
		Assert.assertEquals(1, parameters.size());
		Assert.assertEquals("T", parameters.get(0).getName());
	}
}
