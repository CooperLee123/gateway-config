package com.aethercoder.util;

import com.aethercoder.constants.CommonConstants;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;


public class AESUtil {

    private static Charset CHARSET = Charset.forName("UTF8");

    public static String encrypt(String src, String key) {
        try {
            Cipher cipher = Cipher.getInstance(CommonConstants.AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(key), makeIv());
            return new String(Base64.getEncoder().encode(cipher.doFinal(src.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptToBytes(String src, String key) {
        try {
            Cipher cipher = Cipher.getInstance(CommonConstants.AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(key), makeIv());
            return cipher.doFinal(src.getBytes(CHARSET));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String src, String key) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(CommonConstants.AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(key), makeIv());
            decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(src)), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    public static String decryptBytes(byte[] src, String key) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(CommonConstants.AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(key), makeIv());
            decrypted = new String(cipher.doFinal(src));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    private static AlgorithmParameterSpec makeIv() {
        return new IvParameterSpec(CommonConstants.ENCRYPTION_IV.getBytes(CHARSET));
    }

    private static Key makeKey(String keyString) {
        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] key = md.digest(keyString.getBytes("UTF-8"));
            return new SecretKeySpec(keyString.getBytes(CHARSET), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) throws Exception {
        final String secretKey = CommonConstants.AES_KEY;

//		String originalString = "123456";
//		String originalString = "{\"accountName\":\"test\",\"addresses\":[{\"address\":\"QZDf31y9TMMcZdGG3Xcd3c6YvqHjQZ1o1B\",\"isDefault\":true}],\"captchaCode\":\"qnay\", \"deviceId\":\"aaa\", \"captchaToken\":\"798620688\"}";
        String originalString = "t3zdN1Aq1+Z/yJoJ/7o2CaS86qs7l1XQ7Ra4+BEBRprqQ0bI0ZeT0y8QIIkN6oC9UWWoLPjrbafroPwncjKE3XbFCVxWel7Zd7RKkU6nIJ0PXFoaVgRniOuHaBD2UX9iQKoROqLZ+ncB7GJSif8xNbtxGgZBRktNXp2Z/c9909grY+6N5KQxVZPd+lh07nYVui/EHtBtBI1C5fmq+YcGoHqjnaifm3gC2day68bGbaY/pARXax7cTCbzQfsfT8NxTeEw+4Vexk+LISLmZ9wSugS4QLZvFM2j2eko/OYbKVaT8k+V+n7ae0U1EhCZ8U1kpnKvL1gq90DhABmo6HdsA46C4LSak+H5DcJ8eeCd7Po4q0M7GgeBOvjioXQqmuVQCgmonTcikNGLVCL2l3qlrHvTyh198kiCdjuRjVWEPPP4m113RpBZtxMOnyaOWAe+IHfqAk3bAa3o+uU/errBM8ApgHttoXuXzPpkpp/FAYU7H3htFFPbV9aiT+YRfXgdtrq+aF1sbY+T+4uaeELeP79arAdcqim5I//v2yHfQE3PSya+I3htOUbDEHOMZ9T1A0BKRjj0Gt84KWVCAqYuvHt87ZgC5zOWAPTJd4uyOP+zmrRSa+g0CM3qX8OhagzyucCo0tq+wMVtsKG4IaWWsclWFZuANg80Wg7fMzzOI20F9lNw35IHKeycOQJGG+O8HP4fuNwGL16edM6fJLwma4fHwcsRnJ1k3m7uI7clK7WBgQSl9hSMBK2s5tqBN38eFO8BdlXMMtTFcDImL2bYB5F9fqggK2YnOWRRVxggrQAo1/6tb734FQFODMZWB7GqAF5ypHtjY9DGo3of1JvSjjWnvgsmqhukyZgfmHNanWwDqiC0WwwCqvdZT5j3";
//		String encryptedString = AESUtil.encrypt(originalString, secretKey) ;
		String decryptedString = AESUtil.decrypt(originalString, secretKey);
//        String decryptedString = AESUtil.decrypt(encryptedString, CommonConstants.DB_KEY);

        System.out.println(originalString);
//		System.out.println(encryptedString);
        System.out.println(decryptedString);

//        String accountNo = AESUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0OTc3MDg3MyIsImV4cCI6MTUyMDUzNjQyNn0.sR19MDmquJDCQ50Un658C9qI8pSC_9eAqUaI1i-0bwnh1kex1RvihCtjCuqgoD4CPEmVISJcHnSmEUevCPPwHA");
//        String encrypted = AESUtil.encrypt(accountNo, CommonConstants.AES_KEY) ;

//        System.out.println(accountNo);
//        System.out.println(encrypted);


//        String sign = "http://localhost:8080/account/findAccountName?accountNo=205439QTo#*DHWzie%IC)NV1tqeyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDU0MzkiLCJleHAiOjE1MTQzNDQ0Mjh9.ovA32czFpapww8jJ7GhFJ2xk8X4uGIZg0Hy1O1YZfQmAFKzNHE7UtVYat4fTDd8exAr1P49CvMaVO2M6RmfCIw123456";
//        System.out.println(MD5Util.encodeMD5(sign));

    }
}
