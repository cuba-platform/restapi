-- begin REF_CURRENCY
create table REF_CURRENCY (
    CODE varchar(255),
    UUID uniqueidentifier,
    VERSION integer,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (CODE)
)^
-- end REF_CURRENCY
-- begin REF_SELLER
create table REF_SELLER (
    ID bigint,
    UUID uniqueidentifier,
    VERSION integer,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end REF_SELLER
-- begin REF_CAR_GARAGE_TOKEN
create table REF_CAR_GARAGE_TOKEN (
    ID uniqueidentifier,
    --
    TITLE varchar(50) not null,
    TOKEN varchar(20) not null,
    DESCRIPTION varchar(200),
    LAST_USAGE datetime2,
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR_GARAGE_TOKEN
-- begin REF_ALLOCATED_CAR
create table REF_ALLOCATED_CAR (
    ID uniqueidentifier,
    --
    VIN varchar(255),
    MODEL_ID uniqueidentifier,
    COLOUR_NAME varchar(255),
    DRIVER_NAME varchar(255),
    ALLOC_TS datetime2,
    --
    primary key nonclustered (ID)
)^
-- end REF_ALLOCATED_CAR
-- begin REF_CAR_TOKEN
create table REF_CAR_TOKEN (
    ID uniqueidentifier,
    --
    TOKEN varchar(255),
    REPAIR_ID uniqueidentifier,
    GARAGE_TOKEN_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR_TOKEN
-- begin REF_DRIVER_ALLOC
create table REF_DRIVER_ALLOC (
    ID uniqueidentifier,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    --
    DRIVER_ID uniqueidentifier,
    CAR_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
)^
-- end REF_DRIVER_ALLOC
-- begin REF_IK_ORDER_LINE_TAG
create table REF_IK_ORDER_LINE_TAG (
    ID bigint identity,
    --
    ORDER_LINE_ID bigint,
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end REF_IK_ORDER_LINE_TAG
-- begin REF_IK_ORDER_LINE
create table REF_IK_ORDER_LINE (
    ID bigint identity,
    --
    ORDER_ID bigint,
    PRODUCT varchar(255),
    --
    primary key (ID)
)^
-- end REF_IK_ORDER_LINE
-- begin REF_IK_CUSTOMER
create table REF_IK_CUSTOMER (
    ID bigint identity,
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end REF_IK_CUSTOMER
-- begin REF_IK_ORDER
create table REF_IK_ORDER (
    ID bigint identity,
    --
    ORDER_DATE datetime2,
    CUSTOMER_ID bigint,
    --
    primary key (ID)
)^
-- end REF_IK_ORDER
-- begin REF_CAR_DOCUMENTATION
create table REF_CAR_DOCUMENTATION (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    TITLE varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR_DOCUMENTATION
-- begin REF_MODEL
create table REF_MODEL (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    DTYPE varchar(100),
    --
    NAME varchar(255),
    MANUFACTURER varchar(255),
    NUMBER_OF_SEATS integer,
    --
    -- from ref$ExtModel
    LAUNCH_YEAR integer,
    --
    primary key nonclustered (ID)
)^
-- end REF_MODEL
-- begin REF_DRIVER_GROUP
create table REF_DRIVER_GROUP (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    IN_USE tinyint,
    --
    primary key nonclustered (ID)
)^
-- end REF_DRIVER_GROUP
-- begin REF_COLOUR
create table REF_COLOUR (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    DESCRIPTION varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_COLOUR
-- begin REF_PLACE
create table REF_PLACE (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_PLACE
-- begin DEBT_CASE
create table DEBT_CASE (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    TEST1 varchar(50),
    TEST2 varchar(50),
    TEST3 varchar(50),
    TEST4 varchar(50),
    TEST5 varchar(50),
    TEST6 varchar(50),
    TEST7 varchar(50),
    TEST8 varchar(50),
    TEST9 varchar(50),
    TEST10 varchar(50),
    DEBTOR_ID uniqueidentifier not null,
    --
    primary key nonclustered (ID)
)^
-- end DEBT_CASE
-- begin REF_CAR_DETAILS_ITEM
create table REF_CAR_DETAILS_ITEM (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    CAR_DETAILS_ID uniqueidentifier,
    INFO varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR_DETAILS_ITEM
-- begin REF_DRIVER
create table REF_DRIVER (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    DTYPE varchar(100),
    --
    COUNTRY varchar(255),
    STATE varchar(255),
    CITY varchar(255),
    STREET varchar(255),
    ZIP varchar(255),
    HOUSENUMBER varchar(255),
    FLATNUMBER varchar(255),
    LATITUDE double precision,
    LONGITUDE double precision,
    ADDRESS_SINCE datetime2,
    PLACE_ID uniqueidentifier,
    --
    NAME varchar(255),
    CALLSIGN_ID uniqueidentifier,
    DRIVER_GROUP_ID uniqueidentifier,
    STATUS integer,
    PLATFORM_ENTITY_ID uniqueidentifier,
    --
    -- from ref$ExtDriver
    INFO varchar(50),
    NOTES varchar(max),
    --
    primary key nonclustered (ID)
)^
-- end REF_DRIVER
-- begin REF_INSURANCE_CASE
create table REF_INSURANCE_CASE (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    CAR_ID uniqueidentifier,
    DESCRIPTION varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_INSURANCE_CASE
-- begin REF_SAMPLE_PLATFORM_ENTITY
create table REF_SAMPLE_PLATFORM_ENTITY (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_SAMPLE_PLATFORM_ENTITY
-- begin DEBT_DEBTOR
create table DEBT_DEBTOR (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    TITLE varchar(50) not null,
    --
    primary key nonclustered (ID)
)^
-- end DEBT_DEBTOR
-- begin REF_REPAIR
create table REF_REPAIR (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    CAR_ID uniqueidentifier,
    INSURANCE_CASE_ID uniqueidentifier,
    DESCRIPTION varchar(255),
    REPAIR_DATE datetime2,
    DB1_CUSTOMER_ID bigint,
    --
    primary key nonclustered (ID)
)^
-- end REF_REPAIR
-- begin REF_PRICING_REGION
create table REF_PRICING_REGION (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    NAME varchar(50) not null,
    PARENT_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
)^
-- end REF_PRICING_REGION
-- begin REF_PLANT
create table REF_PLANT (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    DTYPE varchar(100),
    --
    NAME varchar(255),
    DOC_ID uniqueidentifier,
    --
    -- from ref$CustomExtPlant
    ADDITIONAL_INFO varchar(255),
    --
    -- from ref$ExtPlant
    NUMBER_IN_1C varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_PLANT
-- begin REF_DRIVER_LICENSE
create table REF_DRIVER_LICENSE (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    DRIVER_ID uniqueidentifier not null,
    CAR_ID uniqueidentifier not null,
    PRIORITY integer not null,
    --
    primary key nonclustered (ID)
)^
-- end REF_DRIVER_LICENSE
-- begin REF_DRIVER_CALLSIGN
create table REF_DRIVER_CALLSIGN (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    CALLSIGN varchar(50),
    --
    primary key nonclustered (ID)
)^
-- end REF_DRIVER_CALLSIGN
-- begin REF_CAR_DETAILS
create table REF_CAR_DETAILS (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    CAR_ID uniqueidentifier,
    DETAILS varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR_DETAILS
-- begin REF_CARD
create table REF_CARD (
    ID uniqueidentifier,
    CATEGORY_ID uniqueidentifier,
    VERSION integer,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    CARD_TYPE integer,
    --
    STATE varchar(255),
    DESCRIPTION varchar(1000),
    CREATOR_ID uniqueidentifier,
    SUBSTITUTED_CREATOR_ID uniqueidentifier,
    PARENT_CARD_ID uniqueidentifier,
    HAS_ATTACHMENTS tinyint,
    HAS_ATTRIBUTES tinyint,
    PARENT_CARD_ACCESS tinyint,
    --
    primary key nonclustered (ID)
)^
-- end REF_CARD
-- begin REF_CAR
create table REF_CAR (
    ID uniqueidentifier,
    CATEGORY_ID uniqueidentifier,
    VERSION integer,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    --
    VIN varchar(255),
    COLOUR_ID uniqueidentifier,
    MODEL_ID uniqueidentifier,
    CAR_DOCUMENTATION_ID uniqueidentifier,
    TOKEN_ID uniqueidentifier,
    SELLER_ID bigint,
    CURRENCY_CODE varchar(255),
    --
    primary key nonclustered (ID)
)^
-- end REF_CAR
-- begin REF_DOC
create table REF_DOC (
    CARD_ID uniqueidentifier,
    --
    DOC_NUMBER varchar(50),
    AMOUNT decimal(19, 2),
    --
    primary key nonclustered (CARD_ID)
)^
-- end REF_DOC
-- begin REF_PLANT_MODEL_LINK
create table REF_PLANT_MODEL_LINK (
    PLANT_ID uniqueidentifier,
    MODEL_ID uniqueidentifier,
    primary key (PLANT_ID, MODEL_ID)
)^
-- end REF_PLANT_MODEL_LINK
