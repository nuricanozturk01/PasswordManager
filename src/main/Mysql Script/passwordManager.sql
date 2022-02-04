DROP DATABASE IF EXISTS passwordmanager;
CREATE DATABASE passwordmanager;
use passwordmanager;
CREATE TABLE User 
(
  userID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(80) NOT NULL,
  surname varchar(80) NOT NULL,
  username varchar(80) NOT NULL UNIQUE KEY,
  password char(128) NOT NULL,
  email varchar(255) NOT NULL UNIQUE KEY
);
CREATE TABLE App 
(
  appID int NOT NULL AUTO_INCREMENT,
  appName varchar(80) NOT NULL,
  appUsername varchar(80) NOT NULL,
  AppPassword varchar(128) NOT NULL,
  PRIMARY KEY (appID)
) ;

CREATE TABLE Log 
(
  logID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  date datetime NOT NULL,
  verify tinyint NOT NULL,
  userID int NOT NULL,

  CONSTRAINT FOREIGN KEY (userID) REFERENCES User (userID) ON DELETE CASCADE ON UPDATE CASCADE
);




CREATE TABLE UserApp (
  userID int NOT NULL,
  appID int NOT NULL,
  KEY userID (userID),
  KEY appID (appID),
  CONSTRAINT  FOREIGN KEY (userID) REFERENCES User (userID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT  FOREIGN KEY (appID) REFERENCES App (appID)    ON DELETE CASCADE ON UPDATE CASCADE
) ;



DELIMITER ;;
CREATE PROCEDURE addLog(in uID INT, in isValid TINYINT)
BEGIN
	INSERT INTO Log(date,verify,userID) VALUES (NOW(),isValid,uID);
END ;;
DELIMITER ;

DELIMITER ;;
CREATE  PROCEDURE deleteApp(in uID INT, in aID INT)
BEGIN
	SET SQL_SAFE_UPDATES = 0;
	
   -- Delete UserApp, App FROM App, UserApp, User WHERE UserApp.appID = App.appID AND UserApp.userID = uID AND App.appName = aName;
	DELETE UserApp FROM App,UserApp WHERE userApp.appID = aID AND userApp.userID = uID;
    DELETE App FROM App  WHERE App.appID = aID;
    SET @num := 0;
	UPDATE App SET appID = @num := (@num + 1);
	ALTER TABLE App AUTO_INCREMENT = 1;
    
	SET SQL_SAFE_UPDATES = 1;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE  PROCEDURE getAppCount()
BEGIN
	SELECT MAX(appID) FROM App;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE  PROCEDURE getList(in uname VARCHAR(80), in upass VARCHAR(128))
BEGIN
	SELECT a.appID, a.appName,a.appUsername,a.appPassword FROM App a
		INNER JOIN UserApp AS ua ON a.appID = ua.appID 
		INNER JOIN User AS user ON user.userID = ua.userID WHERE user.username = uname AND 
			user.password = upass;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE PROCEDURE getLog(in uID INT)
BEGIN
	SELECT log.date, log.verify FROM Log log, User user WHERE log.userID = uID AND user.userID = uID;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE PROCEDURE insertApp(in name VARCHAR(45), in appuname VARCHAR(80), in password VARCHAR(128))
BEGIN
	INSERT INTO App(appName,appUsername,appPassword) VALUES(name,appuname,password);
END ;;
DELIMITER ;

DELIMITER ;;
CREATE PROCEDURE insertUser(in name VARCHAR(65), in surname VARCHAR(65), in username VARCHAR(80),in password VARCHAR(128),in email VARCHAR(255))
BEGIN
	INSERT INTO User(name,surname,username,password,email) VALUES(name,surname,username,password,email);
END ;;
DELIMITER ;

DELIMITER ;;
CREATE  PROCEDURE insertUserApp(in userIDd INT, in appIDd INT)
BEGIN
	INSERT INTO UserApp(userID,appID) VALUES (userIDd,appIDd);
END ;;
DELIMITER ;

DELIMITER ;;
CREATE PROCEDURE updateApp(in aName VARCHAR(80), in auname VARCHAR(80), in apass VARCHAR(128), in aID INT)
BEGIN
	
    SET @var1 := null; 
    SET @var2 := null; 
    SET @var3 := null; 
    
    SELECT appName, appUsername, appPassword INTO @var1,@var2,@var3 FROM App WHERE appID = aID;
    SET SQL_SAFE_UPDATES = 0;
    IF @var1 <> aName THEN 
		UPDATE App SET appName = aName WHERE appID = aID;
    END IF;
    IF @var2 <> auname THEN
		UPDATE App SET appUsername = auname WHERE appID = aID;
    END IF;
    IF @var3 <> apass THEN
		UPDATE App SET appPassword = apass WHERE appID = aID;
    END IF;
    
	SET SQL_SAFE_UPDATES = 1;
    
END ;;
DELIMITER ;

DELIMITER $
CREATE PROCEDURE deleteLog(in uID INT)
BEGIN
	SET @var := (DATEDIFF(NOW(),(SELECT date From LOG WHERE userID = uID order by logID desc LIMIT 1)));
	
	IF @var = 15 THEN
		SET SQL_SAFE_UPDATES = 0;
		DELETE Log FROM LOG WHERE userID = uID;
        SET SQL_SAFE_UPDATES = 1;
    END IF;    
		
END $
DELIMITER ;




DELIMITER $
CREATE PROCEDURE  updatePassword(in uID INT, in pass char(128))
BEGIN
	SET SQL_SAFE_UPDATES = 0;
    UPDATE User SET password = pass WHERE userID = uID;
	SET SQL_SAFE_UPDATES = 1;
END $
DELIMITER ;




