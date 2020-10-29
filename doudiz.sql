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

 Date: 29/10/2020 22:48:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for battle
-- ----------------------------
DROP TABLE IF EXISTS `battle`;
CREATE TABLE `battle`  (
  `battle_id` int NOT NULL COMMENT 'battle_id = room_id',
  `player_list` json NULL COMMENT '玩家id列表',
  `player_hand_cards` json NULL COMMENT '手牌',
  `player_show_cards` json NULL COMMENT '打出来的牌',
  `count_down` int NULL DEFAULT NULL COMMENT '倒计时',
  `current_player` int NULL DEFAULT NULL COMMENT '当前允许操作的玩家,用0,1,2标识',
  `extra_cards` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '给地主的额外三张牌',
  `identity` json NULL COMMENT '存放身份,比如地主,也存放玩家是否在叫地主或者抢地主',
  `battle_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '当前战局状态',
  PRIMARY KEY (`battle_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`  (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `player_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `enter_room_id` int NULL DEFAULT 0,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `player1_id` int NOT NULL,
  `player2_id` int NULL DEFAULT NULL,
  `player3_id` int NULL DEFAULT NULL,
  `not_ready_player` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '没有准备的用户id,使用,分隔, 比如13,232,42',
  `battle_id` int NULL DEFAULT NULL COMMENT '战局id,代表战局现在的状况',
  `room_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '房间状态:\r\n104代表该房间中没有玩家,需要被清理,\r\n1代表其中有玩家,但是没有满员,\r\n3代表房间中玩家满员但是没有全部准备,\r\n4代表房间中玩家满员且全部准备,应该由服务器开始游戏,\r\n5代表正在游戏,此时battle_id应该指向当前战局\r\n\r\n',
  PRIMARY KEY (`room_id`) USING BTREE,
  INDEX `room_for_battle`(`battle_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
