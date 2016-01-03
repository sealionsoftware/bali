package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;
import org.objectweb.asm.Label;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class VariableInfoTest {

    private VariableNode node = mock(VariableNode.class);
    private Label start = mock(Label.class);
    private Label end = mock(Label.class);
    private VariableInfo subject = new VariableInfo(node, start, end);

    @Test
    public void testGetNode() throws Exception {
        assertThat(subject.node, is(node));
    }

    @Test
    public void testGetStart() throws Exception {
        assertThat(subject.start, is(start));
    }

    @Test
    public void testGetEnd() throws Exception {
        assertThat(subject.end, is(end));
    }
}