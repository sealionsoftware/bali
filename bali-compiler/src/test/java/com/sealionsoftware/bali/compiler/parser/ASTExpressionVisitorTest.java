package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import static bali.number.Primitive.convert;
import static com.sealionsoftware.bali.compiler.parser.Mock.mockContext;
import static com.sealionsoftware.bali.compiler.parser.Mock.mockTerminal;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ASTExpressionVisitorTest {

    private ASTExpressionVisitor subject = new ASTExpressionVisitor(mock(CompilationThreadManager.class));

    @Test
    public void testVisitBooleanLiteral() throws Exception {

        BaliParser.BooleanLiteralContext context = mockContext(BaliParser.BooleanLiteralContext.class);
        when(context.getText()).thenReturn("true");

        BooleanLiteralNode node = subject.visitBooleanLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.isTrue(), equalTo(true));
    }

    @Test
    public void testVisitTextLiteral() throws Exception {

        BaliParser.TextLiteralContext context = mockContext(BaliParser.TextLiteralContext.class);
        when(context.getText()).thenReturn("\"Hello World\"");

        TextLiteralNode node = subject.visitTextLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getValue(), equalTo("Hello World"));
    }

    @Test
    public void testVisitIntegerLiteral() throws Exception {

        BaliParser.IntegerLiteralContext context = mockContext(BaliParser.IntegerLiteralContext.class);
        when(context.getText()).thenReturn("5");

        IntegerLiteralNode node = subject.visitIntegerLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getValue(), equalTo(convert(5)));
    }

    @Test
    public void testVisitReference() throws Exception {

        BaliParser.ReferenceContext context = mockContext(BaliParser.ReferenceContext.class);
        when(context.getText()).thenReturn("aVariable");

        ReferenceNode node = subject.visitReference(context);

        assertThat(node, notNullValue());
        assertThat(node.getName(), equalTo("aVariable"));
    }

    @Test
    public void testVisitInvocation() throws Exception {

        BaliParser.InvocationContext context = mockContext(BaliParser.InvocationContext.class);

        TerminalNode methodName = mockTerminal("aMethod");
        BaliParser.ArgumentListContext argumentListContext = mockContext(BaliParser.ArgumentListContext.class);
        BaliParser.ArgumentContext argumentContext = mockContext(BaliParser.ArgumentContext.class);
        BaliParser.ExpressionContext argumentExpression = mockContext(BaliParser.ExpressionContext.class);

        when(context.IDENTIFIER()).thenReturn(methodName);
        when(context.argumentList()).thenReturn(argumentListContext);
        when(argumentListContext.argument()).thenReturn(asList(argumentContext));
        when(argumentContext.expression()).thenReturn(argumentExpression);

        InvocationNode invocationNode = subject.visitInvocation(context);

        assertThat(invocationNode, notNullValue());
        assertThat(invocationNode.getArguments(), hasSize(1));
        verify(argumentExpression).accept(subject);
    }

    @Test
    public void testVisitTargetedInvocation() throws Exception {

        BaliParser.ExpressionContext parentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.InvocationContext invocationContext = mockContext(BaliParser.InvocationContext.class);
        ExpressionNode target = mock(ExpressionNode.class);
        InvocationNode invocationNode = mock(InvocationNode.class);

        when(parentContext.expression()).thenReturn(targetContext);
        when(targetContext.accept(subject)).thenReturn(target);
        when(parentContext.invocation()).thenReturn(invocationContext);
        when(invocationContext.accept(subject)).thenReturn(invocationNode);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, is(invocationNode));
        verify(targetContext).accept(subject);
        verify(invocationContext).accept(subject);
    }
}