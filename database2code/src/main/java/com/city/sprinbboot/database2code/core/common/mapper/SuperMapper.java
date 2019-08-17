package com.city.sprinbboot.database2code.core.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SuperMapper<T> extends BaseMapper<T> {

    // 这里可以写自己的公共方法、注意不要让 mp 扫描到误以为是实体 Base 的操作
}
