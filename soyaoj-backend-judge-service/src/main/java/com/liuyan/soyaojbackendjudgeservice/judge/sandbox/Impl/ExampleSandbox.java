package com.liuyan.soyaojbackendjudgeservice.judge.sandbox.Impl;


import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeRequest;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeResponse;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandbox;

/**
 * 示例代码沙箱
 */
public class ExampleSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {
        System.out.println("ExampleCodeSandbox");
        return new ExecuteCodeResponse();
    }
}
