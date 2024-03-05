package com.example.zhishibeici.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicDto {
    private String openId;

    private String image;

    private String name;

    private String content;
}
