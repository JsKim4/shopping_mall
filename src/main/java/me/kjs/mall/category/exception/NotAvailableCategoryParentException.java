package me.kjs.mall.category.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableCategoryParentException extends BadRequestException {


    public NotAvailableCategoryParentException() {
        super(ExceptionStatus.NOT_AVAILABLE_CATEGORY_PARENT);
    }

}
