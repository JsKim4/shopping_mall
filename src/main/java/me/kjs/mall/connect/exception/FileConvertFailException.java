package me.kjs.mall.connect.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class FileConvertFailException extends BadRequestException {

    public FileConvertFailException() {
        super(ExceptionStatus.FILE_CONVERT_FAIL);
    }
}
