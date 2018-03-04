# RESTful API

本篇主要介绍后端 API 的搭建思路，以及角色权限的控制。参考博文[RBAC新解](http://globeeip.iteye.com/blog/1236167)。

## 数据库设计

数据库主要包含[五张表](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/src/test/resources/dev/sql)，分别是用户表 user、角色表 role、用户角色表 user_role、权限表 permission、角色权限表 role_permission。

数据库关系模型如下：

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/database.png"/>

user 表：用户信息。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/user.png"/>

role 表：角色信息。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/role.png"/>

user_role 表：用户对应的角色，一对一。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/user_role.png"/>

permission 表：权限能操作的资源以及操作方式。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/permission.png"/>

role_permission 表：角色所对应的权限，一对多。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/README/role_permission.png"/>

## SpringBoot 角色权限控制

Spring Security + Json Web Token 鉴权：

```java UserDetailsServiceImpl.java
// 角色
authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
// 权限
for (final String permissionCode : user.getPermissionCodeList()) {
    authorities.add(new SimpleGrantedAuthority(permissionCode));
}
```

```java JwtUtil.java
Jwts.builder()
        // 设置用户名
        .setSubject(username)
        // 添加权限属性
        .claim(this.AUTHORITIES_KEY, authorities)
        // 设置失效时间
        .setExpiration(date)
        // 私钥加密生成签名
        .signWith(SignatureAlgorithm.RS256, privateKey)
        .compact();
```