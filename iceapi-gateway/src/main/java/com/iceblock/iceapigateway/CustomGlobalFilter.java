package com.iceblock.iceapigateway;

import com.alibaba.nacos.shaded.com.google.errorprone.annotations.Var;
import com.ice.iceapicommon.model.entity.InterfaceInfo;
import com.ice.iceapicommon.model.entity.User;
import com.ice.iceapicommon.service.InnerInterfaceInfoService;
import com.ice.iceapicommon.service.InnerUserInterfaceInfoService;
import com.ice.iceapicommon.service.InnerUserService;
import com.iceblock.client.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @DubboReference
    private InnerUserService userService;

    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.用户发送请求到API网关
        // 2.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        String sourceAddress = request.getLocalAddress().getHostString();
        ServerHttpResponse response = exchange.getResponse();
        // 3.（黑白名单）
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }
        // 4.用户鉴权（判断 ak、sk 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        User invokeUser = null;
        try {
            invokeUser = userService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
//        if (!"ice".equals(accessKey)) {
//            return handleNoAuth(response);
//        }
        if (Long.parseLong(Objects.requireNonNull(nonce)) > 10000) {
            return handleNoAuth(response);
        }
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String secretSign = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(secretSign)) {
            return handleNoAuth(response);
        }
        // 5.请求的模拟接口是否存在以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //6. 是否还有调用次数
        if (!userInterfaceInfoService.getInvokeCountByUserId(invokeUser.getId())) {
            return handleNoCount(response);
        }
        // 6.请求转发，调用模拟接口 + 响应日志
        // Mono<Void> filter = chain.filter(exchange);
        // return filter;
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }



    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                        // 8.调用成功，接口调用次数 + 1
                                        try {
                                            userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        sb2.append("<--- {} {} \n");
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    })
                            );
                        } else {
                            // 9.调用失败，返回一个规范的错误码

                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleNoCount(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}
