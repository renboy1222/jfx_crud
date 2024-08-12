
CREATE DATABASE /*!32312 IF NOT EXISTS*/`javafx_tutorial` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `javafx_tutorial`;

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `course` VARCHAR(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `course` */

INSERT  INTO `course`(`id`,`course`) VALUES (1,'BSIT'),(2,'BSCS'),(3,'BSN'),(4,'BSEED'),(5,'BSED'),(6,'BSA'),(7,'BSBA'),(8,'BSTH'),(9,'BSAG'),(10,'BSIS'),(11,'BSEEd'),(13,'BSSS');

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(60) DEFAULT NULL,
  `surname` VARCHAR(60) DEFAULT NULL,
  `email` VARCHAR(60) DEFAULT NULL,
  `address` VARCHAR(220) DEFAULT NULL,
  `course_id` BIGINT DEFAULT NULL,
  KEY `course_id` (`course_id`),
  KEY `id` (`id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `student` */

INSERT  INTO `student`(`id`,`firstname`,`surname`,`email`,`address`,`course_id`) VALUES (1,'ALDRIN','PAGE','ALDRINPAGE@GMAIL.COM','MANDAUE CITY, CEBU',2),(2,'JAMES','BILTRAN','ALDRINGG@gmail.com','LAPU-LAPU',6);


