-- MySQL dump 10.16  Distrib 10.1.26-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: api_dev
-- ------------------------------------------------------
-- Server version	10.1.26-MariaDB-0+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auth`
--

DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限Id',
  `code` varchar(256) NOT NULL COMMENT '权限的代码/通配符,对应代码中@hasAuthority(xx)',
  `name` varchar(256) NOT NULL COMMENT '本权限的中文释义',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

--
-- Dumping data for table `auth`
--

LOCK TABLES `auth` WRITE;
/*!40000 ALTER TABLE `auth` DISABLE KEYS */;
INSERT INTO `auth` VALUES (1,'user:list','用户列表');
INSERT INTO `auth` VALUES (2,'user:add','用户新增');
INSERT INTO `auth` VALUES (3,'user:update','用户修改');
INSERT INTO `auth` VALUES (4,'user:delete','用户删除');
INSERT INTO `auth` VALUES (5,'role:list','角色列表');
INSERT INTO `auth` VALUES (6,'role:add','角色新增');
INSERT INTO `auth` VALUES (7,'role:update','角色修改');
INSERT INTO `auth` VALUES (8,'role:delete','角色删除');
INSERT INTO `auth` VALUES (9,'problem:add','题目新增');
INSERT INTO `auth` VALUES (10,'problem:update','题目修改');
INSERT INTO `auth` VALUES (11,'problem:delete','题目删除');
INSERT INTO `auth` VALUES (12,'contest:add','比赛新增');
INSERT INTO `auth` VALUES (13,'contest:update','比赛修改');
INSERT INTO `auth` VALUES (14,'contest:delete','比赛删除');
INSERT INTO `auth` VALUES (15,'notice:add','通知新增');
INSERT INTO `auth` VALUES (16,'notice:update','通知修改');
INSERT INTO `auth` VALUES (17,'notice:delete','通知删除');
INSERT INTO `auth` VALUES (18,'post:list','贴子列表');
INSERT INTO `auth` VALUES (19,'post:add','贴子新增');
INSERT INTO `auth` VALUES (20,'post:update','贴子修改');
INSERT INTO `auth` VALUES (21,'post:delete','贴子删除');
/*!40000 ALTER TABLE `auth` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-16 20:28:17
