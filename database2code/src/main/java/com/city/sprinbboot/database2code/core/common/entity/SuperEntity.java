package com.city.sprinbboot.database2code.core.common.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class SuperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    protected String id;

    @TableField(value = "orgId", fill = FieldFill.INSERT)
    protected String orgId; // 组织id

    @TableField(value = "orgCode", fill = FieldFill.INSERT)
    protected String orgCode; // 组织编码

    @TableField(value = "orgName", fill = FieldFill.INSERT)
    protected String orgName; // 组织名称

    @TableField(value = "createById", fill = FieldFill.INSERT)
    protected String createById; // 创建者id

    @TableField(value = "createByName", fill = FieldFill.INSERT)
    protected String createByName; // 创建者名称

    @TableField(value = "createDate", fill = FieldFill.INSERT)
    protected Date createDate; // 创建日期

    @TableField(value = "updateById", fill = FieldFill.INSERT_UPDATE)
    protected String updateById; // 更新者id

    @TableField(value = "updateByName", fill = FieldFill.INSERT_UPDATE)
    protected String updateByName; // 更新者名称

    @TableField(value = "updateDate", fill = FieldFill.INSERT_UPDATE)
    protected Date updateDate; // 更新日期

//	@TableField(value = "remarks")
//	protected String remarks; // 备注

    @TableField(value = "delFlag", fill = FieldFill.INSERT)
    protected String delFlag = "1"; // 删除标记（0：删除；1：正常 ）

    @TableField(exist = false)
    protected String actionType;//点击的保存还是提交等类型

    @TableField(exist = false)
    private String taskId;//流程定义id

    @TableField(exist = false)
    private String isAddIndex;

    @TableField(exist = false)
    private String xxjzMk;//消息机制linkurl
    @TableField(exist = false)
    private String cmsUserId;//网站过来的用户id信息

    public String getCmsUserId() {
        return cmsUserId;
    }

    public void setCmsUserId(String cmsUserId) {
        this.cmsUserId = cmsUserId;
    }

    public String getXxjzMk() {
        return xxjzMk;
    }

    public void setXxjzMk(String xxjzMk) {
        this.xxjzMk = xxjzMk;
    }

    public String getIsAddIndex() {
        return isAddIndex;
    }

    public void setIsAddIndex(String isAddIndex) {
        this.isAddIndex = isAddIndex;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public SuperEntity() {
        super();
    }


    public String getActionType() {
        return actionType;
    }


    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        SuperEntity that = (SuperEntity) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
