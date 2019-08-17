package com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyMaker {
    public static String mac = Utils.getMac();

    public String getReaMac(String temp_verifykey, String app_key) {
        byte[] tempMac = Utils.getByte32(temp_verifykey);
        byte[] appKey = Utils.getByte32(app_key);

        byte[] tempMacLinkAppKey = new byte[64];
        System.arraycopy(tempMac, 0, tempMacLinkAppKey, 0, tempMac.length);
        System.arraycopy(appKey, 0, tempMacLinkAppKey, tempMac.length, appKey.length);

        byte[] tempMacAddAppKey = new byte[32];
        for (int i = 0; (i < tempMac.length) && (i < appKey.length); i++) {
            tempMacAddAppKey[i] = (byte) ((tempMac[i] + appKey[i]) % 128);
        }

        String keyStr = Utils.convertBytesToString(tempMacAddAppKey) + Utils.convertBytesToString(tempMacLinkAppKey);
        String keyStrDigest = getMD5Str(keyStr);
        return keyStrDigest;
    }

    public String getRealKey(String temp_transportkey, String app_key) {
        byte[] appKey = Utils.getByte32(app_key);
        byte[] tempMac = Utils.getByte32(temp_transportkey);

        byte[] tempMacLinkAppKey = new byte[64];
        System.arraycopy(appKey, 0, tempMacLinkAppKey, 0, appKey.length);
        System.arraycopy(tempMac, 0, tempMacLinkAppKey, appKey.length, tempMac.length);

        byte[] tempMacAddAppKey = new byte[32];
        for (int i = 0; (i < tempMac.length) && (i < appKey.length); i++) {
            tempMacAddAppKey[i] = (byte) ((tempMac[i] + appKey[i]) % 128);
        }

        String keyStr = Utils.convertBytesToString(tempMacLinkAppKey) + Utils.convertBytesToString(tempMacAddAppKey);

        String keyStrDigest = getMD5Str(keyStr);
        return keyStrDigest;
    }

    public String getMD5Str(String str) {
        String keyStrDigest = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(Utils.convertStringToBytes(str));
            keyStrDigest = Utils.convertBytesToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStrDigest;
    }
}