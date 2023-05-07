package com.abc.senki.util;

import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;

public class DataUtil {
    public static HashMap<String,Object> getData(String key,Object object){
        HashMap<String,Object> data=new HashMap<>();
        data.put(key,object);
        return data;
    }
}
