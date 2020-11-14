/**
 * Copyright (C), 2015-2019
 */
package com.zxb.mybatis.dao.master;

import com.zxb.mybatis.domain.bo.BaseUserBO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhaoxb
 * @create 2019-12-01 19:26
 */
public interface BaseUserDao {
    void add(BaseUserBO baseUserBO);

    void update(@Param("id") Long id, @Param("bo") BaseUserBO baseUserBO);

    BaseUserBO query(Long id);
}
