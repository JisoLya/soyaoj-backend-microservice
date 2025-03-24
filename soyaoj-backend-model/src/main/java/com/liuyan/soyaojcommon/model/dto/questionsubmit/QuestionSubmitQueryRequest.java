package com.liuyan.soyaojcommon.model.dto.questionsubmit;


import com.liuyan.soyaojcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询提交题目的记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 代码语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
