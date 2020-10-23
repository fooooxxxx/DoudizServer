/*
 Navicat Premium Data Transfer

 Source Server         : shiplu
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : doudiz

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 23/10/2020 08:07:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for battle
-- ----------------------------
DROP TABLE IF EXISTS `battle`;
CREATE TABLE `battle`  (
  `battle_id` int NOT NULL COMMENT 'battle_id = room_id+玩家1id+玩家2id+玩家3id',
  PRIMARY KEY (`battle_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of battle
-- ----------------------------

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`  (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `player_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES (1, '阿伟');
INSERT INTO `player` VALUES (2, '壮汉');
INSERT INTO `player` VALUES (10, '大张伟');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int(6) UNSIGNED ZEROFILL NOT NULL,
  `player1_id` int NULL DEFAULT NULL,
  `player2_id` int NULL DEFAULT NULL,
  `player3_id` int NULL DEFAULT NULL,
  `not_ready_player` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '没有准备的用户id,使用,分隔, 比如13,232,42',
  `battle_id` int NULL DEFAULT NULL COMMENT '战局id,代表战局现在的状况',
  `room_status` int NULL DEFAULT NULL COMMENT '房间状态:\r\n104代表该房间中没有玩家,需要被清理,\r\n1代表其中有玩家,但是没有满员,\r\n3代表房间中玩家满员但是没有全部准备,\r\n4代表房间中玩家满员且全部准备,应该由服务器开始游戏,\r\n5代表正在游戏,此时battle_id应该指向当前战局\r\n\r\n',
  PRIMARY KEY (`room_id`) USING BTREE,
  INDEX `room_for_battle`(`battle_id`) USING BTREE,
  CONSTRAINT `room_for_battle` FOREIGN KEY (`battle_id`) REFERENCES `battle` (`battle_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
