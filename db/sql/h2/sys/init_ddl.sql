create table "sys_dict"
(
    "id"          CHAR(36)  default RANDOM_UUID() not null
        primary key,
    "module"      VARCHAR(64),
    "dict_type"   VARCHAR(64)                     not null,
    "dict_name"   VARCHAR(64),
    "remark"      VARCHAR(128),
    "active"      BOOLEAN   default TRUE          not null,
    "built_in"    BOOLEAN   default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP default now(),
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP
);

comment
on table "sys_dict" is '字典主表';

comment
on column "sys_dict"."id" is '主键';

comment
on column "sys_dict"."module" is '模块';

comment
on column "sys_dict"."dict_type" is '字典类型';

comment
on column "sys_dict"."dict_name" is '字典名称，或其国际化key';

comment
on column "sys_dict"."remark" is '备注，或其国际化key';

comment
on column "sys_dict"."active" is '是否启用';

comment
on column "sys_dict"."built_in" is '是否内置';

comment
on column "sys_dict"."create_user" is '创建用户';

comment
on column "sys_dict"."create_time" is '创建时间';

comment
on column "sys_dict"."update_user" is '更新用户';

comment
on column "sys_dict"."update_time" is '更新时间';

create
unique index "uq_sys_dict__dict_name_module" on "sys_dict" ("dict_name", "module");

create table "sys_dict_item"
(
    "id"          CHAR(36)  default RANDOM_UUID() not null
        primary key,
    "dict_id"     CHAR(36)                        not null,
    "item_code"   VARCHAR(64)                     not null,
    "item_name"   VARCHAR(64)                     not null,
    "parent_code" VARCHAR(64),
    "parent_id"   CHAR(36)                        not null,
    "seq_no"      INT4,
    "remark"      VARCHAR(128),
    "active"      BOOLEAN   default TRUE          not null,
    "built_in"    BOOLEAN   default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP default now(),
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    constraint "fk_sys_dict_item"
        foreign key ("dict_id") references "sys_dict" ("id")
);

comment
on table "sys_dict_item" is '字典项';

comment
on column "sys_dict_item"."id" is '主键';

comment
on column "sys_dict_item"."dict_id" is '外键，sys_dict表的主键';

comment
on column "sys_dict_item"."item_code" is '字典项编号';

comment
on column "sys_dict_item"."item_name" is '字典项名称，或其国际化key';

comment
on column "sys_dict_item"."parent_code" is '父项编号';

comment
on column "sys_dict_item"."parent_id" is '父项主键';

comment
on column "sys_dict_item"."seq_no" is '该字典编号在同父节点下的排序号';

comment
on column "sys_dict_item"."remark" is '备注，或其国际化key';

comment
on column "sys_dict_item"."active" is '是否启用';

comment
on column "sys_dict_item"."built_in" is '是否内置';

comment
on column "sys_dict_item"."create_user" is '创建用户';

comment
on column "sys_dict_item"."create_time" is '创建时间';

comment
on column "sys_dict_item"."update_user" is '更新用户';

comment
on column "sys_dict_item"."update_time" is '更新时间';

create
unique index "uq_sys_dict_item__dict_id_item_code" on "sys_dict_item" ("dict_id", "item_code");

create table "sys_param"
(
    "id"            CHAR(36)  default RANDOM_UUID() not null
        primary key,
    "module"        VARCHAR(64),
    "param_name"    VARCHAR(32)                     not null,
    "param_value"   VARCHAR(128)                    not null,
    "default_value" VARCHAR(128),
    "seq_no"        INT2,
    "remark"        VARCHAR(128),
    "active"        BOOLEAN   default TRUE          not null,
    "built_in"      BOOLEAN   default FALSE         not null,
    "create_user"   VARCHAR(36),
    "create_time"   TIMESTAMP default now()         not null,
    "update_user"   VARCHAR(36),
    "update_time"   TIMESTAMP
);

comment
on table "sys_param" is '参数';

comment
on column "sys_param"."id" is '主键';

comment
on column "sys_param"."module" is '模块';

comment
on column "sys_param"."param_name" is '参数名称';

comment
on column "sys_param"."param_value" is '参数值，或其国际化key';

comment
on column "sys_param"."default_value" is '默认参数值，或其国际化key';

comment
on column "sys_param"."seq_no" is '序号';

comment
on column "sys_param"."remark" is '备注，或其国际化key';

comment
on column "sys_param"."active" is '是否启用';

comment
on column "sys_param"."built_in" is '是否内置';

comment
on column "sys_param"."create_user" is '创建用户';

comment
on column "sys_param"."create_time" is '创建时间';

comment
on column "sys_param"."update_user" is '更新用户';

comment
on column "sys_param"."update_time" is '更新时间';

