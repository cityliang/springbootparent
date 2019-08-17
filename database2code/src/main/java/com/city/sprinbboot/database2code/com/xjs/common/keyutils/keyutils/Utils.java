package com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class Utils {
    public static byte[] convertStringToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return bytes;
    }

    public static String convertBytesToString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        byte[] arrayOfByte = bytes;
        int j = bytes.length;
        for (int i = 0; i < j; i++) {
            byte aByte = arrayOfByte[i];
            str.append(Integer.toHexString(aByte >> 4 & 0xF));
            str.append(Integer.toHexString(aByte & 0xF));
        }
        return str.toString();
    }

    public static byte[] getByte32(String str) {
        if (str.length() > 32) {
            str = str.substring(0, 32);
        } else if (str.length() < 32) {
            StringBuffer buffer = new StringBuffer(str);
            for (int i = 0; i < 32 - str.length(); i++) {
                buffer.append('0');
            }
            str = buffer.toString();
        }
        return convertStringToBytes(str);
    }

    public static String getMac() {
        try {
            Enumeration el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                byte[] mac = ((NetworkInterface) el.nextElement())
                        .getHardwareAddress();
                if (mac != null) {
                    return convertBytesToString(mac);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}