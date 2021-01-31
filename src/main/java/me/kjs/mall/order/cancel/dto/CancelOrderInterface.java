package me.kjs.mall.order.cancel.dto;

import me.kjs.mall.common.type.Bank;

public interface CancelOrderInterface {
    String getCause();

    Bank getBank();

    String getName();

    String getAccountNo();
}
