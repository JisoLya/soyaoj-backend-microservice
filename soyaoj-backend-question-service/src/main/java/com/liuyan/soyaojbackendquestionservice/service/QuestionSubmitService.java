package com.liuyan.soyaojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liuyan.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liuyan.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liuyan.model.entity.QuestionSubmit;
import com.liuyan.model.entity.User;
import com.liuyan.model.vo.QuestionSubmitVO;


/**
* @author liuyan
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-01-01 11:44:46
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录的ID
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);


    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitVOPage
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitVOPage,User loginUser);
}
