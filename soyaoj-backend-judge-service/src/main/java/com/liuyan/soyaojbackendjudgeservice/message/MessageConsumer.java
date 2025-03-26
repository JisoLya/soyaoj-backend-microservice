package com.liuyan.soyaojbackendjudgeservice.message;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.liuyan.model.codesandbox.ExecuteCodeResponse;
import com.liuyan.model.codesandbox.JudgeInfo;
import com.liuyan.model.dto.question.JudgeCase;
import com.liuyan.model.entity.Question;
import com.liuyan.model.entity.QuestionSubmit;
import com.liuyan.model.enums.JudgeStatusEnum;
import com.liuyan.soyaojbackendjudgeservice.judge.JudgeService;
import com.liuyan.soyaojbackendserviceclient.service.QuestionFeignClient;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MessageConsumer {

    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionFeignClient questionFeignClient;


    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive message: {}", message);
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @SneakyThrows
    @RabbitListener(queues = {"submit_queue"}, ackMode = "MANUAL")
    public void receiveSubmitUpdateMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive update message: {}", message);
        //执行实际的判题逻辑
        QuestionSubmit questionSubmit = new QuestionSubmit();
        JudgeStatusEnum judgeStatusEnum = null;
        judgeStatusEnum = JudgeStatusEnum.ACCEPTED;
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(message, ExecuteCodeResponse.class);
        Question question = questionFeignClient.getQuestionById(executeCodeResponse.getId());
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        List<String> outputList = executeCodeResponse.getOutput();
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
        }
        if (memory != null && memory > expectMemory) {
            judgeStatusEnum = JudgeStatusEnum.EXCEED_MEMORY_LIMIT;
        }
        //更新题目提交表
        questionSubmit.setId(executeCodeResponse.getQuestionSubmitId());
        questionSubmit.setQuestionId(executeCodeResponse.getQuestionSubmitId());
        questionSubmit.setStatus(2);
        String setJudgeInfo = JSONUtil.toJsonStr(executeCodeResponse.getJudgeInfo());
        questionSubmit.setJudgeInfo(setJudgeInfo);
        boolean b = questionFeignClient.updateQuestionSubmitById(questionSubmit);
        if (!b) {
            log.error("update question submit failed");
        }
        channel.basicAck(deliveryTag, false);
    }
}
