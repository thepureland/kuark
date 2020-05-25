create table "sys_dict"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "module"      VARCHAR(64),
    "dict_type"   VARCHAR(64)                    not null,
    "dict_name"   VARCHAR(64),
    "remark"      VARCHAR(128),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP
);

comment on table "sys_dict" is '字典主表';

comment on column "sys_dict"."id" is '主键';

comment on column "sys_dict"."module" is '模块';

comment on column "sys_dict"."dict_type" is '字典类型';

comment on column "sys_dict"."dict_name" is '字典名称，或其国际化key';

comment on column "sys_dict"."remark" is '备注，或其国际化key';

comment on column "sys_dict"."is_active" is '是否启用';

comment on column "sys_dict"."is_built_in" is '是否内置';

comment on column "sys_dict"."create_user" is '创建用户';

comment on column "sys_dict"."create_time" is '创建时间';

comment on column "sys_dict"."update_user" is '更新用户';

comment on column "sys_dict"."update_time" is '更新时间';

create unique index "UQ_sys_dict__dict_name_module"
    on "sys_dict" ("dict_name", "module");

create table "sys_dict_item"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "dict_id"     CHAR(36)                       not null,
    "item_code"   VARCHAR(64)                    not null,
    "parent_code" VARCHAR(64),
    "item_name"   VARCHAR(64)                    not null,
    "seq_no"      INT,
    "remark"      VARCHAR(128),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    constraint "fk_sys_dict_item"
        foreign key ("dict_id") references "sys_dict" ("id")
);

comment on table "sys_dict_item" is '字典子表';

comment on column "sys_dict_item"."id" is '主键';

comment on column "sys_dict_item"."dict_id" is '外键，父表sys_dict的主键';

comment on column "sys_dict_item"."item_code" is '字典项编号';

comment on column "sys_dict_item"."parent_code" is '父项编号';

comment on column "sys_dict_item"."item_name" is '字典项名称，或其国际化key';

comment on column "sys_dict_item"."seq_no" is '该字典编号在同父节点下的排序号';

comment on column "sys_dict_item"."remark" is '备注，或其国际化key';

comment on column "sys_dict_item"."is_active" is '是否启用';

comment on column "sys_dict_item"."is_built_in" is '是否内置';

comment on column "sys_dict_item"."create_user" is '创建用户';

comment on column "sys_dict_item"."create_time" is '创建时间';

comment on column "sys_dict_item"."update_user" is '更新用户';

comment on column "sys_dict_item"."update_time" is '更新时间';

create unique index "UQ_sys_dict_item__DICT_ID_ITEM_CODE"
    on "sys_dict_item" ("dict_id", "item_code");

create table "sys_param"
(
    "id"            CHAR(36) default RANDOM_UUID() not null
        primary key,
    "module"        VARCHAR(64),
    "param_name"    VARCHAR(32)                    not null,
    "param_value"   VARCHAR(128)                   not null,
    "default_value" VARCHAR(128),
    "seq_no"        INT,
    "remark"        VARCHAR(128),
    "is_active"     BOOLEAN  default TRUE          not null,
    "is_built_in"   BOOLEAN  default FALSE         not null,
    "create_user"   VARCHAR(36),
    "create_time"   TIMESTAMP,
    "update_user"   VARCHAR(36),
    "update_time"   TIMESTAMP
);

comment on table "sys_param" is '参数';

comment on column "sys_param"."id" is '主键';

comment on column "sys_param"."module" is '模块';

comment on column "sys_param"."param_name" is '参数名称';

comment on column "sys_param"."param_value" is '参数值，或其国际化key';

comment on column "sys_param"."default_value" is '默认参数值，或其国际化key';

comment on column "sys_param"."seq_no" is '序号';

comment on column "sys_param"."remark" is '备注，或其国际化key';

comment on column "sys_param"."is_active" is '是否启用';

comment on column "sys_param"."is_built_in" is '是否内置';

comment on column "sys_param"."create_user" is '创建用户';

comment on column "sys_param"."create_time" is '创建时间';

comment on column "sys_param"."update_user" is '更新用户';

comment on column "sys_param"."update_time" is '更新时间';

create unique index "UQ_sys_param__param_name_module"
    on "sys_param" ("param_name", "module");

create table "sys_resource"
(
    "id"                  CHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"                VARCHAR(64)                    not null,
    "url"                 VARCHAR(128),
    "resource_type__code" CHAR(1)                        not null,
    "parent_id"           CHAR(36),
    "seq_no"              INT,
    "sub_sys"             VARCHAR(32),
    "permission"          VARCHAR(64),
    "icon_url"            VARCHAR(128),
    "remark"              VARCHAR(128),
    "is_active"           BOOLEAN  default TRUE          not null,
    "is_built_in"         BOOLEAN  default FALSE         not null,
    "create_user"         VARCHAR(36),
    "create_time"         TIMESTAMP,
    "update_user"         VARCHAR(36),
    "update_time"         TIMESTAMP
);

