package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExistenceCheckNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.parser.Mock.mockContext;
import static com.sealionsoftware.bali.compiler.parser.Mock.mockTerminal;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ASTExpressionVisitorTest {

    private ASTExpressionVisitor subject = new ASTExpressionVisitor(mock(CompilationThreadManager.class));

    @Test
    public void testVisitBooleanLiteral() throws Exception {

        BaliParser.LogicLiteralContext context = mockContext(BaliParser.LogicLiteralContext.class);
        when(context.getText()).thenReturn("true");

        LogicLiteralNode node = subject.visitLogicLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getValue(), equalTo(true));
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
        assertThat(node.getValue(), equalTo(5));
    }

    @Test
    public void testVisitArrayLiteral() throws Exception {

        BaliParser.ExpressionContext itemExpressionOne = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext itemExpressionTwo = mockContext(BaliParser.ExpressionContext.class);

        BaliParser.ArrayLiteralContext context = mockContext(BaliParser.ArrayLiteralContext.class);
        when(context.expression()).thenReturn(asList(itemExpressionOne, itemExpressionTwo));

        ArrayLiteralNode node = subject.visitArrayLiteral(context);

        assertThat(node, notNullValue());
        assertThat(node.getItems(), hasSize(2));
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
        BaliParser.ArgumentContext argumentContext = mockContext(BaliParser.ArgumentContext.class);
        BaliParser.ExpressionContext argumentExpression = mockContext(BaliParser.ExpressionContext.class);

        when(context.IDENTIFIER()).thenReturn(methodName);
        when(context.argument()).thenReturn(asList(argumentContext));
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

        when(parentContext.expression()).thenReturn(asList(targetContext));
        when(targetContext.accept(subject)).thenReturn(target);
        when(parentContext.invocation()).thenReturn(invocationContext);
        when(invocationContext.accept(subject)).thenReturn(invocationNode);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, is(invocationNode));
        verify(targetContext).accept(subject);
        verify(invocationContext).accept(subject);
    }

    @Test
    public void testVisitOperation() throws Exception {

        BaliParser.ExpressionContext parentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext argumentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.OperatorContext operatorContext = mock(BaliParser.OperatorContext.class);
        ExpressionNode target = mock(ExpressionNode.class);
        ExpressionNode argument = mock(ExpressionNode.class);

        when(parentContext.expression()).thenReturn(asList(targetContext, argumentContext));
        when(targetContext.accept(subject)).thenReturn(target);
        when(argumentContext.accept(subject)).thenReturn(argument);
        when(parentContext.operator()).thenReturn(operatorContext);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, instanceOf(OperationNode.class));
        verify(targetContext).accept(subject);
        verify(argumentContext).accept(subject);
    }

    @Test
    public void testVisitUnaryOperation() throws Exception {

        BaliParser.ExpressionContext parentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.OperatorContext operatorContext = mock(BaliParser.OperatorContext.class);
        ExpressionNode target = mock(ExpressionNode.class);

        when(parentContext.expression()).thenReturn(asList(targetContext));
        when(targetContext.accept(subject)).thenReturn(target);
        when(parentContext.operator()).thenReturn(operatorContext);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, instanceOf(OperationNode.class));
        verify(targetContext).accept(subject);
    }

    @Test
    public void testVisitParenthesisedOperation() throws Exception {

        BaliParser.ExpressionContext parentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        ExpressionNode target = mock(ExpressionNode.class);

        when(parentContext.expression()).thenReturn(asList(targetContext));
        when(parentContext.getChildCount()).thenReturn(1);
        when(parentContext.getChild(0)).thenReturn(targetContext);
        when(targetContext.accept(subject)).thenReturn(target);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, is(target));
        verify(targetContext).accept(subject);
    }

    @Test
    public void testVisitExistenceCheckOperation() throws Exception {

        BaliParser.ExpressionContext parentContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.ExpressionContext targetContext = mockContext(BaliParser.ExpressionContext.class);
        BaliParser.OperatorContext operatorContext = mock(BaliParser.OperatorContext.class);
        ExpressionNode target = mock(ExpressionNode.class);
        TerminalNode questionMark = mockTerminal("?");

        when(parentContext.expression()).thenReturn(asList(targetContext));
        when(targetContext.accept(subject)).thenReturn(target);
        when(parentContext.operator()).thenReturn(operatorContext);
        when(operatorContext.QUERY()).thenReturn(questionMark);

        ExpressionNode result = subject.visitExpression(parentContext);

        assertThat(result, instanceOf(ExistenceCheckNode.class));
        verify(targetContext).accept(subject);

    }
}