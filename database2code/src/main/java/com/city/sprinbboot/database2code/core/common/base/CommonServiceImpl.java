package com.city.sprinbboot.database2code.core.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.city.sprinbboot.database2code.com.xjs.common.base.BaseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

@Transactional
public class CommonServiceImpl<M extends BaseMapper<T>, T> extends
        ServiceImpl<M, T> implements ICommonService<T> {

    @Override
    public BaseResult update1(T entity) throws Exception {
        boolean result = this.updateById(entity);
        if (!result) {
            return new BaseResult(2, "更新失败", "更新失败");
        }
        return new BaseResult(1, "更新成功", "更新成功");
    }

    @Override
    public BaseResult update1(String id, T entity) throws Exception {
        boolean result = this.updateById(entity);
        if (!result) {
            return new BaseResult(2, "更新失败", "更新失败");
        }
        return new BaseResult(1, "更新成功", "更新成功");
    }


}