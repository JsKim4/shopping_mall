package me.kjs.mall.configs.serializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kjs.mall.common.util.CollectionTextUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JsonProcessingException, IOException {
        String text = jsonParser.getText();
        if (CollectionTextUtil.isBlank(text)) {
            return null;
        }
        String[] date = text.split("-");
        if (date.length != 3) {
            return null;
        }
        try {
            int year = Integer.valueOf(date[0]);
            int month = Integer.valueOf(date[1]);
            int day = Integer.valueOf(date[2]);
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }
}
