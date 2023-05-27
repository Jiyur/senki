package com.abc.senki.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {
    private static final String PRICE_ASC = "product:price_up";
    private static final String PRICE_DESC = "product:price_down";

    private static final String TOTAL_ASC="order:total_up";
    private static final String TOTAL_DESC="order:total_down";
    public static Pageable createPageRequest(int pageNo, int pageSize, String sort) {
        return switch (sort) {
            case PRICE_ASC -> PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());
            case PRICE_DESC -> PageRequest.of(pageNo, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        };
    }
    public static Pageable createPageRequestOrder(int pageNo, int pageSize,String sort) {
        return switch (sort){
            case TOTAL_ASC -> PageRequest.of(pageNo,pageSize,Sort.by("total").ascending());
            case TOTAL_DESC -> PageRequest.of(pageNo,pageSize,Sort.by("total").descending());
            default -> PageRequest.of(pageNo,pageSize,Sort.by(sort).descending());

        };
    }
}
