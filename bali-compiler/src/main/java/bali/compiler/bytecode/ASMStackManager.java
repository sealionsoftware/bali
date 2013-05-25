package bali.compiler.bytecode;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.BooleanLiteralValue;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.ConditionalBlock;
import bali.compiler.parser.tree.ConditionalStatement;
import bali.compiler.parser.tree.ConstructionValue;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.ListLiteralValue;
import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.ReferenceValue;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.StringLiteralValue;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.Value;
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
 * User: Richard
 * Date: 13/05/13
 */
public class ASMStackManager implements Opcodes {

	private static final int[] INTCODES = new int[]{ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};

	private ASMConverter converter;
	private Map<String, VariableInfo> declaredVariables = new HashMap<>();
	private Deque<Label> scopeHorizonStack = new ArrayDeque<>();

	public ASMStackManager(ASMConverter converter) {
		this.converter = converter;
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
					variable.getName(),
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
		Value value = statement.getValue();
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
		for (Value value : statement.getArguments()) {
			push(value, v);
			argumentTypes.add(value.getType());
		}
		v.visitMethodInsn(INVOKEVIRTUAL,
				converter.getInternalName(statement.getTarget().getType()),
				statement.getMethod(),
				converter.getMethodDescriptor(statement.getType(), argumentTypes));
	}

	private void execute(Assignment statement, MethodVisitor v) {
		Integer index = declaredVariables.get(statement.getName()).getIndex();
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
		push(statement.getCondition(), v);
		v.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
		v.visitJumpInsn(IF_ACMPNE, end);
		execute(statement.getBody(), v);
		v.visitLabel(end);
	}

	// Push Methods

	public void push(Value value, MethodVisitor v) {
		if (value instanceof NumberLiteralValue) {
			push((NumberLiteralValue) value, v);
		} else if (value instanceof BooleanLiteralValue) {
			push((BooleanLiteralValue) value, v);
		} else if (value instanceof StringLiteralValue) {
			push((StringLiteralValue) value, v);
		} else if (value instanceof ListLiteralValue) {
			push((ListLiteralValue) value, v);
		} else if (value instanceof ReferenceValue) {
			push((ReferenceValue) value, v);
		} else if (value instanceof ConstructionValue) {
			push((ConstructionValue) value, v);
		} else if (value instanceof Invocation) {
			push((Invocation) value, v);
		} else {
			throw new RuntimeException("Cannot push value of type " + value + " onto the stack");
		}
	}

	public void push(NumberLiteralValue value, MethodVisitor v) {
		Integer number = Integer.parseInt(value.getSerialization()); //TODO need to parse directly from java.lang.String to bali.Number via byte[]
		String internalName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		push(number, v);
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "(I)V");
	}

	public void push(BooleanLiteralValue value, MethodVisitor v) {
		Boolean bool = Boolean.valueOf(value.getSerialization());
		v.visitFieldInsn(GETSTATIC, converter.getInternalName(value.getType()), bool ? "TRUE" : "FALSE", converter.getTypeDescriptor(value.getType()));
	}

	public void push(StringLiteralValue value, MethodVisitor v) {
		String string = value.getSerialization();
		String internalName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		push(string.length(), v);
		v.visitIntInsn(NEWARRAY, T_CHAR);
		char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			v.visitInsn(DUP);
			push(i, v);
			push(chars[i], v);
			v.visitInsn(CASTORE);
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "([C)V");
	}

	public void push(ListLiteralValue value, MethodVisitor v) {
		String implName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, implName);
		v.visitInsn(DUP);
		push(value.getValues().size(), v);
		v.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		int i = 0;
		for (Value elementValue : value.getValues()) {
			v.visitInsn(DUP);
			push(i++, v);
			push(elementValue, v);
			v.visitInsn(AASTORE);
		}
		v.visitMethodInsn(INVOKESPECIAL, implName, "<init>", "([Ljava/lang/Object;)V");
	}

	public void push(ReferenceValue value, MethodVisitor v) {
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

	public void push(ConstructionValue value, MethodVisitor v) {
		String internalName = converter.getInternalName(value.getType());
		v.visitTypeInsn(NEW, internalName);
		v.visitInsn(DUP);
		for (Value argumentValue : value.getArguments()) {
			push(argumentValue, v);
		}
		v.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", converter.getMethodDescriptor(null, new ArrayList<Type>())); //TODO: actual constructor method descriptors
	}

	public void push(Invocation value, MethodVisitor v) {
		push(value.getTarget(), v);
		List<Type> argumentClasses = new ArrayList<>();
		for (Value argumentValue : value.getArguments()) {
			push(argumentValue, v);
			argumentClasses.add(argumentValue.getType());
		}
		v.visitMethodInsn(INVOKEVIRTUAL,
				converter.getInternalName(value.getTarget().getType()),
				value.getMethod(),
				converter.getMethodDescriptor(value.getType(), argumentClasses));
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
