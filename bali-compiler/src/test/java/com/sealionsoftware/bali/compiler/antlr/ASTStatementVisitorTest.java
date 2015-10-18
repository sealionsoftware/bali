package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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

    private ASTStatementVisitor subject = new ASTStatementVisitor();

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
    public void testVisitExpressionNode() throws Exception {
        BaliParser.ExpressionContext context = mock(BaliParser.ExpressionContext.class);

        BaliParser.BooleanLiteralContext literalContext = mock(BaliParser.BooleanLiteralContext.class);
        when(context.getChildCount()).thenReturn(1);
        when(context.getChild(0)).thenReturn(literalContext);
        when(literalContext.accept(any(ASTExpressionVisitor.class))).thenReturn(mock(BooleanLiteralNode.class));

        ExpressionNode node = subject.visitExpression(context);
        assertThat(node, notNullValue());
        assertThat(node, instanceOf(BooleanLiteralNode.class));
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