-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: lebabel
-- ------------------------------------------------------
-- Server version	5.5.5-10.6.17-MariaDB-log

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
-- Table structure for table `comment_closure`
--

DROP TABLE IF EXISTS `comment_closure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_closure` (
  `ancestor` bigint(20) NOT NULL,
  `descendant` bigint(20) NOT NULL,
  `depth` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ancestor`,`descendant`),
  KEY `comment_closure_tb_post_comment_comment_id_fk_2` (`descendant`),
  CONSTRAINT `comment_closure_tb_post_comment_comment_id_fk` FOREIGN KEY (`ancestor`) REFERENCES `tb_post_comment` (`comment_id`),
  CONSTRAINT `comment_closure_tb_post_comment_comment_id_fk_2` FOREIGN KEY (`descendant`) REFERENCES `tb_post_comment` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_closure`
--

LOCK TABLES `comment_closure` WRITE;
/*!40000 ALTER TABLE `comment_closure` DISABLE KEYS */;
INSERT INTO `comment_closure` VALUES (48,79,2),(48,86,1),(48,87,1),(55,79,1),(80,80,0),(80,81,1),(80,82,2),(80,83,2),(80,84,1),(81,81,0),(81,82,1),(81,83,1),(82,82,0),(83,83,0),(84,84,0),(85,85,0),(85,89,1),(86,86,0),(87,87,0),(88,88,0),(89,89,0),(90,90,0);
/*!40000 ALTER TABLE `comment_closure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_like`
--

DROP TABLE IF EXISTS `comment_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_like` (
  `comment_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`comment_id`),
  KEY `comment_like_tb_post_comment_comment_id_fk` (`comment_id`),
  CONSTRAINT `comment_like_tb_post_comment_comment_id_fk` FOREIGN KEY (`comment_id`) REFERENCES `tb_post_comment` (`comment_id`),
  CONSTRAINT `comment_like_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_like`
--

LOCK TABLES `comment_like` WRITE;
/*!40000 ALTER TABLE `comment_like` DISABLE KEYS */;
INSERT INTO `comment_like` VALUES (32,175),(37,175),(37,223),(83,223);
/*!40000 ALTER TABLE `comment_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_like` (
  `post_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`post_id`),
  KEY `post_like_tb_post_post_id_fk` (`post_id`),
  CONSTRAINT `post_like_tb_post_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`post_id`),
  CONSTRAINT `post_like_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

LOCK TABLES `post_like` WRITE;
/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
INSERT INTO `post_like` VALUES (17,135),(17,136),(17,137),(17,175),(4,223),(7,223),(8,223),(10,223),(13,223),(17,223);
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `user_id` bigint(20) NOT NULL,
  `subscribed_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`subscribed_user_id`),
  KEY `subscription_tb_user_user_id_fk_2` (`subscribed_user_id`),
  CONSTRAINT `subscription_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `subscription_tb_user_user_id_fk_2` FOREIGN KEY (`subscribed_user_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `prevent_self_subscribe` CHECK (`user_id` <> `subscribed_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='user subscription';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
INSERT INTO `subscription` VALUES (126,130),(126,132),(126,133),(126,134),(126,135),(126,136),(130,126),(132,126),(134,135),(134,136),(135,126);
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_category_member`
--

DROP TABLE IF EXISTS `tb_category_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_category_member` (
  `category_id` int(11) NOT NULL COMMENT '板块外键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户外键id',
  `role` enum('GUEST','SUBSCRIBER','MODERATOR','ADMIN') NOT NULL DEFAULT 'GUEST',
  `experience` int(11) NOT NULL DEFAULT 0 COMMENT '用户经验值',
  `title` varchar(15) DEFAULT NULL COMMENT '用户头衔',
  `mute_expiration_date` datetime DEFAULT from_unixtime(0) COMMENT '禁言时限',
  `join_date` datetime NOT NULL DEFAULT curtime(),
  PRIMARY KEY (`category_id`,`user_id`),
  KEY `tb_category_member_tb_user_user_id_fk` (`user_id`),
  CONSTRAINT `tb_category_member_tb_post_category_category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `tb_post_category` (`category_id`),
  CONSTRAINT `tb_category_member_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块成员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_category_member`
--

LOCK TABLES `tb_category_member` WRITE;
/*!40000 ALTER TABLE `tb_category_member` DISABLE KEYS */;
INSERT INTO `tb_category_member` VALUES (1,2,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(1,4,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(1,130,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-09 01:25:47'),(1,223,'SUBSCRIBER',102,NULL,'1970-01-01 00:00:00','2025-04-17 13:23:55'),(2,2,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,4,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,130,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,132,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,133,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,134,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,135,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,136,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,137,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-08 22:37:20'),(2,223,'SUBSCRIBER',27,NULL,'1970-01-01 00:00:00','2025-04-16 11:50:58'),(3,130,'SUBSCRIBER',0,NULL,'1970-01-01 08:00:00','2025-03-09 01:25:47'),(4,130,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-09 01:25:47'),(4,223,'SUBSCRIBER',9,NULL,'1970-01-01 00:00:00','2025-04-15 02:49:15'),(5,130,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-09 01:25:47'),(5,223,'SUBSCRIBER',3,NULL,'1970-01-01 00:00:00','2025-04-17 13:23:57'),(6,130,'GUEST',0,NULL,'1970-01-01 08:00:00','2025-03-09 01:25:47'),(6,133,'SUBSCRIBER',0,NULL,'1970-01-01 00:00:00','2025-03-08 22:37:20'),(6,136,'SUBSCRIBER',0,NULL,'1970-01-01 00:00:00','2025-03-08 22:37:20');
/*!40000 ALTER TABLE `tb_category_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_chat_channel`
--

DROP TABLE IF EXISTS `tb_chat_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_chat_channel` (
  `channel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '频道主键id',
  `category_id` int(11) NOT NULL COMMENT '频道所在板块外键id',
  `channel_name` varchar(16) NOT NULL COMMENT '频道名',
  PRIMARY KEY (`channel_id`),
  KEY `tb_chat_channel_tb_post_category_category_id_fk` (`category_id`),
  CONSTRAINT `tb_chat_channel_tb_post_category_category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `tb_post_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天频道';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_chat_channel`
--

LOCK TABLES `tb_chat_channel` WRITE;
/*!40000 ALTER TABLE `tb_chat_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_chat_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_chat_message_private`
--

DROP TABLE IF EXISTS `tb_chat_message_private`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_chat_message_private` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息主键id',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者id',
  `message_text` tinytext NOT NULL,
  `reply_to` bigint(20) DEFAULT NULL COMMENT '回复某个消息',
  `target_id` bigint(20) NOT NULL COMMENT '接收者id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`message_id`),
  KEY `tb_chat_message_private_tb_chat_message_private_message_id_fk` (`reply_to`),
  KEY `tb_chat_message_private_tb_user_user_id_fk` (`sender_id`),
  KEY `tb_chat_message_private_tb_user_user_id_fk_2` (`target_id`),
  CONSTRAINT `tb_chat_message_private_tb_chat_message_private_message_id_fk` FOREIGN KEY (`reply_to`) REFERENCES `tb_chat_message_private` (`message_id`),
  CONSTRAINT `tb_chat_message_private_tb_user_user_id_fk` FOREIGN KEY (`sender_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `tb_chat_message_private_tb_user_user_id_fk_2` FOREIGN KEY (`target_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `tb_chat_message_private_target_id` CHECK (`target_id` <> `sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私聊信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_chat_message_private`
--

LOCK TABLES `tb_chat_message_private` WRITE;
/*!40000 ALTER TABLE `tb_chat_message_private` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_chat_message_private` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_chat_message_public`
--

DROP TABLE IF EXISTS `tb_chat_message_public`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_chat_message_public` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息主键id',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者外键id',
  `message_text` tinytext NOT NULL,
  `reply_to` bigint(20) DEFAULT NULL COMMENT '回复消息外键id',
  `target_id` bigint(20) NOT NULL COMMENT '目标频道外键id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`message_id`),
  KEY `tb_chat_message_public_tb_chat_channel_channel_id_fk` (`target_id`),
  KEY `tb_chat_message_public_tb_chat_message_public_message_id_fk` (`reply_to`),
  KEY `tb_chat_message_public_tb_user_user_id_fk` (`sender_id`),
  CONSTRAINT `tb_chat_message_public_tb_chat_channel_channel_id_fk` FOREIGN KEY (`target_id`) REFERENCES `tb_chat_channel` (`channel_id`),
  CONSTRAINT `tb_chat_message_public_tb_chat_message_public_message_id_fk` FOREIGN KEY (`reply_to`) REFERENCES `tb_chat_message_public` (`message_id`),
  CONSTRAINT `tb_chat_message_public_tb_user_user_id_fk` FOREIGN KEY (`sender_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `tb_chat_message_public_target_id` CHECK (`target_id` <> `sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='群聊消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_chat_message_public`
--

LOCK TABLES `tb_chat_message_public` WRITE;
/*!40000 ALTER TABLE `tb_chat_message_public` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_chat_message_public` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_post`
--

DROP TABLE IF EXISTS `tb_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子主键id',
  `publish_user_id` bigint(20) NOT NULL COMMENT '发布者id',
  `post_title` varchar(192) NOT NULL COMMENT '帖子标题',
  `post_content` mediumtext NOT NULL COMMENT '帖子内容',
  `post_category_id` int(11) NOT NULL COMMENT '帖子分类id',
  `post_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0-未审核 1-审核通过 2-审核失败 (默认审核通过)',
  `post_views` bigint(20) NOT NULL DEFAULT 0 COMMENT '阅读量',
  `last_update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '最新修改时间',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `post_tags` tinytext NOT NULL DEFAULT '[]',
  PRIMARY KEY (`post_id`) USING BTREE,
  KEY `tb_post_tb_post_category_category_id_fk` (`post_category_id`),
  KEY `tb_post_tb_user_user_id_fk` (`publish_user_id`),
  CONSTRAINT `tb_post_tb_post_category_category_id_fk` FOREIGN KEY (`post_category_id`) REFERENCES `tb_post_category` (`category_id`),
  CONSTRAINT `tb_post_tb_user_user_id_fk` FOREIGN KEY (`publish_user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_post`
--

LOCK TABLES `tb_post` WRITE;
/*!40000 ALTER TABLE `tb_post` DISABLE KEYS */;
INSERT INTO `tb_post` VALUES (2,1,'6 年前，只会 JSP 和 Servlet 就可以找到工作','<p>沧海桑田，欢迎大家讨论~</p>',4,1,29,'2024-08-09 14:54:49','2024-08-09 14:54:49','[]'),(3,2,'spring-boot框架学起来容易吗？','<p>如题~</p>',1,1,245,'2024-08-09 15:58:30','2024-08-09 15:58:30','[]'),(4,2,'Java知识点归纳(Java基础部分)','<pre><code class=\"Bash\"><xmp>原文地址：https://bbs.csdn.net/topics/392168597</xmp></code></pre><p>写这篇文章的目的是想总结一下自己这么多年来使用java的一些心得体会，主要是和一些java基础知识点相关的，所以也希望能分享给刚刚入门的Java程序员和打算入Java开发这个行当的准新手们，希望可以给大家一些经验，能让大家更好学习和使用Java。<br/>　　这次介绍的主要内容是和J2SE相关的部分，另外，会在以后再介绍些J2EE相关的、和Java中各个框架相关的内容。<br/>　　经过这么多年的Java开发，以及结合平时面试Java开发者的一些经验，我觉得对于J2SE方面主要就是要掌握以下的一些内容。<br/><br/>　　1. JVM相关(包括了各个版本的特性)<br/>　　对于刚刚接触Java的人来说，JVM相关的知识不一定需要理解很深，对此里面的概念有一些简单的了解即可。不过对于一个有着3年以上Java经验的资深开发者来说，不会JVM几乎是不可接受的。<br/>　　JVM作为java运行的基础，很难相信对于JVM一点都不了解的人可以把java语言吃得很透。我在面试有超过3年Java经验的开发者的时候， JVM几乎就是一个必问的问题了。当然JVM不是唯一决定技术能力好坏的面试问题，但是可以佐证java开发能力的高低。<br/>　　在JVM这个大类中，我认为需要掌握的知识有：<br/>JVM内存模型和结构<br/>GC原理，性能调优<br/>调优：Thread Dump， 分析内存结构<br/>class 二进制字节码结构， class loader 体系 ， class加载过程 ， 实例创建过程<br/>方法执行过程<br/>Java各个大版本更新提供的新特性(需要简单了解)<br/><br/>　　2. Java的运行（基础必备）<br/>　　这条可能出看很简单，java程序的运行谁不会呢？不过很多时候， 我们只是单纯通过IDE去执行java程序，底层IDE又是如何执行java程序呢？很多人并不了解。<br/>　　这个知识点是最最基本的java开发者需要掌握的，初学java，第一个肯定是教你如何在命令行中执行java程序，但是很多人一旦把java学完了，IDE用上了，就把这个都忘了。为什么强调要知道这个呢，知道了java最纯粹的启动方式之后，你才能在启动出问题的时候，去分析当时启动的目录多少，执行命名如何，参数如何，是否有缺失等。 这样有利于你真正开发中去解决那些奇奇怪怪的可能和环境相关的问题。<br/>　　在这里需要掌握的知识有：<br/>javac 编译java文件为 class 文件<br/>java 命令的使用， 带package的java类如何在命令行中启动<br/>java程序涉及到的各个路径(classpath， java。library。path， java运行的主目录等)<br/><br/>　　3. 数据类型<br/>　　这条没有什么好多说的，无非就是Java中的基本类型和对象类型的掌握。可以再了解一些JDK如何自动转换方面的知识，包括装箱拆箱等，还要注意避免装箱之后的类型相等的判断<br/>　　主要知识点：<br/>基本类型： int， long， float， double， boolean ， 。。。<br/>对应的对象类型： Integer 等类型到基本类型的转换， 装箱和拆箱<br/>Object类型： equals， hashcode<br/>String 类型的特点<br/><br/>　　4. 对象和实例，对象的创建<br/>　　在这方面，开发者需要了解class和instance的概念以及之间的差别， 这是java面向对象特性的一个基础。主要知识点有：<br/>Class和 Instance 的概念<br/>Instance 创建的过程：<br/>　　1。 无继承：分配内存空间， 初始化变量， 调用构造函数<br/>　　2。 有继承：处理静态动作， 分配内存空间， 变量定义为初始值 ， 从基类-&gt;子类， 处理定义处的初始化， 执行构造方法<br/>　　需要注意的点：<br/>　　　　静态属性等从基类-&gt;子类进行初始化<br/>　　　　默认无参构造方法相关的特性　　<br/><br/>　　5. 访问控制<br/>　　这也是java封装特性的一个基础，需要掌握的有：<br/>public protected default private 对于class， method， field 的修饰作用<br/><br/>　　6. 流程控制<br/>　　Java 流程控制的基础， 虽然有些语法不一定很常用，但是都需要了解，并且在合适的地方使用它们。<br/>需要掌握的有：<br/>if， switch， loop， for， while 等流程控制的语法<br/><br/>　　7. 面向对象编程的概念<br/>　　这是一个java的核心概念，对于任何java开发者都需要熟练掌握。Java中很多特性或者说知识点都是和java面向对象编程概念相关的。在我的理解，一个好的开发者不仅仅需要了解这些特性（知识点）本身，也更需要知道这些对象在java的面向对象编程概念中是如何体现出来的，这样更有利于开发者掌握java这门开发语言，以及其他面向对象编程的语言。在这里只是简单罗列了一下，主要的知识点包括有：<br/>面向对象三大特性：封装，继承，多态; 各自的定义概念，有哪些特性体现出来，各自的使用场景<br/>静态多分派，动态单分派的概念<br/>重载的概念和使用<br/>继承：接口多实现，基类单继承<br/>抽象，抽象类，接口<br/>多态：方法覆盖的概念和使用<br/>接口回调<br/><br/>　　8. Static<br/>　　静态属性在java日常开发中也是经常使用，需要了解和 static 关键字相关的用法，还有和其他关键字的配合使用， 如是否可以和 abstract， final 等关键字联合使用。<br/>主要需要掌握的有：<br/>静态属性的定义，使用，以及类加载时如何初始化<br/>静态方法的定义和使用<br/>静态类的定义和使用<br/>静态代码块的定义和初始化时机<br/><br/>　　9. 基础知识点<br/>　　这里主要罗列一些散落的，没有系统归类的一些java知识点。在日常的开发中用到也不少。 这块内容其实还有很多，目前只是暂时归纳了这几个在这里：<br/>　　包括有：<br/>equals ， hashcode ， string/stringbuffer ，final ， finally ， finalize<br/><br/>　　10.集合框架<br/>　　这个是一个需要多加掌握的部分，做java开发，可以说没有不用到集合框架的，这很重要。但是这里的知识点并不难，但是对于集合最好要了解内部的实现方式，因为这样有助于你在各个不同的场景选择适合的框架来解决问题，比如有1W个元素的集合，经常要进行contains判断操作，知道了集合的特性或者内部实现，就很容易做出正确的选择。<br/>　　这里包括了如下内容(并发相关不包含在内)：<br/>集合框架的体系： 基础Collection ，Map<br/>具体集合实现的内容， List ，Set ，Map 具体的实现，内部结构， 特殊的方法， 适用场景等<br/>集合相关的工具类 Collections 等的用法<br/><br/>　　11.异常框架<br/>　　异常在java的开发中可能没有那么被重视。一般遇到异常，直接上抛，或者随便catch一下处理之后对于程序整体运行也没有什么大的影响。不过在企业级设计开发中， 异常的设计与处理的好坏，往往就关系着这个系统整体的健壮性。一个好的系统的异常对于开发者来说，处理应该统一，避免各处散落很多异常处理逻辑；对于系统来说，异常应该是可控的，并且是易于运维的，某些异常出现后，应该有应对的方法，知道如何运维处理，所以虽然异常框架很简单，但是对于整个企业级应用开发来说，异常处理是很重要的，处理好异常就需要了解Java中的异常体系。<br/>　　这部分需要掌握的知识点不多，主要就是：<br/>异常的体系：<br/>Throwable<br/>Exception<br/>RuntimeException<br/>Error<br/>RuntimeException 和 一般 Exception 的区别， 具体处理方法等<br/><br/>　　12. Java IO<br/>　　IO 在java中不仅仅是文件读写那么简单，也包括了 socket 网络的读写等等一切的输入输出操作。比如说 标准HTTP请求中Post的内容的读取也是一个输出的过程，等等…<br/>　　对于IO，Java不仅提供了基本Input、Output相关的api，也提供了一些简化操作的Reader、Writer等api，在某些开发（涉及大量IO操作的项目）中也很重要，一般日常的开发中也会涉及（日志，临时文件的读写等）。<br/>　　在这中的知识点主要有：<br/>基本IO的体系： 包括有InputStream ， OutputStream， Reader/Writer， 文件读取，各种流读取等<br/>NIO 的概念， 具体使用方式和使用场景<br/><br/>　　13.多线程并发<br/>　　多线程是Java中普遍认为比较难的一块。多线程用好了可以有效提高cpu使用率， 提升整体系统效率， 特别是在有大量IO操作阻塞的情况下；但是它也是一柄双刃剑， 如果用不好，系统非但提升不大，或者没有提升，而且还会带来多线程之间的调试时等问题。<br/>　　在多线程中内容有很多，只是简单说明一下Java中初步使用多线程需要掌握的知识点，以后有机会单独再详细介绍一些高级特性的使用场景。<br/>多线程的实现和启动<br/>callable 与 runable 区别<br/>syncrhoized ，reentrantLock 各自特点和比对<br/>线程池<br/>future 异步方式获取执行结果<br/>concurrent 包<br/>lock<br/>..<br/><br/>　　14.网络<br/>　　Java中也是提供了可以直接操作 TCP协议、UDP协议的API。在需要强调网络性能的情况下，可以直接使用TCP/UDP 进行通讯。在查看Tomcat等的源码中，就可以看到这些相关API的使用情况。不过一般也比较少会直接使用TCP，会使用诸如MINA、Netty这样的框架来进行处理，因为这个方面的开发涉及不多，所以就不再详细罗列了。<br/><br/>　　15.时间日期处理<br/>　　几乎对于每个应用来说，时间日期的处理也是绕不过去的，但是JDK8 之前的时间相关API用法并不友好。在那个时代，可以选择Joda等时间框架。到了JDK8 发布之后，全新的时间API基本融合了其他框架的优点，已经可以很好的直接使用了。<br/>　　对于Java开发者来说，需要熟练地使用API来对时间和日期做相关的处理。<br/>　　具体知识点不再罗列，会在以后再写个专门的文章来总结一下JDK8中时间日期API的用法。<br/><br/>　　16.XML解析/ JSON解析<br/>　　其实这两块内容都不是J2SE里面的内容，但是在日常开发中，和其他程序交互，和配置文件交互，越来越离不开这两种格式的解析。<br/>　　不过对于一个开发者来说，能够了解一些XML/JSON具体解析的原理和方法，有助于你在各个具体的场景中更好的选择合适你的方式来使得你的程序更有效率和更加健壮。<br/>　　XML： 需要了解 DOM解析和 SAX解析的基本原理和各自的适用场景<br/>　　JSON： 需要了解一些常用JSON框架的用法， 如 Jackson， FastJson， Gson 等。。<br/><br/>　　17.Maven的使用<br/>　　Maven也不是Java里面的内容，但是maven是革命性的，给java开发带来了巨大的便利。从依赖的引入和管理，开发流程的更新和发布产出，乃至版本的更新，使用maven可以大大简化开发过程中的复杂度，从而节省大量时间。可以说，maven已经成为java开发者的标配了。所以我把maven也作为一个java开发者对于基础必备的知识点。以后会再放上一些我的一些对于maven使用的经验和技巧等，这里就不再细说了。<br/><br/>　　18.泛型<br/>　　这是JDK5开始引入的新概念，其实是个语法糖，在编写java代码时会有些许便利， 一般的应用或者是业务的开发，只需要简单使用，不一定会用到定义泛型这样的操作， 但是开发一些基础公共组件会使用到，可以在需要的时候再细看这个部分，一般情况下只要会简单使用即可。<br/><br/>　　19.标注<br/>　　也是jdk5 之后引入的。spring是个优秀的框架，最开始就以xml作为标准的配置文件。不过到了Spring3 之后，尤其是 spring-boot 兴起之后，越来越推崇使用标注来简化xml配置文件了，对于开发者来说，可以节省不少xml配置的时间。但是劣势是在于标注散落在各个类中，不像xml，可以对所有配置有个全局性的理解和管理，所以还没有办法说完全就取代所有的xml。对于一般开发者，会使用标注即可，一些公共组建的开发者可能会需要了解标注的定义和实现，可以在具体需要的时候再细看。<br/><br/>　　20.RMI<br/>　　RemoteMethodInvocation ，Java语言特有的远程调用接口，使用还是比较简单方便。不过需要跨语言的情况下，就需要使用 webservice 等其他方式来支持。一般来说，程序都不需要使用RMI，不过可以在特定的情况下使用，我就在一个项目中，使用RMI来进行程序远程启动停止的控制。<br/><br/>　　21.JNI<br/>　　Java Native Interface，可以允许Java中调用本地接口方法，一般用于C/C++代码的调用。需要注意的是在java中加载so/dll文件的路径问题，本身调用接口并不复杂，但是经常在是否加载了所需的本地接口库中花费较多时间。<br/><br/>　　以上也只是简单介绍了下我对于这些java基本知识点和技术点的一些看法和介绍， 这些内容都源自于我这些年来使用java的一些总结， 希望给刚刚接触Java， 或者打算从Java开发的人一些经验， 希望能够更有效率地学习和使用java， 避免走了弯路浪费了宝贵的时间。这当中还有些内容不够完善的地方，会通过以后的文章再添加上。由于个人能力有限，当然其中也会有些错误和疏漏，欢迎指正，一起讨论，共同来把这篇文章再完善下，希望它可以真正帮助到有需要的人。<br/></p>',2,1,80,'2025-04-29 09:09:18','2024-08-09 16:03:36','[]'),(6,4,'小白想学java该如何入手学习？？求有经验大佬告知。','<pre><code class=\"Bash\"><xmp>原文地址：https://bbs.csdn.net/topics/600398682</xmp></code></pre><p>毕业出来半年，目前在广州做着php，感觉前途不算大。目前想转java，但又不知道如何入手。<br/></p>',1,1,121,'2025-04-29 14:07:08','2024-08-09 16:06:13','[]'),(7,2,'高性能的内存数据库（关系型）会不会对Java开发产生影响？','<pre><code class=\"Bash\"><xmp>原文地址：https://bbs.csdn.net/topics/600338509</xmp></code></pre><p>Java开发里很多类似共享，锁，队列的问题都在应用层用Java代码解决，但数据库层一般也是可以提供这样的功能的，如果直接用数据库带的锁功能，代码可以简化很多（比如可以不用考虑锁的问题），数据库的一个缺点是IO读写慢，不过现在也有纯内存级数据库，只是目前还不普及，只是在大公司的企业级系统里才会见到。</p><p>如果数据库性能能够得到极大的提升，是不是可以极大地简化Java开发代码复杂度？</p>',1,1,60,'2024-08-09 16:07:23','2024-08-11 16:08:23','[]'),(8,4,'使用 Lambda 的一些疑惑','<pre><code class=\"Bash\"><xmp>原文地址：https://bbs.csdn.net/topics/600333260</xmp></code></pre><p>开始使用 Lambda 时觉得很爽， 代码结构简单了，代码量减少了。但是时间久了就遇到一些问题（以上主要是针对一些复杂的代码逻辑，不可否认如果是简单逻辑代码的话没有上述问题）</p><p>1)Lambda 改变了自己的编程风格，以前设计虑面向对象（我个人觉得这种更偏向业务流程）， Lambda 使用后变流式风格（这种更偏向数据处理）。如果根据《重构》提到的原则为标准， 使用Lambda 后我的代码结构越来越烂了。</p><p>2)Lambda 本身的一些推断处理，有时候会让理解代码很困难， 更不用说去理解其他人的代码了。敏捷开发和极限编程中都提到不写设计文档使用代码来表示需求。 Lambda被使用后，根据代码理解业务更困难了。 现在读别人写的代码我就头疼。 原罪：自己可写Lambda ，但不想别人写</p><p>欢迎大家讨论，说说自己的想法 。有没有我相同的困惑， 工作中如何取舍呢</p>',4,1,91,'2024-08-09 16:08:26','2024-08-09 16:08:26','[]'),(10,4,'今天也要好好学习啊！','<p>加油~</p>',5,1,91,'2025-04-29 14:08:17','2024-08-09 16:15:29','[]'),(13,2,'作为程序员，听到的最多的词就是“bug”吧','<pre><code class=\"Bash\"><xmp>原文地址：https://www.lanqiao.cn/questions/178374</xmp></code></pre><p>程序员的内心得有多强大，才能淡然接受每天的bug。</p><p>每天都要听到，“你写的东西有问题吧”，“这是bug吧”，我的天</p>',4,1,27,'2025-04-28 17:16:14','2024-08-09 16:19:20','[]'),(17,4,'今天天气真好啊！','<p><img src=\"http://localhost:8080/upload/20240815_10091843.jpg\" style=\"max-width:100%;\" contenteditable=\"false\"/></p><p><img src=\"http://localhost:8080/upload/20240815_10095893.jpg\" style=\"max-width:100%;\" contenteditable=\"false\"/></p><h2 data-we-empty-p=\"\" id=\"uvdeg\" style=\"padding-left:2em;\"><font color=\"#c24f4a\">awwawd<strong></strong></font><font color=\"#c24f4a\"><strong></strong></font><br/></h2>',2,1,162,'2025-04-29 16:15:56','2024-08-15 10:09:27','[]'),(48,223,'VS Code真好用！','![](/api/images/8385603d4073c1a67bdd5e3f133bd19e.png)\n',2,1,20,'2025-04-29 14:09:08','2025-04-27 21:10:07','[]'),(49,223,'Hibernate Criteria how to add new construction in Projections or other?','With HQL I can enter the following statement can be made :\n\nSELECT new MyClass(u.name,u.email) FROM User u ;\n\nwhere MyClass is a normal Javabean with name and email as a Constructor.\n\nI like to use Hibernate Criteria to construct such queries. Is this possible. I know I can restrict the columns to name and email using Projections but how do I get to use the new operator in Criteria ?\n\n',4,1,1,'2025-04-28 16:39:02','2025-04-28 16:39:02','[]');
/*!40000 ALTER TABLE `tb_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_post_category`
--

DROP TABLE IF EXISTS `tb_post_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_post_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类表主键',
  `category_name` varchar(16) NOT NULL COMMENT '分类的名称',
  `category_rank` int(11) NOT NULL DEFAULT 1 COMMENT '分类的排序值 被使用的越多数值越大',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0=否 1=是',
  `is_muted` tinyint(1) DEFAULT 0,
  `is_pending` tinyint(1) DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `category_avatar` varchar(256) DEFAULT '',
  `category_info` mediumtext DEFAULT '' COMMENT '板块介绍',
  `category_rule` mediumtext DEFAULT '',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `tb_post_category_pk` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_post_category`
--

LOCK TABLES `tb_post_category` WRITE;
/*!40000 ALTER TABLE `tb_post_category` DISABLE KEYS */;
INSERT INTO `tb_post_category` VALUES (1,'提问',10,0,0,0,'2024-07-10 14:47:38','/api/images/avatar/space.webp','为用户解决各类疑难杂事','本板块只允许交流问题，请勿讨论无关话题，请遵守相关法律法规'),(2,'分享',9,0,0,0,'2024-07-10 14:47:38','/api/images/avatar/w3omvskr11r.jpg','生活中的点点滴滴，无论好坏都可以分享给大家','请遵守相关法律法规'),(3,'建议',8,0,0,0,'2024-07-10 14:47:38','/api/images/avatar/is1tlhy2why.jpg','为网站的运营提建议，可以是新功能也可以是改良','只允许讨论网站运营相关，请勿讨论无关话题'),(4,'讨论',7,0,0,0,'2024-07-10 14:47:38','/api/images/avatar/logo.jpeg','',''),(5,'动态',6,0,0,0,'2024-07-10 14:47:38','/api/images/avatar/waterlifer_a_plate_of_spinach_stir-fried_with_egg_0a87c12f-e8d6-477f-963a-54c4e0788fcb.webp','',''),(6,'其它',5,0,0,0,'2024-07-10 14:47:38','','','');
/*!40000 ALTER TABLE `tb_post_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_post_comment`
--

DROP TABLE IF EXISTS `tb_post_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_post_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `post_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '关联的帖子主键',
  `comment_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '评论者id',
  `comment_body` varchar(512) NOT NULL DEFAULT '' COMMENT '评论内容',
  `parent_comment_id` bigint(20) DEFAULT NULL COMMENT '所回复的上一级评论Id',
  `comment_create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '评论时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
  PRIMARY KEY (`comment_id`),
  KEY `tb_post_comment_tb_post_comment_comment_id_fk` (`parent_comment_id`),
  KEY `tb_post_comment_tb_post_post_id_fk` (`post_id`),
  KEY `tb_post_comment_tb_user_user_id_fk` (`comment_user_id`),
  CONSTRAINT `tb_post_comment_tb_post_comment_comment_id_fk` FOREIGN KEY (`parent_comment_id`) REFERENCES `tb_post_comment` (`comment_id`),
  CONSTRAINT `tb_post_comment_tb_post_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`post_id`),
  CONSTRAINT `tb_post_comment_tb_user_user_id_fk` FOREIGN KEY (`comment_user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_post_comment`
--

LOCK TABLES `tb_post_comment` WRITE;
/*!40000 ALTER TABLE `tb_post_comment` DISABLE KEYS */;
INSERT INTO `tb_post_comment` VALUES (32,17,137,'root',NULL,'2025-03-05 01:45:58',0),(33,17,136,'0.1',32,'2025-03-05 01:46:36',0),(34,17,135,'0.2',33,'2025-03-05 01:47:18',0),(36,17,134,'0.3',34,'2025-03-05 01:48:54',0),(37,17,133,'1.1',32,'2025-03-05 01:50:06',0),(38,17,132,'1.2',37,'2025-03-05 01:50:37',0),(41,17,130,'1.3',37,'2025-03-05 05:59:08',0),(45,13,223,'我们要习惯于bug，bug是不可能根除的，只能通过不断的测试和修复才能尽可能减少bug带来的影响',NULL,'2025-04-28 17:12:27',0),(46,13,223,'bug真的很讨厌！',NULL,'2025-04-28 17:16:14',0),(47,6,223,'建议你去看一看菜鸟教程，非常适合初学者',NULL,'2025-04-28 17:19:44',0),(48,6,223,'建议你去看一看菜鸟教程，非常适合初学者',NULL,'2025-04-28 17:19:58',0),(49,6,223,'建议你去看一看菜鸟教程，非常适合初学者',NULL,'2025-04-28 17:20:07',0),(50,6,223,'2333333',NULL,'2025-04-28 17:21:57',0),(51,6,223,'1234',NULL,'2025-04-28 17:47:00',0),(52,6,223,'12345',NULL,'2025-04-28 17:49:32',0),(53,6,223,'123456',NULL,'2025-04-28 17:50:39',0),(54,6,223,'1234567',NULL,'2025-04-28 17:57:17',0),(55,6,223,'好的！感谢',48,'2025-04-28 18:32:13',0),(56,6,223,'不用客气！',55,'2025-04-28 18:33:10',0),(57,6,223,'不用客气！',55,'2025-04-28 18:34:38',0),(58,6,223,'1234',49,'2025-04-28 18:36:59',0),(59,6,223,'1212',55,'2025-04-28 19:15:13',0),(61,6,223,'12345678',55,'2025-04-28 19:17:40',0),(62,6,223,'98765',55,'2025-04-28 19:23:21',0),(63,6,223,'987654',55,'2025-04-28 19:24:42',0),(64,6,223,'6543',55,'2025-04-28 19:26:17',0),(65,6,223,'6543',55,'2025-04-28 19:28:49',0),(66,6,223,'7654',55,'2025-04-28 19:39:58',0),(67,6,223,'765',55,'2025-04-28 19:42:48',0),(68,6,223,'addfsd',55,'2025-04-28 19:43:52',0),(69,6,223,'test',55,'2025-04-28 19:46:13',0),(70,6,223,'test3',55,'2025-04-28 19:50:00',0),(71,6,223,'testclosure',55,'2025-04-28 19:52:13',0),(72,6,223,'testclosure1',55,'2025-04-28 19:56:58',0),(73,6,223,'testcl',55,'2025-04-28 19:59:32',0),(74,6,223,'testck',55,'2025-04-28 20:02:36',0),(75,6,223,'testc',55,'2025-04-28 20:18:38',0),(76,6,223,'testch',55,'2025-04-28 20:19:45',0),(77,6,223,'testcl',55,'2025-04-28 20:26:02',0),(78,6,223,'testcl',55,'2025-04-28 20:26:33',0),(79,6,223,'closure',55,'2025-04-28 20:30:26',0),(80,4,223,'感谢分享！',NULL,'2025-04-28 21:07:42',0),(81,4,223,'1',80,'2025-04-28 21:08:36',0),(82,4,223,'2',81,'2025-04-28 21:09:12',0),(83,4,223,'3',81,'2025-04-29 05:49:35',0),(84,4,223,'4',80,'2025-04-29 09:09:18',0),(85,48,223,'VS Code和Jetbrains 全家桶各有优劣吧，前者清凉化，自定义空间大，后者有完整的开发体系，功能强大',NULL,'2025-04-29 13:54:47',0),(86,6,223,'Stack Overflow也是很不错的网站，有很多问题都能在那儿找到答案，就是需要一定的英语基础',48,'2025-04-29 14:05:24',0),(87,6,223,'稀土掘金也很不错！',48,'2025-04-29 14:07:08',0),(88,10,223,'加油，我相信你！',NULL,'2025-04-29 14:08:16',0),(89,48,223,'如果只是写脚本的话，甚至neovim都够用了',85,'2025-04-29 14:09:08',0),(90,17,223,'这是17号城的运河船道吗？天气好的时候确实很美丽！',NULL,'2025-04-29 16:15:56',0);
/*!40000 ALTER TABLE `tb_post_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
  `login_name` varchar(32) NOT NULL COMMENT '登陆邮箱号码',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `nick_name` varchar(8) NOT NULL COMMENT '昵称',
  `head_img_url` varchar(256) NOT NULL DEFAULT '/images/avatar/default.jpg' COMMENT '头像',
  `location` varchar(4) NOT NULL DEFAULT '' COMMENT '居住地',
  `introduce` varchar(32) DEFAULT '' COMMENT '个人简介',
  `user_locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户状态 0=正常 1=封禁',
  `last_login_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '最新登录时间',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  `gender` enum('MALE','FEMALE','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `tb_user_pk` (`login_name`),
  UNIQUE KEY `tb_user_pk_2` (`nick_name`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'coder13@qq.com','e10adc3949ba59abbe56e057f20f883e','coder13','/images/avatar/default.jpg','杭州','我不怕千万人阻挡，只怕自己投降。',0,'2024-08-10 16:33:20','2024-08-10 14:49:38','UNKNOWN'),(2,'coder14@qq.com','e10adc3949ba59abbe56e057f20f883e','coder14','/images/avatar/default.jpg','未知','这个人很懒，什么都没留下~',0,'2024-08-10 16:21:57','2024-08-10 15:57:48','UNKNOWN'),(3,'coder15@qq.com','e10adc3949ba59abbe56e057f20f883e','coder15','/images/avatar/default.jpg','未知','这个人很懒，什么都没留下~',0,'2024-08-10 16:23:32','2024-08-10 16:09:41','UNKNOWN'),(4,'spidey5852@foxmail.com','e10adc3949ba59abbe56e057f20f883e','syh','/images/d712b8a34df6c9509d069ba47f3036eb.png','未知','这个人很懒，什么都没留下~',0,'2024-08-15 10:51:16','2024-08-14 23:21:57','UNKNOWN'),(126,'CRk3xuyK@example.com','$2a$04$oDO08777sIXPoFRN3Nt6Lux3Bw4G.qpG3ZFcFTTerUyPE5VwwHL6i','CEH5Ce6I','/images/avatar/default.jpg','','',0,'2025-03-04 04:01:47','2025-03-04 04:01:47','UNKNOWN'),(130,'mZO36oIA@example.com','$2a$04$UyJWbMnlS0luNVVhpHogv.TrtSJkXDKV04vPfMW9JAwIp2xioMdAi','4eA3bt4O','/images/avatar/default.jpg','','',0,'2025-03-04 04:11:28','2025-03-04 04:11:28','UNKNOWN'),(132,'mrR1P6Bu@example.com','$2a$04$sGQ.mXclISob.XvgkM69Eu7Wx8AgjdZGr4rXhchOoLhyFW9sk0kny','EzbBH760','/images/avatar/default.jpg','','',0,'2025-03-04 04:13:00','2025-03-04 04:13:00','UNKNOWN'),(133,'pWgE7wXm@example.com','$2a$04$Z5cfFqlaz2A5fddoJ25.XuzqBROzUx0i.tV9vqA3QgoNB/FILhwDG','DWMwH5Uw','/images/avatar/default.jpg','','',0,'2025-03-04 04:14:57','2025-03-04 04:14:57','UNKNOWN'),(134,'FO5eTklX@example.com','$2a$04$6T6STxRbroCKd5cfWSL1guyjqasZ73n9DB.VJRIdXbVwwWJTvCI0.','vYmqTdXY','/images/avatar/default.jpg','','',0,'2025-03-04 04:14:57','2025-03-04 04:14:57','UNKNOWN'),(135,'SWtpCeGK@example.com','$2a$04$jN2Nu7eB.TILpw76/v2BQeGKbDENrM3E0cGxE3cvDa2zeFLMM3ERa','sSj4ZTvr','/images/avatar/default.jpg','','',0,'2025-03-04 04:15:56','2025-03-04 04:15:56','UNKNOWN'),(136,'rAwDBwya@example.com','$2a$04$T3MiNhfJ7iNMJoVN3w9YZu9.YdUTv6Nm3Vxc/QT8FIN/D5fkjsL2u','jww5DTvQ','/images/avatar/default.jpg','','',0,'2025-03-04 04:16:32','2025-03-04 04:16:32','UNKNOWN'),(137,'hnLAsEfc@example.com','$2a$04$A3Yml3Ud05WZaYBQXdPXkOghI/sG2s/8YQsT2EoXOIp38tqvhyrS.','sQHQ8KE0','/images/avatar/default.jpg','','',0,'2025-03-04 04:16:32','2025-03-04 04:16:32','UNKNOWN'),(154,'YBiENFai@example.com','$2a$08$lhcN5q2oW9b7zeE5y8LYH.cKJ7RZ.84EivgvSH7Pi7.tu4ujduphK','UBVctaIJ','/images/avatar/default.jpg','','',0,'2025-03-07 16:10:35','2025-03-07 16:10:35','UNKNOWN'),(155,'G3ATzG72@example.com','$2a$08$Q7oB.Bd/9Cm7GgnCzTkhMOsGcw80ddYzZqEacm7.JmAYc3LoD1F16','xCpko7rt','/images/avatar/default.jpg','','',0,'2025-03-07 16:11:43','2025-03-07 16:11:43','UNKNOWN'),(166,'5wzHDvW8@example.com','$2a$04$5AysDN23iPAWrpJcHYtaKOhV0x2D/SGgHVdtzVW13VrYjPXPNfs3y','JRiZ4aGF','/images/avatar/default.jpg','','',0,'2025-03-08 18:24:08','2025-03-08 18:24:08','UNKNOWN'),(170,'SzhNK2Yb@example.com','$2a$04$Iqa4Xi5nLM70pt9rMHaWpOkeLQvdPqkbmzljMz.s/brTpLP9Wj2ma','XLYV3nFE','/images/avatar/default.jpg','','',0,'2025-03-08 18:30:08','2025-03-08 18:30:08','UNKNOWN'),(174,'Q1SyB8cA@example.com','$2a$04$.yEgNg8vywt9Dg4zNqUrte7/pozYZ703y1R6FEvmVjM6LyJLOp9t.','CFqT6eRg','/images/avatar/default.jpg','','',0,'2025-03-13 13:53:50','2025-03-13 13:53:50','UNKNOWN'),(175,'suyh4822@gmail.com','$2a$04$FfBWYQsYlI27SyhsAxw1zu2OQgRdJVAqXNOrJvyNQMkkBArg4aeF6','Hank','/images/avatar/default.jpg','','',0,'2025-03-13 16:44:54','2025-03-13 16:44:54','UNKNOWN'),(181,'VNgnrSU8@example.com','$2a$04$LtlA8RB2JnCMoq0BtwXtfOo2XLXE8aSiUBZXm5mmTYFd8dA2dQMsG','gWuGaspX','/images/avatar/default.jpg','','',0,'2025-03-26 15:16:45','2025-03-26 15:16:45','UNKNOWN'),(186,'MRjp40u4@example.com','$2a$04$QqQ3VCfdIj5zNdzqTliwyOER15T3USQwHW3AUWh6xc7f25UkMel/e','xhVQIUQj','/images/avatar/default.jpg','','',0,'2025-03-26 15:39:20','2025-03-26 15:39:20','UNKNOWN'),(194,'l8IUD1X8@example.com','$2a$04$zt7cRePBTk8xtW768YygNOKp1NelrReBxp68/y4XrTlfMI.ikglSC','qFL0NvcV','/images/avatar/default.jpg','','',0,'2025-03-26 15:50:24','2025-03-26 15:50:24','UNKNOWN'),(195,'2QO4fmEF@example.com','$2a$04$7E35IWkiBIXjTIF6uaJJwOw4vcfe.ylnQ4Tn94DIe67vfSAMdnMgK','o1tXRuhP','/images/avatar/default.jpg','','',0,'2025-03-26 15:52:09','2025-03-26 15:52:09','UNKNOWN'),(197,'XmEuA8P7@example.com','$2a$04$T9EEQKuQeyESMM4O.RuaGOlabxZCy/a/w4WFmgGpRswM2ni2d3WDu','nHPNQBEW','/images/avatar/default.jpg','','',0,'2025-03-26 15:54:41','2025-03-26 15:54:41','UNKNOWN'),(198,'PyisxHfd@example.com','$2a$04$R81.fNOTVj2zk7XJAM.Or.lkw1pFpTGwcJ1ucB/RPTV6eU5GaQ9/.','uILgZUEg','/images/avatar/default.jpg','','',0,'2025-03-26 15:59:58','2025-03-26 15:59:58','UNKNOWN'),(223,'3085084822@qq.com','$2a$04$.uWGTtWjPb45SvBq1SFG4OLL.02Vvk.5JPmPstNKoAlACKH2uJ4Ga','Han','/images/avatar/default.jpg','','',0,'2025-04-09 17:40:53','2025-04-09 17:40:53','UNKNOWN');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_bookmark_comment`
--

DROP TABLE IF EXISTS `tb_user_bookmark_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_bookmark_comment` (
  `comment_id` bigint(20) NOT NULL COMMENT '收藏评论id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`comment_id`,`user_id`),
  KEY `tb_user_bookmark_comment_tb_user_user_id_fk` (`user_id`),
  CONSTRAINT `tb_user_bookmark_comment_tb_post_comment_comment_id_fk` FOREIGN KEY (`comment_id`) REFERENCES `tb_post_comment` (`comment_id`),
  CONSTRAINT `tb_user_bookmark_comment_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_bookmark_comment`
--

LOCK TABLES `tb_user_bookmark_comment` WRITE;
/*!40000 ALTER TABLE `tb_user_bookmark_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_bookmark_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_bookmark_post`
--

DROP TABLE IF EXISTS `tb_user_bookmark_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_bookmark_post` (
  `post_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '收藏帖子主键',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '收藏者id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '评论时间',
  PRIMARY KEY (`user_id`,`post_id`),
  KEY `tb_user_bookmark_post_tb_post_post_id_fk` (`post_id`),
  CONSTRAINT `tb_user_bookmark_post_tb_post_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`post_id`),
  CONSTRAINT `tb_user_bookmark_post_tb_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户收藏帖子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_bookmark_post`
--

LOCK TABLES `tb_user_bookmark_post` WRITE;
/*!40000 ALTER TABLE `tb_user_bookmark_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_bookmark_post` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-01 13:15:29
