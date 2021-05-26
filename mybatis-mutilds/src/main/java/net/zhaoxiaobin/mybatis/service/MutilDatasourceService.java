/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.mybatis.service;

import net.zhaoxiaobin.mybatis.domain.bo.AuthClientServiceBO;
import net.zhaoxiaobin.mybatis.domain.bo.BaseUserBO;

import java.util.List;

/**
 *
 * @author zhaoxb
 * @create 2019-12-01 19:42
 */
public interface MutilDatasourceService {
    void addUser(String userName, String password);
    void updateUser(Long id, String status);
    BaseUserBO queryUser(Long id);

    void addAuthClient(String serviceId, String clientId, String desc);
    void deleteAuthClient(Long id);
    void updateAuthClient(Long id, String serviceId, String desc);
    AuthClientServiceBO queryAuthClient(Long id);
    List<AuthClientServiceBO> queryList(String serviceId);
    int updateDescByServiceId(String desc);
    int batchSave();
}
