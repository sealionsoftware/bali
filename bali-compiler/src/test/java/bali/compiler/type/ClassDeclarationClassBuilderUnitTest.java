package bali.compiler.type;

import bali.annotation.Kind;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.SiteNode;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * User: Richard
 * Date: 22/09/13
 */
public class ClassDeclarationClassBuilderUnitTest {

	private ClassLibrary library = new ClassLibrary(Thread.currentThread().getContextClassLoader());
	private ClassDeclarationTypeBuilder builder = new ClassDeclarationTypeBuilder();

	@Test
	public void testBuildSimpleInterface(){

		InterfaceNode node = new InterfaceNode();
		node.setClassName("AnInterface");
		node.setQualifiedClassName("test.AnInterface");

		Class aClass = builder.build(node);

		Assert.assertEquals("test.AnInterface", aClass.getName());
		Assert.assertEquals(aClass.getMetaType(), Kind.INTERFACE);
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(Collections.<Type>emptyList(), aClass.getInterfaces());
		Assert.assertEquals(Collections.<Declaration<Site>>emptyList(), aClass.getParameters());
		Assert.assertEquals(Collections.<Method>emptyList(), aClass.getMethods());
		Assert.assertEquals(Collections.<Operator>emptyList(), aClass.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), aClass.getUnaryOperators());
		Assert.assertEquals(Collections.<Declaration<Site>>emptyList(), aClass.getProperties());

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
		superInterface.setSite(new ParameterisedSite(library.getReference("test.ASuperInterface")));

		InterfaceNode node = new InterfaceNode();
		node.setClassName("AnInterface");
		node.setQualifiedClassName("test.AnInterface");
		node.addImplementation(superInterface);

		Class aClass = builder.build(node);

		Assert.assertEquals("test.AnInterface", aClass.getName());
		Assert.assertEquals(Kind.INTERFACE, aClass.getMetaType());
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(Collections.<Declaration<Site>>emptyList(), aClass.getParameters());
		Assert.assertEquals(Collections.<Method>emptyList(), aClass.getMethods());
		Assert.assertEquals(Collections.<Operator>emptyList(), aClass.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), aClass.getUnaryOperators());
		Assert.assertEquals(Collections.<Declaration<Site>>emptyList(), aClass.getProperties());

		Assert.assertNotNull(aClass.getInterfaces());
		Assert.assertEquals(1, aClass.getInterfaces().size());

		Type superInterfaceType = aClass.getInterfaces().get(0);
		Assert.assertNotNull(superInterfaceType);
		Assert.assertEquals("test.ASuperInterface", superInterfaceType.getTemplate().getName());

	}

}
