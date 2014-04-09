package bali.compiler.parser;

import bali.compiler.PackageDescription;
import bali.compiler.parser.tree.ArgumentNode;
import bali.compiler.parser.tree.ArrayLiteralExpressionNode;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.CaseStatementNode;
import bali.compiler.parser.tree.CatchStatementNode;
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
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.NullCheckNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.ReferenceAssignmentNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.RunStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.parser.tree.SwitchStatementNode;
import bali.compiler.parser.tree.ThrowStatementNode;
import bali.compiler.parser.tree.TryStatementNode;
import bali.compiler.parser.tree.TypeParameterNode;
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

import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser manager that uses a generated ANTLR Parser to construct an AST, there is no validation here, but there may be
 * implementation of syntax sugar
 * <p/>
 * User: Richard
 * Date: 30/04/13
 */
public class ANTLRParserManager implements ParserManager {

	public static final Pattern STRING_VARIABLE = Pattern.compile("\\$([a-zA-Z_]+)");

	public CompilationUnitNode parse(PackageDescription description) throws Exception {

		ANTLRInputStream input = new ANTLRInputStream(new InputStreamReader(description.getFile()));
		Lexer lexer = new BaliLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		BaliParser parser = new BaliParser(tokens);
		parser.setErrorHandler(new DefaultErrorStrategy());
		BaliParser.PackageDeclarationContext context = parser.packageDeclaration();

		int errors = parser.getNumberOfSyntaxErrors();
		if (errors > 0) {
			throw new Exception("Could not parse " + description.getSourceFileName() + " [" + errors + " errors]");
		}

		return build(context, description.getName(), description.getSourceFileName());
	}

	public CompilationUnitNode build(BaliParser.PackageDeclarationContext context, String name, String source) throws Exception {

		CompilationUnitNode cu = new CompilationUnitNode(l(context), c(context));
		cu.setName(name);
		cu.setSourceFile(source);

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
		for (BaliParser.ObjectDeclarationContext cdc : context.objectDeclaration()) {
			cu.addClass(build(cdc));
		}

		return cu;
	}

	public Integer l(ParserRuleContext context) {
		return context.getStart().getLine();
	}

	public Integer c(ParserRuleContext context) {
		return context.getStart().getCharPositionInLine();
	}

	public ImportNode build(BaliParser.ImportDeclarationContext context) {
		ImportNode iport = new ImportNode(l(context), c(context));
		iport.setName(context.className().getText());
		return iport;
	}

	public ConstantNode build(BaliParser.ConstantDeclarationContext context) {
		ConstantNode constant = new ConstantNode(l(context), c(context));
		constant.setName(context.IDENTIFIER().getText());
		constant.setType(build(context.siteDefinition()));
		constant.setValue(build(context.expression()));
		return constant;
	}

	public BeanNode build(BaliParser.BeanDeclarationContext context) {
		BeanNode bean = new BeanNode(l(context), c(context));

		bean.setClassName(context.typeDefinition().className().getText());

		if(context.siteDefinition()!= null){
			bean.setSuperType(build(context.siteDefinition()));
		}

		BaliParser.TypeParamDeclarationListContext tpdlc = context.typeDefinition().typeParamDeclarationList();
		if (tpdlc != null) for (BaliParser.TypeParamDeclarationContext typeIdentifier : tpdlc.typeParamDeclaration()){
			bean.addTypeParameter(build(typeIdentifier));
		}

		for (BaliParser.PropertyDeclarationContext pdc : context.propertyDeclaration()) {
			bean.addProperty(build(pdc));
		}

		return bean;
	}

	public InterfaceNode build(BaliParser.InterfaceDeclarationContext context) {
		InterfaceNode iface = new InterfaceNode(l(context), c(context));

		iface.setClassName(context.typeDefinition().className().getText());

		BaliParser.TypeParamDeclarationListContext tpdlc = context.typeDefinition().typeParamDeclarationList();
		if (tpdlc != null) for (BaliParser.TypeParamDeclarationContext typeIdentifier : tpdlc.typeParamDeclaration()){
			iface.addTypeParameter(build(typeIdentifier));
		}

		if (context.siteDefinitionList() != null) {
			for (BaliParser.SiteDefinitionContext typeDeclaration : context.siteDefinitionList().siteDefinition()) {
				iface.addImplementation(build(typeDeclaration));
			}
		}
		for (BaliParser.DeclarationDeclarationContext ddc : context.declarationDeclaration()) {
			iface.addMethod(build(ddc));
		}

		return iface;
	}

