package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import org.junit.Test;

import static com.sealionsoftware.Matchers.containsOneValue;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TypeNodeTest {

    private TypeNode subject = new TypeNode(2, 3);

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), equalTo(3));
    }

    @Test
    public void testGetName() throws Exception {
        subject.setName("aName");
        assertThat(subject.getName(), equalTo("aName"));
    }

    @Test
    public void testGetArguments() throws Exception {
        TypeNode argumentNode = mock(TypeNode.class);
        subject.setArguments(asList(argumentNode));
        assertThat(subject.getArguments(), containsOneValue(is(argumentNode)));
    }

    @Test
    public void testGetResolvedType() throws Exception {
        Type resolvedType = mock(Type.class);
        subject.setResolvedType(resolvedType);
        assertThat(subject.getResolvedType(), is(resolvedType));
    }


    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(subject);
    }

    @Test
    public void testGetChildren() throws Exception {
        TypeNode argumentNode = mock(TypeNode.class);
        subject.setArguments(asList(argumentNode));
        assertThat(subject.getChildren(), containsOneValue(is(argumentNode)));
    }

}