package com.example.Used.Article.controller;

import com.example.Used.Article.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {
    private Long id;

    private String name;
    private String detail;
    private int price;
    private int item_count;
    private Member user;
    private String imgPath;
    private String imgName;

}
