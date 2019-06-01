CREATE DATABASE if not exists `test` CHARACTER SET utf8 COLLATE utf8_bin;
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
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;