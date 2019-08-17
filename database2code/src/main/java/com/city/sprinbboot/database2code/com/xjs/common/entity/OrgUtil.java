package com.city.sprinbboot.database2code.com.xjs.common.entity;

public class OrgUtil {
    /**
     * 根据编码返回组织级别，共分为七种
     *
     * @param orgCodeDefault
     * @return
     */
    public static String getOrgRank(String orgCodeDefault) {
        String orgRank = "0";
        if (null != orgCodeDefault && !"".equals(orgCodeDefault)) {
            int size = orgCodeDefault.length();
            switch (size) {
                case 2:
                    orgRank = "1";
                    break;
                case 4:
                    orgRank = "2";
                    break;
                case 6:
                    orgRank = "3";
                    break;
                case 9:
                    orgRank = "4";
                    break;
                case 12:
                    orgRank = "5";
                    break;
                case 15:
                    orgRank = "6";
                    break;
                default:
                    orgRank = "7";//其它
                    break;
            }
        }
        return orgRank;
    }

    /**
     * 根据编码返回组织级别，如国家级，省级，市级，县
     *
     * @param orgCodeDefault
     * @return
     */
    public static String getOrgType(String orgCodeDefault) {
        String orgRank = "国家级";
        if (null != orgCodeDefault && !"".equals(orgCodeDefault)) {
            int size = orgCodeDefault.length();
            switch (size) {
                case 2:
                    orgRank = "省级";
                    break;
                case 4:
                    orgRank = "地市级";
                    break;
                case 6:
                    orgRank = "区县级";
                    break;
                case 9:
                    orgRank = "乡镇级";
                    break;
                case 12:
                    orgRank = "社区级";
                    break;
                case 15:
                    orgRank = "村极";
                    break;
                default:
                    orgRank = "国家级";
                    break;
            }
        }
        return orgRank;
    }
}
