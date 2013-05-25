package bali.compiler.parser;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralValue;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.ForStatement;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.ReferenceValue;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Variable;
import bali.compiler.parser.tree.WhileStatement;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ANTLRParserManagerUnitTest {

	private static ANTLRParserManager parserManager = new ANTLRParserManager();

	private static CompilationUnit unit;

	@BeforeClass
	public static void setUp() throws Exception {
		unit = parserManager.parse(new File(ANTLRParserManager.class.getResource("../example.bali").toURI()), "example");
	}

	@Test
	public void testCompilationUnit(){
		Assert.assertEquals("Package name", "example", unit.getName());
		Assert.assertEquals("Imports", 2, unit.getImports().size());
		Assert.assertEquals("Constants", 1, unit.getConstants().size());
		Assert.assertEquals("Interfaces", 2, unit.getInterfaces().size());
		Assert.assertEquals("Classes", 1, unit.getClasses().size());
		Assert.assertEquals("Children", 6, unit.getChildren().size());
	}

	@Test
	public void testImport(){
		Import iport = unit.getImports().get(0);
		Assert.assertEquals("Import name", "bali.Number", iport.getName());
		Assert.assertEquals("Children", 0, iport.getChildren().size());
	}

	@Test
	public void testConstant(){
		Constant constant = unit.getConstants().get(0);
		Assert.assertEquals("Constant name", "GLOBAL_CONSTANT", constant.getName());
		Assert.assertEquals("Type", "Number", constant.getType().getClassName());
		Assert.assertEquals("Value Type", NumberLiteralValue.class, constant.getValue().getClass());
		Assert.assertEquals("Value", "2", ((NumberLiteralValue) constant.getValue()).getSerialization());
		Assert.assertEquals("Children", 2, constant.getChildren().size());
	}

	@Test
	public void testInterface(){
		Interface iface = unit.getInterfaces().get(1);
		Assert.assertEquals("Name", "AnInterface", iface.getClassName());
		Assert.assertEquals("Super Interfaces", 1, iface.getSuperInterfaces().size());
		Assert.assertEquals("Super Interface Name", "ASuperInterface", iface.getSuperInterfaces().get(0).getClassName());
		Assert.assertEquals("Method Declarations", 1, iface.getMethodDeclarations().size());
		Assert.assertEquals("Children", 2, iface.getChildren().size());
	}

	@Test
	public void testClass(){
		Class clazz = unit.getClasses().get(0);
		Assert.assertEquals("Name", "AnImplementation", clazz.getClassName());
		Assert.assertEquals("Arguments", 1, clazz.getArguments().size());
		Assert.assertEquals("Interfaces", 1, clazz.getInterfaces().size());
		Assert.assertEquals("Interface Name", "AnInterface", clazz.getInterfaces().get(0).getClassName());
		Assert.assertEquals("Fields", 1, clazz.getFields().size());
		Assert.assertEquals("Methods", 1, clazz.getMethods().size());
		Assert.assertEquals("Children", 4, clazz.getChildren().size());
	}

	@Test
	public void testConstructionArguments(){
		Declaration declaration = unit.getClasses().get(0).getArguments().get(0);
		Assert.assertEquals("Name", "aField", declaration.getName());
		Assert.assertEquals("Type", "Number", declaration.getType().getClassName());
		Assert.assertEquals("Children", 1, declaration.getChildren().size());
	}

	@Test
	public void testField(){
		Field field = unit.getClasses().get(0).getFields().get(0);
		Assert.assertEquals("Name", "aField", field.getName());
		Assert.assertEquals("Type", "Number", field.getType().getClassName());
		Assert.assertEquals("Children", 1, field.getChildren().size());
	}

	@Test
	public void testMethod(){
		Method method = unit.getClasses().get(0).getMethods().get(0);
		Assert.assertEquals("Name", "aMethod", method.getName());
		Assert.assertEquals("Type", "Number", method.getType().getClassName());
		Assert.assertEquals("Arguments", 1, method.getArguments().size());
		Assert.assertEquals("Children", 3, method.getChildren().size());
	}

	@Test
	public void testMethodArgument(){
		Declaration declaration = unit.getClasses().get(0).getMethods().get(0).getArguments().get(0);
		Assert.assertEquals("Name", "anArgument", declaration.getName());
		Assert.assertEquals("Type", "Number", declaration.getType().getClassName());
		Assert.assertEquals("Children", 1, declaration.getChildren().size());
	}

	@Test
	public void testCodeBlock(){
		CodeBlock body = unit.getClasses().get(0).getMethods().get(0).getBody();
		Assert.assertEquals("Statements", 4, body.getStatements().size());
		Assert.assertEquals("Assignment", 1, ((Invocation) ((Assignment) body.getStatements().get(0)).getValue()).getArguments().size());
		Assert.assertEquals("Variable", "true", ((BooleanLiteralValue) ((Variable) body.getStatements().get(1)).getValue()).getSerialization());
		Assert.assertEquals("While", 2, (((WhileStatement) body.getStatements().get(2)).getBody().getChildren().size()));
		Assert.assertEquals("For", 1, ((ForStatement)((WhileStatement) body.getStatements().get(2)).getBody().getStatements().get(0)).getBody().getChildren().size());
		Assert.assertEquals("If", 1, ((ConditionalStatement) ((WhileStatement) body.getStatements().get(2)).getBody().getStatements().get(1)).getConditionalBlocks().get(0).getBody().getChildren().size());
		Assert.assertEquals("Return Statement", "anArgument", ((ReferenceValue) ((Return) body.getStatements().get(3)).getValue()).getName());
	}


}
