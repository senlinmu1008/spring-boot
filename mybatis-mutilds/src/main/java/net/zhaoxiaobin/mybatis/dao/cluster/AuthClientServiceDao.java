/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.mybatis.dao.cluster;

import net.zhaoxiaobin.mybatis.domain.bo.AuthClientServiceBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author zhaoxb
 * @create 2019-12-01 19:26
 */
public interface AuthClientServiceDao {
    void add(AuthClientServiceBO authClientServiceBO);

    void delete(Long id);

    void update(AuthClientServiceBO authClientServiceBO);

    AuthClientServiceBO query(Long id);

    List<AuthClientServiceBO> queryList(String serviceId);

    int updateDescByServiceId(@Param("desc") String desc, @Param("serviceIdList") List<String> serviceIdList);

    int batchSave(@Param("authClientServiceBOList") List<AuthClientServiceBO> authClientServiceBOList);

}
