package com.sealionsoftware.bali.compiler.server;

import bali.Iterator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static bali.logic.Primitive.convert;

public class IteratorSerializer extends JsonSerializer<Iterator> {
    public void serialize(Iterator value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        StringBuilder sb = new StringBuilder("[[");
        if (convert(value.hasNext())){
            sb.append(value.next());
        }
        jsonGenerator.writeString(sb.append("]]").toString());
    }
}
