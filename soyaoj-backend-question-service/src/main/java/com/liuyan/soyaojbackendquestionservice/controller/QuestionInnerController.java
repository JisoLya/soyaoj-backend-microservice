package com.liuyan.soyaojbackendquestionservice.controller;

import com.liuyan.soyaojbackendquestionservice.service.QuestionService;
import com.liuyan.soyaojbackendquestionservice.service.QuestionSubmitService;
import com.liuyan.soyaojbackendserviceclient.service.QuestionFeignClient;
import com.liuyan.soyaojcommon.model.entity.Question;
import com.liuyan.soyaojcommon.model.entity.QuestionSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
