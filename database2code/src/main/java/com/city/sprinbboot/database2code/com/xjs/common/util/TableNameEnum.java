package com.city.sprinbboot.database2code.com.xjs.common.util;

public enum TableNameEnum {
    SYFW("SYFW", "z_syrk_syfw"),
    HJRK("HJRK", "z_syrk_hjrk"),
    LDRK("LDRK", "z_syrk_ldrk"),
    LSRY("LSRY", "z_syrk_lsry"),
    JWRY("JWRY", "z_syrk_jwry"),
    LYGL("LYGL", "z_syrk_lygl"),
    CZRK("CZRK", "z_syrk_czrK"),
    HJJT("HJJT", "z_syrk_hjjt"),
    LDZ("LDZ", "z_zzzz_ldz"),
    XMSFRY("XMSFRY", "z_tsrq_xmsfry"),
    SQJZRY("SQJZRY", "z_tsrq_sqjzry"),
    ZSZHDYZJSZAHZ("ZSZHDYZJSZAHZ", "z_tsrq_zszhdyzjszahz"),
    XDRY("XDRY", "z_tsrq_xdry"),
    AZBWXRY("AZBWXRY", "z_tsrq_azbwxry"),
    XX("XX", "z_xxjzbaq_xx"),
    ZDQSN("ZDQSN", "z_zdqsn_zdqsn"),
    FGYZJJZZ("FGYZJJZZ", "z_fgyzjjzzhshzz_fgyzjjzz");
    private String key;
    private String value;

    TableNameEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * desc:key获取value
     *
     * @param key
     * @return
     */
    public static String getValuByKey(String key) {
        for (TableNameEnum type : TableNameEnum.values()) {
            if (type.getKey().equals(key)) {
                return type.getValue();
            }
        }
        return "";
    }

    /**
     * desc:根据value获取key
     *
     * @param value
     * @return
     */
    public static String getKeyByValue(String value) {
        for (TableNameEnum type : TableNameEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getKey();
            }
        }
        return "";
    }

}





