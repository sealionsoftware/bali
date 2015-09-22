package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ASMStackVisitor implements Visitor, Opcodes {

    private static final String BOOLEAN_NAME = internalise(bali.Boolean.class);

    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
    }

    public void visit(BooleanLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, BOOLEAN_NAME, node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(TextLiteralNode node) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    public void visit(VariableNode node) {
        Label varStart = new Label();
        methodVisitor.visitLabel(varStart);
        node.getValue().accept(this);
        variables.add(new VariableInfo(
                node.getName(),
                varStart,
                scopeHorizonStack.peek()
        ));
        methodVisitor.visitVarInsn(ASTORE, variables.size());
    }

    public void visit(CodeBlockNode node) {

    }

    public List<VariableInfo> getVariables(){
        return variables;
    }

    private static String internalise(Class clazz){
        if (clazz == null){
            clazz = Object.class;
        }
        return clazz.getName().replaceAll("\\.", "/");
    }
}
