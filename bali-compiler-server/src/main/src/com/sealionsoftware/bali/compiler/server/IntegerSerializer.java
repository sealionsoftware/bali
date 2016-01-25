package com.sealionsoftware.bali.compiler.server;

import bali.Integer;
import bali.number.Primitive;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IntegerSerializer extends JsonSerializer<Integer> {
    public void serialize(Integer value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(Primitive.convert(value));
    }
}
