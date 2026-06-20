/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : insects_db

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 20/01/2026 13:37:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `is_recommend` tinyint(1) NULL DEFAULT 0,
  `like_count` int NULL DEFAULT 0,
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `view_count` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of articles
-- ----------------------------
INSERT INTO `articles` VALUES (1, '昆虫科普专家', '蝴蝶是昆虫纲鳞翅目中的一类昆虫，它们的生命周期经历了完全变态发育，包括四个阶段：卵、幼虫、蛹和成虫。\r\n\r\n**第一阶段：卵**\r\n雌蝶会在寄主植物上产卵，卵的大小和形状因种类而异。卵期通常持续几天到几周。\r\n\r\n**第二阶段：幼虫（毛毛虫）**\r\n幼虫孵化后，主要任务是进食和生长。它们会经历多次蜕皮，每次蜕皮后体型都会增大。幼虫期是蝴蝶生命周期中最长的阶段，可能持续数周到数月。\r\n\r\n**第三阶段：蛹**\r\n当幼虫发育到一定程度后，会寻找合适的地方化蛹。在蛹内，幼虫的身体会发生巨大的变化，逐渐形成成虫的结构。蛹期通常持续1-2周。\r\n\r\n**第四阶段：成虫**\r\n成虫从蛹中羽化而出，展开翅膀，开始新的生命。成虫的主要任务是繁殖，它们会寻找配偶，交配产卵，完成生命的循环。\r\n\r\n蝴蝶的完全变态发育是自然界中最神奇的转变之一，从丑陋的毛毛虫到美丽的蝴蝶，展现了生命的奇迹。', 'https://img1.baidu.com/it/u=4150808078,3359257201&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=740', '2026-01-19 15:04:44.000000', 1, 0, '了解蝴蝶的完整生命周期，从卵到幼虫、蛹，再到成虫的奇妙转变过程。', '蝴蝶的一生：从毛毛虫到美丽蝴蝶的蜕变', '2026-01-20 13:35:02.907080', 5);
INSERT INTO `articles` VALUES (2, '生物学家', '蜜蜂是高度社会性的昆虫，一个蜂群就像一个完美的社会，每个成员都有明确的职责。\r\n\r\n**蜂王（Queen）**\r\n蜂王是蜂群中唯一的雌性生殖个体，她的主要职责是产卵。蜂王体型最大，寿命可达2-3年。她通过释放信息素来控制整个蜂群的行为。\r\n\r\n**工蜂（Worker）**\r\n工蜂是蜂群中数量最多的成员，都是雌性但不具备生殖能力。工蜂的职责随着年龄变化：\r\n- 1-3日龄：清理巢房\r\n- 4-12日龄：喂养幼虫\r\n- 13-20日龄：筑巢、酿蜜\r\n- 21日龄后：外出采蜜、采粉、采水\r\n\r\n**雄蜂（Drone）**\r\n雄蜂的唯一职责是与新蜂王交配。它们不参与采蜜、筑巢等工作，在交配季节结束后会被工蜂驱逐出蜂群。\r\n\r\n**蜂群协作**\r\n蜜蜂通过复杂的信息交流系统（如舞蹈语言）来协调工作，确保蜂群的高效运转。这种完美的分工合作体系，使得蜜蜂能够成功生存和繁衍。', 'https://img2.baidu.com/it/u=1641511045,1656555518&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800', '2026-01-19 15:04:44.000000', 1, 0, '探索蜜蜂蜂群的复杂社会结构，了解蜂王、工蜂、雄蜂各自的职责。', '蜜蜂的社会结构：一个完美的分工合作体系', '2026-01-20 13:12:40.153506', 8);
INSERT INTO `articles` VALUES (3, '昆虫学家', '蚂蚁虽然体型微小，但它们展现出的智慧和社会组织能力令人惊叹。\r\n\r\n**社会结构**\r\n蚂蚁是典型的社会性昆虫，一个蚁群通常包括：\r\n- 蚁后：负责产卵，是蚁群的核心\r\n- 工蚁：数量最多，负责觅食、筑巢、育幼等工作\r\n- 兵蚁：负责保卫蚁巢\r\n- 雄蚁：负责与蚁后交配\r\n\r\n**通讯方式**\r\n蚂蚁通过多种方式交流：\r\n1. 信息素：释放化学物质标记路径、传递信息\r\n2. 触角接触：通过触角相互触碰传递信息\r\n3. 声音：某些种类会通过摩擦发出声音\r\n\r\n**生存策略**\r\n- 分工合作：不同工蚁负责不同工作，提高效率\r\n- 集体智慧：通过群体决策找到最优解决方案\r\n- 资源管理：能够有效管理和分配食物资源\r\n- 防御机制：通过集体防御保护蚁群\r\n\r\n蚂蚁的世界虽然微小，但它们的智慧和组织能力值得我们学习和研究。', 'https://img0.baidu.com/it/u=2396655815,944282785&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889', '2026-01-19 15:04:44.000000', 1, 0, '了解蚂蚁的复杂社会行为、通讯方式和生存策略。', '蚂蚁的智慧：小小昆虫的大世界', '2026-01-20 13:34:51.981925', 6);
INSERT INTO `articles` VALUES (4, '生态学家', '昆虫在漫长的进化过程中，发展出了各种精妙的伪装技巧来保护自己。\r\n\r\n**保护色**\r\n许多昆虫的体色与周围环境相似，使自己难以被发现：\r\n- 绿色昆虫隐藏在树叶中\r\n- 褐色昆虫隐藏在树干上\r\n- 沙色昆虫隐藏在沙地中\r\n\r\n**拟态**\r\n一些昆虫会模仿其他生物的外形：\r\n- 竹节虫模仿树枝\r\n- 枯叶蝶模仿枯叶\r\n- 某些蛾类模仿鸟粪\r\n\r\n**警戒色**\r\n有毒或难吃的昆虫会使用鲜艳的颜色警告天敌：\r\n- 瓢虫的红色和黑色斑点\r\n- 某些蝴蝶的鲜艳色彩\r\n\r\n**行为伪装**\r\n除了外形，昆虫还会通过行为来伪装：\r\n- 某些昆虫会装死\r\n- 某些昆虫会模仿其他动物的动作\r\n\r\n这些伪装技巧展现了昆虫在生存竞争中的智慧，是自然选择的结果。', 'https://img1.baidu.com/it/u=1971802694,37743572&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=761', '2026-01-19 15:04:44.000000', 0, 0, '探索昆虫如何通过伪装来躲避天敌，包括保护色、拟态等策略。', '昆虫的伪装术：自然界的隐身大师', '2026-01-20 13:32:03.094952', 4);
INSERT INTO `articles` VALUES (5, '生态保护专家', '昆虫与植物之间存在着复杂而密切的关系，这种关系对生态系统的稳定至关重要。\r\n\r\n**传粉作用**\r\n许多昆虫在采蜜或采粉的过程中，会将花粉从一朵花传到另一朵花，帮助植物完成授粉：\r\n- 蜜蜂是最重要的传粉昆虫\r\n- 蝴蝶、蛾类也参与传粉\r\n- 某些甲虫、蝇类也会传粉\r\n\r\n**互利共生**\r\n昆虫和植物之间存在互利关系：\r\n- 植物提供花蜜、花粉作为昆虫的食物\r\n- 昆虫帮助植物完成授粉，促进繁殖\r\n\r\n**防御与适应**\r\n植物也发展出了防御机制：\r\n- 某些植物产生毒素防止昆虫取食\r\n- 某些植物与蚂蚁形成共生关系，蚂蚁保护植物\r\n\r\n**生态平衡**\r\n昆虫与植物的关系维持着生态系统的平衡，任何一方的变化都会影响整个生态系统。保护昆虫就是保护我们的生态环境。', 'https://img2.baidu.com/it/u=2035024598,3457404075&fm=253&fmt=auto?w=800&h=1047', '2026-01-19 15:04:44.000000', 0, 0, '了解昆虫与植物之间的复杂关系，包括传粉、授粉等互利行为。', '昆虫与植物的关系：互利共生的奇妙世界', '2026-01-19 23:46:05.794118', 5);
INSERT INTO `articles` VALUES (6, '城市生态学家', '随着城市化进程的加快，许多昆虫也成功适应了城市环境，成为都市生态系统的重要组成部分。\r\n\r\n**城市常见昆虫**\r\n- 蟑螂：适应性强，能在各种环境中生存\r\n- 蚊子：在城市的水体中繁殖\r\n- 蚂蚁：在公园、绿化带建立巢穴\r\n- 蜜蜂：在城市公园和屋顶花园中筑巢\r\n- 蝴蝶：在城市绿化带中觅食和繁殖\r\n\r\n**适应策略**\r\n城市昆虫通过以下方式适应城市环境：\r\n- 改变生活习性：某些昆虫从夜行改为日行\r\n- 利用人工环境：在建筑物中筑巢\r\n- 改变食物来源：取食人类丢弃的食物\r\n\r\n**城市昆虫的价值**\r\n城市昆虫虽然有时被视为害虫，但它们也有重要价值：\r\n- 传粉作用：帮助城市植物繁殖\r\n- 分解作用：帮助分解有机废物\r\n- 生物指示：反映城市环境质量\r\n\r\n**和谐共存**\r\n我们应该学会与城市昆虫和谐共存，通过科学管理减少害虫，保护有益昆虫。', 'https://img2.baidu.com/it/u=288453639,3020723493&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', '2026-01-19 15:04:44.000000', 0, 0, '探索那些成功适应城市环境的昆虫，了解它们如何在都市中生存。', '城市中的昆虫：适应都市生活的昆虫们', '2026-01-19 23:46:07.942615', 3);

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `level` int NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, '2026-01-19 15:04:44.000000', '包括蝴蝶和蛾类，是昆虫纲中第二大目', NULL, 1, '鳞翅目', NULL);
INSERT INTO `categories` VALUES (2, '2026-01-19 15:04:44.000000', '包括蜜蜂、蚂蚁、黄蜂等', NULL, 1, '膜翅目', NULL);
INSERT INTO `categories` VALUES (3, '2026-01-19 15:04:44.000000', '包括各种甲虫，是昆虫纲中最大的目', NULL, 1, '鞘翅目', NULL);
INSERT INTO `categories` VALUES (4, '2026-01-19 15:04:44.000000', '包括蝗虫、蟋蟀、螽斯等', NULL, 1, '直翅目', NULL);
INSERT INTO `categories` VALUES (5, '2026-01-19 15:04:44.000000', '包括蝉、蚜虫、蝽等', NULL, 1, '半翅目', NULL);
INSERT INTO `categories` VALUES (6, '2026-01-19 15:04:44.000000', '大型美丽的蝴蝶，包括凤蝶、燕尾蝶等', NULL, 2, '凤蝶科', 1);
INSERT INTO `categories` VALUES (7, '2026-01-19 15:04:44.000000', '中小型蝴蝶，多为白色或黄色', NULL, 2, '粉蝶科', 1);
INSERT INTO `categories` VALUES (8, '2026-01-19 15:04:44.000000', '种类最多的蝴蝶科', NULL, 2, '蛱蝶科', 1);
INSERT INTO `categories` VALUES (9, '2026-01-19 15:04:44.000000', '包括蜜蜂、熊蜂等', NULL, 2, '蜜蜂科', 2);
INSERT INTO `categories` VALUES (10, '2026-01-19 15:04:44.000000', '社会性昆虫，包括各种蚂蚁', NULL, 2, '蚁科', 2);

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `share_id` bigint NOT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK8omq0tc18jd43bu5tjh6jvraq`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, '小飞飞', '2026-01-19 16:26:17.810734', NULL, 2, '2026-01-19 16:26:17.810734', 1);
INSERT INTO `comments` VALUES (2, '222', '2026-01-19 16:44:33.428974', NULL, 1, '2026-01-19 16:44:33.428974', 1);
INSERT INTO `comments` VALUES (3, '22332', '2026-01-19 16:44:36.720306', NULL, 1, '2026-01-19 16:44:36.720306', 1);
INSERT INTO `comments` VALUES (4, '1111', '2026-01-19 17:12:48.300856', NULL, 3, '2026-01-19 17:12:48.300856', 1);
INSERT INTO `comments` VALUES (5, '2323', '2026-01-19 17:13:26.002980', NULL, 3, '2026-01-19 17:13:26.002980', 1);
INSERT INTO `comments` VALUES (6, '22222222222222222', '2026-01-19 23:20:54.482171', NULL, 5, '2026-01-19 23:20:54.482171', 1);

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `insect_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorites
-- ----------------------------
INSERT INTO `favorites` VALUES (3, '2026-01-19 16:16:59.059511', 5, 1);
INSERT INTO `favorites` VALUES (6, '2026-01-19 17:10:13.829838', 2, 2);
INSERT INTO `favorites` VALUES (7, '2026-01-19 17:10:16.935084', 4, 2);
INSERT INTO `favorites` VALUES (8, '2026-01-19 18:47:47.270926', 1, 3);
INSERT INTO `favorites` VALUES (9, '2026-01-19 18:47:52.851397', 3, 3);
INSERT INTO `favorites` VALUES (10, '2026-01-19 23:18:42.322381', 9, 1);
INSERT INTO `favorites` VALUES (12, '2026-01-20 10:15:05.051931', 1, 1);
INSERT INTO `favorites` VALUES (13, '2026-01-20 13:11:39.401820', 3, 4);
INSERT INTO `favorites` VALUES (14, '2026-01-20 13:31:38.693367', 3, 1);
INSERT INTO `favorites` VALUES (15, '2026-01-20 13:34:25.490665', 7, 1);

