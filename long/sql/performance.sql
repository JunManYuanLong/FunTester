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

 Date: 02/01/2020 18:13:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for performance
-- ----------------------------
DROP TABLE IF EXISTS `performance`;
CREATE TABLE `performance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `threads` int(4) DEFAULT NULL COMMENT '线程数',
  `rt` int(5) DEFAULT NULL COMMENT '平均响应时间，ms',
  `qps` double(10,4) DEFAULT NULL COMMENT 'QPS处理能力  /s',
  `error` double(10,4) DEFAULT NULL COMMENT '错误率',
  `fail` double(10,4) DEFAULT NULL COMMENT '失败率',
  `des` varchar(1000) DEFAULT NULL COMMENT '任务描述',
  `total` int(10) DEFAULT NULL COMMENT '总请求次数',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
