package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTExpressionVisitorTest {

    private ASTExpressionVisitor subject = new ASTExpressionVisitor(mock(CompilationThreadManager.class));

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
    public void testVisitTextLiteral() throws Exception {

        Token token = mock(Token.class);
        BaliParser.TextLiteralContext context = mock(BaliParser.TextLiteralContext.class);
        when(context.getText()).thenReturn("Hello World");
        context.start = token;

        TextLiteralNode node = subject.visitTextLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getValue(), equalTo("Hello World"));
    }
}