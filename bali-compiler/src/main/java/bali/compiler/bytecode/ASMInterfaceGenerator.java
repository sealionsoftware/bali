package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.SiteNode;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMInterfaceGenerator implements Generator<InterfaceNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(InterfaceNode input) throws Exception {

		String[] extensions = new String[input.getImplementations().size()];
		int i = 0;
		for (SiteNode superInterface : input.getImplementations()) {
			extensions[i++] = converter.getInternalName(superInterface.getSite().getTemplate().getName());
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(Kind.class.getName()), Kind.INTERFACE.name());
		av.visitEnd();

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				extensions);

		cw.visitEnd();

		for (MethodNode method : input.getMethods()) {
			cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT,
					method.getName(),
					converter.getMethodDescriptor(method),
					null,
					null
			).visitEnd();
		}

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}
}
