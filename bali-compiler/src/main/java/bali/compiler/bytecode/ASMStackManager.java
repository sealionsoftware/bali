package bali.compiler.bytecode;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralExpression;
import bali.compiler.parser.tree.CaseStatement;
import bali.compiler.parser.tree.CatchStatement;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.ConditionalBlock;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.ConstructionExpression;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.ForStatement;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.ListLiteralExpression;
import bali.compiler.parser.tree.NumberLiteralExpression;
import bali.compiler.parser.tree.Operation;
import bali.compiler.parser.tree.Reference;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.StringLiteralExpression;
import bali.compiler.parser.tree.SwitchStatement;
import bali.compiler.parser.tree.TryStatement;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.parser.tree.UnaryOperation;
import bali.compiler.parser.tree.Variable;
import bali.compiler.parser.tree.WhileStatement;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Constant Pooling
 *
 * User: Richard
 * Date: 13/05/13
 */
public class ASMStackManager implements Opcodes {

	private static final int[] INTCODES = new int[]{ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};

	private ASMConverter converter;
	private Map<String, VariableInfo> declaredVariables = new HashMap<>();
	private Deque<Label> scopeHorizonStack = new ArrayDeque<>();

	private Type erasedType;

	public ASMStackManager(ASMConverter converter) {
		this.converter = converter;
		this.erasedType = new Type();
		TypeDeclaration t = new bali.compiler.parser.tree.Class();
		t.setQualifiedClassName(Object.class.getName());
		erasedType.setDeclaration(t);
	}

	public List<VariableInfo> getDeclaredVariables() {
		return new ArrayList<>(declaredVariables.values());
	}

	// Execute Methods

	public void execute(CodeBlock codeBlock, MethodVisitor v) {
		scopeHorizonStack.push(new Label());
		for (Statement statement : codeBlock.getStatements()) {
			execute(statement, v);
		}
		v.visitLabel(scopeHorizonStack.pop());
	}

	public void execute(Statement statement, MethodVisitor v) {

		Label label = new Label();
		v.visitLabel(label);
		Integer lineNumber = statement.getLine();
		if (lineNumber != null) {
			v.visitLineNumber(statement.getLine(), label);
		}

		if (statement instanceof Return) {
			execute((Return) statement, v);
		} else if (statement instanceof Expression) {
			execute((Expression) statement, v);
		} else if (statement instanceof Variable) {
			execute((Variable) statement, v);
		} else if (statement instanceof Assignment) {
			execute((Assignment) statement, v);
		} else if (statement instanceof ConditionalStatement) {
			execute((ConditionalStatement) statement, v);
		} else if (statement instanceof WhileStatement) {
			execute((WhileStatement) statement, v);
		} else if (statement instanceof ForStatement) {
			execute((ForStatement) statement, v);
		} else if (statement instanceof SwitchStatement) {
			execute((SwitchStatement) statement, v);
		}  else if (statement instanceof TryStatement) {
			execute((TryStatement) statement, v);
		} else {
			throw new RuntimeException("Cannot handle Statement type: " + statement);
		}
	}

	private void execute(Return statement, MethodVisitor v) {
		Expression value = statement.getValue();
		if (value != null) {
			push(value, v);
			v.visitInsn(ARETURN);
		} else {
			v.visitInsn(RETURN);
		}
	}

	private void execute(Expression statement, MethodVisitor v) {
		push(statement, v);
		if (statement.getType() != null){
			v.visitInsn(POP);
		}
	}

	private void execute(Variable variable, MethodVisitor v) {
		Expression value = variable.getValue();
		if (value != null){
			push(value, v);
		} else {
			v.visitInsn(ACONST_NULL);
		}

		addToVariables(variable.getDeclaration(), scopeHorizonStack.peek(), v);
	}

	private void execute(Assignment statement, MethodVisitor v) {
		Integer index = declaredVariables.get(statement.getReference().getName()).getIndex();
		push(statement.getValue(), v);
		v.visitVarInsn(ASTORE, index);
	}

