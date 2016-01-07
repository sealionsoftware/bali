package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import java.util.List;

import static com.sealionsoftware.Matchers.isEmpty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationNodeTest {

    private InvocationNode subject = new InvocationNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testGetType() throws Exception {

        Type returnType = mock(Type.class);
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);

        subject.setResolvedMethod(method);

        assertThat(subject.getType(), is(returnType));
    }

    @Test
    public void testGetResolvedMethod() throws Exception {

        Method method = mock(Method.class);

        subject.setResolvedMethod(method);

        assertThat(subject.getResolvedMethod(), is(method));
    }

    @Test
    public void testGetTarget() throws Exception {

        ExpressionNode expressionNode = mock(ExpressionNode.class);

        subject.setTarget(expressionNode);

        assertThat(subject.getTarget(), is(expressionNode));
    }

    @Test
    public void testGetMethodName() throws Exception {

        String methodName = "aMethod";

        subject.setMethodName(methodName);

        assertThat(subject.getMethodName(), equalTo(methodName));
    }

    @Test
    public void testGetArguments() throws Exception {

        List<ExpressionNode> arguments = asList(mock(ExpressionNode.class));

        subject.setArguments(arguments);

        assertThat(subject.getArguments(), equalTo(arguments));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), equalTo(3));
    }

    @Test
    public void testGetChildren() throws Exception {
        assertThat(subject.getChildren(), isEmpty());
    }
}