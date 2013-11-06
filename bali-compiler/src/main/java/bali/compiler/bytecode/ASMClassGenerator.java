package bali.compiler.bytecode;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.TypeLibrary;
import org.objectweb.asm.AnnotationVisitor;
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
public class ASMClassGenerator implements Generator<ClassNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();
	private TypeLibrary library;

	public ASMClassGenerator(TypeLibrary library) {
		this.library = library;
	}

	public GeneratedClass build(ClassNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String[] interfaceNames = new String[input.getImplementations().size()];
		int i = 0;
		for (SiteNode iface : input.getImplementations()) {
			interfaceNames[i++] = converter.getInternalName(iface.getSite().getName());
		}

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				interfaceNames);

		cw.visitSource(input.getSourceFile(), null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(MetaTypes.class.getName()), MetaTypes.CLASS.name());
		av.visitEnd();

		Map<String, ExpressionNode> values = new HashMap<>();
		for (FieldNode field : input.getFields()) {
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

		for (DeclarationNode argument : input.getArgumentDeclarations()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					argument.getName(),
					converter.getTypeDescriptor(argument.getType()),
					null,
					null
			).visitEnd();
		}

		buildConstructor(values, cw, input);

		for (MethodDeclarationNode method : input.getMethods()) {
			buildMethod(method, cw);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(Map<String, ExpressionNode> values, ClassWriter cw, ClassNode input) {

		ASMStackManager manager = new ASMStackManager(converter, library);

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

		for (Map.Entry<String, ExpressionNode> valueEntry : values.entrySet()) {
			initv.visitInsn(DUP);
			ExpressionNode value = valueEntry.getValue();
			manager.push(value, initv);
			initv.visitFieldInsn(PUTFIELD,
					converter.getInternalName(input.getQualifiedClassName()),
					valueEntry.getKey(),
					converter.getTypeDescriptor(value.getType().getType()));
		}
		initv.visitInsn(POP);
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildMethod(MethodDeclarationNode method, ClassWriter cw) {

		ASMStackManager manager = new ASMStackManager(converter, library);
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
			DeclarationNode declaration = variable.getDeclaration();
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
