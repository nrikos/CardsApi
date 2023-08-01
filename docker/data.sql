 create database cards_db;
# use cards_db;
#
# CREATE TABLE users
# (
#     user_id  int(45)      NOT NULL AUTO_INCREMENT,
#     email    varchar(45)  NOT NULL,
#     password varchar(125) NOT NULL,
#     PRIMARY KEY (user_id),
#     UNIQUE KEY email_unique (email)
# );
#
# CREATE TABLE user_roles
# (
#     user_id int(45) NOT NULL,
#     role    varchar(45) NOT NULL,
#     primary key (user_id, role),
#     FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE cards
# (
#     card_id     int         NOT NULL AUTO_INCREMENT,
#     name        varchar(45) NOT NULL,
#     description varchar(512),
#     color       varchar(7),
#     user_id     int,
#     PRIMARY KEY (card_id)
# );
#
#
# INSERT INTO users VALUES ('1','patrick', '$2a$10$cTUErxQqYVyU2qmQGIktpup5chLEdhD2zpzNEyYqmxrHHJbSNDOG');
# INSERT INTO user_roles VALUES ('1','ADMIN');
# COMMIT