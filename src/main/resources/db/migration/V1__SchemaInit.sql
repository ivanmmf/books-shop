-- прописываем как заполняется последовательность Id
create sequence book_id_seq start with 1 increment by 50;
create sequence user_id_seq start with 1 increment by 50;

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
    email       numeric                                    not null,
    role        boolean default false                       not null,
    primary key (id)
);