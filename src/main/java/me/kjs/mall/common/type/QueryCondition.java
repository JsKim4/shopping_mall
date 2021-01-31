package me.kjs.mall.common.type;

public interface QueryCondition {
    int getContents();

    int getPage();

    default long getOffset() {
        return getContents() * getPage();
    }

    default long getSliceLimit() {
        return getContents() + 1;
    }

    default long getLimit() {
        return getContents();
    }

}
