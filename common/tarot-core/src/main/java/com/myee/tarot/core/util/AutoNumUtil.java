package com.myee.tarot.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
            Random random = new Random();
            int a = random.nextInt(2);
            System.out.println(a);
        }


}
