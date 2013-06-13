package example;

import bali.BigInteger;

/**
 * package asm.example;
 * import java.util.*;
 * import org.objectweb.asm.*;
 * import org.objectweb.asm.attrs.*;
 * public class CalculatorObjectDump implements Opcodes {
 * <p/>
 * public static byte[] dump () throws Exception {
 * <p/>
 * ClassWriter cw = new ClassWriter(0);
 * FieldVisitor fv;
 * MethodVisitor mv;
 * AnnotationVisitor av0;
 * <p/>
 * cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "bali/compiler/example/CalculatorObject", null, "java/lang/Object", new String[] { "bali/compiler/example/Calculator" });
 * <p/>
 * {
 * fv = cw.visitField(ACC_PUBLIC, "afield", "Lbali/Number;", null, null);
 * fv.visitEnd();
 * }
 * {
 * mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
 * mv.visitCode();
 * mv.visitVarInsn(ALOAD, 0);
 * mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
 * mv.visitInsn(RETURN);
 * mv.visitMaxs(1, 1);
 * mv.visitEnd();
 * }
 * {
 * mv = cw.visitMethod(ACC_PUBLIC, "calculate", "(Lbali/Number;)Lbali/Number;", null, null);
 * mv.visitCode();
 * mv.visitVarInsn(ALOAD, 1);
 * mv.visitFieldInsn(GETSTATIC, "bali/compiler/example/_", "NUMBER_CONSTANT", "Lbali/Number;");
 * mv.visitMethodInsn(INVOKEVIRTUAL, "bali/Number", "add", "(Lbali/Number;)Lbali/Number;");
 * mv.visitInsn(ARETURN);
 * mv.visitMaxs(2, 2);
 * mv.visitEnd();
 * }
 * cw.visitEnd();
 * <p/>
 * return cw.toByteArray();
 * }
 * }
 * <p/>
 * User: Richard
 * Date: 07/05/13
 */
public class CalculatorObject implements Calculator {

	public BigInteger afield;

	public BigInteger calculate(Numbument) {
		return argument.add(_.NUMBER_CONSTANT);
	}

}
