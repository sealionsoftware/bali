package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
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

        Type variableType = mock(Type.class);
        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getResolvedType()).thenReturn(variableType);

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
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
        Type variableType = mock(Type.class);
        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getResolvedType()).thenReturn(variableType);

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
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
        when(referenceNode.getVariableData()).thenReturn(variableData);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitAssignmentWithCorrectType(){

        Type variableType = mock(Type.class);
        VariableData variableData = new VariableData("aName", variableType, UUID.randomUUID());

        ReferenceNode referenceNode = mock(ReferenceNode.class);
        when(referenceNode.getVariableData()).thenReturn(variableData);

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(true);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitAssignmentWithIncorrectType(){
        Type variableType = mock(Type.class);
        VariableData variableData = new VariableData("aName", variableType, UUID.randomUUID());

        ReferenceNode referenceNode = mock(ReferenceNode.class);
        when(referenceNode.getVariableData()).thenReturn(variableData);

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(variableType)).thenReturn(false);

        AssignmentNode node = new AssignmentNode(2, 3);
        node.setTarget(referenceNode);
        node.setValue(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitConditionalWithNonBooleanType(){

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Type.class))).thenReturn(false);

        ConditionalStatementNode node = new ConditionalStatementNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitConditionalWithBooleanType(){

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Type.class))).thenReturn(true);

        ConditionalStatementNode node = new ConditionalStatementNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitLoopWithNonBooleanType(){

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Type.class))).thenReturn(false);

        ConditionalLoopNode node = new ConditionalLoopNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testVisitLoopWithBooleanType(){

        Type expressionType = mock(Type.class);
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        when(expressionNode.getType()).thenReturn(expressionType);
        when(expressionType.isAssignableTo(any(Type.class))).thenReturn(true);

        ConditionalLoopNode node = new ConditionalLoopNode(2, 3);
        node.setCondition(expressionNode);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testInvocationWithoutRequiredArguments(){

        InvocationNode node = mock(InvocationNode.class);
        Method resolvedMethod = mock(Method.class);
        Type parameterType = mock(Type.class);

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
        Type parameterType = mock(Type.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Type argumentType = mock(Type.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getType()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(false);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.INVALID_TYPE));
    }

    @Test
    public void testInvocationWithValidArgumentType(){

        InvocationNode node = mock(InvocationNode.class);
        Method resolvedMethod = mock(Method.class);
        Type parameterType = mock(Type.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Type argumentType = mock(Type.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getType()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(true);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testOperation(){

        OperationNode node = mock(OperationNode.class);
        Method resolvedMethod = mock(Method.class);
        Type parameterType = mock(Type.class);
        ExpressionNode argumentNode = mock(ExpressionNode.class);
        Type argumentType = mock(Type.class);

        when(node.getResolvedMethod()).thenReturn(resolvedMethod);
        when(resolvedMethod.getParameters()).thenReturn(asList(new Parameter("aParameter", parameterType)));
        when(node.getArguments()).thenReturn(asList(argumentNode));
        when(argumentNode.getType()).thenReturn(argumentType);
        when(argumentType.isAssignableTo(parameterType)).thenReturn(true);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

}