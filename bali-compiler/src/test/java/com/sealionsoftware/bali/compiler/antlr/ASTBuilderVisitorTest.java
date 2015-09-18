package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTBuilderVisitorTest {

    private ASTBuilderVisitor subject = new ASTBuilderVisitor();

    @Test
    public void testVisitScript() throws Exception {

        BaliParser.ScriptContext context = mock(BaliParser.ScriptContext.class);

        CodeBlockNode node = subject.visitScript(context);

        assertThat(node, notNullValue());
    }

    @Test
    public void testVisitBooleanLiteral() throws Exception {

        Token token = mock(Token.class);
        BaliParser.BooleanLiteralContext context = mock(BaliParser.BooleanLiteralContext.class);
        when(context.getText()).thenReturn("true");
        context.start = token;

        BooleanLiteralNode node = subject.visitBooleanLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.isTrue(), equalTo(true));
    }

    @Test
    public void testVisitVariableNode() throws Exception {

        BaliParser.ExpressionContext expressionContext = mock(BaliParser.ExpressionContext.class);
        TerminalNode nameNode = mock(TerminalNode.class);
        BaliParser.VariableDeclarationContext context = mock(BaliParser.VariableDeclarationContext.class);
        context.start = mock(Token.class);
        when(context.IDENTIFIER()).thenReturn(nameNode);
        when(nameNode.getText()).thenReturn("aVariable");
        when(context.expression()).thenReturn(expressionContext);

        VariableNode node = subject.visitVariableDeclaration(context);

        assertThat(node, notNullValue());
        assertThat(node.getName(), equalTo("aVariable"));
    }
}