/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50640
 Source Host           : 172.18.4.55:3306
 Source Schema         : okayapi

 Target Server Type    : MySQL
 Target Server Version : 50640
 File Encoding         : 65001

 Date: 02/01/2020 18:14:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for request
-- ----------------------------
DROP TABLE IF EXISTS `request`;
CREATE TABLE `request` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(6) DEFAULT NULL,
  `method` varchar(6) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL,
  `api` varchar(100) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `code` int(10) DEFAULT NULL,
  `expend_time` double(10,2) DEFAULT NULL,
  `data_size` int(6) DEFAULT NULL,
  `local_ip` varchar(20) DEFAULT NULL,
  `local_name` varchar(20) DEFAULT NULL,
  `create_time` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=122167 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
