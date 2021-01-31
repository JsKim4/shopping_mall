package me.kjs.mall.connect.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;


public class FileUploadFailException extends BadRequestException {
    public FileUploadFailException() {
        super(ExceptionStatus.FILE_UPLOAD_FAIL);
    }

}
