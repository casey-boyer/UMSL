DROP DATABASE IF EXISTS dvd_db;

CREATE DATABASE dvd_db;

USE dvd_db;

CREATE TABLE User (
  UserID INT NOT NULL AUTO_INCREMENT,
  Email VARCHAR(50),
  FirstName VARCHAR(50),
  LastName VARCHAR(50),
  Password VARCHAR(50),
  
  PRIMARY KEY(UserID) 
);

INSERT INTO User 
  (Email, FirstName, LastName, Password)
VALUES 
  ('bat@gmail.com', 'Bat', 'Man', 'bat'),
  ('spider@gmail.com', 'Spider', 'Man', 'spider'), 
  ('super@gmail.com', 'Super', 'Man', 'super');
  
CREATE TABLE ProductList (
  pid INT NOT NULL AUTO_INCREMENT,
  Title VARCHAR(50),
  Cover VARCHAR(500),
  Price DOUBLE NOT NULL,
  
  PRIMARY KEY(pid) 
);

INSERT INTO ProductList 
  (pid, Title, Cover, Price)
VALUES 
  (1, 'Death Proof', 'http://img.moviepostershop.com/death-proof-movie-poster-2007-1020403304.jpg', 19.99),
  (2, '2001: A Space Odyssey', 'https://images-na.ssl-images-amazon.com/images/I/51GXZ-j2x0L._SY450_.jpg', 19.99), 
  (3, 'Pom Poko', 'https://static.zerochan.net/Heisei.Tanuki.Gassen.Pom.Poko.full.69629.jpg', 25.99),
  (4, 'Eagle Vs. Shark', 'https://img00.deviantart.net/04a0/i/2007/156/e/4/eagle_vs_shark_2_by_drawie.jpg', 16.99);

 
 -- Create student and grant privileges

DELIMITER //
CREATE PROCEDURE drop_user_if_exists()
BEGIN
    DECLARE userCount BIGINT DEFAULT 0 ;

    SELECT COUNT(*) INTO userCount FROM mysql.user
    WHERE User = 'student' and  Host = 'localhost';

    IF userCount > 0 THEN
        DROP USER student@localhost;
    END IF;
END ; //
DELIMITER ;

CALL drop_user_if_exists() ;

CREATE USER student@localhost IDENTIFIED BY 'sesame';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON dvd_db.*
TO student@localhost;

  
USE dvd_db;