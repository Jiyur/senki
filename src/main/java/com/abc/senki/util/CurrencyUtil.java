package com.abc.senki.util;

public class CurrencyUtil {
    public static Double convertVNDToUsd(Double vnd) {
        return (double) Math.round(vnd / 23000);
    }
}