-- ----------------------------
-- Table structure for insects
-- ----------------------------
DROP TABLE IF EXISTS `insects`;
CREATE TABLE `insects`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `description` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `distribution` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `habitat` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image_urls` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `living_habits` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `morphological_features` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `scientific_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `view_count` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of insects
-- ----------------------------
INSERT INTO `insects` VALUES (1, 6, '2026-01-19 15:04:44.000000', '中华虎凤蝶是中国特有的珍稀蝴蝶，被誉为\"国蝶\"。', '中国中部和东部地区', '海拔800-1500米的山地', '[\"https://img2.baidu.com/it/u=296566819,3323038667&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=667\"]', '一年一代，成虫在3-4月出现。喜欢在阳光充足的山坡上活动，飞行缓慢优雅。', '成虫体长约35-40毫米，翅展约60-70毫米。前翅黑色，有黄色斑纹，后翅有红色斑点和尾突。', '中华虎凤蝶', 'Luehdorfia chinensis', '2026-01-20 13:34:34.111077', 9);
INSERT INTO `insects` VALUES (2, 7, '2026-01-19 15:04:44.000000', '菜粉蝶是最常见的蝴蝶之一，幼虫是重要的农业害虫。', '世界各地广泛分布', '农田、菜园、公园等', '[\"https://img0.baidu.com/it/u=3688026637,279469797&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=662\"]', '一年可发生多代，成虫全年可见。喜欢在十字花科植物上产卵。', '成虫体长约20-25毫米，翅展约40-50毫米。翅白色，前翅顶角有黑色斑，雌蝶前翅有2个黑斑，雄蝶有1个。', '菜粉蝶', 'Pieris rapae', '2026-01-20 11:48:44.797618', 11);
INSERT INTO `insects` VALUES (3, 8, '2026-01-19 15:04:44.000000', '大红蛱蝶是常见的观赏蝴蝶，色彩鲜艳。', '亚洲东部和南部', '草地、林缘、公园等', '[\"https://img2.baidu.com/it/u=4022491499,2177267797&fm=253&fmt=auto&app=138&f=JPEG?w=691&h=500\",\"https://img2.baidu.com/it/u=2057187837,3752954356&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500\"]', '一年可发生多代，成虫飞行迅速。喜欢访花，幼虫以荨麻科植物为食。', '成虫体长约25-30毫米，翅展约50-60毫米。翅橙红色，有黑色斑纹，后翅边缘有蓝色斑点。', '大红蛱蝶', 'Vanessa indica', '2026-01-20 13:31:34.351396', 18);
INSERT INTO `insects` VALUES (4, 6, '2026-01-19 15:04:44.000000', '玉带凤蝶是美丽的凤蝶，有雌雄异型现象。', '中国南方、东南亚', '低海拔山区、公园、果园', '[\"https://img1.baidu.com/it/u=4272422539,991282737&fm=253&fmt=auto&app=138&f=JPEG?w=700&h=500\"]', '一年可发生多代，成虫飞行优雅。幼虫以柑橘类植物为食。', '雄蝶体长约35-40毫米，翅展约80-100毫米。翅黑色，后翅有白色斑带。雌蝶有多种形态。', '玉带凤蝶', 'Papilio polytes', '2026-01-20 13:11:54.848183', 10);
INSERT INTO `insects` VALUES (5, 6, '2026-01-19 15:04:44.000000', '柑橘凤蝶是常见的凤蝶，幼虫以柑橘类植物为食。', '东亚地区', '果园、公园、山区', '[\"https://img2.baidu.com/it/u=258208445,1904874654&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=653\"]', '一年可发生多代，成虫喜欢访花。幼虫会模拟鸟粪来保护自己。', '成虫体长约30-35毫米，翅展约70-90毫米。翅黄色，有黑色斑纹，后翅有尾突和眼斑。', '柑橘凤蝶', 'Papilio xuthus', '2026-01-19 23:20:07.286707', 13);
INSERT INTO `insects` VALUES (6, 9, '2026-01-19 15:04:44.000000', '中华蜜蜂是中国本土的蜜蜂品种，适应性强。', '中国及周边国家', '山区、农村、城市公园', '[\"https://img2.baidu.com/it/u=3718227680,4196395826&fm=253&fmt=auto&app=138&f=JPEG?w=741&h=475\"]', '社会性昆虫，一个蜂群有蜂王、工蜂、雄蜂。工蜂负责采蜜、筑巢、育幼等工作。', '工蜂体长约10-13毫米，体色黑褐色，有黄色环带。蜂王体长约15-18毫米，雄蜂体长约12-14毫米。', '中华蜜蜂', 'Apis cerana', '2026-01-19 23:18:34.539036', 1);
INSERT INTO `insects` VALUES (7, 9, '2026-01-19 15:04:44.000000', '意大利蜜蜂是重要的经济昆虫，广泛用于养蜂业。', '世界各地（人工引入）', '人工饲养为主，也可见于野外', '[\"https://img1.baidu.com/it/u=581247122,753628928&fm=253&fmt=auto?w=534&h=500\"]', '高度社会性，蜂群结构复杂。工蜂寿命约30-60天，蜂王可活2-3年。', '工蜂体长约12-15毫米，体色较浅，有黄色环带。性情温顺，产蜜量高。', '意大利蜜蜂', 'Apis mellifera', '2026-01-20 13:34:15.498537', 2);
INSERT INTO `insects` VALUES (8, 9, '2026-01-19 15:04:44.000000', '熊蜂是重要的传粉昆虫，体型较大，毛茸茸的。', '北半球广泛分布', '温带和寒带地区', '[\"https://img1.baidu.com/it/u=247998832,3585777437&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=630\"]', '社会性昆虫，但蜂群规模较小。喜欢在早春和晚秋活动，耐寒性强。', '体长约15-25毫米，体色多样，多为黑色、黄色、白色相间。全身密被绒毛。', '熊蜂', 'Bombus spp.', '2026-01-19 18:47:55.764727', 2);
INSERT INTO `insects` VALUES (9, 10, '2026-01-19 15:04:44.000000', '黑蚂蚁是最常见的蚂蚁之一，适应性强。', '世界各地', '城市、农村、森林等各种环境', '[\"https://img2.baidu.com/it/u=4159376960,27321507&fm=253&fmt=auto&app=120&f=PNG?w=583&h=500\"]', '社会性昆虫，一个蚁群有蚁后、工蚁、兵蚁、雄蚁。工蚁负责觅食、筑巢、育幼。', '工蚁体长约3-5毫米，体色黑色或深褐色。蚁后体长约8-10毫米。', '黑蚂蚁', 'Lasius niger', '2026-01-19 23:45:55.270692', 5);
INSERT INTO `insects` VALUES (10, 10, '2026-01-19 15:04:44.000000', '红火蚁是入侵性害虫，具有强烈的攻击性。', '原产南美洲，现已入侵多个国家', '农田、草地、公园等', '[\"https://img0.baidu.com/it/u=1874084994,3854055762&fm=253&fmt=auto&app=120&f=JPEG?w=919&h=653\"]', '高度社会性，蚁群规模可达数十万只。攻击性强，被叮咬会引起疼痛和水泡。', '工蚁体长约2-6毫米，体色红褐色。头部较大，触角10节。', '红火蚁', 'Solenopsis invicta', '2026-01-20 12:13:00.405104', 1);
INSERT INTO `insects` VALUES (11, 10, '2026-01-19 15:04:44.000000', '切叶蚁是著名的农业蚂蚁，会切取叶片培养真菌。', '中美洲和南美洲', '热带和亚热带森林', '[\"https://img1.baidu.com/it/u=2265355943,1563328581&fm=253&fmt=auto&app=138&f=JPEG?w=749&h=500\"]', '高度社会性，分工明确。工蚁切取叶片带回巢穴，用于培养真菌作为食物。', '工蚁体长约5-15毫米，体色多为红褐色。头部大，上颚发达。', '切叶蚁', 'Atta spp.', '2026-01-19 17:10:52.721069', 1);
INSERT INTO `insects` VALUES (12, NULL, '2026-01-19 15:04:44.000000', '七星瓢虫是益虫，以蚜虫为食，是农业上的重要天敌。', '亚洲、欧洲、北美洲', '农田、草地、公园', '[\"https://img0.baidu.com/it/u=251397252,1080168103&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=569\"]', '一年可发生多代，成虫和幼虫都以蚜虫为食。有假死习性。', '成虫体长约6-8毫米，体色橙红色，有7个黑色斑点。', '七星瓢虫', 'Coccinella septempunctata', '2026-01-20 12:13:51.343111', 6);
INSERT INTO `insects` VALUES (13, NULL, '2026-01-19 15:04:44.000000', '蝉是夏季常见的昆虫，雄蝉会发出响亮的鸣叫声。', '中国、日本、韩国等', '树林、公园、城市绿化带', '[\"https://img2.baidu.com/it/u=2447727571,3042454168&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=636\"]', '生命周期长，若虫在地下生活数年，成虫寿命约1-2个月。雄蝉通过鸣叫吸引雌蝉。', '成虫体长约30-40毫米，体色黑色，有透明翅膀。头部有复眼和单眼。', '蝉', 'Cryptotympana atrata', '2026-01-19 15:04:44.000000', 0);
INSERT INTO `insects` VALUES (14, NULL, '2026-01-19 15:04:44.000000', '螳螂是捕食性昆虫，前足特化为捕捉足。', '世界各地', '草地、灌木丛、农田', '[\"https://img1.baidu.com/it/u=899755764,3861500414&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\"]', '捕食性，以其他昆虫为食。雌螳螂有\"食夫\"行为。', '成虫体长约50-75毫米，体色多为绿色或褐色。前胸长，前足发达，呈镰刀状。', '螳螂', 'Mantis religiosa', '2026-01-19 15:04:44.000000', 0);
INSERT INTO `insects` VALUES (15, NULL, '2026-01-19 15:04:44.000000', '蜻蜓是优秀的飞行者，是重要的捕食性昆虫。', '世界各地', '池塘、河流、湿地附近', '[\"https://img0.baidu.com/it/u=3518428959,4014364475&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1366\"]', '幼虫（水虿）在水中生活，成虫在空中捕食。飞行速度快，可悬停。', '成虫体长约30-80毫米，有两对透明翅膀，复眼大。体色多样，多为蓝色、绿色、红色等。', '蜻蜓', 'Libellula spp.', '2026-01-19 15:31:30.489103', 1);
INSERT INTO `insects` VALUES (16, NULL, '2026-01-19 15:04:44.000000', '蟋蟀是常见的鸣虫，雄虫会通过摩擦翅膀发出声音。', '世界各地', '草地、农田、公园', '[\"https://img1.baidu.com/it/u=1452964267,2926060635&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=651\"]', '夜行性，雄虫通过鸣叫吸引雌虫。以植物为食，也吃其他小昆虫。', '成虫体长约15-30毫米，体色多为黑色或褐色。后足发达，善跳跃。', '蟋蟀', 'Gryllus spp.', '2026-01-19 17:10:55.175080', 1);