	public TypeParameterNode build(BaliParser.TypeParamDeclarationContext context){
		TypeParameterNode ret = new TypeParameterNode();
		ret.setName(context.IDENTIFIER().getText());
		BaliParser.SiteDefinitionContext sdc = context.siteDefinition();
		if (sdc != null){
			ret.setType(build(context.siteDefinition()));
		}

		return ret;
	}

	public ObjectNode build(BaliParser.ObjectDeclarationContext context) throws Exception {
		ObjectNode object = new ObjectNode(l(context), c(context));
		object.setClassName(context.typeDefinition().className().getText());


		BaliParser.TypeParamDeclarationListContext tpdlc = context.typeDefinition().typeParamDeclarationList();
		if (tpdlc != null) for (BaliParser.TypeParamDeclarationContext typeIdentifier : tpdlc.typeParamDeclaration()){
			object.addTypeParameter(build(typeIdentifier));
		}

		if (context.siteDefinitionList() != null) {
			for (BaliParser.SiteDefinitionContext typeDeclaration : context.siteDefinitionList().siteDefinition()) {
				object.addImplementation(build(typeDeclaration));
			}
		}
		BaliParser.ParameterListContext parameterList = context.parameterList();
		if (parameterList != null) for (BaliParser.ParameterContext parameter : parameterList.parameter()) {
			object.addParameter(build(parameter));
		}
		for (BaliParser.FieldDeclarationContext fdc : context.fieldDeclaration()) {
			object.addField(build(fdc));
		}
		for (BaliParser.MethodDeclarationContext mdc : context.methodDeclaration()) {
			object.addMethod(build(mdc));
		}

		return object;
	}

	public MethodDeclarationNode build(BaliParser.MethodDeclarationContext context) throws Exception {
		MethodDeclarationNode method = new MethodDeclarationNode(l(context), c(context));
		method.setName(context.IDENTIFIER().getText());
		BaliParser.SiteDefinitionContext typeDeclaration = context.siteDefinition();
		if (typeDeclaration != null) {
			method.setType(build(typeDeclaration));
		}
		BaliParser.ParameterListContext parameterList = context.parameterList();
		if (parameterList != null) for (BaliParser.ParameterContext parameter : parameterList.parameter()) {
			method.addParameter(build(parameter));
		}
		method.setBody(build(context.codeBlock()));
		return method;
	}

