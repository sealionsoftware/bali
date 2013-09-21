package bali.compiler.type;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;

import java.util.List;

/**
 * User: Richard
 * Date: 21/09/13
 */
public class MethodSignatureVisitorUnitTest {

	private MethodSignatureVisitor visitor = new MethodSignatureVisitor(new TypeLibrary());

	@Test
	public void testParameterizedReturnMethod() throws Exception {

		new SignatureReader("()TT;").accept(visitor);
		Site returnType = visitor.getReturnType();
		List<Declaration> parameters = visitor.getParameterDeclarations();

		Assert.assertNotNull(returnType);
		Assert.assertNotNull(parameters);
		Assert.assertEquals(0, parameters.size());

	}
}
