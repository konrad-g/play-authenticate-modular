# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table linked_accounts (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_accounts primary key (id))
;

create table security_roles (
  id                        bigint auto_increment not null,
  role_name                 varchar(255),
  constraint pk_security_roles primary key (id))
;

create table token_actions (
  id                        bigint auto_increment not null,
  token                     varchar(255),
  target_user_id            bigint,
  type                      varchar(2),
  created                   datetime(6),
  expires                   datetime(6),
  constraint ck_token_actions_type check (type in ('PR','EV')),
  constraint uq_token_actions_token unique (token),
  constraint pk_token_actions primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  last_login                datetime(6),
  active                    tinyint(1) default 0,
  email_validated           tinyint(1) default 0,
  constraint pk_users primary key (id))
;

create table user_permissions (
  id                        bigint auto_increment not null,
  value                     varchar(255),
  constraint pk_user_permissions primary key (id))
;


create table users_security_roles (
  users_id                       bigint not null,
  security_roles_id              bigint not null,
  constraint pk_users_security_roles primary key (users_id, security_roles_id))
;

create table users_user_permissions (
  users_id                       bigint not null,
  user_permissions_id            bigint not null,
  constraint pk_users_user_permissions primary key (users_id, user_permissions_id))
;
alter table linked_accounts add constraint fk_linked_accounts_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_accounts_user_1 on linked_accounts (user_id);
alter table token_actions add constraint fk_token_actions_targetUser_2 foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_actions_targetUser_2 on token_actions (target_user_id);



alter table users_security_roles add constraint fk_users_security_roles_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_security_roles add constraint fk_users_security_roles_security_roles_02 foreign key (security_roles_id) references security_roles (id) on delete restrict on update restrict;

alter table users_user_permissions add constraint fk_users_user_permissions_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_user_permissions add constraint fk_users_user_permissions_user_permissions_02 foreign key (user_permissions_id) references user_permissions (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table linked_accounts;

drop table security_roles;

drop table token_actions;

drop table users;

drop table users_security_roles;

drop table users_user_permissions;

drop table user_permissions;

SET FOREIGN_KEY_CHECKS=1;

