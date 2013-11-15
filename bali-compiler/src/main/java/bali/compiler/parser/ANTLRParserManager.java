package bali.compiler.parser;

import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.AssignStatementNode;
import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.CaseStatementNode;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.ControlExpressionNode;
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
import bali.compiler.parser.tree.RunStatementNode;
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

import java.io.File;
import java.io.FileReader;
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
			cu.addImport(build(idc));
		}
		for (BaliParser.ConstantDeclarationContext cdc : context.constantDeclaration()) {
			cu.addConstant(build(cdc));
		}
		for (BaliParser.BeanDeclarationContext idc : context.beanDeclaration()) {
			cu.addBean(build(idc));
		}
		for (BaliParser.InterfaceDeclarationContext idc : context.interfaceDeclaration()) {
			cu.addInterface(build(idc));
		}
		for (BaliParser.ClassDeclarationContext cdc : context.classDeclaration()) {
			cu.addClass(build(cdc));
		}

		return cu;
	}

	private Integer l(ParserRuleContext context) {
		return context.getStart().getLine();
	}

	private Integer c(ParserRuleContext context) {
		return context.getStart().getCharPositionInLine();
	}

	private ImportNode build(BaliParser.ImportDeclarationContext context) {
		ImportNode iport = new ImportNode(l(context), c(context));
		iport.setName(context.TYPE_IDENTIFIER().getText());
		return iport;
	}

	private ConstantNode build(BaliParser.ConstantDeclarationContext context) {
		ConstantNode constant = new ConstantNode(l(context), c(context));
		constant.setName(context.CONSTANT_IDENTIFIER().getText());
		constant.setType(build(context.typeDeclaration()));
		constant.setValue(build(context.constantValue()));
		return constant;
	}

	private BeanNode build(BaliParser.BeanDeclarationContext context) {
		BeanNode bean = new BeanNode(l(context), c(context));

		bean.setClassName(context.typeDeclaration().get(0).getText());

		if(context.typeDeclaration().size() > 1){
			bean.setSuperType(build(context.typeDeclaration(1)));
		}

//		for (BaliParser.TypeDeclarationContext typeIdentifier : context.typeDeclaration()){
//			bean.addParameter(getType(typeIdentifier));
//		}

		for (BaliParser.PropertyDeclarationContext pdc : context.propertyDeclaration()) {
			bean.addProperty(build(pdc));
		}

		return bean;
	}

	private InterfaceNode build(BaliParser.InterfaceDeclarationContext context) {
		InterfaceNode iface = new InterfaceNode(l(context), c(context));

		iface.setClassName(context.typeDeclaration().getText());

//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			iface.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : context.typeDeclarationList().typeDeclaration()) {
				iface.addImplementation(build(typeDeclaration));
			}
		}
		for (BaliParser.DeclarationDeclarationContext ddc : context.declarationDeclaration()) {
			iface.addMethod(build(ddc));
		}

		return iface;
	}

	private ClassNode build(BaliParser.ClassDeclarationContext context) throws Exception {
		ClassNode clazz = new ClassNode(l(context), c(context));
		clazz.setClassName(context.typeDeclaration().TYPE_IDENTIFIER().getText());


//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			clazz.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : context.typeDeclarationList().typeDeclaration()) {
				clazz.addImplementation(build(typeDeclaration));
			}
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			clazz.addArgument(build(adc));
		}
		for (BaliParser.FieldDeclarationContext fdc : context.fieldDeclaration()) {
			clazz.addField(build(fdc));
		}
		for (BaliParser.MethodDeclarationContext mdc : context.methodDeclaration()) {
			clazz.addMethod(build(mdc));
		}

		return clazz;
	}

	private MethodDeclarationNode build(BaliParser.MethodDeclarationContext context) throws Exception {
		MethodDeclarationNode method = new MethodDeclarationNode(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		BaliParser.TypeDeclarationContext typeDeclaration = context.typeDeclaration();
		if (typeDeclaration != null) {
			method.setType(build(typeDeclaration));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			method.addArgument(build(adc));
		}
		method.setBody(build(context.codeBlock()));
		return method;
	}

	private CodeBlockNode build(BaliParser.CodeBlockContext context) throws Exception {

		CodeBlockNode codeBlock = new CodeBlockNode(l(context), c(context));

		for (BaliParser.StatementContext statementContext : context.statement()) {

			if (statementContext.controlStatement() != null) {

				codeBlock.addStatement(build(statementContext.controlStatement()));

			} else if (statementContext.lineStatement() != null) {

				BaliParser.LineStatementContext lsc = statementContext.lineStatement();
				if (lsc.variableDeclaration() != null) {
					codeBlock.addStatement(build(lsc.variableDeclaration()));
				} else if (lsc.returnStatement() != null) {
					codeBlock.addStatement(build(lsc.returnStatement()));
				} else if (lsc.assignStatement() != null) {
					codeBlock.addStatement(build(lsc.assignStatement()));
				} else if (lsc.throwStatement() != null) {
					codeBlock.addStatement(build(lsc.throwStatement()));
				} else if (lsc.breakStatement() != null) {
					codeBlock.addStatement(build(lsc.breakStatement()));
				} else if (lsc.continueStatement() != null) {
					codeBlock.addStatement(build(lsc.continueStatement()));
				} else if (lsc.assignment() != null) {
					codeBlock.addStatement(build(lsc.assignment()));
				} else if (lsc.expression() != null) {
					codeBlock.addStatement(build(lsc.expression()));
				} else {
					throw new Exception("Unrecognised line statement type: " + statementContext.getText());
				}

//				TODO: sleep, wait, release

			} else {
				throw new Exception("Unrecognised statement type: " + statementContext.getText());
			}
		}

		return codeBlock;
	}

	private ControlExpressionNode build(BaliParser.ControlStatementContext context) throws Exception {
		if (context.conditionalStatement() != null) {
			return build(context.conditionalStatement());
		} else if (context.forStatement() != null) {
			return build(context.forStatement());
		} else if (context.whileStatement() != null) {
			return build(context.whileStatement());
		} else if (context.switchStatement() != null) {
			return build(context.switchStatement());
		} else if (context.tryStatement() != null) {
			return build(context.tryStatement());
		} else if (context.runStatement() != null) {
			return build(context.runStatement());
		} else {
			throw new Exception("Unrecognised control statement type: " + context.getText());
		}
	}


	private ControlExpressionNode build(BaliParser.RunStatementContext context) throws Exception {
		RunStatementNode runStatement = new RunStatementNode(l(context), c(context));
		runStatement.setBody(build(context.controlExpression()));
		return runStatement;
	}

	private ControlExpressionNode build(BaliParser.WhileStatementContext context) throws Exception {
		WhileStatementNode whileStatement = new WhileStatementNode(l(context), c(context));
		whileStatement.setCondition(build(context.expression()));
		whileStatement.setBody(build(context.controlExpression()));
		return whileStatement;
	}

	private ControlExpressionNode build(BaliParser.ForStatementContext context) throws Exception {
		ForStatementNode forStatement = new ForStatementNode(l(context), c(context));
		forStatement.setElement(build(context.argumentDeclaration()));
		forStatement.setCollection(build(context.expression()));
		forStatement.setBody(build(context.controlExpression()));
		return forStatement;
	}

	private ControlExpressionNode build(BaliParser.SwitchStatementContext context) throws Exception {
		SwitchStatementNode switchStatement = new SwitchStatementNode(l(context), c(context));
		switchStatement.setValue(build(context.expression()));
		for (BaliParser.CaseStatementContext caseStatementContext : context.caseStatement()) {
			switchStatement.addCaseStatement(build(caseStatementContext));
		}
		if (context.defaultStatement() != null) {
			switchStatement.setDefaultStatement(build(context.defaultStatement()));
		}
		return switchStatement;
	}

	private ControlExpressionNode build(BaliParser.TryStatementContext context) throws Exception {
		TryStatementNode tryStatement = new TryStatementNode(l(context), c(context));
		tryStatement.setMain(build(context.controlExpression()));
		for (BaliParser.CatchStatementContext catchContext : context.catchStatement()) {
			tryStatement.addCatchStatement(build(catchContext));
		}
		return tryStatement;
	}

	private CatchStatementNode build(BaliParser.CatchStatementContext context) throws Exception {
		CatchStatementNode catchStatement = new CatchStatementNode(l(context), c(context));
		catchStatement.setDeclaration(build(context.argumentDeclaration()));
		catchStatement.setBody(build(context.controlExpression()));
		return catchStatement;
	}

	private CaseStatementNode build(BaliParser.CaseStatementContext context) throws Exception {
		CaseStatementNode caseStatement = new CaseStatementNode(l(context), c(context));
		caseStatement.setCondition(build(context.expression()));
		caseStatement.setBody(build(context.controlExpression()));
		return caseStatement;
	}

	private ControlExpressionNode build(BaliParser.DefaultStatementContext context) throws Exception {
		return build(context.controlExpression());
	}

	private StatementNode build(BaliParser.AssignmentContext context) throws Exception {
		AssignmentNode assignment = new AssignmentNode(l(context), c(context));
		assignment.setReference(build(context.reference()));
		BaliParser.AssignableContext assignableContext = context.assignable();
		if (assignableContext.expression() != null){
			assignment.setValue(build(assignableContext.expression()));
		} else if (assignableContext.controlExpression() != null){
			assignment.setAssignable(build(assignableContext.controlExpression()));
		}

		return assignment;
	}

	private ReturnStatementNode build(BaliParser.ReturnStatementContext context) {
		ReturnStatementNode ret = new ReturnStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(build(context.expression()));
		}
		return ret;
	}

	private AssignStatementNode build(BaliParser.AssignStatementContext context) {
		AssignStatementNode ret = new AssignStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(build(context.expression()));
		}
		return ret;
	}

	private ThrowStatementNode build(BaliParser.ThrowStatementContext context) {
		ThrowStatementNode ret = new ThrowStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(build(context.expression()));
		}
		return ret;
	}

	private BreakStatementNode build(BaliParser.BreakStatementContext context) {
		return new BreakStatementNode(l(context), c(context));
	}

	private ContinueStatementNode build(BaliParser.ContinueStatementContext context) {
		return new ContinueStatementNode(l(context), c(context));
	}

	private VariableNode build(BaliParser.VariableDeclarationContext context) throws Exception {
		VariableNode variable = new VariableNode(l(context), c(context));
		DeclarationNode declaration = new FieldNode(); // A Variable is not really a field, but it has the same structure
		declaration.setName(build(context.identifier()));
		declaration.setType(build(context.typeDeclaration()));
		variable.setDeclaration(declaration);
		BaliParser.AssignableContext assignableContext = context.assignable();
		if (assignableContext.expression() != null){
			variable.setValue(build(assignableContext.expression()));
		} else if (assignableContext.controlExpression() != null){
			variable.setAssignable(build(assignableContext.controlExpression()));
		}
		return variable;
	}

	private ConditionalStatementNode build(BaliParser.ConditionalStatementContext context) throws Exception {

		ConditionalStatementNode conditionalStatement = new ConditionalStatementNode(l(context), c(context));
		List<BaliParser.ControlExpressionContext> controlExpressions = context.controlExpression();

		conditionalStatement.setCondition(build(context.expression()));
		conditionalStatement.setConditional(build(controlExpressions.get(0)));
		if (controlExpressions.size() > 1){
			conditionalStatement.setAlternate(build(controlExpressions.get(1)));
		}

		return conditionalStatement;
	}

	private FieldNode build(BaliParser.FieldDeclarationContext context) {
		FieldNode field = new FieldNode(l(context), c(context));
		field.setName(context.STANDARD_IDENTIFIER().getText());
		field.setType(build(context.typeDeclaration()));
		if (context.expression() != null) {
			field.setValue(build(context.expression()));
		}
		return field;
	}

	private ArgumentDeclarationNode build(BaliParser.ArgumentDeclarationContext context) {
		ArgumentDeclarationNode argumentDeclaration = new ArgumentDeclarationNode(l(context), c(context));
		argumentDeclaration.setName(context.STANDARD_IDENTIFIER().getText());
		argumentDeclaration.setType(build(context.typeDeclaration()));
		return argumentDeclaration;
	}

	private PropertyNode build(BaliParser.PropertyDeclarationContext context) {
		PropertyNode propertyNode = new PropertyNode(l(context), c(context));
		propertyNode.setName(context.STANDARD_IDENTIFIER().getText());
		propertyNode.setType(build(context.typeDeclaration()));
		return propertyNode;
	}

	private MethodNode build(BaliParser.DeclarationDeclarationContext context) {
		MethodNode method = new MethodNode(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		BaliParser.TypeDeclarationContext typeDeclaration = context.typeDeclaration();
		if (typeDeclaration != null) {
			method.setType(build(typeDeclaration));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()) {
			method.addArgument(build(adc));
		}
		return method;
	}

	private OperationNode build(BaliParser.OperationContext context) {

		OperationNode operation = new OperationNode(l(context), c(context));
		operation.setOne(build(context.expressionForOperation()));
		operation.setTwo(build(context.expression()));
		operation.setOperator(context.operator().getText());
		return operation;
	}

	private UnaryOperationNode build(BaliParser.UnaryOperationContext context) {
		UnaryOperationNode operation = new UnaryOperationNode(l(context), c(context));
		operation.setOperator(context.operator().getText());
		operation.setTarget(build(context.expressionForOperation()));
		return operation;
	}

	private ExpressionNode build(BaliParser.ExpressionBaseContext context) {

		if (context.constantValue() != null) {
			return build(context.constantValue());
		}
		if (context.unaryOperation() != null) {
			return build(context.unaryOperation());
		}
		if (context.operation() != null) {
			return build(context.operation());
		}
		if (context.memberName() != null){
			return build(context.memberName());
		}


		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ExpressionNode build(BaliParser.MemberNameContext context) {
		if (context.identifier() != null){
			ReferenceNode ref = new ReferenceNode(l(context), c(context));
			ref.setName(build(context.identifier()));
			return ref;
		}
		return null;
	}

	private ControlExpressionNode build(BaliParser.ControlExpressionContext context) throws Exception {

		if (context.codeBlock() != null) {
			return build(context.codeBlock());
		}
		if (context.controlStatement() != null){
			return build(context.controlStatement());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ExpressionNode build(BaliParser.ExpressionForOperationContext context) {

		if (context.invocation() != null) {
			return build(context.invocation());
		}
		if (context.reference() != null) {
			return build(context.reference());
		}
		if (context.unaryOperation() != null) {
			return build(context.unaryOperation());
		}
		if (context.expressionBase() != null) {
			return build(context.expressionBase());
		}


		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ExpressionNode build(BaliParser.ExpressionContext context) {

		if (context.operation() != null) {
			return build(context.operation());
		}
		if (context.expressionForOperation() != null){
			return build(context.expressionForOperation());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private ReferenceNode build(BaliParser.ReferenceContext context) {

		ReferenceNode value = new ReferenceNode(l(context), c(context));

		if (context.target() != null){
			value.setTarget(build(context.target()));
		}
		value.setName(build(context.identifier()));

		return value;
	}

	private ExpressionNode build(BaliParser.TargetContext target) {
		if (target.memberName().size() == 0){
			return build(target.expressionBase());
		} else {
			ExpressionNode retarget = build(target.expressionBase());
			for(BaliParser.MemberNameContext memberNameContext : target.memberName()){
				if (memberNameContext.identifier() != null){
					ReferenceNode referenceNode = new ReferenceNode(l(target),c(target));
					referenceNode.setName(build(memberNameContext.identifier()));
					referenceNode.setTarget(retarget);
					retarget = referenceNode;
				} else if (memberNameContext.call() != null){
					InvocationNode invocationNode = new InvocationNode(l(target),c(target));
					invocationNode.setMethodName(build(memberNameContext.call().identifier()));
					for (BaliParser.ExpressionContext vc : memberNameContext.call().argumentList().expression()) {
						invocationNode.addArgument(build(vc));
					}
					invocationNode.setTarget(retarget);
					retarget = invocationNode;
				}
			}
			return retarget;
		}
	}

	private InvocationNode build(BaliParser.InvocationContext context) {

		InvocationNode value = new InvocationNode(l(context), c(context));
		if (context.target() != null){
			value.setTarget(build(context.target()));
		}

		value.setMethodName(build(context.call().identifier()));

		for (BaliParser.ExpressionContext vc : context.call().argumentList().expression()) {
			value.addArgument(build(vc));
		}

		return value;
	}

	private String build(BaliParser.IdentifierContext context) {
		if (context.STANDARD_IDENTIFIER() != null) {
			return context.STANDARD_IDENTIFIER().getText();
		}
		if (context.CONSTANT_IDENTIFIER() != null) {
			return context.CONSTANT_IDENTIFIER().getText();
		}
		throw new RuntimeException("Unrecognised Identifier Type: " + context.getText());
	}

	private ExpressionNode build(BaliParser.ConstantValueContext context) {
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
					llv.addValue(build(v));
				}
				return llv;
			}
		} else if (context.construction() != null) {
			BaliParser.ConstructionContext cc = context.construction();
			ConstructionExpressionNode value = new ConstructionExpressionNode(l(context), c(context));
			value.setClassName(cc.TYPE_IDENTIFIER().getText());
			for (BaliParser.ExpressionContext vc : cc.argumentList().expression()) {
				value.addArgument(build(vc));
			}
			return value;
		}
		throw new RuntimeException("Could not get value for constant expression " + context.getText());
	}

	private SiteNode build(BaliParser.TypeDeclarationContext typeContext) {
		Token className = typeContext.TYPE_IDENTIFIER().getSymbol();
		SiteNode returnType = new SiteNode(className.getLine(), className.getCharPositionInLine());
		returnType.setClassName(className.getText());
		BaliParser.TypeDeclarationListContext parameterTypes = typeContext.typeDeclarationList();
		if (parameterTypes != null) {
			for (BaliParser.TypeDeclarationContext typeDeclaration : parameterTypes.typeDeclaration()) {
				SiteNode parameterType = build(typeDeclaration);
				returnType.addParameter(parameterType);
			}
		}

		return returnType;
	}
}
