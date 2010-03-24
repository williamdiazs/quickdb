-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.31-1ubuntu2


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema testQuickDB
--

CREATE DATABASE IF NOT EXISTS testQuickDB;
USE testQuickDB;

--
-- Definition of table `testQuickDB`.`Dog`
--

DROP TABLE IF EXISTS `testQuickDB`.`Dog`;
CREATE TABLE  `testQuickDB`.`Dog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `color` varchar(20) NOT NULL,
  `idRace` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `testQuickDB`.`Dog`
--

/*!40000 ALTER TABLE `Dog` DISABLE KEYS */;
LOCK TABLES `Dog` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `Dog` ENABLE KEYS */;


--
-- Definition of table `testQuickDB`.`Person`
--

DROP TABLE IF EXISTS `testQuickDB`.`Person`;
CREATE TABLE  `testQuickDB`.`Person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `testQuickDB`.`Person`
--

/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
LOCK TABLES `Person` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;


--
-- Definition of table `testQuickDB`.`Race`
--

DROP TABLE IF EXISTS `testQuickDB`.`Race`;
CREATE TABLE  `testQuickDB`.`Race` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `testQuickDB`.`Race`
--

/*!40000 ALTER TABLE `Race` DISABLE KEYS */;
LOCK TABLES `Race` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `Race` ENABLE KEYS */;


--
-- Definition of table `testQuickDB`.`address`
--

DROP TABLE IF EXISTS `testQuickDB`.`address`;
CREATE TABLE  `testQuickDB`.`address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `street` varchar(200) NOT NULL,
  `idDistrict` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `testQuickDB`.`address`
--

/*!40000 ALTER TABLE `address` DISABLE KEYS */;
LOCK TABLES `address` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;


--
-- Definition of table `testQuickDB`.`district`
--

DROP TABLE IF EXISTS `testQuickDB`.`district`;
CREATE TABLE  `testQuickDB`.`district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `testQuickDB`.`district`
--

/*!40000 ALTER TABLE `district` DISABLE KEYS */;
LOCK TABLES `district` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `district` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