comment on table "sys_resource" is '资源';

comment on column "sys_resource"."id" is '主键';

comment on column "sys_resource"."name" is '名称，或其国际化key';

comment on column "sys_resource"."url" is 'url';

comment on column "sys_resource"."resource_type__code" is '资源类型字典编号';

comment on column "sys_resource"."parent_id" is '父id';

comment on column "sys_resource"."seq_no" is '在同父节点下的排序号';

comment on column "sys_resource"."sub_sys" is '子系统';

comment on column "sys_resource"."permission" is '权限表达式';

comment on column "sys_resource"."icon_url" is '图标url';

comment on column "sys_resource"."remark" is '备注，或其国际化key';

comment on column "sys_resource"."is_active" is '是否启用';

comment on column "sys_resource"."is_built_in" is '是否内置';

comment on column "sys_resource"."create_user" is '创建用户';

comment on column "sys_resource"."create_time" is '创建时间';

comment on column "sys_resource"."update_user" is '更新用户';

comment on column "sys_resource"."update_time" is '更新时间';

create unique index "UQ_sys_resource__name_sub_sys"
    on "sys_resource" ("name", "sub_sys");

create table "sys_datasource"
(
    "id"           VARCHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"         VARCHAR(32)                       not null,
    "url"          VARCHAR(256)                      not null,
    "username"     VARCHAR(32)                       not null,
    "password"     VARCHAR(128)                      not null,
    "initial_size" INT,
    "max_active"   INT,
    "min_idle"     INT,
    "max_wait"     INT,
    "remark"       VARCHAR(128),
    "is_active"    BOOLEAN     default TRUE          not null,
    "is_built_in"  BOOLEAN     default FALSE         not null,
    "create_user"  VARCHAR(36),
    "create_time"  TIMESTAMP,
    "update_user"  VARCHAR(36),
    "update_time"  TIMESTAMP
);

comment on table "sys_datasource" is '数据源';

comment on column "sys_datasource"."id" is '主键';

comment on column "sys_datasource"."name" is '名称，或其国际化key';

comment on column "sys_datasource"."url" is 'url';

comment on column "sys_datasource"."username" is '用户名';

comment on column "sys_datasource"."password" is '密码，强烈建议加密';

comment on column "sys_datasource"."initial_size" is '初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时';

comment on column "sys_datasource"."max_active" is '最大连接池数量';

comment on column "sys_datasource"."min_idle" is '最小连接池数量';

comment on column "sys_datasource"."max_wait" is '获取连接时最大等待时间，单位毫秒';

comment on column "sys_datasource"."remark" is '备注，或其国际化key';

comment on column "sys_datasource"."is_active" is '是否启用';

comment on column "sys_datasource"."is_built_in" is '是否内置';

comment on column "sys_datasource"."create_user" is '创建用户';

comment on column "sys_datasource"."create_time" is '创建时间';

comment on column "sys_datasource"."update_user" is '更新用户';

comment on column "sys_datasource"."update_time" is '更新时间';



create table "user_base"
(
    "id"           VARCHAR(36) default RANDOM_UUID() not null        primary key,
    "real_name"         VARCHAR(64)                       null,
    "nickname"     VARCHAR(64)                       null,
    "sex__code"    CHAR(1)                           null,
    "birthday"    TIMESTAMP                         null,
    "id_card_no" VARCHAR(32)                        null,
    "constellation__code" VARCHAR(11)                       null,
    "country_region_id" CHAR(3)                     null,
    "nation__code" CHAR(2)                     null,
    "province_code" CHAR(2)                     null,
    "city_code" VARCHAR(16)                     null,
    "status__code" CHAR(2)                           null,
    "status_reason" VARCHAR(128)                    null,
    "user_type__code" CHAR(2)                           null,
    "avatar"       VARCHAR(128)                      null,
    "sub_sys__code" VARCHAR(32)                     null,
    "freeze_start_time" TIMESTAMP                         null，
    "freeze_end_time" TIMESTAMP                         null，
    "last_login_time" TIMESTAMP                         null，
    "last_logout_time" TIMESTAMP                         null，
    "last_login_ip" int                             null,
    "last_login_terminal__code" VARCHAR(16)                             null,
    "total_online_time" int                         null,
    "register_ip" int                               null,
    "register_addr" VARCHAR(128)                               null,
    "dynamic_auth_key" VARCHAR(64)                               null,
    "remark"       VARCHAR(128)    null,
    "is_active"    BOOLEAN     default TRUE          not null,
    "is_built_in"  BOOLEAN     default FALSE         not null,
    "create_user"  VARCHAR(36)                         null,
    "create_time"  TIMESTAMP                         null,
    "update_user"  VARCHAR(36)                         null,
    "update_time"  TIMESTAMP                         null
);

comment on table "user_base" is '用户基础信息';

