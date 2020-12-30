
create table if not exists test
(
    id int auto_increment primary key
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

create table if not exists dto (
    id bigint primary key ,
    name varchar2(20)
);
truncate table dto;
insert into dto (id, name)
values (1, '11111'),
       (2, '22222'),
       (3, '33333'),
       (4, '44444'),
       (5, '55555');


select 1 from dual;

create table if not exists transaction(
    id int primary key auto_increment,
    name1 varchar2(100),
    name2 varchar2(100)
);
truncate table transaction;