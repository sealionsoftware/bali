package bali.compiler.bytecode;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralExpression;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.ConditionalBlock;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.ConstructionExpression;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.ListLiteralExpression;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.NumberLiteralExpression;
import bali.compiler.parser.tree.Reference;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.StringLiteralExpression;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.parser.tree.Variable;
import bali.compiler.parser.tree.WhileStatement;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
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
		} else if (statement instanceof Invocation) {
			execute((Invocation) statement, v);
		} else if (statement instanceof Variable) {
			Variable variable = (Variable) statement;
			declaredVariables.put(
					variable.getReference().getName(),
					new VariableInfo(
							variable,
							label,
							scopeHorizonStack.peek(),
							declaredVariables.size() + 1
					)
			);
			execute(variable, v);
		} else if (statement instanceof Assignment) {
			execute((Assignment) statement, v);
		} else if (statement instanceof ConditionalStatement) {
			execute((ConditionalStatement) statement, v);
		} else if (statement instanceof WhileStatement) {
			execute((WhileStatement) statement, v);
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

	private void execute(Invocation statement, MethodVisitor v) {
		push(statement.getTarget(), v);
		List<Type> argumentTypes = new ArrayList<>();
		for (Expression value : statement.getArguments()) {
			push(value, v);
			argumentTypes.add(value.getType());
		}
		v.visitMethodInsn(statement.getTarget().getType().getDeclaration().getAbstract() ? INVOKEINTERFACE : INVOKEVIRTUAL,
				converter.getInternalName(statement.getTarget().getType()),
				statement.getMethod(),
				converter.getMethodDescriptor(statement.getType(), argumentTypes));
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
		for (Expression argumentValue : value.getArguments()) {
			push(argumentValue, v);
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", converter.getMethodDescriptor(null, new ArrayList<Type>())); //TODO: actual constructor method descriptors
	}

	public void push(Invocation value, MethodVisitor v) {
		push(value.getTarget(), v);
		List<Type> argumentClasses = new ArrayList<>();
		for (Expression argumentValue : value.getArguments()) {
			push(argumentValue, v);
			argumentClasses.add(argumentValue.getType());
		}
		Type valueType = value.getType();
		Type targetType = value.getTarget().getType();
		Boolean erased = valueType.getErase();
		v.visitMethodInsn(targetType.getDeclaration().getAbstract() ? INVOKEINTERFACE : INVOKEVIRTUAL,
				converter.getInternalName(targetType),
				value.getMethod(),
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
