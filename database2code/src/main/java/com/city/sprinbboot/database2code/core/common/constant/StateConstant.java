package com.city.sprinbboot.database2code.core.common.constant;


import com.city.sprinbboot.database2code.com.xjs.common.base.BaseConstants;

/**
 * upms系统常量类
 */
public class StateConstant extends BaseConstants {

    public static final String NOT_COMMITTED = "0";// 未提交 未发布 未通过
    public static final String COMMITTED = "1";// 已提交 已通过
    public static final String EFFECT = "2";// 已生效
    public static final String DISCONNECTED = "3";// 已停用 已禁用
    public static final String RELEASE = "4";// 已发布

}
