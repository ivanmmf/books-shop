create table books (id int4 generated by default as identity, author varchar(255), iban varchar(255), name varchar(255), primary key (id));
create table user_books (id int4 generated by default as identity, book_id int4 not null, user_id int4, primary key (id));
create table users (id int4 generated by default as identity, email varchar(255), login varchar(255), password varchar(255), role varchar(255), primary key (id));
alter table if exists user_books add constraint FKa26s0po2ld5t5mi5sww9ca98 foreign key (book_id) references books;
alter table if exists user_books add constraint FKseruwi8quqdx6svnurcamej55 foreign key (user_id) references users;
