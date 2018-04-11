create table tours ( 
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    title varchar(224) NOT NULL,
    price INTEGER NOT NULL,
    category varchar(64) NOT NULL,
    duration varchar(64) NOT NULL,
    level varchar(64),
    departures varchar(120),
    meet varchar(4),
    pickup varchar(5),
    availability varchar(64),
    description text
);

create table reviews(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    tourId references tours(id),
    name varchar(64) NOT NULL,
    email varchar(84),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    review text,
	accepted INTEGER NOT NULL
);

create table bookings(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    Purchaser_name varchar(64),
    Purchaser_email varchar(84),
    tourId references tours(id),
    seats INTEGER
);

create table users(
    username varchar(64) PRIMARY KEY,
    password varchar(64) NOT NULL
);