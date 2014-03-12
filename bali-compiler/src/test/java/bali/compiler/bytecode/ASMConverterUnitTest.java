package bali.compiler.bytecode;

import bali.Boolean;
import bali.Number;
import bali.String;
import bali.compiler.type.Class;
import bali.compiler.type.Site;
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
		Assert.assertEquals("Lbali/String;", converter.getTypeDescriptor(new TestSite(String.class)));
	}

	@Test
	public void testMethodDescriptorString() throws Exception {
		Assert.assertEquals("()V", converter.getMethodDescriptor(null, new ArrayList<Site>()));
		List<bali.compiler.type.Site> argumentTypes = new ArrayList<>();
		argumentTypes.add(new TestSite(String.class));
		argumentTypes.add(new TestSite(Number.class));
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(new TestSite(Boolean.class), argumentTypes));
	}

//	TODO
//	@Test
//	public void testMethodDescriptorDeclaration() throws Exception {
//		Method method = new Method("test", null, );
//		Assert.assertEquals("()V", converter.getMethodDescriptor(method));
//		MethodNode declaration = new MethodNode();
//		SiteNode b = new SiteNode();
//		b.setSite(new TestSite(Boolean.class));
//		declaration.setType(b);
//		SiteNode s = new SiteNode();
//		SiteNode n = new SiteNode();
//		s.setSite(new TestSite(String.class));
//		n.setSite(new TestSite(Number.class));
//		ParameterNode argument1 = new ParameterNode(0, 0);
//		ParameterNode argument2 = new ParameterNode(0, 0);
//		argument1.setType(s);
//		argument2.setType(n);
//		declaration.addTypeParameter(argument1);
//		declaration.addTypeParameter(argument2);
//		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(declaration));
//	}


}
