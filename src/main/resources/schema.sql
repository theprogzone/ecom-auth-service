create table TB_USER (
  ID int not null AUTO_INCREMENT,
  EMAIL varchar(100) not null,
  PASSWORD varchar(100) not null,
  PRIMARY KEY ( ID )
);

create table TB_ROLE (
  ID int not null AUTO_INCREMENT,
  CODE varchar(100) not null,
  PRIMARY KEY ( ID )
);

create table TB_USER_ROLE (
  ID int not null AUTO_INCREMENT,
  USER_ID int not null,
  ROLE_ID int not null,
  PRIMARY KEY ( ID )
);
