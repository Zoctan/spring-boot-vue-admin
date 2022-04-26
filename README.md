# Spring Boot Vue Admin

Provides a set of background permission management templates that separate the front and back ends.

![stars](https://img.shields.io/github/stars/Zoctan/spring-boot-vue-admin.svg?style=flat-square&label=Stars)
![license](https://img.shields.io/github/license/Zoctan/spring-boot-vue-admin.svg?style=flat-square)

English | [简体中文](./README-zh.md)

Front-end ideas reference ["Hand touch, take you to use vue to touch the background series II (login authority)"](https://juejin.im/post/591aa14f570c35006961acac), the template comes from [vue-element-admin](https: //github.com/PanJiaChen/vue-element-admin), other functions can be expanded according to this project.

Back-end ideas reference ["Role-Based Access Control New Solution"](http://globeeip.iteye.com/blog/1236167), the template comes from [spring-boot-api-seedling](https://github.com/Zoctan/spring-boot-api-seedling.git), please see the api's [README](https://github.com/Zoctan/spring-boot-vue-admin/tree/master/api) for design ideas.

Note: Since the front-end has not been updated for several years, there are loopholes and outdated versions. If necessary, please refer to the new project written in Vue3: [admin-vue3-template](https://github.com/Zoctan/admin-vue3-template)。

Welcome friends to star and issues ~ thank you :)

# Preview

![role list](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/1.png)

![role manage](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/2.png)

![user manage](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/3.png)

![user role manage](https://github.com/Zoctan/spring-boot-vue-admin/blob/master/README/4.png)

# Dependency version

frontend | version
--------|------
node    | 8.16.1
npm     | 6.4.1

backend    | version
-----------|------
SpringBoot | 2.1.6

# Quick start

```markdown
# clone project
git clone https://github.com/Zoctan/spring-boot-vue-admin.git

# go to project
cd spring-boot-vue-admin

# go to backend
cd api

# import database sql files (Remember to modify the database information)
sudo chmod a+x resetDB.sh && ./resetDB.sh

# start the backend ...

# go to frontend
cd app

# install dependency
npm install

# start the frontend ...
npm run dev
```

# Problem solve

## no such file/ansi-styles/css-loader

```bash
npm ERR! enoent ENOENT: no such file or directory, rename '/workspace/spring-boot-vue-admin/app/node_modules/.staging/css-loader-b931fe48/node_modules/ansi-styles' -> '/workspace/spring-boot-vue-admin/app/node_modules/.staging/ansi-styles-6535fafb'
```

please install css-loader firstly: `npm install css-loader`, and install project dependency secondly: `npm install`.

# Update log

2019-10-16 The webpack version is rolled back, and there is no time to fix the new version. Update the discovered issues and add services exactly according to the backend template [spring-boot-api-seedling](https://github.com/Zoctan/spring-boot-api-seedling.git).

~~2018-06-10 Redis is removed because Redis is mainly used as a cache database, but it does not play much role in this project. Note that if you need to make the token invalid during logout, you need to use Redis together, you can add it according to the backend template. ~~
