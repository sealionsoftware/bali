package bali.compiler.parser;

import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import junit.framework.Assert;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * User: Richard
 * Date: 07/04/13
 */
public class BaliParserUnitTest {

	@Test
	public void testImportStatements() throws Exception {
		parsePackageDeclaration("imports.bali");
	}

	@Test
	public void testConstantDeclaration() throws Exception {
		parsePackageDeclaration("constants.bali");
	}

//	@Test
//	public void testFunctionsDeclaration() throws Exception {
//		parsePackageDeclaration("functions.bali");
//	}

	@Test
	public void testBeansDeclaration() throws Exception {
		parsePackageDeclaration("beans.bali");
	}

	@Test
	public void testInterfacesDeclaration() throws Exception {
		parsePackageDeclaration("interfaces.bali");
	}

	@Test
	public void testClassesDeclaration() throws Exception {
		parsePackageDeclaration("objects.bali");
	}

	@Test
	public void testVariableDeclaration() throws Exception {
		parseCodeBlock("variables.bali");
	}

	@Test
	public void testUnary() throws Exception {
		parseCodeBlock("unary.bali");
	}

	@Test
	public void testOperations() throws Exception {
		parseCodeBlock("operations.bali");
	}

	@Test
	public void testIfStatement() throws Exception {
		parseCodeBlock("ifstatement.bali");
	}

	@Test
	public void testTryStatement() throws Exception {
		parseCodeBlock("trystatement.bali");
	}

	@Test
	public void testWhileStatement() throws Exception {
		parseCodeBlock("whilestatement.bali");
	}

	@Test
	public void testForStatement() throws Exception {
		parseCodeBlock("forstatement.bali");
	}

	@Test
	public void testRunStatement() throws Exception {
		parseCodeBlock("concurrent.bali");
	}

	@Test
	public void testInvocations() throws Exception {
		parseCodeBlock("invocations.bali");
	}

	@Test
	public void testProperties() throws Exception {
		parseCodeBlock("properties.bali");
	}

	@Test
	public void testPropertyReference() throws Exception {
		parseCodeBlock("propertyReference.bali");
	}

	@Test
	public void testComplexVariable() throws Exception {
		parseCodeBlock("complex.bali");
	}

	//TODO: this is in the wrong place
	@Test
	public void testStringTemplate() throws Exception {

		BaliParser parser = parse("template.bali");
		BaliParser.CodeBlockContext cbc = parser.codeBlock();
		checkSucceeded(parser, cbc);
		ANTLRParserManager pm = new ANTLRParserManager();
		CodeBlockNode cbn = pm.build(cbc);
		List<StatementNode> statementNodeList = cbn.getStatements();
		Assert.assertNotNull(statementNodeList);
		Assert.assertEquals(1, statementNodeList.size());
		StatementNode statementNode = cbn.getStatements().get(0);
		Assert.assertNotNull(statementNode);
		Assert.assertTrue(statementNode instanceof OperationNode);
		OperationNode op = (OperationNode) statementNode;
		Assert.assertTrue(op.getTwo() instanceof StringLiteralExpressionNode);

		StringLiteralExpressionNode tail = (StringLiteralExpressionNode) op.getTwo();
		Assert.assertEquals("...", tail.getSerialization());
		Assert.assertTrue(op.getOne() instanceof OperationNode);
		op = (OperationNode) op.getOne();

		Assert.assertTrue(op.getTwo() instanceof InvocationNode);
		Assert.assertTrue(op.getOne() instanceof OperationNode);
		op = (OperationNode) op.getOne();

		tail = (StringLiteralExpressionNode) op.getTwo();
		Assert.assertEquals(" also ", tail.getSerialization());
		Assert.assertTrue(op.getOne() instanceof OperationNode);
		op = (OperationNode) op.getOne();

		Assert.assertTrue(op.getTwo() instanceof InvocationNode);
		Assert.assertTrue(op.getOne() instanceof OperationNode);
		op = (OperationNode) op.getOne();

		tail = (StringLiteralExpressionNode) op.getTwo();
		Assert.assertEquals(" and ", tail.getSerialization());
		Assert.assertTrue(op.getOne() instanceof OperationNode);
		op = (OperationNode) op.getOne();

		Assert.assertTrue(op.getTwo() instanceof InvocationNode);
		Assert.assertTrue(op.getOne() instanceof StringLiteralExpressionNode);

		tail = (StringLiteralExpressionNode) op.getOne();
		Assert.assertEquals("this is a string template ", tail.getSerialization());
	}

	private void parseCodeBlock(String filename) throws IOException {
		BaliParser parser = parse(filename);
		checkSucceeded(parser, parser.codeBlock());
	}

	private void parsePackageDeclaration(String filename) throws IOException {
		BaliParser parser = parse(filename);
		checkSucceeded(parser, parser.packageDeclaration());
	}

	private BaliParser parse(String filename) throws IOException {

		URL fileResource =  getClass().getResource(filename);
		if (fileResource == null){
			throw new RuntimeException("Could not load resource " + filename);
		}

		ANTLRInputStream input = new ANTLRInputStream(fileResource.openStream());
		Lexer lexer = new BaliLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		BaliParser parser = new BaliParser(tokens);
		parser.setErrorHandler(new DefaultErrorStrategy());
		return parser;
	}

	private void checkSucceeded(BaliParser parser, ParserRuleContext tree){
		System.out.println(tree.toStringTree(parser));
		Assert.assertTrue("The file was not parsed successfully", parser.getNumberOfSyntaxErrors() == 0);
	}
}
