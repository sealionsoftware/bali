package bali.compiler.bytecode;

import bali.Number;
import bali.String;
import bali.Boolean;
import bali.compiler.parser.tree.ArgumentDeclaration;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeReference;
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
		TypeReference declaration = new TypeReference(0, 0);
		declaration.setDeclaration(new TestDeclaration(String.class.getName()));
		Assert.assertEquals("Lbali/String;", converter.getTypeDescriptor(declaration));
	}

	@Test
	public void testMethodDescriptorString() throws Exception {
		Assert.assertEquals("()V", converter.getMethodDescriptor(null, new ArrayList<TypeReference>()));
		List<TypeReference> argumentTypes = new ArrayList<>();
		argumentTypes.add(getType(String.class));
		argumentTypes.add(getType(Number.class));
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(getType(Boolean.class), argumentTypes));
	}

	private TypeReference getType(Class clazz) {
		TypeReference t = new TypeReference();
		t.setDeclaration(new TestDeclaration(clazz.getName()));
		return t;
	}

	@Test
	public void testMethodDescriptorDeclaration() throws Exception {
		Method declaration = new Method(0, 0);
		Assert.assertEquals("()V", converter.getMethodDescriptor(declaration));
		TypeReference b = new TypeReference(0, 0);
		b.setDeclaration(new TestDeclaration(Boolean.class.getName()));
		declaration.setType(b);
		TypeReference s = new TypeReference(0, 0);
		TypeReference n = new TypeReference(0, 0);
		s.setDeclaration(new TestDeclaration(String.class.getName()));
		n.setDeclaration(new TestDeclaration(Number.class.getName()));
		Declaration argument1 = new ArgumentDeclaration(0, 0);
		Declaration argument2 = new ArgumentDeclaration(0, 0);
		argument1.setType(s);
		argument2.setType(n);
		declaration.addArgument(argument1);
		declaration.addArgument(argument2);
		Assert.assertEquals("(Lbali/String;Lbali/Number;)Lbali/Boolean;", converter.getMethodDescriptor(declaration));
	}


}
