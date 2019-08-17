package com.city.sprinbboot.database2code.com.xjs.common.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZTreeNode implements Serializable {
    private Object id; //节点id
    private Object pId;//父节点
    private boolean isParent = false;//是否有子节点,
    private String name;//节点名称
    private String orderCode;//排序
    private boolean checked;//是否被选中
    private boolean nocheck;//是否多选
    private String icon;//节点图标
    private boolean open;//是否展开节点
    private boolean isIsHidden = false;//是否隐藏
    private List<ZTreeNode> children = new ArrayList<ZTreeNode>();//子节点
    private boolean drag = false;//是否可拖动
    private boolean drop = false;//是否可以拖动到
    private boolean rightClick = false;//是否可以右击
    private Map<String, Object> extMap = new HashMap<String, Object>();//专有字段在这里设置
    private String title;//标题提示
    private String backup;
    private String backup1;

    public ZTreeNode(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public ZTreeNode(Object id, Object pId, String name, Boolean checked, Boolean open, Boolean nocheck) {
        this(id, pId, name, checked, open);
        this.nocheck = nocheck;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public ZTreeNode(Object id, Object pId, String name, Boolean checked, Boolean open) {
        this(id, pId, name, checked);
        this.open = open;
    }

    public ZTreeNode(Object id, Object pId, String name, Boolean checked) {
        this(id, pId, name);
        this.checked = checked;
    }

    public ZTreeNode(Object id, Object pId, String name) {
        this.id = id;
        this.pId = pId;
        this.title = name;
        if (name != null && name.length() > 8) {
            /*this.name  = name.substring(0,8) + "...";*/
            this.name = name;
        } else {
            this.name = name;
        }
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1;
    }

    public boolean isIsHidden() {
        return isIsHidden;
    }

    public void setIsHidden(boolean isIsHidden) {
        this.isIsHidden = isIsHidden;
    }


    public Object getpId() {
        return pId;
    }


    public void setpId(Object pId) {
        this.pId = pId;
    }


    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public Object getId() {
        return this.id;
    }

    public void setId(Object id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isHidden() {
        return this.isIsHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isIsHidden = isHidden;
    }

    public List<ZTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<ZTreeNode> children) {
        this.children = children;
    }

    public boolean isDrag() {
        return this.drag;
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    public boolean isDrop() {
        return this.drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public boolean getRightClick() {
        return this.rightClick;
    }

    public void setRightClick(boolean hasRightClick) {
        this.rightClick = hasRightClick;
    }

    public Map<String, Object> getExtMap() {
        return this.extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}