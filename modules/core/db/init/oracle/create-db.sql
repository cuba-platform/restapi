create table SYS_ACCESS_TOKEN (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar2(255),
    TOKEN_BYTES blob,
    AUTHENTICATION_KEY varchar2(255),
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    USER_LOGIN varchar2(50),
    LOCALE varchar2(200),
    REFRESH_TOKEN_VALUE varchar2(255),
    --
    primary key (ID)
)^

create table SYS_REFRESH_TOKEN (
    ID varchar2(32) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar2(255),
    TOKEN_BYTES blob,
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    USER_LOGIN varchar2(50),
    --
    primary key (ID)
)^