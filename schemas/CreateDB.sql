CREATE DATABASE  IF NOT EXISTS `openppm` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `openppm`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: openppm
-- ------------------------------------------------------
-- Server version	5.6.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activityseller`
--

DROP TABLE IF EXISTS `activityseller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activityseller` (
  `idActivitySeller` int(11) NOT NULL AUTO_INCREMENT,
  `idActivity` int(11) NOT NULL,
  `idSeller` int(11) NOT NULL,
  `statementOfWork` varchar(200) DEFAULT NULL,
  `procurementDocuments` varchar(100) DEFAULT NULL,
  `baselineStart` date DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `baselineFinish` date DEFAULT NULL,
  `finishDate` date DEFAULT NULL,
  `workScheduleInfo` varchar(200) DEFAULT NULL,
  `sellerPerformanceInfo` varchar(200) DEFAULT NULL,
  `idProjectAssociate` int(11) DEFAULT NULL,
  `indirect` bit(1) DEFAULT b'0',
  PRIMARY KEY (`idActivitySeller`),
  KEY `idActivity` (`idActivity`),
  KEY `idSeller` (`idSeller`),
  KEY `ACTIVITYSELLER_PROJECT_FK` (`idProjectAssociate`),
  CONSTRAINT `ACTIVITYSELLER_ACTIVITY_FK` FOREIGN KEY (`idActivity`) REFERENCES `projectactivity` (`IdActivity`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACTIVITYSELLER_PROJECT_FK` FOREIGN KEY (`idProjectAssociate`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `ACTIVITYSELLER_SELLER_FK` FOREIGN KEY (`idSeller`) REFERENCES `seller` (`idSeller`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activityseller`
--

LOCK TABLES `activityseller` WRITE;
/*!40000 ALTER TABLE `activityseller` DISABLE KEYS */;
/*!40000 ALTER TABLE `activityseller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assumptionreassessmentlog`
--

DROP TABLE IF EXISTS `assumptionreassessmentlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assumptionreassessmentlog` (
  `IdLog` int(11) NOT NULL AUTO_INCREMENT,
  `IdAssumption` int(11) DEFAULT NULL,
  `Assumption_date` date DEFAULT NULL,
  `ASSUMPTIONCHANGE` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`IdLog`),
  KEY `IdAssumption` (`IdAssumption`),
  CONSTRAINT `ASSREALOG_ASSREG_FK` FOREIGN KEY (`IdAssumption`) REFERENCES `assumptionregister` (`IdAssumption`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assumptionreassessmentlog`
--

LOCK TABLES `assumptionreassessmentlog` WRITE;
/*!40000 ALTER TABLE `assumptionreassessmentlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `assumptionreassessmentlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assumptionregister`
--

DROP TABLE IF EXISTS `assumptionregister`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assumptionregister` (
  `IdAssumption` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `AssumptionCode` varchar(5) DEFAULT NULL,
  `AssumptionName` varchar(50) DEFAULT NULL,
  `Originator` varchar(100) DEFAULT NULL,
  `AssumptionDoc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdAssumption`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `ASSUMPTIONREGISTER_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assumptionregister`
--

LOCK TABLES `assumptionregister` WRITE;
/*!40000 ALTER TABLE `assumptionregister` DISABLE KEYS */;
/*!40000 ALTER TABLE `assumptionregister` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bscdimension`
--

DROP TABLE IF EXISTS `bscdimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bscdimension` (
  `idBscDimension` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idBscDimension`),
  KEY `FK_bscdimension_1` (`idCompany`),
  CONSTRAINT `BSCDIM_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bscdimension`
--

LOCK TABLES `bscdimension` WRITE;
/*!40000 ALTER TABLE `bscdimension` DISABLE KEYS */;
INSERT INTO `bscdimension` VALUES (1,'Financial',1),(2,'Customer',1),(3,'Internal Business Process',1),(4,'Innovation and Learning',1);
/*!40000 ALTER TABLE `bscdimension` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budgetaccounts`
--

DROP TABLE IF EXISTS `budgetaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budgetaccounts` (
  `IdBudgetAccount` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(100) DEFAULT NULL,
  `typeCost` int(11) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdBudgetAccount`),
  KEY `FK_budgetaccounts_1` (`idCompany`),
  CONSTRAINT `BUDGETACCOUNTS_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budgetaccounts`
--

LOCK TABLES `budgetaccounts` WRITE;
/*!40000 ALTER TABLE `budgetaccounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `budgetaccounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessdriver`
--

DROP TABLE IF EXISTS `businessdriver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessdriver` (
  `idBusinessDriver` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `relativePriorization` double DEFAULT '0',
  `idCompany` int(11) NOT NULL,
  `idBusinessDriverSet` int(11) NOT NULL,
  PRIMARY KEY (`idBusinessDriver`),
  KEY `BD_BDSET_FK` (`idBusinessDriverSet`),
  KEY `BD_COMPANY_FK` (`idCompany`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessdriver`
--

LOCK TABLES `businessdriver` WRITE;
/*!40000 ALTER TABLE `businessdriver` DISABLE KEYS */;
/*!40000 ALTER TABLE `businessdriver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessdriverset`
--

DROP TABLE IF EXISTS `businessdriverset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessdriverset` (
  `idBusinessDriverSet` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idBusinessDriverSet`),
  KEY `BDSET_COMPANY_FK` (`idCompany`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessdriverset`
--

LOCK TABLES `businessdriverset` WRITE;
/*!40000 ALTER TABLE `businessdriverset` DISABLE KEYS */;
/*!40000 ALTER TABLE `businessdriverset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendarbase`
--

DROP TABLE IF EXISTS `calendarbase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendarbase` (
  `idCalendarBase` int(11) NOT NULL AUTO_INCREMENT,
  `weekStart` int(11) DEFAULT NULL,
  `fiscalYearStart` int(11) DEFAULT NULL,
  `startTime1` double DEFAULT NULL,
  `startTime2` double DEFAULT NULL,
  `endTime1` double DEFAULT NULL,
  `endTime2` double DEFAULT NULL,
  `hoursDay` double DEFAULT NULL,
  `hoursWeek` double DEFAULT NULL,
  `daysMonth` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCalendarBase`),
  KEY `FK_calendarbase_1` (`idCompany`),
  CONSTRAINT `CALENDARBASE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendarbase`
--

LOCK TABLES `calendarbase` WRITE;
/*!40000 ALTER TABLE `calendarbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendarbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendarbaseexceptions`
--

DROP TABLE IF EXISTS `calendarbaseexceptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendarbaseexceptions` (
  `IdCalendarBaseException` int(11) NOT NULL AUTO_INCREMENT,
  `IdCalendarBase` int(11) DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  `FinishDate` date DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdCalendarBaseException`),
  KEY `IdCalendarBase` (`IdCalendarBase`),
  CONSTRAINT `CALBASEEXCEP_CALBASE_FK` FOREIGN KEY (`IdCalendarBase`) REFERENCES `calendarbase` (`idCalendarBase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendarbaseexceptions`
--

LOCK TABLES `calendarbaseexceptions` WRITE;
/*!40000 ALTER TABLE `calendarbaseexceptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendarbaseexceptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `IdCategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdCategory`),
  KEY `FK_category_1` (`idCompany`),
  CONSTRAINT `CATEGORY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `changecontrol`
--

DROP TABLE IF EXISTS `changecontrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changecontrol` (
  `IdChange` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `IdChangeType` int(11) DEFAULT NULL,
  `IdWBSNode` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `Priority` char(1) DEFAULT NULL,
  `ChangeDate` date DEFAULT NULL,
  `Originator` varchar(50) DEFAULT NULL,
  `RECOMMENDEDSOLUTION` varchar(3000) DEFAULT NULL,
  `IMPACTDESCRIPTION` varchar(3000) DEFAULT NULL,
  `EstimatedEffort` double DEFAULT NULL,
  `EstimatedCost` double DEFAULT NULL,
  `Resolution` bit(1) DEFAULT NULL,
  `RESOLUTIONREASON` varchar(3000) DEFAULT NULL,
  `ResolutionDate` date DEFAULT NULL,
  `ChangeDocLink` varchar(100) DEFAULT NULL,
  `resolutionName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IdChange`),
  KEY `IdChangeType` (`IdChangeType`),
  KEY `IdWBSNode` (`IdWBSNode`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `CHANGECONTROL_CHANGETYPE_FK` FOREIGN KEY (`IdChangeType`) REFERENCES `changetype` (`idChangeType`) ON DELETE SET NULL,
  CONSTRAINT `CHANGECONTROL_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `CHANGECONTROL_WBSNODE_FK` FOREIGN KEY (`IdWBSNode`) REFERENCES `wbsnode` (`IdWBSNode`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `changecontrol`
--

LOCK TABLES `changecontrol` WRITE;
/*!40000 ALTER TABLE `changecontrol` DISABLE KEYS */;
/*!40000 ALTER TABLE `changecontrol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `changerequestwbsnode`
--

DROP TABLE IF EXISTS `changerequestwbsnode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changerequestwbsnode` (
  `idChangeRequestWBSNode` int(11) NOT NULL AUTO_INCREMENT,
  `idWBSNode` int(11) NOT NULL,
  `idChange` int(11) NOT NULL,
  `estimatedEffort` double DEFAULT NULL,
  `estimatedCost` double DEFAULT NULL,
  PRIMARY KEY (`idChangeRequestWBSNode`),
  KEY `CHANGEREQWBSNODE_WBSNODE_FK` (`idWBSNode`),
  KEY `CHANGEREQWBSNODE_CHANGE_FK` (`idChange`),
  CONSTRAINT `CHANGEREQWBSNODE_CHANGE_FK` FOREIGN KEY (`idChange`) REFERENCES `changecontrol` (`IdChange`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `CHANGEREQWBSNODE_WBSNODE_FK` FOREIGN KEY (`idWBSNode`) REFERENCES `wbsnode` (`IdWBSNode`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `changerequestwbsnode`
--

LOCK TABLES `changerequestwbsnode` WRITE;
/*!40000 ALTER TABLE `changerequestwbsnode` DISABLE KEYS */;
/*!40000 ALTER TABLE `changerequestwbsnode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `changetype`
--

DROP TABLE IF EXISTS `changetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changetype` (
  `idChangeType` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(50) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`idChangeType`),
  KEY `FK_changetype_1` (`idCompany`),
  CONSTRAINT `CHANGETYPE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `changetype`
--

LOCK TABLES `changetype` WRITE;
/*!40000 ALTER TABLE `changetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `changetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chargescosts`
--

DROP TABLE IF EXISTS `chargescosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chargescosts` (
  `idChargesCosts` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(75) NOT NULL,
  `cost` double DEFAULT NULL,
  `idChargeType` int(11) NOT NULL,
  `idProject` int(11) NOT NULL,
  `idCurrency` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idChargesCosts`),
  KEY `IdProject` (`idProject`),
  KEY `CHARGESCOSTS_CURRENCY_FK` (`idCurrency`),
  CONSTRAINT `CHARGESCOSTS_CURRENCY_FK` FOREIGN KEY (`idCurrency`) REFERENCES `currency` (`idCurrency`) ON DELETE SET NULL,
  CONSTRAINT `CHARGESCOSTS_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chargescosts`
--

LOCK TABLES `chargescosts` WRITE;
/*!40000 ALTER TABLE `chargescosts` DISABLE KEYS */;
/*!40000 ALTER TABLE `chargescosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklist`
--

DROP TABLE IF EXISTS `checklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checklist` (
  `idChecklist` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idWbsnode` int(11) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `percentageComplete` int(10) unsigned DEFAULT NULL,
  `actualizationDate` date DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `comments` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`idChecklist`),
  KEY `FK_checklist_1` (`idWbsnode`),
  CONSTRAINT `CHECKLIST_WBSNODE_FK` FOREIGN KEY (`idWbsnode`) REFERENCES `wbsnode` (`IdWBSNode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklist`
--

LOCK TABLES `checklist` WRITE;
/*!40000 ALTER TABLE `checklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classificationlevel`
--

DROP TABLE IF EXISTS `classificationlevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classificationlevel` (
  `idClassificationlevel` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `thresholdMin` int(11) DEFAULT NULL,
  `thresholdMax` int(11) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idClassificationlevel`),
  KEY `CLASSIFILEVEL_COMPANY_FK` (`idCompany`),
  CONSTRAINT `CLASSIFILEVEL_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classificationlevel`
--

LOCK TABLES `classificationlevel` WRITE;
/*!40000 ALTER TABLE `classificationlevel` DISABLE KEYS */;
/*!40000 ALTER TABLE `classificationlevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `closurecheck`
--

DROP TABLE IF EXISTS `closurecheck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `closurecheck` (
  `idClosureCheck` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `showCheck` bit(1) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idClosureCheck`),
  KEY `CLOSURECHECK_COMPANY_FK` (`idCompany`),
  CONSTRAINT `CLOSURECHECK_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closurecheck`
--

LOCK TABLES `closurecheck` WRITE;
/*!40000 ALTER TABLE `closurecheck` DISABLE KEYS */;
/*!40000 ALTER TABLE `closurecheck` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `closurecheckproject`
--

DROP TABLE IF EXISTS `closurecheckproject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `closurecheckproject` (
  `idClosureCheckProject` int(11) NOT NULL AUTO_INCREMENT,
  `comments` varchar(3000) DEFAULT NULL,
  `realized` bit(1) NOT NULL DEFAULT b'0',
  `idClosureCheck` int(11) NOT NULL,
  `idProject` int(11) NOT NULL,
  `manager` varchar(100) DEFAULT NULL,
  `departament` varchar(300) DEFAULT NULL,
  `idDocumentProject` int(11) DEFAULT NULL,
  `dateRealized` date DEFAULT NULL,
  PRIMARY KEY (`idClosureCheckProject`),
  KEY `CLOSCHECKPROJ_CLOSURECHECK_FK` (`idClosureCheck`),
  KEY `CLOSCHECKPROJ_PROJECT_FK` (`idProject`),
  KEY `CLOSCHECKPROJ_DOCUMENT_FK` (`idDocumentProject`),
  CONSTRAINT `CLOSCHECKPROJ_CLOSURECHECK_FK` FOREIGN KEY (`idClosureCheck`) REFERENCES `closurecheck` (`idClosureCheck`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `CLOSCHECKPROJ_DOCUMENT_FK` FOREIGN KEY (`idDocumentProject`) REFERENCES `documentproject` (`idDocumentProject`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `CLOSCHECKPROJ_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closurecheckproject`
--

LOCK TABLES `closurecheckproject` WRITE;
/*!40000 ALTER TABLE `closurecheckproject` DISABLE KEYS */;
/*!40000 ALTER TABLE `closurecheckproject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `IdCompany` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'COMPANY',NULL);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companyfile`
--

DROP TABLE IF EXISTS `companyfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companyfile` (
  `idCompanyfile` int(11) NOT NULL AUTO_INCREMENT,
  `idContentFile` int(10) unsigned NOT NULL,
  `idCompany` int(11) NOT NULL,
  `typeFile` varchar(50) NOT NULL,
  PRIMARY KEY (`idCompanyfile`),
  KEY `COMPANYFILE_CONTENTFILE_FK` (`idContentFile`),
  KEY `COMPANYFILE_COMPANY_FK` (`idCompany`),
  CONSTRAINT `COMPANYFILE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `COMPANYFILE_CONTENTFILE_FK` FOREIGN KEY (`idContentFile`) REFERENCES `contentfile` (`idContentFile`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companyfile`
--

LOCK TABLES `companyfile` WRITE;
/*!40000 ALTER TABLE `companyfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `companyfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration` (
  `idConfiguration` int(11) NOT NULL AUTO_INCREMENT,
  `idContact` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(1000) DEFAULT NULL,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`idConfiguration`),
  KEY `CONFIGURARION_CONTACT_FK` (`idContact`),
  CONSTRAINT `CONFIGURARION_CONTACT_FK` FOREIGN KEY (`idContact`) REFERENCES `contact` (`IdContact`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `IdContact` int(11) NOT NULL AUTO_INCREMENT,
  `IdCompany` int(11) DEFAULT NULL,
  `FullName` varchar(50) DEFAULT NULL,
  `JobTitle` varchar(50) DEFAULT NULL,
  `FileAs` varchar(60) DEFAULT NULL,
  `BusinessPhone` varchar(12) DEFAULT NULL,
  `MobilePhone` varchar(12) DEFAULT NULL,
  `BusinessAddress` varchar(200) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Notes` varchar(200) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`IdContact`),
  KEY `IdCompany` (`IdCompany`),
  CONSTRAINT `CONTACT_COMPANY_FK` FOREIGN KEY (`IdCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,1,'Admin','Admin','Admin','','','','admin@openppm.es','','en_US',NULL);
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contactnotification`
--

DROP TABLE IF EXISTS `contactnotification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contactnotification` (
  `idContactNotification` int(11) NOT NULL AUTO_INCREMENT,
  `idContact` int(11) DEFAULT NULL,
  `idNotification` int(11) NOT NULL,
  `type` varchar(100) DEFAULT NULL,
  `readNotify` bit(1) NOT NULL DEFAULT b'0',
  `readDate` datetime DEFAULT NULL,
  PRIMARY KEY (`idContactNotification`),
  KEY `CONTACTNOTIFICATION_CONTACT_FK` (`idContact`),
  KEY `CONTACTNOTIFICATION_NOTI_FK` (`idNotification`),
  CONSTRAINT `CONTACTNOTIFICATION_CONTACT_FK` FOREIGN KEY (`idContact`) REFERENCES `contact` (`IdContact`) ON DELETE CASCADE,
  CONSTRAINT `CONTACTNOTIFICATION_NOTI_FK` FOREIGN KEY (`idNotification`) REFERENCES `notification` (`idNotification`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contactnotification`
--

LOCK TABLES `contactnotification` WRITE;
/*!40000 ALTER TABLE `contactnotification` DISABLE KEYS */;
/*!40000 ALTER TABLE `contactnotification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contentfile`
--

DROP TABLE IF EXISTS `contentfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contentfile` (
  `idContentFile` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` mediumblob,
  `extension` varchar(10) DEFAULT NULL,
  `mime` varchar(250) NOT NULL,
  PRIMARY KEY (`idContentFile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contentfile`
--

LOCK TABLES `contentfile` WRITE;
/*!40000 ALTER TABLE `contentfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `contentfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracttype`
--

DROP TABLE IF EXISTS `contracttype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contracttype` (
  `IdContractType` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdContractType`),
  KEY `FK_contracttype_1` (`idCompany`),
  KEY `CONTRACTTYPE_COMPANY_FK` (`idCompany`),
  CONSTRAINT `CONTRACTTYPE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracttype`
--

LOCK TABLES `contracttype` WRITE;
/*!40000 ALTER TABLE `contracttype` DISABLE KEYS */;
/*!40000 ALTER TABLE `contracttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `idCurrency` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `currency` varchar(4) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idCurrency`),
  KEY `CURRENCY_COMPANY_FK` (`idCompany`),
  CONSTRAINT `CURRENCY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `currency`
--

LOCK TABLES `currency` WRITE;
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
INSERT INTO `currency` VALUES (1,'Euro','€',1),(2,'Dolar','$',1);
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `IdCustomer` int(11) NOT NULL AUTO_INCREMENT,
  `IdCompany` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `idCustomerType` int(10) unsigned DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`IdCustomer`),
  KEY `IdCompany` (`IdCompany`),
  KEY `FK_customer_2` (`idCustomerType`),
  CONSTRAINT `CUSTOMER_COMPANY_FK` FOREIGN KEY (`IdCompany`) REFERENCES `company` (`IdCompany`),
  CONSTRAINT `CUSTOMER_CUSTOMERTYPE_FK` FOREIGN KEY (`idCustomerType`) REFERENCES `customertype` (`idCustomerType`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customertype`
--

DROP TABLE IF EXISTS `customertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customertype` (
  `idCustomerType` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCompany` int(11) NOT NULL,
  `name` varchar(75) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idCustomerType`),
  KEY `FK_customertype_1` (`idCompany`),
  KEY `CUSTOMERTYPE_COMPANY_FK` (`idCompany`),
  CONSTRAINT `CUSTOMERTYPE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customertype`
--

LOCK TABLES `customertype` WRITE;
/*!40000 ALTER TABLE `customertype` DISABLE KEYS */;
/*!40000 ALTER TABLE `customertype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directcosts`
--

DROP TABLE IF EXISTS `directcosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `directcosts` (
  `IdDirectCosts` int(11) NOT NULL AUTO_INCREMENT,
  `IdBudgetAccount` int(11) DEFAULT NULL,
  `IdProjectCosts` int(11) DEFAULT NULL,
  `Planned` double DEFAULT NULL,
  `Actual` double DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `ProjectCost` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`IdDirectCosts`),
  KEY `IdBudgetAccount` (`IdBudgetAccount`),
  KEY `IdProjectCosts` (`IdProjectCosts`),
  CONSTRAINT `DIRECTCOSTS_BUDGETACCOUNTS_FK` FOREIGN KEY (`IdBudgetAccount`) REFERENCES `budgetaccounts` (`IdBudgetAccount`) ON DELETE SET NULL,
  CONSTRAINT `DIRECTCOSTS_PROJECTCOSTS_FK` FOREIGN KEY (`IdProjectCosts`) REFERENCES `projectcosts` (`IdProjectCosts`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directcosts`
--

LOCK TABLES `directcosts` WRITE;
/*!40000 ALTER TABLE `directcosts` DISABLE KEYS */;
/*!40000 ALTER TABLE `directcosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documentation`
--

DROP TABLE IF EXISTS `documentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documentation` (
  `idDocumentation` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nameFile` varchar(150) DEFAULT NULL,
  `namePopup` varchar(250) NOT NULL,
  `idCompany` int(11) NOT NULL,
  `idContentFile` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idDocumentation`),
  KEY `FK_documentation_1` (`idCompany`),
  KEY `FK_documentation_2` (`idContentFile`),
  KEY `DOCUMENTATION_COMPANY_FK` (`idCompany`),
  CONSTRAINT `DOCUMENTATION_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`),
  CONSTRAINT `DOCUMENTATION_CONTENTFILE_FK` FOREIGN KEY (`idContentFile`) REFERENCES `contentfile` (`idContentFile`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documentation`
--

LOCK TABLES `documentation` WRITE;
/*!40000 ALTER TABLE `documentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `documentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documentproject`
--

DROP TABLE IF EXISTS `documentproject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documentproject` (
  `idDocumentProject` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `link` varchar(3000) DEFAULT NULL,
  `type` varchar(25) NOT NULL,
  `mime` varchar(100) DEFAULT NULL,
  `extension` varchar(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `contentComment` varchar(250) DEFAULT NULL,
  `creationContact` varchar(50) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`idDocumentProject`),
  KEY `FK_documentproject_1` (`idProject`),
  CONSTRAINT `DOCUMENTPROJECT_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documentproject`
--

LOCK TABLES `documentproject` WRITE;
/*!40000 ALTER TABLE `documentproject` DISABLE KEYS */;
/*!40000 ALTER TABLE `documentproject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `idEmployee` int(11) NOT NULL AUTO_INCREMENT,
  `costRate` double DEFAULT NULL,
  `idContact` int(11) DEFAULT NULL,
  `idPerfOrg` int(11) DEFAULT NULL,
  `idProfile` int(11) DEFAULT NULL,
  `profileDate` date DEFAULT NULL,
  `token` varchar(30) DEFAULT NULL,
  `idCalendarBase` int(11) DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  `idSeller` int(11) DEFAULT NULL,
  `idResourcePool` int(11) DEFAULT NULL,
  PRIMARY KEY (`idEmployee`),
  KEY `IdProfile` (`idProfile`),
  KEY `IdPerfOrg` (`idPerfOrg`),
  KEY `IdContact` (`idContact`),
  KEY `EMPLOYEE_CALENDARBASE_FK` (`idCalendarBase`),
  KEY `EMPLOYEE_SELLER_FK` (`idSeller`),
  KEY `EMPLOYEE_RESOURCEPOOL_FK` (`idResourcePool`),
  CONSTRAINT `EMPLOYEE_CALENDARBASE_FK` FOREIGN KEY (`idCalendarBase`) REFERENCES `calendarbase` (`idCalendarBase`) ON DELETE SET NULL,
  CONSTRAINT `EMPLOYEE_CONTACT_FK` FOREIGN KEY (`idContact`) REFERENCES `contact` (`IdContact`),
  CONSTRAINT `EMPLOYEE_PERFORG_FK` FOREIGN KEY (`idPerfOrg`) REFERENCES `performingorg` (`IdPerfOrg`) ON DELETE SET NULL,
  CONSTRAINT `EMPLOYEE_RESOURCEPOOL_FK` FOREIGN KEY (`idResourcePool`) REFERENCES `resourcepool` (`idResourcepool`),
  CONSTRAINT `EMPLOYEE_RESPROFILES_FK` FOREIGN KEY (`idProfile`) REFERENCES `resourceprofiles` (`IdProfile`) ON DELETE SET NULL,
  CONSTRAINT `EMPLOYEE_SELLER_FK` FOREIGN KEY (`idSeller`) REFERENCES `seller` (`idSeller`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (171,NULL,1,NULL,10,'2015-06-17',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeeoperationdate`
--

DROP TABLE IF EXISTS `employeeoperationdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employeeoperationdate` (
  `idEmplOpeDate` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idEmployee` int(11) NOT NULL,
  `idOperation` int(11) NOT NULL,
  `dateForOperation` date NOT NULL,
  PRIMARY KEY (`idEmplOpeDate`),
  KEY `EMPLOPEDATE_EMPLOYEE_FK` (`idEmployee`),
  KEY `EMPLOPEDATE_OPERATION_FK` (`idOperation`),
  CONSTRAINT `EMPLOPEDATE_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `EMPLOPEDATE_OPERATION_FK` FOREIGN KEY (`idOperation`) REFERENCES `operation` (`IdOperation`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeeoperationdate`
--

LOCK TABLES `employeeoperationdate` WRITE;
/*!40000 ALTER TABLE `employeeoperationdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `employeeoperationdate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `executivereport`
--

DROP TABLE IF EXISTS `executivereport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `executivereport` (
  `idExecutiveReport` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `statusDate` date NOT NULL,
  `internal` varchar(3000) DEFAULT NULL,
  `external` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`idExecutiveReport`),
  KEY `EXECUTIVEREPORT_PROJECT_FK` (`idProject`),
  CONSTRAINT `EXECUTIVEREPORT_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `executivereport`
--

LOCK TABLES `executivereport` WRITE;
/*!40000 ALTER TABLE `executivereport` DISABLE KEYS */;
/*!40000 ALTER TABLE `executivereport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenseaccounts`
--

DROP TABLE IF EXISTS `expenseaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expenseaccounts` (
  `IdExpenseAccount` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdExpenseAccount`),
  KEY `FK_expenseaccounts_1` (`idCompany`),
  CONSTRAINT `EXPENSEACCOUNTS_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenseaccounts`
--

LOCK TABLES `expenseaccounts` WRITE;
/*!40000 ALTER TABLE `expenseaccounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `expenseaccounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expenses` (
  `IdExpense` int(11) NOT NULL AUTO_INCREMENT,
  `IdBudgetAccount` int(11) DEFAULT NULL,
  `IdProjectCosts` int(11) DEFAULT NULL,
  `Planned` double DEFAULT NULL,
  `Actual` double DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `ProjectExpense` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`IdExpense`),
  KEY `IdBudgetAccount` (`IdBudgetAccount`),
  KEY `IdProjectCosts` (`IdProjectCosts`),
  CONSTRAINT `EXPENSES_BUDGETACCOUNTS_FK` FOREIGN KEY (`IdBudgetAccount`) REFERENCES `budgetaccounts` (`IdBudgetAccount`) ON DELETE SET NULL,
  CONSTRAINT `EXPENSES_PROJECTCOSTS_FK` FOREIGN KEY (`IdProjectCosts`) REFERENCES `projectcosts` (`IdProjectCosts`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expensesheet`
--

DROP TABLE IF EXISTS `expensesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expensesheet` (
  `idExpenseSheet` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idProject` int(11) DEFAULT NULL,
  `idOperation` int(11) DEFAULT NULL,
  `idExpenseAccount` int(11) DEFAULT NULL,
  `idEmployee` int(11) NOT NULL,
  `cost` double DEFAULT NULL,
  `reimbursable` bit(1) DEFAULT NULL,
  `paidEmployee` bit(1) DEFAULT NULL,
  `expenseDate` date DEFAULT NULL,
  `autorizationNumber` varchar(50) DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `idExpense` int(11) DEFAULT NULL,
  PRIMARY KEY (`idExpenseSheet`),
  KEY `FK_expensesheet_1` (`idProject`),
  KEY `FK_expensesheet_2` (`idOperation`),
  KEY `FK_expensesheet_3` (`idEmployee`),
  KEY `FK_expensesheet_4` (`idExpenseAccount`),
  KEY `FK_expensesheet_5` (`idExpense`),
  CONSTRAINT `EXPENSESHEET_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EXPENSESHEET_EXPACCOUNTS_FK` FOREIGN KEY (`idExpenseAccount`) REFERENCES `expenseaccounts` (`IdExpenseAccount`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `EXPENSESHEET_EXPENSE_FK` FOREIGN KEY (`idExpense`) REFERENCES `expenses` (`IdExpense`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `EXPENSESHEET_OPERATION_FK` FOREIGN KEY (`idOperation`) REFERENCES `operation` (`IdOperation`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `EXPENSESHEET_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expensesheet`
--

LOCK TABLES `expensesheet` WRITE;
/*!40000 ALTER TABLE `expensesheet` DISABLE KEYS */;
/*!40000 ALTER TABLE `expensesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expensesheetcomment`
--

DROP TABLE IF EXISTS `expensesheetcomment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expensesheetcomment` (
  `idExpenseSheetComment` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idExpenseSheet` int(10) unsigned NOT NULL,
  `previousStatus` varchar(10) NOT NULL,
  `actualStatus` varchar(10) NOT NULL,
  `commentDate` datetime NOT NULL,
  `contentComment` varchar(1000) NOT NULL,
  PRIMARY KEY (`idExpenseSheetComment`),
  KEY `FK_expensesheetcomment_1` (`idExpenseSheet`),
  CONSTRAINT `EXPSHEETCOMMENT_EXPSHEET_FK` FOREIGN KEY (`idExpenseSheet`) REFERENCES `expensesheet` (`idExpenseSheet`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expensesheetcomment`
--

LOCK TABLES `expensesheetcomment` WRITE;
/*!40000 ALTER TABLE `expensesheetcomment` DISABLE KEYS */;
/*!40000 ALTER TABLE `expensesheetcomment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fundingsource`
--

DROP TABLE IF EXISTS `fundingsource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fundingsource` (
  `idFundingSource` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idFundingSource`),
  KEY `FK_fundingsource_1` (`idCompany`),
  CONSTRAINT `FUNDINGSOURCE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fundingsource`
--

LOCK TABLES `fundingsource` WRITE;
/*!40000 ALTER TABLE `fundingsource` DISABLE KEYS */;
/*!40000 ALTER TABLE `fundingsource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `geography`
--

DROP TABLE IF EXISTS `geography`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `geography` (
  `idGeography` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGeography`),
  KEY `FK_geography_1` (`idCompany`),
  CONSTRAINT `GEOGRAPHY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `geography`
--

LOCK TABLES `geography` WRITE;
/*!40000 ALTER TABLE `geography` DISABLE KEYS */;
/*!40000 ALTER TABLE `geography` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historickpi`
--

DROP TABLE IF EXISTS `historickpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historickpi` (
  `idHistoricKpi` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idProjectKpi` int(11) NOT NULL,
  `upperThreshold` double DEFAULT NULL,
  `lowerThreshold` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `valueKpi` double DEFAULT NULL,
  `idEmployee` int(11) NOT NULL,
  `actualDate` datetime NOT NULL,
  `updatedType` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idHistoricKpi`),
  KEY `HISTORICKPI_PROJECTKPI_FK` (`idProjectKpi`),
  KEY `HISTORICKPI_EMPLOYEE_FK` (`idEmployee`),
  CONSTRAINT `HISTORICKPI_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `HISTORICKPI_PROJECTKPI_FK` FOREIGN KEY (`idProjectKpi`) REFERENCES `projectkpi` (`IdProjectKPI`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historickpi`
--

LOCK TABLES `historickpi` WRITE;
/*!40000 ALTER TABLE `historickpi` DISABLE KEYS */;
/*!40000 ALTER TABLE `historickpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historicrisk`
--

DROP TABLE IF EXISTS `historicrisk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historicrisk` (
  `idHistoricrisk` int(11) NOT NULL AUTO_INCREMENT,
  `idRisk` int(11) NOT NULL,
  `probability` int(11) DEFAULT NULL,
  `impact` int(11) DEFAULT NULL,
  `idEmployee` int(11) NOT NULL,
  `actualDate` datetime NOT NULL,
  PRIMARY KEY (`idHistoricrisk`),
  KEY `HISTORICRISK_RISKREGISTER_FK` (`idRisk`),
  KEY `HISTORICRISK_EMPLOYEE_FK` (`idEmployee`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historicrisk`
--

LOCK TABLES `historicrisk` WRITE;
/*!40000 ALTER TABLE `historicrisk` DISABLE KEYS */;
/*!40000 ALTER TABLE `historicrisk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incomes`
--

DROP TABLE IF EXISTS `incomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incomes` (
  `idIncome` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `plannedBillDate` date DEFAULT NULL,
  `plannedBillAmmount` double DEFAULT NULL,
  `actualBillDate` date DEFAULT NULL,
  `actualBillAmmount` double DEFAULT NULL,
  `plannedDescription` varchar(200) DEFAULT NULL,
  `actualPaymentDate` date DEFAULT NULL,
  `actualDescription` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idIncome`),
  KEY `INCOMES_PROJECT_FK` (`idProject`),
  CONSTRAINT `INCOMES_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incomes`
--

LOCK TABLES `incomes` WRITE;
/*!40000 ALTER TABLE `incomes` DISABLE KEYS */;
/*!40000 ALTER TABLE `incomes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuelog`
--

DROP TABLE IF EXISTS `issuelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuelog` (
  `IdIssue` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `Priority` char(1) DEFAULT NULL,
  `DateLogged` date DEFAULT NULL,
  `Originator` varchar(100) DEFAULT NULL,
  `AssignedTo` varchar(100) DEFAULT NULL,
  `TargetDate` date DEFAULT NULL,
  `RESOLUTION` varchar(3000) DEFAULT NULL,
  `DateClosed` date DEFAULT NULL,
  `IssueDoc` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdIssue`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `ISSUELOG_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuelog`
--

LOCK TABLES `issuelog` WRITE;
/*!40000 ALTER TABLE `issuelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `issuelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iwo`
--

DROP TABLE IF EXISTS `iwo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iwo` (
  `IdIWO` int(11) NOT NULL AUTO_INCREMENT,
  `IdProjectCosts` int(11) DEFAULT NULL,
  `Planned` double DEFAULT NULL,
  `Actual` double DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `IWODoc` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IdIWO`),
  KEY `IdProjectCosts` (`IdProjectCosts`),
  CONSTRAINT `IWO_PROJECTCOSTS_FK` FOREIGN KEY (`IdProjectCosts`) REFERENCES `projectcosts` (`IdProjectCosts`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iwo`
--

LOCK TABLES `iwo` WRITE;
/*!40000 ALTER TABLE `iwo` DISABLE KEYS */;
/*!40000 ALTER TABLE `iwo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobcategory`
--

DROP TABLE IF EXISTS `jobcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobcategory` (
  `idJobCategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idJobCategory`),
  KEY `JOBCATEGORY_COMPANY_FK` (`idCompany`),
  CONSTRAINT `JOBCATEGORY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobcategory`
--

LOCK TABLES `jobcategory` WRITE;
/*!40000 ALTER TABLE `jobcategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobcatemployee`
--

DROP TABLE IF EXISTS `jobcatemployee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobcatemployee` (
  `idJobCatEmployee` int(11) NOT NULL AUTO_INCREMENT,
  `idEmployee` int(11) NOT NULL,
  `idJobCategory` int(11) NOT NULL,
  PRIMARY KEY (`idJobCatEmployee`),
  KEY `JOBCATEMPLOYEE_EMPLOYEE_FK` (`idEmployee`),
  KEY `JOBCATEMPLOYEE_JOBCATEGORY_FK` (`idJobCategory`),
  CONSTRAINT `JOBCATEMPLOYEE_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `JOBCATEMPLOYEE_JOBCATEGORY_FK` FOREIGN KEY (`idJobCategory`) REFERENCES `jobcategory` (`idJobCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobcatemployee`
--

LOCK TABLES `jobcatemployee` WRITE;
/*!40000 ALTER TABLE `jobcatemployee` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobcatemployee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `idLabel` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`idLabel`),
  KEY `LABEL_COMPANY_FK` (`idCompany`),
  CONSTRAINT `LABEL_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logprojectstatus`
--

DROP TABLE IF EXISTS `logprojectstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logprojectstatus` (
  `idLogProjectStatus` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `idEmployee` int(11) NOT NULL,
  `projectStatus` varchar(11) NOT NULL,
  `investmentStatus` varchar(11) NOT NULL,
  `logDate` datetime NOT NULL,
  PRIMARY KEY (`idLogProjectStatus`),
  KEY `LOGPROJECTSTATUS_EMPLOYEE_FK` (`idEmployee`),
  KEY `LOGPROJECTSTATUS_PROJECT_FK` (`idProject`),
  CONSTRAINT `LOGPROJECTSTATUS_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`) ON DELETE CASCADE,
  CONSTRAINT `LOGPROJECTSTATUS_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logprojectstatus`
--

LOCK TABLES `logprojectstatus` WRITE;
/*!40000 ALTER TABLE `logprojectstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `logprojectstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `managepool`
--

DROP TABLE IF EXISTS `managepool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managepool` (
  `idManagePool` int(11) NOT NULL AUTO_INCREMENT,
  `idResourcePool` int(11) NOT NULL,
  `idResourceManager` int(11) NOT NULL,
  PRIMARY KEY (`idManagePool`),
  KEY `idResourcePool` (`idResourcePool`),
  KEY `idResourceManager` (`idResourceManager`),
  CONSTRAINT `idResourceManager` FOREIGN KEY (`idResourceManager`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `idResourcePool` FOREIGN KEY (`idResourcePool`) REFERENCES `resourcepool` (`idResourcepool`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managepool`
--

LOCK TABLES `managepool` WRITE;
/*!40000 ALTER TABLE `managepool` DISABLE KEYS */;
/*!40000 ALTER TABLE `managepool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `market`
--

DROP TABLE IF EXISTS `market`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `market` (
  `IdMarket` int(11) NOT NULL AUTO_INCREMENT,
  `MarketName` varchar(50) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdMarket`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `market`
--

LOCK TABLES `market` WRITE;
/*!40000 ALTER TABLE `market` DISABLE KEYS */;
/*!40000 ALTER TABLE `market` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metrickpi`
--

DROP TABLE IF EXISTS `metrickpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metrickpi` (
  `idMetricKpi` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idBscDimension` int(10) unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `definition` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  `type` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idMetricKpi`),
  KEY `FK_metrickpi_1` (`idBscDimension`),
  KEY `FK_metrickpi_2` (`idCompany`),
  CONSTRAINT `METRICKPI_BSCDIMENSION_FK` FOREIGN KEY (`idBscDimension`) REFERENCES `bscdimension` (`idBscDimension`),
  CONSTRAINT `METRICKPI_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metrickpi`
--

LOCK TABLES `metrickpi` WRITE;
/*!40000 ALTER TABLE `metrickpi` DISABLE KEYS */;
/*!40000 ALTER TABLE `metrickpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `milestonecategory`
--

DROP TABLE IF EXISTS `milestonecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `milestonecategory` (
  `idMilestoneCategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`idMilestoneCategory`),
  KEY `MILESTONECATEGORY_COMPANY_FK` (`idCompany`),
  CONSTRAINT `MILESTONECATEGORY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `milestonecategory`
--

LOCK TABLES `milestonecategory` WRITE;
/*!40000 ALTER TABLE `milestonecategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `milestonecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `milestones`
--

DROP TABLE IF EXISTS `milestones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `milestones` (
  `IdMilestone` int(11) NOT NULL AUTO_INCREMENT,
  `IdActivity` int(11) DEFAULT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `Label` varchar(30) DEFAULT NULL,
  `ReportType` char(1) DEFAULT NULL,
  `Planned` date DEFAULT NULL,
  `Achieved` date DEFAULT NULL,
  `IdProject` int(11) NOT NULL,
  `achievedComments` varchar(1500) DEFAULT NULL,
  `notify` bit(1) DEFAULT NULL,
  `notifyDays` int(10) unsigned DEFAULT NULL,
  `notificationText` varchar(200) DEFAULT NULL,
  `notifyDate` date DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `estimatedDate` date NOT NULL,
  `idMilestoneType` int(11) DEFAULT NULL,
  `idMilestoneCategory` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdMilestone`),
  KEY `milestones_ibfk_1` (`IdActivity`),
  KEY `milestones_ibfk_2` (`IdProject`),
  KEY `MILESTONE_MILESTONETYPE_FK` (`idMilestoneType`),
  KEY `MILESTONE_MILESTONECATEGORY_FK` (`idMilestoneCategory`),
  CONSTRAINT `MILESTONE_ACTIVITY_FK` FOREIGN KEY (`IdActivity`) REFERENCES `projectactivity` (`IdActivity`) ON DELETE SET NULL,
  CONSTRAINT `MILESTONE_MILESTONECATEGORY_FK` FOREIGN KEY (`idMilestoneCategory`) REFERENCES `milestonecategory` (`idMilestoneCategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `MILESTONE_MILESTONETYPE_FK` FOREIGN KEY (`idMilestoneType`) REFERENCES `milestonetype` (`idMilestoneType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `MILESTONE_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `milestones`
--

LOCK TABLES `milestones` WRITE;
/*!40000 ALTER TABLE `milestones` DISABLE KEYS */;
/*!40000 ALTER TABLE `milestones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `milestonetype`
--

DROP TABLE IF EXISTS `milestonetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `milestonetype` (
  `idMilestoneType` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idMilestoneType`),
  KEY `MILESTONETYPE_COMPANY_FK` (`idCompany`),
  CONSTRAINT `MILESTONETYPE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `milestonetype`
--

LOCK TABLES `milestonetype` WRITE;
/*!40000 ALTER TABLE `milestonetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `milestonetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `idNotification` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(1000) NOT NULL,
  `body` varchar(5000) NOT NULL,
  `status` varchar(100) NOT NULL,
  `direction` varchar(100) DEFAULT NULL,
  `messageError` varchar(200) DEFAULT NULL,
  `type` varchar(100) NOT NULL,
  `modeNotification` varchar(100) NOT NULL,
  `creationDate` datetime NOT NULL,
  `changeStatusDate` datetime DEFAULT NULL,
  PRIMARY KEY (`idNotification`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation` (
  `IdOperation` int(11) NOT NULL AUTO_INCREMENT,
  `OperationName` char(18) DEFAULT NULL,
  `OperationCode` char(18) DEFAULT NULL,
  `IdOpAccount` int(11) DEFAULT NULL,
  `availableForManager` bit(1) DEFAULT NULL,
  `excludeExternals` bit(1) DEFAULT b'0',
  PRIMARY KEY (`IdOperation`),
  KEY `IdOpAccount` (`IdOpAccount`),
  CONSTRAINT `OPERATION_OPERATIONACCOUNT_FK` FOREIGN KEY (`IdOpAccount`) REFERENCES `operationaccount` (`IdOpAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation`
--

LOCK TABLES `operation` WRITE;
/*!40000 ALTER TABLE `operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operationaccount`
--

DROP TABLE IF EXISTS `operationaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operationaccount` (
  `IdOpAccount` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdOpAccount`),
  KEY `FK_operationaccount_1` (`idCompany`),
  CONSTRAINT `OPERATIONACCOUNT_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operationaccount`
--

LOCK TABLES `operationaccount` WRITE;
/*!40000 ALTER TABLE `operationaccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `operationaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performingorg`
--

DROP TABLE IF EXISTS `performingorg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `performingorg` (
  `IdPerfOrg` int(11) NOT NULL AUTO_INCREMENT,
  `Manager` int(11) DEFAULT NULL,
  `IdCompany` int(11) DEFAULT NULL,
  `OnSaaS` bit(1) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`IdPerfOrg`),
  KEY `Manager` (`Manager`),
  KEY `IdCompany` (`IdCompany`),
  CONSTRAINT `PERFORMINGORG_COMPANY_FK` FOREIGN KEY (`IdCompany`) REFERENCES `company` (`IdCompany`),
  CONSTRAINT `PERFORMINGORG_EMPLOYEE_FK` FOREIGN KEY (`Manager`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performingorg`
--

LOCK TABLES `performingorg` WRITE;
/*!40000 ALTER TABLE `performingorg` DISABLE KEYS */;
/*!40000 ALTER TABLE `performingorg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plugin`
--

DROP TABLE IF EXISTS `plugin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plugin` (
  `idPlugin` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCompany` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`idPlugin`),
  KEY `FK_plugin_1` (`idCompany`),
  CONSTRAINT `PLUGIN_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plugin`
--

LOCK TABLES `plugin` WRITE;
/*!40000 ALTER TABLE `plugin` DISABLE KEYS */;
/*!40000 ALTER TABLE `plugin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pluginconfiguration`
--

DROP TABLE IF EXISTS `pluginconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pluginconfiguration` (
  `idPluginConfiguration` int(11) NOT NULL AUTO_INCREMENT,
  `idPlugin` int(10) unsigned NOT NULL,
  `configuration` varchar(100) NOT NULL,
  `value` varchar(3000) NOT NULL,
  PRIMARY KEY (`idPluginConfiguration`),
  KEY `PLUGINCONF_PLUGIN_FK_idx` (`idPlugin`),
  CONSTRAINT `PLUGINCONF_PLUGIN_FK` FOREIGN KEY (`idPlugin`) REFERENCES `plugin` (`idPlugin`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pluginconfiguration`
--

LOCK TABLES `pluginconfiguration` WRITE;
/*!40000 ALTER TABLE `pluginconfiguration` DISABLE KEYS */;
/*!40000 ALTER TABLE `pluginconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pluginfile`
--

DROP TABLE IF EXISTS `pluginfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pluginfile` (
  `idPluginfile` int(11) NOT NULL AUTO_INCREMENT,
  `idContentFile` int(10) unsigned NOT NULL,
  `idPlugin` int(10) unsigned NOT NULL,
  `typeFile` varchar(50) NOT NULL,
  PRIMARY KEY (`idPluginfile`),
  KEY `PLUGINFILE_CONTENTFILE_FK` (`idContentFile`),
  KEY `PLUGINFILE_PLUGIN_FK` (`idPlugin`),
  CONSTRAINT `PLUGINFILE_CONTENTFILE_FK` FOREIGN KEY (`idContentFile`) REFERENCES `contentfile` (`idContentFile`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PLUGINFILE_PLUGIN_FK` FOREIGN KEY (`idPlugin`) REFERENCES `plugin` (`idPlugin`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pluginfile`
--

LOCK TABLES `pluginfile` WRITE;
/*!40000 ALTER TABLE `pluginfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `pluginfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problemcheck`
--

DROP TABLE IF EXISTS `problemcheck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problemcheck` (
  `idProblemCheck` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `showCheck` bit(1) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idProblemCheck`),
  KEY `PROBLEMCHECK_COMPANY_FK` (`idCompany`),
  CONSTRAINT `PROBLEMCHECK_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problemcheck`
--

LOCK TABLES `problemcheck` WRITE;
/*!40000 ALTER TABLE `problemcheck` DISABLE KEYS */;
/*!40000 ALTER TABLE `problemcheck` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problemcheckproject`
--

DROP TABLE IF EXISTS `problemcheckproject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problemcheckproject` (
  `idProblemCheckProject` int(11) NOT NULL AUTO_INCREMENT,
  `idProblemCheck` int(11) NOT NULL,
  `idProject` int(11) NOT NULL,
  PRIMARY KEY (`idProblemCheckProject`),
  KEY `PROBLCHECKPROJ_PROBLEMCHECK_FK` (`idProblemCheck`),
  KEY `PROBLCHECKPROJ_PROJECT_FK` (`idProject`),
  CONSTRAINT `PROBLCHECKPROJ_PROBLEMCHECK_FK` FOREIGN KEY (`idProblemCheck`) REFERENCES `problemcheck` (`idProblemCheck`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `PROBLCHECKPROJ_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problemcheckproject`
--

LOCK TABLES `problemcheckproject` WRITE;
/*!40000 ALTER TABLE `problemcheckproject` DISABLE KEYS */;
/*!40000 ALTER TABLE `problemcheckproject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `procurementpayments`
--

DROP TABLE IF EXISTS `procurementpayments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procurementpayments` (
  `idProcurementPayment` int(11) NOT NULL AUTO_INCREMENT,
  `idSeller` int(11) NOT NULL,
  `idProject` int(11) NOT NULL,
  `purchaseOrder` varchar(50) DEFAULT NULL,
  `plannedDate` date DEFAULT NULL,
  `actualDate` date DEFAULT NULL,
  `plannedPayment` double DEFAULT NULL,
  `actualPayment` double DEFAULT NULL,
  `paymentScheduleInfo` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idProcurementPayment`),
  KEY `idSeller` (`idSeller`),
  KEY `idProject` (`idProject`),
  CONSTRAINT `PROCPAYMENTS_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `PROCPAYMENTS_SELLER_FK` FOREIGN KEY (`idSeller`) REFERENCES `seller` (`idSeller`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `procurementpayments`
--

LOCK TABLES `procurementpayments` WRITE;
/*!40000 ALTER TABLE `procurementpayments` DISABLE KEYS */;
/*!40000 ALTER TABLE `procurementpayments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program` (
  `IdProgram` int(11) NOT NULL AUTO_INCREMENT,
  `ProgramManager` int(11) DEFAULT NULL,
  `ProgramCode` varchar(20) NOT NULL,
  `ProgramName` varchar(50) NOT NULL,
  `ProgramTitle` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `ProgramDoc` varchar(200) DEFAULT NULL,
  `budget` double DEFAULT NULL,
  `idPerfOrg` int(11) DEFAULT NULL,
  `InitBudgetYear` varchar(4) DEFAULT NULL,
  `FinishBudgetYear` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`IdProgram`),
  KEY `ProgramManager` (`ProgramManager`),
  KEY `FK_program_2` (`idPerfOrg`),
  CONSTRAINT `PROGRAM_PERFORMINGORG_FK` FOREIGN KEY (`idPerfOrg`) REFERENCES `performingorg` (`IdPerfOrg`),
  CONSTRAINT `PROGRAM_PMEMPLOYEE_FK` FOREIGN KEY (`ProgramManager`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `idProject` int(11) NOT NULL AUTO_INCREMENT,
  `projectName` varchar(80) DEFAULT NULL,
  `status` varchar(11) NOT NULL,
  `risk` char(1) DEFAULT NULL,
  `priority` int(10) unsigned DEFAULT NULL,
  `idPerfOrg` int(11) DEFAULT NULL,
  `idCustomer` int(11) DEFAULT NULL,
  `idProgram` int(11) DEFAULT NULL,
  `projectManager` int(11) DEFAULT NULL,
  `functionalManager` int(11) DEFAULT NULL,
  `sponsor` int(11) DEFAULT NULL,
  `idProjectCalendar` int(11) DEFAULT NULL,
  `bac` double DEFAULT NULL,
  `netIncome` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `initDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `effort` int(11) DEFAULT NULL,
  `IdContractType` int(11) DEFAULT NULL,
  `plannedFinishDate` date DEFAULT NULL,
  `planDate` date DEFAULT NULL,
  `execDate` date DEFAULT NULL,
  `plannedInitDate` date DEFAULT NULL,
  `closeComments` varchar(200) DEFAULT NULL,
  `closeStakeholderComments` varchar(200) DEFAULT NULL,
  `closeUrlLessons` varchar(50) DEFAULT NULL,
  `closeLessons` varchar(200) DEFAULT NULL,
  `internalProject` bit(1) DEFAULT NULL,
  `projectDoc` varchar(20) DEFAULT NULL,
  `budgetYear` int(4) unsigned DEFAULT NULL,
  `chartLabel` varchar(25) DEFAULT NULL,
  `investmentManager` int(11) DEFAULT NULL,
  `probability` int(10) unsigned DEFAULT NULL,
  `isGeoSelling` bit(1) DEFAULT NULL,
  `idCategory` int(11) DEFAULT NULL,
  `idGeography` int(11) DEFAULT NULL,
  `investmentStatus` varchar(11) NOT NULL,
  `sended` bit(1) DEFAULT NULL,
  `numCompetitors` int(10) unsigned DEFAULT NULL,
  `finalPosition` int(10) unsigned DEFAULT NULL,
  `clientComments` varchar(1000) DEFAULT NULL,
  `canceledComments` varchar(200) DEFAULT NULL,
  `comments` varchar(200) DEFAULT NULL,
  `linkDoc` varchar(200) DEFAULT NULL,
  `accountingCode` varchar(25) DEFAULT NULL,
  `statusDate` date DEFAULT NULL,
  `lowerThreshold` double DEFAULT NULL,
  `upperThreshold` double DEFAULT NULL,
  `linkComment` varchar(250) DEFAULT NULL,
  `scopeStatement` varchar(1500) DEFAULT NULL,
  `hdDescription` varchar(1500) DEFAULT NULL,
  `rag` char(1) DEFAULT NULL,
  `currencyOptional1` double DEFAULT NULL,
  `currencyOptional2` double DEFAULT NULL,
  `currencyOptional3` double DEFAULT NULL,
  `currencyOptional4` double DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `finishDate` date DEFAULT NULL,
  `poc` double DEFAULT NULL,
  `kpiStatus` varchar(50) DEFAULT NULL,
  `idStageGate` int(11) DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  `calculatedPlanStartDate` date DEFAULT NULL,
  `calculatedPlanFinishDate` date DEFAULT NULL,
  `idClassificationlevel` int(11) DEFAULT NULL,
  `riskRatingAdjustament` int(11) DEFAULT NULL,
  `strategicAdjustament` int(11) DEFAULT NULL,
  `useRiskAdjust` bit(1) DEFAULT b'0',
  `useStrategicAdjust` bit(1) DEFAULT b'0',
  `archiveDate` date DEFAULT NULL,
  PRIMARY KEY (`idProject`),
  KEY `IdContractType` (`IdContractType`),
  KEY `IdProgram` (`idProgram`),
  KEY `ProjectManager` (`projectManager`),
  KEY `IdPerfOrg` (`idPerfOrg`),
  KEY `IdCustomer` (`idCustomer`),
  KEY `IdProjectCalendar` (`idProjectCalendar`),
  KEY `Sponsor` (`sponsor`),
  KEY `project_ibfk_9` (`investmentManager`),
  KEY `project_ibfk_10` (`idCategory`),
  KEY `project_ibfk_11` (`idGeography`),
  KEY `PROJECT_FMEMPLOYEE_FK` (`functionalManager`),
  KEY `PROJECT_STAGEGATE_FK` (`idStageGate`),
  KEY `PROJECT_CLASSIFICATIONLEVEL_FK_idx` (`idClassificationlevel`),
  CONSTRAINT `PROJECT_CATEGORY_FK` FOREIGN KEY (`idCategory`) REFERENCES `category` (`IdCategory`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_CLASSIFICATIONLEVEL_FK` FOREIGN KEY (`idClassificationlevel`) REFERENCES `classificationlevel` (`idClassificationlevel`),
  CONSTRAINT `PROJECT_CONTRACTTYPE_FK` FOREIGN KEY (`IdContractType`) REFERENCES `contracttype` (`IdContractType`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_CUSTOMER_FK` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`IdCustomer`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_FMEMPLOYEE_FK` FOREIGN KEY (`functionalManager`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_GEOGRAPHY_FK` FOREIGN KEY (`idGeography`) REFERENCES `geography` (`idGeography`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_IMEMPLOYEE_FK` FOREIGN KEY (`investmentManager`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_PERFORMINGORG_FK` FOREIGN KEY (`idPerfOrg`) REFERENCES `performingorg` (`IdPerfOrg`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_PMEMPLOYEE_FK` FOREIGN KEY (`projectManager`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_PROGRAM_FK` FOREIGN KEY (`idProgram`) REFERENCES `program` (`IdProgram`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_PROJECTCALENDAR_FK` FOREIGN KEY (`idProjectCalendar`) REFERENCES `projectcalendar` (`IdProjectCalendar`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_SPEMPLOYEE_FK` FOREIGN KEY (`sponsor`) REFERENCES `employee` (`idEmployee`) ON DELETE SET NULL,
  CONSTRAINT `PROJECT_STAGEGATE_FK` FOREIGN KEY (`idStageGate`) REFERENCES `stagegate` (`idStageGate`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectactivity`
--

DROP TABLE IF EXISTS `projectactivity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectactivity` (
  `IdActivity` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `ActivityName` varchar(200) DEFAULT NULL,
  `WBSDictionary` varchar(2500) DEFAULT NULL,
  `WorkPackage` int(11) NOT NULL,
  `PlanInitDate` date DEFAULT NULL,
  `ActualInitDate` date DEFAULT NULL,
  `PlanEndDate` date DEFAULT NULL,
  `ActualEndDate` date DEFAULT NULL,
  `EV` double DEFAULT NULL,
  `PV` double DEFAULT NULL,
  `AC` double DEFAULT NULL,
  `poc` double DEFAULT NULL,
  `commentsPOC` varchar(1500) DEFAULT NULL,
  `commentsDates` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`IdActivity`),
  KEY `projectactivity_ibfk_1` (`WorkPackage`),
  KEY `PROJECTACTIVITY_PROJECT_FK` (`idProject`),
  CONSTRAINT `PROJECTACTIVITY_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `PROJECTACTIVITY_WBSNODE_FK` FOREIGN KEY (`WorkPackage`) REFERENCES `wbsnode` (`IdWBSNode`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectactivity`
--

LOCK TABLES `projectactivity` WRITE;
/*!40000 ALTER TABLE `projectactivity` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectactivity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectassociation`
--

DROP TABLE IF EXISTS `projectassociation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectassociation` (
  `idProjectAssociation` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lead` int(11) NOT NULL,
  `dependent` int(11) NOT NULL,
  `updateDates` bit(1) DEFAULT NULL,
  PRIMARY KEY (`idProjectAssociation`),
  KEY `FK_projectassociation_1` (`lead`),
  KEY `FK_projectassociation_2` (`dependent`),
  CONSTRAINT `PROJSSOCIATION_DEPPROJECT_FK` FOREIGN KEY (`dependent`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `PROJSSOCIATION_LEADPROJECT_FK` FOREIGN KEY (`lead`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectassociation`
--

LOCK TABLES `projectassociation` WRITE;
/*!40000 ALTER TABLE `projectassociation` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectassociation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectcalendar`
--

DROP TABLE IF EXISTS `projectcalendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectcalendar` (
  `IdProjectCalendar` int(11) NOT NULL AUTO_INCREMENT,
  `idCalendarBase` int(11) DEFAULT NULL,
  `weekStart` int(11) DEFAULT NULL,
  `fiscalYearStart` int(11) DEFAULT NULL,
  `startTime1` double DEFAULT NULL,
  `startTime2` double DEFAULT NULL,
  `endTime1` double DEFAULT NULL,
  `endTime2` double DEFAULT NULL,
  `hoursDay` double DEFAULT NULL,
  `hoursWeek` double DEFAULT NULL,
  `daysMonth` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdProjectCalendar`),
  KEY `calendarbase_ibfk_1` (`idCalendarBase`),
  CONSTRAINT `PROJCALENDAR_CALENDARBASE_FK` FOREIGN KEY (`idCalendarBase`) REFERENCES `calendarbase` (`idCalendarBase`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectcalendar`
--

LOCK TABLES `projectcalendar` WRITE;
/*!40000 ALTER TABLE `projectcalendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectcalendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectcalendarexceptions`
--

DROP TABLE IF EXISTS `projectcalendarexceptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectcalendarexceptions` (
  `IdProjectCalendarException` int(11) NOT NULL AUTO_INCREMENT,
  `IdProjectCalendar` int(11) DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  `FinishDate` date DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdProjectCalendarException`),
  KEY `IdProjectCalendar` (`IdProjectCalendar`),
  CONSTRAINT `PROJCALEXCEPTIONS_PROJCAL_FK` FOREIGN KEY (`IdProjectCalendar`) REFERENCES `projectcalendar` (`IdProjectCalendar`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectcalendarexceptions`
--

LOCK TABLES `projectcalendarexceptions` WRITE;
/*!40000 ALTER TABLE `projectcalendarexceptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectcalendarexceptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectcharter`
--

DROP TABLE IF EXISTS `projectcharter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectcharter` (
  `IdProjectCharter` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) NOT NULL,
  `BusinessNeed` varchar(1500) DEFAULT NULL,
  `ProjectObjectives` varchar(1500) DEFAULT NULL,
  `SucessCriteria` varchar(1500) DEFAULT NULL,
  `MainConstraints` varchar(1500) DEFAULT NULL,
  `Milestones` varchar(200) DEFAULT NULL,
  `MainAssumptions` varchar(1500) DEFAULT NULL,
  `MainRisks` varchar(1500) DEFAULT NULL,
  `exclusions` varchar(1500) DEFAULT NULL,
  `mainDeliverables` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`IdProjectCharter`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `PROJECTCHARTER_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectcharter`
--

LOCK TABLES `projectcharter` WRITE;
/*!40000 ALTER TABLE `projectcharter` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectcharter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectclosure`
--

DROP TABLE IF EXISTS `projectclosure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectclosure` (
  `idProjectClouse` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `projectResults` varchar(1500) DEFAULT NULL,
  `goalAchievement` varchar(1500) DEFAULT NULL,
  `lessonsLearned` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`idProjectClouse`),
  KEY `FK_projectclosure_1` (`idProject`),
  CONSTRAINT `PROJECTCLOSURE_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectclosure`
--

LOCK TABLES `projectclosure` WRITE;
/*!40000 ALTER TABLE `projectclosure` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectclosure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectcosts`
--

DROP TABLE IF EXISTS `projectcosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectcosts` (
  `IdProjectCosts` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `CostDate` date DEFAULT NULL,
  PRIMARY KEY (`IdProjectCosts`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `PROJECTCOSTS_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectcosts`
--

LOCK TABLES `projectcosts` WRITE;
/*!40000 ALTER TABLE `projectcosts` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectcosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectdata`
--

DROP TABLE IF EXISTS `projectdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectdata` (
  `idProjectData` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `canceled` bit(1) DEFAULT b'0',
  `dateCanceled` date DEFAULT NULL,
  `commentCanceled` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`idProjectData`),
  KEY `idProject` (`idProject`),
  CONSTRAINT `projectdata_ibfk_1` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectdata`
--

LOCK TABLES `projectdata` WRITE;
/*!40000 ALTER TABLE `projectdata` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectdata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectfollowup`
--

DROP TABLE IF EXISTS `projectfollowup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectfollowup` (
  `IdProjectFollowup` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `FollowupDate` date DEFAULT NULL,
  `EV` double DEFAULT NULL,
  `PV` double DEFAULT NULL,
  `AC` double DEFAULT NULL,
  `GeneralComments` varchar(3000) DEFAULT NULL,
  `RisksComments` varchar(3000) DEFAULT NULL,
  `CostComments` varchar(3000) DEFAULT NULL,
  `ScheduleComments` varchar(3000) DEFAULT NULL,
  `GeneralFlag` char(1) DEFAULT NULL,
  `RiskFlag` char(1) DEFAULT NULL,
  `CostFlag` char(1) DEFAULT NULL,
  `ScheduleFlag` char(1) DEFAULT NULL,
  `PerformanceDoc` varchar(50) DEFAULT NULL,
  `riskRating` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdProjectFollowup`),
  KEY `IdProject` (`IdProject`),
  CONSTRAINT `PROJECTFOLLOWUP_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectfollowup`
--

LOCK TABLES `projectfollowup` WRITE;
/*!40000 ALTER TABLE `projectfollowup` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectfollowup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectfundingsource`
--

DROP TABLE IF EXISTS `projectfundingsource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectfundingsource` (
  `idProjFundingSource` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idFundingSource` int(10) unsigned NOT NULL,
  `idProject` int(11) NOT NULL,
  `percentage` int(11) DEFAULT NULL,
  `epigrafeEuro` varchar(50) DEFAULT NULL,
  `epigrafeDolar` varchar(50) DEFAULT NULL,
  `euros` double DEFAULT NULL,
  `dolares` double DEFAULT NULL,
  PRIMARY KEY (`idProjFundingSource`),
  KEY `PROJFUNDSOURC_PROJECT_FK` (`idProject`),
  KEY `PROJFUNDSOURC_FOUNDSOURC_FK` (`idFundingSource`),
  CONSTRAINT `PROJFUNDSOURC_FOUNDSOURC_FK` FOREIGN KEY (`idFundingSource`) REFERENCES `fundingsource` (`idFundingSource`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PROJFUNDSOURC_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectfundingsource`
--

LOCK TABLES `projectfundingsource` WRITE;
/*!40000 ALTER TABLE `projectfundingsource` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectfundingsource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectkpi`
--

DROP TABLE IF EXISTS `projectkpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectkpi` (
  `IdProjectKPI` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) NOT NULL,
  `idMetricKpi` int(10) unsigned DEFAULT NULL,
  `UpperThreshold` double DEFAULT NULL,
  `LowerThreshold` double DEFAULT NULL,
  `Weight` double DEFAULT NULL,
  `Value` double DEFAULT NULL,
  `aggregateKpi` bit(1) DEFAULT NULL,
  `specificKpi` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdProjectKPI`),
  KEY `IdProject` (`IdProject`),
  KEY `IdKPI` (`idMetricKpi`),
  CONSTRAINT `PROJECTKPI_METRICKPI_FK` FOREIGN KEY (`idMetricKpi`) REFERENCES `metrickpi` (`idMetricKpi`),
  CONSTRAINT `PROJECTKPI_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectkpi`
--

LOCK TABLES `projectkpi` WRITE;
/*!40000 ALTER TABLE `projectkpi` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectkpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectlabel`
--

DROP TABLE IF EXISTS `projectlabel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectlabel` (
  `idProjectLabel` int(11) NOT NULL AUTO_INCREMENT,
  `idLabel` int(11) NOT NULL,
  `idProject` int(11) NOT NULL,
  PRIMARY KEY (`idProjectLabel`),
  KEY `PROJLABEL_PROJ_FK` (`idProject`),
  KEY `PROJLABEL_LABEL_FK` (`idLabel`),
  CONSTRAINT `PROJLABEL_LABEL_FK` FOREIGN KEY (`idLabel`) REFERENCES `label` (`idLabel`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `PROJLABEL_PROJ_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectlabel`
--

LOCK TABLES `projectlabel` WRITE;
/*!40000 ALTER TABLE `projectlabel` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectlabel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcepool`
--

DROP TABLE IF EXISTS `resourcepool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcepool` (
  `idResourcepool` int(11) NOT NULL AUTO_INCREMENT,
  `idCompany` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idResourcepool`),
  KEY `idCompany` (`idCompany`),
  CONSTRAINT `idCompany` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcepool`
--

LOCK TABLES `resourcepool` WRITE;
/*!40000 ALTER TABLE `resourcepool` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourcepool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourceprofiles`
--

DROP TABLE IF EXISTS `resourceprofiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceprofiles` (
  `IdProfile` int(11) NOT NULL,
  `ProfileName` varchar(50) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IdProfile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourceprofiles`
--

LOCK TABLES `resourceprofiles` WRITE;
/*!40000 ALTER TABLE `resourceprofiles` DISABLE KEYS */;
INSERT INTO `resourceprofiles` VALUES (1,'Team Member',NULL),(2,'Project Manager',NULL),(3,'Investment Manager',NULL),(4,'Functional Manager',NULL),(5,'Program Manager',NULL),(6,'Resource Manager',NULL),(7,'PMO',NULL),(8,'Sponsor',NULL),(9,'Portfolio Manager',NULL),(10,'Admin',NULL),(11,'Stakeholder',NULL);
/*!40000 ALTER TABLE `resourceprofiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riskcategories`
--

DROP TABLE IF EXISTS `riskcategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riskcategories` (
  `IdRiskCategory` int(11) NOT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `riskType` int(11) NOT NULL,
  `mitigate` bit(1) NOT NULL,
  `accept` bit(1) NOT NULL,
  PRIMARY KEY (`IdRiskCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riskcategories`
--

LOCK TABLES `riskcategories` WRITE;
/*!40000 ALTER TABLE `riskcategories` DISABLE KEYS */;
INSERT INTO `riskcategories` VALUES (1,'Accept (contain)',1,'\0',''),(2,'Avoid',1,'\0','\0'),(3,'Mitigate',1,'',''),(4,'Transfer',1,'\0','\0'),(5,'Accept',0,'\0','\0'),(6,'Exploit',0,'\0','\0'),(7,'Enhance',0,'\0','\0'),(8,'Share',0,'\0','\0'),(9,'Accept (passive)',1,'\0','');
/*!40000 ALTER TABLE `riskcategories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riskcategory`
--

DROP TABLE IF EXISTS `riskcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riskcategory` (
  `idRiskcategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idRiskcategory`),
  KEY `RISKCATEGORY_COMPANY_FK` (`idCompany`),
  CONSTRAINT `RISKCATEGORY_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riskcategory`
--

LOCK TABLES `riskcategory` WRITE;
/*!40000 ALTER TABLE `riskcategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `riskcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riskreassessmentlog`
--

DROP TABLE IF EXISTS `riskreassessmentlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riskreassessmentlog` (
  `IdLog` int(11) NOT NULL AUTO_INCREMENT,
  `IdRisk` int(11) DEFAULT NULL,
  `RiskDate` date DEFAULT NULL,
  `RISKCHANGE` varchar(3000) DEFAULT NULL,
  `idEmployee` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdLog`),
  KEY `IdRisk` (`IdRisk`),
  KEY `RISKLOG_EMPLOYEE_FK` (`idEmployee`),
  CONSTRAINT `RISKLOG_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `RISKREASSLOG_RISKREGISTER_FK` FOREIGN KEY (`IdRisk`) REFERENCES `riskregister` (`IdRisk`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riskreassessmentlog`
--

LOCK TABLES `riskreassessmentlog` WRITE;
/*!40000 ALTER TABLE `riskreassessmentlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `riskreassessmentlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riskregister`
--

DROP TABLE IF EXISTS `riskregister`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riskregister` (
  `IdRisk` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) DEFAULT NULL,
  `IdRiskCategory` int(11) DEFAULT NULL,
  `RiskCode` varchar(5) DEFAULT NULL,
  `RiskName` varchar(50) DEFAULT NULL,
  `Owner` varchar(100) DEFAULT NULL,
  `DateRaised` date DEFAULT NULL,
  `PotentialCost` double DEFAULT NULL,
  `PotentialDelay` int(11) DEFAULT NULL,
  `RISK_TRIGGER` varchar(3000) DEFAULT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `Probability` int(11) DEFAULT NULL,
  `Impact` int(11) DEFAULT NULL,
  `Materialized` bit(1) DEFAULT NULL,
  `MITIGATIONACTIONSREQUIRED` varchar(3000) DEFAULT NULL,
  `CONTINGENCYACTIONSREQUIRED` varchar(3000) DEFAULT NULL,
  `ActualMaterializationCost` double DEFAULT NULL,
  `ActualMaterializationDelay` int(11) DEFAULT NULL,
  `FINALCOMMENTS` varchar(3000) DEFAULT NULL,
  `RiskDoc` varchar(100) DEFAULT NULL,
  `riskType` int(11) DEFAULT NULL,
  `PlannedMitigationCost` double DEFAULT NULL,
  `PlannedContingencyCost` double DEFAULT NULL,
  `Closed` bit(1) DEFAULT NULL,
  `dateMaterialization` date DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `RESPONSEDESCRIPTION` varchar(3000) DEFAULT NULL,
  `RESIDUALRISK` varchar(3000) DEFAULT NULL,
  `residualCost` double DEFAULT NULL,
  `idRiskcategoryTemp` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdRisk`),
  KEY `IdProject` (`IdProject`),
  KEY `IdRiskCategory` (`IdRiskCategory`),
  KEY `RISKREGISTER_RISKCATEGORY_FK_idx` (`idRiskcategoryTemp`),
  CONSTRAINT `RISKREGISTER_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `RISKREGISTER_RISKCATEGORIES_FK` FOREIGN KEY (`IdRiskCategory`) REFERENCES `riskcategories` (`IdRiskCategory`) ON DELETE SET NULL,
  CONSTRAINT `RISKREGISTER_RISKCATEGORY_FK` FOREIGN KEY (`idRiskcategoryTemp`) REFERENCES `riskcategory` (`idRiskcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riskregister`
--

LOCK TABLES `riskregister` WRITE;
/*!40000 ALTER TABLE `riskregister` DISABLE KEYS */;
/*!40000 ALTER TABLE `riskregister` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riskregistertemplate`
--

DROP TABLE IF EXISTS `riskregistertemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riskregistertemplate` (
  `IdRisk` int(11) NOT NULL AUTO_INCREMENT,
  `IdRiskCategory` int(11) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  `RiskCode` varchar(5) DEFAULT NULL,
  `RiskName` varchar(50) DEFAULT NULL,
  `Owner` varchar(100) DEFAULT NULL,
  `PotentialCost` double DEFAULT NULL,
  `PotentialDelay` int(11) DEFAULT NULL,
  `Risk_Trigger` varchar(200) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Probability` int(11) DEFAULT NULL,
  `Impact` int(11) DEFAULT NULL,
  `Materialized` bit(1) DEFAULT NULL,
  `MitigationActionsRequired` varchar(200) DEFAULT NULL,
  `ContingencyActionsRequired` varchar(200) DEFAULT NULL,
  `ActualMaterializationCost` double DEFAULT NULL,
  `ActualMaterializationDelay` int(11) DEFAULT NULL,
  `finalComments` varchar(200) DEFAULT NULL,
  `RiskDoc` varchar(100) DEFAULT NULL,
  `riskType` int(11) DEFAULT NULL,
  `PlannedMitigationCost` double DEFAULT NULL,
  `PlannedContingencyCost` double DEFAULT NULL,
  `Closed` bit(1) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `responseDescription` varchar(200) DEFAULT NULL,
  `residualRisk` varchar(200) DEFAULT NULL,
  `residualCost` double DEFAULT NULL,
  `idRiskCateg` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdRisk`),
  KEY `RISKREGISTERTEMPLATE_RISKCATEGORIES_FK` (`IdRiskCategory`),
  KEY `RISKREGISTERTEMPLATE_COMPANY_FK` (`idCompany`),
  KEY `RISKREGTEMPL_RISKCATEGORY_FK` (`idRiskCateg`),
  CONSTRAINT `RISKREGISTERTEMPLATE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`),
  CONSTRAINT `RISKREGISTERTEMPLATE_RISKCATEGORIES_FK` FOREIGN KEY (`IdRiskCategory`) REFERENCES `riskcategories` (`IdRiskCategory`),
  CONSTRAINT `RISKREGTEMPL_RISKCATEGORY_FK` FOREIGN KEY (`idRiskCateg`) REFERENCES `riskcategory` (`idRiskcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riskregistertemplate`
--

LOCK TABLES `riskregistertemplate` WRITE;
/*!40000 ALTER TABLE `riskregistertemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `riskregistertemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security`
--

DROP TABLE IF EXISTS `security`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security` (
  `IdSec` int(11) NOT NULL AUTO_INCREMENT,
  `Login` varchar(20) DEFAULT NULL,
  `Password` varchar(35) DEFAULT NULL,
  `AutorizationLevel` char(1) DEFAULT NULL,
  `dateCreation` date NOT NULL,
  `dateLapsed` datetime DEFAULT NULL,
  `attempts` int(10) unsigned DEFAULT '0',
  `idContact` int(11) NOT NULL,
  `dateLastAttempt` datetime DEFAULT NULL,
  PRIMARY KEY (`IdSec`),
  KEY `FK_security_1` (`idContact`),
  CONSTRAINT `SECURITY_CONTACT_FK` FOREIGN KEY (`idContact`) REFERENCES `contact` (`IdContact`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security`
--

LOCK TABLES `security` WRITE;
/*!40000 ALTER TABLE `security` DISABLE KEYS */;
INSERT INTO `security` VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3','2','2015-06-17',NULL,0,1,NULL);
/*!40000 ALTER TABLE `security` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller`
--

DROP TABLE IF EXISTS `seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seller` (
  `idSeller` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `selected` bit(1) DEFAULT NULL,
  `qualified` bit(1) DEFAULT NULL,
  `qualificationDate` date DEFAULT NULL,
  `soleSource` bit(1) DEFAULT NULL,
  `singleSource` bit(1) DEFAULT NULL,
  `information` varchar(100) DEFAULT NULL,
  `idCompany` int(11) DEFAULT NULL,
  PRIMARY KEY (`idSeller`),
  KEY `idCompany` (`idCompany`),
  CONSTRAINT `SELLER_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller`
--

LOCK TABLES `seller` WRITE;
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `idSetting` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCompany` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idSetting`),
  KEY `SETTING_COMPANY_FK` (`idCompany`),
  CONSTRAINT `SETTING_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
INSERT INTO `setting` VALUES (1,1,'setting.performance_reports','true');
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill` (
  `idSkill` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idSkill`),
  KEY `FK_skill_1` (`idCompany`),
  CONSTRAINT `SKILL_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skillsemployee`
--

DROP TABLE IF EXISTS `skillsemployee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skillsemployee` (
  `idSkillsEmployee` int(11) NOT NULL AUTO_INCREMENT,
  `idEmployee` int(11) NOT NULL,
  `idSkill` int(11) NOT NULL,
  PRIMARY KEY (`idSkillsEmployee`),
  KEY `IdEmployee` (`idEmployee`),
  KEY `IdSkill` (`idSkill`),
  CONSTRAINT `SKILLSEMPLOYEE_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `SKILLSEMPLOYEE_SKILL_FK` FOREIGN KEY (`idSkill`) REFERENCES `skill` (`idSkill`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skillsemployee`
--

LOCK TABLES `skillsemployee` WRITE;
/*!40000 ALTER TABLE `skillsemployee` DISABLE KEYS */;
/*!40000 ALTER TABLE `skillsemployee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stagegate`
--

DROP TABLE IF EXISTS `stagegate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stagegate` (
  `idStageGate` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idStageGate`),
  KEY `STAGE_COMPANY_FK` (`idCompany`),
  CONSTRAINT `STAGE_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stagegate`
--

LOCK TABLES `stagegate` WRITE;
/*!40000 ALTER TABLE `stagegate` DISABLE KEYS */;
/*!40000 ALTER TABLE `stagegate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stakeholder`
--

DROP TABLE IF EXISTS `stakeholder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stakeholder` (
  `IdStakeholder` int(11) NOT NULL AUTO_INCREMENT,
  `IdProject` int(11) NOT NULL,
  `ProjectRole` varchar(50) DEFAULT NULL,
  `Requirements` varchar(1500) DEFAULT NULL,
  `Expectations` varchar(1500) DEFAULT NULL,
  `Influence` varchar(50) DEFAULT NULL,
  `MgtStrategy` varchar(1500) DEFAULT NULL,
  `Type` char(1) DEFAULT NULL,
  `contactName` varchar(50) DEFAULT NULL,
  `department` varchar(150) DEFAULT NULL,
  `orderToShow` int(11) DEFAULT '100',
  `idEmployee` int(11) DEFAULT NULL,
  `comments` varchar(1500) DEFAULT NULL,
  `idStakeholderClassification` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdStakeholder`),
  KEY `IdProject` (`IdProject`),
  KEY `STAKEHOLDER_EMPLESTAKEHOLDER_FK` (`idEmployee`),
  KEY `STAKEHOLDER_SHCLASSIF_FK` (`idStakeholderClassification`),
  CONSTRAINT `STAKEHOLDER_EMPLESTAKEHOLDER_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`),
  CONSTRAINT `STAKEHOLDER_PROJECT_FK` FOREIGN KEY (`IdProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE,
  CONSTRAINT `STAKEHOLDER_SHCLASSIF_FK` FOREIGN KEY (`idStakeholderClassification`) REFERENCES `stakeholderclassification` (`idStakeholderClassification`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stakeholder`
--

LOCK TABLES `stakeholder` WRITE;
/*!40000 ALTER TABLE `stakeholder` DISABLE KEYS */;
/*!40000 ALTER TABLE `stakeholder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stakeholderclassification`
--

DROP TABLE IF EXISTS `stakeholderclassification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stakeholderclassification` (
  `idStakeholderClassification` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `idCompany` int(11) NOT NULL,
  PRIMARY KEY (`idStakeholderClassification`),
  KEY `SHCLASSIFICATION_COMPANY_FK` (`idCompany`),
  CONSTRAINT `SHCLASSIFICATION_COMPANY_FK` FOREIGN KEY (`idCompany`) REFERENCES `company` (`IdCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stakeholderclassification`
--

LOCK TABLES `stakeholderclassification` WRITE;
/*!40000 ALTER TABLE `stakeholderclassification` DISABLE KEYS */;
INSERT INTO `stakeholderclassification` VALUES (1,'External','External',1),(2,'Internal','Internal',1);
/*!40000 ALTER TABLE `stakeholderclassification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teammember`
--

DROP TABLE IF EXISTS `teammember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teammember` (
  `idTeamMember` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idEmployee` int(11) NOT NULL,
  `dateApproved` date DEFAULT NULL,
  `sellRate` double DEFAULT NULL,
  `fte` int(11) DEFAULT NULL,
  `dateIn` date DEFAULT NULL,
  `dateOut` date DEFAULT NULL,
  `hours` int(10) unsigned DEFAULT NULL,
  `expenses` int(11) DEFAULT NULL,
  `IdActivity` int(11) DEFAULT NULL,
  `idJobCategory` int(11) DEFAULT NULL,
  `status` varchar(11) DEFAULT NULL,
  `commentsPM` varchar(2000) DEFAULT NULL,
  `commentsRM` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`idTeamMember`),
  KEY `IdEmployee` (`idEmployee`),
  KEY `IdActivity` (`IdActivity`),
  KEY `FK_teammember_3` (`idJobCategory`),
  KEY `teammember_ibfk_2` (`idEmployee`),
  CONSTRAINT `TEAMMEMBER_ACTIVITY_FK` FOREIGN KEY (`IdActivity`) REFERENCES `projectactivity` (`IdActivity`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TEAMMEMBER_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TEAMMEMBER_JOBCATEGORY_FK` FOREIGN KEY (`idJobCategory`) REFERENCES `jobcategory` (`idJobCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teammember`
--

LOCK TABLES `teammember` WRITE;
/*!40000 ALTER TABLE `teammember` DISABLE KEYS */;
/*!40000 ALTER TABLE `teammember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timeline`
--

DROP TABLE IF EXISTS `timeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timeline` (
  `idTimeline` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `timelineDate` date NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `importance` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idTimeline`),
  KEY `TIMELINE_PROJECT_FK` (`idProject`),
  CONSTRAINT `TIMELINE_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timeline`
--

LOCK TABLES `timeline` WRITE;
/*!40000 ALTER TABLE `timeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `timeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheet`
--

DROP TABLE IF EXISTS `timesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timesheet` (
  `idTimeSheet` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idEmployee` int(11) NOT NULL,
  `idActivity` int(11) DEFAULT NULL,
  `idOperation` int(11) DEFAULT NULL,
  `initDate` date NOT NULL,
  `endDate` date NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `hoursDay1` double DEFAULT NULL,
  `hoursDay2` double DEFAULT NULL,
  `hoursDay3` double DEFAULT NULL,
  `hoursDay4` double DEFAULT NULL,
  `hoursDay5` double DEFAULT NULL,
  `hoursDay6` double DEFAULT NULL,
  `hoursDay7` double DEFAULT NULL,
  `suggestReject` bit(1) DEFAULT NULL,
  `suggestRejectComment` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`idTimeSheet`),
  KEY `FK_timesheet_1` (`idActivity`),
  KEY `FK_timesheet_2` (`idOperation`),
  KEY `FK_timesheet_3` (`idEmployee`),
  CONSTRAINT `TIMESHEET_ACTIVITY_FK` FOREIGN KEY (`idActivity`) REFERENCES `projectactivity` (`IdActivity`) ON DELETE CASCADE,
  CONSTRAINT `TIMESHEET_EMPLOYEE_FK` FOREIGN KEY (`idEmployee`) REFERENCES `employee` (`idEmployee`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TIMESHEET_OPERATION_FK` FOREIGN KEY (`idOperation`) REFERENCES `operation` (`IdOperation`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheet`
--

LOCK TABLES `timesheet` WRITE;
/*!40000 ALTER TABLE `timesheet` DISABLE KEYS */;
/*!40000 ALTER TABLE `timesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheetcomment`
--

DROP TABLE IF EXISTS `timesheetcomment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timesheetcomment` (
  `idTemesheetComment` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `previousStatus` varchar(10) NOT NULL,
  `actualStatus` varchar(10) NOT NULL,
  `idTimeSheet` int(10) unsigned NOT NULL,
  `contentComment` varchar(1000) DEFAULT NULL,
  `commentDate` datetime NOT NULL,
  PRIMARY KEY (`idTemesheetComment`),
  KEY `FK_timesheetcomment_1` (`idTimeSheet`),
  CONSTRAINT `TIMESHEETCOMMENT_TIMESHEET_FK` FOREIGN KEY (`idTimeSheet`) REFERENCES `timesheet` (`idTimeSheet`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheetcomment`
--

LOCK TABLES `timesheetcomment` WRITE;
/*!40000 ALTER TABLE `timesheetcomment` DISABLE KEYS */;
/*!40000 ALTER TABLE `timesheetcomment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wbsnode`
--

DROP TABLE IF EXISTS `wbsnode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wbsnode` (
  `IdWBSNode` int(11) NOT NULL AUTO_INCREMENT,
  `idProject` int(11) NOT NULL,
  `Code` varchar(25) DEFAULT NULL,
  `Name` varchar(80) DEFAULT NULL,
  `Description` varchar(2500) DEFAULT NULL,
  `IsControlAccount` bit(1) DEFAULT NULL,
  `Budget` double DEFAULT NULL,
  `Parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdWBSNode`),
  KEY `wbsnode_ibfk_2` (`Parent`),
  KEY `WBSNODE_PROJECT_FK` (`idProject`),
  CONSTRAINT `WBSNODE_PARENT_FK` FOREIGN KEY (`Parent`) REFERENCES `wbsnode` (`IdWBSNode`) ON DELETE CASCADE,
  CONSTRAINT `WBSNODE_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wbsnode`
--

LOCK TABLES `wbsnode` WRITE;
/*!40000 ALTER TABLE `wbsnode` DISABLE KEYS */;
/*!40000 ALTER TABLE `wbsnode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wbstemplate`
--

DROP TABLE IF EXISTS `wbstemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wbstemplate` (
  `IdWBSNode` int(11) NOT NULL AUTO_INCREMENT,
  `IdCompany` int(11) NOT NULL,
  `Code` varchar(10) DEFAULT NULL,
  `Name` varchar(80) DEFAULT NULL,
  `Description` varchar(2500) DEFAULT NULL,
  `IsControlAccount` bit(1) DEFAULT NULL,
  `Parent` int(11) DEFAULT NULL,
  `Root` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdWBSNode`),
  KEY `WBSTEMPLATE_PARENT_FK` (`Parent`),
  KEY `company_fk` (`IdCompany`)
) ENGINE=MyISAM AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wbstemplate`
--

LOCK TABLES `wbstemplate` WRITE;
/*!40000 ALTER TABLE `wbstemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `wbstemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workingcosts`
--

DROP TABLE IF EXISTS `workingcosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workingcosts` (
  `idWorkingCosts` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `resourceName` varchar(50) DEFAULT NULL,
  `resourceDepartment` varchar(50) DEFAULT NULL,
  `effort` int(10) unsigned DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `workCost` double DEFAULT NULL,
  `idProject` int(11) NOT NULL,
  `q1` int(10) unsigned DEFAULT NULL,
  `q2` int(10) unsigned DEFAULT NULL,
  `q3` int(10) unsigned DEFAULT NULL,
  `q4` int(10) unsigned DEFAULT NULL,
  `realEffort` int(10) unsigned DEFAULT NULL,
  `idCurrency` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idWorkingCosts`),
  KEY `IdProject` (`idProject`),
  KEY `WORKINGCOSTS_CURRENCY_FK` (`idCurrency`),
  CONSTRAINT `WORKINGCOSTS_CURRENCY_FK` FOREIGN KEY (`idCurrency`) REFERENCES `currency` (`idCurrency`) ON DELETE SET NULL,
  CONSTRAINT `WORKINGCOSTS_PROJECT_FK` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workingcosts`
--

LOCK TABLES `workingcosts` WRITE;
/*!40000 ALTER TABLE `workingcosts` DISABLE KEYS */;
/*!40000 ALTER TABLE `workingcosts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-27 12:35:57
