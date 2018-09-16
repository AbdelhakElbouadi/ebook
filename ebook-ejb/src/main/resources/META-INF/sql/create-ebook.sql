CREATE TABLE IF NOT EXISTS Users(DTYPE varchar(31) NOT NULL , id bigint(20) NOT NULL AUTO_INCREMENT, fname varchar(255) DEFAULT NULL,lname varchar(255) DEFAULT NULL, email varchar(45) UNIQUE NOT NULL, password varchar(255) NOT NULL,phone_home varchar(50) DEFAULT NULL,phone_work varchar(50) DEFAULT NULL,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS author(id bigint(20) NOT NULL AUTO_INCREMENT, fname varchar(255) DEFAULT NULL,lname varchar(255) DEFAULT NULL, email varchar(45) UNIQUE NOT NULL, phone_home varchar(50) DEFAULT NULL,phone_work varchar(50) DEFAULT NULL,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `publisher`
--
CREATE TABLE IF NOT EXISTS publisher (id bigint(20) NOT NULL AUTO_INCREMENT, name varchar(255) DEFAULT NULL, publisher_url varchar(255) DEFAULT NULL, PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `category`
--
CREATE TABLE IF NOT EXISTS category(id bigint(20) NOT NULL AUTO_INCREMENT, name varchar(255) DEFAULT NULL, tags varchar(255) DEFAULT NULL ,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table 'category_authors'
-- 
CREATE TABLE IF NOT EXISTS category_authors(category_id bigint(20) NOT NULL, author_id bigint(20) NOT NULL, PRIMARY KEY(category_id,author_id), CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id), CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES author (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for image 'image'
--
CREATE TABLE IF NOT EXISTS image (id bigint(20) NOT NULL AUTO_INCREMENT, name varchar(100) NOT NULL, tag varchar(255) DEFAULT NULL, mime_type varchar(10) NOT NULL, img_data longblob NOT NULL, PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `book`
--
CREATE TABLE IF NOT EXISTS Product (id bigint(20) NOT NULL AUTO_INCREMENT, description varchar(255) DEFAULT NULL, onsale boolean DEFAULT FALSE, instock bigint(20) DEFAULT 0, recharge_at int(5) DEFAULT 5 ,price double NOT NULL, percentage_off double DEFAULT 0.0,last_updated date DEFAULT NULL ,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS book(id bigint(20) NOT NULL, category_id bigint(20) NOT NULL, title varchar(50) NOT NULL, isbn varchar(25) NOT NULL, img_id  bigint(20) NOT NULL, publisher_id bigint(20) NOT NULL, PRIMARY KEY (id),CONSTRAINT fk_book_product_id FOREIGN KEY(id) REFERENCES Product (id), CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id), CONSTRAINT fk_book_publisher_id FOREIGN KEY(publisher_id) REFERENCES publisher (id) on delete cascade on update cascade, CONSTRAINT fk_book_image_id FOREIGN KEY(img_id) REFERENCES image (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- ALTER TABLE book ADD CONSTRAINT fk_book_publisher_id FOREIGN KEY(publisher_id) REFERENCES publisher (id);
--
-- Table structure for table `book_authors`
--
CREATE TABLE IF NOT EXISTS book_authors(book_id bigint(20) NOT NULL,author_id bigint(20) NOT NULL,CONSTRAINT fk_book_authors_book FOREIGN KEY(book_id) REFERENCES ebook.book (id) on delete cascade, CONSTRAINT fk_book_authors_author FOREIGN KEY(author_id) REFERENCES ebook.author (id) on delete cascade) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for image_book 'img_book'
--
-- CREATE TABLE IF NOT EXISTS img_book (book_id bigint(20) NOT NULL,img_id  bigint(20) NOT NULL, CONSTRAINT fk_imgbook_book FOREIGN KEY(book_id) REFERENCES book (id), CONSTRAINT fk_imgbook_img FOREIGN KEY(img_id) REFERENCES image (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `groups`
--
CREATE TABLE IF NOT EXISTS groups (id bigint(20) NOT NULL AUTO_INCREMENT,name varchar(255) DEFAULT NULL,description varchar(255) DEFAULT NULL,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `orders`
--
CREATE TABLE IF NOT EXISTS customerorder (id bigint(20) NOT NULL AUTO_INCREMENT, date_created datetime DEFAULT NULL, order_status varchar(255) DEFAULT NULL,customer_id bigint(20) NOT NULL, PRIMARY KEY (id),CONSTRAINT fk_customerorder_users FOREIGN KEY(customer_id) REFERENCES ebook.Users (id) on delete cascade) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `orderitem`
--
CREATE TABLE IF NOT EXISTS orderitem (id bigint(20) NOT NULL AUTO_INCREMENT, quantity bigint(20) DEFAULT 1, order_id  bigint(20) NOT NULL, ordered_product_id bigint(20) NOT NULL, PRIMARY KEY(id),CONSTRAINT fk_orderitem_order FOREIGN KEY(order_id) REFERENCES ebook.customerorder(id) on delete cascade,CONSTRAINT fk_orderitem_product FOREIGN KEY(ordered_product_id ) REFERENCES ebook.Product(id) on delete cascade) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `user_group`
--
CREATE TABLE IF NOT EXISTS user_group (user_id bigint(20) NOT NULL, group_id bigint(20) NOT NULL, PRIMARY KEY(user_id, group_id),CONSTRAINT fk_usergroup_user FOREIGN KEY(user_id) REFERENCES ebook.Users(id),CONSTRAINT fk_usergroup_group FOREIGN KEY(group_id) REFERENCES ebook.groups(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `bank_card`
--
CREATE TABLE IF NOT EXISTS bank_card (id bigint(20) NOT NULL AUTO_INCREMENT, fname varchar(255) DEFAULT NULL,lname varchar(255) DEFAULT NULL, card_number varchar(255) DEFAULT NULL, cvv varchar(255) DEFAULT NULL, exp_date datetime DEFAULT NULL, PRIMARY KEY(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 
--
-- Table structure for table `bank_card`
--
CREATE TABLE IF NOT EXISTS customer_bankcards (customer_id bigint(20) NOT NULL, bankcard_id bigint(20) NOT NULL, PRIMARY KEY(customer_id, bankcard_id),CONSTRAINT fk_customerbankcard_users FOREIGN KEY(customer_id) REFERENCES ebook.Users(id),CONSTRAINT fk_customerbankcard_bankcard FOREIGN KEY(bankcard_id) REFERENCES ebook.bank_card(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `rate`
--
CREATE TABLE IF NOT EXISTS rate (id bigint(20) NOT NULL AUTO_INCREMENT, rate int(2) DEFAULT 0, date_created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, product_id bigint(20) NOT NULL, customer_id bigint(20) NOT NULL, PRIMARY KEY(id),CONSTRAINT fk_rate_product FOREIGN KEY(product_id) REFERENCES ebook.Product(id) on delete cascade,CONSTRAINT fk_rate_customer FOREIGN KEY(customer_id) REFERENCES ebook.Users(id) on delete cascade) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `comment`
--
CREATE TABLE IF NOT EXISTS comment (id bigint(20) NOT NULL AUTO_INCREMENT, content text NOT NULL, date_created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, product_id bigint(20) NOT NULL, customer_id bigint(20) NOT NULL, PRIMARY KEY(id),CONSTRAINT fk_comment_product FOREIGN KEY(product_id) REFERENCES ebook.Product(id) on delete cascade,CONSTRAINT fk_comment_customer FOREIGN KEY(customer_id) REFERENCES ebook.Users(id) on delete cascade) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Table structure for table `comment_replies`
--
CREATE TABLE IF NOT EXISTS comment_replies(comment_id bigint(20) NOT NULL, reply_id bigint(20) NOT NULL, PRIMARY KEY(comment_id, reply_id),CONSTRAINT fk_commentreplies_comment FOREIGN KEY(comment_id) REFERENCES ebook.comment(id),CONSTRAINT fk_commentreplies_reply FOREIGN KEY(reply_id) REFERENCES ebook.comment(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

