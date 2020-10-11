create table "sys_dict"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "module"      VARCHAR(63),
    "dict_type"   VARCHAR(63)                    not null,
    "dict_name"   VARCHAR(63),
    "remark"      VARCHAR(127),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP default now() not null,
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

create unique index "uq_sys_dict__dict_name_module" on "sys_dict" ("dict_name", "module");

create table "sys_dict_item"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "dict_id"     CHAR(36)                       not null,
    "item_code"   VARCHAR(63)                    not null,
    "parent_code" VARCHAR(63),
    "item_name"   VARCHAR(63)                    not null,
    "seq_no"      INT4,
    "remark"      VARCHAR(127),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP default now() not null,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    constraint "fk_sys_dict_item"
        foreign key ("dict_id") references "sys_dict" ("id")
);

comment on table "sys_dict_item" is '字典项';

comment on column "sys_dict_item"."id" is '主键';

comment on column "sys_dict_item"."dict_id" is '外键，sys_dict表的主键';

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

create unique index "uq_sys_dict_item__dict_id_item_code" on "sys_dict_item" ("dict_id", "item_code");

create table "sys_param"
(
    "id"            CHAR(36) default RANDOM_UUID() not null
        primary key,
    "module"        VARCHAR(63),
    "param_name"    VARCHAR(31)                    not null,
    "param_value"   VARCHAR(127)                   not null,
    "default_value" VARCHAR(127),
    "seq_no"        INT2,
    "remark"        VARCHAR(127),
    "is_active"     BOOLEAN  default TRUE          not null,
    "is_built_in"   BOOLEAN  default FALSE         not null,
    "create_user"   VARCHAR(36),
    "create_time"   TIMESTAMP default now() not null,
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

create unique index "uq_sys_param__param_name_module" on "sys_param" ("param_name", "module");

create table "sys_resource"
(
    "id"                  CHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"                VARCHAR(63)                    not null,
    "url"                 VARCHAR(127),
    "resource_type_dict_code" CHAR(1)                        not null,
    "parent_id"           CHAR(36),
    "seq_no"              TINYINT,
    "sub_sys_dict_code"       VARCHAR(31),
    "permission"          VARCHAR(63),
    "icon_url"            VARCHAR(127),
    "remark"              VARCHAR(127),
    "is_active"           BOOLEAN  default TRUE          not null,
    "is_built_in"         BOOLEAN  default FALSE         not null,
    "create_user"         VARCHAR(36),
    "create_time"         TIMESTAMP  default now() not null,
    "update_user"         VARCHAR(36),
    "update_time"         TIMESTAMP
);

comment on table "sys_resource" is '资源';

comment on column "sys_resource"."id" is '主键';

comment on column "sys_resource"."name" is '名称，或其国际化key';

comment on column "sys_resource"."url" is 'url';

comment on column "sys_resource"."resource_type_dict_code" is '资源类型字典代码';

comment on column "sys_resource"."parent_id" is '父id';

comment on column "sys_resource"."seq_no" is '在同父节点下的排序号';

comment on column "sys_resource"."sub_sys_dict_code" is '子系统代码';

comment on column "sys_resource"."permission" is '权限表达式';

comment on column "sys_resource"."icon_url" is '图标url';

comment on column "sys_resource"."remark" is '备注，或其国际化key';

comment on column "sys_resource"."is_active" is '是否启用';

comment on column "sys_resource"."is_built_in" is '是否内置';

comment on column "sys_resource"."create_user" is '创建用户';

comment on column "sys_resource"."create_time" is '创建时间';

comment on column "sys_resource"."update_user" is '更新用户';

comment on column "sys_resource"."update_time" is '更新时间';

create unique index "uq_sys_resource__name_sub_sys"  on "sys_resource" ("name", "sub_sys_dict_code");

create table "sys_data_source"
(
    "id"           VARCHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"         VARCHAR(31)                       not null,
    "url"          VARCHAR(256)                      not null,
    "username"     VARCHAR(31)                       not null,
    "password"     VARCHAR(127)                      not null,
    "initial_size" INT2,
    "max_active"   INT2,
    "min_idle"     INT2,
    "max_wait"     INT2,
    "remark"       VARCHAR(127),
    "is_active"    BOOLEAN     default TRUE          not null,
    "is_built_in"  BOOLEAN     default FALSE         not null,
    "create_user"  VARCHAR(36),
    "create_time"  TIMESTAMP  default now() not null,
    "update_user"  VARCHAR(36),
    "update_time"  TIMESTAMP
);

comment on table "sys_data_source" is '数据源';

comment on column "sys_data_source"."id" is '主键';

comment on column "sys_data_source"."name" is '名称，或其国际化key';

comment on column "sys_data_source"."url" is 'url';

comment on column "sys_data_source"."username" is '用户名';

comment on column "sys_data_source"."password" is '密码，强烈建议加密';

comment on column "sys_data_source"."initial_size" is '初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时';

comment on column "sys_data_source"."max_active" is '最大连接池数量';

comment on column "sys_data_source"."min_idle" is '最小连接池数量';

comment on column "sys_data_source"."max_wait" is '获取连接时最大等待时间，单位毫秒';

comment on column "sys_data_source"."remark" is '备注，或其国际化key';

comment on column "sys_data_source"."is_active" is '是否启用';

comment on column "sys_data_source"."is_built_in" is '是否内置';

comment on column "sys_data_source"."create_user" is '创建用户';

comment on column "sys_data_source"."create_time" is '创建时间';

comment on column "sys_data_source"."update_user" is '更新用户';

comment on column "sys_data_source"."update_time" is '更新时间';