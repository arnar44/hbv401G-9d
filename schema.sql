create table tours ( 
    id SERIAL PRIMARY KEY,
    title varchar(64) UNIQUE NOT NULL,
    location varchar(64) NOT NULL,
    duration varchar(64) NOT NULL,
    difficulty varchar(64),
    iternirary test
);

create table reviews(
    id SERIAL PRIMARY KEY,
    tourId references tours(id),
    name varchar(64) NOT NULL,
    email varchar(84),
    date Date,
    review text
);

create table bookings(
    id SERIAL PRIMARY KEY,
    Purchaser_name varchar(64),
    Purchaser_email varchar(84),
    tourId references tours(id),
    seats INTEGER
);

