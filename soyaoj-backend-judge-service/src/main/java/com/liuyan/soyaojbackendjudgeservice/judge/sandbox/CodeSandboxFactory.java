package com.liuyan.soyaojbackendjudgeservice.judge.sandbox;


import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.Impl.ExampleSandbox;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.Impl.RemoteSandbox;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.Impl.ThirdPartySandbox;

public class CodeSandboxFactory {
    public static CodeSandbox getInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteSandbox();
            case "third-party":
                return new ThirdPartySandbox();
            default:
                return new ExampleSandbox();
        }
    }
}
