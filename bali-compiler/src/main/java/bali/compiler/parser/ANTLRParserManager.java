package bali.compiler.parser;

import bali.compiler.parser.tree.Argument;
import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralExpression;
import bali.compiler.parser.tree.BreakStatement;
import bali.compiler.parser.tree.CaseStatement;
import bali.compiler.parser.tree.CatchStatement;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.ConditionalBlock;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.ConstructionExpression;
import bali.compiler.parser.tree.ContinueStatement;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.ForStatement;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.ListLiteralExpression;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.NumberLiteralExpression;
import bali.compiler.parser.tree.Operation;
import bali.compiler.parser.tree.Reference;
import bali.compiler.parser.tree.ReturnStatement;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.StringLiteralExpression;
import bali.compiler.parser.tree.SwitchStatement;
import bali.compiler.parser.tree.ThrowStatement;
import bali.compiler.parser.tree.TryStatement;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.UnaryOperation;
import bali.compiler.parser.tree.Variable;
import bali.compiler.parser.tree.WhileStatement;
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

	public CompilationUnit parse(File compilationUnit, String name) throws Exception {

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

	private CompilationUnit build(BaliParser.PackageDeclarationContext context, String name) throws Exception {

		CompilationUnit cu = new CompilationUnit(l(context), c(context));
		cu.setName(name);

		for (BaliParser.ImportDeclarationContext idc : context.importDeclaration()) {
			cu.addImport(buildImport(idc));
		}
		for (BaliParser.ConstantDeclarationContext cdc : context.constantDeclaration()) {
			cu.addConstant(buildConstant(cdc));
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


	private Import buildImport(BaliParser.ImportDeclarationContext context) {
		Import iport = new Import(l(context), c(context));
		iport.setName(context.TYPE_IDENTIFIER().getText());
		return iport;
	}

	private Constant buildConstant(BaliParser.ConstantDeclarationContext context) {
		Constant constant = new Constant(l(context), c(context));
		constant.setName(context.CONSTANT_IDENTIFIER().getText());
		constant.setType(getType(context.typeDeclaration()));
		constant.setValue(getConstantValue(context.constantValue()));
		return constant;
	}

	private Interface buildInterface(BaliParser.InterfaceDeclarationContext context) {
		Interface iface = new Interface(l(context), c(context));

		iface.setClassName(context.typeDeclaration().getText());

//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			iface.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : context.typeDeclarationList().typeDeclaration()) {
				iface.addSuperInterface(getType(typeDeclaration));
			}
		}
		for (BaliParser.DeclarationDeclarationContext ddc : context.declarationDeclaration()) {
			iface.addMethod(buildMethodDeclaration(ddc));
		}

		return iface;
	}

	private Class buildClassDeclaration(BaliParser.ClassDeclarationContext context) throws Exception {
		Class clazz = new Class(l(context), c(context));
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

	private Method buildMethod(BaliParser.MethodDeclarationContext context) throws Exception {
		Method method = new Method(l(context), c(context));
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

	private CodeBlock buildCodeBlock(BaliParser.CodeBlockContext context) throws Exception {

		CodeBlock codeBlock = new CodeBlock(l(context), c(context));

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

	private Statement buildWhileStatement(BaliParser.WhileStatementContext context) throws Exception {
		WhileStatement whileStatement = new WhileStatement(l(context), c(context));
		whileStatement.setCondition(buildExpression(context.expression()));
		whileStatement.setBody(buildCodeBlock(context.codeBlock()));
		return whileStatement;
	}

	private Statement buildForStatement(BaliParser.ForStatementContext context) throws Exception {
		ForStatement forStatement = new ForStatement(l(context), c(context));
		forStatement.setElement(buildDeclaration(context.argumentDeclaration()));
		forStatement.setCollection(buildExpression(context.expression()));
		forStatement.setBody(buildCodeBlock(context.codeBlock()));
		return forStatement;
	}

	private Statement buildSwitchStatement(BaliParser.SwitchStatementContext context) throws Exception {
		SwitchStatement switchStatement = new SwitchStatement(l(context), c(context));
		switchStatement.setValue(buildExpression(context.expression()));
		for (BaliParser.CaseStatementContext caseStatementContext : context.caseStatement()) {
			switchStatement.addCaseStatement(buildCaseStatement(caseStatementContext));
		}
		if (context.defaultStatement() != null) {
			switchStatement.setDefaultStatement(buildDefaultStatement(context.defaultStatement()));
		}
		return switchStatement;
	}

	private Statement buildTryStatement(BaliParser.TryStatementContext context) throws Exception {
		TryStatement tryStatement = new TryStatement(l(context), c(context));
		tryStatement.setMain(buildCodeBlock(context.codeBlock()));
		for (BaliParser.CatchStatementContext catchContext : context.catchStatement()) {
			tryStatement.addCatchStatement(buildCatchStatement(catchContext));
		}
		return tryStatement;
	}

	private CatchStatement buildCatchStatement(BaliParser.CatchStatementContext context) throws Exception {
		CatchStatement catchStatement = new CatchStatement(l(context), c(context));
		catchStatement.setDeclaration(buildDeclaration(context.argumentDeclaration()));
		catchStatement.setCodeBlock(buildCodeBlock(context.codeBlock()));
		return catchStatement;
	}

	private CaseStatement buildCaseStatement(BaliParser.CaseStatementContext context) throws Exception {
		CaseStatement caseStatement = new CaseStatement(l(context), c(context));
		caseStatement.setCondition(buildExpression(context.expression()));
		caseStatement.setBody(buildCodeBlock(context.codeBlock()));
		return caseStatement;
	}

	private CodeBlock buildDefaultStatement(BaliParser.DefaultStatementContext context) throws Exception {
		return buildCodeBlock(context.codeBlock());
	}

	private Statement buildAssignmentStatement(BaliParser.AssignmentContext context) {
		Assignment assignment = new Assignment(l(context), c(context));
		assignment.setReference(getReferenceExpression(context.identifier()));
		assignment.setValue(buildExpression(context.expression()));
		return assignment;
	}

	private ReturnStatement buildReturnStatement(BaliParser.ReturnStatementContext context) {
		ReturnStatement ret = new ReturnStatement(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(buildExpression(context.expression()));
		}
		return ret;
	}

	private ThrowStatement buildThrowStatement(BaliParser.ThrowStatementContext context) {
		ThrowStatement ret = new ThrowStatement(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(buildExpression(context.expression()));
		}
		return ret;
	}

	private BreakStatement buildBreakStatement(BaliParser.BreakStatementContext context) {
		return new BreakStatement(l(context), c(context));
	}

	private ContinueStatement buildContinueStatement(BaliParser.ContinueStatementContext context) {
		return new ContinueStatement(l(context), c(context));
	}

	private Variable buildVariableDeclaration(BaliParser.VariableDeclarationContext context) {
		Variable variable = new Variable(l(context), c(context));
		Declaration declaration = new Field(); // A Variable is not really a field, but it has the same structure
		declaration.setName(getIdentifier(context.identifier()));
		declaration.setType(getType(context.typeDeclaration()));
		variable.setDeclaration(declaration);
		if (context.expression() != null) {
			variable.setValue(buildExpression(context.expression()));
		}
		return variable;
	}

	private ConditionalStatement buildConditionalStatement(BaliParser.ConditionalStatementContext context) throws Exception {

		Iterator<BaliParser.ExpressionContext> i = context.expression().iterator();
		Iterator<BaliParser.CodeBlockContext> j = context.codeBlock().iterator();

		ConditionalStatement conditionalStatement = new ConditionalStatement(l(context), c(context));

		while (i.hasNext()) {
			ConditionalBlock cb = new ConditionalBlock(l(context), c(context));
			cb.setCondition(buildExpression(i.next()));
			cb.setBody(buildCodeBlock(j.next()));
			conditionalStatement.addConditionalBlock(cb);
		}
		if (j.hasNext()) {
			conditionalStatement.setAlternate(buildCodeBlock(j.next()));
		}

		return conditionalStatement;
	}

	private Field buildField(BaliParser.FieldDeclarationContext context) {
		Field field = new Field(l(context), c(context));
		field.setName(context.STANDARD_IDENTIFIER().getText());
		field.setType(getType(context.typeDeclaration()));
		if (context.expression() != null) {
			field.setValue(buildExpression(context.expression()));
		}
		return field;
	}

	private Declaration buildArgument(BaliParser.ArgumentDeclarationContext context) {
		Declaration argument = new Argument(l(context), c(context));
		argument.setName(context.STANDARD_IDENTIFIER().getText());
		argument.setType(getType(context.typeDeclaration()));
		return argument;
	}

	private MethodDeclaration buildMethodDeclaration(BaliParser.DeclarationDeclarationContext context) {
		MethodDeclaration method = new MethodDeclaration(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		BaliParser.TypeDeclarationContext typeDeclaration = context.typeDeclaration();
		if (typeDeclaration != null) {
			method.setType(getType(typeDeclaration));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			method.addArgument(buildDeclaration(adc));
		}
		return method;
	}

	private Declaration buildDeclaration(BaliParser.ArgumentDeclarationContext context) {
		Declaration declaration = new Argument(l(context), c(context));
		declaration.setName(context.STANDARD_IDENTIFIER().getText());
		declaration.setType(getType(context.typeDeclaration()));
		return declaration;
	}

	private Operation buildOperation(BaliParser.OperationContext context) {

		Operation operation = new Operation(l(context), c(context));
		List<BaliParser.ExpressionForOperationContext> operands = new ArrayList<>(context.expressionForOperation());
		List<TerminalNode> operators = new ArrayList<>(context.OPERATOR());
		Collections.reverse(operands);
		Collections.reverse(operators);
		Iterator<BaliParser.ExpressionForOperationContext> i = operands.iterator();
		Iterator<TerminalNode> j = operators.iterator();

		operation.setTwo(buildExpression(i.next()));
		Operation currentOperation = operation;

		while (i.hasNext()) {
			BaliParser.ExpressionForOperationContext expressionForOperationContext = i.next();
			Expression next = buildExpression(expressionForOperationContext);
			currentOperation.setOperator(j.next().getText());

			if (i.hasNext()) {
				Operation newOperation = new Operation(l(expressionForOperationContext), c(expressionForOperationContext));
				newOperation.setTwo(next);
				currentOperation.setOne(newOperation);
				currentOperation = newOperation;
			} else {
				currentOperation.setOne(next);
			}
		}

		return operation;
	}

	private UnaryOperation buildUnaryOperation(BaliParser.UnaryOperationContext context) {
		UnaryOperation operation = new UnaryOperation(l(context), c(context));
		operation.setOperator(context.OPERATOR().getText());
		operation.setTarget(buildExpression(context.expressionForOperation()));
		return operation;
	}

	private Expression buildExpression(BaliParser.ExpressionForOperationContext context) {

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

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private Expression buildExpression(BaliParser.ExpressionContext context) {

		if (context.expressionForOperation() != null) {
			return buildExpression(context.expressionForOperation());
		}

		if (context.unaryOperation() != null) {
			return buildUnaryOperation(context.unaryOperation());
		}
		if (context.operation() != null) {
			return buildOperation(context.operation());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private Reference getReferenceExpression(BaliParser.IdentifierContext context) {
		Reference v = new Reference(l(context), c(context));
		v.setName(getIdentifier(context));
		return v;
	}

	private Invocation buildInvocationStatement(BaliParser.InvocationContext context) {
		Invocation value = new Invocation(l(context), c(context));

		Expression target = null;

		if (context.identifier().size() == 2) {
			target = getReferenceExpression(context.identifier(0));
			value.setMethod(context.identifier(1).getText());
		} else {
			value.setMethod(context.identifier(0).getText());
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

	private Expression getConstantValue(BaliParser.ConstantValueContext context) {
		if (context.literal() != null) {
			BaliParser.LiteralContext lc = context.literal();
			if (lc.booleanLiteral() != null) {
				BooleanLiteralExpression blv = new BooleanLiteralExpression(l(context), c(context));
				blv.setSerialization(lc.booleanLiteral().getText());
				return blv;
			}
			if (lc.STRING_LITERAL() != null) {
				StringLiteralExpression slv = new StringLiteralExpression(l(context), c(context));
				String token = lc.STRING_LITERAL().getText();
				slv.setSerialization(token.substring(1, token.length() - 1));
				return slv;
			}
			if (lc.NUMBER_LITERAL() != null) {
				NumberLiteralExpression nlv = new NumberLiteralExpression(l(context), c(context));
				nlv.setSerialization(lc.NUMBER_LITERAL().getText());
				return nlv;
			}
			if (lc.listLiteral() != null) {
				ListLiteralExpression llv = new ListLiteralExpression(l(context), c(context));
				for (BaliParser.ExpressionContext v : lc.listLiteral().expression()) {
					llv.addValue(buildExpression(v));
				}
				return llv;
			}
		} else if (context.construction() != null) {
			BaliParser.ConstructionContext cc = context.construction();
			ConstructionExpression value = new ConstructionExpression(l(context), c(context));
			value.setType(getType(cc.typeDeclaration()));
			for (BaliParser.ExpressionContext vc : cc.argumentList().expression()) {
				value.addArgument(buildExpression(vc));
			}
			return value;
		}
		throw new RuntimeException("Could not get value for constant expression " + context.getText());
	}

	private Type getType(BaliParser.TypeDeclarationContext typeContext) {
		Token className = typeContext.TYPE_IDENTIFIER().getSymbol();
		Type returnType = new Type(className.getLine(), className.getCharPositionInLine());
		returnType.setClassName(className.getText());
		BaliParser.TypeDeclarationListContext parameterTypes = typeContext.typeDeclarationList();
		if (parameterTypes != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : parameterTypes.typeDeclaration()) {
				Type parameterType = getType(typeDeclaration);
				parameterType.setErase(true);
				returnType.addParameter(parameterType);
			}
		}

		return returnType;
	}
}
