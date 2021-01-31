package me.kjs.mall.cert.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableCertGeneratorException extends BadRequestException {
    public NotAvailableCertGeneratorException() {
        super(ExceptionStatus.NOT_AVAILABLE_CERT_GENERATOR);
    }

}
