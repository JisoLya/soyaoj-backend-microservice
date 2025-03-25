package com.liuyan.soyaojbackendjudgeservice.controller;

import com.liuyan.soyaojbackendjudgeservice.judge.JudgeService;
import com.liuyan.soyaojbackendserviceclient.service.JudgeFeignClient;
import com.liuyan.soyaojcommon.model.entity.QuestionSubmit;
import com.liuyan.soyaojcommon.model.vo.QuestionSubmitVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit duJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
