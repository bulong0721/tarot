package com.myee.tarot.core.util;

public class RandomUtil {
	private static final int MILLISECOND_PER_SECOND = 1000;
	private static final java.util.Random random = new java.util.Random();
	private static final StringBuffer SOURCE = new StringBuffer("2345678abcdefhkmnpqrstuvwxyz");

	public static int nextInt(int max) {
		return random.nextInt(max);
	}

	public static int nextInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}

	public static boolean hit(float randomRewardProbability) {
		return random.nextInt(10000) < (int) (randomRewardProbability * 10000);
	}

	public static int nextIntIncludBothMinAndMax(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}


	public static String nextString(int length) {
		StringBuffer sb = new StringBuffer();
		
		int range=SOURCE.length();
		for(int i=0;i<length;i++){
			sb.append(SOURCE.charAt(nextInt(range)));
		}
		return sb.toString();
	}
	
	public static String generateVerifyCode() {
		return String.valueOf(random.nextInt(899999) + 100000);
	}

    public static String threeRandom() {
        return String.valueOf(random.nextInt(899) + 100);
    }
}
