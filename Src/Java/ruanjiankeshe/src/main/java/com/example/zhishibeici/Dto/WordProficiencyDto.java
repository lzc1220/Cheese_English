package com.example.zhishibeici.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordProficiencyDto {
    private String openId;

    private String wordContent;

    private Double proficiency;
}
