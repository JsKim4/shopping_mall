package me.kjs.mall.common.exception;

import lombok.Getter;

@Getter
public final class NotGrantException extends StatusException {

    public NotGrantException() {
        super(ExceptionStatus.GRANT_EXCEPTION, "요청한 리소스에 대한 권한이 없습니다.");
    }
}
