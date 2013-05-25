package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.Value;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMPackageClassGenerator implements Generator<CompilationUnit, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();

	public GeneratedClass build(CompilationUnit input) throws Exception {

		ASMStackManager manager = new ASMStackManager(converter);

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String qualified = converter.getInternalName(input.getName() + "." + PACKAGE_CLASS_NAME);

		cw.visit(V1_7,
				ACC_PUBLIC,
				qualified,
				null,
				"java/lang/Object",
				null);

		Map<String, Value> constantValues = new HashMap<>();
		for (Constant constant : input.getConstants()) {
			cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
					constant.getName(),
					converter.getTypeDescriptor(constant.getType()),
					null,
					null
			).visitEnd();
			constantValues.put(constant.getName(), constant.getValue());
		}

		MethodVisitor clinitv = cw.visitMethod(ACC_STATIC,
				"<clinit>",
				"()V",
				null,
				null
		);

		clinitv.visitCode();
		for (Map.Entry<String, Value> constantValueEntry : constantValues.entrySet()) {
			Value value = constantValueEntry.getValue();
			manager.push(value, clinitv);
			clinitv.visitFieldInsn(PUTSTATIC, qualified, constantValueEntry.getKey(), converter.getTypeDescriptor(value.getType()));
		}
		clinitv.visitInsn(RETURN);
		clinitv.visitMaxs(1, 1);
		clinitv.visitEnd();

		cw.visitEnd();

		return new GeneratedClass(PACKAGE_CLASS_NAME, cw.toByteArray());
	}
}
