package com.example.zhishibeici.service.Impl;

import com.example.zhishibeici.po.WordDetails;
import com.example.zhishibeici.service.UserService;
import com.example.zhishibeici.utils.JwtUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JwtUtils jwt;

    @Autowired
    StringRedisTemplate stringRedisTemplate = null;

    @Override
    public Map<String, String> logIn(String code) throws Exception {
        String appid = "wx3c0bab4e394b7f31";
        String secret = "2642f89af11b6593da381140a6075a2d";
        String url = "https://api.weixin.qq.com/sns/jscode2session?";
        url += "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false).build();

        httpget.setConfig(requestConfig);
        response = httpClient.execute(httpget);
        HttpEntity responseEntity = response.getEntity();

        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println(res);
        }

        if (httpClient != null) {
            httpClient.close();
        }

        if (response != null) {
            response.close();
        }

        JSONObject jo = JSON.parseObject(res);
        String openid = jo.getString("openid");
        String session_key = jo.getString("session_key");
        Map<String, String> data = new HashMap<>();
        data.put("openid", openid);
        data.put("session_key", session_key);
        data.put("token", jwt.generateJwtToken(openid, session_key));
        return data;
    }

    @Override
    public double likesOnDynamic(String openId) {
        Boolean res = stringRedisTemplate.opsForSet().isMember("userLikesOrNot", openId);
        if (res != null && res == true) {
            return 0.0;
        }
        Double score = stringRedisTemplate.opsForZSet().score("userLikes", openId);
        if (score == null) {
            stringRedisTemplate.opsForZSet().add("userLikes", openId, 1.0);
        } else {
            stringRedisTemplate.opsForZSet().incrementScore("userLikes", openId, 1.0);
        }
        stringRedisTemplate.opsForSet().add("userLikesOrNot", openId);
        return stringRedisTemplate.opsForZSet().score("userLikes", openId);
    }

    @Override
    public Set<String> rank(int start, int end) {
        Set<String> ranks = stringRedisTemplate.opsForZSet().reverseRange("userLikes", start, end);
        return ranks;
    }

    @Override
    public Set<String> getUserDynamic(String openId) {
        Set<String> prevContent = stringRedisTemplate.opsForZSet().reverseRange(openId + "Dynamic", 0, -1);
        return prevContent;
    }

    @Override
    public String userDynamicPublish(String openId, String image, String name, String content) {
        Set<String> prevContent = stringRedisTemplate.opsForZSet().reverseRange(openId + "Dynamic", 0, 0);
        if (prevContent.size() == 0) {
            stringRedisTemplate.opsForZSet().add(openId + "Dynamic", content, 1.0);
            stringRedisTemplate.opsForList().rightPushAll(openId + "Info", openId, image, name);
        } else {
            double score = stringRedisTemplate.opsForZSet().score(openId + "Dynamic", prevContent.iterator().next());
            stringRedisTemplate.opsForZSet().add(openId + "Dynamic", content, score + 1.0);
        }

        prevContent = stringRedisTemplate.opsForZSet().reverseRange(openId + "allDynamic", 0, 0);
        if (prevContent.size() == 0) {
            stringRedisTemplate.opsForZSet().add("allDynamic", openId + "*" + content, 1.0);
        } else {
            double score = stringRedisTemplate.opsForZSet().score("allDynamic", prevContent.iterator().next());
            stringRedisTemplate.opsForZSet().add("allDynamic", openId + "*" + content, score + 1.0);
        }
        return "发布成功";
    }

    @Override
    public String userDynamicDelete(String openId, String content) {
        stringRedisTemplate.opsForZSet().remove(openId + "Dynamic", content);
        stringRedisTemplate.opsForZSet().remove("allDynamic", openId + "*" + content);
        return content;
    }

    @Override
    public Set<Map<List<String>, String>> randomDynamics(int start, int num) {
        Set<String> contents = stringRedisTemplate.opsForZSet().reverseRange("allDynamic", start, start + num - 1);
        Set<Map<List<String>, String>> retDynamics = new HashSet<>();
        for (String content : contents) {
            Map<List<String>, String> map = new HashMap<>();
            String[] strs = content.split("\\*");
            String realContent = "";
            String openId = strs[0];
            for (int i = 1; i < strs.length; i++) {
                realContent += strs[i];
            }
            List<String> info = new ArrayList<>();
            BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps(openId + "Info");
            long size = listOps.size();
            for (long idx = 0; idx < size; idx++) {
                info.add(listOps.index(idx));
            }
            map.put(info, realContent);
            retDynamics.add(map);
        }
        return retDynamics;
    }

    @Override
    public String writeSecret(String openId, String content) {
        Set<String> prevContent = stringRedisTemplate.opsForZSet().reverseRange(openId + "Secret", 0, 0);
        if (prevContent.size() == 0) {
            stringRedisTemplate.opsForZSet().add(openId + "Secret", content, 1.0);
        } else {
            double score = stringRedisTemplate.opsForZSet().score(openId + "Secret", prevContent.iterator().next());
            stringRedisTemplate.opsForZSet().add(openId + "Secret", content, score + 1.0);
        }
        return "发布成功";
    }

    @Override
    public String deleteSecret(String openId, String content) {
        stringRedisTemplate.opsForZSet().remove(openId + "Secret", content);
        return "删除成功";
    }

    @Override
    public Set<String> getSecret(String openId) {
        Set<String> prevContent = stringRedisTemplate.opsForZSet().reverseRange(openId + "Secret", 0, -1);
        return prevContent;
    }
}
