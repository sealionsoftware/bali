package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Site;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMInterfaceGenerator implements Generator<InterfaceNode, GeneratedClass> {

	private static final ASMConverter CONVERTER = new ASMConverter();

	public GeneratedClass build(InterfaceNode input) throws Exception {

		String[] extensions = new String[input.getImplementations().size()];
		int i = 0;
		for (SiteNode superInterface : input.getImplementations()) {
			extensions[i++] = CONVERTER.getInternalName(superInterface.getSite().getTemplate().getName());
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		AnnotationVisitor av = cw.visitAnnotation(CONVERTER.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", CONVERTER.getTypeDescriptor(Kind.class.getName()), Kind.INTERFACE.name());
		av.visitEnd();

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
				CONVERTER.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				extensions);

		cw.visitEnd();

		for (MethodNode method : input.getMethods()) {
			String methodName = method.getName();

			List<Site> parameterSites = new ArrayList<>();
			for (ParameterNode declaration : method.getParameters()){
				Site parameterSite = declaration.getType().getSite();
				parameterSites.add(parameterSite);
			}
			Site returnSite = method.getType() != null ? method.getType().getSite() : null;

			//TODO Annotations
			cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT,
					methodName,
					CONVERTER.getMethodDescriptor(returnSite, parameterSites),
					CONVERTER.getMethodSignature(returnSite, parameterSites),
					null
			).visitEnd();
		}

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}
}
