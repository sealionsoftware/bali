package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Parameters;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
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
				null, //TODO: signature
				"java/lang/Object",
				interfaceNames);

		cw.visitSource(input.getSourceFile(), null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(Kind.class.getName()), Kind.OBJECT.name());
		av.visitEnd();

		for (FieldNode field : input.getFields()) {
			Site type = field.getType().getSite();
			cw.visitField(ACC_PRIVATE,
					field.getName(),
					converter.getTypeDescriptor(type),
					converter.getSignature(type),
					null
			).visitEnd();
		}

		for (DeclarationNode parameter : input.getTypeParameters()) {
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					parameter.getName(),
					"Lbali/type/Type;",
					"Lbali/type/Type;",
					null
			).visitEnd();
		}

		for (DeclarationNode parameter : input.getParameters()) {
			Site type = parameter.getType().getSite();
			cw.visitField(ACC_PRIVATE + ACC_FINAL,
					parameter.getName(),
					converter.getTypeDescriptor(type),
					converter.getSignature(type),
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

		List<Site> parameterSites = new ArrayList<>();
		for (ParameterNode declaration : input.getParameters()){
			Site parameterSite = declaration.getType().getSite();
			parameterSites.add(parameterSite);
		}

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, parameterSites),
				converter.getMethodSignature(null, parameterSites),
				null
		);

		initv.visitAnnotation(converter.getTypeDescriptor(Parameters.class.getName()), true).visitEnd();

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitInsn(DUP);
		initv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

		int i = 1;
		String qualifiedClassName = converter.getInternalName(input.getQualifiedClassName());
		for (DeclarationNode declaration : input.getTypeParameters()) {
			initv.visitInsn(DUP);
			initv.visitVarInsn(ALOAD, i++);
			initv.visitFieldInsn(PUTFIELD,
					qualifiedClassName,
					declaration.getName(),
					"Lbali/type/Type;");
		}
		for (DeclarationNode declaration : input.getParameters()) {
			initv.visitInsn(DUP);
			initv.visitVarInsn(ALOAD, i++);
			initv.visitFieldInsn(PUTFIELD,
					qualifiedClassName,
					declaration.getName(),
					converter.getTypeDescriptor(declaration.getType().getSite().getTemplate()));
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
		int flags = method.getFinal() ? ACC_FINAL : 0;
		Method declared = method.getDeclared();
		String descriptor, signature;

		if (declared != null){
			flags += ACC_PUBLIC;
			descriptor = converter.getMethodDescriptor(declared);
			signature = converter.getMethodSignature(declared);
		} else {
			flags += ACC_PRIVATE;
			List<Site> parameterSites = new ArrayList<>();
			for (ParameterNode declaration : method.getParameters()){
				Site parameterSite = declaration.getType().getSite();
				parameterSites.add(parameterSite);
			}
			Site returnSite = method.getType() != null ? method.getType().getSite() : null;
			descriptor = converter.getMethodDescriptor(returnSite, parameterSites);
			signature = converter.getMethodSignature(returnSite, parameterSites);
		}

		MethodVisitor methodVisitor = cw.visitMethod(flags,
				method.getName(),
				descriptor,
				signature,
				null
		);

		methodVisitor.visitCode();
		manager.execute(method, methodVisitor);

		for (VariableInfo variable : manager.getDeclaredVariables()) {
			methodVisitor.visitLocalVariable(
					variable.getName(),
					converter.getTypeDescriptor(variable.getType()),
					converter.getSignature(variable.getType()),
					variable.getStart(),
					variable.getEnd(),
					variable.getIndex()
			);
		}

		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();

	}

}
