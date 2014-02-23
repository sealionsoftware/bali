package bali.compiler.type;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.signature.SignatureReader;

import java.util.HashMap;


/**
 * User: Richard
 * Date: 13/09/13
 */
public class SiteSignatureVisitorUnitTest {

	private SiteSignatureVisitor visitor = new SiteSignatureVisitor(new ClassLibrary(), new HashMap<String, Type>(), false, false);

	@Test
	public void testVisitTypeVariable() throws Exception {
		new SignatureReader("TT;").accept(visitor);
		Site site = visitor.getSite();
		Assert.assertNotNull(site);
		Assert.assertTrue(site instanceof VariableSite);
		Assert.assertEquals("T", ((VariableSite) site).getName());
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

		Assert.assertEquals("bali.compiler.type.A", site.getTemplate().getName());
		Assert.assertNotNull(site.getTypeArguments());
		Assert.assertEquals(site.getTypeArguments().size(), 0);
	}

	@Test
	public void testVisitTypeParameterWithRealArgument() throws Exception {

		new SignatureReader("Lbali/compiler/type/B<Lbali/compiler/type/A;>;").accept(visitor);
		Site site = visitor.getSite();

		Assert.assertEquals("bali.compiler.type.B", site.getTemplate().getName());
		Assert.assertNotNull(site.getTypeArguments());
		Assert.assertEquals(1, site.getTypeArguments().size());

		Site argument = site.getTypeArguments().get(0);

		Assert.assertEquals("bali.compiler.type.A", argument.getTemplate().getName());
		Assert.assertNotNull(argument.getTypeArguments());
		Assert.assertEquals(0, argument.getTypeArguments().size());
	}

	@Test
	public void testVisitTypeParameterWithVariableArgument() throws Exception {

		new SignatureReader("Lbali/compiler/type/B<TT;>;").accept(visitor);
		Site site = visitor.getSite();

		Assert.assertEquals("bali.compiler.type.B", site.getTemplate().getName());
		Assert.assertNotNull(site.getTypeArguments());
		Assert.assertEquals(1, site.getTypeArguments().size());

		Site argument = site.getTypeArguments().get(0);
		Assert.assertNotNull(argument);
		Assert.assertTrue(argument instanceof VariableSite);
		Assert.assertEquals("T", ((VariableSite) argument).getName());

	}

}
