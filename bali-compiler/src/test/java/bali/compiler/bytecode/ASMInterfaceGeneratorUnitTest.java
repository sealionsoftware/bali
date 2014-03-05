package bali.compiler.bytecode;

import bali.Number;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.SiteNode;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMInterfaceGeneratorUnitTest {

	private static ASMInterfaceGenerator generator = new ASMInterfaceGenerator();

	private InterfaceNode iface;

	@Before
	public void setUp() {
		iface = new InterfaceNode(0, 0);
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

		SiteNode type = new SiteNode();
		type.setSite(new TestVanillaSite(ASuperInterface.class));

		iface.addImplementation(type);

		java.lang.Class loadedClass = build();

		Assert.assertEquals("# Super interfaces", 1, loadedClass.getInterfaces().length);
		Assert.assertEquals("Superinterfaces name", "bali.compiler.bytecode.ASuperInterface", loadedClass.getInterfaces()[0].getName());

	}

	@Test
	public void testGenerateInterfaceWithVoidDeclaration() throws Exception {

		MethodNode declaration = new MethodNode();
		declaration.setName("aMethod");

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Class", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberReturnDeclaration() throws Exception {

		SiteNode type = new SiteNode();
		type.setClassName("Number");
		type.setSite(new TestVanillaSite(Number.class));

		MethodNode declaration = new MethodNode();
		declaration.setName("aMethod");
		declaration.setType(type);

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Class", Number.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateInterfaceWithNumberParamDeclaration() throws Exception {

		SiteNode type = new SiteNode();
		type.setClassName("Number");
		type.setSite(new TestVanillaSite(Number.class));

		ParameterNode argument = new ParameterNode();
		argument.setType(type);
		argument.setName("anArgument");

		MethodNode declaration = new MethodNode();
		declaration.setName("aMethod");
		declaration.addParameter(argument);

		iface.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
		Assert.assertEquals("Return Class", void.class, declaredMethod.getReturnType());
	}

	private java.lang.Class build() throws Exception {
		GeneratedClass generated = generator.build(iface);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.AnInterface");
	}


}
