package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CompileErrorTest {

    private Node node = mock(Node.class);
    private CompileError subject = new CompileError(ErrorCode.INVALID_TYPE, node);

    @Test
    public void testEquals() throws Exception {
        CompileError other = new CompileError(ErrorCode.INVALID_TYPE, null);
        assertThat(subject.equals(other), is(true));
    }

    @Test
    public void testNotEquals() throws Exception {
        CompileError other = new CompileError(ErrorCode.UNKNOWN, null);
        assertThat(subject.equals(other), is(false));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(subject.toString(), containsString(ErrorCode.INVALID_TYPE.name()));
    }
}