create
unique index "uq_sys_param__param_name_module" on "sys_param" ("param_name", "module");

create table "sys_resource"
(
    "id"                      CHAR(36)  default RANDOM_UUID() not null
        primary key,
    "name"                    VARCHAR(64)                     not null,
    "url"                     VARCHAR(128),
    "resource_type_dict_code" CHAR(1)                         not null,
    "parent_id"               CHAR(36),
    "seq_no"                  INT2,
    "sub_sys_dict_code"       VARCHAR(32)                     not null,
    "icon"                    VARCHAR(128),
    "remark"                  VARCHAR(128),
    "active"                  BOOLEAN   default TRUE          not null,
    "built_in"                BOOLEAN   default FALSE         not null,
    "create_user"             VARCHAR(36),
    "create_time"             TIMESTAMP default now()         not null,
    "update_user"             VARCHAR(36),
    "update_time"             TIMESTAMP
);

comment
on table "sys_resource" is '资源';

comment
on column "sys_resource"."id" is '主键';

comment
on column "sys_resource"."name" is '名称，或其国际化key';

comment
on column "sys_resource"."url" is 'url';

comment
on column "sys_resource"."resource_type_dict_code" is '资源类型字典代码';

comment
on column "sys_resource"."parent_id" is '父id';

comment
on column "sys_resource"."seq_no" is '在同父节点下的排序号';

comment
on column "sys_resource"."sub_sys_dict_code" is '子系统代码';

comment
on column "sys_resource"."icon" is '图标';

comment
on column "sys_resource"."remark" is '备注，或其国际化key';

comment
on column "sys_resource"."active" is '是否启用';

comment
on column "sys_resource"."built_in" is '是否内置';

comment
on column "sys_resource"."create_user" is '创建用户';

comment
on column "sys_resource"."create_time" is '创建时间';

comment
on column "sys_resource"."update_user" is '更新用户';

comment
on column "sys_resource"."update_time" is '更新时间';

create
unique index "uq_sys_resource__name_sub_sys"  on "sys_resource" ("name", "sub_sys_dict_code");

create table "sys_data_source"
(
    "id"                VARCHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"              VARCHAR(32)                       not null,
    "sub_sys_dict_code" VARCHAR(32)                       not null,
    "tenant_id"         VARCHAR(36),
    "url"               VARCHAR(256)                      not null,
    "username"          VARCHAR(32)                       not null,
    "password"          VARCHAR(128),
    "initial_size"      INT2,
    "max_active"        INT2,
    "max_idle"          INT2,
    "min_idle"          INT2,
    "max_wait"          INT2,
    "max_age"           INT2,
    "remark"            VARCHAR(128),
    "active"            BOOLEAN     default TRUE          not null,
    "built_in"          BOOLEAN     default FALSE         not null,
    "create_user"       VARCHAR(36),
    "create_time"       TIMESTAMP   default now()         not null,
    "update_user"       VARCHAR(36),
    "update_time"       TIMESTAMP
);

comment
on table "sys_data_source" is '数据源';

comment
on column "sys_data_source"."id" is '主键';

comment
on column "sys_data_source"."name" is '名称，或其国际化key';

comment
on column "sys_data_source"."sub_sys_dict_code" is '子系统代码';

comment
on column "sys_data_source"."tenant_id" is '租户id';

comment
on column "sys_data_source"."url" is 'url';

comment
on column "sys_data_source"."username" is '用户名';

comment
on column "sys_data_source"."password" is '密码，强烈建议加密';

comment
on column "sys_data_source"."initial_size" is '初始连接数。初始化发生在显示调用init方法，或者第一次getConnection时';

comment
on column "sys_data_source"."max_active" is '最大连接数';

comment
on column "sys_data_source"."max_idle" is '最大空闲连接数';

comment
on column "sys_data_source"."min_idle" is '最小空闲连接数。至少维持多少个空闲连接';

comment
on column "sys_data_source"."max_wait" is '出借最长期限(毫秒)。客户端从连接池获取（借出）一个连接后，超时没有归还（return），则连接池会抛出异常';

comment
on column "sys_data_source"."max_age" is '连接寿命(毫秒)。超时(相对于初始化时间)连接池将在出借或归还时删除这个连接';

comment
on column "sys_data_source"."remark" is '备注，或其国际化key';

comment
on column "sys_data_source"."active" is '是否启用';

comment
on column "sys_data_source"."built_in" is '是否内置';

comment
on column "sys_data_source"."create_user" is '创建用户';

comment
on column "sys_data_source"."create_time" is '创建时间';

comment
on column "sys_data_source"."update_user" is '更新用户';

comment
on column "sys_data_source"."update_time" is '更新时间';


