package com.liuyan.soyaojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.liuyan.soyaojbackendserviceclient.service.QuestionFeignClient;
import com.liuyan.soyaojcommon.common.ErrorCode;
import com.liuyan.soyaojcommon.exception.BusinessException;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeRequest;
import com.liuyan.soyaojcommon.model.codesandbox.ExecuteCodeResponse;
import com.liuyan.soyaojcommon.model.codesandbox.JudgeInfo;
import com.liuyan.soyaojcommon.model.dto.question.JudgeCase;
import com.liuyan.soyaojcommon.model.entity.Question;
import com.liuyan.soyaojcommon.model.entity.QuestionSubmit;
import com.liuyan.soyaojcommon.model.enums.JudgeStatusEnum;
import com.liuyan.soyaojcommon.model.enums.QuestionSubmitStatusEnum;
import com.liuyan.soyaojcommon.model.vo.QuestionSubmitVO;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandbox;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandboxFactory;
import com.liuyan.soyaojbackendjudgeservice.judge.sandbox.CodeSandboxProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        codeRequest.setLanguage(language);
        codeRequest.setCode(code);
        codeRequest.setInputList(inputList);
        //执行沙箱
        ExecuteCodeResponse executeCodeResponse = sandbox.execute(codeRequest);

        //判断信息是否正确
        List<String> outputList = executeCodeResponse.getOutput();
        JudgeStatusEnum judgeStatusEnum = JudgeStatusEnum.WAITING;

        if (outputList.size() != inputList.size()) {
            judgeStatusEnum = JudgeStatusEnum.WRONG_ANSWER;
        }
        //依次判断每项预期输出
        for (int i = 0; i < judgeCases.size(); i++) {
            if (!judgeCases.get(i).equals(outputList.get(i))) {
                judgeStatusEnum = JudgeStatusEnum.WRONG_ANSWER;
                break;
            }
        }
        judgeStatusEnum = JudgeStatusEnum.ACCEPTED;

        //拿到响应结果,进一步判断内存与时间
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        Long time = judgeInfo.getTimeLimit();
        Long memory = judgeInfo.getMemoryLimit();
        //预期的判题结果
        String judgeConfigStr = question.getJudgeConfig();
        JudgeInfo expectedRes = JSONUtil.toBean(judgeConfigStr, JudgeInfo.class);
        Long expectMemory = expectedRes.getMemoryLimit();
        Long expectedTime = expectedRes.getTimeLimit();

        if (time != null && time > expectedTime) {
            judgeStatusEnum = JudgeStatusEnum.EXCEED_TIME_LIMIT;
            return null;
        }
        if (memory != null && memory > expectMemory) {
            judgeStatusEnum = JudgeStatusEnum.EXCEED_MEMORY_LIMIT;
            return null;
        }

        //更新题目提交表
        QuestionSubmit submit = new QuestionSubmit();
        submit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        submit.setLanguage(language);
        submit.setCode(code);
        submit.setId(questionSubmitId);
        submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        boolean update = questionFeignClient.updateQuestionSubmitById(submit);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
//        QuestionSubmitVO submitVO = new QuestionSubmitVO();
//        submitVO.setId(questionSubmitId);
//        submitVO.setLanguage(language);
//        submitVO.setCode(code);
//        submitVO.setStatus(judgeInfo.getSuccess() ? QuestionSubmitStatusEnum.SUCCEED.getText() : QuestionSubmitStatusEnum.FAILED.getText());
        return submit;
    }
}
