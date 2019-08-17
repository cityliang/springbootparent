package com.city.sprinbboot.database2code.com.xjs.common.page;

import com.city.sprinbboot.database2code.com.xjs.common.util.StringUtil;

import java.io.Serializable;
import java.util.List;


public class PageResponse implements Serializable {
    private static final long serialVersionUID = 768549219446295665L;
    private long total;  //总条数
    private List records; //当前页显示数据

    private String parameter;//列表搜索框值
    private String search;//高级查询条件
    private String treeId;//树的id
    private String permission;//菜单id
    private String listType = "0";//流程节点类型区分，针对列表数据 默认为保存状态

    private Integer offset = 0;

    private Integer limit = 10;//每页多少条数

    private Integer pageNo;//页码

    private String sort;

    private String order;

    private String orgCode;//组织编码
    private String itemIds;//选择导出的记录id
    private String fieldIds;//选择导出的字段


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getItemIds() {
        return itemIds;
    }

    public void setItemIds(String itemIds) {
        this.itemIds = itemIds;
    }

    public String getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(String fieldIds) {
        this.fieldIds = fieldIds;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public Integer getPageNo() {
        this.pageNo = offset / limit + 1;
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSort() {
        return StringUtil.humpToLine2(sort);
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }
}