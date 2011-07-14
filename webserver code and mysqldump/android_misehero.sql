-- phpMyAdmin SQL Dump
-- version 3.3.10.2
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2011 年 07 月 14 日 04:47
-- 服务器版本: 5.0.92
-- PHP 版本: 5.2.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `android_misehero`
--

-- --------------------------------------------------------

--
-- 表的结构 `wp_rank_easy`
--

CREATE TABLE IF NOT EXISTS `wp_rank_easy` (
  `phone_id` varchar(45) NOT NULL,
  `time` int(11) NOT NULL,
  `ranking` int(11) NOT NULL,
  PRIMARY KEY  (`phone_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `wp_rank_easy`
--

INSERT INTO `wp_rank_easy` (`phone_id`, `time`, `ranking`) VALUES
('00000000-0033-c587-ffff-ffffcdf05926', 19, 1),
('ffffffff-9eaf-9658-d845-dcd301260093', 42, 2);

-- --------------------------------------------------------

--
-- 表的结构 `wp_rank_hard`
--

CREATE TABLE IF NOT EXISTS `wp_rank_hard` (
  `phone_id` varchar(45) NOT NULL,
  `time` int(11) NOT NULL,
  `ranking` int(11) NOT NULL,
  PRIMARY KEY  (`phone_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `wp_rank_hard`
--


-- --------------------------------------------------------

--
-- 表的结构 `wp_rank_normal`
--

CREATE TABLE IF NOT EXISTS `wp_rank_normal` (
  `phone_id` varchar(45) NOT NULL,
  `time` int(11) NOT NULL,
  `ranking` int(11) NOT NULL,
  PRIMARY KEY  (`phone_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `wp_rank_normal`
--


-- --------------------------------------------------------

--
-- 表的结构 `wp_user`
--

CREATE TABLE IF NOT EXISTS `wp_user` (
  `phone_id` varchar(45) character set latin1 NOT NULL,
  `username` varchar(45) character set utf8 NOT NULL,
  PRIMARY KEY  (`phone_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- 转存表中的数据 `wp_user`
--

INSERT INTO `wp_user` (`phone_id`, `username`) VALUES
('ffffffff-9eaf-9658-d845-dcd301260093', '黄浪'),
('00000000-0033-c587-ffff-ffffcdf05926', '邓 宏立');
