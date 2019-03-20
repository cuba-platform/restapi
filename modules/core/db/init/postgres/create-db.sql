create table SYS_ACCESS_TOKEN (
    ID uuid not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES bytea,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES bytea,
    EXPIRY timestamp,
    USER_LOGIN varchar(50),
    LOCALE varchar(200),
    REFRESH_TOKEN_VALUE varchar(255),
    --
    primary key (ID)
)^

create table SYS_REFRESH_TOKEN (
    ID uuid not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES bytea,
    AUTHENTICATION_BYTES bytea,
    EXPIRY timestamp,
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^
