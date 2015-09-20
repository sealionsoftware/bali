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

import java.util.List;

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
        buildMethod(cw, fragment);

        cw.visitEnd();

        GeneratedPackage generatedPackage = new GeneratedPackage("");
        generatedPackage.addClass(new GeneratedClass(Interpreter.FRAGMENT_CLASS_NAME, cw.toByteArray()));
        return generatedPackage;
    }

    private void buildConstructor(ClassWriter cw) {

        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        Label constructorStart = new Label();
        methodVisitor.visitLabel(constructorStart);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        Label constructorEnd = new Label();
        methodVisitor.visitLabel(constructorEnd);
        methodVisitor.visitLocalVariable("this", "LFragment;", null, constructorStart, constructorEnd, 0);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    private void buildMethod(ClassWriter cw, CodeBlockNode fragment) {

        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "execute", "()Ljava/util/Map;", "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;", null);
        methodVisitor.visitCode();
        Label startLabel = new Label();
        Label endLabel = new Label();
        methodVisitor.visitLabel(startLabel);

        ASMStackVisitor visitor = new ASMStackVisitor(methodVisitor);
        fragment.accept(visitor);

        List<VariableInfo> variables = visitor.getVariables();
        int retIndex = variables.size() + 1;

        methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
        methodVisitor.visitVarInsn(ASTORE, retIndex);
        Label retStart = new Label();
        methodVisitor.visitLabel(retStart);

        int i = 1;
        for (VariableInfo variable : variables){
            methodVisitor.visitVarInsn(ALOAD, retIndex);
            methodVisitor.visitLdcInsn(variable.getName());
            methodVisitor.visitVarInsn(ALOAD, i++);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            methodVisitor.visitInsn(POP);
        }

        methodVisitor.visitVarInsn(ALOAD, retIndex);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitLabel(endLabel);

        i = 0;
        methodVisitor.visitLocalVariable("this", "LFragment;", null, startLabel, endLabel, i++);
        for (VariableInfo variable: variables){
            Label to = variable.getEnd();
            if (to == null){
                to = endLabel;
            }
            methodVisitor.visitLocalVariable(variable.getName(), "Ljava/lang/Object;", null, variable.getStart(), to, i++);
        }
        methodVisitor.visitLocalVariable("ret", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;", retStart, endLabel, retIndex);

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }


}
