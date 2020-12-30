create table if not exists test
(
    id bigint
        constraint test_pk
            primary key
);
truncate table test;
insert into test (id)
values (1),
       (2),
       (3),
       (4),
       (5),
       (6),
       (7),
       (8),
       (9),
       (10);

create table if not exists dto
(
    id   bigint constraint dto_pk primary key,
    name varchar(20)
);
truncate table dto;
insert into dto (id, name)
values (1, '11111'),
       (2, '22222'),
       (3, '33333'),
       (4, '44444'),
       (5, '55555');


select 1;

create table if not exists transaction
(
    id    bigint
        constraint transaction_pk primary key,
    name1 varchar(100),
    name2 varchar(100)
);
truncate table transaction;