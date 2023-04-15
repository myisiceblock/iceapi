package com.iceblock.iceapiinterface.controller;

import com.iceblock.iceapiinterface.model.User;
import com.iceblock.iceapiinterface.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 API
 *
 * @author iceblock
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name,HttpServletRequest request) {
        System.out.println(request.getHeader("ice"));
        return "Get 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String body = request.getHeader("body");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        if(!accessKey.equals("ice")) {
//            throw new RuntimeException("无权限");
//        }
//        if(Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        Long currentTime = System.currentTimeMillis() / 1000;
//        Long FIVE_MINUTES = 60 * 5L;
//        if((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
//            throw new RuntimeException("无权限");
//        }
//        String secretKey = SignUtils.getSign(body, "abcdefgh");
//        if(!sign.equals(secretKey)) {
//            throw new RuntimeException("无权限");
//        }
        String result =  "Post 用户名字是" + user.getUserName();

        return result;
    }
}
