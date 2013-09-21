package bali.compiler.type;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;


/**
 * User: Richard
 * Date: 13/09/13
 */
public class SiteSignatureVisitorUnitTest {

	private SiteSignatureVisitor visitor = new SiteSignatureVisitor(new TypeLibrary());

	@Test
	public void testVisitTypeVariable() throws Exception {
		new SignatureReader("TT;").accept(visitor);
		Site site = visitor.getSite();
		Assert.assertNotNull(site);
		Assert.assertEquals("T", site.getName());
	}

	@Test
	public void testVisitSimpleTypeParameter() throws Exception {

		new SignatureReader("Ljava/lang/Object;").accept(visitor);
		Site site = visitor.getSite();
		Assert.assertNull(site);
	}

	@Test
	public void testVisitSimpleBoundedTypeParameter() throws Exception {

		new SignatureReader("Lbali/compiler/type/A;").accept(visitor);
		Site site = visitor.getSite();

		Assert.assertEquals("bali.compiler.type.A", site.getName());
		Assert.assertNotNull(site.getTypeParameters());
		Assert.assertEquals(site.getTypeParameters().size(), 0);
	}

	@Test
	public void testVisitTypeParameterWithRealArgument() throws Exception {

		new SignatureReader("Lbali/compiler/type/B<Lbali/compiler/type/A;>;").accept(visitor);
		Site site = visitor.getSite();

		Assert.assertEquals("bali.compiler.type.B", site.getName());
		Assert.assertNotNull(site.getTypeParameters());
		Assert.assertEquals(1, site.getTypeParameters().size());

		Declaration parameterDeclaration = site.getTypeParameters().get(0);

		Assert.assertEquals("T", parameterDeclaration.getName());

		Site parameterType = parameterDeclaration.getType();

		Assert.assertEquals("bali.compiler.type.A", parameterType.getName());
		Assert.assertNotNull(parameterType.getTypeParameters());
		Assert.assertEquals(0, parameterType.getTypeParameters().size());
	}

	@Test
	public void testVisitTypeParameterWithVariableArgument() throws Exception {

		new SignatureReader("Lbali/compiler/type/B<TT;>;").accept(visitor);
		Site site = visitor.getSite();

		Assert.assertEquals("bali.compiler.type.B", site.getName());
		Assert.assertNotNull(site.getTypeParameters());
		Assert.assertEquals(1, site.getTypeParameters().size());

		Declaration parameterDeclaration = site.getTypeParameters().get(0);

		Assert.assertEquals("T", parameterDeclaration.getName());

		Site parameterType = parameterDeclaration.getType();
		Assert.assertNotNull(parameterType);
		Assert.assertEquals("T", parameterType.getName());

	}

}
