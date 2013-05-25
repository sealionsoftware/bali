package bali.compiler.bytecode;

import bali.Number;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
* User: Richard
* Date: 08/05/13
*/
public class ASMInterfaceGeneratorUnitTest {

	private static ASMInterfaceGenerator generator = new ASMInterfaceGenerator();

	private Interface iface;

	@Before
	public void setUp(){
		iface = new Interface(0,0);
		iface.setQualifiedClassName("bali.test.AnInterface");
	}

	@Test
	public void testGenerateEmptyInterface() throws Exception{

		java.lang.Class loadedClass = build();

		Assert.assertEquals("Number of fields", 0, loadedClass.getFields().length);
		Assert.assertEquals("Number of methods",0, loadedClass.getMethods().length);

	}

	@Test
	public void testGenerateInterfaceExtension() throws Exception {

		Type type = new Type(0,0);
		type.setQualifiedClassName("bali.compiler.bytecode.ASuperInterface");

		iface.addSuperInterface(type);

		java.lang.Class loadedClass = build();

		Assert.assertEquals("# Super interfaces", 1, loadedClass.getInterfaces().length);
		Assert.assertEquals("Superinterfaces name", "bali.compiler.bytecode.ASuperInterface", loadedClass.getInterfaces()[0].getName());

	}

	@Test
	public void testGenerateInterfaceWithVoidDeclaration() throws Exception {

		MethodDeclaration declaration = new MethodDeclaration(0,0);
		declaration.setName("aMethod");

		iface.addMethodDeclaration(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberReturnDeclaration() throws Exception {

		Type type = new Type(0,0);
		type.setClassName("Number");
		type.setQualifiedClassName("bali.Number");

		MethodDeclaration declaration = new MethodDeclaration(0,0);
		declaration.setName("aMethod");
		declaration.setType(type);

		iface.addMethodDeclaration(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", Number.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberParamDeclaration() throws Exception {

		Type type = new Type(0,0);
		type.setClassName("Number");
		type.setQualifiedClassName("bali.Number");

		Declaration argument = new Declaration(0,0);
		argument.setType(type);
		argument.setName("anArgument");

		MethodDeclaration declaration = new MethodDeclaration(0,0);
		declaration.setName("aMethod");
		declaration.addArgument(argument);

		iface.addMethodDeclaration(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	private java.lang.Class build() throws Exception{
		GeneratedClass generated = generator.build(iface);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.AnInterface");
	}


}
