/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.mybatis.service.impl;

import net.zhaoxiaobin.mybatis.dao.cluster.AuthClientServiceDao;
import net.zhaoxiaobin.mybatis.dao.master.BaseUserDao;
import net.zhaoxiaobin.mybatis.domain.bo.AuthClientServiceBO;
import net.zhaoxiaobin.mybatis.domain.bo.BaseUserBO;
import net.zhaoxiaobin.mybatis.service.MutilDatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author zhaoxb
 * @create 2019-12-01 19:42
 */
@Service("mutilDatasourceService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MutilDatasourceServiceImpl implements MutilDatasourceService {
    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private AuthClientServiceDao authClientServiceDao;

    @Override
    public void addUser(String userName, String password) {
        BaseUserBO baseUser = new BaseUserBO();
        baseUser.setUserName(userName);
        baseUser.setPassword(password);
        baseUser.setName(userName);
        baseUser.setBirthday("20191201");
        baseUser.setAddress("上海市");
        baseUser.setMobilePhone("13485527323");
        baseUser.setTelPhone("123456789");
        baseUser.setEmail("zhaoxb@163.com");
        baseUser.setSex("1");
        baseUser.setType("0");
        baseUser.setStatus("1");
        baseUser.setDescription("xiaoaojianghu");
        baseUser.setCrtUser("令狐冲");
        baseUser.setCrtName("令狐冲");
        baseUser.setCrtHost("127.0.0.1");
        baseUserDao.add(baseUser);
    }

    @Override
    public void updateUser(Long id, String status) {
        BaseUserBO userBO = baseUserDao.query(id);
        userBO.setStatus(status);
        userBO.setUpdUser("曲洋");
        userBO.setUpdName("曲洋");
        userBO.setUpdHost("127.0.0.1");
        baseUserDao.update(id, userBO);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseUserBO queryUser(Long id) {
        return baseUserDao.query(id);
    }



    @Override
    public void addAuthClient(String serviceId, String clientId, String desc) {
        AuthClientServiceBO authClientService = new AuthClientServiceBO();
        authClientService.setServiceId(serviceId);
        authClientService.setClientId(clientId);
        authClientService.setDescription(desc);
        authClientService.setCrtUser("admin");
        authClientService.setCrtName("admin");
        authClientService.setCrtHost("127.0.0.1");
        authClientServiceDao.add(authClientService);
    }

    @Override
    public void deleteAuthClient(Long id) {
        authClientServiceDao.delete(id);
    }

    @Override
    public void updateAuthClient(Long id, String serviceId, String desc) {
        AuthClientServiceBO authClientServiceBO = authClientServiceDao.query(id);
        authClientServiceBO.setServiceId(serviceId);
        authClientServiceBO.setDescription(desc);
        authClientServiceDao.update(authClientServiceBO);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthClientServiceBO queryAuthClient(Long id) {
        return authClientServiceDao.query(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthClientServiceBO> queryList(String serviceId) {
        return authClientServiceDao.queryList(serviceId);
    }

    @Override
    public int updateDescByServiceId(String desc) {
        List<String> serviceIdList = Arrays.asList(new String[]{"3", "4", "6"});
        return authClientServiceDao.updateDescByServiceId(desc, serviceIdList);
    }

    @Override
    public int batchSave() {
        List<AuthClientServiceBO> authClientServiceBOList = IntStream.range(1,100).mapToObj(i -> {
            AuthClientServiceBO authClientServiceBO = new AuthClientServiceBO();
            authClientServiceBO.setServiceId(StringUtils.rightPad(i + "", 4, "0"));
            authClientServiceBO.setClientId(StringUtils.leftPad(i + "", 4, "9"));
            authClientServiceBO.setDescription("批量入库");
            authClientServiceBO.setCrtUser("admin");
            authClientServiceBO.setCrtName("admin");
            authClientServiceBO.setCrtHost("127.0.0.1");
            return authClientServiceBO;
        }).collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        int count = authClientServiceDao.batchSave(authClientServiceBOList);
        long endTime = System.currentTimeMillis();
        log.info("=====耗时:[{}]ms=====", endTime - startTime);
        return count;
    }

}