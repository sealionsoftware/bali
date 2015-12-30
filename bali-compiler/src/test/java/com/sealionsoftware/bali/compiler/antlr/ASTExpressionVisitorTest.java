package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import static bali.number.Primitive.convert;
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

    @Test
    public void testVisitIntegerLiteral() throws Exception {

        Token token = mock(Token.class);
        BaliParser.IntegerLiteralContext context = mock(BaliParser.IntegerLiteralContext.class);
        when(context.getText()).thenReturn("5");
        context.start = token;

        IntegerLiteralNode node = subject.visitIntegerLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getValue(), equalTo(convert(5)));
    }

    @Test
    public void testVisitReference() throws Exception {

        Token token = mock(Token.class);
        BaliParser.ReferenceContext context = mock(BaliParser.ReferenceContext.class);
        when(context.getText()).thenReturn("aVariable");
        context.start = token;

        ReferenceNode node = subject.visitReference(context);

        assertThat(node, notNullValue());
        assertThat(node.getName(), equalTo("aVariable"));
    }
}