package bali.compiler.parser;

import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.CaseStatementNode;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConditionalBlockNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.ArrayLiteralExpressionNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.parser.tree.SwitchStatementNode;
import bali.compiler.parser.tree.ThrowStatementNode;
import bali.compiler.parser.tree.TryStatementNode;
import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.parser.tree.WhileStatementNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Parser manager that uses a generated ANTLR Parser to construct an AST
 * <p/>
 * User: Richard
 * Date: 30/04/13
 */
public class ANTLRParserManager implements ParserManager {

	public CompilationUnitNode parse(File compilationUnit, String name) throws Exception {

		ANTLRInputStream input = new ANTLRInputStream(new FileReader(compilationUnit));
		Lexer lexer = new BaliLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		BaliParser parser = new BaliParser(tokens);
		parser.setErrorHandler(new DefaultErrorStrategy());

		BaliParser.PackageDeclarationContext context = parser.packageDeclaration();

		int errors = parser.getNumberOfSyntaxErrors();
		if (errors > 0) {
			throw new Exception("Could not compile " + compilationUnit.getName() + " [" + errors + " errors]");
		}

		return build(context, name);
	}

	private CompilationUnitNode build(BaliParser.PackageDeclarationContext context, String name) throws Exception {

		CompilationUnitNode cu = new CompilationUnitNode(l(context), c(context));
		cu.setName(name);

		for (BaliParser.ImportDeclarationContext idc : context.importDeclaration()) {
			cu.addImport(buildImport(idc));
		}
		for (BaliParser.ConstantDeclarationContext cdc : context.constantDeclaration()) {
			cu.addConstant(buildConstant(cdc));
		}
		for (BaliParser.BeanDeclarationContext idc : context.beanDeclaration()) {
			cu.addBean(buildBean(idc));
		}
		for (BaliParser.InterfaceDeclarationContext idc : context.interfaceDeclaration()) {
			cu.addInterface(buildInterface(idc));
		}
		for (BaliParser.ClassDeclarationContext cdc : context.classDeclaration()) {
			cu.addClass(buildClassDeclaration(cdc));
		}

		return cu;
	}

	private Integer l(ParserRuleContext context) {
		return context.getStart().getLine();
	}

	private Integer c(ParserRuleContext context) {
		return context.getStart().getCharPositionInLine();
	}

	private ImportNode buildImport(BaliParser.ImportDeclarationContext context) {
		ImportNode iport = new ImportNode(l(context), c(context));
		iport.setName(context.TYPE_IDENTIFIER().getText());
		return iport;
	}

	private ConstantNode buildConstant(BaliParser.ConstantDeclarationContext context) {
		ConstantNode constant = new ConstantNode(l(context), c(context));
		constant.setName(context.CONSTANT_IDENTIFIER().getText());
		constant.setType(getType(context.typeDeclaration()));
		constant.setValue(getConstantValue(context.constantValue()));
		return constant;
	}

	private BeanNode buildBean(BaliParser.BeanDeclarationContext context) {
		BeanNode bean = new BeanNode(l(context), c(context));

		bean.setClassName(context.typeDeclaration().get(0).getText());

		if(context.typeDeclaration().size() > 1){
			bean.setSuperType(getType(context.typeDeclaration(1)));
		}

//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			iface.addParameter(getType(typeIdentifier));
//		}

		for (BaliParser.PropertyDeclarationContext pdc : context.propertyDeclaration()) {
			bean.addProperty(buildProperty(pdc));
		}

		return bean;
	}

