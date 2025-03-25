package com.liuyan.soyaojbackendjudgeservice.judge;


import com.liuyan.soyaojcommon.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);

}
