package com.example.coa.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringNormalizer {

    private StringNormalizer() {
        // prevent instantiation
    }

    public static String normalize(String s) {
        return s == null ? "" :
                s.toLowerCase()
                        .replaceAll("[^a-z0-9 ]", "")
                        .replaceAll("\\s+", " ")
                        .trim();
    }
    public static String extractPartNumber(String text) {
        if (text == null) return null;

        Pattern p = Pattern.compile("PART\\s*#\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);

        return m.find() ? m.group(1) : null;
    }

}