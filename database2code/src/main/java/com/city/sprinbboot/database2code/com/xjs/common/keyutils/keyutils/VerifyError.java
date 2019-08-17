package com.city.sprinbboot.database2code.com.xjs.common.keyutils.keyutils;

public class VerifyError extends Error {
    private static final long serialVersionUID = 1L;

    public VerifyError() {
        super(
                "Verify fail ,the data maybe have been changed,or the verify key is incorrect");
    }
}