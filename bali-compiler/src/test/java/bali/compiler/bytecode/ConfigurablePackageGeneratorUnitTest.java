package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.RunStatementNode;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ConfigurablePackageGeneratorUnitTest {

	private static Generator<CompilationUnitNode, GeneratedClass> packageClassGenerator = Mockito.mock(Generator.class);
	private static Generator<BeanNode, GeneratedClass> beanGenerator = Mockito.mock(Generator.class);
	private static Generator<InterfaceNode, GeneratedClass> interfaceGenerator = Mockito.mock(Generator.class);
	private static Generator<ObjectNode, GeneratedClass> classGenerator = Mockito.mock(Generator.class);
	private static Generator<RunStatementNode, GeneratedClass> runStatementGenerator = Mockito.mock(Generator.class);

	private static ConfigurablePackageGenerator generator = new ConfigurablePackageGenerator(
			packageClassGenerator,
			beanGenerator,
			interfaceGenerator,
			classGenerator,
			runStatementGenerator
	);

	private CompilationUnitNode unit;

	@Before
	public void setUp() throws Exception{
		unit = new CompilationUnitNode(0,0);
		unit.setRunStatements(Collections.<RunStatementNode>emptyList());
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

		InterfaceNode iface = new InterfaceNode(0,0);
		unit.addInterface(iface);

		GeneratedClass generatedInterface = new GeneratedClass("testInterface", new byte[0]);
		Mockito.stub(interfaceGenerator.build(iface)).toReturn(generatedInterface);

		GeneratedPackage generated = generator.build(unit);

		Assert.assertEquals("# Classes", 2, generated.getClasses().size());
		Assert.assertEquals("Interface Class Name", "testInterface", generated.getClasses().get(1).getName());
	}

	@Test
	public void testGeneratePackageWithClass() throws Exception{

		ObjectNode clazz = new ObjectNode(0,0);
		unit.addClass(clazz);

		GeneratedClass generatedClass = new GeneratedClass("testClass", new byte[0]);
		Mockito.stub(classGenerator.build(clazz)).toReturn(generatedClass);

		GeneratedPackage generated = generator.build(unit);

		Assert.assertEquals("# Classes", 2, generated.getClasses().size());
		Assert.assertEquals("Class Name", "testClass", generated.getClasses().get(1).getName());
	}

}
