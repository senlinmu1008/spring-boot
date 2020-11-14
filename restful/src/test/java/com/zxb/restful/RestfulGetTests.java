package com.zxb.restful;

import cn.hutool.json.JSONUtil;
import com.zxb.restful.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RestfulGetTests {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试getForEntity
     * 不带参数
     */
    @Test
    public void getForEntity1() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:31000/getUser1", String.class);
        log.info("响应码:{}", responseEntity.getStatusCodeValue());
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }

    /**
     * 测试getForEntity
     * 带参数，用占位符方式
     */
    @Test
    public void getForEntity2() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:31000/getUser2?id={1}&name={2}", String.class, 2, "zhaoxb");
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }

    /**
     * 测试getForEntity
     * 带参数，用map传参
     */
    @Test
    public void getForEntity3() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 3);
        map.put("name", "zhaoxb");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:31000/getUser2?id={id}&name={name}", String.class, map);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }

    /**
     * 测试getForEntity
     * 不带参数，返回对象
     */
    @Test
    public void getForEntity4() {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("http://127.0.0.1:31000/getUser1", User.class);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(responseEntity.getBody()));
    }

    /**
     * 测试getForObject
     * 不带参数
     */
    @Test
    public void getForObject() {
        User User = restTemplate.getForObject("http://127.0.0.1:31000/getUser1", User.class);
        log.info("响应体:{}", JSONUtil.toJsonPrettyStr(User));
    }
}
