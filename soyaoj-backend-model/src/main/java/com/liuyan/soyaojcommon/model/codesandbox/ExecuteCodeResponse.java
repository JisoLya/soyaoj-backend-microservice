package com.liuyan.soyaojcommon.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    private JudgeInfo judgeInfo;

    /**
     * 程序输出
     */
    private List<String> output;

    /**
     * 执行的堆栈输出
     */
    private String stackInfo;
}