	public CodeBlockNode build(BaliParser.CodeBlockContext context) throws Exception {

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

	public ControlExpressionNode build(BaliParser.ControlStatementContext context) throws Exception {
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


	public ControlExpressionNode build(BaliParser.RunStatementContext context) throws Exception {
		RunStatementNode runStatement = new RunStatementNode(l(context), c(context));
		runStatement.setBody(build(context.controlExpression()));
		return runStatement;
	}

	public ControlExpressionNode build(BaliParser.WhileStatementContext context) throws Exception {
		WhileStatementNode whileStatement = new WhileStatementNode(l(context), c(context));
		whileStatement.setCondition(build(context.expression()));
		whileStatement.setBody(build(context.controlExpression()));
		return whileStatement;
	}

	public ControlExpressionNode build(BaliParser.ForStatementContext context) throws Exception {
		ForStatementNode forStatement = new ForStatementNode(l(context), c(context));
		forStatement.setElement(build(context.parameter()));
		forStatement.setCollection(build(context.expression()));
		forStatement.setBody(build(context.controlExpression()));
		return forStatement;
	}

	public ControlExpressionNode build(BaliParser.SwitchStatementContext context) throws Exception {
		SwitchStatementNode switchStatement = new SwitchStatementNode(l(context), c(context));
		switchStatement.setValue(build(context.expression()));
		for (BaliParser.CaseBlockContext caseStatementContext : context.caseBlock()) {
			switchStatement.addCaseStatement(build(caseStatementContext));
		}
		if (context.defaultBlock() != null) {
			switchStatement.setDefaultStatement(build(context.defaultBlock()));
		}
		return switchStatement;
	}

	public ControlExpressionNode build(BaliParser.TryStatementContext context) throws Exception {
		TryStatementNode tryStatement = new TryStatementNode(l(context), c(context));
		tryStatement.setMain(build(context.controlExpression()));
		for (BaliParser.CatchBlockContext catchContext : context.catchBlock()) {
			tryStatement.addCatchStatement(build(catchContext));
		}
		return tryStatement;
	}

	public CatchStatementNode build(BaliParser.CatchBlockContext context) throws Exception {
		CatchStatementNode catchStatement = new CatchStatementNode(l(context), c(context));
		catchStatement.setDeclaration(build(context.parameter()));
		catchStatement.setBody(build(context.controlExpression()));
		return catchStatement;
	}

	public CaseStatementNode build(BaliParser.CaseBlockContext context) throws Exception {
		CaseStatementNode caseStatement = new CaseStatementNode(l(context), c(context));
		caseStatement.setCondition(build(context.expression()));
		caseStatement.setBody(build(context.controlExpression()));
		return caseStatement;
	}

	public ControlExpressionNode build(BaliParser.DefaultBlockContext context) throws Exception {
		return build(context.controlExpression());
	}

	public StatementNode build(BaliParser.AssignmentContext context) throws Exception {
		ReferenceAssignmentNode assignment = new ReferenceAssignmentNode(l(context), c(context));
		assignment.setReference(build(context.reference()));
		assignment.setValue(build(context.expression()));
		return assignment;
	}

	public ReturnStatementNode build(BaliParser.ReturnStatementContext context) {
		ReturnStatementNode ret = new ReturnStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(build(context.expression()));
		}
		return ret;
	}

	public ThrowStatementNode build(BaliParser.ThrowStatementContext context) {
		ThrowStatementNode ret = new ThrowStatementNode(l(context), c(context));
		if (context.expression() != null) {
			ret.setValue(build(context.expression()));
		}
		return ret;
	}

	public BreakStatementNode build(BaliParser.BreakStatementContext context) {
		return new BreakStatementNode(l(context), c(context));
	}

	public ContinueStatementNode build(BaliParser.ContinueStatementContext context) {
		return new ContinueStatementNode(l(context), c(context));
	}

	public VariableNode build(BaliParser.VariableDeclarationContext context) throws Exception {
		VariableNode variable = new VariableNode(l(context), c(context));
		DeclarationNode declaration = new FieldNode(); // A Variable is not really a field, but it has the same structure
		declaration.setName(context.IDENTIFIER().getText());
		declaration.setType(build(context.siteDefinition()));
		variable.setDeclaration(declaration);
		if (context.expression() != null){
			variable.setValue(build(context.expression()));
		}
		return variable;
	}

	public ConditionalStatementNode build(BaliParser.ConditionalStatementContext context) throws Exception {

		ConditionalStatementNode conditionalStatement = new ConditionalStatementNode(l(context), c(context));
		List<BaliParser.ControlExpressionContext> controlExpressions = context.controlExpression();

		conditionalStatement.setCondition(build(context.expression()));
		conditionalStatement.setConditional(build(controlExpressions.get(0)));
		if (controlExpressions.size() > 1){
			conditionalStatement.setAlternate(build(controlExpressions.get(1)));
		}

		return conditionalStatement;
	}

	public FieldNode build(BaliParser.FieldDeclarationContext context) {
		FieldNode field = new FieldNode(l(context), c(context));
		field.setName(context.IDENTIFIER().getText());
		field.setType(build(context.siteDefinition()));
		if (context.expression() != null) {
			field.setValue(build(context.expression()));
		}
		return field;
	}

	public ParameterNode build(BaliParser.ParameterContext context) {
		ParameterNode argumentDeclaration = new ParameterNode(l(context), c(context));
		argumentDeclaration.setName(context.IDENTIFIER().getText());
		argumentDeclaration.setType(build(context.siteDefinition()));
		return argumentDeclaration;
	}

	public PropertyNode build(BaliParser.PropertyDeclarationContext context) {
		PropertyNode propertyNode = new PropertyNode(l(context), c(context));
		propertyNode.setName(context.IDENTIFIER().getText());
		propertyNode.setType(build(context.siteDefinition()));
		return propertyNode;
	}

	public MethodNode build(BaliParser.DeclarationDeclarationContext context) {
		MethodNode method = new MethodNode(l(context), c(context));
		method.setName(context.IDENTIFIER().getText());
		BaliParser.SiteDefinitionContext typeDeclaration = context.siteDefinition();
		if (typeDeclaration != null) {
			method.setType(build(typeDeclaration));
		}
		BaliParser.ParameterListContext adlc = context.parameterList();
		if (adlc != null) for (BaliParser.ParameterContext parameter : adlc.parameter()) {
			method.addParameter(build(parameter));
		}
		return method;
	}

	public OperationNode build(BaliParser.OperationContext context) {

		OperationNode operation = new OperationNode(l(context), c(context));
		operation.setOne(build(context.expressionForOperation()));
		operation.setOperator(context.OPERATOR().getText());
		operation.setTwo(build(context.expression()));

		return operation;
	}

	public UnaryOperationNode build(BaliParser.UnaryOperationContext context) {
		UnaryOperationNode operation = new UnaryOperationNode(l(context), c(context));
		operation.setOperator(context.OPERATOR().getText());
		operation.setTarget(build(context.expressionForOperation()));
		return operation;
	}

	public NullCheckNode build(BaliParser.NullCheckContext context) {
		NullCheckNode check = new NullCheckNode(l(context), c(context));
		check.setTarget(build(context.expressionForOperation()));
		return check;
	}

	public ExpressionNode build(BaliParser.ExpressionBaseContext context) {

		if (context.literal() != null) {
			return build(context.literal());
		}
		if (context.construction() != null) {
			return build(context.construction());
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

	public ExpressionNode build(BaliParser.MemberNameContext context) {
		if (context.IDENTIFIER() != null){
			ReferenceNode ref = new ReferenceNode(l(context), c(context));
			ref.setName(context.IDENTIFIER().getText());
			return ref;
		}
		return null;
	}

	public ControlExpressionNode build(BaliParser.ControlExpressionContext context) throws Exception {

		if (context.codeBlock() != null) {
			return build(context.codeBlock());
		}
		if (context.controlStatement() != null){
			return build(context.controlStatement());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	public ExpressionNode build(BaliParser.ExpressionForOperationContext context) {

		if (context.invocation() != null) {
			return build(context.invocation());
		}
		if (context.reference() != null) {
			return build(context.reference());
		}
		if (context.unaryOperation() != null) {
			return build(context.unaryOperation());
		}
		if (context.nullCheck() != null){
			return build(context.nullCheck());
		}
		if (context.expressionBase() != null) {
			return build(context.expressionBase());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	public ExpressionNode build(BaliParser.ExpressionContext context) {

		if (context.operation() != null) {
			return build(context.operation());
		}
		if (context.expressionForOperation() != null){
			return build(context.expressionForOperation());
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	public ReferenceNode build(BaliParser.ReferenceContext context) {

		ReferenceNode value = new ReferenceNode(l(context), c(context));

		if (context.target() != null){
			value.setTarget(build(context.target()));
		}
		value.setName(context.IDENTIFIER().getText());

		return value;
	}

	public ExpressionNode build(BaliParser.TargetContext target) {
		if (target.memberName().size() == 0){
			return build(target.expressionBase());
		} else {
			ExpressionNode retarget = build(target.expressionBase());
			for(BaliParser.MemberNameContext memberNameContext : target.memberName()){
				if (memberNameContext.IDENTIFIER() != null){
					ReferenceNode referenceNode = new ReferenceNode(l(target),c(target));
					referenceNode.setName(memberNameContext.IDENTIFIER().getText());
					referenceNode.setTarget(retarget);
					retarget = referenceNode;
				} else if (memberNameContext.call() != null){
					InvocationNode invocationNode = new InvocationNode(l(target),c(target));
					invocationNode.setMethodName(memberNameContext.call().IDENTIFIER().getText());
					for (BaliParser.ArgumentContext vc : memberNameContext.call().argumentList().argument()) {
						invocationNode.addArgument(build(vc));
					}
					invocationNode.setTarget(retarget);
					retarget = invocationNode;
				}
			}
			return retarget;
		}
	}

	public InvocationNode build(BaliParser.InvocationContext context) {

		InvocationNode value = new InvocationNode(l(context), c(context));
		if (context.target() != null){
			value.setTarget(build(context.target()));
		}

		value.setMethodName(context.call().IDENTIFIER().getText());

		for (BaliParser.ArgumentContext vc : context.call().argumentList().argument()) {
			value.addArgument(build(vc));
		}

		return value;
	}

	public ArgumentNode build(BaliParser.ArgumentContext context) {
		ArgumentNode node = new ArgumentNode(l(context), c(context));
		TerminalNode name = context.IDENTIFIER();
		if (name != null){
			node.setName(name.getText());
		}
		node.setValue(build(context.expression()));
		return node;
	}

	public ExpressionNode build(BaliParser.ConstructionContext context) {

		ConstructionExpressionNode value = new ConstructionExpressionNode(l(context), c(context));
		value.setClassName(context.className().getText());
		for (BaliParser.ArgumentContext vc : context.argumentList().argument()) {
			value.addArgument(build(vc));
		}
		return value;
	}

	public ExpressionNode build(BaliParser.LiteralContext context) {
		if (context.booleanLiteral() != null) {
			BooleanLiteralExpressionNode blv = new BooleanLiteralExpressionNode(l(context), c(context));
			blv.setSerialization(context.booleanLiteral().getText());
			return blv;
		}
		if (context.STRING_LITERAL() != null) {
			return buildString(context);
		}
		if (context.NUMBER_LITERAL() != null) {
			NumberLiteralExpressionNode nlv = new NumberLiteralExpressionNode(l(context), c(context));
			nlv.setSerialization(context.NUMBER_LITERAL().getText());
			return nlv;
		}
		if (context.arrayLiteral() != null) {
			ArrayLiteralExpressionNode llv = new ArrayLiteralExpressionNode(l(context), c(context));
			for (BaliParser.ExpressionContext v : context.arrayLiteral().expression()) {
				llv.addValue(build(v));
			}
			return llv;
		}
		throw new RuntimeException("Could not get value for literal expression " + context.getText());
	}

	public ExpressionNode buildString(BaliParser.LiteralContext context) {
		int l = l(context);
		int c = c(context);
		StringLiteralExpressionNode slv = new StringLiteralExpressionNode(l, c);
		String token = context.getText();
		token = token.substring(1, token.length() - 1);

		Matcher matcher = STRING_VARIABLE.matcher(token);

		if(matcher.find()){

			int start = matcher.start();
			slv.setSerialization(token.substring(0, start));
			ReferenceNode referenceNode = new ReferenceNode(l, c + start);
			referenceNode.setName(matcher.group(1));
			ExpressionNode ret = buildJoin(slv, buildFormat(referenceNode));

			int pos = matcher.end();
			while (matcher.find()){

				start = matcher.start();
				slv = new StringLiteralExpressionNode(l, c + pos);
				slv.setSerialization(token.substring(pos, start));
				ret = buildJoin(ret, slv);

				referenceNode = new ReferenceNode(l, c + start);
				referenceNode.setName(matcher.group(1));
				ret = buildJoin(ret,  buildFormat(referenceNode));

				pos = matcher.end();
			}

			if (pos < token.length()){
				slv = new StringLiteralExpressionNode(l, c);
				slv.setSerialization(token.substring(pos));
				ret = buildJoin(ret, slv);
			}

			return ret;
		} else {

			slv.setSerialization(token);
			return slv;
		}
	}

	private OperationNode buildJoin(ExpressionNode one, ExpressionNode two){
		OperationNode operationNode = new OperationNode(one.getLine(), one.getCharacter());
		operationNode.setOne(one);
		operationNode.setOperator("+");
		operationNode.setTwo(two);
		return operationNode;
	}

	private InvocationNode buildFormat(ExpressionNode toFormat){
		int l = toFormat.getLine();
		int c =  toFormat.getCharacter();
		InvocationNode invocationNode = new InvocationNode(l, c);
		ReferenceNode target = new ReferenceNode(l, c);
		target.setName("VALUE_FORMATTER");
		invocationNode.setTarget(target);
		invocationNode.setMethodName("format");
		ArgumentNode argumentNode = new ArgumentNode(l, c);
		argumentNode.setValue(toFormat);
		invocationNode.addArgument(argumentNode);
		return invocationNode;
	}

	public SiteNode build(BaliParser.SiteDefinitionContext typeContext) {

		BaliParser.ClassNameContext tic = typeContext.className();
		Token start = tic.IDENTIFIER(0).getSymbol();
		SiteNode returnType = new SiteNode(start.getLine(), start.getCharPositionInLine());
		returnType.setClassName(tic.getText());
		BaliParser.SiteDefinitionListContext parameterTypes = typeContext.siteDefinitionList();
		if (parameterTypes != null) {
			for (BaliParser.SiteDefinitionContext typeDeclaration : parameterTypes.siteDefinition()) {
				SiteNode parameterType = build(typeDeclaration);
				returnType.addParameter(parameterType);
			}
		}
		if(typeContext.QM() != null){
			returnType.setNullable(true);
		}
		if(typeContext.AT() != null){
			returnType.setThreadSafe(true);
		}

		return returnType;
	}
}
