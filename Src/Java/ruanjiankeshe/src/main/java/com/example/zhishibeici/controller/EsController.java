package com.example.zhishibeici.controller;

import com.example.zhishibeici.Dto.CommentDto;
import com.example.zhishibeici.Dto.OpenIdDto;
import com.example.zhishibeici.Dto.RandomNumDto;
import com.example.zhishibeici.Dto.ResponseDto;
import com.example.zhishibeici.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping(value = "/es", produces = "application/json")
public class EsController {
    @Autowired
    private EsService esService;

    @RequestMapping(value = "/text", method = RequestMethod.POST)
    public ResponseDto<Object> text(@RequestBody RandomNumDto randomNumDto) {
        Map<String, String> content = esService.getText(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", content);
    }

    @RequestMapping(value = "/random_text", method = RequestMethod.POST)
    public ResponseDto<Object> randomText(@RequestBody RandomNumDto randomNumDto) {
        Set<Map<String, String>> content = esService.getRandomTexts(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", content);
    }

    @RequestMapping(value = "/sentence", method = RequestMethod.POST)
    public ResponseDto<Object> getSentence(@RequestBody RandomNumDto randomNumDto) {
        Map<String, String> content = esService.getSentence(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", content);
    }

    @RequestMapping(value = "/random_sentence", method = RequestMethod.POST)
    public ResponseDto<Object> randomSentence(@RequestBody RandomNumDto randomNumDto) {
        Set<Map<String, String>> content = esService.getRandomSentence(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", content);
    }

    @RequestMapping(value = "/text_comments", method = RequestMethod.POST)
    public ResponseDto<Object> getComments(@RequestBody RandomNumDto randomNumDto) {
        Map<Long, String> comments = esService.getTextComments(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", comments);
    }

    @RequestMapping(value = "/comments_on_text", method = RequestMethod.POST)
    public ResponseDto<Object> comments(@RequestBody CommentDto commentDto) {
        String res = esService.commentOnText(commentDto.getNum(), commentDto.getContent());
        return new ResponseDto<>("200", "success", res);
    }

    @RequestMapping(value = "/likes_text", method = RequestMethod.POST)
    public ResponseDto<Object> textLikes(@RequestBody RandomNumDto randomNumDto) {
        String likes = esService.likesOnText(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", likes);
    }

    @RequestMapping(value = "/likes_sentence", method = RequestMethod.POST)
    public ResponseDto<Object> sentenceLikes(@RequestBody RandomNumDto randomNumDto) {
        String likes = esService.likesOnSentence(randomNumDto.getNum());
        return new ResponseDto<>("200", "success", likes);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public ResponseDto<Object> init() {
        esService.initTextAndSentence();
        return new ResponseDto<>("200", "success", null);
    }
}
