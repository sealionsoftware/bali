package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Class;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
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

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
        assertThat(subject, containsNoFailures());
    }

}