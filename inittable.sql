drop database if exists network;
create database network;
use network;

CREATE TABLE client_list (
    client_id VARCHAR(10),
    client_password VARCHAR(64),
    client_name VARCHAR(11),
    client_email VARCHAR(20),
    client_phone VARCHAR(13),
    client_nick VARCHAR(20),
    client_birth DATE,
    salt VARCHAR(40),
    PRIMARY KEY (client_id)
);


CREATE TABLE login_check (
    client_id VARCHAR(10),
    log VARCHAR(7),
    FOREIGN KEY (client_id)
        REFERENCES client_list (client_id)
);

CREATE TABLE client_friend_list (
    client_id VARCHAR(10),
    friend_id VARCHAR(10),
    FOREIGN KEY (client_id)
        REFERENCES client_list (client_id)
);