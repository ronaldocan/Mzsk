package com.rona.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhengguican
 * @date 2021/11/01 16:20:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer age;

    private String name;

    private Integer level;

    public User(Integer age, String name) {
        this.age = age;
        this.name = name;
    }
}
