drop schema public cascade^

-- table for selecting sequence values in HSQL
create table DUAL (ID integer)^
insert into DUAL (ID) values (0)^

create table customer (
    id bigint not null primary key,
    name varchar(100)
)^

create sequence customer_sequence start with 1 increment by 1^

create table order_ (
    id bigint not null primary key,
    order_date varchar(100),
    customer_id bigint,
    mem_cust_id varchar(36),
    ik_cust_id bigint,
    ik_order_id bigint,
    ck_customer_tenant_id integer,
    ck_customer_entity_id bigint,
    constraint fk_order_customer foreign key (customer_id) references customer
)^

create sequence order_sequence start with 1 increment by 1^

create table foo (
    id bigint not null primary key,
    name varchar(100)
)^
