CREATE TABLE `product` (
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
