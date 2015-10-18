package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ASMStackVisitor implements Visitor, Opcodes {

    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();
    private Map<UUID, Integer> variablesIndex = new HashMap<>();

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
    }

    public void visit(BooleanLiteralNode node, Control control) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(TextLiteralNode node, Control control) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    public void visit(TypeNode node, Control control) {
        control.visitChildren();
    }

    public void visit(VariableNode node, Control control) {
        Label varStart = new Label();
        methodVisitor.visitLabel(varStart);
        control.visitChildren();
        variables.add(new VariableInfo(
                node,
                varStart,
                scopeHorizonStack.peek()
        ));
        variablesIndex.put(node.getId(), variables.size());
        methodVisitor.visitVarInsn(ASTORE, variables.size());
    }

    public void visit(CodeBlockNode node, Control control) {
        control.visitChildren();
    }

    public void visit(AssignmentNode node, Control control) {
        control.visitChildren();
        methodVisitor.visitVarInsn(ASTORE, variablesIndex.get(node.getTarget().getVariableData().id));
    }

    public void visit(ReferenceNode node, Control control){

    }

    public void visit(ConditionalStatementNode node, Control control) {

    }

    public List<VariableInfo> getVariables(){
        return variables;
    }

}
