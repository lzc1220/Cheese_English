package com.example.zhishibeici.service;

import java.util.*;

public interface UserService {

    Map<String, String> logIn(String code) throws Exception;

    double likesOnDynamic(String openId);

    Set<String> rank(int start, int end);

    Set<String> getUserDynamic(String openId);

    String userDynamicPublish(String openId, String image, String name, String content);

    String userDynamicDelete(String openId, String content);

    Set<Map<List<String>, String>> randomDynamics(int start, int num);

    String writeSecret(String openId, String content);

    String deleteSecret(String openId, String content);

    Set<String> getSecret(String openId);

}
