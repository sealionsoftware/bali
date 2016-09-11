package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMethodResolverTest {

    private InvocationMethodResolver subject = new InvocationMethodResolver();

    @Test
    public void testVisitInvocationWithoutTarget() throws Exception {

        ExpressionNode target = mock(ExpressionNode.class);
        InvocationNode node = mock(InvocationNode.class);
        when(node.getTarget()).thenReturn(target);
        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.METHOD_NOT_FOUND));
    }

    @Test
    public void testVisitInvocationWithUnresolvedMethod() throws Exception {

        String methodName = "notAMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(targetType.getMethod(methodName)).thenReturn(null);

        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.METHOD_NOT_FOUND));
    }

    @Test
    public void testVisitInvocationWithResolvedMethod() throws Exception {

        String methodName = "notAMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getSite()).thenReturn(new Site(targetType));

        Method resolvedMethod = mock(Method.class);
        when(targetType.getMethod(methodName)).thenReturn(resolvedMethod);

        subject.visit(node);

        verify(node).setResolvedMethod(resolvedMethod);
        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitOperationWithoutTarget() throws Exception {

        ExpressionNode target = mock(ExpressionNode.class);
        OperationNode node = mock(OperationNode.class);
        when(node.getTarget()).thenReturn(target);
        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.OPERATOR_NOT_FOUND));
    }

    @Test
    public void testVisitOperationWithUnresolvedMethod() throws Exception {

        String operatorName = "+";

        OperationNode node = mock(OperationNode.class);
        when(node.getOperatorName()).thenReturn("+");

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);

        Type targetType = mock(Type.class);
        when(target.getSite()).thenReturn(new Site(targetType));
        when(targetType.getOperator(operatorName)).thenReturn(null);

        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.OPERATOR_NOT_FOUND));
    }

    @Test
    public void testVisitOperationWithResolvedMethod() throws Exception {

        String operatorName = "+";

        OperationNode node = mock(OperationNode.class);
        when(node.getOperatorName()).thenReturn(operatorName);
        when(node.getArguments()).thenReturn(asList(mock(ExpressionNode.class)));

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getSite()).thenReturn(new Site(targetType));

        Operator resolvedOperator = mock(Operator.class);
        when(targetType.getOperator(operatorName)).thenReturn(resolvedOperator);

        subject.visit(node);

        verify(node).setResolvedMethod(resolvedOperator);
        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitInvocationOnOptionalType() throws Exception {

        String methodName = "aMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getSite()).thenReturn(new Site(targetType, true));
        when(targetType.getMethod(methodName)).thenReturn(mock(Method.class));

        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.CANNOT_INVOKE_ON_OPTIONAL));
    }

    @Test
    public void testVisitInvocationTypelessExpression() throws Exception {

        String methodName = "aMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        when(target.getSite()).thenReturn(null);

        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.METHOD_NOT_FOUND));
    }
}