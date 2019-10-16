-- MySQL dump 10.16  Distrib 10.1.26-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: admin_dev
-- ------------------------------------------------------
-- Server version	10.1.26-MariaDB-0+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`       bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限Id',
    `resource` varchar(256)        NOT NULL COMMENT '权限对应的资源',
    `code`     varchar(256)        NOT NULL COMMENT '权限的代码/通配符,对应代码中@hasAuthority(xx)',
    `handle`   varchar(256)        NOT NULL COMMENT '对应的资源操作',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (1, '账号', 'account:list', '列表');
INSERT INTO `permission`
VALUES (2, '账号', 'account:detail', '详情');
INSERT INTO `permission`
VALUES (3, '账号', 'account:add', '添加');
INSERT INTO `permission`
VALUES (4, '账号', 'account:update', '更新');
INSERT INTO `permission`
VALUES (5, '账号', 'account:delete', '删除');
INSERT INTO `permission`
VALUES (6, '账号', 'account:search', '搜索');
INSERT INTO `permission`
VALUES (7, '角色', 'role:list', '列表');
INSERT INTO `permission`
VALUES (8, '角色', 'role:detail', '详情');
INSERT INTO `permission`
VALUES (9, '角色', 'role:add', '添加');
INSERT INTO `permission`
VALUES (10, '角色', 'role:update', '更新');
INSERT INTO `permission`
VALUES (11, '角色', 'role:delete', '删除');
INSERT INTO `permission`
VALUES (12, '角色', 'role:search', '搜索');
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2018-02-16 20:28:17
