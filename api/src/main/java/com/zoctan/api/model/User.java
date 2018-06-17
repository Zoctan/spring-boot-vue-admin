package com.zoctan.api.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
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
    @NotEmpty(message = "邮箱不能为空")
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
    @Column(name = "login_time")
    private Timestamp loginTime;

    /* ---------- 以下字段来自联表查询 ------------*/
    /**
     * 用户的角色Id
     */
    @Transient
    private Long roleId;

    /**
     * 用户的角色名
     */
    @Transient
    private String roleName;

    /**
     * 用户的角色对应的权限code
     */
    @Transient
    private List<String> permissionCodeList;


    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public String getResume() {
        return this.resume;
    }

    public void setResume(final String resume) {
        this.resume = resume;
    }

    public Timestamp getRegisterTime() {
        return this.registerTime;
    }

    public void setRegisterTime(final Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public List<String> getPermissionCodeList() {
        return this.permissionCodeList;
    }

    public void setPermissionCodeList(final List<String> permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}