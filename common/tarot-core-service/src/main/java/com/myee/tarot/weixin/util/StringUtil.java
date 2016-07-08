package com.myee.tarot.weixin.util;

import java.math.BigDecimal;

public final class StringUtil {

    public static final String EMPTY      = "";
    public static final char   WHITESPACE = ' ';

    /**
     * @param text
     * @return
     */
    public static boolean isBlank(String text) {
        return (null == text || text.length() == 0);
    }

    public static boolean isBlank(String text, boolean trimWhitespaces) {
        if (null != text && trimWhitespaces) {
            return isBlank(text.trim());
        }
        return isBlank(text);
    }

    public static String trimEnd(String text, String postfix) {
        if (text.endsWith(postfix)) {
            return text.substring(0, text.lastIndexOf(postfix));
        }
        return text;
    }

    public static String strip(String str, String stripChars) {
        if (isBlank(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while (start != strLen && WHITESPACE == str.charAt(start)) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                start++;
            }
        }
        return str.substring(start);
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while (end != 0 && WHITESPACE == str.charAt(end - 1)) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    public static String toLowerCase(String text) {
        if (null != text) {
            return text.toLowerCase();
        }
        return text;
    }

    public static String convertToCamel(String colName) {
        if (null == colName || 0 == colName.length()) {
            return colName;
        }
        if (colName.contains("_")) {
            return toCamelStyle(colName);
        } else {
            return colName.substring(0, 1).toLowerCase() + colName.substring(1);
        }
    }

    static String convertToPascal(String tableName) {
        if (null == tableName || 0 == tableName.length()) {
            return tableName;
        }
        if (tableName.contains("_")) {
            return toPascalStyle(tableName);
        } else {
            return tableName;
        }
    }

    private static String toPascalStyle(String name) {
        StringBuffer sb = new StringBuffer();
        String[] str = name.split("_");
        for (String temp : str) {
            sb.append(Character.toUpperCase(temp.charAt(0)));
            sb.append(temp.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private static String toCamelStyle(String name) {
        StringBuffer sb = new StringBuffer();
        String[] str = name.split("_");
        boolean firstTime = true;
        for (String temp : str) {
            if (firstTime) {
                sb.append(temp.toLowerCase());
                firstTime = false;
            } else {
                sb.append(Character.toUpperCase(temp.charAt(0)));
                sb.append(temp.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String removeCRLF(String in) {
        char[] inArray = in.toCharArray();
        StringBuffer out = new StringBuffer(inArray.length);
        for (int i = 0; i < inArray.length; i++) {
            char c = inArray[i];
            if (c == '\n' || c == '\r')
                ;
            else out.append(c);
        }
        return out.toString();
    }

    public static int getCount(String string, char countChar) {
        if (string == null || string.length() == 0) return 0;
        int counter = 0;
        char[] array = string.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == countChar) counter++;
        }
        return counter;
    } // getCount

    public static boolean is8Bit(String headerString) {
        // TODO Auto-generated method stub
        return false;
    }

    public static String toString(Object value) {
        if (null != value) {
            return value.toString();
        }
        return EMPTY;
    }

    public static Number toNumber(String value) {
        System.out.println();
        return new BigDecimal(value);

    }

    public static boolean equals(String lhs, String rhs) {
        if (null == lhs) {
            return (null == rhs);
        }
        return lhs.equals(rhs);
    }
}
