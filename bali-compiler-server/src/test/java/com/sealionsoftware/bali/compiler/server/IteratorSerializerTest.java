package com.sealionsoftware.bali.compiler.server;

import bali.collection.Array;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IteratorSerializerTest {

    private IteratorSerializer subject = new IteratorSerializer();

    @Test
    public void testSerialize() throws Exception {

        JsonGenerator generator = mock(JsonGenerator.class);

        subject.serialize(new Array<>("Testing").iterator(), generator, mock(SerializerProvider.class));
        verify(generator).writeString("[[Testing]]");
    }

}