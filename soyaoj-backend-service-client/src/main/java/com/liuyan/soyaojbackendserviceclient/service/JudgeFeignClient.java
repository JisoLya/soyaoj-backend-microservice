package com.liuyan.soyaojbackendserviceclient.service;


import com.liuyan.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "soyaoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    @PostMapping("/do")
    QuestionSubmit duJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
