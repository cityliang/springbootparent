package com.city.sprinbboot.database2code.com.xjs.common.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.qos.logback.classic.pattern.DateConverter;
import com.city.sprinbboot.database2code.com.xjs.common.util.Reflections;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;


public class ConvertUtils {

    public static List convertElementPropertyToList(Collection collection,
                                                    String propertyName) {
        List list = new ArrayList();
        try {
            for (Iterator localIterator = collection.iterator(); localIterator
                    .hasNext(); ) {
                Object obj = localIterator.next();
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    public static String convertElementPropertyToString(Collection collection,
                                                        String propertyName, String separator) {
        List list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static Object convertStringToObject(String value, Class<?> toType) {
        try {
            return org.apache.commons.beanutils.ConvertUtils.convert(value,
                    toType);
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }
}