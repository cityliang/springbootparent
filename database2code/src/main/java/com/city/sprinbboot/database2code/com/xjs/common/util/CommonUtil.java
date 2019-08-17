package com.city.sprinbboot.database2code.com.xjs.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

public class CommonUtil {

    /**
     * 获取实体类中设置的Column的长度，必需设置过@Column才能获取
     *
     * @param object
     * @param fieldName
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int getFieldLength(Class clazz, String fieldName) {
        Class<Object> clsEntity = clazz;
        Field tableColumn = null;
        try {
            tableColumn = clsEntity.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            System.out.println("权限异常！");
        } catch (NoSuchFieldException e) {
            System.out.println("找不到字段！");
        }
        Column columnAnnotation = (Column) tableColumn.getAnnotation(Column.class);
        int len = 0;
        if (columnAnnotation == null) {
            // 获取默认的长度
        } else {
            len = columnAnnotation.length();
        }
        return len;
    }

    /**
     * 根据某个类的属性获取该属性对应的值
     *
     * @param bean      实体名
     * @param paramName 属性名
     * @return10:05:56 AM
     */
    public static String getValueByParamName(Object bean, String paramName) {
        String fieldValue = "";

        String parGetName = "get" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);

        Class<?> clazz = bean.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                Method fieldGetMet = clazz.getMethod(parGetName);
                Object fieldVal = fieldGetMet.invoke(bean);
                String result = null;

                if (null != fieldVal) {
                    result = String.valueOf(fieldVal);
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (valueMap.containsKey(paramName)) {
            fieldValue = valueMap.get(paramName);
        }
        return fieldValue;
    }

    /**
     * 根据某个类的属性设置该属性对应的值
     *
     * @param bean       实体名
     * @param paramName  属性名
     * @param fieldValue 属性的值
     *                   10:57:51 AM
     */
    public static void setValueByParamName(Object bean, String paramName, String fieldValue) {
        String parGetName = "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);

        Class<?> cls = bean.getClass();
        try {
            Field field = cls.getDeclaredField(paramName);
            Method fieldSetMet = cls.getMethod(parGetName, field.getType());
            fieldSetMet.invoke(bean, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
