package com.sealionsoftware.bali.compiler.asm;

import org.junit.Test;
import org.objectweb.asm.Label;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class VariableInfoTest {

    private Label start = mock(Label.class);
    private Label end = mock(Label.class);
    private VariableInfo subject = new VariableInfo("aVariable", start, end);

    @Test
    public void testGetName() throws Exception {
        String name = subject.getName();
        assertThat(name, equalTo("aVariable"));
    }

    @Test
    public void testGetStart() throws Exception {
        Label name = subject.getStart();
        assertThat(name, equalTo(start));
    }

    @Test
    public void testGetEnd() throws Exception {
        Label name = subject.getEnd();
        assertThat(name, equalTo(end));
    }
}