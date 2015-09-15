package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.BytecodeEngine;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMBytecodeEngine implements BytecodeEngine, Opcodes {

    public GeneratedPackage generate(CodeBlockNode fragment) {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_8,
                ACC_PUBLIC + ACC_SUPER + ACC_FINAL,
                Interpreter.FRAGMENT_CLASS_NAME,
                null,
                "java/lang/Object",
                new String[]{"com/sealionsoftware/bali/compiler/Fragment"});

        buildConstructor(cw);
        buildMethod(cw);

        cw.visitEnd();

        GeneratedPackage generatedPackage = new GeneratedPackage("");
        generatedPackage.addClass(new GeneratedClass(Interpreter.FRAGMENT_CLASS_NAME, cw.toByteArray()));
        return generatedPackage;
    }

    private void buildConstructor(ClassWriter cw) {

        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        Label l0 = new Label();
        methodVisitor.visitLabel(l0);
        methodVisitor.visitLineNumber(4, l0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        Label l1 = new Label();
        methodVisitor.visitLabel(l1);
        methodVisitor.visitLocalVariable("this", "LFragment;", null, l0, l1, 0);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    private void buildMethod(ClassWriter cw) {

        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "execute", "()Ljava/util/Map;", "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;", null);
        methodVisitor.visitCode();
        Label l0 = new Label();
        methodVisitor.visitLabel(l0);
        methodVisitor.visitLineNumber(6, l0);
        methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
        methodVisitor.visitInsn(ARETURN);
        Label l1 = new Label();
        methodVisitor.visitLabel(l1);
        methodVisitor.visitLocalVariable("this", "LFragment;", null, l0, l1, 0);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
    }


}
