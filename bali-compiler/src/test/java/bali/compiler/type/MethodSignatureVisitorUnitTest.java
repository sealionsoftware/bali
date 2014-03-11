package bali.compiler.type;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class MethodSignatureVisitorUnitTest {

	private MethodSignatureVisitor visitor = new MethodSignatureVisitor(
			new ClassLibrary(Thread.currentThread().getContextClassLoader()),
			new HashMap<String, Type>(), new SiteData(), Collections.<SiteData>emptyList()
	);

	@Test
	public void testParameterizedReturnMethod() throws Exception {

		new SignatureReader("()TT;").accept(visitor);
		Type returnType = visitor.getReturnType();
		List<Site> parameters = visitor.getParameterTypes();

		Assert.assertNotNull(returnType);
		Assert.assertTrue(returnType instanceof VariableType);
		Assert.assertEquals("T", ((VariableType) returnType).getName());
		Assert.assertNotNull(parameters);
		Assert.assertEquals(0, parameters.size());
	}

	@Test
	public void testParameterizedMethodParameter() throws Exception {

		new SignatureReader("(TT;)V").accept(visitor);
		Type returnType = visitor.getReturnType();
		List<Site> parameters = visitor.getParameterTypes();

		Assert.assertNull(returnType);
		Assert.assertNotNull(parameters);
		Assert.assertEquals(1, parameters.size());
		Type parameter = parameters.get(0);
		Assert.assertTrue(parameter instanceof VariableType);
		Assert.assertEquals("T", ((VariableType) parameter).getName());
	}
}