	private InterfaceNode buildInterface(BaliParser.InterfaceDeclarationContext context) {
		InterfaceNode iface = new InterfaceNode(l(context), c(context));

		iface.setClassName(context.typeDeclaration().getText());

//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			iface.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : context.typeDeclarationList().typeDeclaration()) {
				iface.addImplementation(getType(typeDeclaration));
			}
		}
		for (BaliParser.DeclarationDeclarationContext ddc : context.declarationDeclaration()) {
			iface.addMethod(buildMethodDeclaration(ddc));
		}

		return iface;
	}

	private ClassNode buildClassDeclaration(BaliParser.ClassDeclarationContext context) throws Exception {
		ClassNode clazz = new ClassNode(l(context), c(context));
		clazz.setClassName(context.typeDeclaration().TYPE_IDENTIFIER().getText());


//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			clazz.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : context.typeDeclarationList().typeDeclaration()) {
				clazz.addImplementation(getType(typeDeclaration));
			}
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			clazz.addArgument(buildArgument(adc));
		}
		for (BaliParser.FieldDeclarationContext fdc : context.fieldDeclaration()) {
			clazz.addField(buildField(fdc));
		}
		for (BaliParser.MethodDeclarationContext mdc : context.methodDeclaration()) {
			clazz.addMethod(buildMethod(mdc));
		}

		return clazz;
	}

	private MethodDeclarationNode buildMethod(BaliParser.MethodDeclarationContext context) throws Exception {
		MethodDeclarationNode method = new MethodDeclarationNode(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		BaliParser.TypeDeclarationContext typeDeclaration = context.typeDeclaration();
		if (typeDeclaration != null) {
			method.setType(getType(typeDeclaration));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			method.addArgument(buildArgument(adc));
		}
		method.setBody(buildCodeBlock(context.codeBlock()));
		return method;
	}

	private CodeBlockNode buildCodeBlock(BaliParser.CodeBlockContext context) throws Exception {

		CodeBlockNode codeBlock = new CodeBlockNode(l(context), c(context));

		for (BaliParser.StatementContext statementContext : context.statement()) {

			if (statementContext.controlStatement() != null) {

				BaliParser.ControlStatementContext csc = statementContext.controlStatement();
				if (csc.conditionalStatement() != null) {
					codeBlock.addStatement(buildConditionalStatement(csc.conditionalStatement()));
				} else if (csc.forStatement() != null) {
					codeBlock.addStatement(buildForStatement(csc.forStatement()));
				} else if (csc.whileStatement() != null) {
					codeBlock.addStatement(buildWhileStatement(csc.whileStatement()));
				} else if (csc.switchStatement() != null) {
					codeBlock.addStatement(buildSwitchStatement(csc.switchStatement()));
				} else if (csc.tryStatement() != null) {
					codeBlock.addStatement(buildTryStatement(csc.tryStatement()));
				} else {
					throw new Exception("Unrecognised control statement type: " + statementContext.getText());
				}

			} else if (statementContext.lineStatement() != null) {

				BaliParser.LineStatementContext lsc = statementContext.lineStatement();
				if (lsc.variableDeclaration() != null) {
					codeBlock.addStatement(buildVariableDeclaration(lsc.variableDeclaration()));
				} else if (lsc.returnStatement() != null) {
					codeBlock.addStatement(buildReturnStatement(lsc.returnStatement()));
				} else if (lsc.throwStatement() != null) {
					codeBlock.addStatement(buildThrowStatement(lsc.throwStatement()));
				} else if (lsc.breakStatement() != null) {
					codeBlock.addStatement(buildBreakStatement(lsc.breakStatement()));
				} else if (lsc.continueStatement() != null) {
					codeBlock.addStatement(buildContinueStatement(lsc.continueStatement()));
				} else if (lsc.assignment() != null) {
					codeBlock.addStatement(buildAssignmentStatement(lsc.assignment()));
				} else if (lsc.expression() != null) {
					codeBlock.addStatement(buildExpression(lsc.expression()));
				} else {
					throw new Exception("Unrecognised line statement type: " + statementContext.getText());
				}

//				TODO: run, sleep, wait, release

			} else {
				throw new Exception("Unrecognised statement type: " + statementContext.getText());
			}
		}

		return codeBlock;
	}

	private StatementNode buildWhileStatement(BaliParser.WhileStatementContext context) throws Exception {
		WhileStatementNode whileStatement = new WhileStatementNode(l(context), c(context));
		whileStatement.setCondition(buildExpression(context.expression()));
		whileStatement.setBody(buildCodeBlock(context.codeBlock()));
		return whileStatement;
	}

	private StatementNode buildForStatement(BaliParser.ForStatementContext context) throws Exception {
		ForStatementNode forStatement = new ForStatementNode(l(context), c(context));
		forStatement.setElement(buildArgument(context.argumentDeclaration()));
		forStatement.setCollection(buildExpression(context.expression()));
		forStatement.setBody(buildCodeBlock(context.codeBlock()));
		return forStatement;
	}

	private StatementNode buildSwitchStatement(BaliParser.SwitchStatementContext context) throws Exception {
		SwitchStatementNode switchStatement = new SwitchStatementNode(l(context), c(context));
		switchStatement.setValue(buildExpression(context.expression()));
		for (BaliParser.CaseStatementContext caseStatementContext : context.caseStatement()) {
			switchStatement.addCaseStatement(buildCaseStatement(caseStatementContext));
		}
		if (context.defaultStatement() != null) {
			switchStatement.setDefaultStatement(buildDefaultStatement(context.defaultStatement()));
		}
		return switchStatement;
	}

	private StatementNode buildTryStatement(BaliParser.TryStatementContext context) throws Exception {
		TryStatementNode tryStatement = new TryStatementNode(l(context), c(context));
		tryStatement.setMain(buildCodeBlock(context.codeBlock()));
		for (BaliParser.CatchStatementContext catchContext : context.catchStatement()) {
			tryStatement.addCatchStatement(buildCatchStatement(catchContext));
		}
		return tryStatement;
	}

	private CatchStatementNode buildCatchStatement(BaliParser.CatchStatementContext context) throws Exception {
		CatchStatementNode catchStatement = new CatchStatementNode(l(context), c(context));
		catchStatement.setDeclaration(buildArgument(context.argumentDeclaration()));
		catchStatement.setCodeBlock(buildCodeBlock(context.codeBlock()));
		return catchStatement;
	}

	private CaseStatementNode buildCaseStatement(BaliParser.CaseStatementContext context) throws Exception {
		CaseStatementNode caseStatement = new CaseStatementNode(l(context), c(context));
		caseStatement.setCondition(buildExpression(context.expression()));
		caseStatement.setBody(buildCodeBlock(context.codeBlock()));
		return caseStatement;
	}

	private CodeBlockNode buildDefaultStatement(BaliParser.DefaultStatementContext context) throws Exception {
		return buildCodeBlock(context.codeBlock());
	}

	private StatementNode buildAssignmentStatement(BaliParser.AssignmentContext context) {
		AssignmentNode assignment = new AssignmentNode(l(context), c(context));
		assignment.setReference(getReferenceExpression(context.identifier()));
		assignment.setValue(buildExpression(context.expression()));
		return assignment;
	}

	private ReturnStatementNode buildReturnStatement(BaliParser.ReturnStatementContext context) {
		ReturnStatementNode ret = new ReturnStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(buildExpression(context.expression()));
		}
		return ret;
	}

	private ThrowStatementNode buildThrowStatement(BaliParser.ThrowStatementContext context) {
		ThrowStatementNode ret = new ThrowStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(buildExpression(context.expression()));
		}
		return ret;
	}

	private BreakStatementNode buildBreakStatement(BaliParser.BreakStatementContext context) {
		return new BreakStatementNode(l(context), c(context));
	}

	private ContinueStatementNode buildContinueStatement(BaliParser.ContinueStatementContext context) {
		return new ContinueStatementNode(l(context), c(context));
	}

	private VariableNode buildVariableDeclaration(BaliParser.VariableDeclarationContext context) {
		VariableNode variable = new VariableNode(l(context), c(context));
		DeclarationNode declaration = new FieldNode(); // A Variable is not really a field, but it has the same structure
		declaration.setName(getIdentifier(context.identifier()));
		declaration.setType(getType(context.typeDeclaration()));
		variable.setDeclaration(declaration);
		if (context.expression() != null) {
			variable.setValue(buildExpression(context.expression()));
		}
		return variable;
	}

	private ConditionalStatementNode buildConditionalStatement(BaliParser.ConditionalStatementContext context) throws Exception {

		Iterator<BaliParser.ExpressionContext> i = context.expression().iterator();
		Iterator<BaliParser.CodeBlockContext> j = context.codeBlock().iterator();

		ConditionalStatementNode conditionalStatement = new ConditionalStatementNode(l(context), c(context));

		while (i.hasNext()) {
			ConditionalBlockNode cb = new ConditionalBlockNode(l(context), c(context));
			cb.setCondition(buildExpression(i.next()));
			cb.setBody(buildCodeBlock(j.next()));
			conditionalStatement.addConditionalBlock(cb);
		}
		if (j.hasNext()) {
			conditionalStatement.setAlternate(buildCodeBlock(j.next()));
		}

		return conditionalStatement;
	}

	private FieldNode buildField(BaliParser.FieldDeclarationContext context) {
		FieldNode field = new FieldNode(l(context), c(context));
		field.setName(context.STANDARD_IDENTIFIER().getText());
		field.setType(getType(context.typeDeclaration()));
		if (context.expression() != null) {
			field.setValue(buildExpression(context.expression()));
		}
		return field;
	}

	private ArgumentDeclarationNode buildArgument(BaliParser.ArgumentDeclarationContext context) {
		ArgumentDeclarationNode argumentDeclaration = new ArgumentDeclarationNode(l(context), c(context));
		argumentDeclaration.setName(context.STANDARD_IDENTIFIER().getText());
		argumentDeclaration.setType(getType(context.typeDeclaration()));
		return argumentDeclaration;
	}

	private PropertyNode buildProperty(BaliParser.PropertyDeclarationContext context) {
		PropertyNode propertyNode = new PropertyNode(l(context), c(context));
		propertyNode.setName(context.STANDARD_IDENTIFIER().getText());
		propertyNode.setType(getType(context.typeDeclaration()));
		return propertyNode;
	}

	private MethodNode buildMethodDeclaration(BaliParser.DeclarationDeclarationContext context) {
		MethodNode method = new MethodNode(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		BaliParser.TypeDeclarationContext typeDeclaration = context.typeDeclaration();
		if (typeDeclaration != null) {
			method.setType(getType(typeDeclaration));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			method.addArgument(buildArgument(adc));
		}
		return method;
	}

	private OperationNode buildOperation(BaliParser.OperationContext context) {

		OperationNode operation = new OperationNode(l(context), c(context));
		List<BaliParser.ExpressionForOperationContext> operands = new ArrayList<>(context.expressionForOperation());
		List<TerminalNode> operators = new ArrayList<>(context.OPERATOR());
		Collections.reverse(operands);
		Collections.reverse(operators);
		Iterator<BaliParser.ExpressionForOperationContext> i = operands.iterator();
		Iterator<TerminalNode> j = operators.iterator();

		operation.setTwo(buildExpression(i.next()));
		OperationNode currentOperation = operation;

		while (i.hasNext()) {
			BaliParser.ExpressionForOperationContext expressionForOperationContext = i.next();
			ExpressionNode next = buildExpression(expressionForOperationContext);
			currentOperation.setOperator(j.next().getText());

			if (i.hasNext()) {
				OperationNode newOperation = new OperationNode(l(expressionForOperationContext), c(expressionForOperationContext));
				newOperation.setTwo(next);
				currentOperation.setOne(newOperation);
				currentOperation = newOperation;
			} else {
				currentOperation.setOne(next);
			}
		}

		return operation;
	}

	private UnaryOperationNode buildUnaryOperation(BaliParser.UnaryOperationContext context) {
		UnaryOperationNode operation = new UnaryOperationNode(l(context), c(context));
		operation.setOperator(context.OPERATOR().getText());
		operation.setTarget(buildExpression(context.expression()));
		return operation;
	}

	private ExpressionNode buildExpression(BaliParser.ExpressionForOperationContext context) {

		if (context.constantValue() != null) {
			return getConstantValue(context.constantValue());
		}
		if (context.identifier() != null) {
			return getReferenceExpression(context.identifier());
		}
		if (context.invocation() != null) {
			return buildInvocationStatement(context.invocation());
		}
		if (context.operation() != null) {
			return buildOperation(context.operation());
		}
		if (context.unaryOperation() != null) {
			return buildUnaryOperation(context.unaryOperation());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ExpressionNode buildExpression(BaliParser.ExpressionContext context) {

		if (context.expressionForOperation() != null) {
			return buildExpression(context.expressionForOperation());
		}

		if (context.operation() != null) {
			return buildOperation(context.operation());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ReferenceNode getReferenceExpression(BaliParser.IdentifierContext context) {
		ReferenceNode v = new ReferenceNode(l(context), c(context));
		v.setName(getIdentifier(context));
		return v;
	}

	private InvocationNode buildInvocationStatement(BaliParser.InvocationContext context) {
		InvocationNode value = new InvocationNode(l(context), c(context));

		ExpressionNode target = null;

		if (context.identifier().size() == 2) {
			target = getReferenceExpression(context.identifier(0));
			value.setMethodName(context.identifier(1).getText());
		} else {
			value.setMethodName(context.identifier(0).getText());
			if (context.constantValue() != null) {
				target = getConstantValue(context.constantValue());
			} else if (context.invocation() != null) {
				target = buildInvocationStatement(context.invocation());
			}
		}

		if (target != null) {
			value.setTarget(target);
		}

		for (BaliParser.ExpressionContext vc : context.argumentList().expression()) {
			value.addArgument(buildExpression(vc));
		}

		return value;
	}

	private String getIdentifier(BaliParser.IdentifierContext context) {
		if (context.STANDARD_IDENTIFIER() != null) {
			return context.STANDARD_IDENTIFIER().getText();
		}
		if (context.CONSTANT_IDENTIFIER() != null) {
			return context.CONSTANT_IDENTIFIER().getText();
		}
		throw new RuntimeException("Unrecognised Identifier Type: " + context.getText());
	}

	private ExpressionNode getConstantValue(BaliParser.ConstantValueContext context) {
		if (context.literal() != null) {
			BaliParser.LiteralContext lc = context.literal();
			if (lc.booleanLiteral() != null) {
				BooleanLiteralExpressionNode blv = new BooleanLiteralExpressionNode(l(context), c(context));
				blv.setSerialization(lc.booleanLiteral().getText());
				return blv;
			}
			if (lc.STRING_LITERAL() != null) {
				StringLiteralExpressionNode slv = new StringLiteralExpressionNode(l(context), c(context));
				String token = lc.STRING_LITERAL().getText();
				slv.setSerialization(token.substring(1, token.length() - 1));
				return slv;
			}
			if (lc.NUMBER_LITERAL() != null) {
				NumberLiteralExpressionNode nlv = new NumberLiteralExpressionNode(l(context), c(context));
				nlv.setSerialization(lc.NUMBER_LITERAL().getText());
				return nlv;
			}
			if (lc.arrayLiteral() != null) {
				ArrayLiteralExpressionNode llv = new ArrayLiteralExpressionNode(l(context), c(context));
				for (BaliParser.ExpressionContext v : lc.arrayLiteral().expression()) {
					llv.addValue(buildExpression(v));
				}
				return llv;
			}
		} else if (context.construction() != null) {
			BaliParser.ConstructionContext cc = context.construction();
			ConstructionExpressionNode value = new ConstructionExpressionNode(l(context), c(context));
			value.setClassName(cc.TYPE_IDENTIFIER().getText());
			for (BaliParser.ExpressionContext vc : cc.argumentList().expression()) {
				value.addArgument(buildExpression(vc));
			}
			return value;
		}
		throw new RuntimeException("Could not get value for constant expression " + context.getText());
	}

	private SiteNode getType(BaliParser.TypeDeclarationContext typeContext) {
		Token className = typeContext.TYPE_IDENTIFIER().getSymbol();
		SiteNode returnType = new SiteNode(className.getLine(), className.getCharPositionInLine());
		returnType.setClassName(className.getText());
		BaliParser.TypeDeclarationListContext parameterTypes = typeContext.typeDeclarationList();
		if (parameterTypes != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : parameterTypes.typeDeclaration()) {
				SiteNode parameterType = getType(typeDeclaration);
				returnType.addParameter(parameterType);
			}
		}

		return returnType;
	}
}