create table "sys_tenant"
(
    "id"                CHAR(36)  default RANDOM_UUID() not null primary key,
    "sub_sys_dict_code" VARCHAR(32)                     not null,
    "name"              VARCHAR(64)                     not null,
    "remark"            VARCHAR(128),
    "active"            BOOLEAN   default TRUE          not null,
    "built_in"          BOOLEAN   default FALSE         not null,
    "create_user"       VARCHAR(36),
    "create_time"       TIMESTAMP default now(),
    "update_user"       VARCHAR(36),
    "update_time"       TIMESTAMP
);

create
unique index "uq_sys_tenant" on "sys_tenant" ("sub_sys_dict_code", "name");

comment
on table "sys_tenant" is '租户';

comment
on column "sys_tenant"."id" is '主键';

comment
on column "sys_tenant"."sub_sys_dict_code" is '子系统代码';

comment
on column "sys_tenant"."name" is '名称';

comment
on column "sys_tenant"."remark" is '备注，或其国际化key';

comment
on column "sys_tenant"."active" is '是否启用';

comment
on column "sys_tenant"."built_in" is '是否内置';

comment
on column "sys_tenant"."create_user" is '创建用户';

comment
on column "sys_tenant"."create_time" is '创建时间';

comment
on column "sys_tenant"."update_user" is '更新用户';

comment
on column "sys_tenant"."update_time" is '更新时间';



create table "sys_cache"
(
    "id"                 CHAR(36)  default RANDOM_UUID() not null primary key,
    "name"               VARCHAR(64)                     not null,
    "sub_sys_dict_code"  VARCHAR(32)                     not null,
    "strategy_dict_code" VARCHAR(16)                     not null,
    "write_on_boot"      BOOLEAN   default FALSE         not null,
    "write_in_time"      BOOLEAN   default FALSE         not null,
    "ttl"                INT2,
    "remark"             VARCHAR(128),
    "active"             BOOLEAN   default TRUE          not null,
    "built_in"           BOOLEAN   default FALSE         not null,
    "create_user"        VARCHAR(36),
    "create_time"        TIMESTAMP default now(),
    "update_user"        VARCHAR(36),
    "update_time"        TIMESTAMP
);

create
unique index "uq_sys_cache" on "sys_cache" ("name");

comment
on table "sys_cache" is '缓存';

comment
on column "sys_cache"."id" is '主键';

comment
on column "sys_cache"."name" is '名称';

comment
on column "sys_cache"."sub_sys_dict_code" is '子系统代码';

comment
on column "sys_cache"."strategy_dict_code" is '缓存策略代码';

comment
on column "sys_cache"."write_on_boot" is '是否启动时写缓存';

comment
on column "sys_cache"."write_in_time" is '是否及时回写缓存';

comment
on column "sys_cache"."ttl" is '缓存生存时间(秒)';

comment
on column "sys_cache"."remark" is '备注，或其国际化key';

comment
on column "sys_cache"."active" is '是否启用';

comment
on column "sys_cache"."built_in" is '是否内置';

comment
on column "sys_cache"."create_user" is '创建用户';

comment
on column "sys_cache"."create_time" is '创建时间';

comment
on column "sys_cache"."update_user" is '更新用户';

comment
on column "sys_cache"."update_time" is '更新时间';



create table "sys_domain"
(
    "id"                VARCHAR(36) default RANDOM_UUID() not null primary key,
    "domain"            VARCHAR(64)                       not null,
    "sub_sys_dict_code" VARCHAR(32)                       not null,
    "tenant_id"         VARCHAR(36),
    "remark"            VARCHAR(128),
    "active"            BOOLEAN     default TRUE          not null,
    "built_in"          BOOLEAN     default FALSE         not null,
    "create_user"       VARCHAR(36),
    "create_time"       TIMESTAMP   default now()         not null,
    "update_user"       VARCHAR(36),
    "update_time"       TIMESTAMP
);

create
unique index "uq_sys_domain" on "sys_domain" ("domain");

comment
on table "sys_domain" is '域名';

comment
on column "sys_domain"."id" is '主键';

comment
on column "sys_domain"."domain" is '域名';

comment
on column "sys_domain"."sub_sys_dict_code" is '子系统代码';

comment
on column "sys_domain"."tenant_id" is '租户id';

comment
on column "sys_domain"."remark" is '备注，或其国际化key';

comment
on column "sys_domain"."active" is '是否启用';

comment
on column "sys_domain"."built_in" is '是否内置';

comment
on column "sys_domain"."create_user" is '创建用户';

comment
on column "sys_domain"."create_time" is '创建时间';

comment
on column "sys_domain"."update_user" is '更新用户';

comment
on column "sys_domain"."update_time" is '更新时间';