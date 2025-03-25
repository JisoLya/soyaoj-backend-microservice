package com.liuyan.soyaojbackenduserservice.controller;

import com.liuyan.soyaojbackendserviceclient.service.UserFeignClient;
import com.liuyan.soyaojbackenduserservice.service.UserService;
import com.liuyan.soyaojcommon.model.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource

    private UserService userService;

    @Override
    @GetMapping("/get/id")
    public User getById(long userId) {
        return userService.getById(userId);
    }

    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(Collection<Long> idList) {
        return userService.listByIds(idList);
    }

}
