/**
 * @author Icedog
 * @date 2019年2月28日 上午10:13:05
 * @Description 从服务器获取密钥
 */
package com.city.sprinbboot.database2code.com.xjs.common.keyutils.api;

import com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils.KeyMaker;
import com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils.KeyOP;

public class GainKey {
    private static String getKeyUrl = "http://127.0.0.1:8080/api/public/key/update/gajdev";
    private static String transportTempKey_old = "2f4d41348a51b5ab31f77afb148a59c4";//昨日临时传输密钥
    private static String verifyTempKey_old = "47c534751ad40dc660a3f429c34f3e6f";//昨日临时验证密钥

    public GainKey() {

    }

    public static String gainKeyFromSVR(String transportTempKeyOld, String verifyTempKeyOld, String url) {
        return KeyOP.getTransportKeyAndVerifyKeyViaPostAndSetValue(transportTempKeyOld, verifyTempKeyOld, url);
    }

    //获取真实的传输密钥（加密密钥）
    public static String getTransportKey(String transportTempKey_new, String appkey) {
        KeyMaker keyMaker = new KeyMaker();
        String transportKey = keyMaker.getRealKey(transportTempKey_new, appkey);
        return transportKey;
    }

    //获取真实的验证密钥
    public static String getVerifyKey(String verifyTempKey_new, String appkey) {
        KeyMaker keyMaker = new KeyMaker();
        String verifyKey = keyMaker.getReaMac(verifyTempKey_new, appkey);
        return verifyKey;
    }

    public static void main(String[] args) {

        String transportKey = GainKey.getTransportKey("2f4d41348a51b5ab31f77afb148a59c4", "39276fb855994233");
        System.out.println("transportKey:" + transportKey);
        String verifyKey = GainKey.getVerifyKey("47c534751ad40dc660a3f429c34f3e6f", "39276fb855994233");
        System.out.println("verifyKey" + verifyKey);

        //String ret = KeyOP.getTransportKeyAndVerifyKeyViaPostAndSetValue(GainKey.transportTempKey_old,GainKey.verifyTempKey_old,GainKey.getKeyUrl) ;
        //System.out.println("ret:"+ret);
    }
}