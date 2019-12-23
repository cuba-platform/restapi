-- begin REF_CAR
alter table REF_CAR add constraint FK_REF_CAR_ON_COLOUR foreign key (COLOUR_ID) references REF_COLOUR(ID)^
alter table REF_CAR add constraint FK_REF_CAR_ON_MODEL foreign key (MODEL_ID) references REF_MODEL(ID)^
alter table REF_CAR add constraint FK_REF_CAR_ON_CAR_DOCUMENTATION foreign key (CAR_DOCUMENTATION_ID) references REF_CAR_DOCUMENTATION(ID)^
alter table REF_CAR add constraint FK_REF_CAR_ON_TOKEN foreign key (TOKEN_ID) references REF_CAR_TOKEN(ID)^
alter table REF_CAR add constraint FK_REF_CAR_ON_SELLER foreign key (SELLER_ID) references REF_SELLER(ID)^
alter table REF_CAR add constraint FK_REF_CAR_ON_CURRENCY_CODE foreign key (CURRENCY_CODE) references REF_CURRENCY(CODE)^
alter table REF_CAR add constraint FK_REF_CAR_ON_CATEGORY foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)^
create index IDX_REF_CAR_ON_COLOUR on REF_CAR (COLOUR_ID)^
create index IDX_REF_CAR_ON_MODEL on REF_CAR (MODEL_ID)^
create index IDX_REF_CAR_ON_CAR_DOCUMENTATION on REF_CAR (CAR_DOCUMENTATION_ID)^
create index IDX_REF_CAR_ON_TOKEN on REF_CAR (TOKEN_ID)^
create index IDX_REF_CAR_ON_SELLER on REF_CAR (SELLER_ID)^
create index IDX_REF_CAR_ON_CURRENCY_CODE on REF_CAR (CURRENCY_CODE)^
create index IDX_REF_CAR_ON_CATEGORY on REF_CAR (CATEGORY_ID)^
-- end REF_CAR
-- begin REF_DOC
alter table REF_DOC add constraint FK_REF_DOC_ON_CARD foreign key (CARD_ID) references REF_CARD(ID) on delete CASCADE^
-- end REF_DOC
-- begin REF_IK_ORDER
alter table REF_IK_ORDER add constraint FK_REF_IK_ORDER_ON_CUSTOMER foreign key (CUSTOMER_ID) references REF_IK_CUSTOMER(ID)^
create index IDX_REF_IK_ORDER_ON_CUSTOMER on REF_IK_ORDER (CUSTOMER_ID)^
-- end REF_IK_ORDER
-- begin REF_IK_ORDER_LINE
alter table REF_IK_ORDER_LINE add constraint FK_REF_IK_ORDER_LINE_ON_ORDER foreign key (ORDER_ID) references REF_IK_ORDER(ID)^
create index IDX_REF_IK_ORDER_LINE_ON_ORDER on REF_IK_ORDER_LINE (ORDER_ID)^
-- end REF_IK_ORDER_LINE
-- begin REF_IK_ORDER_LINE_TAG
alter table REF_IK_ORDER_LINE_TAG add constraint FK_REF_IK_ORDER_LINE_TAG_ON_ORDER_LINE foreign key (ORDER_LINE_ID) references REF_IK_ORDER_LINE(ID)^
create index IDX_REF_IK_ORDER_LINE_TAG_ON_ORDER_LINE on REF_IK_ORDER_LINE_TAG (ORDER_LINE_ID)^
-- end REF_IK_ORDER_LINE_TAG
-- begin REF_CARD
alter table REF_CARD add constraint FK_REF_CARD_ON_CREATOR foreign key (CREATOR_ID) references SEC_USER(ID)^
alter table REF_CARD add constraint FK_REF_CARD_ON_SUBSTITUTED_CREATOR foreign key (SUBSTITUTED_CREATOR_ID) references SEC_USER(ID)^
alter table REF_CARD add constraint FK_REF_CARD_ON_PARENT_CARD foreign key (PARENT_CARD_ID) references REF_CARD(ID)^
alter table REF_CARD add constraint FK_REF_CARD_ON_CATEGORY foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)^
create index IDX_REF_CARD_ON_CREATOR on REF_CARD (CREATOR_ID)^
create index IDX_REF_CARD_ON_SUBSTITUTED_CREATOR on REF_CARD (SUBSTITUTED_CREATOR_ID)^
create index IDX_REF_CARD_ON_PARENT_CARD on REF_CARD (PARENT_CARD_ID)^
create index IDX_REF_CARD_ON_CATEGORY on REF_CARD (CATEGORY_ID)^
-- end REF_CARD
-- begin REF_PLANT
alter table REF_PLANT add constraint FK_REF_PLANT_ON_DOC foreign key (DOC_ID) references REF_DOC(CARD_ID)^
create index IDX_REF_PLANT_ON_DOC on REF_PLANT (DOC_ID)^
-- end REF_PLANT
-- begin REF_DRIVER
alter table REF_DRIVER add constraint FK_REF_DRIVER_ON_CALLSIGN foreign key (CALLSIGN_ID) references REF_DRIVER_CALLSIGN(ID)^
alter table REF_DRIVER add constraint FK_REF_DRIVER_ON_PLACE foreign key (PLACE_ID) references REF_PLACE(ID)^
alter table REF_DRIVER add constraint FK_REF_DRIVER_ON_DRIVER_GROUP foreign key (DRIVER_GROUP_ID) references REF_DRIVER_GROUP(ID)^
alter table REF_DRIVER add constraint FK_REF_DRIVER_ON_PLATFORM_ENTITY foreign key (PLATFORM_ENTITY_ID) references REF_SAMPLE_PLATFORM_ENTITY(ID)^
create index IDX_REF_DRIVER_ON_CALLSIGN on REF_DRIVER (CALLSIGN_ID)^
create index IDX_REF_DRIVER_ON_PLACE on REF_DRIVER (PLACE_ID)^
create index IDX_REF_DRIVER_ON_DRIVER_GROUP on REF_DRIVER (DRIVER_GROUP_ID)^
create index IDX_REF_DRIVER_ON_PLATFORM_ENTITY on REF_DRIVER (PLATFORM_ENTITY_ID)^
-- end REF_DRIVER
-- begin REF_REPAIR
alter table REF_REPAIR add constraint FK_REF_REPAIR_ON_CAR foreign key (CAR_ID) references REF_CAR(ID)^
alter table REF_REPAIR add constraint FK_REF_REPAIR_ON_INSURANCE_CASE foreign key (INSURANCE_CASE_ID) references REF_INSURANCE_CASE(ID)^
create index IDX_REF_REPAIR_ON_CAR on REF_REPAIR (CAR_ID)^
create index IDX_REF_REPAIR_ON_INSURANCE_CASE on REF_REPAIR (INSURANCE_CASE_ID)^
-- end REF_REPAIR
-- begin REF_CAR_TOKEN
alter table REF_CAR_TOKEN add constraint FK_REF_CAR_TOKEN_ON_REPAIR foreign key (REPAIR_ID) references REF_REPAIR(ID)^
alter table REF_CAR_TOKEN add constraint FK_REF_CAR_TOKEN_ON_GARAGE_TOKEN foreign key (GARAGE_TOKEN_ID) references REF_CAR_GARAGE_TOKEN(ID)^
create index IDX_REF_CAR_TOKEN_ON_REPAIR on REF_CAR_TOKEN (REPAIR_ID)^
create index IDX_REF_CAR_TOKEN_ON_GARAGE_TOKEN on REF_CAR_TOKEN (GARAGE_TOKEN_ID)^
-- end REF_CAR_TOKEN
-- begin REF_CAR_DETAILS
alter table REF_CAR_DETAILS add constraint FK_REF_CAR_DETAILS_ON_CAR foreign key (CAR_ID) references REF_CAR(ID)^
create index IDX_REF_CAR_DETAILS_ON_CAR on REF_CAR_DETAILS (CAR_ID)^
-- end REF_CAR_DETAILS
-- begin REF_DRIVER_LICENSE
alter table REF_DRIVER_LICENSE add constraint FK_REF_DRIVER_LICENSE_ON_DRIVER foreign key (DRIVER_ID) references REF_DRIVER(ID)^
alter table REF_DRIVER_LICENSE add constraint FK_REF_DRIVER_LICENSE_ON_CAR foreign key (CAR_ID) references REF_CAR(ID)^
create index IDX_REF_DRIVER_LICENSE_ON_DRIVER on REF_DRIVER_LICENSE (DRIVER_ID)^
create index IDX_REF_DRIVER_LICENSE_ON_CAR on REF_DRIVER_LICENSE (CAR_ID)^
-- end REF_DRIVER_LICENSE
-- begin REF_INSURANCE_CASE
alter table REF_INSURANCE_CASE add constraint FK_REF_INSURANCE_CASE_ON_CAR foreign key (CAR_ID) references REF_CAR(ID)^
create index IDX_REF_INSURANCE_CASE_ON_CAR on REF_INSURANCE_CASE (CAR_ID)^
-- end REF_INSURANCE_CASE
-- begin REF_CAR_DETAILS_ITEM
alter table REF_CAR_DETAILS_ITEM add constraint FK_REF_CAR_DETAILS_ITEM_ON_CAR_DETAILS foreign key (CAR_DETAILS_ID) references REF_CAR_DETAILS(ID)^
create index IDX_REF_CAR_DETAILS_ITEM_ON_CAR_DETAILS on REF_CAR_DETAILS_ITEM (CAR_DETAILS_ID)^
-- end REF_CAR_DETAILS_ITEM
-- begin REF_DRIVER_ALLOC
alter table REF_DRIVER_ALLOC add constraint FK_REF_DRIVER_ALLOC_ON_DRIVER foreign key (DRIVER_ID) references REF_DRIVER(ID)^
alter table REF_DRIVER_ALLOC add constraint FK_REF_DRIVER_ALLOC_ON_CAR foreign key (CAR_ID) references REF_CAR(ID)^
create index IDX_REF_DRIVER_ALLOC_ON_DRIVER on REF_DRIVER_ALLOC (DRIVER_ID)^
create index IDX_REF_DRIVER_ALLOC_ON_CAR on REF_DRIVER_ALLOC (CAR_ID)^
-- end REF_DRIVER_ALLOC
-- begin REF_PRICING_REGION
alter table REF_PRICING_REGION add constraint FK_REF_PRICING_REGION_ON_PARENT foreign key (PARENT_ID) references REF_PRICING_REGION(ID)^
create index IDX_REF_PRICING_REGION_ON_PARENT on REF_PRICING_REGION (PARENT_ID)^
-- end REF_PRICING_REGION
-- begin REF_PLANT_MODEL_LINK
alter table REF_PLANT_MODEL_LINK add constraint FK_REFPLAMOD_ON_PLANT foreign key (PLANT_ID) references REF_PLANT(ID)^
alter table REF_PLANT_MODEL_LINK add constraint FK_REFPLAMOD_ON_MODEL foreign key (MODEL_ID) references REF_MODEL(ID)^
-- end REF_PLANT_MODEL_LINK
-- begin DEBT_CASE
alter table DEBT_CASE add constraint FK_DEBT_CASE_ON_DEBTOR foreign key (DEBTOR_ID) references DEBT_DEBTOR(ID)^
create index IDX_DEBT_CASE_ON_DEBTOR on DEBT_CASE (DEBTOR_ID)^
-- end DEBT_CASE
