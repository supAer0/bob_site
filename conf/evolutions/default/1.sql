# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table attribute (
  id                        varchar(40) not null,
  name                      varchar(255),
  group_root_atr_id         varchar(40),
  constraint pk_attribute primary key (id))
;

create table group_product (
  id                        varchar(40) not null,
  name                      varchar(255),
  transliteration_name      varchar(255),
  group_root_id             varchar(40),
  constraint pk_group_product primary key (id))
;

create table group_root (
  id                        varchar(40) not null,
  name                      varchar(255),
  transliteration_name      varchar(255),
  constraint pk_group_root primary key (id))
;

create table product (
  id                        varchar(40) not null,
  name                      varchar(255),
  transliteration_name      varchar(255),
  description               varchar(255),
  cost                      double,
  amount                    double,
  image                     varchar(255),
  group_product_id          varchar(40),
  constraint pk_product primary key (id))
;

create table product_value (
  id                        varchar(40),
  attribute_id              varchar(40),
  name                      varchar(255),
  value                     varchar(255),
  product_id                varchar(40))
;

create table user (
  email                     varchar(255) not null,
  is_admin                  boolean,
  password_hash             varchar(255),
  salt                      varchar(255),
  constraint pk_user primary key (email))
;

create sequence user_seq;

alter table attribute add constraint fk_attribute_groupRootAtr_1 foreign key (group_root_atr_id) references group_root (id) on delete restrict on update restrict;
create index ix_attribute_groupRootAtr_1 on attribute (group_root_atr_id);
alter table group_product add constraint fk_group_product_groupRoot_2 foreign key (group_root_id) references group_root (id) on delete restrict on update restrict;
create index ix_group_product_groupRoot_2 on group_product (group_root_id);
alter table product add constraint fk_product_groupProduct_3 foreign key (group_product_id) references group_product (id) on delete restrict on update restrict;
create index ix_product_groupProduct_3 on product (group_product_id);
alter table product_value add constraint fk_product_value_attribute_4 foreign key (attribute_id) references attribute (id) on delete restrict on update restrict;
create index ix_product_value_attribute_4 on product_value (attribute_id);
alter table product_value add constraint fk_product_value_product_5 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_product_value_product_5 on product_value (product_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists attribute;

drop table if exists group_product;

drop table if exists group_root;

drop table if exists product;

drop table if exists product_value;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_seq;

