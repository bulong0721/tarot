package com.myee.tarot.core.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 2016/7/19.
 */
public class AutoNumUtil {

        /*
         * 生成编号
         */
        public static String getnum(String flowType) {
            Integer randomNum = (int)(java.lang.Math.random()*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            return flowType+sdf.format(date)+randomNum;
        }


        /**
         * 生成6位验证码
         * @return
         */
        public static String createRandomVcode(){
            //验证码
            String vcode = "";
            for (int i = 0; i < 6; i++) {
                vcode = vcode + (int)(Math.random() * 9);
            }
            return vcode;
        }

        public static void main(String[] args) {


        }


        public static String random(int length) {
            final Random r = new Random();
            final char[] arr = new char[62];
            for (int q = 0; q < 26; q++) {
                arr[q] = (char) ('A' + q);
            }
            for (int q = 26; q < 52; q++) {
                arr[q] = (char) ('a' + q - 26);
            }
            for (int q = 52; q < arr.length; q++) {
                arr[q] = (char) ('0' + q - 52);
            }
            // 接着你的程序往下写，就是在里面随机抽取6个
            final char[] code = new char[length];
            final List<Character> lst = new LinkedList<Character>();
            for (final char c : arr) {
                lst.add(c);
            }
            for (int i = 0; i < length; i++) {
                final int rnd = r.nextInt();
                // 使随机数长度总是在 0 ～ 列表长度 区间内
                final int index = (rnd < 0 ? -rnd : rnd) % lst.size();
                code[i] = lst.remove(index);
            }
            return Arrays.toString(code);
        }





}
