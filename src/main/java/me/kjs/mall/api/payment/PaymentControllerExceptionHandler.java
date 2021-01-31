package me.kjs.mall.api.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.payment.exception.PaymentException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class PaymentControllerExceptionHandler {


    @ExceptionHandler(PaymentException.class)
    public ModelAndView paymentExceptionHandler(PaymentException ex) {
        log.info("Status : {}", ex.getStatus());
        log.info("Message: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PaymentProperties.getRedirectResultPage());
        modelAndView.addObject("status", ex.getStatus());
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

}
