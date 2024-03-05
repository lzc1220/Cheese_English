package com.example.zhishibeici.service;

import com.example.zhishibeici.Dto.WordOpDto;
import com.example.zhishibeici.Dto.WordProficiencyDto;
import com.example.zhishibeici.Dto.WordSoloDto;
import com.example.zhishibeici.po.WordDetails;

import java.util.*;

public interface WordService {

    void collect(WordOpDto wordOpDto);

    void delete(WordOpDto wordOpDto);

    WordDetails select(WordOpDto wordOpDto);

    List<WordDetails> randomWord(String openId);

    Map<Long, WordDetails> getCollectedWords(String openId);

    void setWordProficiency(WordProficiencyDto wordProficiencyDto);

    String wordSpellingSolo(WordSoloDto wordSoloDto);

    void initWords();
}
