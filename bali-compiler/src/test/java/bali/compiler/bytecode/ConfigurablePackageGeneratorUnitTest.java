package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.ClassDeclaration;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.InterfaceDeclaration;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ConfigurablePackageGeneratorUnitTest {

	private static Generator<CompilationUnit, GeneratedClass> packageClassGenerator = Mockito.mock(Generator.class);
	private static Generator<InterfaceDeclaration, GeneratedClass> interfaceGenerator = Mockito.mock(Generator.class);
	private static Generator<ClassDeclaration, GeneratedClass> classGenerator = Mockito.mock(Generator.class);

	private static ConfigurablePackageGenerator generator = new ConfigurablePackageGenerator(
			packageClassGenerator,
			interfaceGenerator,
			classGenerator
	);

	private CompilationUnit unit;

	@Before
	public void setUp() throws Exception{
		unit = new CompilationUnit(0,0);
		unit.setName("bali.test");

	}

	@Test
	public void testGeneratePackageWithConstants() throws Exception{

		GeneratedClass packageClass = new GeneratedClass("testPackageClass", new byte[0]);
		Mockito.stub(packageClassGenerator.build(unit)).toReturn(packageClass);

		GeneratedPackage generated = generator.build(unit);

		Assert.assertEquals("Package Name", unit.getName(), generated.getName());
		Assert.assertEquals("# Classes", 1, generated.getClasses().size());
		Assert.assertEquals("Package Class Name", "testPackageClass", generated.getClasses().get(0).getName());
	}

	@Test
	public void testGeneratePackageWithInterfaces() throws Exception {

		InterfaceDeclaration iface = new InterfaceDeclaration(0,0);
		unit.addInterface(iface);

		GeneratedClass generatedInterface = new GeneratedClass("testInterface", new byte[0]);
		Mockito.stub(interfaceGenerator.build(iface)).toReturn(generatedInterface);

		GeneratedPackage generated = generator.build(unit);

		Assert.assertEquals("# Classes", 2, generated.getClasses().size());
		Assert.assertEquals("Interface Class Name", "testInterface", generated.getClasses().get(1).getName());
	}

	@Test
	public void testGeneratePackageWithClass() throws Exception{

		ClassDeclaration clazz = new ClassDeclaration(0,0);
		unit.addClass(clazz);

		GeneratedClass generatedClass = new GeneratedClass("testClass", new byte[0]);
		Mockito.stub(classGenerator.build(clazz)).toReturn(generatedClass);

		GeneratedPackage generated = generator.build(unit);

		Assert.assertEquals("# Classes", 2, generated.getClasses().size());
		Assert.assertEquals("Class Name", "testClass", generated.getClasses().get(1).getName());
	}

}
