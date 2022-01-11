create table "auth_user_account"
(
    "id"                        CHAR(36) default RANDOM_UUID() not null
        primary key,
    "username"             VARCHAR(64) not null,
    "password"             VARCHAR(64) not null,
    "sub_sys_dict_code"             VARCHAR(32),
    "user_status_dict_code"         CHAR(2),
    "user_status_reason"        VARCHAR(127),
    "user_type_dict_code"           CHAR(2),
    "lock_time_start"         TIMESTAMP,
    "lock_time_end"           TIMESTAMP,
    "last_login_time"           TIMESTAMP,
    "last_logout_time"          TIMESTAMP,
    "last_login_ip"             CHAR(39),
    "last_login_terminal_dict_code" VARCHAR(15),
    "total_online_time"         REAL,
    "register_ip"               CHAR(39),
    "register_url"              VARCHAR(127),
    "dynamic_auth_key"          VARCHAR(64),
    "second_password"           VARCHAR(64),
    "remark"                    VARCHAR(127),
    "active"                 BOOLEAN  default TRUE          not null,
    "built_in"               BOOLEAN  default FALSE         not null,
    "create_user"               VARCHAR(36),
    "create_time"               TIMESTAMP  default now() not null,
    "update_user"               VARCHAR(36),
    "update_time"               TIMESTAMP,
    "owner_id" VARCHAR(36)
);

create unique index "uq_user_account__name_subsys_owner" on "auth_user_account" ("username", "sub_sys_dict_code", "owner_id");

comment on table "auth_user_account" is '用户账号';

comment on column "auth_user_account"."id" is '主键';

comment on column "auth_user_account"."username" is '用户名';

comment on column "auth_user_account"."password" is '密码';

comment on column "auth_user_account"."sub_sys_dict_code" is '子系统代码';

comment on column "auth_user_account"."user_status_dict_code" is '用户状态代码';

comment on column "auth_user_account"."user_status_reason" is '用户状态原因';

comment on column "auth_user_account"."user_type_dict_code" is '用户类型代码';

comment on column "auth_user_account"."lock_time_start" is '账号锁定时间起';

comment on column "auth_user_account"."lock_time_end" is '账号锁定时间止';

comment on column "auth_user_account"."last_login_time" is '最后一次登入时间';

comment on column "auth_user_account"."last_logout_time" is '最后一次登出时间';

comment on column "auth_user_account"."last_login_ip" is '最后一次登入ip(标准ipv6全格式)';

comment on column "auth_user_account"."last_login_terminal_dict_code" is '最后一次登入终端代码';

comment on column "auth_user_account"."total_online_time" is '总在线时长(小时)';

comment on column "auth_user_account"."register_ip" is '注册ip(标准ipv6全格式)';

comment on column "auth_user_account"."register_url" is '注册url';

comment on column "auth_user_account"."dynamic_auth_key" is '动态验证码的密钥';

comment on column "auth_user_account"."second_password" is '二级密码';

comment on column "auth_user_account"."remark" is '备注，或其国际化key';

comment on column "auth_user_account"."active" is '是否启用';

comment on column "auth_user_account"."built_in" is '是否内置';

comment on column "auth_user_account"."create_user" is '创建用户';

comment on column "auth_user_account"."create_time" is '创建时间';

comment on column "auth_user_account"."update_user" is '更新用户';

comment on column "auth_user_account"."update_time" is '更新时间';

COMMENT ON COLUMN "auth_user_account"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';


create table "auth_user_account_third_party"
(
    "id"                  CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_account_id"     CHAR(36)                       not null,
    "principal_type_dict_code" VARCHAR(15)                    not null,
    "principal"          VARCHAR(64)                    not null,
    "credentials"          VARCHAR(64),
    "sub_sys_dict_code"       VARCHAR(32),
    "owner_id"       VARCHAR(36),
    "remark"              VARCHAR(127),
    "active"           BOOLEAN  default TRUE          not null,
    "built_in"         BOOLEAN  default FALSE         not null,
    "create_user"         VARCHAR(36),
    "create_time"         TIMESTAMP  default now() not null,
    "update_user"         VARCHAR(36),
    "update_time"         TIMESTAMP,
    constraint "fk_user_account_auth"
        foreign key ("user_account_id") references "auth_user_account" ("id")
);

comment on table "auth_user_account_third_party" is '用户账号第三方授权信息';

comment on column "auth_user_account_third_party"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "auth_user_account_third_party"."principal_type_dict_code" is '身份类型代码';

comment on column "auth_user_account_third_party"."principal" is '唯一身份标识';

comment on column "auth_user_account_third_party"."credentials" is '凭证';

comment on column "auth_user_account_third_party"."sub_sys_dict_code" is '子系统代码';

comment on column "auth_user_account_third_party"."remark" is '备注，或其国际化key';

comment on column "auth_user_account_third_party"."active" is '是否启用';

comment on column "auth_user_account_third_party"."built_in" is '是否内置';

comment on column "auth_user_account_third_party"."create_user" is '创建用户';

