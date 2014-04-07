package bali.compiler.bytecode;

import bali.Boolean;
import bali.CharArrayString;
import bali.logic.False;
import bali.logic.True;
import bali.annotation.Kind;
import bali.collection.Array;
import bali.compiler.parser.tree.ArrayLiteralExpressionNode;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.CaseStatementNode;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.ConditionalStatementNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.ControlExpressionNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.NullCheckNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.parser.tree.ReferenceAssignmentNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.RunStatementNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.parser.tree.SwitchStatementNode;
import bali.compiler.parser.tree.ThrowStatementNode;
import bali.compiler.parser.tree.TryStatementNode;
import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.parser.tree.WhileStatementNode;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.Class;
import bali.compiler.type.Declaration;
import bali.compiler.type.ErasedSite;
import bali.compiler.type.MutableClassModel;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static bali.Primitive.convert;
import static bali.compiler.bytecode._.CONVERTER;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMStackManager implements Opcodes {

	public static final Site TYPE_SITE = new ParameterisedSite(new SimpleReference<Class>(new MutableClassModel(bali.type.Type.class.getName())));
	private static final int[] INTCODES = new int[]{ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};

	private List<VariableInfo> declaredVariables = new ArrayList<>();
	private Deque<Label> scopeHorizonStack = new ArrayDeque<>();
	private Deque<LoopContext> loopContextStack = new ArrayDeque<>();

	public List<VariableInfo> getDeclaredVariables() {
		return declaredVariables;
	}

	// Execute Methods

	public void execute(MethodDeclarationNode method, MethodVisitor v) {
		Label start = new Label();
		Label end = new Label();
		v.visitLabel(start);
		for (DeclarationNode declaration : method.getParameters()) {
			declaredVariables.add(
					new VariableInfo(
							declaration.getName(),
							declaration.getType().getSite(),
							start,
							end,
							declaration.getId()
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
		} else if (statement instanceof ReferenceAssignmentNode) {
			execute((ReferenceAssignmentNode) statement, v);
		} else if (statement instanceof ControlExpressionNode) {
			execute((ControlExpressionNode) statement, v);
		} else {
			throw new RuntimeException("Cannot handle Statement type: " + statement);
		}
	}

	public void execute(ControlExpressionNode statement, MethodVisitor v) {

		if (statement instanceof CodeBlockNode) {
			execute((CodeBlockNode) statement, v);
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
		} else if (statement instanceof RunStatementNode) {
			execute((RunStatementNode) statement, v);
		} else {
			throw new RuntimeException("Cannot handle Control Expression type: " + statement);
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
		v.visitTypeInsn(NEW, "bali/BaliThrowable");
		v.visitInsn(DUP);
		push(statement.getValue(), v);
		v.visitMethodInsn(INVOKESPECIAL, "bali/BaliThrowable", "<init>", "(Lbali/Exception;)V");
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
			checkCast(value.getType(), variable.getType(),v);
		} else {
			v.visitInsn(ACONST_NULL);
		}
		Label varStart = new Label();
		v.visitLabel(varStart);
		addToVariables(variable.getDeclaration(), varStart, scopeHorizonStack.peek(), v);
	}

	private void execute(ReferenceAssignmentNode statement, MethodVisitor v) {

		ReferenceNode referenceNode = statement.getReference();
		Type referenceType = referenceNode.getType();
		ExpressionNode targetNode = referenceNode.getTarget();
		ExpressionNode value = statement.getValue();
		Type valueType = value.getType();

		switch (referenceNode.getScope()){
			case FIELD: {
				if (targetNode != null){
					push(targetNode, v);
				} else {
					v.visitVarInsn(ALOAD, 0);
				}

				push(value, v);
				checkCast(valueType, referenceType, v);
				v.visitFieldInsn(PUTFIELD,
						CONVERTER.getInternalName(referenceNode.getHostClass()),
						referenceNode.getName(),
						CONVERTER.getTypeDescriptor(referenceType)
				);
			} break;
			case VARIABLE: {
				push(value, v);
				checkCast(valueType, referenceType, v);
				v.visitVarInsn(ASTORE, getIndex(referenceNode.getId()));
			} break;
			default: {
				throw new RuntimeException("Cannot compile assignment to variable in scope " + referenceNode.getScope());
			}
		}
	}

	private void checkCast(Type stackType, Type requiredType, MethodVisitor v) {
		if (stackType instanceof ErasedSite){
			Type erasure =  ((ErasedSite) stackType).getErasure();
			if (erasure == null || !erasure.isAssignableTo(requiredType)){
				v.visitTypeInsn(CHECKCAST, CONVERTER.getInternalName(requiredType.getTemplate()));
			}
		}
	}

	private void execute(ConditionalStatementNode statement, MethodVisitor v) {

		Label end = new Label();
		Label next = new Label();

		push(statement.getCondition(), v);
		push(True.TRUE, v);
		v.visitJumpInsn(IF_ACMPNE, next);
		execute(statement.getConditional(), v);
		v.visitJumpInsn(GOTO, end);
		v.visitLabel(next);

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
		push(True.TRUE, v);
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
		Class collectionClass = statement.getCollection().getType().getTemplate();
		v.visitMethodInsn(invokeInsn(collectionClass), CONVERTER.getInternalName(collectionClass), "iterator", "()Lbali/Iterator;");
		v.visitLabel(start);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "hasNext", "()Lbali/Boolean;");
		push(True.TRUE, v);
		v.visitJumpInsn(IF_ACMPNE, end);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "next", "()Ljava/lang/Object;");
		DeclarationNode element = statement.getElement();
		v.visitTypeInsn(CHECKCAST, CONVERTER.getInternalName(element.getType().getSite().getTemplate().getName()));
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
		for (CaseStatementNode caseStatement : statement.getCaseStatements()) {
			v.visitInsn(DUP);
			push(caseStatement.getCondition(), v);
			v.visitMethodInsn(INVOKEINTERFACE, "bali/Value", "equalTo", "(Lbali/Value;)Lbali/Boolean;");
			push(True.TRUE, v);
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
		Label mainCatchStart = new Label();
		Label end = new Label();
		v.visitTryCatchBlock(start, mainCatchStart, mainCatchStart, "bali/BaliThrowable");
		v.visitLabel(start);
		execute(statement.getMain(), v);
		v.visitJumpInsn(GOTO, end);
		v.visitLabel(mainCatchStart);
		v.visitInsn(DUP);
		v.visitFieldInsn(GETFIELD, "bali/BaliThrowable", "thrown", "Lbali/Exception;");

		for (CatchStatementNode catchStatement : statement.getCatchStatements()) {
			Label catchStart = new Label();
			Label catchEnd = new Label();
			DeclarationNode declaration = catchStatement.getDeclaration();
			Site site = declaration.getType().getSite();
			v.visitInsn(DUP);
			v.visitTypeInsn(INSTANCEOF, CONVERTER.getInternalName(site.getTemplate()));
			v.visitJumpInsn(IFEQ, catchEnd);
			v.visitInsn(DUP);
			addToVariables(declaration, catchStart, catchEnd, v);
			v.visitLabel(catchStart);
			execute(catchStatement.getBody(), v);
			v.visitLabel(catchEnd);
			v.visitInsn(POP);
			v.visitInsn(POP);
			v.visitJumpInsn(GOTO, end);
		}
		v.visitInsn(POP);
		v.visitInsn(ATHROW);
		v.visitLabel(end);
	}

	private void execute(RunStatementNode statement, MethodVisitor v) {

		String runnableClassName = CONVERTER.getInternalName(statement.getRunnableClassName());

		v.visitTypeInsn(NEW, "java/lang/Thread");
		v.visitInsn(DUP);
		v.visitTypeInsn(NEW, runnableClassName);
		v.visitInsn(DUP);

		List<Site> parameterTypes = new ArrayList<>();
		for (RunStatementNode.RunArgument argument : statement.getArguments()){
			Site parameterType = argument.getType();
			parameterTypes.add(parameterType);
			switch (argument.getScope()) {
				case FIELD: {
					v.visitVarInsn(ALOAD, 0);
					v.visitFieldInsn(GETFIELD, CONVERTER.getInternalName(argument.getHostClassName()), argument.getName(), CONVERTER.getTypeDescriptor(parameterType));
				}
				break;
				case VARIABLE: {
					v.visitVarInsn(ALOAD, getIndex(argument.getId()));
				}
				break;
			}
		}

		v.visitMethodInsn(INVOKESPECIAL, runnableClassName, "<init>", CONVERTER.getMethodDescriptor(null, parameterTypes));
		v.visitMethodInsn(INVOKESPECIAL, "java/lang/Thread", "<init>", "(Ljava/lang/Runnable;)V");
		v.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "start", "()V");
	}

	private void addToVariables(DeclarationNode declaration, Label start, Label end, MethodVisitor v) {
		declaredVariables.add(
				new VariableInfo(
						declaration.getName(),
						declaration.getType().getSite(),
						start,
						end,
						declaration.getId()
				)
		);
		v.visitVarInsn(ASTORE, declaredVariables.size());
	}

	private int getIndex(UUID id){
		int i = 0;
		for (VariableInfo var : declaredVariables){
			i++;
			if (id.equals(var.getId())){
				return i;
			}
		}
		throw new RuntimeException("Compiler error, no variable with id " + id + " has been registered for this method");
	}

	private int invokeInsn(Class t) {
		return Kind.INTERFACE.equals(t.getMetaType()) ? INVOKEINTERFACE : INVOKEVIRTUAL;
	}

	// Push Methods

	public void push(ExpressionNode value, MethodVisitor v) {
		if (value instanceof NumberLiteralExpressionNode) {
			push((NumberLiteralExpressionNode) value, v);
		} else if (value instanceof BooleanLiteralExpressionNode) {
			push((BooleanLiteralExpressionNode) value, v);
		} else if (value instanceof StringLiteralExpressionNode) {
			push((StringLiteralExpressionNode) value, v);
		} else if (value instanceof ArrayLiteralExpressionNode) {
			push((ArrayLiteralExpressionNode) value, v);
		} else if (value instanceof ReferenceNode) {
			push((ReferenceNode) value, v);
		} else if (value instanceof ConstructionExpressionNode) {
			push((ConstructionExpressionNode) value, v);
		} else if (value instanceof InvocationNode) {
			push((InvocationNode) value, v);
		} else if (value instanceof UnaryOperationNode) {
			push((UnaryOperationNode) value, v);
		} else if (value instanceof NullCheckNode) {
			push((NullCheckNode) value, v);
		} else if (value instanceof OperationNode) {
			push((OperationNode) value, v);
		} else if (value == null) {
			v.visitInsn(ACONST_NULL);
		}
	}

	public void push(NumberLiteralExpressionNode value, MethodVisitor v) {
		v.visitFieldInsn(GETSTATIC, "bali/number/NumberFactory", "NUMBER_FACTORY", "Lbali/number/NumberFactory;");
		v.visitLdcInsn(value.getSerialization());
		v.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
		v.visitMethodInsn(INVOKEVIRTUAL, "bali/number/NumberFactory", "forDecimalString", "([C)Lbali/Number;");
	}

	public void push(BooleanLiteralExpressionNode value, MethodVisitor v) {
		String serialization = value.getSerialization();
		if ("true".equals(serialization)){
			push(True.TRUE, v);
		} else if ("false".equals(serialization)) {
			push(False.FALSE, v);
		}
	}

	public void push(StringLiteralExpressionNode value, MethodVisitor v) {
		String string = value.getSerialization();
		String internalName = CONVERTER.getInternalName(CharArrayString.class.getName());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		v.visitLdcInsn(string);
		v.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "([C)V");
	}

	public void push(ArrayLiteralExpressionNode value, MethodVisitor v) {
		String implName = CONVERTER.getInternalName(Array.class.getName());
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

	public void push(ReferenceNode referenceNode, MethodVisitor v) {

		ExpressionNode target = referenceNode.getTarget();
		String name = referenceNode.getName();
		Site referenceType = referenceNode.getType();
		switch (referenceNode.getScope()) {
			case STATIC: {
				v.visitFieldInsn(GETSTATIC, CONVERTER.getInternalName(referenceNode.getHostClass()), name, CONVERTER.getTypeDescriptor(referenceType));
			}
			break;
			case FIELD: {
				if (target != null){
					push(target, v);
				} else {
					v.visitVarInsn(ALOAD, 0);
				}
				v.visitFieldInsn(GETFIELD, CONVERTER.getInternalName(referenceNode.getHostClass()), name, CONVERTER.getTypeDescriptor(referenceType));
			}
			break;
			case VARIABLE: {
				v.visitVarInsn(ALOAD, getIndex(referenceNode.getId()));
			}
			break;
		}
	}

	public void push(ConstructionExpressionNode value, MethodVisitor v) {

		Site targetType = value.getType();
		Class targetClass = targetType.getTemplate();
		String internalName = CONVERTER.getInternalName(targetClass);
		List<Site> parameterTypes = new ArrayList<>();
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		if (targetClass.getMetaType().isReified()){
			for (Type type : targetType.getTypeArguments()) {
				push(type, v);
				parameterTypes.add(TYPE_SITE);
			}
		}
		collectParameters(targetClass, parameterTypes);
		for (ExpressionNode argumentValue : value.getResolvedArguments()) {
			push(argumentValue, v);
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", CONVERTER.getMethodDescriptor(null, parameterTypes));
	}

	public void push(Type type, MethodVisitor v) {
		v.visitLdcInsn(type.toString());
		v.visitMethodInsn(INVOKESTATIC, "bali/type/TypeFactory", "getType", "(Ljava/lang/String;)Lbali/type/Type;");
	}

	private void collectParameters(Class clazz, List<Site> parameterTypes){
		for (Type superType : clazz.getSuperTypes()){
			collectParameters(superType.getTemplate(), parameterTypes);
		}
		for (Declaration<Site> parameter : clazz.getParameters()){
			parameterTypes.add(parameter.getType());
		}
	}

	public void push(InvocationNode value, MethodVisitor v) {
		pushInvocation(
				value.getTarget(),
				value.getTargetType(),
				value.getType(),
				value.getResolvedMethod().getParameters(),
				value.getResolvedArguments(),
				value.getMethodName(),
				v
		);
	}

	public void push(UnaryOperationNode value, MethodVisitor v) {
		pushInvocation(
			value.getTarget(),
			value.getTarget().getType(),
			value.getType(),
			Collections.<Declaration<Site>>emptyList(),
			Collections.<ExpressionNode>emptyList(),
			value.getResolvedOperator().getMethodName(),
			v
		);
	}

	public void push(NullCheckNode value, MethodVisitor v) {
		push(value.getTarget(), v);
		Label isNull = new Label();
		Label end = new Label();
		v.visitJumpInsn(IFNULL, isNull);
		push(True.TRUE, v);
		v.visitJumpInsn(GOTO, end);
		v.visitLabel(isNull);
		push(False.FALSE, v);
		v.visitLabel(end);
	}

	public void push(OperationNode value, MethodVisitor v) {
		String operator = value.getOperator();
		if (Boolean.class.getName().equals(value.getOne().getType().getTemplate().getName())){
			if (bali.Boolean.AND.equals(operator)){
				pushLogicalShortCut(value.getOne(), value.getTwo(), False.FALSE, v);
				return;
			}
			if (bali.Boolean.OR.equals(operator)){
				pushLogicalShortCut(value.getOne(), value.getTwo(), True.TRUE, v);
				return;
			}
		}
		pushInvocation(
				value.getOne(),
				value.getOne().getType(),
				value.getType(),
				Collections.singletonList(new Declaration<>(null, value.getResolvedOperator().getParameter())),
				Collections.singletonList(value.getTwo()),
				value.getResolvedOperator().getMethodName(),
				v
		);
	}

	public void pushLogicalShortCut(ExpressionNode target, ExpressionNode argument, Boolean returnIf, MethodVisitor v ) {
		Label end = new Label();
		push(target, v);
		v.visitInsn(DUP);
		push(returnIf, v);
		v.visitJumpInsn(IF_ACMPEQ, end);
		v.visitInsn(POP);
		push(argument, v);
		v.visitLabel(end);
	}

	public void pushInvocation(ExpressionNode target, Site targetType, Site valueType, List<Declaration<Site>> parameters, List<ExpressionNode> arguments, String methodName, MethodVisitor v) {
		if (target != null){
			push(target, v);
		} else {
			v.visitVarInsn(ALOAD, 0);
		}

		List<Type> parameterSites = new ArrayList<>();
		Iterator<ExpressionNode> i = arguments.iterator();
		for (Declaration<Site> parameter : parameters){
			Site parameterType = parameter.getType();
			parameterSites.add(parameterType);
			ExpressionNode argument = i.next();
			push(argument, v);
		}

		v.visitMethodInsn(invokeInsn(targetType.getTemplate()),
				CONVERTER.getInternalName(targetType.getTemplate()),
				methodName,
				CONVERTER.getMethodDescriptor(valueType, parameterSites));
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

	private void push(Boolean b, MethodVisitor v){
		if (convert(b)){
			v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
		} else if (b == False.FALSE){
			v.visitFieldInsn(GETSTATIC, "bali/Boolean", "FALSE", "Lbali/Boolean;");
		} else {
			throw new RuntimeException("Cannot push boolean value " + b);
		}
	}

}
