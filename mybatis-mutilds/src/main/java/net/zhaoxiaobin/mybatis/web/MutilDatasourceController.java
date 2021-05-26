/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.mybatis.web;

import net.zhaoxiaobin.mybatis.domain.bo.AuthClientServiceBO;
import net.zhaoxiaobin.mybatis.domain.bo.BaseUserBO;
import net.zhaoxiaobin.mybatis.service.MutilDatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author zhaoxb
 * @create 2019-12-01 19:42
 */
@RestController
@Slf4j
@RequestMapping("/mutilDatasource")
public class MutilDatasourceController {
    @Autowired
    private MutilDatasourceService mutilDatasourceService;

    // 主库
    @PostMapping("/addUser")
    public String addUser(String userName, String password) {
        log.info("新增用户,姓名:[{}]", userName);
        mutilDatasourceService.addUser(userName, password);
        return "success";
    }

    @PostMapping("/updateUser")
    public String updateUser(Long id, String status) {
        log.info("用户id:[{}]修改状态:[{}]", id, status);
        mutilDatasourceService.updateUser(id, status);
        return "success";
    }

    @PostMapping("/queryUser")
    public BaseUserBO queryUser(Long id) {
        log.info("查询用户id:[{}]", id);
        return mutilDatasourceService.queryUser(id);
    }


    // 从库
    @PostMapping("/addAuthClient")
    public String addAuthClient(String serviceId, String clientId, String desc) {
        mutilDatasourceService.addAuthClient(serviceId, clientId, desc);
        return "success";
    }

    @PostMapping("/deleteAuthClient")
    public String deleteAuthClient(Long id) {
        mutilDatasourceService.deleteAuthClient(id);
        return "success";
    }

    @PostMapping("/updateAuthClient")
    public String updateAuthClient(Long id, String serviceId, String desc) {
        mutilDatasourceService.updateAuthClient(id, serviceId, desc);
        return "success";
    }

    @PostMapping("/queryAuthClient")
    public AuthClientServiceBO queryAuthClient(Long id) {
        return mutilDatasourceService.queryAuthClient(id);
    }

    @PostMapping("/queryList")
    public List queryList(String serviceId) {
        return mutilDatasourceService.queryList(serviceId);
    }

    @PostMapping("/updateDescByServiceId")
    public Integer updateDescByServiceId(String desc) {
        return mutilDatasourceService.updateDescByServiceId(desc);
    }

    @PostMapping("/batchSave")
    public Integer batchSave() {
        return mutilDatasourceService.batchSave();
    }
}