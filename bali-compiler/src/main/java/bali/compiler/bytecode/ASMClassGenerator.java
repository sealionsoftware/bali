package bali.compiler.bytecode;

import bali.compiler.parser.tree.Class;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.Invocation;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.Value;
import bali.compiler.parser.tree.Variable;
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
		String[] interfaceNames = new String[input.getInterfaces().size()];
		int i = 0;
		for (bali.compiler.parser.tree.Type iface : input.getInterfaces()){
			interfaceNames[i++] = converter.getInternalName(iface.getQualifiedClassName());
		}

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				interfaceNames);

		Map<String, Value> values = new HashMap<>();
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

		buildConstructor(values, cw, input);

		for (Method method : input.getMethods()) {
			buildMethod(method, cw, input);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(Map<String, Value> values, ClassWriter cw, Class input){

		ASMStackManager manager = new ASMStackManager(converter);

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				"()V",
				null,
				null
		);

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

		for (Map.Entry<String, Value> valueEntry : values.entrySet()){
			initv.visitVarInsn(ALOAD, 0);
			Value value = valueEntry.getValue();
			manager.push(value, initv);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getQualifiedClassName()),
					valueEntry.getKey(),
					converter.getTypeDescriptor(value.getValueTypeName()));
		}
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod(Method method, ClassWriter cw, Class clazz){

		ASMStackManager manager = new ASMStackManager(converter);

		MethodVisitor methodVisitor = cw.visitMethod(method.getDeclared() ? ACC_PUBLIC : ACC_PRIVATE,
				method.getName(),
				converter.getMethodDescriptor(method),
				null,
				null
		);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, method.getArguments().size());

		manager.execute(method.getBody(), methodVisitor);

		for (VariableInfo variable :  manager.getDeclaredVariables()){
			Variable declaration = variable.getDeclaration();
			methodVisitor.visitLocalVariable(
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType()),
					null,
					variable.getStart(),
					variable.getEnd(),
					variable.getIndex()
			);
		}

		methodVisitor.visitMaxs(manager.getMaxDepth(), 2);
		methodVisitor.visitEnd();

	}

}
