CREATE DATABASE if not exists `demo` CHARACTER SET utf8 COLLATE utf8_bin;



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
