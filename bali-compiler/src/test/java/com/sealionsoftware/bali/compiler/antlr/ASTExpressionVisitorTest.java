package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTExpressionVisitorTest {

    private ASTExpressionVisitor subject = new ASTExpressionVisitor();

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
}