package com.controller;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ming.li
 * @date 2023/10/17 20:23
 */
@RestController
public class UserController {

    @RequestMapping("user")
    public String user(){
        return "user";
    }
}
