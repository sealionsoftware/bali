package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.DescendingVisitor;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExistenceCheckNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionStatementNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
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
        if (node.getExpressionNode().getSite() != null){
            methodVisitor.visitInsn(POP);
        }
    }

    public void visit(LogicLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", node.getValue() ? "TRUE" : "FALSE", "Lbali/Logic;");
    }

    public void visit(TextLiteralNode node) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    public void visit(IntegerLiteralNode node) {
        methodVisitor.visitLdcInsn(node.getValue());
        methodVisitor.visitMethodInsn(INVOKESTATIC, "bali/number/Primitive", "convert", "(I)Lbali/Integer;", false);
    }

    public void visit(ArrayLiteralNode node) {

        visitChildren(node);

        methodVisitor.visitTypeInsn(NEW, "bali/collection/Array");
        methodVisitor.visitInsn(DUP);

        List<ExpressionNode> items = node.getItems();
        int size = items.size();

        push(size);
        methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");

        int i = 0;
        for (ExpressionNode item : items){
            methodVisitor.visitInsn(DUP);
            push(i++);
            item.accept(this);
            methodVisitor.visitInsn(AASTORE);
        }

        methodVisitor.visitMethodInsn(INVOKESPECIAL, "bali/collection/Array", "<init>", "([Ljava/lang/Object;)V", false);
    }

    private void push(int i){
        switch (i){
            case 0 : methodVisitor.visitInsn(ICONST_0);  break;
            case 1 : methodVisitor.visitInsn(ICONST_1);  break;
            case 2 : methodVisitor.visitInsn(ICONST_2);  break;
            case 3 : methodVisitor.visitInsn(ICONST_3);  break;
            case 4 : methodVisitor.visitInsn(ICONST_4);  break;
            case 5 : methodVisitor.visitInsn(ICONST_5);  break;
            default: methodVisitor.visitIntInsn(BIPUSH, i);
        }
    }

    public void visit(TypeNode node) {
        visitChildren(node);
    }

    public void visit(VariableNode node) {
        Label varStart = new Label();
        methodVisitor.visitLabel(varStart);
        ExpressionNode value = node.getValue();
        if (value != null) {
            value.accept(this);
        } else {
            methodVisitor.visitInsn(ACONST_NULL);
        }

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
            methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
            node.getCondition().accept(this);
            methodVisitor.visitJumpInsn(IF_ACMPNE, end);
            node.getConditional().accept(this);
            methodVisitor.visitLabel(end);
        } else {
            Label end = new Label();
            Label contra = new Label();

            methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
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
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
        node.getCondition().accept(this);
        methodVisitor.visitJumpInsn(IF_ACMPNE, end);
        node.getConditional().accept(this);
        checkInterruptStatus();
        methodVisitor.visitJumpInsn(GOTO, start);
        methodVisitor.visitLabel(end);
    }

    private void checkInterruptStatus(){
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "interrupted", "()Z", false);
        Label notInterruptedLabel = new Label();
        methodVisitor.visitJumpInsn(IFEQ, notInterruptedLabel);
        methodVisitor.visitTypeInsn(NEW, "java/lang/RuntimeException");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitLdcInsn("Thread has been interrupted");
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
        methodVisitor.visitInsn(ATHROW);
        methodVisitor.visitLabel(notInterruptedLabel);
    }

    public void visit(InvocationNode node) {

        ExpressionNode target = node.getTarget();
        target.accept(this);

        for (ExpressionNode argument : node.getArguments()){
            argument.accept(this);
        }

        Method signatureMethod = node.getResolvedMethod().getTemplateMethod();

        methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                toLocalName(target.getSite()),
                signatureMethod.getName(),
                toSignature(signatureMethod.getReturnType(), signatureMethod.getParameters().stream().map((item) -> item.site).collect(Collectors.toList())),
                true);
    }

    public void visit(OperationNode node) {
        visit((InvocationNode) node);
    }

    public void visit(ExistenceCheckNode node) {
        node.getTarget().accept(this);

        Label falseLabel = new Label();
        Label endLabel = new Label();
        methodVisitor.visitJumpInsn(IFNULL, falseLabel);
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
        methodVisitor.visitJumpInsn(GOTO, endLabel);
        methodVisitor.visitLabel(falseLabel);
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "FALSE", "Lbali/Logic;");
        methodVisitor.visitLabel(endLabel);
    }

    public List<VariableInfo> getVariables(){
        return variables;
    }

    private static String toLocalName(Site site){
        return toLocalName(site == null ? null : site.type);
    }

    private static String toLocalName(Type type){
        return (type == null || type.getClassName() == null ? Object.class.getName() : type.getClassName()).replaceAll("\\.", "/");
    }

    private static String toSignature(Site type){
        return "L" + toLocalName(type) + ";";
    }

    private static String toSignature(Site returnType, List<Site> parameterTypes){
        return "(" + parameterTypes.stream().map(ASMStackVisitor::toSignature).collect(Collectors.joining(","))+ ")" + (returnType == null ? "V" : toSignature(returnType));
    }

}
