package me.kjs.mall.cert;

import java.util.UUID;

public enum KeyGeneratorType {
    ONLY_NUMBER_GENERATOR,
    UUID_GENERATOR; //default

    public String generatorKey(int keyRepeat) {

        String result = "";
        for (int i = 0; i < keyRepeat; i++) {
            if (this == ONLY_NUMBER_GENERATOR) {
                result += (int) ((Math.random() * 9) % 9);
            } else {
                result += UUID.randomUUID().toString();
            }
        }
        return result;
    }

}
