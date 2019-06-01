CREATE DATABASE if not exists `demo` CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`
(
    `ID`         int(10)      NOT NULL AUTO_INCREMENT,
    `NAME`       varchar(100) NOT NULL,
    `BRANCH`     varchar(255) NOT NULL,
    `PERCENTAGE` int(3)       NOT NULL,
    `PHONE`      int(10)      NOT NULL,
    `EMAIL`      varchar(255) NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`
(
    `ID`   int(10)      NOT NULL AUTO_INCREMENT,
    `NAME` varchar(100) NOT NULL,
    `age`  int(3)       NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `ID`       int(10)      NOT NULL AUTO_INCREMENT,
    `username` varchar(100) NOT NULL,
    `password` varchar(100) NOT NULL,
    `age`      int(3)       NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;

/*DROP TABLE IF EXISTS `user`;
CREATE TABLE user
(
    id          INT(10) unsigned PRIMARY KEY NOT NULL COMMENT '用户编号' AUTO_INCREMENT,
    user_name   VARCHAR(25) COMMENT '用户名称',
    description VARCHAR(25) COMMENT '描述'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;*/

DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '城市编号',
    `province_id` int(10) unsigned NOT NULL COMMENT '省份编号',
    `city_name`   varchar(25) DEFAULT NULL COMMENT '城市名称',
    `description` varchar(25) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;