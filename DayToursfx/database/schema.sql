create table tours ( 
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    title varchar(224) UNIQUE NOT NULL,
    price varchar(20) NOT NULL,
    location varchar(64) NOT NULL,
    duration varchar(64) NOT NULL,
    difficulty varchar(64),
    description text
);

create table reviews(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    tourId references tours(id),
    name varchar(64) NOT NULL,
    email varchar(84),
    date Date 
    review text
);

create table bookings(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    Purchaser_name varchar(64),
    Purchaser_email varchar(84),
    tourId references tours(id),
    seats INTEGER
);

