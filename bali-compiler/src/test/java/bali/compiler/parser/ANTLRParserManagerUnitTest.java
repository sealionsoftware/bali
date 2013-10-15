package bali.compiler.parser;

import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.parser.tree.WhileStatementNode;
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

	private static CompilationUnitNode unit;

	@BeforeClass
	public static void setUp() throws Exception {
		unit = parserManager.parse(new File(ANTLRParserManager.class.getResource("../example.bali").toURI()), "example");
	}

	@Test
	public void testCompilationUnit() {
		Assert.assertEquals("Package name", "example", unit.getName());
		Assert.assertEquals("Imports", 3, unit.getImports().size());
		Assert.assertEquals("Constants", 1, unit.getConstants().size());
		Assert.assertEquals("Interfaces", 2, unit.getInterfaces().size());
		Assert.assertEquals("Classes", 1, unit.getClasses().size());
		Assert.assertEquals("Children", 7, unit.getChildren().size());
	}

	@Test
	public void testImport() {
		ImportNode iport = unit.getImports().get(0);
		Assert.assertEquals("Import name", "bali.Number", iport.getName());
		Assert.assertEquals("Children", 0, iport.getChildren().size());
	}

	@Test
	public void testConstant() {
		ConstantNode constant = unit.getConstants().get(0);
		Assert.assertEquals("Constant name", "GLOBAL_CONSTANT", constant.getName());
		Assert.assertEquals("Type", "Number", constant.getType().getClassName());
		Assert.assertEquals("Value Type", NumberLiteralExpressionNode.class, constant.getValue().getClass());
		Assert.assertEquals("Value", "2", ((NumberLiteralExpressionNode) constant.getValue()).getSerialization());
		Assert.assertEquals("Children", 2, constant.getChildren().size());
	}

	@Test
	public void testInterface() {
		InterfaceNode iface = unit.getInterfaces().get(1);
		Assert.assertEquals("Name", "AnInterface", iface.getClassName());
		Assert.assertEquals("Super Interfaces", 1, iface.getImplementations().size());
		Assert.assertEquals("Super Interface Name", "ASuperInterface", iface.getImplementations().get(0).getClassName());
		Assert.assertEquals("Method Declarations", 1, iface.getMethods().size());
		Assert.assertEquals("Children", 2, iface.getChildren().size());
	}

	@Test
	public void testClass() {
		ClassNode clazz = unit.getClasses().get(0);
		Assert.assertEquals("Name", "AnImplementation", clazz.getClassName());
		Assert.assertEquals("Arguments", 1, clazz.getArgumentDeclarations().size());
		Assert.assertEquals("Interfaces", 1, clazz.getImplementations().size());
		Assert.assertEquals("Interface Name", "AnInterface", clazz.getImplementations().get(0).getClassName());
		Assert.assertEquals("Fields", 1, clazz.getFields().size());
		Assert.assertEquals("Methods", 2, clazz.getMethods().size());
		Assert.assertEquals("Children", 5, clazz.getChildren().size());
	}

	@Test
	public void testConstructionArguments() {
		DeclarationNode declaration = unit.getClasses().get(0).getArgumentDeclarations().get(0);
		Assert.assertEquals("Name", "aParameter", declaration.getName());
		Assert.assertEquals("Type", "Number", declaration.getType().getClassName());
		Assert.assertEquals("Children", 1, declaration.getChildren().size());
	}

	@Test
	public void testField() {
		FieldNode field = unit.getClasses().get(0).getFields().get(0);
		Assert.assertEquals("Name", "aField", field.getName());
		Assert.assertEquals("Type", "Number", field.getType().getClassName());
		Assert.assertEquals("Children", 1, field.getChildren().size());
	}

	@Test
	public void testMethod() {
		MethodDeclarationNode method = unit.getClasses().get(0).getMethods().get(0);
		Assert.assertEquals("Name", "aMethod", method.getName());
		Assert.assertEquals("Type", "Number", method.getType().getClassName());
		Assert.assertEquals("Arguments", 1, method.getArguments().size());
		Assert.assertEquals("Children", 3, method.getChildren().size());
	}

	@Test
	public void testMethodArgument() {
		DeclarationNode declaration = unit.getClasses().get(0).getMethods().get(0).getArguments().get(0);
		Assert.assertEquals("Name", "anArgument", declaration.getName());
		Assert.assertEquals("Type", "Number", declaration.getType().getClassName());
		Assert.assertEquals("Children", 1, declaration.getChildren().size());
	}

	@Test
	public void testCodeBlock() {
		CodeBlockNode body = unit.getClasses().get(0).getMethods().get(0).getBody();
		Assert.assertEquals("Statements", 5, body.getStatements().size());
		Assert.assertEquals("Variable", "false", ((BooleanLiteralExpressionNode) ((VariableNode) body.getStatements().get(2)).getValue()).getSerialization());
		Assert.assertEquals("While", 2, (((WhileStatementNode) body.getStatements().get(3)).getBody().getChildren().size()));
		Assert.assertEquals("For", 1, ((ForStatementNode) ((WhileStatementNode) body.getStatements().get(3)).getBody().getStatements().get(0)).getBody().getChildren().size());
		Assert.assertEquals("If", 1, ((ConditionalStatementNode) ((WhileStatementNode) body.getStatements().get(3)).getBody().getStatements().get(1)).getConditionalBlocks().get(0).getBody().getChildren().size());
		Assert.assertEquals("Return Statement", "anArgument", ((ReferenceNode) ((ReturnStatementNode) body.getStatements().get(4)).getValue()).getName());
	}


}
