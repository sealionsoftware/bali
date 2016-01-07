package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMethodResolverTest {

    private InvocationMethodResolver subject = new InvocationMethodResolver();

    @Test
    public void testVisitNodeWithoutTarget() throws Exception {

        InvocationNode node = mock(InvocationNode.class);
        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.METHOD_NOT_FOUND));
    }

    @Test
    public void testVisitNodeWithUnresolvedMethod() throws Exception {

        String methodName = "notAMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getType()).thenReturn(targetType);
        when(targetType.getMethod(methodName)).thenReturn(null);

        subject.visit(node);
        assertThat(subject, containsOneFailure(ErrorCode.METHOD_NOT_FOUND));
    }

    @Test
    public void testVisitNodeWithResolvedMethod() throws Exception {

        String methodName = "notAMethod";

        InvocationNode node = mock(InvocationNode.class);
        when(node.getMethodName()).thenReturn(methodName);

        ExpressionNode target = mock(ExpressionNode.class);
        when(node.getTarget()).thenReturn(target);
        Type targetType = mock(Type.class);
        when(target.getType()).thenReturn(targetType);
        Method resolvedMethod = mock(Method.class);
        when(targetType.getMethod(methodName)).thenReturn(resolvedMethod);

        subject.visit(node);

        verify(node).setResolvedMethod(resolvedMethod);
        assertThat(subject, containsNoFailures());
    }
}