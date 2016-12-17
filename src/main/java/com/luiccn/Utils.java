package com.luiccn;

public class Utils {

    private Utils() {
    }

    public static String quote(String s) {
        return "\"" + s + "\"";
    }

    public static String unQuote(String s) {
        return s.replaceAll("\"", "");
    }
}
