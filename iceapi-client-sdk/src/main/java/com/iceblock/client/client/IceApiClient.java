package com.iceblock.client.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.iceblock.client.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.iceblock.client.utils.SignUtils.getSign;

/**
 * 调用第三方接口客户端
 *
 * @author iceblock
 */
public class IceApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public IceApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/get", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/post", paramMap);
        System.out.println(result);
        return result;
    }

    public Map<String, String> getHeaders(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
//        map.put("secretKey","abcdefgh");
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", body);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", getSign(body, secretKey));
        return map;
    }


    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();
        System.out.println(response.getStatus());
        String body = response.body();
        System.out.println(body);
        return body;
    }
}
