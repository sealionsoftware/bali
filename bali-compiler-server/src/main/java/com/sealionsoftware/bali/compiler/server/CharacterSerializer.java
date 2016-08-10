package com.sealionsoftware.bali.compiler.server;

import bali.Character;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static bali.text.Primitive.convert;

public class CharacterSerializer extends JsonSerializer<Character> {
    public void serialize(Character value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new String(new char[]{convert(value)}));
    }
}
