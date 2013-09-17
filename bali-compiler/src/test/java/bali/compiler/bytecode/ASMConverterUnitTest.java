package bali.compiler.bytecode;

import bali.Boolean;
import bali.Number;
import bali.String;
import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Type;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMConverterUnitTest {

	private static ASMConverter converter = new ASMConverter();

	@Test
	public void testInternalName() throws Exception {
		Assert.assertEquals("bali/String", converter.getInternalName(String.class.getName()));
	}

	@Test
	public void testTypeDescriptorString() throws Exception {
		Assert.assertEquals("Lbali/String;", converter.getTypeDescriptor(String.class.getName()));
	}

	@Test
	public void testTypeDescriptorDeclaration() throws Exception {
		SiteNode<Type> declaration = new SiteNode<>();
		declaration.setSite(new TestSite(String.class));
		Assert.assertEquals("Lbali/String;", converter.getTypeDescriptor(declaration));
	}

	@Test
	public void testMethodDescriptorString() throws Exception {
		Assert.assertEquals("()V", converter.getMethodDescriptor(null, new ArrayList<SiteNode>()));
		List<SiteNode> argumentTypes = new ArrayList<>();
		argumentTypes.add(getType(String.class));
		argumentTypes.add(getType(Number.class));
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(getType(Boolean.class), argumentTypes));
	}

	private SiteNode getType(Class clazz) {
		SiteNode<Type> t = new SiteNode<>();
		t.setSite(new TestSite(clazz));
		return t;
	}

	@Test
	public void testMethodDescriptorDeclaration() throws Exception {
		MethodNode declaration = new MethodNode();
		Assert.assertEquals("()V", converter.getMethodDescriptor(declaration));
		SiteNode<Type> b = new SiteNode<>();
		b.setSite(new TestSite(Boolean.class));
		declaration.setType(b);
		SiteNode<Type> s = new SiteNode<>();
		SiteNode<Type> n = new SiteNode<>();
		s.setSite(new TestSite(String.class));
		n.setSite(new TestSite(Number.class));
		ArgumentDeclarationNode argument1 = new ArgumentDeclarationNode(0, 0);
		ArgumentDeclarationNode argument2 = new ArgumentDeclarationNode(0, 0);
		argument1.setType(s);
		argument2.setType(n);
		declaration.addArgument(argument1);
		declaration.addArgument(argument2);
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(declaration));
	}


}
