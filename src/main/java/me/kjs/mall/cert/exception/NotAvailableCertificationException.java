package me.kjs.mall.cert.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableCertificationException extends BadRequestException {

    public NotAvailableCertificationException() {
        super(ExceptionStatus.NOT_AVAILABLE_CERTIFICATION);
    }
}
