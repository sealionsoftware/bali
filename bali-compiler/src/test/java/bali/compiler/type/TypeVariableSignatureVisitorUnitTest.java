package bali.compiler.type;

import bali.compiler.bytecode.TestSite;
import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;

import java.util.Collections;

/**
 * User: Richard
 * Date: 13/09/13
 */
public class TypeVariableSignatureVisitorUnitTest {

	private TypeVariableSignatureVisitor visitor = new TypeVariableSignatureVisitor();

	@Test
	public void testVisitTypeParameter() throws Exception {

		new SignatureReader("Lbali/compiler/type/TypeParamBase<TT;>;").accept(visitor);
		Site<Type> site = visitor.getUninitialisedSite();

		Assert.assertEquals("bali.compiler.type.TypeParamBase", site.getClassName());

		Type expected = new Class("bali.compiler.type.TypeParamBase",
				Collections.singletonList(new Declaration("T", new TestSite(Object.class))),
				Collections.<Declaration>emptyList(),
				Collections.<Method>emptyList(),
				Collections.<Site>emptyList()
		);

		site.init(expected);

		Assert.assertEquals(site.getParameters().size(), 1);
		Declaration parameterDeclaration = site.getParameters().get(0);

		Assert.assertEquals("T", parameterDeclaration.getName());
		Assert.assertEquals(new TestSite(Object.class), parameterDeclaration.getType());

	}

}
