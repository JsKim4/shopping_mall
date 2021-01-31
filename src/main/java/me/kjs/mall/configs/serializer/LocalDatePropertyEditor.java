package me.kjs.mall.configs.serializer;

import me.kjs.mall.common.util.CollectionTextUtil;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

public class LocalDatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (CollectionTextUtil.isBlank(text)) {
            super.setValue(null);
        }
        String[] date = text.split("-");
        if (date.length != 3) {
            super.setValue(null);
        }
        try {
            int year = Integer.valueOf(date[0]);
            int month = Integer.valueOf(date[1]);
            int day = Integer.valueOf(date[2]);
            super.setValue(LocalDate.of(year, month, day));
        } catch (Exception e) {
            super.setValue(null);
        }
    }

}