comment on column "auth_user_account_third_party"."create_time" is '创建时间';

comment on column "auth_user_account_third_party"."update_user" is '更新用户';

comment on column "auth_user_account_third_party"."update_time" is '更新时间';
COMMENT ON COLUMN "auth_user_account_third_party"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create unique index "uq_u_a_a__principal_id_type_sub_sys_owner"
    on "auth_user_account_third_party" ("principal", "principal_type_dict_code", "sub_sys_dict_code", "owner_id");



create table "auth_persistent_logins"
(
    "id"                        CHAR(64) not null        primary key,
    "username"             VARCHAR(64) not null,
    "token"             VARCHAR(64),
    "last_used"               TIMESTAMP
);

comment on table "auth_persistent_logins" is '登陆持久化';

comment on column "auth_persistent_logins"."id" is '主键，series，登陆令牌散列，仅在用户使用密码重新登录时创建';

comment on column "auth_persistent_logins"."username" is '用户名';

comment on column "auth_persistent_logins"."token" is '自动登陆会话令牌，会在每一个新的session中都重新生成';

comment on column "auth_persistent_logins"."last_used" is '最后一次使用时间';




create table "auth_user_group"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "group_code"  VARCHAR(64)                    not null,
    "group_name"  VARCHAR(64)                    not null,
    "sub_sys_dict_code"       VARCHAR(32),
    "remark"      VARCHAR(127),
    "active"   BOOLEAN  default TRUE          not null,
    "built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP  default now() not null,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    "owner_id" VARCHAR(36)
);

comment on table "auth_user_group" is '用户组';

comment on column "auth_user_group"."id" is '主键';

comment on column "auth_user_group"."group_code" is '用户编码';

comment on column "auth_user_group"."group_name" is '用户组名';

comment on column "auth_user_group"."sub_sys_dict_code" is '子系统代码';

comment on column "auth_user_group"."remark" is '备注，或其国际化key';

comment on column "auth_user_group"."active" is '是否启用';

comment on column "auth_user_group"."built_in" is '是否内置';

comment on column "auth_user_group"."create_user" is '创建用户';

comment on column "auth_user_group"."create_time" is '创建时间';

comment on column "auth_user_group"."update_user" is '更新用户';

comment on column "auth_user_group"."update_time" is '更新时间';
COMMENT ON COLUMN "auth_user_group"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create table "auth_user_group_user"
(
    "id"       CHAR(36) default RANDOM_UUID() not null
        primary key,
    "group_id" CHAR(36)                       not null,
    "user_id"  CHAR(36)                       not null
);

comment on table "auth_user_group_user" is '用户组-用户关系';

comment on column "auth_user_group_user"."id" is '主键';

comment on column "auth_user_group_user"."group_id" is '用户组id';

comment on column "auth_user_group_user"."user_id" is '用户id';

create table "auth_role"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_code"   VARCHAR(64)                    not null,
    "role_name"   VARCHAR(64)                    not null,
    "sub_sys_dict_code"       VARCHAR(32),
    "remark"      VARCHAR(127),
    "active"   BOOLEAN  default TRUE          not null,
    "built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP  default now() not null,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    "owner_id" VARCHAR(36)
);

comment on table "auth_role" is '角色';

comment on column "auth_role"."id" is '主键';

comment on column "auth_role"."role_code" is '角色编码';

comment on column "auth_role"."role_name" is '角色名';

comment on column "auth_role"."sub_sys_dict_code" is '子系统代码';

comment on column "auth_role"."remark" is '备注，或其国际化key';

comment on column "auth_role"."active" is '是否启用';

comment on column "auth_role"."built_in" is '是否内置';

comment on column "auth_role"."create_user" is '创建用户';

comment on column "auth_role"."create_time" is '创建时间';

comment on column "auth_role"."update_user" is '更新用户';

comment on column "auth_role"."update_time" is '更新时间';
COMMENT ON COLUMN "auth_role"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create table "auth_role_user"
(
    "id"      CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id" CHAR(36)                       not null,
    "user_id" CHAR(36)                       not null
);

comment on table "auth_role_user" is '角色-用户关系';

comment on column "auth_role_user"."id" is '主键';

comment on column "auth_role_user"."role_id" is '角色id';

comment on column "auth_role_user"."user_id" is '用户id';

create table "auth_role_user_group"
(
    "id"       CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id"  CHAR(36)                       not null,
    "group_id" CHAR(36)                       not null
);

comment on table "auth_role_user_group" is '角色-用户组关系';

comment on column "auth_role_user_group"."id" is '主键';

comment on column "auth_role_user_group"."role_id" is '角色id';

comment on column "auth_role_user_group"."group_id" is '用户组id';

create table "auth_role_resource"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id"     CHAR(36)                       not null,
    "resource_id" CHAR(36)                       not null
);

comment on table "auth_role_resource" is '角色-资源关系';

comment on column "auth_role_resource"."id" is '主键';

comment on column "auth_role_resource"."role_id" is '角色id';

comment on column "auth_role_resource"."resource_id" is '资源id';