package com.rona.can.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengguican
 * @description
 * @date 2021/11/21 15:29
 */
@RestController
public class InfluxController {

    @RequestMapping("/sql/execute")
    public void executeSql(String sql) {

    }
}