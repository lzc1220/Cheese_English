package com.example.zhishibeici.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class WordSoloDto {
    private String flag;

    private String openId;

    private Integer count;

    private String user;
}
