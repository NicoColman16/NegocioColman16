CREATE DATABASE  IF NOT EXISTS `negocio_colman` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `negocio_colman`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: LocalHost    Database: negocio_colman
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `id` varchar(255) NOT NULL,
  `fecha_venta` date DEFAULT NULL,
  `monto` int DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
INSERT INTO `venta` VALUES ('01a77e08-fc05-4bb5-bf77-fa5006387a25','2023-10-19',300,'Billetera'),('0275fb23-87af-4cb9-883b-02bdc739a5c8','2023-10-20',25,'Efectivo'),('0e5c4fd2-2a4e-4542-b94a-718bcca99a07','2023-10-21',2000,'Virtual'),('1fd79b5b-cd15-4481-97ec-3f302e9d8527','2023-10-19',250,'Billetera'),('25dac5f3-5139-41df-92d6-c1a80e0e26d6','2023-10-21',200,'Billetera'),('33c40097-3bc8-4956-ba30-c14369cdb84c','2023-10-19',200,'Billetera'),('3afc5282-030d-43c4-81b9-7485d658a5f0','2023-10-21',500,'Efectivo'),('53f8d00d-4ae1-4c8a-a8a8-8d981edc55b2','2023-10-21',250,'Efectivo'),('6ecc1301-9066-49d6-8726-01583dafa4b6','2023-10-21',1500,'Tarjeta'),('75e365ca-07b1-4272-900e-b40844af55bc','2023-10-21',2500,'Efectivo'),('7b1bcf67-0c5e-4d09-8d98-a33167d0054d','2023-10-19',200,'Billetera'),('7c2b3e81-90b6-42d0-bceb-c312a922bc27','2023-10-21',1000000,'Tarjeta'),('8190e138-c984-4050-9fbb-c664fcabef09','2023-10-21',300,'Efectivo'),('86ade485-338e-4535-82c6-66eca26a0dd2','2023-10-20',25,'Efectivo'),('89d407ab-c23b-44d7-bd81-28862c007e90','2023-10-21',1500,'Tarjeta'),('a2828e01-4f85-4f4a-a463-94ee24322454','2023-10-21',200,'Billetera'),('b388168f-175d-4a85-84e0-a73a1489761b','2023-10-21',2500,'Virtual'),('d3c09d6a-67a1-4445-af1d-39440a6b5ffc','2023-10-20',5000,'Efectivo'),('db106558-541e-45f6-b930-ee85eb040cca','2023-10-19',300,'Billetera'),('e18b4b17-1d5b-4267-877e-37986e2a0c28','2023-10-19',522,'Billetera'),('fd87eb6f-44b2-4565-8da4-32e27e56b912','2023-10-19',300,'Billetera');
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-24 21:49:45
