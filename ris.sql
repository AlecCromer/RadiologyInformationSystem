-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 08, 2019 at 09:52 PM
-- Server version: 5.6.26
-- PHP Version: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ris`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE IF NOT EXISTS `address` (
  `address_id` varchar(30) NOT NULL,
  `street_name` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `zip` varchar(30) NOT NULL,
  `state` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE IF NOT EXISTS `appointments` (
  `appointment_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_time` time NOT NULL,
  `patient_sign_in_time` time NOT NULL,
  `patient_sign_out_time` time NOT NULL,
  `procedure_id` int(11) NOT NULL,
  `machine_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `reason_for_referral` varchar(20) NOT NULL,
  `special_comments` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE IF NOT EXISTS `employees` (
  `employee_id` int(11) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `available` varchar(20) NOT NULL,
  `email` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `image`
--

CREATE TABLE IF NOT EXISTS `image` (
  `image_id` int(11) NOT NULL,
  `imagedata_or_imagelink` blob NOT NULL,
  `machine_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `exam_date` date NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `image_report_relationship`
--

CREATE TABLE IF NOT EXISTS `image_report_relationship` (
  `report_relationship_id` int(11) NOT NULL,
  `image_id` int(11) NOT NULL,
  `report_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `machine_type`
--

CREATE TABLE IF NOT EXISTS `machine_type` (
  `machine_type_id` int(6) NOT NULL,
  `machine_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `modality`
--

CREATE TABLE IF NOT EXISTS `modality` (
  `machine_id` int(11) NOT NULL,
  `machine_type_id` int(11) NOT NULL,
  `date_purchased` date NOT NULL,
  `time_between_checkups` time NOT NULL,
  `machine_information` varchar(20) NOT NULL,
  `next_maintainence_date` date NOT NULL,
  `available` tinyint(1) NOT NULL,
  `machine_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `patient`
--

CREATE TABLE IF NOT EXISTS `patient` (
  `patient_id` int(11) NOT NULL,
  `first_name` varchar(0) NOT NULL,
  `last_name` varchar(0) NOT NULL,
  `date_of_birth` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sex` varchar(0) NOT NULL,
  `home_phone` int(11) NOT NULL,
  `email` varchar(0) NOT NULL,
  `insurance_number` int(11) NOT NULL,
  `policy_number` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `patient_medications_list` varchar(0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `physician`
--

CREATE TABLE IF NOT EXISTS `physician` (
  `physician_id` int(6) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `procedure`
--

CREATE TABLE IF NOT EXISTS `procedure` (
  `procedure_id` int(6) NOT NULL,
  `procedure_name` varchar(20) NOT NULL,
  `machine_type_id` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `procedure_relationship`
--

CREATE TABLE IF NOT EXISTS `procedure_relationship` (
  `procedure_relationship_id` int(11) NOT NULL,
  `procedure_id` int(11) NOT NULL,
  `image_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `refer`
--

CREATE TABLE IF NOT EXISTS `refer` (
  `referring_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE IF NOT EXISTS `report` (
  `report_id` int(11) NOT NULL,
  `clinical_indication` varchar(20) NOT NULL,
  `exam` varchar(30) NOT NULL,
  `isotope` varchar(30) NOT NULL,
  `report_details` varchar(20) NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `permissions` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `role_relationship`
--

CREATE TABLE IF NOT EXISTS `role_relationship` (
  `role_relationship_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`appointment_id`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `image`
--
ALTER TABLE `image`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `machine_id` (`machine_id`);

--
-- Indexes for table `image_report_relationship`
--
ALTER TABLE `image_report_relationship`
  ADD PRIMARY KEY (`report_relationship_id`);

--
-- Indexes for table `machine_type`
--
ALTER TABLE `machine_type`
  ADD PRIMARY KEY (`machine_type_id`);

--
-- Indexes for table `modality`
--
ALTER TABLE `modality`
  ADD PRIMARY KEY (`machine_id`);

--
-- Indexes for table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`patient_id`);

--
-- Indexes for table `physician`
--
ALTER TABLE `physician`
  ADD PRIMARY KEY (`physician_id`);

--
-- Indexes for table `procedure`
--
ALTER TABLE `procedure`
  ADD PRIMARY KEY (`procedure_id`);

--
-- Indexes for table `procedure_relationship`
--
ALTER TABLE `procedure_relationship`
  ADD PRIMARY KEY (`procedure_relationship_id`),
  ADD KEY `procedure_id` (`procedure_id`),
  ADD KEY `image_id` (`image_id`);

--
-- Indexes for table `refer`
--
ALTER TABLE `refer`
  ADD PRIMARY KEY (`referring_id`),
  ADD KEY `employee_id` (`employee_id`),
  ADD KEY `patient_id` (`patient_id`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`report_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `role_relationship`
--
ALTER TABLE `role_relationship`
  ADD PRIMARY KEY (`role_relationship_id`),
  ADD UNIQUE KEY `employee_id_2` (`employee_id`),
  ADD KEY `employee_id` (`employee_id`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `modality`
--
ALTER TABLE `modality`
  MODIFY `machine_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `physician`
--
ALTER TABLE `physician`
  MODIFY `physician_id` int(6) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `procedure`
--
ALTER TABLE `procedure`
  MODIFY `procedure_id` int(6) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `procedure_relationship`
--
ALTER TABLE `procedure_relationship`
  MODIFY `procedure_relationship_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `refer`
--
ALTER TABLE `refer`
  MODIFY `referring_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `role_relationship`
--
ALTER TABLE `role_relationship`
  MODIFY `role_relationship_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `image`
--
ALTER TABLE `image`
  ADD CONSTRAINT `image_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `image_ibfk_2` FOREIGN KEY (`machine_id`) REFERENCES `modality` (`machine_id`);

--
-- Constraints for table `procedure_relationship`
--
ALTER TABLE `procedure_relationship`
  ADD CONSTRAINT `procedure_relationship_ibfk_1` FOREIGN KEY (`procedure_id`) REFERENCES `procedure` (`procedure_id`),
  ADD CONSTRAINT `procedure_relationship_ibfk_2` FOREIGN KEY (`image_id`) REFERENCES `image` (`image_id`);

--
-- Constraints for table `refer`
--
ALTER TABLE `refer`
  ADD CONSTRAINT `refer_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  ADD CONSTRAINT `refer_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