-- ----------------------------
-- Table structure for notes
-- ----------------------------
DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `insect_id` bigint NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notes
-- ----------------------------

-- ----------------------------
-- Table structure for shares
-- ----------------------------
DROP TABLE IF EXISTS `shares`;
CREATE TABLE `shares`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment_count` int NULL DEFAULT 0,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `insect_id` bigint NULL DEFAULT NULL,
  `like_count` int NULL DEFAULT 0,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK3fm4apvet0nv9od7iomh0agj5`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK3fm4apvet0nv9od7iomh0agj5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shares
-- ----------------------------
INSERT INTO `shares` VALUES (2, 1, '2026-01-19 16:09:45.003796', '123131321321321', 'https://img1.baidu.com/it/u=1907957131,3138944965&fm=253&fmt=auto&app=138&f=JPEG?w=713&h=475', NULL, 0, '2026-01-19 21:46:58.023392', 1, '12122');
INSERT INTO `shares` VALUES (3, 2, '2026-01-19 17:11:48.545767', '很可爱的', 'https://img1.baidu.com/it/u=3757622542,1524744539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', NULL, 0, '2026-01-19 17:13:26.008027', 2, '测试发布');
INSERT INTO `shares` VALUES (4, 0, '2026-01-19 18:49:28.545939', '一只很好看的蜜蜂', 'https://img1.baidu.com/it/u=2139527481,1415494375&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=693', NULL, 0, '2026-01-19 18:49:28.545939', 3, '看到一只蜜蜂');
INSERT INTO `shares` VALUES (5, 1, '2026-01-19 23:19:44.763316', '11111111111111111111111111111111111111111111111111111111111111', 'https://img1.baidu.com/it/u=963224930,4309036&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750', NULL, 0, '2026-01-19 23:20:54.496489', 1, '测试发布哈哈');
INSERT INTO `shares` VALUES (7, 0, '2026-01-20 13:13:39.587533', '很好看的小昆虫111', 'https://img1.baidu.com/it/u=2866164979,3409086986&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1067', NULL, 0, '2026-01-20 13:23:58.413046', 4, '啦啦啦');
INSERT INTO `shares` VALUES (8, 0, '2026-01-20 13:35:47.397016', '一只小蜜蜂，哈哈哈哈哈', 'https://img0.baidu.com/it/u=158069970,3929031351&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=571', NULL, 0, '2026-01-20 13:36:15.279249', 1, '小蜜蜂');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKr43af9ap4edm43mmtq01oddj6`(`username` ASC) USING BTREE,
  UNIQUE INDEX `UK6dotkott2kjsp8vw4d0m25fb7`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000001007/ORIGINAL/NONE/image%2Fpng/1123673194', '2026-01-19 15:06:04.799538', 'tt@qq.com', 'cuckoo', '$2a$10$3YQj/Z99nmFJxnKlBs9ymOLfG0kHAT04d/dF3vff55xdncatPSXua', NULL, '2026-01-20 12:05:58.536637', 'cuckoo');
INSERT INTO `users` VALUES (2, NULL, '2026-01-19 17:10:06.401019', 'hh@qq.com', 'test', '$2a$10$EnRE6iHPWsG0XRxZrRWDcuBikLqtHHdjUj4x1/QZ5VVm8EQtUnnCu', NULL, '2026-01-19 17:10:06.401019', 'test');
INSERT INTO `users` VALUES (3, NULL, '2026-01-19 18:47:38.809856', 'test@qq.com', 'hahah', '$2a$10$ASQ7Upe9OJSlGcLN7RWVXOKFCv1LgnvQOczsaWiG3smwESR1f0ZyK', NULL, '2026-01-19 18:47:38.809856', '测试账号');
INSERT INTO `users` VALUES (4, 'content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000001005/ORIGINAL/NONE/image%2Fpng/2028791348', '2026-01-20 13:11:19.098087', 'Haha@qq.com', 'xixi', '$2a$10$a1Wx0EbVtDAHAw8f6h20Ue8uGWS4fwmt6r6qu6UgIrNWjIXOl9Yj2', NULL, '2026-01-20 13:14:01.186672', 'haha');

SET FOREIGN_KEY_CHECKS = 1;
