CREATE DATABASE `my_notes` CHARACTER SET 'utf8mb4';

USE `my_notes`;

CREATE TABLE `note` (
                        `noteId`    INT(11)         AUTO_INCREMENT,
                        `ownerId`   INT(11)         DEFAULT 0,
                        `title`     VARCHAR(255)    DEFAULT '',
                        `text`      LONGTEXT,
                        `isPublic`  BOOL            DEFAULT FALSE,
                        `isDeleted` BOOL            DEFAULT FALSE,
                        `created`   DATETIME        DEFAULT '0001-01-01 00:00:01',
                        `modified`  DATETIME        DEFAULT '0001-01-01 00:00:01',
                        PRIMARY KEY (`noteId`),
                        KEY (`ownerId`),
                        KEY (`modified`)
) ENGINE = INNODB;

CREATE TABLE `share` (
                         `noteId`    INT(11)         DEFAULT 0,
                         `userId`    INT(11)         DEFAULT 0,
                         `modified`  DATETIME        DEFAULT '0001-01-01 00:00:01',
                         PRIMARY KEY (`noteId`, `userId`),
                         KEY (`modified`)
) ENGINE = INNODB;

CREATE TABLE `user` (
                         `userId`    INT(11)         DEFAULT 0,
                         `login`     VARCHAR(255)    DEFAULT '',
                         `isAdmin`   BOOL            DEFAULT FALSE,
                         PRIMARY KEY (`userId`)
) ENGINE = INNODB;