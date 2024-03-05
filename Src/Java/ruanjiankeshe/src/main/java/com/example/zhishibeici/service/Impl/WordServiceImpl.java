package com.example.zhishibeici.service.Impl;

import com.example.zhishibeici.Dto.WordOpDto;
import com.example.zhishibeici.Dto.WordProficiencyDto;
import com.example.zhishibeici.Dto.WordSoloDto;
import com.example.zhishibeici.po.WordDetails;
import com.example.zhishibeici.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;


@Service
public class WordServiceImpl implements WordService {

    @Autowired
    StringRedisTemplate stringRedisTemplate = null;

    @Override
    public void collect(WordOpDto wordOpDto) {
        String openId = wordOpDto.getOpenId();
        String word = wordOpDto.getWordContent();
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps(openId + "List");
        listOps.rightPush(word);
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(openId + "Hash");
        hashOps.put(word, "0.0");
    }

    @Override
    public void delete(WordOpDto wordOpDto) {
        String openId = wordOpDto.getOpenId();
        String word = wordOpDto.getWordContent();
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps(openId + "List");
        listOps.remove(1, word);
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(openId + "Hash");
        hashOps.delete(word);
    }

    @Override
    public WordDetails select(WordOpDto wordOpDto) {
        String openId = wordOpDto.getOpenId();
        int idx = wordOpDto.getIdx();
        String word = null;
        System.out.println(idx);
        if (idx != -1) {
            BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps(openId + "List");
            word = listOps.index(idx);
            return getWordDetails(word, getWordProficiency(openId, word));
        } else {
            word = wordOpDto.getWordContent();
            return getWordDetails(word, 0.0);
        }
    }

    @Override
    public List<WordDetails> randomWord(String openId) {
        BoundSetOperations<String, String> setOps = stringRedisTemplate.boundSetOps("words");
        List<WordDetails> ret = new ArrayList<>();
        Set<String> words = setOps.distinctRandomMembers(5);
        for (String word : words) {
            ret.add(getWordDetails(word, getWordProficiency(openId, word)));
        }
        return ret;
    }

    @Override
    public Map<Long, WordDetails> getCollectedWords(String openId) {
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps(openId + "List");
        Map<Long, WordDetails> ret = new HashMap<>();
        long size = listOps.size();
        for (long idx = 0; idx < size; idx++) {
            String word = listOps.index(idx);
            ret.put(idx, getWordDetails(word, getWordProficiency(openId, word)));
        }
        return ret;
    }

    @Override
    public void setWordProficiency(WordProficiencyDto wordProficiencyDto) {
        String openId = wordProficiencyDto.getOpenId();
        String word = wordProficiencyDto.getWordContent();
        String proficiency = wordProficiencyDto.getProficiency() + "";
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(openId + "Hash");
        hashOps.put(word, proficiency);
    }

    @Override
    public String wordSpellingSolo(WordSoloDto wordSoloDto) {
        String openId = wordSoloDto.getOpenId();
        Integer cnt = wordSoloDto.getCount();
        String flag = wordSoloDto.getFlag();
        String user = wordSoloDto.getUser();
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(openId + "Solo");
        if (flag.equals("done")) {
            Integer aNum = Integer.parseInt(hashOps.get("A"));
            Integer bNum = Integer.parseInt(hashOps.get("B"));
            hashOps.delete("A", "B");
            if (aNum > bNum) {
                return "user A wins!";
            } else {
                return "user B wins!";
            }
        } else {
            if (user.equals("A")) {
                hashOps.put("A", cnt + "");
                String res = hashOps.get("B");
                if (res == null) {
                    return "-1";
                }
                return res;
            } else {
                hashOps.put("B", cnt + "");
                String res = hashOps.get("A");
                if (res == null) {
                    return "-1";
                }
                return res;
            }
        }
    }

    @Override
    public void initWords() {
        String docDir = System.getProperty("user.dir") + "/text";
        String wordDir = docDir + "/word.txt";
        String pronunciationDir = docDir + "/accent.txt";
        String meaningDir = docDir + "/mean.txt";
        File words = new File(wordDir);
        File pronunciations = new File(pronunciationDir);
        File meanings = new File(meaningDir);
        String word = null;
        String pronunciation = null;
        String meaning = null;
        try {
            BufferedReader wr = new BufferedReader(new InputStreamReader(new FileInputStream(words)));
            BufferedReader pr = new BufferedReader(new InputStreamReader(new FileInputStream(pronunciations)));
            BufferedReader mr = new BufferedReader(new InputStreamReader(new FileInputStream(meanings)));
            word = wr.readLine();
            pronunciation = pr.readLine();
            meaning = mr.readLine();
            while (word != null) {
                Map<String, String> map = new HashMap<>();
                map.put("pronunciation", pronunciation);
                map.put("meaning", meaning);
                stringRedisTemplate.opsForHash().putAll(word, map);
                stringRedisTemplate.opsForSet().add("words", word);
                word = wr.readLine();
                pronunciation = pr.readLine();
                meaning = mr.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(word);
        }
    }
    private WordDetails getWordDetails(String word, double proficiency) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(word);
        String pronunciation = hashOps.get("pronunciation");
        String meanings = hashOps.get("meaning");
        Set<String> set = new HashSet<>();
        for (String meaning : meanings.split("\\*")) {
            set.add(meaning);
        }
        WordDetails wordDetails = WordDetails.builder().wordContent(word)
                .pronunciation(pronunciation)
                .meaning(set)
                .proficiency(proficiency)
                .build();
        return wordDetails;
    }

    private double getWordProficiency(String openId, String word) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(openId + "Hash");
        String proficiency = hashOps.get(word);
        if (proficiency == null) {
            return 0.0;
        } else {
            return Double.parseDouble(hashOps.get(word));
        }
    }

}
