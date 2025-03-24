package com.liuyan.soyaojbackendjudgeservice.judge;


import com.liuyan.soyaojcommon.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmitVO doJudge(long questionSubmitId);

}
