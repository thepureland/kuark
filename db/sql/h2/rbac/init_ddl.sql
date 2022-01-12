create table "rbac_user_group"
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

comment on table "rbac_user_group" is '用户组';

comment on column "rbac_user_group"."id" is '主键';

comment on column "rbac_user_group"."group_code" is '用户编码';

comment on column "rbac_user_group"."group_name" is '用户组名';

comment on column "rbac_user_group"."sub_sys_dict_code" is '子系统代码';

comment on column "rbac_user_group"."remark" is '备注，或其国际化key';

comment on column "rbac_user_group"."active" is '是否启用';

comment on column "rbac_user_group"."built_in" is '是否内置';

comment on column "rbac_user_group"."create_user" is '创建用户';

comment on column "rbac_user_group"."create_time" is '创建时间';

comment on column "rbac_user_group"."update_user" is '更新用户';

comment on column "rbac_user_group"."update_time" is '更新时间';
COMMENT ON COLUMN "rbac_user_group"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create table "rbac_user_group_user"
(
    "id"       CHAR(36) default RANDOM_UUID() not null
        primary key,
    "group_id" CHAR(36)                       not null,
    "user_id"  CHAR(36)                       not null
);

comment on table "rbac_user_group_user" is '用户组-用户关系';

comment on column "rbac_user_group_user"."id" is '主键';

comment on column "rbac_user_group_user"."group_id" is '用户组id';

comment on column "rbac_user_group_user"."user_id" is '用户id';

create table "rbac_role"
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

comment on table "rbac_role" is '角色';

comment on column "rbac_role"."id" is '主键';

comment on column "rbac_role"."role_code" is '角色编码';

comment on column "rbac_role"."role_name" is '角色名';

comment on column "rbac_role"."sub_sys_dict_code" is '子系统代码';

comment on column "rbac_role"."remark" is '备注，或其国际化key';

comment on column "rbac_role"."active" is '是否启用';

comment on column "rbac_role"."built_in" is '是否内置';

comment on column "rbac_role"."create_user" is '创建用户';

comment on column "rbac_role"."create_time" is '创建时间';

comment on column "rbac_role"."update_user" is '更新用户';

comment on column "rbac_role"."update_time" is '更新时间';
COMMENT ON COLUMN "rbac_role"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create table "rbac_role_user"
(
    "id"      CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id" CHAR(36)                       not null,
    "user_id" CHAR(36)                       not null
);

comment on table "rbac_role_user" is '角色-用户关系';

comment on column "rbac_role_user"."id" is '主键';

comment on column "rbac_role_user"."role_id" is '角色id';

comment on column "rbac_role_user"."user_id" is '用户id';

create table "rbac_role_user_group"
(
    "id"       CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id"  CHAR(36)                       not null,
    "group_id" CHAR(36)                       not null
);

comment on table "rbac_role_user_group" is '角色-用户组关系';

comment on column "rbac_role_user_group"."id" is '主键';

comment on column "rbac_role_user_group"."role_id" is '角色id';

comment on column "rbac_role_user_group"."group_id" is '用户组id';

create table "rbac_role_resource"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "role_id"     CHAR(36)                       not null,
    "resource_id" CHAR(36)                       not null
);

comment on table "rbac_role_resource" is '角色-资源关系';

comment on column "rbac_role_resource"."id" is '主键';

comment on column "rbac_role_resource"."role_id" is '角色id';

comment on column "rbac_role_resource"."resource_id" is '资源id';