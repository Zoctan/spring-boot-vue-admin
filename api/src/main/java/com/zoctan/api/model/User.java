package com.zoctan.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
public class User {
    /**
     * 用户Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不能小于3")
    private String username;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 简介
     */
    private String resume;

    /**
     * 注册时间
     */
    @Column(name = "register_time")
    private Timestamp registerTime;

    /**
     * 上一次登录时间
     */
    @Column(name = "last_login_time")
    private Timestamp lastLoginTime;

    /**
     * 用户的角色
     */
    @Transient
    private String roleName;

    /**
     * 用户的角色对应的权限
     */
    @Transient
    private List<String> authList;
}