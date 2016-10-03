package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.IterationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.type.Class;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeCheckVisitorTest {

    private Class booleanMock = mock(Class.class);
    private Map<String, Class> library = map(
            put("bali.Boolean", booleanMock)
    );
    private TypeCheckVisitor subject = new TypeCheckVisitor(library);

    @Test
    public void testVisitVariableWithNoType(){

        ExpressionNode expressionNode = mock(ExpressionNode.class);

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitVariableWithCorrectType(){

        Site variableType = mock(Site.class);
        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getResolvedType()).thenReturn(variableType);

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(true);

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");
        node.setValue(expressionNode);
        node.setType(typeNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitVariableWithIncorrectType(){
        Site variableType = mock(Site.class);
        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getResolvedType()).thenReturn(variableType);

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(false);

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");
        node.setValue(expressionNode);
        node.setType(typeNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitAssignmentWithNoType(){

        VariableData variableData = new VariableData("aName", null, UUID.randomUUID());

        ExpressionNode expressionNode = mock(ExpressionNode.class);
        ReferenceNode referenceNode = mock(ReferenceNode.class);
        when(referenceNode.getReferenceData()).thenReturn(variableData);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitAssignmentWithCorrectType(){

        Site variableType = mock(Site.class);
        VariableData variableData = new VariableData("aName", variableType, UUID.randomUUID());

        ReferenceNode referenceNode = mock(ReferenceNode.class);
        when(referenceNode.getReferenceData()).thenReturn(variableData);

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(true);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitAssignmentWithIncorrectType(){
        Site variableType = mock(Site.class);
        VariableData variableData = new VariableData("aName", variableType, UUID.randomUUID());

        ReferenceNode referenceNode = mock(ReferenceNode.class);
        when(referenceNode.getReferenceData()).thenReturn(variableData);

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(false);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitConditionalWithNonBooleanType(){

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Site.class))).thenReturn(false);

        ConditionalStatementNode node = new ConditionalStatementNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitConditionalWithBooleanType(){

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Site.class))).thenReturn(true);

        ConditionalStatementNode node = new ConditionalStatementNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitLoopWithNonBooleanType(){

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Site.class))).thenReturn(false);

        ConditionalLoopNode node = new ConditionalLoopNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitLoopWithBooleanType(){

        Site expressionType = mock(Site.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getSite()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Site.class))).thenReturn(true);

        ConditionalLoopNode node = new ConditionalLoopNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testInvocationWithoutRequiredArguments(){

        InvocationNode node = mock(InvocationNode.class);
        Method resolvedMethod = mock(Method.class);
        Site parameterType = mock(Site.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(emptyList());

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_ARGUMENT_LIST));
    }

    @Test
    public void testInvocationWithInvalidArgumentType(){

        InvocationNode node = mock(InvocationNode.class);
        Method resolvedMethod = mock(Method.class);
        Site parameterType = mock(Site.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Site argumentType = mock(Site.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getSite()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(false);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testInvocationWithValidArgumentType(){

        InvocationNode node = mock(InvocationNode.class);
        Method resolvedMethod = mock(Method.class);
        Site parameterType = mock(Site.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Site argumentType = mock(Site.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getSite()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(true);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testOperation(){

        OperationNode node = mock(OperationNode.class);
        Method resolvedMethod = mock(Method.class);
        Site parameterType = mock(Site.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Site argumentType = mock(Site.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getSite()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(true);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testArrayLiteralHasInvalidArgumentType(){

        ArrayLiteralNode node = mock(ArrayLiteralNode.class);
        Parameter inferredTypeArgument = mock(Parameter.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Site argumentType = mock(Site.class);
        Type resolvedType = mock(Type.class);

        when(node.getSite()).thenReturn(new Site(resolvedType));
        when(resolvedType.getTypeArguments()).thenReturn(asList(inferredTypeArgument));
        when(node.getItems()).thenReturn(asList(argumentNode));
        when(argumentNode.getSite()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(any())).thenReturn(false);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testIterationHasInvalidTargetType(){

        IterationNode node = mock(IterationNode.class);
        ExpressionNode targetNode = mock(ExpressionNode.class);
        StatementNode statementNode = mock(StatementNode.class);
        VariableData itemData = mock(VariableData.class);
        Type targetType = mock(Type.class);

        when(node.getTarget()).thenReturn(targetNode);
        when(node.getStatement()).thenReturn(statementNode);
        when(node.getItemData()).thenReturn(itemData);
        when(targetNode.getSite()).thenReturn(new Site(targetType));
        when(targetType.isAssignableTo(any())).thenReturn(false);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testIteration(){

        IterationNode node = mock(IterationNode.class);
        ExpressionNode targetNode = mock(ExpressionNode.class);
        StatementNode statementNode = mock(StatementNode.class);
        VariableData itemData = mock(VariableData.class);
        Type targetType = mock(Type.class);

        when(node.getTarget()).thenReturn(targetNode);
        when(node.getStatement()).thenReturn(statementNode);
        when(node.getItemData()).thenReturn(itemData);
        when(targetNode.getSite()).thenReturn(new Site(targetType));
        when(targetType.isAssignableTo(any())).thenReturn(true);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

}