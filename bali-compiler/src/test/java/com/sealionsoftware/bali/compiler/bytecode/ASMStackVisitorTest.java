package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.FieldData;
import com.sealionsoftware.bali.compiler.assembly.ReferenceData;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.tree.*;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

public class ASMStackVisitorTest implements Opcodes {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private MethodVisitor visitor = mock(MethodVisitor.class);
    private ASMStackVisitor subject = new ASMStackVisitor(visitor, "AType");

    @Test
    public void testVisitLiteralTrue() {

        LogicLiteralNode node = new LogicLiteralNode(0, 0, monitor);
        node.setValue(true);

        subject.visit(node);

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Logic", "TRUE", "Lbali/Logic;");
    }

    @Test
    public void testVisitLiteralFalse() {

        LogicLiteralNode node = new LogicLiteralNode(0, 0, monitor);
        node.setValue(false);

        subject.visit(node);

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Logic", "FALSE", "Lbali/Logic;");
    }

    @Test
    public void testVisitTextLiteral() {
        TextLiteralNode node = mock(TextLiteralNode.class);
        subject.visit(node);
        verify(visitor).visitLdcInsn(node.getValue());
        verify(visitor).visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);
    }

    @Test
    public void testVisitIntegerLiteral() {
        IntegerLiteralNode node = mock(IntegerLiteralNode.class);
        when(node.getValue()).thenReturn(5);
        subject.visit(node);
        verify(visitor).visitLdcInsn(5);
        verify(visitor).visitMethodInsn(INVOKESTATIC, "bali/number/Primitive", "convert", "(I)Lbali/Integer;", false);
    }

    @Test
    public void testVisitVariableDeclaration() {

        ExpressionNode expressionNode = mock(ExpressionNode.class);

        VariableNode node = new VariableNode(0, 0);
        node.setName("aVariable");
        node.setValue(expressionNode);

        subject.visit(node);

        verify(visitor).visitLabel(any(Label.class));
        verify(visitor).visitVarInsn(ASTORE, 1);

        List<VariableInfo> variables = subject.getVariables();
        assertThat(variables, notNullValue());
        assertThat(variables.size(), equalTo(1));
        VariableInfo variableInfo = variables.get(0);
        assertThat(variableInfo.name, equalTo("aVariable"));
    }

    @Test
    public void testVisitOptionalVariableDeclaration() {

        VariableNode node = new VariableNode(0, 0);
        node.setName("aVariable");

        subject.visit(node);

        verify(visitor).visitLabel(any(Label.class));
        verify(visitor).visitInsn(ACONST_NULL);
        verify(visitor).visitVarInsn(ASTORE, 1);

        List<VariableInfo> variables = subject.getVariables();
        assertThat(variables, notNullValue());
        assertThat(variables.size(), equalTo(1));
        VariableInfo variableInfo = variables.get(0);
        assertThat(variableInfo.name, equalTo("aVariable"));
    }

    @Test
    public void testVisitAssignment() {

        ExpressionNode expressionNode = mock(ExpressionNode.class);
        ReferenceNode referenceNode = mock(ReferenceNode.class);

        VariableNode setupNode = new VariableNode(0, 0);
        setupNode.setName("aVariable");
        setupNode.setValue(mock(ExpressionNode.class));

        subject.visit(setupNode);

        VariableNode variableNode = mock(VariableNode.class);
        when(variableNode.getId()).thenReturn(setupNode.getId());

        VariableData data = new VariableData(
                "aVariable", mock(Site.class), setupNode.getId()
        );
        when(referenceNode.getReferenceData()).thenReturn(data);

        AssignmentNode node = new AssignmentNode(0, 0);
        node.setValue(expressionNode);
        node.setTarget(referenceNode);

        subject.visit(node);

        verify(visitor, times(2)).visitVarInsn(ASTORE, 1);
    }

    @Test
    public void testVisitFieldAssignment() {

        ExpressionNode expressionNode = mock(ExpressionNode.class);
        ReferenceNode referenceNode = mock(ReferenceNode.class);

        FieldData data = new FieldData(
                "aField", mock(Site.class)
        );
        when(referenceNode.getReferenceData()).thenReturn(data);

        AssignmentNode node = new AssignmentNode(0, 0);
        node.setValue(expressionNode);
        node.setTarget(referenceNode);

        subject.visit(node);

        verify(visitor).visitFieldInsn(PUTFIELD, "AType", "aField", "Ljava/lang/Object;");
    }

    @Test(expected = RuntimeException.class)
    public void testVisitUnknownReferenceAssignment() {

        ExpressionNode expressionNode = mock(ExpressionNode.class);
        ReferenceNode referenceNode = mock(ReferenceNode.class);

        ReferenceData data = mock(ReferenceData.class);
        when(referenceNode.getReferenceData()).thenReturn(data);

        AssignmentNode node = new AssignmentNode(0, 0);
        node.setValue(expressionNode);
        node.setTarget(referenceNode);

        subject.visit(node);

        verify(visitor, times(2)).visitVarInsn(ASTORE, 1);
    }

    @Test
     public void testVisitCodeBlock() {
        CodeBlockNode mockNode = mock(CodeBlockNode.class);
        subject.visit(mockNode);
    }

    @Test
    public void testVisitTypeNode() {
        TypeNode mockNode = mock(TypeNode.class);
        subject.visit(mockNode);
    }

    @Test
    public void testVisitVariableReferenceNode() {
        UUID id = UUID.randomUUID();
        VariableNode mockTarget = mock(VariableNode.class);
        when(mockTarget.getId()).thenReturn(id);
        subject.visit(mockTarget);

        VariableData variableData = new VariableData("name", null, id);
        ReferenceNode mockNode = mock(ReferenceNode.class);
        when(mockNode.getReferenceData()).thenReturn(variableData);

        subject.visit(mockNode);
        verify(visitor).visitVarInsn(ALOAD, 1);
    }

    @Test
    public void testVisitFieldReferenceNode() {

        FieldData variableData = new FieldData("name", null);
        ReferenceNode mockNode = mock(ReferenceNode.class);
        when(mockNode.getReferenceData()).thenReturn(variableData);

        subject.visit(mockNode);
        verify(visitor).visitFieldInsn(GETFIELD, "AType", "name", "Ljava/lang/Object;");
    }

    @Test(expected = RuntimeException.class)
    public void testVisitUnknownReferenceNode() {
        ReferenceData referenceData = mock(ReferenceData.class);
        ReferenceNode mockNode = mock(ReferenceNode.class);
        when(mockNode.getReferenceData()).thenReturn(referenceData);

        subject.visit(mockNode);
        verify(visitor).visitVarInsn(ALOAD, 1);
    }

    @Test
    public void testVisitConditionalStatementNode() {
        ConditionalStatementNode mockNode = mock(ConditionalStatementNode.class);
        when(mockNode.getCondition()).thenReturn(mock(ExpressionNode.class));
        when(mockNode.getConditional()).thenReturn(mock(StatementNode.class));
        subject.visit(mockNode);
    }

    @Test
    public void testVisitConditionalNodeWithContraBlock() {
        ConditionalStatementNode mockNode = mock(ConditionalStatementNode.class);
        when(mockNode.getCondition()).thenReturn(mock(ExpressionNode.class));
        when(mockNode.getConditional()).thenReturn(mock(StatementNode.class));
        when(mockNode.getContraConditional()).thenReturn(mock(StatementNode.class));
        subject.visit(mockNode);
    }

    @Test
    public void testVisitConditionalLoop() {
        ConditionalLoopNode mockNode = mock(ConditionalLoopNode.class);
        when(mockNode.getCondition()).thenReturn(mock(ExpressionNode.class));
        when(mockNode.getConditional()).thenReturn(mock(StatementNode.class));
        subject.visit(mockNode);
    }

    @Test
    public void testVisitExpressionStatement() {
        ExpressionStatementNode mockNode = mock(ExpressionStatementNode.class);
        ExpressionNode mockExpressionNode = mock(ExpressionNode.class);
        Site returnType = mock(Site.class);

        when(mockNode.getExpressionNode()).thenReturn(mockExpressionNode);
        when(mockExpressionNode.getSite()).thenReturn(returnType);

        subject.visit(mockNode);

        verify(visitor).visitInsn(POP);
    }

    @Test
    public void testVisitInvocation() {

        InvocationNode node = mock(InvocationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        ExpressionNode argumentOne = mock(ExpressionNode.class);
        ExpressionNode argumentTwo = mock(ExpressionNode.class);
        Method resolvedMethod = mock(Method.class);
        Type targetType = mock(Type.class);
        Type argumentType = mock(Type.class);
        Type returnType = mock(Type.class);

        when(targetType.getClassName()).thenReturn("com.sealionsoftware.Target");
        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getTemplateMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getName()).thenReturn("aMethod");
        when(node.getTarget()).thenReturn(target);
        when(node.getArguments()).thenReturn(asList(argumentOne, argumentTwo));
        when(target.getSite()).thenReturn(new Site(targetType));
        when(resolvedMethod.getParameters()).thenReturn(asList(
                new Parameter("aParameter", new Site(argumentType)),
                new Parameter("aNullParameter", null))
        );
        when(argumentType.getClassName()).thenReturn("com.sealionsoftware.Argument");
        when(resolvedMethod.getReturnType()).thenReturn(new Site(returnType));
        when(returnType.getClassName()).thenReturn("com.sealionsoftware.Return");

        subject.visit(node);

        verify(target).accept(subject);
        verify(argumentOne).accept(subject);
        verify(argumentTwo).accept(subject);
        verify(visitor).visitMethodInsn(INVOKEINTERFACE, "com/sealionsoftware/Target", "aMethod", "(Lcom/sealionsoftware/Argument;,Ljava/lang/Object;)Lcom/sealionsoftware/Return;", true);
    }

    @Test
    public void testVisitOperation() {

        OperationNode node = mock(OperationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        ExpressionNode argumentOne = mock(ExpressionNode.class);
        Method resolvedMethod = mock(Method.class);
        Type targetType = mock(Type.class);
        Type argumentType = mock(Type.class);
        Type returnType = mock(Type.class);

        when(targetType.getClassName()).thenReturn("com.sealionsoftware.Target");
        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getTemplateMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getName()).thenReturn("aMethod");
        when(node.getTarget()).thenReturn(target);
        when(node.getArguments()).thenReturn(asList(argumentOne));
        when(target.getSite()).thenReturn(new Site(targetType));
        when(resolvedMethod.getParameters()).thenReturn(asList(
                new Parameter("aParameter", new Site(argumentType)))
        );
        when(argumentType.getClassName()).thenReturn("com.sealionsoftware.Argument");
        when(resolvedMethod.getReturnType()).thenReturn(new Site(returnType));
        when(returnType.getClassName()).thenReturn("com.sealionsoftware.Return");

        subject.visit(node);

        verify(target).accept(subject);
        verify(argumentOne).accept(subject);
        verify(visitor).visitMethodInsn(INVOKEINTERFACE, "com/sealionsoftware/Target", "aMethod", "(Lcom/sealionsoftware/Argument;)Lcom/sealionsoftware/Return;", true);
    }

    @Test
    public void testVisitIteration() {

        IterationNode node = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode statementNode = mock(StatementNode.class);

        when(node.getTarget()).thenReturn(target);
        when(node.getStatement()).thenReturn(statementNode);
        when(node.getIdentifier()).thenReturn("anItem");
        when(node.getItemData()).thenReturn(new VariableData("anItem", mock(Site.class), UUID.randomUUID()));


        subject.visit(node);


        verify(statementNode).accept(subject);
        verify(target).accept(subject);

    }

    @Test
    public void testVisitArrayNode(){

        List<ExpressionNode> mockItems = asList(
                setupMock(1),
                setupMock(2),
                setupMock(3),
                setupMock(4),
                setupMock(5),
                setupMock(6)
        );

        ArrayLiteralNode node = mock(ArrayLiteralNode.class);
        when(node.getItems()).thenReturn(mockItems);

        subject.visit(node);
    }

    @Test
    public void testVisitExistenceCheckNode() {
        ExistenceCheckNode node = mock(ExistenceCheckNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);

        subject.visit(node);
        verify(target).accept(subject);
        verify(visitor).visitJumpInsn(eq(IFNULL), any(Label.class));
    }

    @Test
    public void testVisitThrowNode() {
        ThrowNode node = mock(ThrowNode.class);
        ExpressionNode payload = mock(ExpressionNode.class);
        when(node.getPayload()).thenReturn(payload);

        subject.visit(node);
        verify(payload).accept(subject);
        verify(visitor).visitInsn(ATHROW);
    }

    @Test
    public void testVisitTryStatementNode() {

        TryStatementNode node = mock(TryStatementNode.class);
        StatementNode covered = mock(StatementNode.class);
        StatementNode catchStatement = mock(StatementNode.class);
        TypeNode typeNode = mock(TypeNode.class);
        Type type = mock(Type.class);

        when(node.getCoveredStatement()).thenReturn(covered);
        when(node.getCatchBlock()).thenReturn(catchStatement);
        when(node.getCaughtType()).thenReturn(typeNode);
        when(typeNode.getResolvedType()).thenReturn(new Site(type));
        when(type.getClassName()).thenReturn("foo.Bar");

        subject.visit(node);

        verify(covered).accept(subject);
        verify(visitor).visitVarInsn(ASTORE, 1);
        verify(visitor).visitTypeInsn(INSTANCEOF, "foo/Bar");
        verify(catchStatement).accept(subject);
        verify(visitor, times(4)).visitLabel(any(Label.class));

        verify(catchStatement).accept(subject);

        List<CatchInfo> catchInfos = subject.getCatchBlocks();
        assertThat(catchInfos, hasSize(1));
        CatchInfo catchInfo = catchInfos.get(0);
        assertThat(catchInfo.start, instanceOf(Label.class));
        assertThat(catchInfo.end, instanceOf(Label.class));
    }

    private IntegerLiteralNode setupMock(int i) {
        IntegerLiteralNode oneMock = mock(IntegerLiteralNode.class);
        when(oneMock.getValue()).thenReturn(i);
        return oneMock;
    }
}