package com.gerry.yitao.common.entity;

import java.util.HashMap;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/14 14:00
 * @Description: 公共响应类
 */
public class CommonResponse extends HashMap<String, Object> {

    private static final String MESSAGE = "message";

    private static final String SUCCESS = "success";

    private static final String DATA = "data";

    private static final String REDIRECT = "redirect";

    private static final String EMPTY = "";

    public boolean isSuccess() {
        return get(SUCCESS) != null && (Boolean) get(SUCCESS);
    }

    public String getMessage() {
        if (get(MESSAGE) != null) {
            return (String) get(MESSAGE);
        }
        return EMPTY;
    }

    private CommonResponse() {
        super();
        this.put(SUCCESS, false);
    }

    public CommonResponse success() {
        this.put(SUCCESS, true);
        return this;
    }

    public CommonResponse success(String message) {
        this.put(SUCCESS, true);
        this.put(MESSAGE, message);
        return this;
    }

    public CommonResponse fail(String message) {
        this.put(SUCCESS, false);
        this.put(MESSAGE, message);
        return this;
    }

    public CommonResponse redirect(String url) {
        this.put(REDIRECT, url);
        return this;
    }

    public CommonResponse setData(Object data) {
        return putData(DATA, data);
    }

    public CommonResponse putData(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public static CommonResponse createCommonResponse() {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        return commonResponse;
    }

    public static CommonResponse createCommonResponse(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        commonResponse.setData(data);
        return commonResponse;
    }
}

