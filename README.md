# Spring Boot Vue Admin

## 简介

提供一套前后端分离的后台权限管理模版。

前端 Vue 模板来自 [vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)，其他功能可以根据该项目进行拓展。

后端 Spring Boot 模板来自[种子项目](https://github.com/Zoctan/spring-boot-api-seedling.git)，详细请看 [api](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api)。

## 预览

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/1.png"/>

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/2.png"/>

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/3.png"/>

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/4.png"/>

## Demo

在线 Demo：[暂无]()

## 依赖版本

前端依赖 | 版本
--------|------
node    | 10.5.0
npm     | 6.1.0

后端依赖    | 版本
-----------|------
SpringBoot | 2.0.2

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

欢迎小伙伴 star 和 issues ~ 谢谢:)

## 问题解决

### 前端依赖安装问题

如果出现以下错误，请单独安装 `npm install css-loader`，再安装项目依赖 `npm install`。

```bash
npm ERR! enoent ENOENT: no such file or directory, rename '/workspace/spring-boot-vue-admin/app/node_modules/.staging/css-loader-b931fe48/node_modules/ansi-styles' -> '/workspace/spring-boot-vue-admin/app/node_modules/.staging/ansi-styles-6535fafb'
```

## 更新日志

2018-06-10 由于 Redis 主要充当缓存数据库，但在该项目没起多大作用，故而移除 Redis。注意，如果需要在注销时使得 token 无效就需要搭配使用 Redis，可以自行根据种子项目进行添加。