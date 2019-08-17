package com.city.sprinbboot.database2code.com.xjs.common.page;

import java.io.Serializable;


public class CustomPageResponse extends PageResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3774606696657645458L;

    private String datBaseEvtName;//选择导出的字段

    public String getDatBaseEvtName() {
        return datBaseEvtName;
    }

    public void setDatBaseEvtName(String datBaseEvtName) {
        this.datBaseEvtName = datBaseEvtName;
    }


}