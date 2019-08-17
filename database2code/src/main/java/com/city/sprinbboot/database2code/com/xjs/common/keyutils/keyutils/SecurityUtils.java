package com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.VerifyError;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityUtils {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String encrypt(String src, String transportKey,
                                 String verifyKey) {
        byte[] data1 = encryptAES(src.getBytes(Charset.forName("utf8")),
                convertStringToBytes(transportKey));
        byte[] data2 = mac(data1, convertStringToBytes(verifyKey));
        byte[] data = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data, 0, data1.length);
        System.arraycopy(data2, 0, data, data1.length, data2.length);
        return new BASE64Encoder().encode(data).replaceAll("[\r\n]", "");
    }

    public static String decrypt(String src, String transportKey,
                                 String verifyKey) throws java.lang.VerifyError {
        byte[] data = null;
        try {
            data = new BASE64Decoder().decodeBuffer(src);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] data1 = new byte[data.length - 20];
        byte[] data2 = new byte[20];
        System.arraycopy(data, 0, data1, 0, data1.length);
        System.arraycopy(data, data1.length, data2, 0, data2.length);
        byte[] verify = mac(data1, convertStringToBytes(verifyKey));
        if (!Arrays.equals(verify, data2)) {
            throw new VerifyError();
        }
        byte[] bytes = decryptAES(data1, convertStringToBytes(transportKey));
        if (bytes == null)
            return null;
        try {
            return new String(bytes, "utf8").trim();
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    private static byte[] encryptAES(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int plaintextLength = data.length + 15 & 0x7FFFFFF0;
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(data, 0, plaintext, 0, data.length);
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(key);
            cipher.init(1, keyspec, ivspec);
            return cipher.doFinal(plaintext);
        } catch (Exception localException) {
        }
        return null;
    }

    private static byte[] decryptAES(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(key);
            cipher.init(2, keyspec, ivspec);
            return cipher.doFinal(data);
        } catch (Exception e) {
        }
        return null;
    }

    private static byte[] mac(byte[] src, byte[] secretKey) {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return mac.doFinal(src);
        } catch (Exception e) {
        }
        return null;
    }

    private static byte[] convertStringToBytes(String hex) {
        byte[] bytes = new byte[hex.length() >> 1];
        for (int i = 0; i < hex.length() >> 1; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return bytes;
    }

    public static String encrypt(String appkey, String str) {
        String result = "";
        try {
            String data = str;
            String key = appkey;
            String iv = appkey;
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes("utf-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - plaintextLength % blockSize;
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(1, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String encryptedStr = new BASE64Encoder().encode(encrypted);
            encryptedStr = encryptedStr.replaceAll("\n", "").replaceAll("\r",
                    "");

            result = encryptedStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String key, String data) {
        try {
            String iv = key;

            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(2, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            String strAscii0 = asciiToString("0");
            originalString = originalString.replaceAll(strAscii0, "");
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}