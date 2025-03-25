package com.liuyan.soyaojbackendjudgeservice.judge;


import com.liuyan.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);

}
