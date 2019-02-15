package com.github.tubus.vkgroupstatistics.utils;

public class ParseUtils {

    public static int parseIntegerOrDefault(String Id, int defaultId) {
        int result = defaultId;
        try {
            result = Integer.parseInt(Id);
        } catch (Exception ex) {
        }
        return result;
    }
}
