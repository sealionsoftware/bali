package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.type.Site;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

import static bali.compiler.bytecode._.CONVERTER;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMPackageClassGenerator implements Generator<CompilationUnitNode, GeneratedClass> {

	public GeneratedClass build(CompilationUnitNode input) throws Exception {

		ASMStackManager manager = new ASMStackManager();

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String qualified = CONVERTER.getInternalName(input.getName() + "." + PACKAGE_CLASS_NAME);

		cw.visit(V1_7,
				ACC_PUBLIC,
				qualified,
				null,
				"java/lang/Object",
				null);

		//TODO Annotations
		Map<ConstantNode, ExpressionNode> constantValues = new HashMap<>();
		for (ConstantNode constant : input.getConstants()) {
			Site type = constant.getType().getSite();
			cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
					constant.getName(),
					CONVERTER.getTypeDescriptor(type),
					CONVERTER.getSignature(type),
					null
			).visitEnd();
			constantValues.put(constant, constant.getValue());
		}

		MethodVisitor clinitv = cw.visitMethod(ACC_STATIC,
				"<clinit>",
				"()V",
				null,
				null
		);

		clinitv.visitCode();
		for (Map.Entry<ConstantNode, ExpressionNode> constantValueEntry : constantValues.entrySet()) {
			ConstantNode constant = constantValueEntry.getKey();
			ExpressionNode value = constantValueEntry.getValue();
			manager.push(value, clinitv);
			clinitv.visitFieldInsn(PUTSTATIC, qualified, constant.getName(), CONVERTER.getTypeDescriptor(constant.getType().getSite()));
		}
		clinitv.visitInsn(RETURN);
		clinitv.visitMaxs(1, 1);
		clinitv.visitEnd();

		cw.visitEnd();

		return new GeneratedClass(PACKAGE_CLASS_NAME, cw.toByteArray());
	}
}
