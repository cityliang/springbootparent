package com.city.sprinbboot.database2code.com.xjs.common.token;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Target;

/**
 * <p>
 * <p>
 * 防止重复提交注解，用于方法上<br/>
 * <p>
 * 在新建页面方法上，设置needSaveToken()为true，此时拦截器会在Session中保存一个token，
 * <p>
 * 同时需要在新建的页面中添加
 * <p>
 * <input type="hidden" id="tokens" name="tokens" value="${(token)  !}">
 * <p>
 * <br/>
 * <p>
 * 保存方法需要验证重复提交的，设置needRemoveToken为true
 * <p>
 * 此时会在拦截器中验证是否重复提交
 * <p>
 * </p>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateSubmission {

    boolean needSaveToken() default false;

    boolean needRemoveToken() default false;

}