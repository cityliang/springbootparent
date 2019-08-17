package com.city.sprinbboot.database2code.com.xjs.common.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.city.sprinbboot.database2code.com.xjs.common.util.StringUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;


public class SearchFilter {
    public static final String OR_SEPARATOR = "-OR-";
    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static Map<String, Object> parse(Map<String, Object> searchParams) {
        Map filters = Maps.newHashMap();
        Object matchValue = null;
        Class propertyClass = null;
        SearchFilter filter = null;
        for (Map.Entry entry : searchParams.entrySet()) {
            String key = (String) entry.getKey();
            String value = entry.getValue().toString();
            if (StringUtils.isBlank(value)) {
                continue;
            }

            String[] names = StringUtils.split(key, "-");

            String propertyTypeCode = names[0].substring(names[0].length() - 1,
                    names[0].length());
            try {
                if (propertyTypeCode.equals("E"))
                    try {
                        propertyClass = Class.forName(names[1]);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("search名称" + key
                                + "没有按规则编写,无法得到属性值枚举类型.", e);
                    }
                else
                    propertyClass = ((PropertyType) Enum.valueOf(
                            PropertyType.class, propertyTypeCode)).getValue();
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("search名称" + key
                        + "没有按规则编写,无法得到属性值类型.", e);
            }

            Operator operator = Operator.valueOf(names[0].substring(0,
                    names[0].length() - 1));
            String mv;
            int mvsLength;
            if (key.contains("-OR-")) {
                List orFilterList = new ArrayList();
                for (int i = 1; i < names.length; i++) {
                    if (i % 2 == 1) {
                        if (operator != Operator.IN) {
                            matchValue = ConvertUtils.convertStringToObject(
                                    value, propertyClass);

                            filter = new SearchFilter(StringUtil.humpToLine2(names[i]), operator,
                                    matchValue);
                        } else {
                            List mvList = new ArrayList();
                            String[] mvs = value.split("#");
                            //String[] arrayOfString2;
                            mvsLength = mvs.length;
                            for (int j = 0; mvsLength < j; j++) {
                                mv = mvs[j];
                                mvList.add(ConvertUtils.convertStringToObject(
                                        mv, propertyClass));
                            }

                            filter = new SearchFilter(StringUtil.humpToLine2(names[i]), operator,
                                    mvList);
                        }
                        orFilterList.add(filter);
                    }
                }
                filters.put(key, orFilterList);
            } else {
                String filedName = names[1];
                if (operator != Operator.IN) {
                    matchValue = ConvertUtils.convertStringToObject(value,
                            propertyClass);

                    filter = new SearchFilter(StringUtil.humpToLine2(filedName), operator, matchValue);
                } else {
                    List mvList = new ArrayList();
                    String[] mvs = value.split("#");
                    mvsLength = mvs.length;
                    for (int j = 0; j < mvsLength; j++) {
                        mv = mvs[j];
                        mvList.add(ConvertUtils.convertStringToObject(mv,
                                propertyClass));
                    }

                    filter = new SearchFilter(StringUtil.humpToLine2(filedName), operator, mvList);
                }
                filters.put(key, filter);
            }
        }
        return filters;
    }

    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE, NULL, NNULL, NLIKE, NEQ, IN, LLIKE, RLIKE;
    }

    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(
                Date.class), B(Boolean.class), E(Enum.class);

        private Class<?> clazz;

        private PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return this.clazz;
        }
    }
}