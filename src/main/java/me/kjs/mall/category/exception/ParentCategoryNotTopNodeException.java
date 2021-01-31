package me.kjs.mall.category.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class ParentCategoryNotTopNodeException extends BadRequestException {
    public ParentCategoryNotTopNodeException() {
        super(ExceptionStatus.PARENT_CATEGORY_NOT_TOP_NODE);
    }

}
