# Spring Boot Vue Admin

## 简介

提供一套前后端分离的后台权限管理模版。

前端 Vue 模板来自 [vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)，其他功能可以根据该项目进行拓展。

后端 Spring Boot 模板来自[种子项目](https://github.com/Zoctan/spring-boot-api-seedling.git)，详细请看 [api](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api)。

# 预览

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/1.png"/>

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/2.png"/>

<img src="https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/3.png"/>

# Demo

在线 Demo：[暂无]()

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

# 前端
cd vue

# 使用其他源，解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 安装依赖
npm install

# 启动服务
npm run dev
```

欢迎小伙伴 star 和 issues ~ 谢谢:)

# 搭建后台服务

明确目标：后台需要什么功能，要解决哪些问题？

在本模版中，

# 搭建前台服务