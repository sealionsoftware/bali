package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.DescendingVisitor;
import com.sealionsoftware.bali.compiler.assembly.FieldData;
import com.sealionsoftware.bali.compiler.assembly.ReferenceData;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.tree.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class ASMStackVisitor extends DescendingVisitor implements Opcodes {

    private String targetLocalName;
    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<CatchInfo> catchBlocks = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();
    private Map<UUID, Integer> variablesIndex = new HashMap<>();

    public ASMStackVisitor(MethodVisitor methodVisitor, String targetLocalName) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
        this.targetLocalName = targetLocalName;
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

        addToVariables(node.getId(), node.getName(), varStart);
    }

    private void addToVariables(UUID id, String name, Label startLabel){
        variables.add(new VariableInfo(
                name,
                startLabel,
                scopeHorizonStack.peek()
        ));
        variablesIndex.put(id, variables.size());
        methodVisitor.visitVarInsn(ASTORE, variables.size());
    }

    public void visit(CodeBlockNode node) {
        visitChildren(node);
    }

    public void visit(AssignmentNode node) {

        ReferenceData data = node.getTarget().getReferenceData();
        if (data instanceof VariableData){
            node.getValue().accept(this);
            methodVisitor.visitVarInsn(ASTORE, variablesIndex.get(((VariableData) data).id));
        } else if (data instanceof FieldData) {
            methodVisitor.visitVarInsn(ALOAD, 0);
            node.getValue().accept(this);
            methodVisitor.visitFieldInsn(PUTFIELD, targetLocalName, data.name, toSignature(data.type));
        } else {
            throw new RuntimeException("Invalid reference type");
        }
    }

    public void visit(ReferenceNode node){

        ReferenceData data = node.getReferenceData();
        if (data instanceof VariableData){
            methodVisitor.visitVarInsn(ALOAD, variablesIndex.get(((VariableData) data).id));
        } else if (data instanceof FieldData) {
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, targetLocalName, data.name, toSignature(data.type));
        } else {
            throw new RuntimeException("Invalid reference type");
        }
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

    public void visit(IterationNode node) {
        Label top = new Label();
        Label start = new Label();
        Label end = new Label();
        methodVisitor.visitLabel(top);
        node.getTarget().accept(this);
        Site collectionClass = node.getTarget().getSite();
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, toLocalName(collectionClass), "iterator", "()Lbali/Iterator;", true);
        methodVisitor.visitLabel(start);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "hasNext", "()Lbali/Logic;", true);
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
        methodVisitor.visitJumpInsn(IF_ACMPNE, end);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "bali/Iterator", "next", "()Ljava/lang/Object;", true);

        VariableData itemData = node.getItemData();
        methodVisitor.visitTypeInsn(CHECKCAST, toLocalName(itemData.type));

        addToVariables(itemData.id, node.getIdentifier(), start);
        node.getStatement().accept(this);
        methodVisitor.visitJumpInsn(GOTO, start);
        methodVisitor.visitLabel(end);
        methodVisitor.visitInsn(POP);
    }

    public void visit(ThrowNode node) {
        methodVisitor.visitTypeInsn(NEW, "bali/RuntimeException");
        methodVisitor.visitInsn(DUP);
        node.getPayload().accept(this);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "bali/RuntimeException", "<init>", "(Ljava/lang/Object;)V", false);
        methodVisitor.visitInsn(ATHROW);
    }

    public void visit(TryStatementNode node) {

        Label tryStart = new Label();
        Label tryEnd = new Label();
        Label catchStart = new Label();
        Label catchEnd = new Label();

        catchBlocks.add(new CatchInfo(tryStart, tryEnd));
        methodVisitor.visitLabel(tryStart);

        node.getCoveredStatement().accept(this);
        methodVisitor.visitJumpInsn(GOTO, catchEnd);
        scopeHorizonStack.push(catchEnd);
        methodVisitor.visitLabel(tryEnd);

        methodVisitor.visitInsn(DUP);
        methodVisitor.visitFieldInsn(GETFIELD, "bali/RuntimeException", "payload", "Ljava/lang/Object;");
        methodVisitor.visitInsn(DUP);
        addToVariables(node.getId(), node.getCaughtName(), tryEnd);

        methodVisitor.visitTypeInsn(INSTANCEOF, toLocalName(node.getCaughtType().getResolvedType()));
        methodVisitor.visitJumpInsn(IFNE, catchStart);

        methodVisitor.visitInsn(ATHROW);

        methodVisitor.visitLabel(catchStart);
        methodVisitor.visitInsn(POP);

        node.getCatchBlock().accept(this);
        methodVisitor.visitJumpInsn(GOTO, catchEnd);

        methodVisitor.visitLabel(catchEnd);
        scopeHorizonStack.pop();
    }

    public List<VariableInfo> getVariables(){
        return Collections.unmodifiableList(variables);
    }

    public List<CatchInfo> getCatchBlocks(){
        return Collections.unmodifiableList(catchBlocks);
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
        return "(" + parameterTypes.stream().map(ASMStackVisitor::toSignature).collect(joining(","))+ ")" + (returnType == null ? "V" : toSignature(returnType));
    }

}
