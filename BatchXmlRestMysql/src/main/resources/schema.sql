drop table if exists manager;

CREATE TABLE manager  (
    manager_id INT auto_increment NOT NULL PRIMARY KEY,
    firstname VARCHAR(60),
    lastname VARCHAR(60),
    age INT
);


drop table if exists employee;

CREATE TABLE employee  (
    employee_id INT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(60),
    city VARCHAR(60),
    country VARCHAR(60)
);
