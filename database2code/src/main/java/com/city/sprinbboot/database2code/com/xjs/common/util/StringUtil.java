package com.city.sprinbboot.database2code.com.xjs.common.util;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 */
public class StringUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        str = sb.toString();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        return str;
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 驼峰转下划线,效率比上面高
     *
     * @param str
     * @return
     */
    public static String humpToLine2(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder())
                    .append(Character.toLowerCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuffer())
                    .append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * object转String
     *
     * @param object
     * @return
     */
    public static String getString(Object object) {
        return getString(object, "");
    }

    public static String getString(Object object, String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return object.toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转Integer
     *
     * @param object
     * @return
     */
    public static int getInt(Object object) {
        return getInt(object, -1);
    }

    public static int getInt(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转Boolean
     *
     * @param object
     * @return
     */
    public static boolean getBoolean(Object object) {
        return getBoolean(object, false);
    }

    public static boolean getBoolean(Object object, Boolean defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String array2String(String[] str, String symbol) {
        String string = "";

        for (int i = 0; i < str.length; ) {
            string = string + String.valueOf(str[i]);

            ++i;
            string = string + symbol;
        }

        if ((!("".equals(string)))
                && (symbol.equals(string.substring(string.length() - 1)))) {
            string = string.substring(0, string.length() - 1);
        }

        return string;
    }

    public static String fillStringBeginAndEnd(String str, String checkStr) {
        String str1;
        if (str.indexOf(checkStr) == 0) {
            if (str.endsWith(checkStr)) {
                str1 = str;
            } else {
                str1 = str + checkStr;
            }

        } else if (str.endsWith(checkStr)) {
            str1 = checkStr + str;
        } else {
            str1 = checkStr + str + checkStr;
        }

        return str1;
    }

    public static boolean isEmptyString(String eStr) {
        return ((eStr == null) || ("".equals(eStr)));
    }

    public static boolean isBlank(Object str) {
        return (str == null) || (str.toString().trim().equals(""))
                || (str.toString().length() < 0);
    }

    public static boolean isBlank(String[] args) {
        return (args == null) || (args.length == 0);
    }

    public static String replaceDian(String content) {
        String str = "([\\\\/][^/\\.\\\\]*[\\\\/]\\.\\.)";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(content);
        if (m.find()) {
            content = content.replace(m.group(1), "");
            return replaceDian(content);
        }
        return content;
    }

    public static String replaceStr(String str, String s) {
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

    public static List<Integer> tranStringToIntegerOfArray(String[] array) {
        String[] arrayOfString;
        List integerList = new LinkedList();
        int j = (arrayOfString = array).length;
        for (int i = 0; i < j; ++i) {
            String str = arrayOfString[i];
            integerList.add(Integer.valueOf(Integer.parseInt(str)));
        }
        return integerList;
    }

    public static String matchTagAppend(String content, String startWith,
                                        String endWith, String replaceContent, boolean autoEnterRow) {
        String rn = "";
        if (autoEnterRow)
            rn = "\r\n\t";
        String i_startWith = startWith.replaceAll("\\[", "\\\\[").replaceAll(
                "\\]", "\\\\]");
        String i_endWith = endWith.replaceAll("\\[", "\\\\[").replaceAll("\\]",
                "\\\\]");
        String s = i_startWith + "[\\s\\S]*" + i_endWith;
        Pattern p = Pattern.compile(s);
        Matcher matcher = p.matcher(content);
        if (matcher.find()) {
            String sg = content.substring(matcher.start(), matcher.end());
            sg = sg.replace(endWith, replaceContent + rn + endWith);
            return content.substring(0, matcher.start()) + sg
                    + content.substring(matcher.end());
        }
        return content;
    }

    public static String matchTagBefore(String content, String startWith,
                                        String endWith, String replaceContent, boolean autoEnterRow) {
        String rn = "";
        if (autoEnterRow)
            rn = "\r\n\t";
        String i_startWith = startWith.replaceAll("\\[", "\\\\[").replaceAll(
                "\\]", "\\\\]");
        String i_endWith = endWith.replaceAll("\\[", "\\\\[").replaceAll("\\]",
                "\\\\]");
        String s = i_startWith + "[\\s\\S]*" + i_endWith;
        Pattern p = Pattern.compile(s);
        Matcher matcher = p.matcher(content);
        if (matcher.find()) {
            String sg = content.substring(matcher.start(), matcher.end());
            sg = sg.replace(startWith, startWith + replaceContent + rn);
            return content.substring(0, matcher.start()) + sg
                    + content.substring(matcher.end());
        }
        return content;
    }

    public static String matchTagInner(String content, String startWith,
                                       String endWith, String replaceContent, boolean autoEnterRow) {
        String rn = "";
        if (autoEnterRow)
            rn = "\r\n\t";
        String i_startWith = startWith.replaceAll("\\[", "\\\\[").replaceAll(
                "\\]", "\\\\]");
        String i_endWith = endWith.replaceAll("\\[", "\\\\[").replaceAll("\\]",
                "\\\\]");
        String s = i_startWith + "[\\s\\S]*" + i_endWith;
        Pattern p = Pattern.compile(s);
        Matcher matcher = p.matcher(content);
        return matcher.replaceAll(rn + startWith + replaceContent + rn
                + endWith);
    }

    public static String matchTagOuter(String content, String startWith,
                                       String endWith, String replaceContent) {
        String i_startWith = startWith.replaceAll("\\[", "\\\\[").replaceAll(
                "\\]", "\\\\]");
        String i_endWith = endWith.replaceAll("\\[", "\\\\[").replaceAll("\\]",
                "\\\\]");
        String s = i_startWith + "[\\s\\S]*" + i_endWith;
        Pattern p = Pattern.compile(s);
        Matcher matcher = p.matcher(content);
        return matcher.replaceAll(replaceContent);
    }

    public static String clearTags(String content) {
        if (null != content && "".equals(content)) {
            String result = content.replaceAll("<[^>]*>", "");
            result = result.replaceAll("&nbsp;", " ");
            result = result.replaceAll("\\s+", " ");
            return result;
        }
        return content;
    }

    public static String replaceBlank(String str) {
        if (str == null)
            return "";
        Pattern p = Pattern.compile("\t|\r|\n|\\s");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static String replaceByMap(String replaceStr,
                                      Map<String, String> replaceMap) {
        for (Iterator it = replaceMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            replaceStr = replaceStr.replace(
                    ((String) entry.getKey()).toString(),
                    ((String) entry.getValue()).toString());
        }
        return replaceStr;
    }

    // public static String interLength(String string, int length)
    // {
    // StringBuffer sb = new StringBuffer();
    // length *= 2;
    // if (byteLength(string) > length) {
    // int count = 0;
    // for (int ii = 0; ii < string.length(); ++ii) {
    // char temp = string.charAt(ii);
    // if (Integer.toHexString(temp).length() == 4)
    // count += 2;
    // else
    // ++count;
    //
    // if (count < length)
    // sb.append(temp);
    //
    // if (count == length) {
    // sb.append(temp);
    // break label120;
    // }
    // if (count > length) {
    // sb.append(" ");
    // break label120;
    // }
    // }
    // } else {
    // sb.append(string);
    // }
    // label120: return sb.toString();
    // }

    public static int byteLength(String string) {
        int count = 0;
        for (int ii = 0; ii < string.length(); ++ii)
            if (Integer.toHexString(string.charAt(ii)).length() == 4)
                count += 2;
            else
                ++count;

        return count;
    }

    public static boolean checkStringBeginAndEnd(String str, String checkStr) {
        if (str.indexOf(checkStr) == 0) {
            return (str.endsWith(checkStr));
        }

        return false;
    }

    /*
     * 改过
     */
    public static List<String> cutString(String str, String model) {
        String[] arrayOfString1;
        List strList = new ArrayList();
        Pattern p = Pattern.compile(model);
        String[] ss = p.split(str);
        int j = (arrayOfString1 = ss).length;
        for (int i = 0; i < j; ++i) {
            String ii = arrayOfString1[i];
            strList.add(ii);
        }
        return strList;
    }

    public static String deleteHtml(String eValue) {
        String result = eValue;
        while (result.indexOf("<") != -1) {
            int bPos = result.indexOf("<");
            int ePos = result.indexOf(">");
            result = result.substring(0, bPos) + result.substring(ePos + 1);
        }

        return result;
    }

    public static String getValue(String stag, String Pname) {
        if (stag.indexOf(Pname) == -1)
            return "";

        if (stag.indexOf(Pname + "\"") != -1)
            return "";

        String str1 = "";
        int start = stag.indexOf(Pname) + Pname.length();
        int end = stag.indexOf("\"", start + 1);
        str1 = stag.substring(start, end);
        return str1;
    }

    public static String getSpace(int depth) {
        String resultSpace = "";
        for (int i = 0; i < depth; ++i)
            resultSpace = resultSpace + "&nbsp;&nbsp;";

        return resultSpace;
    }

    public static int countChar(String content, String character) {
        int count = 0;
        Pattern p = Pattern.compile(character);
        Matcher m = p.matcher(content);
        while (m.find())
            ++count;

        return count;
    }

    public static int countString(String content, String symbol) {
        int count = 0;

        String[] arr = content.split("\\" + symbol);
        for (int i = 0; i < arr.length; ++i) {
            if ("".equals(arr[i])) {
                continue;
            }
            ++count;
        }

        return count;
    }

    public static String joinDanYinHao(String str) {
        String strNew = "";

        strNew = "'" + str + "'";

        strNew = strNew.replace(",", "','");

        return strNew;
    }

    public static String replaceComma(String str) {
        String strNew = "";

        strNew = str.replace(",,", ",");

        return strNew;
    }

    public static String trimGangForDisk(String str) {
        return replaceDian(str).replaceAll("/+", "\\\\").replaceAll("(\\\\)+",
                "/");
    }

    public static String trimGangForWeb(String str) {
        return replaceDian(str).replaceAll("\\\\+", "/").replaceAll("/+", "/");
    }

    public static String replaceEnter(String replaceAll) {
        return null;
    }

    public static String strReplaceAll(String str, String oldChar,
                                       String newChar) {
        String newStr = "";

        Pattern p = Pattern.compile(oldChar);
        Matcher m = p.matcher(str);
        newStr = m.replaceAll(newChar);

        return newStr;
    }

    public static String RandomStr(int length) {
        String retStr = "";
        Random random = new Random();
        for (int i = 0; i < length; ++i)
            retStr = retStr + random.nextInt(9);
        return retStr;
    }

    public static int countDepth(String path) {
        int count = 0;
        String[] s = path.split("/");
        for (int i = 0; i < s.length; ++i)
            if (!("".equals(s[i])))
                ++count;
        return count;
    }

    public static String subStr(String content, String symbol, int startIndex,
                                int endIndex) {
        String ret = "";

        String[] arr = content.split("\\" + symbol);

        for (int i = startIndex; i < endIndex; ++i) {
            if ("".equals(arr[i]))
                continue;
            ret = ret + arr[i] + symbol;
        }

        return ret;
    }

    public static String subStr(String content, int len, String value) {
        if (content != null && content.length() > len) {
            return content.substring(0, len) + value.trim();
        } else {
            return content;
        }
    }
}
