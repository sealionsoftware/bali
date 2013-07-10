package bali.compiler.bytecode;

import bali.Boolean;
import bali.Number;
import bali.String;
import bali.compiler.parser.tree.Argument;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
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
		Type declaration = new Type(0, 0);
		declaration.setDeclaration(new TestDeclaration(String.class.getName()));
		Assert.assertEquals("Lbali/String;", converter.getTypeDescriptor(declaration));
	}

	@Test
	public void testMethodDescriptorString() throws Exception {
		Assert.assertEquals("()V", converter.getMethodDescriptor(null, new ArrayList<Type>()));
		List<Type> argumentTypes = new ArrayList<>();
		argumentTypes.add(getType(String.class));
		argumentTypes.add(getType(Number.class));
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(getType(Boolean.class), argumentTypes));
	}

	private Type getType(Class clazz) {
		Type t = new Type();
		t.setDeclaration(new TestDeclaration(clazz.getName()));
		return t;
	}

	@Test
	public void testMethodDescriptorDeclaration() throws Exception {
		MethodDeclaration declaration = new MethodDeclaration(0, 0);
		Assert.assertEquals("()V", converter.getMethodDescriptor(declaration));
		Type b = new Type(0, 0);
		b.setDeclaration(new TestDeclaration(bali.Boolean.class.getName()));
		declaration.setType(b);
		Type s = new Type(0, 0);
		Type n = new Type(0, 0);
		s.setDeclaration(new TestDeclaration(String.class.getName()));
		n.setDeclaration(new TestDeclaration(Number.class.getName()));
		Declaration argument1 = new Argument(0, 0);
		Declaration argument2 = new Argument(0, 0);
		argument1.setType(s);
		argument2.setType(n);
		declaration.addArgument(argument1);
		declaration.addArgument(argument2);
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(declaration));
	}


}
