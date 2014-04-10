DROP TABLE IF EXISTS users;
CREATE TABLE users (id int(11) NOT NULL AUTO_INCREMENT,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  email varchar(255) NOT NULL DEFAULT '',
  encrypted_password varchar(255) NOT NULL DEFAULT '',
  reset_password_token varchar(255) DEFAULT NULL,
  reset_password_sent_at datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_users_on_email` (`email`)
) ENGINE=InnoDB CHARSET=utf8;

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
  id int(11) NOT NULL AUTO_INCREMENT,
  date date,
  species varchar(255) DEFAULT NULL,
  description text COLLATE utf8_unicode_ci NOT NULL,
  amount float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8;
