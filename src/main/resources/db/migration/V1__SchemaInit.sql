-- прописываем как заполняется последовательность Id
create sequence book_id_seq start with 1 increment by 50;
create sequence user_id_seq start with 1 increment by 50;

/*! SET storage_engine=INNODB */;
create table books
(
    id    int DEFAULT nextval('book_id_seq') not null,
    name  varchar(255)                              not null,
    author varchar(255)                              not null,
    iban varchar(255)                              not null,
    primary key (id)
);

create table users
(
    id          int  DEFAULT nextval('user_id_seq') not null,
    login        varchar(255)                               not null,
    password varchar(255)                                   not null,
    email       varchar(255)                                     ,
    role        varchar(255)                  not null,
    primary key (id)
);

create table user_books
(
    id          int  DEFAULT nextval('user_books_id_seq') not null,
    book_id        int                              not null,
    user_id        int                                   not null,
    primary key (id)
);