	private void execute(ConditionalStatement statement, MethodVisitor v) {

		Label end = new Label();
		Label next = new Label();
		for (ConditionalBlock block : statement.getConditionalBlocks()) {
			push(block.getCondition(), v);
			v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
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

	private void execute(WhileStatement statement, MethodVisitor v) {
		Label end = new Label();
		Label start = new Label();
		v.visitLabel(start);
		push(statement.getCondition(), v);
		v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
		v.visitJumpInsn(IF_ACMPNE, end);
		execute(statement.getBody(), v);
		v.visitJumpInsn(GOTO, start);
		v.visitLabel(end);
	}

	private void execute(ForStatement statement, MethodVisitor v) {
		Label top = new Label();
		Label start = new Label();
		Label end = new Label();
		v.visitLabel(top);
		push(statement.getCollection(), v);
		Type collectionType = statement.getCollection().getType();
		v.visitMethodInsn(invokeInsn(collectionType), converter.getInternalName(collectionType), "iterator", "()Lbali/Iterator;");
		v.visitLabel(start);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "hasNext", "()Lbali/Boolean;");
		v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
		v.visitJumpInsn(IF_ACMPNE, end);
		v.visitInsn(DUP);
		v.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "next", "()Ljava/lang/Object;"); // TODO: INVOKE VIRTUAL ON REAL TYPE?
		Declaration element = statement.getElement();
		Type variableType = element.getType();
		v.visitTypeInsn(CHECKCAST, converter.getInternalName(variableType.getDeclaration().getQualifiedClassName()));
		addToVariables(element, end, v);
		execute(statement.getBody(), v);
		v.visitJumpInsn(GOTO, start);
		v.visitLabel(end);
		v.visitInsn(POP);
	}

	//TODO: Add support for constant switch values
	public void execute(SwitchStatement statement, MethodVisitor v) {

		Label end = new Label();
		Label next = new Label();
		push(statement.getValue(), v);
		Type statementType = statement.getValue().getType();
		for (CaseStatement caseStatement : statement.getCaseStatements()){
			v.visitInsn(DUP);
			push(caseStatement.getCondition(), v);
			v.visitMethodInsn(invokeInsn(statementType), converter.getInternalName(statementType), "equalTo", "(Ljava/lang/Object;)Lbali/Boolean;");
			v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
			v.visitJumpInsn(IF_ACMPNE, next);
			execute(caseStatement.getBody(), v);
			v.visitJumpInsn(GOTO, end);
			v.visitLabel(next);
			next = new Label();
		}
		if (statement.getDefaultStatement() != null){
			execute(statement.getDefaultStatement(), v);
		}
		v.visitLabel(end);
		v.visitInsn(POP);
	}

	public void execute(TryStatement statement, MethodVisitor v) {
		Label start = new Label();
		Label end = new Label();
		Map<Label, CatchStatement> markers = new LinkedHashMap<>();
		for (CatchStatement catchStatement : statement.getCatchStatements()){
			Label catchStart = new Label();
			markers.put(catchStart, catchStatement);
			v.visitTryCatchBlock(start, catchStart, catchStart, converter.getInternalName(catchStatement.getDeclaration().getType()));
		}
		v.visitLabel(start);
		execute(statement.getMain(), v);
		v.visitJumpInsn(GOTO, end);

		for (Map.Entry<Label, CatchStatement> entry : markers.entrySet()){
			Label catchEnd = new Label();
			v.visitLabel(entry.getKey());
			addToVariables(entry.getValue().getDeclaration(), catchEnd, v);
			execute(entry.getValue().getCodeBlock(), v);
			v.visitLabel(catchEnd);
			v.visitJumpInsn(GOTO, end);
		}

		v.visitLabel(end);
	}

	private void addToVariables(Declaration declaration, Label end, MethodVisitor v){
		String variableName = declaration.getName();
		Integer variableIndex = declaredVariables.size() + 1;
		Label start = new Label();
		declaredVariables.put(
				variableName,
				new VariableInfo(
						declaration,
						start,
						end,
						variableIndex
				)
		);
		v.visitLabel(start);
		v.visitVarInsn(ASTORE, variableIndex);
	}

	private int invokeInsn(Type t){
		return t.getDeclaration().getAbstract() ? INVOKEINTERFACE : INVOKEVIRTUAL;
	}

	// Push Methods

	public void push(Expression value, MethodVisitor v) {
		if (value instanceof NumberLiteralExpression) {
			push((NumberLiteralExpression) value, v);
		} else if (value instanceof BooleanLiteralExpression) {
			push((BooleanLiteralExpression) value, v);
		} else if (value instanceof StringLiteralExpression) {
			push((StringLiteralExpression) value, v);
		} else if (value instanceof ListLiteralExpression) {
			push((ListLiteralExpression) value, v);
		} else if (value instanceof Reference) {
			push((Reference) value, v);
		} else if (value instanceof ConstructionExpression) {
			push((ConstructionExpression) value, v);
		} else if (value instanceof Invocation) {
			push((Invocation) value, v);
		} else if (value instanceof UnaryOperation) {
			push((UnaryOperation) value, v);
		} else if (value instanceof Operation) {
			push((Operation ) value, v);
		} else {
			throw new RuntimeException("Cannot push value of type " + value + " onto the stack");
		}
	}

