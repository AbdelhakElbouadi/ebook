--
-- Dumping data for table `Users`
--
INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Customer','abdo','elbo','abdo@mail.net','abdo');
INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Customer','abdo','elbo','admin@mail.net','customer');
INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Customer','lili','loula','loula@mail.net','loula');
INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Admin','abdo','elbo','lili@mail.net','admin');

-- INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Admin','abdo','elbo','admin@mail.net','admin');

-- INSERT INTO Users(DTYPE, fname, lname, email, password) VALUES('Admin','souna','elbo','lili@mail.net','admin');

INSERT INTO Users(DTYPE, fname, lname, email, password, phone_home, phone_work)VALUES('Author','Namir','Jabran','dolga@mail.net','nemo','04225333','45523966'),('Author','Abdo','Rima','sand@mail.net','frank','42563963','42588963');

--
-- Dumping data for table `publisher`
--
INSERT INTO publisher(name, publisher_url) VALUES('Saleem','http://saleem.com'),('Nadeem','http://nadeem.com');

-- Dumping data for table 'category'
INSERT INTO category(name, tags) VALUES('Science Fiction','imagination,fiction'),('Development Personal','personal,development');
-- Dumping data for table 'category_authors'
INSERT INTO category_authors(category_id, author_id) VALUES(1,5),(2,5);
--
-- Dumping data for table `Product`
--
INSERT INTO Product(price,instock,last_updated) VALUES(45.12,100,'2018-05-07'),(99.45,45,'2018-05-07'),(44.66,10,'2018-05-07'),(453.25,15,'2018-05-07'),(150.15,12,'2018-05-07'),(100.99,2,'2018-05-07');
--
-- Dumping data for table `book`
--
INSERT INTO book(id,category_id,title,isbn, image, publisher_id) VALUES(1,2,'My Life And Work','1234567899',NULL, 1),(2,2,'No Way Out','1472583691',NULL,2),(3,2,'One Things','8529637890',NULL,1),(4,2,'Working Hard','1234567899',NULL,1),(5,2,'Working Smart','1234567899',NULL,1),(6,2,'One Love','1234567899',NULL,1);
--
-- Dumping data for table `book_authors`
--

INSERT INTO book_authors(book_id, author_id) VALUES(1,5),(2,6),(3,5),(4,6),(5,5),(6,6);

--
-- Dumping data for table 'rate'
--
INSERT INTO rate(rate, date_created, product_id, customer_id)VALUES(2,'2018-10-12 14:23:20',1,2),(3,'2018-10-12 15:23:20',1,3),(4,'2018-12-12 14:23:20',2,2),(5,'2018-12-12 14:23:20',2,3),(3,'2018-12-12 14:23:20',3,2),(1,'2018-12-12 14:23:20',3,3),(3,'2018-09-12 11:30:12',6,2),(4,'2018-09-12 12:30:12',6,3);

--
-- Dumping data for table `groups`
--

INSERT INTO groups(name) VALUES('Admin'),('Customer'),('Author');

--
-- Dumping data for table `user_group`
--

INSERT INTO user_group(user_id, group_id) VALUES(1,2),(2,2),(3,2),(4,1),(5,3),(6,3);

--
-- Dumping data for table `customerorder`
--

INSERT INTO customerorder(date_created,order_status,customer_id) VALUES('2018-10-12 10:24:10','Pending',1),('2018-10-10 12:30:30','Pending',1),('2018-11-20 10:15:25','Complete',2);

--
-- Dumping data for table `orderitem`
--

INSERT INTO orderitem(quantity,order_id,ordered_product_id) VALUES(3,1,1),(5,2,2),(5,3,4),(3,3,5),(10,3,1),(6,3,3);

