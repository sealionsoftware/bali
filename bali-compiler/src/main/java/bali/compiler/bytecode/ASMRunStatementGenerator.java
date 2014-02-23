package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.RunStatementNode;
import bali.compiler.type.Class;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMRunStatementGenerator implements Generator<RunStatementNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(RunStatementNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		String runnableClassName = input.getRunnableClassName();

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
				converter.getInternalName(runnableClassName),
				null,
				"java/lang/Object",
				new String[]{converter.getInternalName(Runnable.class.getName())});

		cw.visitSource(input.getSourceFileName(), null);

		for (RunStatementNode.RunArgument argument : input.getArguments()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					argument.getName(),
					converter.getTypeDescriptor(argument.getType().getTemplate()),
					null,
					null
			).visitEnd();
		}

		buildConstructor(cw, input);
		buildMethod(cw, input);

		cw.visitEnd();

		String className = runnableClassName.substring(runnableClassName.lastIndexOf('.') + 1);

		return new GeneratedClass(className, cw.toByteArray());
	}

	private void buildConstructor(ClassWriter cw, RunStatementNode input) {

		List<Class> parameterClasses = getParameterTypes(input.getArguments());

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, parameterClasses),
				null,
				null
		);

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitInsn(DUP);
		initv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

		int i = 1;
		for (RunStatementNode.RunArgument declaration : input.getArguments()) {
			initv.visitInsn(DUP);
			initv.visitVarInsn(ALOAD, i++);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getRunnableClassName()),
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType().getTemplate()));
		}

		initv.visitInsn(POP);
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod( ClassWriter cw, RunStatementNode runStatementNode) {

		ASMStackManager manager = new ASMStackManager(converter);
		int flags = ACC_PUBLIC + ACC_FINAL;

		MethodVisitor methodVisitor = cw.visitMethod(flags,
				"run",
				"()V",
				null,
				null
		);

		methodVisitor.visitCode();
		manager.execute(runStatementNode.getBody(), methodVisitor);
		methodVisitor.visitInsn(RETURN);

		for (VariableInfo variable : manager.getDeclaredVariables()) {
			methodVisitor.visitLocalVariable(
					variable.getName(),
					converter.getTypeDescriptor(variable.getType().getTemplate()),
					null,
					variable.getStart(),
					variable.getEnd(),
					variable.getIndex()
			);
		}

		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();

	}

	private List<Class> getParameterTypes(List<RunStatementNode.RunArgument> arguments) {
		List<Class> parameterClasses = new ArrayList<>();
		for (RunStatementNode.RunArgument declaration : arguments){
			parameterClasses.add(declaration.getType().getTemplate());
		}
		return parameterClasses;
	}


}
