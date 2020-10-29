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

 Date: 29/10/2020 23:13:04
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
-- Records of battle
-- ----------------------------
INSERT INTO `battle` VALUES (67229, '[1, 2, 10]', '[\"2_15,4_13,1_6,3_15,2_4,1_5,3_13,4_10,4_9,1_4,3_6,1_8,1_7,2_3,1_10,2_13,2_12\", \"1_15,2_5,3_14,3_11,4_8,4_11,4_15,3_10,3_8,3_5,3_9,3_7,4_5,2_9,1_9,3_4,2_11\", \"2_6,4_14,4_4,4_12,4_7,4_3,2_7,4_6,2_8,1_11,2_10,1_12,3_3,1_14,1_13,1_3,3_12\"]', '[\"\", \"\", \"\"]', 15, 0, '5_17,5_16,2_14', '[603, 603, 603]', '600');

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
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES (1, '阿伟', 67229);
INSERT INTO `player` VALUES (2, '壮汉', 67229);
INSERT INTO `player` VALUES (10, '大张伟', 67229);
INSERT INTO `player` VALUES (17, '安卓测试', NULL);
INSERT INTO `player` VALUES (18, '安卓测试', NULL);
INSERT INTO `player` VALUES (19, '安卓测试', NULL);
INSERT INTO `player` VALUES (20, '安卓测试', 13597);

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

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (13597, 20, 0, 0, NULL, 0, 'NOT_FULL');
INSERT INTO `room` VALUES (67229, 1, 2, 10, NULL, 0, 'FULL_AND_READY');

SET FOREIGN_KEY_CHECKS = 1;
