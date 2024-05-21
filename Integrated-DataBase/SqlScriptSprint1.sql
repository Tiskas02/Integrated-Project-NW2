create database kradanItBangMod;
use kradanItBangMod;
CREATE TABLE `task` (
    `taskId` INT NOT NULL AUTO_INCREMENT,
    `taskTitle` VARCHAR(100) NOT NULL,
    `taskDescription` VARCHAR(500) DEFAULT NULL,
    `taskAssignees` VARCHAR(30) DEFAULT NULL,
    `taskStatus` ENUM('NO_STATUS', 'TO_DO', 'DOING', 'DONE') NOT NULL DEFAULT 'NO_STATUS',
    `createdOn` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updatedOn` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`taskId`),
    UNIQUE KEY `taskId_UNIQUE` (`taskId`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

SET GLOBAL time_zone = '+00:00';

SELECT @@global.time_zone AS global_time_zone, @@session.time_zone AS session_time_zone;

INSERT INTO task (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) 
VALUES ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0', 
'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5', 
'Assignees1Assignees2Assignees3', 'No_Status' , '2024-04-22 09:00:00', '2024-04-22 09:00:00');

INSERT INTO task (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) 
VALUES ('Repository', null , null , 'To_Do' , '2024-04-22 09:05:00', '2024-04-22 14:00:00');

INSERT INTO task (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) 
VALUES ('ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)' , 'Doing' , '2024-04-22 09:10:00', '2024-04-25 00:00:00');

INSERT INTO task (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) 
VALUES ('_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน' , 'Done' , '2024-04-22 09:15:00', '2024-04-22 10:00:00');
