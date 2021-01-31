package me.kjs.mall.connect;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class FileUploadServerException extends BadRequestException {
    public FileUploadServerException() {
        super(ExceptionStatus.FILE_UPLOAD_SERVER);
    }

}
