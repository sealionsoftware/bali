package bali.compiler.bytecode;

import bali.CharArrayString;
import bali.IdentityBoolean;
import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.CaseStatementNode;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.ConditionalBlockNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.ListLiteralExpressionNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.OperationNode;
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
import bali.compiler.type.Declaration;
import bali.compiler.type.ErasedSite;
import bali.compiler.type.Method;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.UnaryOperator;
import bali.compiler.validation.visitor.UnaryOperationValidator;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMStackManager implements Opcodes {

	private static final int[] INTCODES = new int[]{ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};

	private ASMConverter converter;
	private Map<String, VariableInfo> declaredVariables = new HashMap<>();
	private Deque<Label> scopeHorizonStack = new ArrayDeque<>();
	private Deque<LoopContext> loopContextStack = new ArrayDeque<>();

	private Type erasedType;

	public ASMStackManager(ASMConverter converter, TypeLibrary library) {
		this.converter = converter;
		erasedType = new Type(Object.class.getName(),
				Collections.<Declaration>emptyList(),
				Collections.<Site>emptyList(),
				Collections.<Declaration>emptyList(),
				Collections.<Method>emptyList(),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				Collections.<Declaration>emptyList(),
				false
		);
	}

	public List<VariableInfo> getDeclaredVariables() {
		return new ArrayList<>(declaredVariables.values());
	}

	// Execute Methods

	public void execute(MethodDeclarationNode method, MethodVisitor v) {
		Label start = new Label();
		Label end = new Label();
		v.visitLabel(start);
		for (DeclarationNode declaration : method.getArguments()) {
			declaredVariables.put(
					declaration.getName(),
					new VariableInfo(
							declaration,
							start,
							end,
							declaredVariables.size() + 1
					)
			);
		}
		execute(method.getBody(), v);
		v.visitLabel(end);

	}

	public void execute(CodeBlockNode codeBlock, MethodVisitor v) {
		scopeHorizonStack.push(new Label());
		for (StatementNode statement : codeBlock.getStatements()) {
			execute(statement, v);
		}
		v.visitLabel(scopeHorizonStack.pop());
	}

	public void execute(StatementNode statement, MethodVisitor v) {

		Label label = new Label();
		v.visitLabel(label);
		Integer lineNumber = statement.getLine();
		if (lineNumber != null) {
			v.visitLineNumber(statement.getLine(), label);
		}

		if (statement instanceof ReturnStatementNode) {
			execute((ReturnStatementNode) statement, v);
		} else if (statement instanceof ThrowStatementNode) {
			execute((ThrowStatementNode) statement, v);
		} else if (statement instanceof BreakStatementNode) {
			execute((BreakStatementNode) statement, v);
		} else if (statement instanceof ContinueStatementNode) {
			execute((ContinueStatementNode) statement, v);
		} else if (statement instanceof ExpressionNode) {
			execute((ExpressionNode) statement, v);
		} else if (statement instanceof VariableNode) {
			execute((VariableNode) statement, v);
		} else if (statement instanceof AssignmentNode) {
			execute((AssignmentNode) statement, v);
		} else if (statement instanceof ConditionalStatementNode) {
			execute((ConditionalStatementNode) statement, v);
		} else if (statement instanceof WhileStatementNode) {
			execute((WhileStatementNode) statement, v);
		} else if (statement instanceof ForStatementNode) {
			execute((ForStatementNode) statement, v);
		} else if (statement instanceof SwitchStatementNode) {
			execute((SwitchStatementNode) statement, v);
		} else if (statement instanceof TryStatementNode) {
			execute((TryStatementNode) statement, v);
		} else {
			throw new RuntimeException("Cannot handle Statement type: " + statement);
		}
	}

	private void execute(ReturnStatementNode statement, MethodVisitor v) {
		ExpressionNode value = statement.getValue();
		if (value != null) {
			push(value, v);
			v.visitInsn(ARETURN);
		} else {
			v.visitInsn(RETURN);
		}
	}

	private void execute(ThrowStatementNode statement, MethodVisitor v) {
		push(statement.getValue(), v);
		v.visitInsn(ATHROW);
	}

	private void execute(BreakStatementNode statement, MethodVisitor v) {
		v.visitJumpInsn(GOTO, loopContextStack.peek().getEnd());
	}

	private void execute(ContinueStatementNode statement, MethodVisitor v) {
		v.visitJumpInsn(GOTO, loopContextStack.peek().getStart());
	}

	private void execute(ExpressionNode statement, MethodVisitor v) {
		push(statement, v);
		if (statement.getType() != null) {
			v.visitInsn(POP);
		}
	}

	private void execute(VariableNode variable, MethodVisitor v) {
		ExpressionNode value = variable.getValue();
		if (value != null) {
			push(value, v);
		} else {
			v.visitInsn(ACONST_NULL);
		}
		Label varStart = new Label();
		v.visitLabel(varStart);
		addToVariables(variable.getDeclaration(), varStart, scopeHorizonStack.peek(), v);
	}

	private void execute(AssignmentNode statement, MethodVisitor v) {
		Integer index = declaredVariables.get(statement.getReference().getName()).getIndex();
		push(statement.getValue(), v);
		v.visitVarInsn(ASTORE, index);
	}

	private void execute(ConditionalStatementNode statement, MethodVisitor v) {

		Label end = new Label();
		Label next = new Label();
		for (ConditionalBlockNode block : statement.getConditionalBlocks()) {
			push(block.getCondition(), v);
			v.visitFieldInsn(GETSTATIC, "bali/IdentityBoolean", "TRUE", "Lbali/IdentityBoolean;");
			v.visitJumpInsn(IF_ACMPNE, next);
			execute(block.getBody(), v);
			v.visitJumpInsn(GOTO, end);
			v.visitLabel(next);
			next = new Label();
		}
		if (statement.getAlternate() != null) {
			execute(statement.getAlternate(), v);
		}
		v.visitLabel(end);
	}

	private void execute(WhileStatementNode statement, MethodVisitor v) {
		Label end = new Label();
		Label start = new Label();
		v.visitLabel(start);
		push(statement.getCondition(), v);
		push(IdentityBoolean.TRUE, v);
		v.visitJumpInsn(IF_ACMPNE, end);
		loopContextStack.push(new LoopContext(start, end));
		execute(statement.getBody(), v);
		loopContextStack.pop();
		v.visitJumpInsn(GOTO, start);
		v.visitLabel(end);
	}

	private void execute(ForStatementNode statement, MethodVisitor v) {
		Label top = new Label();
		Label start = new Label();
		Label end = new Label();
		v.visitLabel(top);
		push(statement.getCollection(), v);
		Type collectionType = statement.getCollection().getType().getType();
		v.visitMethodInsn(invokeInsn(collectionType), converter.getInternalName(collectionType), "iterator", "()Lbali/Iterator;");
		v.visitLabel(start);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "hasNext", "()Lbali/Boolean;");
		v.visitFieldInsn(GETSTATIC, "bali/IdentityBoolean", "TRUE", "Lbali/IdentityBoolean;");
		v.visitJumpInsn(IF_ACMPNE, end);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "next", "()Ljava/lang/Object;");
		DeclarationNode element = statement.getElement();
		SiteNode variableType = element.getType();
		v.visitTypeInsn(CHECKCAST, converter.getInternalName(variableType.getSite().getName()));
		addToVariables(element, start, end, v);
		loopContextStack.push(new LoopContext(start, end));
		execute(statement.getBody(), v);
		loopContextStack.pop();
		v.visitJumpInsn(GOTO, start);
		v.visitLabel(end);
		v.visitInsn(POP);
	}

	//TODO: Add support for constant switch values
	public void execute(SwitchStatementNode statement, MethodVisitor v) {

		Label end = new Label();
		Label next = new Label();
		push(statement.getValue(), v);
		Type statementType = statement.getValue().getType().getType();
		for (CaseStatementNode caseStatement : statement.getCaseStatements()) {
			v.visitInsn(DUP);
			push(caseStatement.getCondition(), v);
			v.visitMethodInsn(invokeInsn(statementType), converter.getInternalName(statementType), "equalTo", "(Ljava/lang/Object;)Lbali/Boolean;");
			v.visitFieldInsn(GETSTATIC, "bali/IdentityBoolean", "TRUE", "Lbali/Boolean;");
			v.visitJumpInsn(IF_ACMPNE, next);
			execute(caseStatement.getBody(), v);
			v.visitJumpInsn(GOTO, end);
			v.visitLabel(next);
			next = new Label();
		}
		if (statement.getDefaultStatement() != null) {
			execute(statement.getDefaultStatement(), v);
		}
		v.visitLabel(end);
		v.visitInsn(POP);
	}

	public void execute(TryStatementNode statement, MethodVisitor v) {
		Label start = new Label();
		Label end = new Label();
		Map<Label, CatchStatementNode> markers = new LinkedHashMap<>();
		for (CatchStatementNode catchStatement : statement.getCatchStatements()) {
			Label catchStart = new Label();
			markers.put(catchStart, catchStatement);
			v.visitTryCatchBlock(start, catchStart, catchStart, converter.getInternalName(catchStatement.getDeclaration().getType()));
		}
		v.visitLabel(start);
		execute(statement.getMain(), v);
		v.visitJumpInsn(GOTO, end);

		for (Map.Entry<Label, CatchStatementNode> entry : markers.entrySet()) {
			Label catchStart = entry.getKey();
			Label catchEnd = new Label();
			v.visitLabel(catchStart);
			addToVariables(entry.getValue().getDeclaration(), catchStart, catchEnd, v);
			execute(entry.getValue().getCodeBlock(), v);
			v.visitLabel(catchEnd);
			v.visitJumpInsn(GOTO, end);
		}

		v.visitLabel(end);
	}

	private void addToVariables(DeclarationNode declaration, Label start, Label end, MethodVisitor v) {
		String variableName = declaration.getName();
		Integer variableIndex = declaredVariables.size() + 1;
		declaredVariables.put(
				variableName,
				new VariableInfo(
						declaration,
						start,
						end,
						variableIndex
				)
		);
		v.visitVarInsn(ASTORE, variableIndex);
	}

	private int invokeInsn(Type t) {
		return t.isAbstract() ? INVOKEINTERFACE : INVOKEVIRTUAL;
	}

	// Push Methods

	public void push(ExpressionNode value, MethodVisitor v) {
		if (value instanceof NumberLiteralExpressionNode) {
			push((NumberLiteralExpressionNode) value, v);
		} else if (value instanceof BooleanLiteralExpressionNode) {
			push((BooleanLiteralExpressionNode) value, v);
		} else if (value instanceof StringLiteralExpressionNode) {
			push((StringLiteralExpressionNode) value, v);
		} else if (value instanceof ListLiteralExpressionNode) {
			push((ListLiteralExpressionNode) value, v);
		} else if (value instanceof ReferenceNode) {
			push((ReferenceNode) value, v);
		} else if (value instanceof ConstructionExpressionNode) {
			push((ConstructionExpressionNode) value, v);
		} else if (value instanceof InvocationNode) {
			push((InvocationNode) value, v);
		} else if (value instanceof UnaryOperationNode) {
			push((UnaryOperationNode) value, v);
		} else if (value instanceof OperationNode) {
			push((OperationNode) value, v);
		} else {
			throw new RuntimeException("Cannot push value of type " + value + " onto the stack");
		}
	}

	public void push(NumberLiteralExpressionNode value, MethodVisitor v) {
		v.visitFieldInsn(GETSTATIC, "bali/number/NumberFactory", "NUMBER_FACTORY", "Lbali/number/NumberFactory;");
		v.visitLdcInsn(value.getSerialization());
		v.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
		v.visitMethodInsn(INVOKEVIRTUAL, "bali/number/NumberFactory", "forDecimalString", "([C)Lbali/Number;");
	}

	public void push(BooleanLiteralExpressionNode value, MethodVisitor v) {
		Boolean bool = Boolean.valueOf(value.getSerialization());
		v.visitFieldInsn(GETSTATIC, converter.getInternalName(IdentityBoolean.class.getName()), bool ? "TRUE" : "FALSE", converter.getTypeDescriptor(IdentityBoolean.class.getName()));
	}

	public void push(StringLiteralExpressionNode value, MethodVisitor v) {
		String string = value.getSerialization();
		String internalName = converter.getInternalName(CharArrayString.class.getName());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		v.visitLdcInsn(string);
		v.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "([C)V");
	}

	public void push(ListLiteralExpressionNode value, MethodVisitor v) {
		String implName = converter.getInternalName(value.getType().getType());
		v.visitTypeInsn(NEW, implName);
		v.visitInsn(DUP);
		push(value.getValues().size(), v);
		v.visitTypeInsn(ANEWARRAY, "bali/Value");
		int i = 0;
		for (ExpressionNode elementValue : value.getValues()) {
			v.visitInsn(DUP);
			push(i++, v);
			push(elementValue, v);
			v.visitInsn(AASTORE);
		}
		v.visitMethodInsn(INVOKESPECIAL, implName, "<init>", "([Lbali/Value;)V");
	}

	public void push(ReferenceNode value, MethodVisitor v) {
		switch (value.getScope()) {
			case STATIC: {
				v.visitFieldInsn(GETSTATIC, converter.getInternalName(value.getHostClass()), value.getName(), converter.getTypeDescriptor(value.getType().getType()));
			}
			break;
			case FIELD: {
				v.visitVarInsn(ALOAD, 0);
				v.visitFieldInsn(GETFIELD, converter.getInternalName(value.getHostClass()), value.getName(), converter.getTypeDescriptor(value.getType().getType()));
			}
			break;
			case VARIABLE: {
				v.visitVarInsn(ALOAD, declaredVariables.get(value.getName()).getIndex());
			}
			break;
		}
	}

	public void push(ConstructionExpressionNode value, MethodVisitor v) {
		String internalName = converter.getInternalName(value.getType().getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		List<Type> argumentTypes = new ArrayList<>();
		for (ExpressionNode argumentValue : value.getArguments()) {
			push(argumentValue, v);
			argumentTypes.add(argumentValue.getType().getType());
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", converter.getMethodDescriptor(null, argumentTypes));
	}

	public void push(InvocationNode value, MethodVisitor v) {
		List<Site> parameters = new ArrayList<>();
		for (Declaration declaration : value.getResolvedMethod().getParameters()){
			parameters.add(declaration.getType());
		}
		pushInvocation(
				value.getTarget(),
				value.getType(),
				parameters,
				value.getArguments(),
				value.getMethodName(),
				v
		);
	}

	public void push(UnaryOperationNode value, MethodVisitor v) {
		if (UnaryOperationValidator.NULL_CHECK_OPERATOR_NAME.equals(value.getOperator())){
			pushNullCheck(value.getTarget(), v);

		} else {
			pushInvocation(
					value.getTarget(),
					value.getType(),
					Collections.<Site>emptyList(),
					new ArrayList<ExpressionNode>(),
					value.getResolvedOperator().getMethodName(),
					v
			);
		}
	}

	public void pushNullCheck(ExpressionNode target, MethodVisitor v) {
		push(target, v);
		Label isNull = new Label();
		Label end = new Label();
		v.visitJumpInsn(IFNULL, isNull);
		push(IdentityBoolean.TRUE, v);
		v.visitJumpInsn(GOTO, end);
		v.visitLabel(isNull);
		push(IdentityBoolean.FALSE, v);
		v.visitLabel(end);
	}

	public void push(OperationNode value, MethodVisitor v) {
		pushInvocation(
				value.getOne(),
				value.getType(),
				Collections.singletonList(value.getResolvedOperator().getParameter()),
				Collections.singletonList(value.getTwo()),
				value.getResolvedOperator().getMethodName(),
				v
		);
	}

	public void pushInvocation(ExpressionNode target, Site valueType, List<Site> parameterTypes, List<ExpressionNode> arguments, String methodName, MethodVisitor v) {
		push(target, v);
		Type targetType = target.getType().getType();
		List<Type> argumentsErased = new ArrayList<>();

		Iterator<ExpressionNode> i = arguments.iterator();
		Iterator<Site> j = parameterTypes.iterator();

		while (i.hasNext()){
			push(i.next(), v);
			argumentsErased.add(getErasure(j.next()));
		}

		Type valueErased = getErasure(valueType);
		v.visitMethodInsn(invokeInsn(targetType),
				converter.getInternalName(targetType),
				methodName,
				converter.getMethodDescriptor(valueErased, argumentsErased));
		if (valueType != null && !valueErased.getName().equals(valueType.getName())) {
			v.visitTypeInsn(CHECKCAST, converter.getInternalName(valueType.getName()));
		}
	}

	private Type getErasure(Site site){
		if (site == null){
			return null;
		}
		if (site instanceof ErasedSite){
			ErasedSite erased = (ErasedSite) site;
			Type bound = erased.getBoundType();
			return bound != null ? bound : erasedType;
		}
		return site.getType();
	}

	public void push(int i, MethodVisitor v) {
		if (i >= -1 && i <= 5) {
			v.visitInsn(INTCODES[i + 1]);
		} else if (i >= Byte.MIN_VALUE || i <= Byte.MAX_VALUE) {
			v.visitIntInsn(BIPUSH, i);
		} else if (i >= -Short.MIN_VALUE || i <= Short.MAX_VALUE) {
			v.visitIntInsn(SIPUSH, i);
		} else {
			v.visitLdcInsn(i);
		}
	}

	private void push(bali.IdentityBoolean b, MethodVisitor v){
		v.visitFieldInsn(GETSTATIC, "bali/IdentityBoolean", b.name(), "Lbali/IdentityBoolean;");
	}

}
