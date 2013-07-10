package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMClassGenerator implements Generator<Class, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(Class input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String[] interfaceNames = new String[input.getImplementations().size()];
		int i = 0;
		for (bali.compiler.parser.tree.Type iface : input.getImplementations()) {
			interfaceNames[i++] = converter.getInternalName(iface.getDeclaration().getQualifiedClassName());
		}

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				interfaceNames);

		cw.visitSource(input.getSourceFile(), null);

		Map<String, Expression> values = new HashMap<>();
		for (Field field : input.getFields()) {
			cw.visitField(ACC_PRIVATE,
					field.getName(),
					converter.getTypeDescriptor(field.getType()),
					null,
					null
			).visitEnd();
			if (field.getValue() != null) {
				values.put(field.getName(), field.getValue());
			}
		}

		for (Declaration argument : input.getArguments()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					argument.getName(),
					converter.getTypeDescriptor(argument.getType()),
					null,
					null
			).visitEnd();
		}

		buildConstructor(values, cw, input);

		for (Method method : input.getMethods()) {
			buildMethod(method, cw);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(Map<String, Expression> values, ClassWriter cw, Class input) {

		ASMStackManager manager = new ASMStackManager(converter);

		List<Type> argumentTypes = new ArrayList<>();
		for (Declaration argument : input.getArguments()){
			argumentTypes.add(argument.getType());
		}

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, argumentTypes),
				null,
				null
		);

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitInsn(DUP);
		initv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

		int i = 1;
		for (Declaration declaration : input.getArguments()){
			initv.visitInsn(DUP);
			initv.visitVarInsn(ALOAD, i++);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getQualifiedClassName()),
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType()));
		}

		for (Map.Entry<String, Expression> valueEntry : values.entrySet()) {
			initv.visitInsn(DUP);
			Expression value = valueEntry.getValue();
			manager.push(value, initv);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getQualifiedClassName()),
					valueEntry.getKey(),
					converter.getTypeDescriptor(value.getType()));
		}
		initv.visitInsn(POP);
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod(Method method, ClassWriter cw) {

		ASMStackManager manager = new ASMStackManager(converter);
		int flags = (method.getDeclared() ? ACC_PUBLIC : ACC_PRIVATE);
		if (method.getFinal()){
			flags += ACC_FINAL;
		}

		MethodVisitor methodVisitor = cw.visitMethod(flags,
				method.getName(),
				converter.getMethodDescriptor(method),
				null,
				null
		);

		methodVisitor.visitCode();
		manager.execute(method, methodVisitor);

		for (VariableInfo variable : manager.getDeclaredVariables()) {
			Declaration declaration = variable.getDeclaration();
			methodVisitor.visitLocalVariable(
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType()),
					null,
					variable.getStart(),
					variable.getEnd(),
					variable.getIndex()
			);
		}

		methodVisitor.visitMaxs(10, 10);
		methodVisitor.visitEnd();

	}

}
