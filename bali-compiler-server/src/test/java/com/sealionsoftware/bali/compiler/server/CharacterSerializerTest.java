package com.sealionsoftware.bali.compiler.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import static bali.text.Primitive.convert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CharacterSerializerTest {

    private CharacterSerializer subject = new CharacterSerializer();

    @Test
    public void testSerialize() throws Exception {

        JsonGenerator generator = mock(JsonGenerator.class);

        subject.serialize(convert('H'), generator, mock(SerializerProvider.class));
        verify(generator).writeString("H");
    }

}