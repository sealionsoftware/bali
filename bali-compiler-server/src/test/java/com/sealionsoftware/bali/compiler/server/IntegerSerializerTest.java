package com.sealionsoftware.bali.compiler.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import static bali.number.Primitive.convert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IntegerSerializerTest {

    private IntegerSerializer subject = new IntegerSerializer();

    @Test
    public void testSerialize() throws Exception {

        JsonGenerator generator = mock(JsonGenerator.class);

        subject.serialize(convert(4), generator, mock(SerializerProvider.class));
        verify(generator).writeNumber(4);
    }

}