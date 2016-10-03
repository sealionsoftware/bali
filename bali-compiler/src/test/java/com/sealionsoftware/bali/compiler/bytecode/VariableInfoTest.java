package com.sealionsoftware.bali.compiler.bytecode;

import org.junit.Test;
import org.objectweb.asm.Label;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class VariableInfoTest {

    private String name = "aVariable";
    private Label start = mock(Label.class);
    private Label end = mock(Label.class);
    private VariableInfo subject = new VariableInfo(name, start, end);

    @Test
    public void testGetNode() throws Exception {
        assertThat(subject.name, is(name));
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