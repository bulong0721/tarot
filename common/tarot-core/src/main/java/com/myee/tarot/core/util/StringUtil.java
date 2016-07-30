package com.myee.tarot.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 * Created by Martin on 2016/4/18.
 */
public class StringUtil {

    public static long getChecksum(String test) {
        try {
            byte buffer[] = test.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            CheckedInputStream cis = new CheckedInputStream(bais, new Adler32());
            byte readBuffer[] = new byte[buffer.length];
            cis.read(readBuffer);
            return cis.getChecksum().getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double determineSimilarity(String test1, String test2) {
        String first = new String(test1);
        first = first.replaceAll("[ \\t\\n\\r\\f\\v\\/'-]", "");
        Long originalChecksum = StringUtil.getChecksum(first);
        String second = new String(test2);
        second = second.replaceAll("[ \\t\\n\\r\\f\\v\\/'-]", "");
        Long myChecksum = StringUtil.getChecksum(second);
        StatCalc calc = new StatCalc();
        calc.enter(originalChecksum);
        calc.enter(myChecksum);
        return calc.getStandardDeviation();
    }

    /**
     * Protect against HTTP Response Splitting
     *
     * @return
     */
    public static String cleanseUrlString(String input) {
        return removeSpecialCharacters(decodeUrl(input));
    }

    public static String decodeUrl(String encodedUrl) {
        try {
            return encodedUrl == null ? null : URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // this should not happen
            e.printStackTrace();
            return encodedUrl;
        }
    }

    public static String removeSpecialCharacters(String input) {
        if (input != null) {
            input = input.replaceAll("[ \\r\\n]", "");
        }
        return input;
    }

    /**
     * given a string with the format "fields[someFieldName].value" (very common in error validation), returns
     * only "someFieldName
     *
     * @param expression
     * @return
     */
    public static String extractFieldNameFromExpression(String expression) {
        return expression.substring(expression.indexOf('[') + 1, expression.lastIndexOf(']'));
    }

    /**
     * @param text
     * @return
     */
    public static boolean isBlank(String text) {
        return (null == text || text.length() == 0);
    }

    /**
     * String转Map
     *
     * @param mapString
     * @return
     */
    public static Map transStringToMap(String mapString) {
        JSONObject jasonObject = JSON.parseObject(mapString);
        Map map = (Map) jasonObject;
        return map;
    }

    private static final int    PAD_LIMIT = 8192;
    public static final  String SPACE     = " ";

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }
}
