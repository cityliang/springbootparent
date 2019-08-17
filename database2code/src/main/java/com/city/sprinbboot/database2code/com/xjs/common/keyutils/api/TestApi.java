package com.city.sprinbboot.database2code.com.xjs.common.keyutils.api;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestApi {

    public TestApi() {
    }

    /**
     * 银行：8fff32532ecc4743     块数据：1c0ab9c1281046bd
     * 银行： 392e4a617876455ebc97b7b7cb68ad20  544f28640aca6a78a4d183d2df71b208
     * 块数据：9a1e66f30f94fee15dcaf9d049c08561 dc1d23449b807d49eb248c6e5e9d0ff2
     * http://10.11.56.114/api/public/api/GYSZJJZJJLHCJ/fysxbzxrList.json
     * 银行：1c455cf4ec3a49b4b26d12f36d04be38  块数据:
     */

    //用户：块数据开发
    //private static String appkey="8747f7321ea54491";//管理员分配的appkey，用于对数据进行加解密
    //private static String apiUrl = "http://10.11.56.114/api/public/api/GYSGAJGYSGAJRKK/GYSGAJGYSGAJRKKRKSJ.json";//接口地址
    //private static String appid = "a24d1d7f8d9d4d809e39e8b92f82143b";//接口的appid 1c455cf4ec3a49b4b26d12f36d04be38 c265f6b072512418737daa087c0519ac
    //private static String sendId = "1000000031";//请求方标识，10位
    //private static String transportKey = "" ;//真实的传输密钥（用于加密） dfffe6be021c86aeacf209cef231b660
    //private static String verifyKey = "" ;//真实的验证密钥（用于验证）5c379dd90ad839b02a94476f4302fc52
    public static void main(String[] args) {
        //TestApi.doPost() ;
    }

    /*
     * transportKey=dfffe6be021c86aeacf209cef231b660
verifyKey=5c379dd90ad839b02a94476f4302fc52
     */
    public static String doPost(KeyParameter kp, Map paramMap) {
        try {
            TestApi testApi = new TestApi();
            //组装请求参数
            //Map paramMap = new HashMap();
            //paramMap.put("GMSFHM", "5201***5");

            //组装请求消息体
            Map bodyMap = new HashMap();
            bodyMap.put("body", paramMap);
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(bodyMap);
            //System.out.println("加密前的body消息体："+jsonBody);

            //获取用于加密body消息的传输密钥和验证密钥
            String transportKey = GainKey.getTransportKey(kp.getTransportKey(), kp.getAppkey());
            String verifyKey = GainKey.getVerifyKey(kp.getVerifyKey(), kp.getAppkey());
//			String transportKey = kp.getTransportKey();
//			String verifyKey = kp.getVerifyKey();
            //加密body消息体
            String encryptBody = SecurityUtils.encrypt(jsonBody, transportKey, verifyKey);
            //System.out.println("加密后的body消息体："+encryptBody);

            //组装请求数据
            String jsonStr = testApi.packRequestData(encryptBody, kp);
            //System.out.println("提交的查询报文："+jsonStr);

            //请求接口
            String result = testApi.post(jsonStr, kp.getApiUrl(), 0);
            //System.out.println("服务器返回的加密报文："+result);

            //获取body消息体（密文）
            Map resultRep = mapper.readValue(result, Map.class);
            Map appResMap = (Map) resultRep.get("appRes");
            //System.out.println("解密前的body消息体:"+(String)appResMap.get("body"));

            //解密body消息体
            String bodyStr = SecurityUtils.decrypt((String) appResMap.get("body"), transportKey, verifyKey);
            //System.out.println("解密后的body消息体:"+bodyStr);
//			Map keyDataMap = mapper.readValue(bodyStr, Map.class);
            return bodyStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /****
     * 组装请求数据
     * @return
     */
    private String packRequestData(String body, KeyParameter kp) {
        String jsonStr = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            //组装请求头
            Map headMap = new HashMap();
            headMap.put("sendId", kp.getSendId());
            headMap.put("sendOrder", String.valueOf(System.currentTimeMillis()));
            headMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
            headMap.put("appid", kp.getAppid());
            //组装请求消息
            Map appReqMap = new HashMap();
            appReqMap.put("head", headMap);
            appReqMap.put("body", body);
            Map requestMap = new HashMap();
            requestMap.put("appReq", appReqMap);
            jsonStr = mapper.writeValueAsString(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 调用http接口
     *
     * @param jObject
     * @param url
     * @return
     */
    private String post(String jsonStr, String url, int i) {
        InputStream in = null;
        ByteArrayOutputStream barray = null;
        String ret = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
            //http请求连接起时时间（单位：毫秒）
            conn.setConnectTimeout(10 * 1000);
            //数据读取超时时间（单位：毫秒）
            conn.setReadTimeout(10 * 1000);
            //设置允许输出
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //不用缓存
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            byte[] dataReq = jsonStr.getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(dataReq.length));
            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            out.write(dataReq);
            out.flush();
            out.close();
            // 接收返回信息
            in = new DataInputStream(conn.getInputStream());
            byte[] array = new byte[4096];
            int count = -1;
            barray = new ByteArrayOutputStream();
            while (-1 != (count = in.read(array))) {
                barray.write(array, 0, count);
            }
            byte[] dataResult = barray.toByteArray();
            barray.close();
            ret = new String(dataResult, "utf-8");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("aaaaaaaaaaaaaaa:" + "".equals(ret));
            if ("".equals(ret) && i < 10) {
                return post(jsonStr, url, i + 1);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (barray != null) {
                try {
                    barray.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
