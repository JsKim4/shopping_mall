package me.kjs.mall.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.ExistStatusBodyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerViewController {

    @ExceptionHandler(ExistStatusBodyException.class)
    public ModelAndView existStatusBodyExceptionHandler(ExistStatusBodyException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ex.getViewName());
        modelAndView.addObject("status", ex.getStatus());
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("data", ex.getBody());
        log.info("ex.getBody() : {}", ex.getBody());
        return modelAndView;
    }
}
