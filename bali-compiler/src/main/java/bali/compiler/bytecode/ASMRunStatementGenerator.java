package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.RunStatementNode;
import bali.compiler.type.Class;
import bali.compiler.type.Site;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

import static bali.compiler.bytecode._.CONVERTER;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMRunStatementGenerator implements Generator<RunStatementNode, GeneratedClass> {

	public GeneratedClass build(RunStatementNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		String runnableClassName = input.getRunnableClassName();

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
				CONVERTER.getInternalName(runnableClassName),
				null,
				"java/lang/Object",
				new String[]{CONVERTER.getInternalName(Runnable.class.getName())});

		cw.visitSource(input.getSourceFileName(), null);

		for (RunStatementNode.RunArgument argument : input.getArguments()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					argument.getName(),
					CONVERTER.getTypeDescriptor(argument.getType()),
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

		List<Site> parameterTypes = getParameterTypes(input.getArguments());

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				CONVERTER.getMethodDescriptor(null, parameterTypes),
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
					CONVERTER.getInternalName(input.getRunnableClassName()),
					declaration.getName(),
					CONVERTER.getTypeDescriptor(declaration.getType()));
		}

		initv.visitInsn(POP);
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod( ClassWriter cw, RunStatementNode runStatementNode) {

		ASMStackManager manager = new ASMStackManager();
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

		int i = 1;
		for (VariableInfo variable : manager.getDeclaredVariables()) {
			methodVisitor.visitLocalVariable(
					variable.getName(),
					CONVERTER.getTypeDescriptor(variable.getType()),
					null,
					variable.getStart(),
					variable.getEnd(),
					i++
			);
		}

		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();

	}

	private List<Site> getParameterTypes(List<RunStatementNode.RunArgument> arguments) {
		List<Site> parameterTypes = new ArrayList<>();
		for (RunStatementNode.RunArgument declaration : arguments){
			parameterTypes.add(declaration.getType());
		}
		return parameterTypes;
	}


}
