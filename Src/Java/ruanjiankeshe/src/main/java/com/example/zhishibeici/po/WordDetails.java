package com.example.zhishibeici.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDetails {
    private String wordContent;

    private String pronunciation;

    private Set<String> meaning;

    private Double proficiency;
}
