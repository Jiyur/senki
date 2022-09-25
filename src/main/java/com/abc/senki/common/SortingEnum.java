package com.abc.senki.common;

public enum SortingEnum {
    price_asc("product:price_up"),
    price_desc("product:price_down"),
    product_id("id");
    private final String sort;

    SortingEnum(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }
}
