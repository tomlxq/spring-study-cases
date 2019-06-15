CREATE DATABASE if not exists `demo` CHARACTER SET utf8 COLLATE utf8_bin;
drop table if exists `t_user`;
CREATE TABLE `t_user`
(
    `ID`   int(10)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `NAME` varchar(100) NOT null
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;


