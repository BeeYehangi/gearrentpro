-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: gearrentpro
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch` (
  `branch_id` varchar(10) NOT NULL,
  `branch_name` varchar(100) NOT NULL,
  `address` varchar(255) NOT NULL,
  `contact_phone` varchar(15) DEFAULT NULL,
  `contact_email` varchar(100) DEFAULT NULL,
  `manager_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES ('BR001','Panadura Main','123 Galle Road, Panadura','038-2234567','panadura@gearrentpro.lk','U002'),('BR002','Galle City','45 Church Street, Galle','091-2237890','galle@gearrentpro.lk','U004'),('BR003','Colombo Fort','78 York Street, Colombo 01','011-2234455','colombo@gearrentpro.lk',NULL);
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` varchar(10) NOT NULL,
  `category_name` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `base_price_factor` decimal(5,2) DEFAULT '1.00',
  `weekend_multiplier` decimal(5,2) DEFAULT '1.00',
  `late_fee_per_day` decimal(10,2) DEFAULT '1000.00',
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('CAT001','Camera','Professional cameras and DSLRs',1.50,1.20,1500.00,0),('CAT002','Drone','Professional drones and aerial equipment',2.00,1.30,2000.00,0),('CAT003','Lens','Camera lenses and attachments',1.20,1.10,1000.00,1),('CAT004','Lighting','Studio and portable lighting kits',1.30,1.15,1200.00,1),('CAT005','Audio','Microphones and audio equipment',1.10,1.10,800.00,1);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` varchar(10) NOT NULL,
  `nic_passport` varchar(20) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `membership_id` varchar(10) DEFAULT 'REG',
  `total_deposit_held` decimal(12,2) DEFAULT '0.00',
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `nic_passport` (`nic_passport`),
  KEY `membership_id` (`membership_id`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`membership_id`) REFERENCES `membership` (`membership_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('C001','199012345678','Amal Perera','amal@email.com','0771234567','12 Main St, Colombo','REG',25000.00,1,'2026-04-04 14:29:02'),('C002','198756781234','Nirosha Fernando','nirosha@email.com','0777654321','45 Beach Rd, Galle','SIL',0.00,1,'2026-04-04 14:29:02'),('C003','200134561234','Kasun Silva','kasun@email.com','0769876543','78 Hill St, Kandy','GOLD',0.00,1,'2026-04-04 14:29:02'),('C004','199567894321','Dilani Jayawardena','dilani@email.com','0751234321','23 Lake Rd, Negombo','REG',0.00,1,'2026-04-04 14:29:02'),('C005','198934567890','Ruwan Bandara','ruwan@email.com','0712345678','56 Fort Rd, Galle','SIL',0.00,1,'2026-04-04 14:29:02'),('C006','199234567890','Chamara Wickrama','chamara@email.com','0723456789','34 Temple Rd, Colombo','GOLD',27000.00,1,'2026-04-04 14:54:54'),('C007','200056781234','Sanduni Perera','sanduni@email.com','0734567890','67 Sea Rd, Negombo','SIL',30000.00,1,'2026-04-04 14:54:54'),('C008','198812345670','Pradeep Kumar','pradeep@email.com','0745678901','89 Main Rd, Galle','REG',10000.00,1,'2026-04-04 14:54:54'),('C009','199945678901','Malini Silva','malini@email.com','0756789012','12 Park Rd, Kandy','GOLD',0.00,1,'2026-04-04 14:54:54'),('C010','200167890123','Tharaka Jayasena','tharaka@email.com','0767890123','45 Hill Rd, Colombo','REG',0.00,1,'2026-04-04 14:54:54');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `damage`
--

DROP TABLE IF EXISTS `damage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `damage` (
  `damage_id` int NOT NULL AUTO_INCREMENT,
  `rental_id` varchar(10) NOT NULL,
  `equipment_id` varchar(10) NOT NULL,
  `description` text NOT NULL,
  `charge_amount` decimal(10,2) NOT NULL,
  `reported_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`damage_id`),
  KEY `rental_id` (`rental_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `reported_by` (`reported_by`),
  CONSTRAINT `damage_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rental` (`rental_id`),
  CONSTRAINT `damage_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
  CONSTRAINT `damage_ibfk_3` FOREIGN KEY (`reported_by`) REFERENCES `system_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `damage`
--

LOCK TABLES `damage` WRITE;
/*!40000 ALTER TABLE `damage` DISABLE KEYS */;
/*!40000 ALTER TABLE `damage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` varchar(10) NOT NULL,
  `category_id` varchar(10) NOT NULL,
  `branch_id` varchar(10) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `serial_number` varchar(50) DEFAULT NULL,
  `purchase_year` year DEFAULT NULL,
  `base_daily_price` decimal(10,2) NOT NULL,
  `deposit_amount` decimal(10,2) NOT NULL,
  `status` enum('AVAILABLE','RESERVED','RENTED','UNDER_MAINTENANCE') DEFAULT 'AVAILABLE',
  `notes` text,
  PRIMARY KEY (`equipment_id`),
  UNIQUE KEY `serial_number` (`serial_number`),
  KEY `category_id` (`category_id`),
  KEY `branch_id` (`branch_id`),
  CONSTRAINT `equipment_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `equipment_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES ('EQ001','CAT001','BR001','Sony','Alpha A7III','SN-A7III-001',2022,5000.00,25000.00,'RENTED',NULL),('EQ002','CAT001','BR001','Canon','EOS R5','SN-R5-001',2023,6000.00,30000.00,'AVAILABLE',NULL),('EQ003','CAT002','BR001','DJI','Mavic 3 Pro','SN-MAV3-001',2023,8000.00,40000.00,'AVAILABLE',NULL),('EQ004','CAT003','BR002','Sony','FE 24-70mm','SN-2470-001',2021,3000.00,15000.00,'AVAILABLE',NULL),('EQ005','CAT004','BR002','Godox','SL-60W Kit','SN-SL60-001',2022,2500.00,12000.00,'AVAILABLE',NULL),('EQ006','CAT005','BR002','Rode','NTG5 Shotgun','SN-NTG5-001',2022,2000.00,10000.00,'RENTED',NULL),('EQ007','CAT001','BR003','Nikon','Z6 II','SN-Z6II-001',2022,5500.00,27000.00,'RENTED',NULL),('EQ008','CAT002','BR003','DJI','Air 2S','SN-AIR2S-001',2023,6000.00,30000.00,'RENTED',NULL),('EQ009','CAT003','BR001','Canon','EF 50mm f1.8','SN-50MM-001',2021,1500.00,8000.00,'AVAILABLE',NULL),('EQ010','CAT004','BR001','Godox','AD200 Pro','SN-AD200-001',2022,3500.00,18000.00,'AVAILABLE',NULL),('EQ011','CAT005','BR001','Rode','Wireless GO II','SN-WIGO-001',2023,2500.00,12000.00,'AVAILABLE',NULL),('EQ012','CAT002','BR001','DJI','Mini 3 Pro','SN-MINI3-001',2023,5000.00,25000.00,'AVAILABLE',NULL),('EQ013','CAT001','BR002','Sony','Alpha A6400','SN-A6400-001',2021,4000.00,20000.00,'AVAILABLE',NULL),('EQ014','CAT003','BR002','Sigma','18-35mm f1.8','SN-1835-001',2022,3500.00,17000.00,'AVAILABLE',NULL),('EQ015','CAT004','BR002','Aputure','120D II','SN-120D-001',2022,4500.00,22000.00,'AVAILABLE',NULL),('EQ016','CAT005','BR002','Zoom','H6 Recorder','SN-H6-001',2021,1800.00,9000.00,'AVAILABLE',NULL),('EQ017','CAT001','BR003','Canon','EOS 90D','SN-90D-001',2022,4500.00,22000.00,'AVAILABLE',NULL),('EQ018','CAT003','BR003','Tamron','24-70mm f2.8','SN-2470T-001',2022,3200.00,16000.00,'AVAILABLE',NULL),('EQ019','CAT004','BR003','Godox','SL-100D','SN-SL100-001',2023,3000.00,15000.00,'AVAILABLE',NULL),('EQ020','CAT005','BR003','Sennheiser','MKE 600','SN-MKE600-001',2022,2200.00,11000.00,'AVAILABLE',NULL);
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership`
--

DROP TABLE IF EXISTS `membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership` (
  `membership_id` varchar(10) NOT NULL,
  `level_name` varchar(20) NOT NULL,
  `discount_percentage` decimal(5,2) DEFAULT '0.00',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`membership_id`),
  UNIQUE KEY `level_name` (`level_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership`
--

LOCK TABLES `membership` WRITE;
/*!40000 ALTER TABLE `membership` DISABLE KEYS */;
INSERT INTO `membership` VALUES ('GOLD','Gold',10.00,'10% discount on rentals'),('REG','Regular',0.00,'Standard membership'),('SIL','Silver',5.00,'5% discount on rentals');
/*!40000 ALTER TABLE `membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental`
--

DROP TABLE IF EXISTS `rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental` (
  `rental_id` varchar(10) NOT NULL,
  `reservation_id` varchar(10) DEFAULT NULL,
  `equipment_id` varchar(10) NOT NULL,
  `customer_id` varchar(10) NOT NULL,
  `branch_id` varchar(10) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `actual_return_date` date DEFAULT NULL,
  `calculated_rental_amount` decimal(12,2) NOT NULL,
  `security_deposit` decimal(12,2) NOT NULL,
  `membership_discount` decimal(12,2) DEFAULT '0.00',
  `long_rental_discount` decimal(12,2) DEFAULT '0.00',
  `final_payable_amount` decimal(12,2) NOT NULL,
  `late_fee` decimal(12,2) DEFAULT '0.00',
  `damage_charges` decimal(12,2) DEFAULT '0.00',
  `payment_status` enum('PAID','UNPAID') DEFAULT 'UNPAID',
  `rental_status` enum('ACTIVE','RETURNED','OVERDUE','CANCELLED') DEFAULT 'ACTIVE',
  `created_by` varchar(10) DEFAULT NULL,
  `returned_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`rental_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `customer_id` (`customer_id`),
  KEY `branch_id` (`branch_id`),
  KEY `reservation_id` (`reservation_id`),
  KEY `created_by` (`created_by`),
  KEY `returned_by` (`returned_by`),
  CONSTRAINT `rental_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
  CONSTRAINT `rental_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `rental_ibfk_3` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`),
  CONSTRAINT `rental_ibfk_4` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`),
  CONSTRAINT `rental_ibfk_5` FOREIGN KEY (`created_by`) REFERENCES `system_user` (`user_id`),
  CONSTRAINT `rental_ibfk_6` FOREIGN KEY (`returned_by`) REFERENCES `system_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental`
--

LOCK TABLES `rental` WRITE;
/*!40000 ALTER TABLE `rental` DISABLE KEYS */;
INSERT INTO `rental` VALUES ('RNT001',NULL,'EQ001','C001','BR003','2026-04-05','2026-04-09',NULL,39000.00,25000.00,0.00,0.00,39000.00,0.00,0.00,'PAID','ACTIVE','U001',NULL),('RNT002',NULL,'EQ003','C002','BR001','2026-03-10','2026-03-15',NULL,48000.00,40000.00,2400.00,0.00,45600.00,0.00,0.00,'PAID','RETURNED','U003',NULL),('RNT003',NULL,'EQ004','C003','BR002','2026-03-15','2026-03-20',NULL,18000.00,15000.00,1800.00,0.00,16200.00,0.00,0.00,'PAID','RETURNED','U004',NULL),('RNT004',NULL,'EQ002','C004','BR001','2026-03-20','2026-03-22','2026-03-25',18000.00,30000.00,0.00,0.00,18000.00,3000.00,0.00,'PAID','RETURNED','U003','U003'),('RNT005',NULL,'EQ005','C005','BR002','2026-03-25','2026-03-28',NULL,7500.00,12000.00,375.00,0.00,7125.00,0.00,0.00,'PAID','RETURNED','U004',NULL),('RNT006',NULL,'EQ007','C006','BR003','2026-03-28','2026-04-02',NULL,27500.00,27000.00,2750.00,0.00,24750.00,0.00,0.00,'UNPAID','ACTIVE','U004',NULL),('RNT007',NULL,'EQ008','C007','BR003','2026-03-20','2026-03-25',NULL,30000.00,30000.00,1500.00,0.00,28500.00,0.00,0.00,'UNPAID','ACTIVE','U004',NULL),('RNT008',NULL,'EQ006','C008','BR002','2026-03-15','2026-03-20',NULL,10000.00,10000.00,0.00,0.00,10000.00,0.00,0.00,'UNPAID','ACTIVE','U004',NULL);
/*!40000 ALTER TABLE `rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` varchar(10) NOT NULL,
  `equipment_id` varchar(10) NOT NULL,
  `customer_id` varchar(10) NOT NULL,
  `branch_id` varchar(10) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED','CONVERTED') DEFAULT 'PENDING',
  `created_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `customer_id` (`customer_id`),
  KEY `branch_id` (`branch_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`),
  CONSTRAINT `reservation_ibfk_4` FOREIGN KEY (`created_by`) REFERENCES `system_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES ('RES001','EQ001','C001','BR003','2026-04-05','2026-04-09','PENDING','U001'),('RES002','EQ013','C002','BR002','2026-04-12','2026-04-18','PENDING','U004');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
  `config_key` varchar(50) NOT NULL,
  `config_value` varchar(255) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES ('LONG_RENTAL_DAYS','7','Days for long rental'),('LONG_RENTAL_DISCOUNT','10','Discount percentage'),('MAX_DEPOSIT_PER_CUSTOMER','500000','Deposit limit'),('MAX_RENTAL_DAYS','30','Maximum rental duration');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_user`
--

DROP TABLE IF EXISTS `system_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_user` (
  `user_id` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('ADMIN','BRANCH_MANAGER','STAFF') NOT NULL,
  `branch_id` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `branch_id` (`branch_id`),
  CONSTRAINT `system_user_ibfk_1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_user`
--

LOCK TABLES `system_user` WRITE;
/*!40000 ALTER TABLE `system_user` DISABLE KEYS */;
INSERT INTO `system_user` VALUES ('U001','admin','admin123','System Administrator','ADMIN',NULL,'admin@gearrentpro.lk','0111111111',1),('U002','manager1','manager123','Kamal Perera','BRANCH_MANAGER','BR001','kamal@gearrentpro.lk','0382222222',1),('U003','staff1','staff123','Nimal Silva','STAFF','BR001','nimal@gearrentpro.lk','0383333333',1),('U004','manager2','manager123','Samantha Rajapakse','BRANCH_MANAGER','BR002','samantha@gearrentpro.lk','0914444444',1);
/*!40000 ALTER TABLE `system_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_overdue_rentals`
--

DROP TABLE IF EXISTS `vw_overdue_rentals`;
/*!50001 DROP VIEW IF EXISTS `vw_overdue_rentals`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_overdue_rentals` AS SELECT 
 1 AS `rental_id`,
 1 AS `full_name`,
 1 AS `brand`,
 1 AS `model`,
 1 AS `branch_name`,
 1 AS `days_overdue`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_overdue_rentals`
--

/*!50001 DROP VIEW IF EXISTS `vw_overdue_rentals`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_overdue_rentals` AS select `r`.`rental_id` AS `rental_id`,`c`.`full_name` AS `full_name`,`e`.`brand` AS `brand`,`e`.`model` AS `model`,`b`.`branch_name` AS `branch_name`,(to_days(curdate()) - to_days(`r`.`end_date`)) AS `days_overdue` from (((`rental` `r` join `customer` `c` on((`r`.`customer_id` = `c`.`customer_id`))) join `equipment` `e` on((`r`.`equipment_id` = `e`.`equipment_id`))) join `branch` `b` on((`r`.`branch_id` = `b`.`branch_id`))) where ((`r`.`rental_status` = 'ACTIVE') and (curdate() > `r`.`end_date`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-04 21:07:47
