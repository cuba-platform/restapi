create table RESTAPI_ACCESS_TOKEN (
    ID uuid,
    --
    CREATE_TS timestamp,
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES bytea,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES bytea,
    EXPIRY timestamp,
    USER_LOGIN varchar(255),
    LOCALE varchar(255),
    REFRESH_TOKEN_VALUE varchar(255),
    --
    primary key (ID)
)^

create table RESTAPI_REFRESH_TOKEN (
    ID uuid,
    --
    CREATE_TS timestamp,
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES bytea,
    AUTHENTICATION_BYTES bytea,
    EXPIRY timestamp,
    USER_LOGIN varchar(255),
    --
    primary key (ID)
)^
