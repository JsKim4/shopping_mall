package me.kjs.mall.configs.serializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kjs.mall.common.util.CollectionTextUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JsonProcessingException, IOException {
        if (CollectionTextUtil.isBlank(jsonParser.getText())) {
            return null;
        }
        return LocalDateTime.parse(jsonParser.getText(), DATE_FORMAT);
    }
}
