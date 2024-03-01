-- TODO Task 3
drop database if exists ecommerce;
create database ecommerce;
use ecommerce;

create table orders (
	orderId varchar(128),
    date date,
    name varchar(128),
    address varchar(128),
    priority boolean,
    comments varchar(128),
    primary key (orderId)
);

create table line_items (
	line_items_id int auto_increment,
	orderId varchar(128),
	productId varchar(128),
    name varchar(256),
    quantity int,
    price float,
    primary key(line_items_id), foreign key (orderId) references orders(orderId)
);