	public void push(NumberLiteralExpression value, MethodVisitor v) {
		v.visitFieldInsn(GETSTATIC, "bali/_", "NUMBER_FACTORY", "Lbali/NumberFactory;");
		push(value.getSerialization().toCharArray(), v);
		v.visitMethodInsn(INVOKEVIRTUAL, "bali/NumberFactory", "forDecimalString", "([C)Lbali/Number;");
	}

	public void push(BooleanLiteralExpression value, MethodVisitor v) {
		Boolean bool = Boolean.valueOf(value.getSerialization());
		v.visitFieldInsn(GETSTATIC, converter.getInternalName(value.getType()), bool ? "TRUE" : "FALSE", converter.getTypeDescriptor(value.getType()));
	}

	public void push(StringLiteralExpression value, MethodVisitor v) {
		String string = value.getSerialization();
		String internalName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		push(string.toCharArray(), v);
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "([C)V");
	}

	public void push(char[] value, MethodVisitor v) {
		push(value.length, v);
		v.visitIntInsn(NEWARRAY, T_CHAR);
		for (int i = 0; i < value.length; i++) {
			v.visitInsn(DUP);
			push(i, v);
			push(value[i], v);
			v.visitInsn(CASTORE);
		}
	}

	public void push(ListLiteralExpression value, MethodVisitor v) {
		String implName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, implName);
		v.visitInsn(DUP);
		push(value.getValues().size(), v);
		v.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		int i = 0;
		for (Expression elementValue : value.getValues()) {
			v.visitInsn(DUP);
			push(i++, v);
			push(elementValue, v);
			v.visitInsn(AASTORE);
		}
		v.visitMethodInsn(INVOKESPECIAL, implName, "<init>", "([Ljava/lang/Object;)V");
	}

	public void push(Reference value, MethodVisitor v) {
		switch (value.getScope()) {
			case STATIC: {
				v.visitFieldInsn(GETSTATIC, converter.getInternalName(value.getHostClass()), value.getName(), converter.getTypeDescriptor(value.getType()));
			}
			break;
			case FIELD: {
				v.visitVarInsn(ALOAD, 0);
				v.visitFieldInsn(GETFIELD, converter.getInternalName(value.getHostClass()), value.getName(), converter.getTypeDescriptor(value.getType()));
			}
			break;
			case VARIABLE: {
				v.visitVarInsn(ALOAD, declaredVariables.get(value.getName()).getIndex());
			}
			break;
		}
	}

	public void push(ConstructionExpression value, MethodVisitor v) {
		String internalName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		List<Type> argumentTypes = new ArrayList<>();
		for (Expression argumentValue : value.getArguments()) {
			push(argumentValue, v);
			argumentTypes.add(argumentValue.getType());
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", converter.getMethodDescriptor(null, argumentTypes));
	}

	public void push(Invocation value, MethodVisitor v) {
		pushInvocation(
				value.getTarget(),
				value.getType(),
				value.getArguments(),
				value.getMethod(),
				v
		);
	}

	public void push(UnaryOperation value, MethodVisitor v) {
		pushInvocation(
				value.getTarget(),
				value.getType(),
				new ArrayList<Expression>(),
				value.getMethod(),
				v
		);
	}

	public void push(Operation value, MethodVisitor v) {
		pushInvocation(
				value.getOne(),
				value.getType(),
				Collections.singletonList(value.getTwo()),
				value.getMethod(),
				v
		);
	}

	public void pushInvocation(Expression target, Type valueType, List<Expression> arguments, String methodName, MethodVisitor v) {
			push(target, v);
			List<Type> argumentClasses = new ArrayList<>();
			for (Expression argumentValue : arguments) {
				push(argumentValue, v);
				argumentClasses.add(argumentValue.getType());
			}
			Type targetType = target.getType();
			Boolean erased = valueType != null && valueType.getErase();
			v.visitMethodInsn(invokeInsn(targetType),
					converter.getInternalName(targetType),
					methodName,
					converter.getMethodDescriptor(erased ? erasedType : valueType, argumentClasses));
			if (erased) {
				v.visitTypeInsn(CHECKCAST, converter.getInternalName(valueType.getDeclaration().getQualifiedClassName()));
			}
		}

	public void push(int i, MethodVisitor v) {
		if (i >= -1 && i <= 5) {
			v.visitInsn(INTCODES[i + 1]);
		} else if (i >= -128 || i < 128) {
			v.visitIntInsn(BIPUSH, i);
		} else if (i >= -32768 || i < -32768) {
			v.visitIntInsn(SIPUSH, i);
		} else {
			throw new RuntimeException("Cannot push integer of size" + i);
		}
	}

}
