package com.city.sprinbboot.database2code.core.common.constant;

public enum WorkFlowState {
    /**
     * 0:未提交
     * 1:已提交
     * 2:未启用
     * 3:已启用
     * 4:已停用
     * 5:未发布
     * 6:已发布
     * 7:未通过
     * 8:已通过
     * 9:审核中
     * 10:审批中
     * 11:流程完成
     */
    SAVE("0"), SUBMIT("1"), NOT_ENABLED("2"), ENABLED("3"),
    DEACTIVATED("4"), UNPUBLISHED("5"), PUBLISHED("6"), NOTTHROUGH("7"),
    ALREADYPASSED("8"), AUDIT("9"), APPROVAL("10"), END("11");

    private String value;

    private WorkFlowState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}