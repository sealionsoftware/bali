package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.assembly.DescendingVisitor;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ASMStackVisitor extends DescendingVisitor implements Opcodes {

    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();
    private Map<UUID, Integer> variablesIndex = new HashMap<>();

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
    }

    public void visit(BooleanLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(TextLiteralNode node) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    public void visit(TypeNode node) {
        visitChildren(node);
    }

    public void visit(VariableNode node) {
        Label varStart = new Label();
        methodVisitor.visitLabel(varStart);
        visitChildren(node);
        variables.add(new VariableInfo(
                node,
                varStart,
                scopeHorizonStack.peek()
        ));
        variablesIndex.put(node.getId(), variables.size());
        methodVisitor.visitVarInsn(ASTORE, variables.size());
    }

    public void visit(CodeBlockNode node) {
        visitChildren(node);
    }

    public void visit(AssignmentNode node) {
        node.getValue().accept(this);
        methodVisitor.visitVarInsn(ASTORE, variablesIndex.get(node.getTarget().getVariableData().id));
    }

    public void visit(ReferenceNode node){

    }

    public void visit(ConditionalStatementNode node) {
        Label end = new Label();
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
        node.getCondition().accept(this);
        methodVisitor.visitJumpInsn(IF_ACMPNE, end);
        node.getConditional().accept(this);
        methodVisitor.visitLabel(end);
    }

    public List<VariableInfo> getVariables(){
        return variables;
    }

}
