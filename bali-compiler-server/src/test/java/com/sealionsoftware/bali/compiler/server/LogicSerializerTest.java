package com.sealionsoftware.bali.compiler.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import static bali.logic.Primitive.convert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogicSerializerTest {

    private BooleanSerializer subject = new BooleanSerializer();

    @Test
    public void testSerialize() throws Exception {

        JsonGenerator generator = mock(JsonGenerator.class);

        subject.serialize(convert(true), generator, mock(SerializerProvider.class));
        verify(generator).writeBoolean(true);
    }

}