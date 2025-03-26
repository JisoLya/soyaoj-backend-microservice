package com.liuyan.soyaojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.liuyan.model.vo.QuestionSubmitVO;
import com.liuyan.soyaojbackendserviceclient.service.QuestionFeignClient;
import com.liuyan.common.ErrorCode;
import com.liuyan.exception.BusinessException;
import com.liuyan.model.codesandbox.ExecuteCodeRequest;
import com.liuyan.model.codesandbox.ExecuteCodeResponse;
import com.liuyan.model.codesandbox.JudgeInfo;
import com.liuyan.model.dto.question.JudgeCase;
import com.liuyan.model.entity.Question;
import com.liuyan.model.entity.QuestionSubmit;
import com.liuyan.model.enums.JudgeStatusEnum;
import com.liuyan.model.enums.QuestionSubmitStatusEnum;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandbox;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandboxFactory;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandboxProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignClient questionFeignClient;


    @Value("${sandbox.type}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1. 获取题目信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        //2. 获取当前题目的状态
        if (Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.RUNNING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已在判题");
        }

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        //修改为判题中直接可以返回了
        boolean b = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更改题目状态为判题中失败！");
        }
        //3. 调用代码沙箱
        CodeSandbox sandbox = CodeSandboxFactory.getInstance(type);
        sandbox = new CodeSandboxProxy(sandbox);

        String judgeCaseStr = question.getJudgeCase();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        //创建提交信息
        ExecuteCodeRequest codeRequest = new ExecuteCodeRequest();
        codeRequest.setQuestionSubmitId(questionSubmitId);
        codeRequest.setId(questionId);
        codeRequest.setLanguage(language);
        codeRequest.setCode(code);
        codeRequest.setInputList(inputList);
        //执行沙箱
        //修改为向消息队列中发送消息来获取响应
        sandbox.execute(codeRequest);
        //这里返回也没有实际用到，因此返回null即可
        return null;
    }
}
