package me.kjs.mall.configs;

import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.configs.serializer.LocalDatePropertyEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDate;

@ControllerAdvice
@Slf4j
public class SetterToReflectionSetting {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDatePropertyEditor());
    }
}
