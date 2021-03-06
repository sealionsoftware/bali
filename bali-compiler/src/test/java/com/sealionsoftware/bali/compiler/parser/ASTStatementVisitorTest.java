package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.parser.Mock.mockContext;
import static com.sealionsoftware.bali.compiler.parser.Mock.mockTerminal;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTStatementVisitorTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private ASTStatementVisitor subject = new ASTStatementVisitor(monitor);

    @Test
    public void testVisitScript() {

        BaliParser.ScriptContext context = mockContext(BaliParser.ScriptContext.class);
        BaliParser.StatementContext statementContext = mockContext(BaliParser.StatementContext.class);
        when(context.statement()).thenReturn(asList(statementContext));

        CodeBlockNode node = subject.visitScript(context);

        assertThat(node, notNullValue());
        assertThat(node.getChildren(), hasSize(1));
    }

    @Test
    public void testVisitVariableNode() {

        TerminalNode typeTerminal = mockTerminal("AType");
        TerminalNode typeArgumentTerminal = mockTerminal("ATypeArgument");
        TerminalNode nameTerminal = mockTerminal("aVariable");

        BaliParser.TypeContext typeContext = mockContext(BaliParser.TypeContext.class);
        BaliParser.TypeContext typeArgumentContext = mockContext(BaliParser.TypeContext.class);
        BaliParser.ExpressionContext expressionContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.VariableDeclarationContext context = mockContext(BaliParser.VariableDeclarationContext.class);

        when(typeContext.IDENTIFIER()).thenReturn(typeTerminal);
        when(typeContext.type()).thenReturn(asList(typeArgumentContext));
        when(typeArgumentContext.IDENTIFIER()).thenReturn(typeArgumentTerminal);
        when(context.IDENTIFIER()).thenReturn(nameTerminal);
        when(context.expression()).thenReturn(expressionContext);
        when(context.type()).thenReturn(typeContext);

        VariableNode node = subject.visitVariableDeclaration(context);

        assertThat(node, notNullValue());
        assertThat(node.getName(), equalTo("aVariable"));
        assertThat(node.getType(), notNullValue());
    }

    @Test
    public void testVisitAssignmentNode() {

        TerminalNode nameTerminal = mockTerminal("aVariable");

        BaliParser.ReferenceContext referenceContext = mockContext(BaliParser.ReferenceContext.class);
        when(referenceContext.IDENTIFIER()).thenReturn(nameTerminal);
        BaliParser.ExpressionContext expressionContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.AssignmentContext context = mockContext(BaliParser.AssignmentContext.class);

        when(context.reference()).thenReturn(referenceContext);
        when(context.expression()).thenReturn(expressionContext);

        AssignmentNode node = subject.visitAssignment(context);

        assertThat(node, notNullValue());
        assertThat(node.getTarget(), notNullValue());
        assertThat(node.getTarget().getName(), equalTo("aVariable"));
    }

    @Test
    public void testVisitExpressionNode() {
        
        BaliParser.ExpressionContext context = mockContext(BaliParser.ExpressionContext.class);
        when(context.accept(any(ASTExpressionVisitor.class))).thenReturn(mock(LogicLiteralNode.class));

        ExpressionStatementNode node = subject.visitExpression(context);
        assertThat(node.getExpressionNode(), instanceOf(LogicLiteralNode.class));
    }

    @Test
    public void testVisitConditionalNode() {
        BaliParser.ConditionalStatementContext context = mockContext(BaliParser.ConditionalStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlStatementContext bodyContext = mockContext(BaliParser.ControlStatementContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlStatement()).thenReturn(asList(bodyContext));
        when(bodyContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalStatementNode node = subject.visitConditionalStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getCondition(), notNullValue());
        assertThat(node.getConditional(), notNullValue());
    }

    @Test
    public void testVisitConditionalNodeWithContraryBranch() {
        BaliParser.ConditionalStatementContext context = mockContext(BaliParser.ConditionalStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlStatementContext bodyContext = mockContext(BaliParser.ControlStatementContext.class);
        BaliParser.ControlStatementContext contraContext = mockContext(BaliParser.ControlStatementContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlStatement()).thenReturn(asList(bodyContext, contraContext));
        when(contraContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalStatementNode node = subject.visitConditionalStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getContraConditional(), notNullValue());
    }

    @Test
    public void testVisitLoopNode() {
        BaliParser.LoopStatementContext context = mockContext(BaliParser.LoopStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlStatementContext bodyContext = mockContext(BaliParser.ControlStatementContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlStatement()).thenReturn(bodyContext);
        when(bodyContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalLoopNode node = subject.visitLoopStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getCondition(), notNullValue());
        assertThat(node.getConditional(), notNullValue());
    }

    @Test
    public void testVisitIterationNode() {
        BaliParser.IterationStatementContext context = mockContext(BaliParser.IterationStatementContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlStatementContext statementContext = mockContext(BaliParser.ControlStatementContext.class);
        TerminalNode identifier = mockTerminal("item");

        when(context.IDENTIFIER()).thenReturn(identifier);
        when(context.expression()).thenReturn(targetContext);
        when(targetContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlStatement()).thenReturn(statementContext);
        when(statementContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        IterationNode node = subject.visitIterationStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getTarget(), notNullValue());
        assertThat(node.getStatement(), notNullValue());
        assertThat(node.getIdentifier(), equalTo("item"));
    }

    @Test
    public void testVisitCodeBlockNode() {
        BaliParser.CodeBlockContext context = mockContext(BaliParser.CodeBlockContext.class);
        BaliParser.StatementContext statementContext = mockContext(BaliParser.StatementContext.class);
        when(context.statement()).thenReturn(asList(statementContext));

        CodeBlockNode node = subject.visitCodeBlock(context);

        assertThat(node, notNullValue());
        assertThat(node.getStatements(), hasSize(1));
    }

    @Test
    public void testVisitThrowNode() {
        BaliParser.ThrowStatementContext context = mockContext(BaliParser.ThrowStatementContext.class);
        BaliParser.ExpressionContext payloadContext = mockContext(BaliParser.ExpressionContext.class);

        when(context.expression()).thenReturn(payloadContext);
        when(payloadContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));

        ThrowNode node = subject.visitThrowStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getPayload(), notNullValue());
    }

    @Test
    public void testVisitTryStatementNode() {
        BaliParser.TryStatementContext tryStatement = mockContext(BaliParser.TryStatementContext.class);
        BaliParser.TryableStatementContext coveredContext = mockContext(BaliParser.TryableStatementContext.class);
        BaliParser.CatchBlockContext catchBlock1 = mockContext(BaliParser.CatchBlockContext.class);
        BaliParser.CatchBlockContext catchBlock2 = mockContext(BaliParser.CatchBlockContext.class);
        BaliParser.TypeContext typeContext1 = mockContext(BaliParser.TypeContext.class);
        BaliParser.TypeContext typeContext2 = mockContext(BaliParser.TypeContext.class);

        when(tryStatement.tryableStatement()).thenReturn(coveredContext);
        when(coveredContext.accept(subject)).thenReturn(mock(StatementNode.class));

        TerminalNode type1Terminal = mockTerminal("AType");
        TerminalNode name1Terminal = mockTerminal("aVariable");
        when(typeContext1.IDENTIFIER()).thenReturn(type1Terminal);
        when(catchBlock1.IDENTIFIER()).thenReturn(name1Terminal);
        when(catchBlock1.type()).thenReturn(typeContext1);
        when(catchBlock1.accept(subject)).thenReturn(mock(StatementNode.class));

        TerminalNode type2Terminal = mockTerminal("AnotherType");
        TerminalNode name2Terminal = mockTerminal("anotherVariable");
        when(typeContext2.IDENTIFIER()).thenReturn(type2Terminal);
        when(catchBlock2.IDENTIFIER()).thenReturn(name2Terminal);
        when(catchBlock2.type()).thenReturn(typeContext2);
        when(catchBlock2.accept(subject)).thenReturn(mock(StatementNode.class));

        when(tryStatement.catchBlock()).thenReturn(asList(catchBlock1, catchBlock2));

        TryStatementNode node = subject.visitTryStatement(tryStatement);

        assertThat(node, notNullValue());
        assertThat(node.getCoveredStatement(), instanceOf(TryStatementNode.class));

        TryStatementNode coveredNode = (TryStatementNode) node.getCoveredStatement();

        assertThat(node.getCaughtName(), equalTo("anotherVariable"));
        assertThat(node.getCaughtType(), notNullValue());
        assertThat(node.getCatchBlock(), notNullValue());

        assertThat(coveredNode.getCaughtName(), equalTo("aVariable"));
        assertThat(coveredNode.getCaughtType(), notNullValue());
        assertThat(coveredNode.getCatchBlock(), notNullValue());
    }

}