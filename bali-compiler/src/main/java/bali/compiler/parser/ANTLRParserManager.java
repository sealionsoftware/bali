package bali.compiler.parser;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralValue;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.ConditionalBlock;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.ConstructionValue;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.ForStatement;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.ListLiteralValue;
import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.StringLiteralValue;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.ReferenceValue;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.Value;
import bali.compiler.parser.tree.Variable;
import bali.compiler.parser.tree.WhileStatement;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

/**
 * Parser manager that uses a generated ANTLR Parser to construct an AST
 *
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


		return build(parser.packageDeclaration(), name);
	}

	private CompilationUnit build(BaliParser.PackageDeclarationContext context, String name) throws Exception {

		CompilationUnit cu = new CompilationUnit(l(context), c(context));
		cu.setName(name);

		for (BaliParser.ImportDeclarationContext idc : context.importDeclaration()){
			cu.addImport(buildImport(idc));
		}
		for (BaliParser.ConstantDeclarationContext cdc : context.constantDeclaration()){
			cu.addConstant(buildConstant(cdc));
		}
		for (BaliParser.InterfaceDeclarationContext idc : context.interfaceDeclaration()){
			cu.addInterface(buildInterface(idc));
		}
		for (BaliParser.ClassDeclarationContext cdc : context.classDeclaration()){
			cu.addClass(buildClassDeclaration(cdc));
		}

		return cu;
	}

	private Integer l(ParserRuleContext context){
		return context.getStart().getLine();
	}

	private Integer c(ParserRuleContext context){
		return context.getStart().getCharPositionInLine();
	}


	private Import buildImport(BaliParser.ImportDeclarationContext context){
		Import iport = new Import(l(context), c(context));
		iport.setName(context.TYPE_IDENTIFIER().getText());
		return iport;
	}

	private Constant buildConstant(BaliParser.ConstantDeclarationContext context){
		Constant constant = new Constant(l(context), c(context));
		constant.setName(context.CONSTANT_IDENTIFIER().getText());
		constant.setType(getType(context.TYPE_IDENTIFIER()));
		constant.setValue(getConstantValue(context.constantValue()));
		return constant;
	}

	private Interface buildInterface(BaliParser.InterfaceDeclarationContext context){
		Interface iface = new Interface(l(context), c(context));

		iface.setClassName(context.TYPE_IDENTIFIER().getText());

//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			iface.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null){
			for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
				iface.addSuperInterface(getType(typeIdentifier));
			}
		}
		for (BaliParser.DeclarationDeclarationContext ddc : context.declarationDeclaration()){
			iface.addMethodDeclaration(buildMethodDeclaration(ddc));
		}

		return iface;
	}

	private Class buildClassDeclaration(BaliParser.ClassDeclarationContext context) throws Exception {
		Class clazz = new Class(l(context), c(context));
		clazz.setClassName(context.TYPE_IDENTIFIER().getText());


//		for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
//			clazz.addParameter(getType(typeIdentifier));
//		}
		if (context.typeDeclarationList() != null){
			for (TerminalNode typeIdentifier : context.typeDeclarationList().TYPE_IDENTIFIER()){
				clazz.addInterface(getType(typeIdentifier));
			}
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()){
			clazz.addArgument(buildArgument(adc));
		}
		for (BaliParser.FieldDeclarationContext fdc : context.fieldDeclaration()){
			clazz.addField(buildField(fdc));
		}
		for (BaliParser.MethodDeclarationContext mdc : context.methodDeclaration()){
			clazz.addMethod(buildMethod(mdc));
		}

		return clazz;
	}

	private Method buildMethod(BaliParser.MethodDeclarationContext context) throws Exception{
		Method method = new Method(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		if (context.TYPE_IDENTIFIER() != null){
			method.setType(getType(context.TYPE_IDENTIFIER()));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()){
			method.addArgument(buildArgument(adc));
		}
		method.setBody(buildCodeBlock(context.codeBlock()));
		return method;
	}

	private CodeBlock buildCodeBlock(BaliParser.CodeBlockContext context) throws Exception {

		CodeBlock codeBlock = new CodeBlock(l(context), c(context));

		for (BaliParser.StatementContext statementContext : context.statement()){

			if (statementContext.controlStatement() != null){

				BaliParser.ControlStatementContext csc = statementContext.controlStatement();
				if (csc.conditionalStatement() != null){
					codeBlock.addStatement(buildConditionalStatement(csc.conditionalStatement()));
				} else if (csc.forStatement() != null){
					codeBlock.addStatement(buildForStatement(csc.forStatement()));
				} else if (csc.whileStatement() != null){
					codeBlock.addStatement(buildWhileStatement(csc.whileStatement()));
				} else {
					throw new Exception("Unrecognised control statement type: " + statementContext.getText());
				}

//				TODO: switch, try

			} else if (statementContext.lineStatement() != null){

				BaliParser.LineStatementContext lsc = statementContext.lineStatement();
				if (lsc.variableDeclaration() != null){
					codeBlock.addStatement(buildVariableDeclaration(lsc.variableDeclaration()));
				} else if (lsc.returnStatement() != null){
					codeBlock.addStatement(buildReturnStatement(lsc.returnStatement()));
				} else if (lsc.assignment() != null){
					codeBlock.addStatement(buildAssignmentStatement(lsc.assignment()));
				} else if (lsc.invocation() != null){
					codeBlock.addStatement(buildInvocationStatement(lsc.invocation()));
				} else {
					throw new Exception("Unrecognised line statement type: " + statementContext.getText());
				}

//				TODO: throw, break, continue, run, sleep, wait, release

			} else {
				throw new Exception("Unrecognised statement type: " + statementContext.getText());
			}
		}

		return codeBlock;
	}

	private Statement buildWhileStatement(BaliParser.WhileStatementContext context) throws Exception {
		WhileStatement whileStatement = new WhileStatement(l(context), c(context));
		whileStatement.setCondition(getValue(context.value()));
		whileStatement.setBody(buildCodeBlock(context.codeBlock()));
		return whileStatement;
	}

	private Statement buildForStatement(BaliParser.ForStatementContext context) throws Exception {
		ForStatement forStatement = new ForStatement(l(context), c(context));
		forStatement.setElement(buildDeclaration(context.argumentDeclaration()));
		forStatement.setCollection(getValue(context.value()));
		forStatement.setBody(buildCodeBlock(context.codeBlock()));
		return forStatement;
	}

	private Statement buildAssignmentStatement(BaliParser.AssignmentContext context) {
		Assignment assignment = new Assignment(l(context), c(context));
		assignment.setName(context.STANDARD_IDENTIFIER().getText());
		assignment.setValue(getValue(context.value()));
		return assignment;
	}

	private Return buildReturnStatement(BaliParser.ReturnStatementContext context) {
		Return ret = new Return(l(context), c(context));
		if (context.value() != null){
			ret.setValue(getValue(context.value()));
		}
		return ret;
	}

	private Variable buildVariableDeclaration(BaliParser.VariableDeclarationContext context) {
		Variable variable = new Variable(l(context), c(context));
		variable.setName(context.assignment().STANDARD_IDENTIFIER().getText());
		variable.setType(getType(context.TYPE_IDENTIFIER()));
		variable.setValue(getValue(context.assignment().value()));
		return variable;
	}

	private ConditionalStatement buildConditionalStatement(BaliParser.ConditionalStatementContext context) throws Exception {

		Iterator<BaliParser.ValueContext> i = context.value().iterator();
		Iterator<BaliParser.CodeBlockContext> j = context.codeBlock().iterator();

		ConditionalStatement conditionalStatement = new ConditionalStatement(l(context), c(context));

		while(i.hasNext()){
			ConditionalBlock cb = new ConditionalBlock(l(context), c(context));
			cb.setCondition(getValue(i.next()));
			cb.setBody(buildCodeBlock(j.next()));
			conditionalStatement.addConditionalBlock(cb);
		}
		if(j.hasNext()){
			conditionalStatement.setAlternate(buildCodeBlock(j.next()));
		}

		return conditionalStatement;
	}

	private Field buildField(BaliParser.FieldDeclarationContext context) {
		Field field = new Field(l(context), c(context));
		field.setName(context.STANDARD_IDENTIFIER().getText());
		field.setType(getType(context.TYPE_IDENTIFIER()));
		if (context.value() != null){
			field.setValue(getValue(context.value()));
		}
		return field;
	}

	private Declaration buildArgument(BaliParser.ArgumentDeclarationContext context) {
		Declaration argument = new Declaration(l(context), c(context));
		argument.setName(context.STANDARD_IDENTIFIER().getText());
		argument.setType(getType(context.TYPE_IDENTIFIER()));
		return argument;
	}

	private MethodDeclaration buildMethodDeclaration(BaliParser.DeclarationDeclarationContext context){
		MethodDeclaration method = new MethodDeclaration(l(context), c(context));
		method.setName(context.STANDARD_IDENTIFIER().getText());
		if (context.TYPE_IDENTIFIER() != null){
			method.setType(getType(context.TYPE_IDENTIFIER()));
		}
		for (BaliParser.ArgumentDeclarationContext adc : context.argumentDeclarationList().argumentDeclaration()){
			method.addArgument(buildDeclaration(adc));
		}
		return method;
	}

	private Declaration buildDeclaration(BaliParser.ArgumentDeclarationContext context){
		Declaration declaration = new Declaration(l(context), c(context));
		declaration.setName(context.STANDARD_IDENTIFIER().getText());
		declaration.setType(getType(context.TYPE_IDENTIFIER()));
		return declaration;
	}

	private Value getValue(BaliParser.ValueContext context) {

		if(context.constantValue() != null){
			return getConstantValue(context.constantValue());
		}

		if (context.identifier() != null){
			ReferenceValue v = new ReferenceValue(l(context), c(context));
			v.setName(getIdentifier(context.identifier()));
			return v;
		}

		if(context.invocation() != null){
			BaliParser.InvocationContext ic = context.invocation();
			return buildInvocationStatement(ic);
		}

		throw new RuntimeException("Could not get value for expression " + context.getText());
	}

	private Invocation buildInvocationStatement(BaliParser.InvocationContext context){
		Invocation value = new Invocation(l(context), c(context));
		if (context.identifier().size() == 1){

			value.setMethod(context.identifier(0).getText());
		} else {
			BaliParser.IdentifierContext referenceContext = context.identifier(0);
			ReferenceValue referenceValue = new ReferenceValue(l(referenceContext), c(referenceContext));
			referenceValue.setName(getIdentifier(referenceContext));
			value.setTarget(referenceValue);
			value.setMethod(context.identifier(1).getText());
		}

		for (BaliParser.ValueContext vc : context.argumentList().value()){
			value.addArgument(getValue(vc));
		}

		return value;
	}

	private String getIdentifier(BaliParser.IdentifierContext context){
		if (context.STANDARD_IDENTIFIER() != null){
			return context.STANDARD_IDENTIFIER().getText();
		}
		if (context.CONSTANT_IDENTIFIER() != null){
			return context.CONSTANT_IDENTIFIER().getText();
		}
		throw new RuntimeException("Unrecognised Identifier Type: " + context.getText());
	}

	private Value getConstantValue(BaliParser.ConstantValueContext context) {
		if(context.literal() != null){
			BaliParser.LiteralContext lc = context.literal();
			if (lc.booleanLiteral() != null){
				BooleanLiteralValue blv = new BooleanLiteralValue(l(context), c(context));
				blv.setSerialization(lc.booleanLiteral().getText());
				return blv;
			}
			if (lc.STRING_LITERAL() != null){
				StringLiteralValue slv = new StringLiteralValue(l(context), c(context));
				slv.setSerialization(lc.STRING_LITERAL().getText());
				return slv;
			}
			if (lc.NUMBER_LITERAL() != null){
				NumberLiteralValue nlv = new NumberLiteralValue(l(context), c(context));
				nlv.setSerialization(lc.NUMBER_LITERAL().getText());
				return nlv;
			}
			if (lc.listLiteral() != null){
				ListLiteralValue llv = new ListLiteralValue(l(context), c(context));
				for (BaliParser.ValueContext v : lc.listLiteral().value()){
					llv.addValue(getValue(v));
				}
				return llv;
			}
		} else if (context.construction() != null){
			BaliParser.ConstructionContext cc = context.construction();
			ConstructionValue value = new ConstructionValue(l(context), c(context));
			value.setType(getType(cc.TYPE_IDENTIFIER()));
			for (BaliParser.ValueContext vc : cc.argumentList().value()){
				value.addArgument(getValue(vc));
			}
			return value;
		}
		throw new RuntimeException("Could not get value for constant expression " + context.getText());
	}

	private Type getType(TerminalNode type) {
		Type t = new Type(type.getSymbol().getLine(), type.getSymbol().getCharPositionInLine());
		t.setClassName(type.getText());
		return t;
	}
}
