package me.kjs.mall.configs.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ExceptionSerializer extends JsonSerializer<Exception> {

    @Override
    public void serialize(Exception e, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("message", e.getMessage());
        gen.writeEndObject();
    }
}
