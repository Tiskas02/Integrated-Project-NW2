CREATE TABLE IF NOT EXISTS `kradanitbangmod`.`taskv2` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `taskTitle` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `taskDescription` VARCHAR(500) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `taskAssignees` VARCHAR(30) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `createdOn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `taskStatusId` INT NOT NULL,
  PRIMARY KEY (`id`, `taskStatusId`),
  INDEX `fk_taskStatusId` (`taskStatusId` ASC) VISIBLE,
  CONSTRAINT `fk_taskStatusId`
    FOREIGN KEY (`taskStatusId`)
    REFERENCES `kradanitbangmod`.`status` (`statusId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
SET GLOBAL time_zone = '+00:00';
SET time_zone = '+00:00';

INSERT INTO taskv2 (taskTitle, taskDescription, taskAssignees, taskStatusId, createdOn, updatedOn) 
VALUES ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0', 
'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5', 
'Assignees1Assignees2Assignees3', 1 , '2024-04-22 09:00:00', '2024-04-22 09:00:00');

INSERT INTO taskv2 (taskTitle, taskDescription, taskAssignees, taskStatusId, createdOn, updatedOn) 
VALUES ('Repository', null , null , 4 , '2024-04-22 09:05:00', '2024-04-22 14:00:00');

INSERT INTO taskv2 (taskTitle, taskDescription, taskAssignees, taskStatusId, createdOn, updatedOn) 
VALUES ('ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)' , 3 , '2024-04-22 09:10:00', '2024-04-25 00:00:00');

INSERT INTO taskv2 (taskTitle, taskDescription, taskAssignees, taskStatusId, createdOn, updatedOn) 
VALUES ('_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน' , 2 , '2024-04-22 09:15:00', '2024-04-22 10:00:00');