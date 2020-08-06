package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class CommonError {

    private String commonError;

    public CommonError(){}
    public CommonError(String commonError) {
        this.commonError = commonError;
    }
}
