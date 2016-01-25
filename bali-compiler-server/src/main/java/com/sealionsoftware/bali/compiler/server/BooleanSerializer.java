package com.sealionsoftware.bali.compiler.server;

import bali.Boolean;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static bali.logic.Primitive.convert;

public class BooleanSerializer extends JsonSerializer<Boolean> {
    public void serialize(Boolean value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeBoolean(convert(value));
    }
}
