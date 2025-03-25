package com.liuyan.model.dto.question;

import lombok.Data;

@Data
public class JudgeCase {
    /**
     * 判题用例输入
     */
    private String input;
    /**
     * 输出
     */
    private String output;
}
