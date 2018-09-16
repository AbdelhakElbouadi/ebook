ALTER TABLE ebook.user_group DROP FOREIGN KEY fk_usergroup_user;
ALTER TABLE ebook.user_group DROP FOREIGN KEY fk_usergroup_group;

ALTER TABLE ebook.orderitem DROP FOREIGN KEY fk_orderitem_order;
ALTER TABLE ebook.orderitem DROP FOREIGN KEY fk_orderitem_product;

ALTER TABLE ebook.customerorder DROP FOREIGN KEY fk_customerorder_users;

ALTER TABLE ebook.book_authors DROP FOREIGN KEY fk_book_authors_book;
ALTER TABLE ebook.book_authors DROP FOREIGN KEY fk_book_authors_author;

ALTER TABLE ebook.book DROP FOREIGN KEY fk_book_product_id;
ALTER TABLE ebook.book DROP FOREIGN KEY fk_book_publisher_id;
ALTER TABLE ebook.book DROP FOREIGN KEY fk_product_category;

ALTER TABLE ebook.category_authors DROP FOREIGN KEY fk_category;
ALTER TABLE ebook.category_authors DROP FOREIGN KEY fk_author;

ALTER TABLE ebook.customer_bankcards DROP FOREIGN KEY fk_customerbankcard_users;
ALTER TABLE ebook.customer_bankcards DROP FOREIGN KEY fk_customerbankcard_bankcard;

ALTER TABLE ebook.rate DROP FOREIGN KEY fk_rate_product;
ALTER TABLE ebook.rate DROP FOREIGN KEY fk_rate_customer;

ALTER TABLE ebook.comment_replies DROP FOREIGN KEY fk_commentreplies_comment;
ALTER TABLE ebook.comment_replies DROP FOREIGN KEY fk_commentreplies_reply;

ALTER TABLE ebook.comment DROP FOREIGN KEY fk_comment_product;
ALTER TABLE ebook.comment DROP FOREIGN KEY fk_comment_customer;


DROP TABLE  ebook.user_group;
DROP TABLE  ebook.groups;
DROP TABLE  ebook.orderitem;
DROP TABLE  ebook.customerorder;
DROP TABLE  ebook.book_authors;
DROP TABLE  ebook.book;
DROP TABLE  ebook.customer_bankcards;
DROP TABLE  ebook.rate;
DROP TABLE  ebook.comment_replies;
DROP TABLE  ebook.comment;
DROP TABLE  ebook.publisher;
DROP TABLE  ebook.bank_card;
DROP TABLE  ebook.category_authors;
DROP TABLE  ebook.category;
DROP TABLE  ebook.Product;
DROP TABLE  ebook.Users;








