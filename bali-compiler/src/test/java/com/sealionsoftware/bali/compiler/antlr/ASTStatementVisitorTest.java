package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
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

        BaliParser.ScriptContext context = mock(BaliParser.ScriptContext.class);

        CodeBlockNode node = subject.visitScript(context);

        assertThat(node, notNullValue());
    }

    @Test
    public void testVisitVariableNode() throws Exception {

        TerminalNode typeTerminal = mockTerminal("AType");
        TerminalNode typeArgumentTerminal = mockTerminal("ATypeArgument");
        TerminalNode nameTerminal = mockTerminal("aVariable");

        BaliParser.TypeContext typeContext = mockContext(BaliParser.TypeContext.class);
        BaliParser.TypeListContext typeArgumentsContext = mockContext(BaliParser.TypeListContext.class);
        BaliParser.TypeContext typeArgumentContext = mockContext(BaliParser.TypeContext.class);
        BaliParser.ExpressionContext expressionContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.VariableDeclarationContext context = mockContext(BaliParser.VariableDeclarationContext.class);

        when(typeContext.IDENTIFIER()).thenReturn(typeTerminal);
        when(typeContext.typeList()).thenReturn(typeArgumentsContext);
        when(typeArgumentsContext.type()).thenReturn(asList(typeArgumentContext));
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

        BaliParser.BooleanLiteralContext literalContext = mockContext(BaliParser.BooleanLiteralContext.class);
        when(context.getChildCount()).thenReturn(1);
        when(context.getChild(0)).thenReturn(literalContext);
        when(literalContext.accept(any(ASTExpressionVisitor.class))).thenReturn(mock(BooleanLiteralNode.class));

        ExpressionNode node = subject.visitExpression(context);
        assertThat(node, instanceOf(BooleanLiteralNode.class));
    }

    @Test
    public void testVisitConditionalNode() throws Exception {
        BaliParser.ConditionalStatementContext context = mockContext(BaliParser.ConditionalStatementContext.class);
        BaliParser.ExpressionContext predicateContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ControlExpressionContext bodyContext = mockContext(BaliParser.ControlExpressionContext.class);

        when(context.expression()).thenReturn(predicateContext);
        when(predicateContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(ExpressionNode.class));
        when(context.controlExpression()).thenReturn(bodyContext);
        when(bodyContext.accept(any(ParseTreeVisitor.class))).thenReturn(mock(StatementNode.class));

        ConditionalStatementNode node = subject.visitConditionalStatement(context);
        assertThat(node, notNullValue());
        assertThat(node.getCondition(), notNullValue());
        assertThat(node.getConditional(), notNullValue());
    }

    @Test
    public void testVisitCodeBlockNode() throws Exception {
        BaliParser.CodeBlockContext context = mockContext(BaliParser.CodeBlockContext.class);
        CodeBlockNode node = subject.visitCodeBlock(context);
        assertThat(node, notNullValue());
    }

    private static TerminalNode mockTerminal(String value){
        TerminalNode ret = mock(TerminalNode.class);
        when(ret.getText()).thenReturn(value);
        return ret;
    }

    private static <T extends ParserRuleContext> T mockContext(Class<T> contextClass){
        T ret = mock(contextClass);
        ret.start = mock(Token.class);
        return ret;
    }

}