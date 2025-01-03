package dev.wuason.adapter;

import java.util.Locale;

public class Utils {

    public static String convert(String type, String id) {
        return type + ":" + id;
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
