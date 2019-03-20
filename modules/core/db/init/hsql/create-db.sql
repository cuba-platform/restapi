create table SYS_ACCESS_TOKEN (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES longvarbinary,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES longvarbinary,
    EXPIRY timestamp,
    USER_LOGIN varchar(50),
    LOCALE varchar(200),
    REFRESH_TOKEN_VALUE varchar(255),
    --
    primary key (ID)
)^

create table SYS_REFRESH_TOKEN (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES longvarbinary,
    AUTHENTICATION_BYTES longvarbinary,
    EXPIRY timestamp,
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^