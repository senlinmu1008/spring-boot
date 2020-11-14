/**
 * Copyright (C), 2015-2019
 */
package com.zxb.mybatis.domain.bo;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2019-11-30 20:52
 */
@Data
public class AuthClientServiceBO {
    private Long id;
    private String serviceId;
    private String clientId;
    private String description;
    private Date crtTime;
    private String crtUser;
    private String crtName;
    private String crtHost;
}