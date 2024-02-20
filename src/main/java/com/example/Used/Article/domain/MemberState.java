package com.example.Used.Article.domain;
import lombok.Getter;
@Getter
public enum MemberState {

    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    MemberState(String value) {
        this.value = value;
    }

    private String value;
}


