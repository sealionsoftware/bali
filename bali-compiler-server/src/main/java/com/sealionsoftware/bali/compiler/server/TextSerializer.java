package com.sealionsoftware.bali.compiler.server;

import bali.Text;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static bali.text.Primitive.convert;

public class TextSerializer extends JsonSerializer<Text> {
    public void serialize(Text value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(convert(value));
    }
}
