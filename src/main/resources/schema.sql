create table if not exists organization (
    id          bigint primary key auto_increment,
    name        varchar(60) not null,
    fullname    varchar(255) not null,
    inn         varchar(12) not null,
    kpp         varchar(9) not null,
    address     varchar(255) not null,
    phone       varchar(15),
    active      boolean not null default true,
    version    integer not null,
    constraint uk_inn unique (inn)
);
comment on table organization is 'Организация';

create table if not exists office (
    id          bigint primary key auto_increment,
    name        varchar(50) not null,
    address     varchar(255) not null,
    phone       varchar(15),
    active      boolean not null default true,
    org_id      bigint not null,
    version     integer not null,
    foreign key (org_id) references organization(id)
);
comment on table office is 'Офис';

create table if not exists document (
    code        smallint not null,
    name        varchar(50) not null,
    version     integer not null
);
comment on table document is 'Документ';

create table if not exists country (
    code        smallint primary key,
    name        varchar(70) not null,
    version     integer not null,
);
comment on table country is 'Страна';

create table if not exists person (
    id          bigint primary key auto_increment,
    first_name   varchar(50) not null,
    secon_dname  varchar(50),
    middle_name  varchar(50),
    position    varchar(50) not null,
    phone       varchar(15),
    doc_date    date,
    doc_number  varchar(35),
    identified  boolean,
    off_id      bigint,
    doc_id      tinyint,
    country_id  smallint,
    version     integer not null,
    foreign key (off_id) references office(id),
    foreign key (doc_id) references document(code),
    foreign key (country_id) references country(code)
);
comment on table person is 'Пользователи';
