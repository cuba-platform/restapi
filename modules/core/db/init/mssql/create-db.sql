create table SYS_ACCESS_TOKEN (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES image,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES image,
    EXPIRY datetime,
    USER_LOGIN varchar(50),
    LOCALE varchar(200),
    REFRESH_TOKEN_VALUE varchar(255),
    --
    primary key (ID)
)^

create table SYS_REFRESH_TOKEN (
    ID uniqueidentifier not null,
    CREATE_TS datetime,
    --
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES image,
    AUTHENTICATION_BYTES image,
    EXPIRY datetime,
    USER_LOGIN varchar(50),
    --
    primary key (ID)
)^