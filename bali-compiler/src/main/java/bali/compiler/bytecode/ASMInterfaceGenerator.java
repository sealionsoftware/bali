package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.InterfaceDeclaration;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.TypeReference;
import org.objectweb.asm.ClassWriter;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMInterfaceGenerator implements Generator<InterfaceDeclaration, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(InterfaceDeclaration input) throws Exception {

		String[] extensions = new String[input.getSuperInterfaces().size()];
		int i = 0;
		for (TypeReference superInterface : input.getSuperInterfaces()) {
			extensions[i++] = converter.getInternalName(superInterface.getDeclaration().getClassName());
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
				converter.getInternalName(input.getQualifiedClassName()),
				null,
				"java/lang/Object",
				extensions);

		cw.visitEnd();

		for (Method method : input.getMethods()) {
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
