package net.zhaoxiaobin.restful.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author zhaoxb
 * @date 2020/10/05 10:06 上午
 */
@Configuration
public class RestConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(5000L))
                .setReadTimeout(Duration.ofMillis(30000L))
                .requestFactory(() -> clientHttpRequestFactory())
                .build();
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        //开始设置连接池
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(100);  //最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);  //同路由并发数
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);

        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接配置
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 设置超时，如果 RestTemplateBuilder 已经设置，这里就不需要设置
//        clientHttpRequestFactory.setConnectTimeout(5 * 1000); // 连接超时
//        clientHttpRequestFactory.setReadTimeout(30 * 1000); // 数据读取超时时间
        clientHttpRequestFactory.setConnectionRequestTimeout(30 * 1000); // 连接不够用时的等待时间
        return clientHttpRequestFactory;
    }
}