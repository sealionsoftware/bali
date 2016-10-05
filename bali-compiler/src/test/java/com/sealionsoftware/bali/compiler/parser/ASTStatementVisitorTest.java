package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionStatementNode;
import com.sealionsoftware.bali.compiler.tree.IterationNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.ThrowNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTStatementVisitorTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private ASTStatementVisitor subject = new ASTStatementVisitor(monitor);

    @Test
    public void testVisitScript() throws Exception {

        BaliParser.ScriptContext context = mockContext(BaliParser.ScriptContext.class);
        BaliParser.StatementContext statementContext = mockContext(BaliParser.StatementContext.class);
        when(context.statement()).thenReturn(asList(statementContext));

        CodeBlockNode node = subject.visitScript(context);

        assertThat(node, notNullValue());
        assertThat(node.getChildren(), hasSize(1));
    }

    @Test
    public void testVisitVariableNode() throws Exception {

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
    public void testVisitAssignmentNode() throws Exception {

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
    public void testVisitExpressionNode() throws Exception {
        
        BaliParser.ExpressionContext context = mockContext(BaliParser.ExpressionContext.class);
        when(context.accept(any(ASTExpressionVisitor.class))).thenReturn(mock(LogicLiteralNode.class));

        ExpressionStatementNode node = subject.visitExpression(context);
        assertThat(node.getExpressionNode(), instanceOf(LogicLiteralNode.class));
    }

    @Test
    public void testVisitConditionalNode() throws Exception {
        BaliParser.ConditionalStatementContext context = mockContext(BaliParser.ConditionalStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlExpressionContext bodyContext = mockContext(BaliParser.ControlExpressionContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlExpression()).thenReturn(asList(bodyContext));
        when(bodyContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalStatementNode node = subject.visitConditionalStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getCondition(), notNullValue());
        assertThat(node.getConditional(), notNullValue());
    }

    @Test
    public void testVisitConditionalNodeWithContraryBranch() throws Exception {
        BaliParser.ConditionalStatementContext context = mockContext(BaliParser.ConditionalStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlExpressionContext bodyContext = mockContext(BaliParser.ControlExpressionContext.class);
        BaliParser.ControlExpressionContext contraContext = mockContext(BaliParser.ControlExpressionContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlExpression()).thenReturn(asList(bodyContext, contraContext));
        when(contraContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalStatementNode node = subject.visitConditionalStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getContraConditional(), notNullValue());
    }

    @Test
    public void testVisitLoopNode() throws Exception {
        BaliParser.LoopStatementContext context = mockContext(BaliParser.LoopStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlExpressionContext bodyContext = mockContext(BaliParser.ControlExpressionContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlExpression()).thenReturn(bodyContext);
        when(bodyContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalLoopNode node = subject.visitLoopStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getCondition(), notNullValue());
        assertThat(node.getConditional(), notNullValue());
    }

    @Test
    public void testVisitIterationNode() throws Exception {
        BaliParser.IterationStatementContext context = mockContext(BaliParser.IterationStatementContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlExpressionContext statementContext = mockContext(BaliParser.ControlExpressionContext.class);
        TerminalNode identifier = mockTerminal("item");

        when(context.IDENTIFIER()).thenReturn(identifier);
        when(context.expression()).thenReturn(targetContext);
        when(targetContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlExpression()).thenReturn(statementContext);
        when(statementContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        IterationNode node = subject.visitIterationStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getTarget(), notNullValue());
        assertThat(node.getStatement(), notNullValue());
        assertThat(node.getIdentifier(), equalTo("item"));
    }

    @Test
    public void testVisitCodeBlockNode() throws Exception {
        BaliParser.CodeBlockContext context = mockContext(BaliParser.CodeBlockContext.class);
        BaliParser.StatementContext statementContext = mockContext(BaliParser.StatementContext.class);
        when(context.statement()).thenReturn(asList(statementContext));

        CodeBlockNode node = subject.visitCodeBlock(context);

        assertThat(node, notNullValue());
        assertThat(node.getStatements(), hasSize(1));
    }

    @Test
    public void testVisitThrowNode() throws Exception {
        BaliParser.ThrowStatementContext context = mockContext(BaliParser.ThrowStatementContext.class);
        BaliParser.ExpressionContext payloadContext = mockContext(BaliParser.ExpressionContext.class);

        when(context.expression()).thenReturn(payloadContext);
        when(payloadContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));

        ThrowNode node = subject.visitThrowStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getPayload(), notNullValue());
    }

}