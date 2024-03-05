package com.example.zhishibeici.controller;

import com.example.zhishibeici.Dto.ResponseDto;
import com.example.zhishibeici.Dto.WordOpDto;
import com.example.zhishibeici.Dto.WordProficiencyDto;
import com.example.zhishibeici.Dto.WordSoloDto;
import com.example.zhishibeici.po.WordDetails;
import com.example.zhishibeici.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/word", produces = "application/json")
public class WordController {
    @Autowired
    private WordService wordService;

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public ResponseDto<Object> select(@RequestBody WordOpDto wordOpDto) {
        WordDetails wordDetails = wordService.select(wordOpDto);
        return new ResponseDto<>("200", "success", wordDetails);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public ResponseDto<Object> collect(@RequestBody WordOpDto wordOpDto) {
        wordService.collect(wordOpDto);
        return new ResponseDto<>("200", "success", null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDto<Object> delete(@RequestBody WordOpDto wordOpDto) {
        wordService.delete(wordOpDto);
        return new ResponseDto<>("200", "success", null);
    }


    @RequestMapping(value = "/random", method = RequestMethod.POST)
    public ResponseDto<Object> random(@RequestBody WordOpDto wordOpDto) {
        List<WordDetails> randomWords = wordService.randomWord(wordOpDto.getOpenId());
        return new ResponseDto<>("200", "success", randomWords);
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseDto<Object> review(@RequestBody WordOpDto wordOpDto) {
        Map<Long, WordDetails> words = wordService.getCollectedWords(wordOpDto.getOpenId());
        return new ResponseDto<>("200", "success", words);
    }

    @RequestMapping(value = "/set_proficiency", method = RequestMethod.POST)
    public ResponseDto<Object> setProficiency(@RequestBody WordProficiencyDto wordProficiencyDto) {
        wordService.setWordProficiency(wordProficiencyDto);
        return new ResponseDto<>("200", "success", null);
    }

    @RequestMapping(value = "/solo1", method = RequestMethod.POST)
    public ResponseDto<Object> solo1(@RequestBody WordSoloDto wordSoloDto) {
        String res = wordService.wordSpellingSolo(wordSoloDto);
        Map<String, String> data = new HashMap<>();
        String flag = wordSoloDto.getFlag();
        if (flag.equals("done")) {
            data.put("res", res);
        } else {
            if (wordSoloDto.getCount() > 0) {
                data.put("count_B", res);
            } else {
                data.put("count_A", res);
            }
        }

        return new ResponseDto<>("200", "success", data);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public ResponseDto<Object> initWords() {
        wordService.initWords();
        return new ResponseDto<>("200", "success", null);
    }

}
