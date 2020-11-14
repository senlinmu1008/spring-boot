/**
 * Copyright (C), 2015-2019
 */
package com.zxb.mybatis.domain.bo;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2019-11-30 21:05
 */
@Data
public class BaseUserBO {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private String birthday;
    private String address;
    private String mobilePhone;
    private String telPhone;
    private String email;
    private String sex;
    private String type;
    private String status;
    private String description;
    private Date crtTime;
    private String crtUser;
    private String crtName;
    private String crtHost;
    private Date updTime;
    private String updUser;
    private String updName;
    private String updHost;

}