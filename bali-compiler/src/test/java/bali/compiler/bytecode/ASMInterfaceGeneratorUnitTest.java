package bali.compiler.bytecode;

import bali.Number;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.ArgumentDeclaration;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.InterfaceDeclaration;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeReference;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMInterfaceGeneratorUnitTest {

	private static ASMInterfaceGenerator generator = new ASMInterfaceGenerator();

	private InterfaceDeclaration iface;

	@Before
	public void setUp() {
		iface = new InterfaceDeclaration(0, 0);
		iface.setQualifiedClassName("bali.test.AnInterface");
	}

	@Test
	public void testGenerateEmptyInterface() throws Exception {

		java.lang.Class loadedClass = build();

		Assert.assertEquals("Number of fields", 0, loadedClass.getFields().length);
		Assert.assertEquals("Number of methods", 0, loadedClass.getMethods().length);

	}

	@Test
	public void testGenerateInterfaceExtension() throws Exception {

		TypeReference type = new TypeReference(0, 0);
		type.setDeclaration(new TestDeclaration("bali.compiler.bytecode.ASuperInterface"));

		iface.addSuperInterface(type);

		java.lang.Class loadedClass = build();

		Assert.assertEquals("# Super interfaces", 1, loadedClass.getInterfaces().length);
		Assert.assertEquals("Superinterfaces name", "bali.compiler.bytecode.ASuperInterface", loadedClass.getInterfaces()[0].getName());

	}

	@Test
	public void testGenerateInterfaceWithVoidDeclaration() throws Exception {

		Method declaration = new Method(0, 0);
		declaration.setName("aMethod");

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberReturnDeclaration() throws Exception {

		TypeReference type = new TypeReference(0, 0);
		type.setClassName("Number");
		type.setDeclaration(new TestDeclaration("bali.Number"));

		Method declaration = new Method(0, 0);
		declaration.setName("aMethod");
		declaration.setType(type);

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", Number.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberParamDeclaration() throws Exception {

		TypeReference type = new TypeReference(0, 0);
		type.setClassName("Number");
		type.setDeclaration(new TestDeclaration("bali.Number"));

		Declaration argument = new ArgumentDeclaration(0, 0);
		argument.setType(type);
		argument.setName("anArgument");

		Method declaration = new Method(0, 0);
		declaration.setName("aMethod");
		declaration.addArgument(argument);

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	private java.lang.Class build() throws Exception {
		GeneratedClass generated = generator.build(iface);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.AnInterface");
	}


}
