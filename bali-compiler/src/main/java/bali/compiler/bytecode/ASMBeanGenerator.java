package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.type.Class;
import bali.compiler.type.Site;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import java.util.Collections;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMBeanGenerator implements Generator<BeanNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(BeanNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		Class superClass = input.getSuperType() != null ? input.getSuperType().getSite().getTemplate() : null;
		String hostClassInternalName = converter.getInternalName(input.getQualifiedClassName());
		String superClassInternalName = superClass != null ? converter.getInternalName(superClass) : "java/lang/Object";

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER,
				hostClassInternalName,
				null,
				superClassInternalName,
				null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(Kind.class.getName()), Kind.BEAN.name());
		av.visitEnd();

		buildConstructor(cw, superClassInternalName);

		for (PropertyNode property : input.getProperties()) {
			buildProperty(property, cw);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(ClassWriter cw, String superClassInternalName) {

		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, Collections.<Class>emptyList()),
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

	private void buildProperty(PropertyNode property, ClassWriter cw) {

		String propertyName = property.getName();
		Site site = property.getType().getSite();

		cw.visitField(ACC_PUBLIC,
				propertyName,
				converter.getTypeDescriptor(property.getType().getSite()),
				converter.getSignature(site),
				null
		).visitEnd();
	}



}
