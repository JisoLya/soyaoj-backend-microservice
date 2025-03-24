package com.liuyan.soyaojbackendjudgeservice.judge.sandbox;


import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeRequest;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    /**
     * 执行代码
     * @param request 执行代码的请求
     * @return
     */
    ExecuteCodeResponse execute(ExecuteCodeRequest request);
}
