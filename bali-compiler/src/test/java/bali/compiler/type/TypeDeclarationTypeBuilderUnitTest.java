package bali.compiler.type;

import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.SiteNode;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * User: Richard
 * Date: 22/09/13
 */
public class TypeDeclarationTypeBuilderUnitTest {

	private TypeLibrary library = new TypeLibrary();
	private TypeDeclarationTypeBuilder builder = new TypeDeclarationTypeBuilder(library);

	@Test
	public void testBuildSimpleInterface(){

		InterfaceNode node = new InterfaceNode();
		node.setClassName("AnInterface");
		node.setQualifiedClassName("test.AnInterface");

		Type type = builder.build(node);

		Assert.assertEquals("test.AnInterface", type.getName());
		Assert.assertTrue(type.isAbstract());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(Collections.<Site>emptyList(), type.getInterfaces());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getParameters());
		Assert.assertEquals(Collections.<Method>emptyList(), type.getMethods());
		Assert.assertEquals(Collections.<Operator>emptyList(), type.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), type.getUnaryOperators());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getProperties());

	}

	@Test
	public void testBuildExtensionInterface(){

		InterfaceNode superNode = new InterfaceNode();
		superNode.setClassName("ASuperInterface");
		superNode.setQualifiedClassName("test.ASuperInterface");

		library.notifyOfDeclaration(superNode.getQualifiedClassName());
		library.addDeclaration(superNode);

		SiteNode superInterface = new SiteNode();
		superInterface.setClassName("ASuperInterface");
		superInterface.setSite(new VanillaSite(library.getType("test.ASuperInterface")));

		InterfaceNode node = new InterfaceNode();
		node.setClassName("AnInterface");
		node.setQualifiedClassName("test.AnInterface");
		node.addImplementation(superInterface);

		Type type = builder.build(node);

		Assert.assertEquals("test.AnInterface", type.getName());
		Assert.assertTrue(type.isAbstract());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getParameters());
		Assert.assertEquals(Collections.<Method>emptyList(), type.getMethods());
		Assert.assertEquals(Collections.<Operator>emptyList(), type.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), type.getUnaryOperators());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getProperties());

		Assert.assertNotNull(type.getInterfaces());
		Assert.assertEquals(1, type.getInterfaces().size());

		Site superInterfaceSite = type.getInterfaces().get(0);
		Assert.assertNotNull(superInterfaceSite);
		Assert.assertEquals("test.ASuperInterface", superInterfaceSite.getName());

	}

}
