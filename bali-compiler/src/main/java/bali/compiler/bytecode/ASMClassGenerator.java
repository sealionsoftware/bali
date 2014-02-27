package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Type;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMClassGenerator implements Generator<ObjectNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(ObjectNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String[] interfaceNames = new String[input.getImplementations().size()];
		int i = 0;
		for (SiteNode iface : input.getImplementations()) {
			interfaceNames[i++] = converter.getInternalName(iface.getSite().getTemplate().getName());
		}

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				interfaceNames);

		cw.visitSource(input.getSourceFile(), null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(Kind.class.getName()), Kind.OBJECT.name());
		av.visitEnd();

		for (FieldNode field : input.getFields()) {
			cw.visitField(ACC_PRIVATE,
					field.getName(),
					converter.getTypeDescriptor(field.getType()),
					null,
					null
			).visitEnd();
		}

		for (DeclarationNode argument : input.getArgumentDeclarations()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					argument.getName(),
					converter.getTypeDescriptor(argument.getType()),
					null,
					null
			).visitEnd();
		}

		buildConstructor(input, cw);

		for (MethodDeclarationNode method : input.getMethods()) {
			buildMethod(method, cw);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(ObjectNode input, ClassWriter cw) {

		ASMStackManager manager = new ASMStackManager(converter);

		List<SiteNode> argumentTypes = new ArrayList<>();
		for (DeclarationNode argument : input.getArgumentDeclarations()) {
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
		for (DeclarationNode declaration : input.getArgumentDeclarations()) {
			initv.visitInsn(DUP);
			initv.visitVarInsn(ALOAD, i++);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getQualifiedClassName()),
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType()));
		}

		for (FieldNode node : input.getFields()) {
			ExpressionNode value = node.getValue();
			if (value != null){
				initv.visitInsn(DUP);
				manager.push(value, initv);
				initv.visitFieldInsn(PUTFIELD,
						converter.getInternalName(input.getQualifiedClassName()),
						node.getName(),
						converter.getTypeDescriptor(node.getType().getSite().getTemplate()));
			}

		}
		initv.visitInsn(POP);
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod(MethodDeclarationNode method, ClassWriter cw) {

		ASMStackManager manager = new ASMStackManager(converter);
		int flags = (method.getDeclared() ? ACC_PUBLIC : ACC_PRIVATE);
		if (method.getFinal()) {
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

	private class InlineValue {

		private Type type;
		private ExpressionNode value;

		private InlineValue(Type type, ExpressionNode value) {
			this.type = type;
			this.value = value;
		}

		public Type getType() {
			return type;
		}

		public ExpressionNode getValue() {
			return value;
		}
	}

}
