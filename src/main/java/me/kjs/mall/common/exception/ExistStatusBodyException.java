package me.kjs.mall.common.exception;

import lombok.Getter;
import me.kjs.mall.member.social.SocialType;

@Getter
public class ExistStatusBodyException extends RuntimeException {
    private final int status;
    private final SocialType socialType;
    private Object body;

    private String viewName;

    public ExistStatusBodyException(ExceptionStatus exceptionStatus, Object body, SocialType socialType, String viewName) {
        super(exceptionStatus.getMessage());
        this.status = exceptionStatus.getStatus();
        this.body = body;
        this.socialType = socialType;
        this.viewName = viewName;
    }

}
