package com.myee.tarot.campaign.service.impl.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 有关Object的工具类
 */
public class ObjectUtils {
    private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    // 半角/全角 字母 数字  及汉字
    public static final String PRODUCT_NAME_PATTERN = "[(a-zA-Z0-9\\u4e00-\\u9fa5\\uff10-\\uff19\\uff21-\\uff3a\\uff41-\\uff5a)]{1,30}";
    // public static final String PRODUCT_NAME_PATTERN = "[(a-zA-Z0-9\\u4e00-\\u9fa5)]{1,30}";

    // 半角 字母 数字  及汉字
    public static final String LABEL_NAME_PATTERN = "[(a-zA-Z0-9\\u4e00-\\u9fa5)]{1,10}";


    /**
     * 判断对象是否为空
     * null				-> true
     * ""				-> true
     * " "				-> true
     * "null"			-> true
     * empty Collection	-> true
     * empty Array		-> true
     * others			-> false
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            return ((String) object).trim().equals("") || ((String) object).trim().equals("null");
        } else if (object instanceof Long) { //增加long参数的判断
            return ((Long) object).longValue() == 0l;
        } else if (object instanceof Collection<?> || object instanceof Map<?, ?>) {
            return ((Collection<?>) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }




    /**
     * 价格的正则表达式的验证
     *
     * @param str
     * @return
     */
    public static boolean validPrice(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d{0,6})|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        //java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d{0,6})|([0]{1}))(\\.(([1-9]{1}\\d{0,6})|([0]{1})){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }





    /**
     * 验证 内容是否包含数据库不能识别的字符  特别是EMOJ表情
     * 是否含有四字节的utf8
     *
     * @param s
     * @return true含有    false不含有
     */
    public static boolean has4Butf8(String s) {
        byte[] bs = s.getBytes(Charset.forName("utf8"));
        int size = bs.length;
        byte b;
        for (int i = 0; i < size; i++) {
            b = bs[i];
            if ((b & 0xF0) == 0xF0 && (i + 3 < size)) {
                if ((bs[i + 1] & 0x80) == 0x80 &&
                        (bs[i + 2] & 0x80) == 0x80 &&
                        (bs[i + 3] & 0x80) == 0x80) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 将价格为分单位 转成  元为单位
     * 1100-> 11
     * 1110 -> 11.1
     * 1111 -> 11.11
     *
     * @param priceFen
     * @return
     */
    public static String formatProductPrice(Long priceFen) {
        if (null != priceFen) {
            Double price = Double.parseDouble(priceFen.toString());
            DecimalFormat df = new DecimalFormat("0.##");
            return df.format(price / 100);
        }
        return "";
    }

    public static  String newStr(Long id){
        return StringUtils.leftPad(String.valueOf(id), 11, "0");
    }

    public static void main(String[] args){
        System.out.println(newStr(0l));
        System.out.println(newStr(110l));
        System.out.println(newStr(1234567890l));
        System.out.println(newStr(12345678901l));
        System.out.println(newStr(123456789012l));
    }
}
