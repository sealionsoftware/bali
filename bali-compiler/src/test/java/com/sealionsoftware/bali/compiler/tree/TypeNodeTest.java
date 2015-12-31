package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TypeNodeTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private TypeNode subject = new TypeNode(monitor, 2, 3);

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
        assertThat(subject.getArguments(), hasItem(is(argumentNode)));
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
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetChildren() throws Exception {
        TypeNode argumentNode = mock(TypeNode.class);
        subject.setArguments(asList(argumentNode));
        assertThat(subject.getChildren(), hasItem(is(argumentNode)));
    }

    @Test
    public void testToString() throws Exception {

        String name = "AType";
        String argumentName = "ATypeArgument";

        TypeNode argumentNode = new TypeNode(monitor, 4, 5);
        argumentNode.setName(argumentName);

        subject.setName(name);
        subject.setArguments(asList(argumentNode));

        String stringRepresentation = subject.toString();
        assertThat(stringRepresentation, containsString(name));
        assertThat(stringRepresentation, containsString(argumentName));
    }

}