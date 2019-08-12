package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.isSiteOfType;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TextLiteralNodeTest {

    private TextLiteralNode subject = new TextLiteralNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testSetValue() {
        subject.setValue("Hello World");
        assertThat(subject.getValue(), equalTo("Hello World"));
    }

    @Test
    public void testGetLine() {
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter() {
        assertThat(subject.getCharacter(), equalTo(3));
    }

    @Test
    public void testGetChildren() {
        assertThat(subject.getChildren(), is(empty()));
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetType() {
        Type type = mock(Type.class);
        subject.setType(type);
        assertThat(subject.getSite(), isSiteOfType(type));
    }
    
    @Test
    public void testToString(){

        subject.setValue("Hello World");
        assertThat(subject.toString(), equalTo("\"Hello World\""));
    }
}