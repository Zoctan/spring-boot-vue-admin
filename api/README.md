# RESTful API

主要介绍后端 API 的角色权限控制。参考博文 [RBAC 新解](http://globeeip.iteye.com/blog/1236167)。

## 数据库设计

数据库主要包含[五张表](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api/src/test/resources/dev/sql)，分别是用户表 user、角色表 role、用户角色表 user_role、权限表 permission、角色权限表 role_permission。

数据库关系模型如下：

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/database.png"/>

user 表：用户信息。

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/user.png"/>

role 表：角色信息。

<img width="20%" height="20%" src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/role.png"/>

user_role 表：用户对应的角色，一对一。

<img width="20%" height="20%" src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/user_role.png"/>

permission 表：权限能操作的资源以及操作方式。

<img width="30%" height="30%" src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/permission.png"/>

role_permission 表：角色所对应的权限，一对多。

<img width="20%" height="20%" src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/api/README/role_permission.png"/>

> 为什么 ROLE_ADMIN 角色在数据库没有权限？
> ROLE_ADMIN 作为超级管理员这类角色，应该是具有所有权限的，但是对于数据库来说，没必要保存所有权限，只要在查询到该角色时返回所有权限即可。

## 角色权限控制

Spring Security + Json Web Token 鉴权：

最终效果，在控制器上的注解：

```java
@PreAuthorize("hasAuthority('user:list')")
```

实现思路：用户登录 -> 服务端生成 token -> 客户端保存 token，之后的每次请求都携带该 token，服务端认证解析。

所以在服务端认证解析的 token 就要保存有用户的角色和相应的权限：

```java
// service/impl/UserDetailsServiceImpl.java
// 为了方便，角色和权限都放在一起
// 角色名
authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
// 权限
for (final String permissionCode : user.getPermissionCodeList()) {
    authorities.add(new SimpleGrantedAuthority(permissionCode));
}
// [ROLE_TEST, role:list, user:list]
```

JWT 生成 token：

```java
// core/jwt/JwtUtil.java
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

Base64 解码 JWT 生成的 token：

```
{"alg":"RS256"}{"sub":"test","auth":"ROLE_TEST,role:list,user:list,"exp":1519742226}<wZJ69e,x	옮J܃a}
@ϋ+sˆvफ़t|Tq
|7uƙ
```

之后的控制器就可以使用 hasAuthority 和 hasRole 注解控制权限访问了：

```java
@PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('user:list')")
```

## axios 预请求和跨域

由于前后端分离，会出现跨域问题，参考[跨域资源共享 CORS 详解](http://www.ruanyifeng.com/blog/2016/04/cors)。

```java
// core/jwt/JwtAuthenticationFilter.java
// 解决跨域问题
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Credentials", "true");
response.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Length, Authorization, Accept, X-Requested-With");
// 明确允许通过的方法，不建议使用*
response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
response.setHeader("Access-Control-Max-Age", "3600");
response.setHeader("Access-Control-Expose-Headers", "*");
// axios 预请求后，直接返回
// 返回码必须为 200 否则视为请求失败
if (request.getMethod().equals("OPTIONS")) {
    return;
}
```