package com.city.sprinbboot.database2code.com.xjs.common.gethtml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetHtml {

    public void getHtel(String url) {
        StringBuilder strBuilder = new StringBuilder();
        try {
            URL url1 = new URL(url);
            InputStream in = url1.openStream();
            BufferedReader bufr = new BufferedReader(new InputStreamReader(in, "gbk"));
            String str;
            while ((str = bufr.readLine()) != null) {
                strBuilder.append(str);
                strBuilder.append("\r\n");
            }
            bufr.close();
            in.close();
        } catch (Exception e) {
        }
        StringBuffer ssBuffer = new StringBuffer();
        //GetRegValue("<tr class='towntr'><td><a href='(?<url>\\d{2}/\\d{9}.html)'>(?<code>\\d{12})</a></td><td><a href='\\d{2}/\\d{9}.html'>(?<name>\\w*)</a></td></tr>","");
        GetRegValue("<td><a href='\\d{2}.html'>(\\w*)</a></td></tr>", strBuilder.toString());
    }

    /**
     * 执行正则提取出值
     *
     * @param regexString
     * @param remoteStr
     * @return
     */
    private List<AreaHtmlValue> GetRegValue(String regexString, String remoteStr) {
        Pattern p = Pattern.compile(regexString);
        Matcher mc = p.matcher(remoteStr);
        if (mc.find()) {
            System.out.println(mc.group());
        }
        /*return (from Match m in mc
                select new AreaHtmlValue
                {
                    Code = m.Groups["code"].Value,
                    Name = m.Groups["name"].Value,
                    Type = m.Groups["type"].Value
                }).ToList();*/
        return null;
    }

    public static void main(String[] args) {
        GetHtml gHtml = new GetHtml();
        gHtml.getHtel("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016");
    }
}
