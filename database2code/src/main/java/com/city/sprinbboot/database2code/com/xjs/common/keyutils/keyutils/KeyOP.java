package com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class KeyOP {
    public static String getTransportKeyAndVerifyKeyViaPostAndSetValue(
            String transportTempKeyOld, String verifyTempKeyOld, String url) {
        String oldKeyJsonStr = "";
        ObjectMapper mapper = new ObjectMapper();
        Map paramMap = new HashMap();
        paramMap.put("transportTempKey", transportTempKeyOld);
        paramMap.put("verifyTempKey", verifyTempKeyOld);
        try {
            oldKeyJsonStr = mapper.writeValueAsString(paramMap);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("旧密钥oldKeyJsonStr：" + oldKeyJsonStr);
        String newKeyJsonStr = "";

        InputStream in = null;
        ByteArrayOutputStream barray = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setRequestProperty("Content-type", "application/json");

            conn.setConnectTimeout(10000);

            conn.setReadTimeout(10000);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            byte[] dataReq = oldKeyJsonStr.getBytes();

            conn.setRequestProperty("Content-Length",
                    String.valueOf(dataReq.length));

            conn.connect();
            OutputStream out = conn.getOutputStream();

            out.write(dataReq);
            out.flush();
            out.close();

            in = new DataInputStream(conn.getInputStream());
            byte[] array = new byte[4096];
            int count = -1;
            barray = new ByteArrayOutputStream();
            while (-1 != (count = in.read(array))) {
                barray.write(array, 0, count);
            }
            byte[] dataResult = barray.toByteArray();
            barray.close();

            String resultStr = new String(dataResult, "utf-8");
            System.out.println("服务器返回密钥结果resultStr：" + resultStr);

            Map resultMap = (Map) mapper.readValue(resultStr, Map.class);
            String transportTempKey_new = "";
            String verifyTempKey_new = "";
            if (Integer.valueOf(resultMap.get("code").toString()).intValue() == 0) {
                String keyDataJsonstr = mapper.writeValueAsString(resultMap
                        .get("data"));
                Map keyDataMap = (Map) mapper.readValue(keyDataJsonstr,
                        Map.class);
                transportTempKey_new = keyDataMap.get("transportTempKey")
                        .toString();
                verifyTempKey_new = keyDataMap.get("verifyTempKey").toString();

                Map keyMap = new HashMap();
                keyMap.put("transportTempKey", transportTempKey_new);
                keyMap.put("verifyTempKey", verifyTempKey_new);
                newKeyJsonStr = mapper.writeValueAsString(keyMap);
            }
            String str1 = newKeyJsonStr;
            return str1;
        } catch (Exception e) {
            e.printStackTrace();
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