# Spring Boot Vue Admin

![stars](https://img.shields.io/github/stars/Zoctan/spring-boot-vue-admin.svg?style=flat-square&label=Stars)
![license](https://img.shields.io/github/license/Zoctan/spring-boot-vue-admin.svg?style=flat-square)

## 简介

提供一套前后端分离的后台权限管理模版。

前端思路参考[《手摸手，带你用vue撸后台 系列二(登录权限篇)》](https://juejin.im/post/591aa14f570c35006961acac)，模板来自 [vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)，其他功能可以根据该项目进行拓展。

后端思路参考[《Role-Based Access Control 新解》](http://globeeip.iteye.com/blog/1236167)，模板来自 [spring-boot-api-seedling](https://github.com/Zoctan/spring-boot-api-seedling.git)，设计思路请看 api 的 [README](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api)。

## 预览

![权限列表](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/1.png)

![角色管理](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/2.png)

![用户管理](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/3.png)

![用户角色控制](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/4.png)

## Demo

在线 Demo：[暂无]()

## 依赖版本

前端依赖 | 版本
--------|------
node    | 8.16.1
npm     | 6.4.1

后端依赖    | 版本
-----------|------
SpringBoot | 2.1.6

其他依赖版本没有测试，请自行测试，如有问题请提 issues。

## 快速开始

```markdown
# 克隆项目
git clone https://github.com/Zoctan/spring-boot-vue-admin.git

# 进入项目
cd spring-boot-vue-admin

# 后端
cd api

# 导入数据库文件（记得修改数据库信息）
sudo chmod a+x resetDB.sh && ./resetDB.sh

# 启动后端服务...

# 前端
cd app

# 安装依赖
npm install

# 启动前端服务
npm run dev
```

欢迎小伙伴 star 和 issues ~ 谢谢 :）

## 问题解决

### no such file/ansi-styles/css-loader

如果出现以下错误，请先单独安装 `npm install css-loader`，再安装项目依赖 `npm install`。

```bash
npm ERR! enoent ENOENT: no such file or directory, rename '/workspace/spring-boot-vue-admin/app/node_modules/.staging/css-loader-b931fe48/node_modules/ansi-styles' -> '/workspace/spring-boot-vue-admin/app/node_modules/.staging/ansi-styles-6535fafb'
```

## 更新日志

2019-10-16 回退 webpack 版本，暂时没时间修复新版问题。更新已发现的问题，完全按照后端模板 [spring-boot-api-seedling](https://github.com/Zoctan/spring-boot-api-seedling.git) 添加业务。

~~2018-06-10 由于 Redis 主要充当缓存数据库，但在该项目没起多大作用，故而移除 Redis。注意，如果需要在注销时使得 token 无效就需要搭配使用 Redis，可以自行根据后端模板进行添加。~~
