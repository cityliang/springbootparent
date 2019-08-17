package com.city.sprinbboot.database2code.com.xjs.common.dict;

import com.city.sprinbboot.database2code.com.xjs.common.util.RedisUtil;

import java.util.Arrays;
import java.util.List;


public class DictUtil {

    public static String toName(String dict, Object value) {
        if (null == dict || "".equals(dict)) {
            return null != value ? value.toString() : "";
        }
        List<String> dics = RedisUtil.getList(dict);
        List<String> strings = null;
        if (null == dics) {
            return null != value ? value.toString() : "";
        }
        for (String dictdata : dics) {
            strings = Arrays.asList(dictdata.split("-"));
            if (strings.get(1).equals((value))) {
                return strings.get(2);
            }

        }
        return null != value ? value.toString() : "";
    }
}
