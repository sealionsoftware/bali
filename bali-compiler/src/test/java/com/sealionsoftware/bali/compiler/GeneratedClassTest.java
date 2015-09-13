package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GeneratedClassTest {

    private static String NAME = "com.sealionsoftware.bali.HelloWorld";
    private static byte[] CODE = "TEST".getBytes();

    private GeneratedClass subject = new GeneratedClass(NAME, CODE);

    @Test
    public void testGetName() throws Exception {
        assertThat(subject.getName(), equalTo(NAME));
    }

    @Test
    public void testGetCode() throws Exception {
        assertThat(subject.getCode(), equalTo(CODE));
    }
}