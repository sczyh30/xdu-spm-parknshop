CREATE TABLE `parknshop`.`admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(45) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`advertisement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `apply_id` bigint(20) NOT NULL,
  `ad_type` tinyint(2) NOT NULL,
  `ad_owner` bigint(20) NOT NULL,
  `ad_target` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `ad_pic_url` varchar(1024) NOT NULL,
  `ad_total_price` int(11) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(2) NOT NULL,
  `payment_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ad_type_target` (`ad_type`,`ad_target`),
  KEY `idx_start_time` (`start_date`),
  KEY `idx_end_time` (`end_date`),
  KEY `idx_ad_owner` (`ad_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`apply_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `apply_id` bigint(20) NOT NULL,
  `apply_event_type` tinyint(3) NOT NULL,
  `processor_id` varchar(128) NOT NULL COMMENT 'The role who process the application and produce the event',
  `extra_info` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_apply_id` (`apply_id`),
  KEY `idx_processor_id` (`processor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`apply_metadata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `proposer_id` varchar(128) NOT NULL,
  `apply_type` tinyint(2) NOT NULL,
  `apply_data` json NOT NULL,
  `status` tinyint(3) NOT NULL,
  `ad_payment_virtual` varchar(64) GENERATED ALWAYS AS (json_extract(`apply_data`,'$.paymentId')) VIRTUAL,
  PRIMARY KEY (`id`),
  KEY `idx_proposer_id` (`proposer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `comment_text` varchar(1024) NOT NULL,
  `rate` tinyint(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`delivery_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `detail_address` varchar(255) NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`delivery_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` bigint(20) NOT NULL,
  `delivery_type` int(11) NOT NULL,
  `outer_delivery_id` varchar(64) NOT NULL,
  `delivery_data` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id_UNIQUE` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`delivery_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `store_id` bigint(20) NOT NULL,
  `express_type` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `default_price` decimal(10,2) NOT NULL,
  `freight_rule` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_express_type` (`express_type`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint(20) NOT NULL,
  `favorite_type` tinyint(2) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_favorite_type` (`favorite_type`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`inventory` (
  `id` bigint(20) NOT NULL,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`order_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `order_id` bigint(20) NOT NULL,
  `order_event_type` tinyint(3) NOT NULL,
  `extra_data` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_event_type` (`order_event_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`order_metadata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creator_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `payment_id` varchar(64) NOT NULL,
  `address_snapshot` json NOT NULL,
  `delivery_id` bigint(20) DEFAULT NULL,
  `freight_price` decimal(6,2) NOT NULL,
  `final_total_price` decimal(10,2) NOT NULL,
  `order_status` tinyint(3) NOT NULL,
  `commission_snapshot` decimal(4,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_gmt_create` (`gmt_create`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`order_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `amount` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`payment_record` (
  `id` varchar(64) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_type` tinyint(2) NOT NULL,
  `payment_id` varchar(64) DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` tinyint(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(128) NOT NULL,
  `catalog_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `pic_uri` varchar(128) NOT NULL DEFAULT 'default_product_picture.jpg',
  `description` text NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `status` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_catalog_id` (`catalog_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_is_deleted` (`is_deleted`),
  FULLTEXT KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`refund_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` bigint(20) NOT NULL,
  `sub_order_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `refund_transaction_no` varchar(64) DEFAULT NULL,
  `refund_amount` varchar(45) NOT NULL,
  `buy_payment_id` varchar(64) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `refund_status` int(11) NOT NULL DEFAULT '0',
  `refund_request_message` varchar(1024) NOT NULL,
  `refund_response_message` varchar(1024) DEFAULT NULL,
  `refund_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `seller_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `brief_description` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(60) DEFAULT NULL,
  `telephone` varchar(32) NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_seller_id` (`seller_id`),
  KEY `idx_status` (`status`),
  FULLTEXT KEY `fulltext_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`transfer_transaction` (
  `id` varchar(64) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `transaction_no` varchar(64) NOT NULL,
  `payee_account` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `parknshop`.`user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `telephone` varchar(32) DEFAULT NULL,
  `user_type` tinyint(1) NOT NULL DEFAULT '0',
  `user_status` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_telephone` (`telephone`),
  KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;