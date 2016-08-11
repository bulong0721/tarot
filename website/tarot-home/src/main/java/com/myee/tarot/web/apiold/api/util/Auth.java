package com.myee.tarot.web.apiold.api.util;

import com.google.gson.Gson;
import me.chanjar.weixin.common.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public final class Auth {
	    private final String accessKey;
	    private final SecretKeySpec secretKey;

	    private Auth(String accessKey, SecretKeySpec secretKeySpec) {
	        this.accessKey = accessKey;
	        this.secretKey = secretKeySpec;
	    }

	    public static Auth create(String accessKey, String secretKey) {
	        if(!StringUtils.isBlank(accessKey) && !StringUtils.isBlank(secretKey)) {
	            byte[] sk = secretKey.getBytes(Charset.forName("UTF-8"));
	            SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
	            return new Auth(accessKey, secretKeySpec);
	        } else {
	            throw new IllegalArgumentException("empty key");
	        }
	    }

	    private Mac createMac() {
	        try {
	            Mac mac = Mac.getInstance("HmacSHA1");
	            mac.init(this.secretKey);
	            return mac;
	        } catch (GeneralSecurityException var3) {
	            var3.printStackTrace();
	            throw new IllegalArgumentException(var3);
	        }
	    }

	    private String sign(byte[] data) {
	        Mac mac = this.createMac();
	        String encodedSign = Base64.encode(mac.doFinal(data));
	        return this.accessKey + ":" + encodedSign;
	    }


	    private String signWithData(byte[] data) {
	        String s = Base64.encode(data);
	        return this.sign(s.getBytes(Charset.forName("UTF-8"))) + ":" + s;
	    }

	    public String sendToken(long expires) {
	        long deadline = System.currentTimeMillis() / 1000L + expires;
	        return this.sendTokenWithDeadline(deadline);
	    }

	    public String sendTokenWithDeadline(long deadline) {
	        Map<String, Object> x = new HashMap<String, Object>();
	        x.put("deadline", Long.valueOf(deadline));
	        String s = (new Gson()).toJson(x);
	        return this.signWithData(s.getBytes(Charset.forName("UTF-8")));
	    }

}
