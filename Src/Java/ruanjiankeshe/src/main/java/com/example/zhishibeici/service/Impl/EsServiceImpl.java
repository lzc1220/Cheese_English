package com.example.zhishibeici.service.Impl;

import com.example.zhishibeici.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class EsServiceImpl implements EsService {

    @Autowired
    StringRedisTemplate stringRedisTemplate = null;

    @Override
    public Map<String, String> getText(int num) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("texts");
        String content = hashOps.get(num + "");
        String[] strs = content.split("\\*");
        Map<String, String> res = new HashMap<>();
        res.put("num", num + "");
        res.put("title", strs[0]);
        res.put("abstract", strs[1]);
        res.put("content", strs[2]);
        return res;
    }

    @Override
    public Set<Map<String, String>> getRandomTexts(int num) {
        Set<Map<String, String>> ret = new HashSet<>();
        int cnt = 1;
        int randomIdx = new Random().nextInt(6);
        while (randomIdx == num || cnt <= 3) {
            randomIdx = new Random().nextInt(6);
            if (randomIdx != num) {
                cnt++;
                ret.add(getText(randomIdx));
            }
        }
        return ret;
    }

    @Override
    public Map<String, String> getSentence(int num) {
        Map<String, String> map = new HashMap<>();
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("sentences");
        map.put("num", num + "");
        map.put("content", hashOps.get(num + ""));
        return map;
    }

    @Override
    public Set<Map<String, String>> getRandomSentence(int num) {
        int randomIdx = new Random().nextInt(10);
        Set<Map<String, String>> ret = new HashSet<>();
        int cnt = 1;
        while (randomIdx == num || cnt <= 3) {
            randomIdx = new Random().nextInt(10);
            if (randomIdx != num) {
                ret.add(getSentence(randomIdx));
                cnt++;
            }
        }
        return ret;
    }

    @Override
    public Map<Long, String> getTextComments(int num) {
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps("textsComments" + num);
        Map<Long, String> ret = new HashMap<>();
        long size = listOps.size();
        for (long idx = 0; idx < size; idx++) {
            String comment = listOps.index(idx);
            ret.put(idx, comment);
        }
        return ret;
    }

    @Override
    public String commentOnText(int num, String commentContent) {
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps("textsComments" + num);
        listOps.rightPush(commentContent);
        return "评论成功!";
    }

    @Override
    public String likesOnText(int num) {
        return setLikes("textsLikes", num);
    }

    @Override
    public String likesOnSentence(int num) {
        return setLikes("sentencesLikes", num);
    }

    private String setLikes(String hashName, int num) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(hashName);
        String likes = hashOps.get(num + "");
        if (likes == null) {
            hashOps.put(num + "", "1");
            return "1";
        } else {
            int nextLikes = Integer.parseInt(likes) + 1;
            hashOps.put(num + "", nextLikes + "");
            return nextLikes + "";
        }
    }

    @Override
    public void initTextAndSentence() {
        String docDir = System.getProperty("user.dir") + "/text";
        for (int i = 0; i < 6; i++) {
            String textDir = docDir + "/" + i + ".txt";
            File text = new File(textDir);
            try {
                BufferedReader tr = new BufferedReader(new InputStreamReader(new FileInputStream(text)));
                StringBuilder sb = new StringBuilder();
                sb.append(tr.readLine()).append("*");
                sb.append(tr.readLine()).append("*");
                sb.append(tr.readLine());
                BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("texts");
                hashOps.put(i + "", sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            int j = i + 6;
            String sentenceDir = docDir + "/" + j + ".txt";
            File sentence = new File(sentenceDir);
            String sentenceContent = null;
            try {
                BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(sentence)));
                sentenceContent = sr.readLine();
                StringBuilder sb = new StringBuilder();
                while (sentenceContent != null) {
                    sb.append(sentenceContent).append("\n");
                    sentenceContent = sr.readLine();
                }
                BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("sentences");
                hashOps.put(i + "", sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
