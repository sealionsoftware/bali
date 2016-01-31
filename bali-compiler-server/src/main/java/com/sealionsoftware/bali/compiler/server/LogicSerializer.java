package com.sealionsoftware.bali.compiler.server;

import bali.Logic;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static bali.logic.Primitive.convert;

public class LogicSerializer extends JsonSerializer<Logic> {
    public void serialize(Logic value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeBoolean(convert(value));
    }
}
