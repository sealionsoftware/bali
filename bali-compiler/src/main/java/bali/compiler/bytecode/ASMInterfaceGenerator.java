package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import org.objectweb.asm.ClassWriter;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMInterfaceGenerator implements Generator<Interface, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(Interface input) throws Exception {

		String[] extensions = new String[input.getSuperInterfaces().size()];
		int i = 0;
		for (Type superInterface : input.getSuperInterfaces()) {
			extensions[i++] = converter.getInternalName(superInterface.getQualifiedClassName());
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				extensions);

		cw.visitEnd();

		for (MethodDeclaration method : input.getMethods()) {
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
