package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.DescendingVisitor;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionStatementNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
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
import java.util.stream.Collectors;

import static bali.number.Primitive.convert;

public class ASMStackVisitor extends DescendingVisitor implements Opcodes {

    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();
    private Map<UUID, Integer> variablesIndex = new HashMap<>();

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
    }

    public void visit(ExpressionStatementNode node) {
        visitChildren(node);
        if (node.getExpressionNode().getType() != null){
            methodVisitor.visitInsn(POP);
        }
    }

    public void visit(BooleanLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(TextLiteralNode node) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    public void visit(IntegerLiteralNode node) {
        methodVisitor.visitLdcInsn(convert(node.getValue()));
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/number/Primitive", "convert", "(I)Lbali/Integer;", false);
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
        methodVisitor.visitVarInsn(ALOAD, variablesIndex.get(node.getVariableData().id));
    }

    public void visit(ConditionalStatementNode node) {
        StatementNode contraStatementNode = node.getContraConditional();

        if (contraStatementNode == null ){
            Label end = new Label();
            methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
            node.getCondition().accept(this);
            methodVisitor.visitJumpInsn(IF_ACMPNE, end);
            node.getConditional().accept(this);
            methodVisitor.visitLabel(end);
        } else {
            Label end = new Label();
            Label contra = new Label();

            methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
            node.getCondition().accept(this);
            methodVisitor.visitJumpInsn(IF_ACMPNE, contra);
            node.getConditional().accept(this);
            methodVisitor.visitJumpInsn(GOTO, end);
            methodVisitor.visitLabel(contra);
            node.getContraConditional().accept(this);
            methodVisitor.visitLabel(end);
        }
    }

    public void visit(ConditionalLoopNode node) {
        Label start = new Label();
        Label end = new Label();
        methodVisitor.visitLabel(start);
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
        node.getCondition().accept(this);
        methodVisitor.visitJumpInsn(IF_ACMPNE, end);
        node.getConditional().accept(this);
        methodVisitor.visitJumpInsn(GOTO, start);
        methodVisitor.visitLabel(end);
    }

    public void visit(InvocationNode node) {

        ExpressionNode target = node.getTarget();

        if (target != null){
            target.accept(this);
        } else {
            methodVisitor.visitVarInsn(ALOAD, 0);
        }

        for (ExpressionNode argument : node.getArguments()){
            argument.accept(this);
        }

        methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                toLocalName(node.getTarget().getType().getClassName()),
                node.getMethodName(),
                toSignature(node.getType(), node.getResolvedMethod().getTemplateMethod().getParameters().stream().map((item) -> item.type).collect(Collectors.toList())),
                true);
    }

    public List<VariableInfo> getVariables(){
        return variables;
    }

    private static String toLocalName(String className){
        if (className == null){
            className = Object.class.getName();
        }
        return className.replaceAll("\\.", "/");
    }

    private static String toSignature(String className){
        return "L" + toLocalName(className) + ";";
    }

    private static String toSignature(Type returnType, List<Type> parameterTypes){
        return "(" + parameterTypes.stream().map((type) -> toSignature(type.getClassName())).collect(Collectors.joining(","))+ ")" + (returnType == null ? "V" : toSignature(returnType.getClassName()));
    }

}
