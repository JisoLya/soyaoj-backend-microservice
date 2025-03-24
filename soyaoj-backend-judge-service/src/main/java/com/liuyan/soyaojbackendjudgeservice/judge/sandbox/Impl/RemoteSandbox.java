package com.liuyan.soyaojbackendjudgeservice.judge.sandbox.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.liuyan.soyaojcommon.common.ErrorCode;
import com.liuyan.soyaojcommon.exception.BusinessException;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeRequest;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeResponse;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandbox;


public class RemoteSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {
        String url = "http://192.168.121.129:8080/executeCode";
        String requestJson = JSONUtil.toJsonStr(request);
        String response = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(requestJson)
                .execute()
                .body();
        if (StrUtil.isBlankIfStr(response)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "execute remote codeSandbox error");
        }
        return JSONUtil.toBean(response, ExecuteCodeResponse.class);
    }
}
