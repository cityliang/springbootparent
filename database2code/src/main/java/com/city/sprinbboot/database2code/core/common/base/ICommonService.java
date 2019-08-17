package com.city.sprinbboot.database2code.core.common.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.city.sprinbboot.database2code.com.xjs.common.base.BaseResult;
import com.sun.xml.internal.ws.api.message.Attachment;

import java.util.List;

public interface ICommonService<T> extends IService<T> {

    /**
     * 创建数据
     *
     * @param entity         要创建的数据实体
     * @param attachmentList 上传的附件集合
     * @return BaseResult
     * @throws Exception Exception
     */
//    BaseResult create1(T entity, List<Attachment> attachmentList) throws Exception;

    /**
     * 更新数据
     *
     * @param entity 要更新的实体
     * @return BaseResult
     * @throws Exception Exception
     */
    BaseResult update1(T entity) throws Exception;

    /**
     * 更新数据
     *
     * @param id     要更新的数据ID
     * @param entity 要更新的实体
     * @return BaseResult
     * @throws Exception Exception
     */
    BaseResult update1(String id, T entity) throws Exception;

    /**
     * 更新数据
     *
     * @param entity 要更新的实体
     * @param attachmentList 上传的附件集合
     * @return BaseResult
     * @throws Exception Exception
     */
//	BaseResult update1(T entity, List<Attachment> attachmentList) throws Exception;

    /**
     * 删除数据
     *
     * @param ids 要更新的数据ID集合
     * @return BaseResult
     * @throws Exception Exception
     */
//    BaseResult delete1(String ids) throws Exception;

    /**
     * 删除数据
     *
     * @param ids            要更新的数据ID集合
     * @param attachmentList 上传的附件集合
     * @return BaseResult
     * @throws Exception Exception
     */
//    BaseResult delete1(String ids, List<Attachment> attachmentList) throws Exception;
    /**
     * 业务表新增和附件绑定
     * @param entity
     * @param attachmentList
     * @return
     */
//	public boolean insertAndAttachment(T entity, List<Attachment> attachmentList);

    /**
     * 业务表新增和附件绑定 批量绑定
     *
     * @param entityList     entityList
     * @param attachmentList attachmentList
     * @return boolean
     */
//    public boolean insertAndAttachmentBatch(List<T> entityList, List<Attachment> attachmentList);

    /**
     * 业务表更新和附件绑定
     * @param entity
     * @param attachmentList
     * @return
     */
//	public boolean updateAndAttachment(T entity, List<Attachment> attachmentList);

    /**
     * 业务表删除和附件删除
     * @param ids
     */
//	public boolean deleteBatchIdsAndAttachment(List<String> ids);

}