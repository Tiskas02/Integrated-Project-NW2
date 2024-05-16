CREATE TABLE IF NOT EXISTS `kradanitbangmod`.`status` (
  `statusId` INT NOT NULL  AUTO_INCREMENT,
  `name` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`statusId`),
  UNIQUE INDEX `statusId_UNIQUE` (`statusId` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;

INSERT INTO `status` (`name`,`description`) VALUES
('No Status', 'The default status'),
('To Do',null),
('Doing','Being worked on'),
('Done','Finished');