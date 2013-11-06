package bali.compiler.bytecode;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMBeanGenerator implements Generator<BeanNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(BeanNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		Type superType = input.getSuperType() != null ? input.getSuperType().getSite().getType() : null;
		String hostClassInternalName = converter.getInternalName(input.getQualifiedClassName());
		String superClassInternalName = superType != null ? converter.getInternalName(superType) : "java/lang/Object";

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER,
				hostClassInternalName,
				null,
				superClassInternalName,
				null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(MetaTypes.class.getName()), MetaTypes.BEAN.name());
		av.visitEnd();

		buildConstructor(cw, superClassInternalName);

		for (PropertyNode property : input.getProperties()) {
			buildProperty(property, cw, hostClassInternalName);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(ClassWriter cw, String superClassInternalName) {

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, Collections.<Type>emptyList()),
				null,
				null
		);

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitInsn(DUP);
		initv.visitMethodInsn(INVOKESPECIAL, superClassInternalName, "<init>", "()V");
		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private void buildProperty(PropertyNode property, ClassWriter cw, String hostClassInternalName) {

		String propertyName = property.getName();
		Type type = property.getType().getSite().getType();
		String typeDesc = converter.getTypeDescriptor(type);

		cw.visitField(ACC_PRIVATE,
				propertyName,
				converter.getTypeDescriptor(property.getType()),
				null,
				null
		).visitEnd();

		String propertyStem = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);

		MethodVisitor getterVisitor = cw.visitMethod(ACC_PUBLIC + ACC_FINAL,
				"get" + propertyStem,
				converter.getMethodDescriptor(type, Collections.<Type>emptyList()),
				null,
				null
		);

		getterVisitor.visitCode();
		getterVisitor.visitVarInsn(ALOAD, 0);
		getterVisitor.visitFieldInsn(GETFIELD, hostClassInternalName, propertyName, converter.getTypeDescriptor(type));
		getterVisitor.visitInsn(ARETURN);

		getterVisitor.visitMaxs(1, 1);
		getterVisitor.visitEnd();

		MethodVisitor setterVisitor = cw.visitMethod(ACC_PUBLIC + ACC_FINAL,
				"set" + propertyStem,
				converter.getMethodDescriptor(null, Collections.singletonList(type)),
				null,
				null
		);

		setterVisitor.visitCode();

		Label start = new Label();
		Label end = new Label();

		setterVisitor.visitLabel(start);
		setterVisitor.visitVarInsn(ALOAD, 0);
		setterVisitor.visitVarInsn(ALOAD, 1);
		setterVisitor.visitFieldInsn(PUTFIELD, hostClassInternalName, propertyName, typeDesc);
		setterVisitor.visitInsn(RETURN);
		setterVisitor.visitLabel(end);
		setterVisitor.visitLocalVariable(propertyName, typeDesc, null, start, end, 1);
		setterVisitor.visitMaxs(2, 2);
		setterVisitor.visitEnd();

	}

}
