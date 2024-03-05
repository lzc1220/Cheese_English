package com.example.zhishibeici.controller;

import com.example.zhishibeici.Dto.*;
import com.example.zhishibeici.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/log_in", method = RequestMethod.POST)
    public ResponseDto<Object> logIn(@RequestBody LogInDto logInDto) throws Exception {
        Map<String, String> data = userService.logIn(logInDto.getCode());
        return new ResponseDto<>("200", "success", data);
    }

    @RequestMapping(value = "/likes_dynamic", method = RequestMethod.POST)
    public ResponseDto<Object> likes(@RequestBody OpenIdDto openIdDto) {
        int res = (int) userService.likesOnDynamic(openIdDto.getOpenId());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/rank", method = RequestMethod.POST)
    public ResponseDto<Object> rank(@RequestBody RankDto rankDto) {
        Set<String> rankUsers = userService.rank(rankDto.getStart(), rankDto.getEnd());
        return new ResponseDto<>("200", "success", rankUsers);
    }

    @RequestMapping(value = "/pub_dynamic", method = RequestMethod.POST)
    public ResponseDto<Object> userDynamicPublish (@RequestBody DynamicDto dynamicDto) {
        String res = userService.userDynamicPublish(dynamicDto.getOpenId(), dynamicDto.getImage(), dynamicDto.getName(), dynamicDto.getContent());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/delete_dynamic", method = RequestMethod.POST)
    public ResponseDto<Object> userDynamicDelete (@RequestBody SecretDto dynamicDto) {
        String res = userService.userDynamicDelete(dynamicDto.getOpenId(), dynamicDto.getContent());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/dynamic", method = RequestMethod.POST)
    public ResponseDto<Object> getRandomDynamic (@RequestBody OpenIdDto openIdDto) {
        Set<String> res = userService.getUserDynamic(openIdDto.getOpenId());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/random_dynamic", method = RequestMethod.POST)
    public ResponseDto<Object> getRandomDynamic (@RequestBody RandomDynamicDto randomDynamicDto) {
        Set<Map<List<String>, String>> res = userService.randomDynamics(randomDynamicDto.getStart(), randomDynamicDto.getNum());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public ResponseDto<Object> getSecret (@RequestBody OpenIdDto openIdDto) {
        Set<String> res = userService.getSecret(openIdDto.getOpenId());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/pub_secret", method = RequestMethod.POST)
    public ResponseDto<Object> userSecretPublish (@RequestBody SecretDto secretDto) {
        String res = userService.writeSecret(secretDto.getOpenId(), secretDto.getContent());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/delete_secret", method = RequestMethod.POST)
    public ResponseDto<Object> userSecretDelete (@RequestBody SecretDto secretDto) {
        String res = userService.deleteSecret(secretDto.getOpenId(), secretDto.getContent());
        return new ResponseDto<>("200", "success", res);
    }
}
