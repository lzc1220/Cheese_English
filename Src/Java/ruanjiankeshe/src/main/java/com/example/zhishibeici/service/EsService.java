package com.example.zhishibeici.service;

import java.util.Map;
import java.util.Set;

public interface EsService {
    Map<String, String> getText(int num);

    Set<Map<String, String>> getRandomTexts(int num);

    Map<String, String> getSentence(int num);

    Set<Map<String, String>> getRandomSentence(int num);

    Map<Long, String> getTextComments(int num);

    String commentOnText(int num, String commentContent);

    String likesOnText(int num);

    String likesOnSentence(int num);

    void initTextAndSentence();

}
