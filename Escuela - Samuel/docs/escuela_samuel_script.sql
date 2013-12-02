SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `escuela_samuel` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `escuela_samuel` ;

-- -----------------------------------------------------
-- Table `escuela_samuel`.`alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`alumno` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido_paterno` VARCHAR(45) NOT NULL,
  `apellido_materno` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`profesor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`profesor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido_paterno` VARCHAR(45) NOT NULL,
  `apellido_materno` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`grupo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`grupo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `fecha_creacion` DATE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`materia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`materia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `plan` DATE NOT NULL,
  `grupo_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_materia_grupo_idx` (`grupo_id` ASC),
  CONSTRAINT `fk_materia_grupo`
    FOREIGN KEY (`grupo_id`)
    REFERENCES `escuela_samuel`.`grupo` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`calificacion_grupo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`calificacion_grupo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `alumno_id` INT NOT NULL,
  `grupo_id` INT NOT NULL,
  `calificacion` DOUBLE NOT NULL DEFAULT 0,
  `fecha_creada` DATE NOT NULL,
  `fecha_editada` DATE NOT NULL,
  PRIMARY KEY (`id`, `alumno_id`, `grupo_id`),
  INDEX `fk_alumno_has_grupo_grupo1_idx` (`grupo_id` ASC),
  INDEX `fk_alumno_has_grupo_alumno1_idx` (`alumno_id` ASC),
  CONSTRAINT `fk_alumno_has_grupo_alumno1`
    FOREIGN KEY (`alumno_id`)
    REFERENCES `escuela_samuel`.`alumno` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_has_grupo_grupo1`
    FOREIGN KEY (`grupo_id`)
    REFERENCES `escuela_samuel`.`grupo` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`materia_asignada`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`materia_asignada` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `profesor_id` INT NOT NULL,
  `materia_id` INT NOT NULL,
  `fecha_asignacion` DATE NOT NULL,
  PRIMARY KEY (`id`, `profesor_id`, `materia_id`),
  INDEX `fk_profesor_has_materia_materia1_idx` (`materia_id` ASC),
  INDEX `fk_profesor_has_materia_profesor1_idx` (`profesor_id` ASC),
  CONSTRAINT `fk_profesor_has_materia_profesor1`
    FOREIGN KEY (`profesor_id`)
    REFERENCES `escuela_samuel`.`profesor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_profesor_has_materia_materia1`
    FOREIGN KEY (`materia_id`)
    REFERENCES `escuela_samuel`.`materia` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `escuela_samuel`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escuela_samuel`.`usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
