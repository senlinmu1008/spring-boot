package net.zhaoxiaobin.restful;

import cn.hutool.json.JSONUtil;
import net.zhaoxiaobin.restful.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RestfulPostTests {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试postForEntity
     * 带参数，如果用map传参，需要定义为 LinkedMultiValueMap 类型
     */
    @Test
    public void postForEntity() {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", 5);
        multiValueMap.add("name", "zhaoxb");
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://127.0.0.1:31000/postUser", multiValueMap, User.class);
        log.info("响应码:{}", responseEntity.getStatusCodeValue());
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }

    /**
     * 测试postForObject
     * 带参数，如果用map传参，需要定义为 LinkedMultiValueMap 类型
     */
    @Test
    public void postForObject() {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", 6);
        multiValueMap.add("name", "zhaoxb");
        User user = restTemplate.postForObject("http://127.0.0.1:31000/postUser", multiValueMap, User.class);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(user));
    }

    /**
     * 测试postForObject
     * 带有请求体
     */
    @Test
    public void postForObject2() {
        User reqUser = new User(10L, "zhaoxb");
        User user = restTemplate.postForObject("http://127.0.0.1:31000/postBody", reqUser, User.class);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(user));
    }

    /**
     * 测试exchange
     */
    @Test
    public void exchange() {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", 7);
        multiValueMap.add("name", "zhaoxb");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap);
//        HttpEntity<MultiValueMap<String, Object>> httpEntityWithHeaders = new HttpEntity<>(multiValueMap, new HttpHeaders());
        ResponseEntity<User> responseEntity = restTemplate.exchange("http://127.0.0.1:31000/postUser", HttpMethod.POST, httpEntity, User.class);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }
}
