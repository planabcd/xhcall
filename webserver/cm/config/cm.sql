/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 5.0.18-nt : Database - cm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `cm`;

/*Table structure for table `route` */

DROP TABLE IF EXISTS `route`;

CREATE TABLE `route` (
  `id` int(11) NOT NULL auto_increment,
  `station_name` varchar(30) default NULL,
  `prev` varchar(30) default NULL,
  `next` varchar(30) default NULL,
  `direction` varchar(20) default NULL,
  `circuit_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `route` */

insert  into `route`(`id`,`station_name`,`prev`,`next`,`direction`,`circuit_id`) values (7,'东门',NULL,'15号楼','上行',1),(8,'15号楼','东门','图书馆','上行',1),(9,'图书馆','15号楼','宏远教学楼B','上行',1),(10,'宏远教学楼B','图书馆','西街','上行',1),(11,'西街','宏远教学楼B',NULL,'上行',1),(13,'东门',NULL,'北门','上行',2),(14,'北门','东门','15号楼','上行',2),(15,'15号楼','北门','行政楼','上行',2),(16,'行政楼','15号楼','宏远教学楼A','上行',2),(17,'宏远教学楼A','行政楼',NULL,'上行',2);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `pwd` varchar(40) default NULL,
  `type` varchar(10) default NULL,
  `state` char(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`pwd`,`type`,`state`) values (1,'hbn','670b14728ad9902aecba32e22fa4f6bd','Admin','0'),(6,'wangjinghui','81dc9bdb52d04dc20036dbd8313ed055','Customer','0'),(7,'苏瑶','670b14728ad9902aecba32e22fa4f6bd','Customer','0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
