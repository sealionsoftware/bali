package com.sealionsoftware.bali.compiler.assembly;

import bali.Iterable;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExistenceCheckNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IterationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.type.InferredType;
import org.junit.Test;

import java.util.UUID;

import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReferenceMatchingVisitorTest {

    private ReferenceMatchingVisitor subject = new ReferenceMatchingVisitor();

    @Test
    public void testVisitCodeBlockNode() throws Exception {

        CodeBlockNode node = new CodeBlockNode(2, 3);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitVariableNode() throws Exception {

        VariableNode node = new VariableNode(2, 3);

        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitVariableNodeWithAlreadyUsedName() throws Exception {

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");

        subject.visit(node);
        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.NAME_ALREADY_USED));
    }

    @Test
    public void testVisitResolvedReferenceNode() throws Exception {

        CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
        TypeNode typeNode = mock(TypeNode.class);
        typeNode.setResolvedType(mock(Site.class));

        VariableNode variableNode = new VariableNode(2, 3);
        variableNode.setName("aVariable");
        variableNode.setType(typeNode);
        ReferenceNode node = new ReferenceNode(4, 5, mockMonitor);
        node.setName("aVariable");

        subject.visit(variableNode);
        subject.visit(node);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitUnresolvedReferenceNode() throws Exception {

        CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
        ReferenceNode node = new ReferenceNode(2, 3, mockMonitor);
        node.setName("aVariable");

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.CANNOT_RESOLVE_REFERENCE));
    }

    @Test
    public void testVisitFlowTypingConditionalStatementNode() throws Exception {

        ConditionalStatementNode conditionalNode = mock(ConditionalStatementNode.class);
        ExistenceCheckNode expressionNode = mock(ExistenceCheckNode.class);
        ReferenceNode node = mock(ReferenceNode.class);
        StatementNode statementNode = mock(StatementNode.class);

        when(conditionalNode.getCondition()).thenReturn(expressionNode);
        when(conditionalNode.getConditional()).thenReturn(statementNode);
        when(expressionNode.getTarget()).thenReturn(node);
        when(node.getReferenceData()).thenReturn(new VariableData("reference", new Site(mock(Type.class), true), UUID.randomUUID()));
        when(statementNode.getChildren()).thenReturn(emptyList());

        subject.visit(conditionalNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitFlowTypingFieldConditionalStatementNode() throws Exception {

        ConditionalStatementNode conditionalNode = mock(ConditionalStatementNode.class);
        ExistenceCheckNode expressionNode = mock(ExistenceCheckNode.class);
        ReferenceNode node = mock(ReferenceNode.class);
        StatementNode statementNode = mock(StatementNode.class);

        when(conditionalNode.getCondition()).thenReturn(expressionNode);
        when(conditionalNode.getConditional()).thenReturn(statementNode);
        when(expressionNode.getTarget()).thenReturn(node);
        when(node.getReferenceData()).thenReturn(new FieldData("reference", new Site(mock(Type.class), true)));
        when(statementNode.getChildren()).thenReturn(emptyList());

        subject.visit(conditionalNode);

        assertThat(subject, containsNoFailures());
    }

    @Test(expected = RuntimeException.class)
    public void testVisitFlowTypingOtherReferenceConditionalStatementNode() throws Exception {

        ConditionalStatementNode conditionalNode = mock(ConditionalStatementNode.class);
        ExistenceCheckNode expressionNode = mock(ExistenceCheckNode.class);
        ReferenceNode node = mock(ReferenceNode.class);
        StatementNode statementNode = mock(StatementNode.class);

        when(conditionalNode.getCondition()).thenReturn(expressionNode);
        when(conditionalNode.getConditional()).thenReturn(statementNode);
        when(expressionNode.getTarget()).thenReturn(node);
        when(node.getReferenceData()).thenReturn(mock(ReferenceData.class));
        when(statementNode.getChildren()).thenReturn(emptyList());

        subject.visit(conditionalNode);
    }

    @Test
    public void testVisitNonFlowTypingConditionalStatementNode() throws Exception {

        ConditionalStatementNode conditionalNode = mock(ConditionalStatementNode.class);
        ExpressionNode condition = mock(ExpressionNode.class);
        StatementNode conditional = mock(StatementNode.class);
        StatementNode contraConditional = mock(StatementNode.class);

        when(conditionalNode.getCondition()).thenReturn(condition);
        when(conditionalNode.getConditional()).thenReturn(conditional);
        when(conditionalNode.getContraConditional()).thenReturn(contraConditional);

        subject.visit(conditionalNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitFlowTypingConditionalLoopNode() throws Exception {

        ConditionalLoopNode conditionalNode = mock(ConditionalLoopNode.class);
        ExpressionNode condition = mock(ExpressionNode.class);
        StatementNode conditional = mock(StatementNode.class);

        when(conditionalNode.getCondition()).thenReturn(condition);
        when(conditionalNode.getConditional()).thenReturn(conditional);

        subject.visit(conditionalNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeOverExpressionWithNoTypedArgument() throws Exception {

        IterationNode iterationNode = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode iteration = mock(StatementNode.class);
        Type targetType = mock(Type.class);

        when(targetType.getClassName()).thenReturn(Iterable.class.getName());
        when(iterationNode.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(iterationNode.getStatement()).thenReturn(iteration);

        subject.visit(iterationNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeOverExpressionWithTypedArgument() throws Exception {

        IterationNode iterationNode = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode iteration = mock(StatementNode.class);
        Type targetType = mock(Type.class);
        Type itemType = mock(Type.class);

        when(targetType.getClassName()).thenReturn(Iterable.class.getName());
        when(targetType.getTypeArguments()).thenReturn(asList(new Parameter("T", new Site(itemType))));
        when(iterationNode.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(iterationNode.getStatement()).thenReturn(iteration);

        subject.visit(iterationNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeOverSubTypeOfIterable() throws Exception {

        IterationNode iterationNode = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode iteration = mock(StatementNode.class);
        Type targetType = mock(Type.class);
        Type nonIterableSuperType = mock(Type.class);
        Type iterableSuperType = mock(Type.class);
        Type itemType = mock(Type.class);

        when(targetType.getClassName()).thenReturn("ASubtypeOfIterable");
        when(targetType.getInterfaces()).thenReturn(asList(nonIterableSuperType, iterableSuperType));
        when(iterableSuperType.getClassName()).thenReturn(Iterable.class.getName());
        when(iterableSuperType.getTypeArguments()).thenReturn(asList(new Parameter("T", new Site(itemType))));
        when(iterationNode.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(iterationNode.getStatement()).thenReturn(iteration);

        subject.visit(iterationNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeOverNonIterable() throws Exception {

        IterationNode iterationNode = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode iteration = mock(StatementNode.class);
        Type targetType = mock(Type.class);

        when(targetType.getClassName()).thenReturn("NotIterable");

        when(iterationNode.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(iterationNode.getStatement()).thenReturn(iteration);

        subject.visit(iterationNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeOverInferredIterable() throws Exception {

        IterationNode iterationNode = mock(IterationNode.class);
        ExpressionNode target = mock(ExpressionNode.class);
        StatementNode iteration = mock(StatementNode.class);
        Type targetType = mock(InferredType.class);

        when(iterationNode.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(iterationNode.getStatement()).thenReturn(iteration);

        subject.visit(iterationNode);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitIterationNodeWithAlreadyUsedName() throws Exception {

        VariableNode aVariable = new VariableNode(0, 0);
        aVariable.setName("aVariable");
        subject.visit(aVariable);

        IterationNode iterationNode = mock(IterationNode.class);
        when(iterationNode.getIdentifier()).thenReturn("aVariable");

        subject.visit(iterationNode);

        assertThat(subject, containsOneFailure(ErrorCode.NAME_ALREADY_USED));
    }

}