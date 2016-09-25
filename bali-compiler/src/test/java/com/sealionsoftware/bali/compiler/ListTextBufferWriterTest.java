package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static bali.text.Primitive.convert;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;

public class ListTextBufferWriterTest {

    private ListTextBufferWriter subject = new ListTextBufferWriter();

    @Test
    public void testWrite() throws Exception {

        subject.write(convert('H'));
        subject.write(convert('e'));
        subject.write(convert('\n'));
        subject.write(convert('W'));
        assertThat(subject, hasToString("[He, W]"));
    }

    @Test
    public void testWriteLine() throws Exception {

        subject.writeLine(convert("Hello"));
        subject.writeLine(convert("World"));
        assertThat(subject, hasToString("[Hello, World]"));

    }

    @Test
    public void testWriteLineDrainsCharBuffer() throws Exception {

        subject.write(convert('H'));
        subject.writeLine(convert("ello"));
        assertThat(subject, hasToString("[Hello]"));

    }

    @Test
    public void testGetWrittenLines() throws Exception {
        subject.writeLine(convert("Hello World"));

        assertThat(subject.getWrittenLines(), contains(convert("Hello World")));

    }



}