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

create unique index "uq_sys_dict__dict_name_module"
    on "sys_dict" ("dict_name", "module");

create table "sys_dict_item"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "dict_id"     CHAR(36)                       not null,
    "item_code"   VARCHAR(64)                    not null,
    "parent_code" VARCHAR(64),
    "item_name"   VARCHAR(64)                    not null,
    "seq_no"      INT4,
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

create unique index "uq_sys_dict_item__dict_id_item_code"
    on "sys_dict_item" ("dict_id", "item_code");

create table "sys_param"
(
    "id"            CHAR(36) default RANDOM_UUID() not null
        primary key,
    "module"        VARCHAR(64),
    "param_name"    VARCHAR(32)                    not null,
    "param_value"   VARCHAR(128)                   not null,
    "default_value" VARCHAR(128),
    "seq_no"        INT2,
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

create unique index "uq_sys_param__param_name_module"
    on "sys_param" ("param_name", "module");

create table "sys_resource"
(
    "id"                  CHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"                VARCHAR(64)                    not null,
    "url"                 VARCHAR(128),
    "resource_type__code" CHAR(1)                        not null,
    "parent_id"           CHAR(36),
    "seq_no"              TINYINT,
    "sub_sys__code"       VARCHAR(32),
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

comment on column "sys_resource"."resource_type__code" is '资源类型字典代码';

comment on column "sys_resource"."parent_id" is '父id';

comment on column "sys_resource"."seq_no" is '在同父节点下的排序号';

comment on column "sys_resource"."sub_sys__code" is '子系统代码';

comment on column "sys_resource"."permission" is '权限表达式';

comment on column "sys_resource"."icon_url" is '图标url';

comment on column "sys_resource"."remark" is '备注，或其国际化key';

comment on column "sys_resource"."is_active" is '是否启用';

comment on column "sys_resource"."is_built_in" is '是否内置';

comment on column "sys_resource"."create_user" is '创建用户';

comment on column "sys_resource"."create_time" is '创建时间';

comment on column "sys_resource"."update_user" is '更新用户';

comment on column "sys_resource"."update_time" is '更新时间';

create unique index "uq_sys_resource__name_sub_sys"
    on "sys_resource" ("name", "sub_sys__code");

create table "sys_data_source"
(
    "id"           VARCHAR(36) default RANDOM_UUID() not null
        primary key,
    "name"         VARCHAR(32)                       not null,
    "url"          VARCHAR(256)                      not null,
    "username"     VARCHAR(32)                       not null,
    "password"     VARCHAR(128)                      not null,
    "initial_size" INT2,
    "max_active"   INT2,
    "min_idle"     INT2,
    "max_wait"     INT2,
    "remark"       VARCHAR(128),
    "is_active"    BOOLEAN     default TRUE          not null,
    "is_built_in"  BOOLEAN     default FALSE         not null,
    "create_user"  VARCHAR(36),
    "create_time"  TIMESTAMP,
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

create table "user_account"
(
    "id"                        CHAR(36) default RANDOM_UUID() not null
        primary key,
    "sub_sys__code"             VARCHAR(32),
    "user_status__code"         CHAR(2),
    "user_status_reason"        VARCHAR(128),
    "user_type__code"           CHAR(2),
    "freeze_time_start"         TIMESTAMP,
    "freeze_time_end"           TIMESTAMP,
    "last_login_time"           TIMESTAMP,
    "last_logout_time"          TIMESTAMP,
    "last_login_ip"             CHAR(39),
    "last_login_terminal__code" VARCHAR(16),
    "total_online_time"         REAL,
    "register_ip"               CHAR(39),
    "register_url"              VARCHAR(128),
    "dynamic_auth_key"          VARCHAR(64),
    "second_password"           VARCHAR(32),
    "remark"                    VARCHAR(128),
    "is_active"                 BOOLEAN  default TRUE          not null,
    "is_built_in"               BOOLEAN  default FALSE         not null,
    "create_user"               VARCHAR(36),
    "create_time"               TIMESTAMP,
    "update_user"               VARCHAR(36),
    "update_time"               TIMESTAMP,
    "owner_id" VARCHAR(36)
);

comment on table "user_account" is '用户账号';

comment on column "user_account"."id" is '主键';

comment on column "user_account"."sub_sys__code" is '子系统代码';

comment on column "user_account"."user_status__code" is '用户状态代码';

comment on column "user_account"."user_status_reason" is '用户状态原因';

comment on column "user_account"."user_type__code" is '用户类型代码';

comment on column "user_account"."freeze_time_start" is '账号冻结时间起';

comment on column "user_account"."freeze_time_end" is '账号冻结时间止';

comment on column "user_account"."last_login_time" is '最后一次登入时间';

comment on column "user_account"."last_logout_time" is '最后一次登出时间';

comment on column "user_account"."last_login_ip" is '最后一次登入ip(标准ipv6全格式)';

comment on column "user_account"."last_login_terminal__code" is '最后一次登入终端代码';

comment on column "user_account"."total_online_time" is '总在线时长(小时)';

comment on column "user_account"."register_ip" is '注册ip(标准ipv6全格式)';

comment on column "user_account"."register_url" is '注册url';

comment on column "user_account"."dynamic_auth_key" is '动态验证码的密钥';

comment on column "user_account"."second_password" is '二级密码';

comment on column "user_account"."remark" is '备注，或其国际化key';

comment on column "user_account"."is_active" is '是否启用';

comment on column "user_account"."is_built_in" is '是否内置';

comment on column "user_account"."create_user" is '创建用户';

comment on column "user_account"."create_time" is '创建时间';

comment on column "user_account"."update_user" is '更新用户';

comment on column "user_account"."update_time" is '更新时间';
COMMENT ON COLUMN "user_account"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';


create table "user_account_auth"
(
    "id"                  CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_account_id"     CHAR(36)                       not null,
    "identity_type__code" VARCHAR(16)                    not null,
    "identifier"          VARCHAR(64)                    not null,
    "sub_sys__code"       VARCHAR(32),
    "owner_id"       VARCHAR(36),
    "credential"          VARCHAR(64)                    not null,
    "is_verified"         BOOLEAN  default FALSE         not null,
    "remark"              VARCHAR(128),
    "is_active"           BOOLEAN  default TRUE          not null,
    "is_built_in"         BOOLEAN  default FALSE         not null,
    "create_user"         VARCHAR(36),
    "create_time"         TIMESTAMP,
    "update_user"         VARCHAR(36),
    "update_time"         TIMESTAMP,
    constraint "fk_user_account_auth"
        foreign key ("user_account_id") references "user_account" ("id")
);

comment on table "user_account_auth" is '用户账号授权';

comment on column "user_account_auth"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "user_account_auth"."identity_type__code" is '身份类型代码';

comment on column "user_account_auth"."identifier" is '唯一身份标识';

comment on column "user_account_auth"."sub_sys__code" is '子系统代码';

comment on column "user_account_auth"."credential" is '本系统账号是密码、第三方的是Token';

comment on column "user_account_auth"."is_verified" is '授权账号是否被验证';

comment on column "user_account_auth"."remark" is '备注，或其国际化key';

comment on column "user_account_auth"."is_active" is '是否启用';

comment on column "user_account_auth"."is_built_in" is '是否内置';

comment on column "user_account_auth"."create_user" is '创建用户';

comment on column "user_account_auth"."create_time" is '创建时间';

comment on column "user_account_auth"."update_user" is '更新用户';

comment on column "user_account_auth"."update_time" is '更新时间';
COMMENT ON COLUMN "user_account_auth"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';

create unique index "uq_u_a_a__identifier_id_type_sub_sys_owner"
    on "user_account_auth" ("identifier", "identity_type__code", "sub_sys__code", "owner_id");

create table "user_db_audit_log"
(
    "id"                 CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_account_id"    CHAR(36)                       not null,
    "table_name"         VARCHAR(64)                    not null,
    "operate_time"       TIMESTAMP                      not null,
    "operate_type__code" CHAR(1)                        not null,
    constraint "fk_user_db_audit_log"
        foreign key ("user_account_id") references "user_account" ("id")
);

comment on table "user_db_audit_log" is '用户数据库操作审计日志';

comment on column "user_db_audit_log"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "user_db_audit_log"."table_name" is '表名';

comment on column "user_db_audit_log"."operate_time" is '操作时间';

comment on column "user_db_audit_log"."operate_type__code" is '操作类型代码';

create table "user_db_audit_log_item"
(
    "id"              CHAR(36) default RANDOM_UUID() not null
        primary key,
    "db_audit_log_id" CHAR(36)                       not null,
    "record_id"       VARCHAR(36)                    not null,
    "column_name"     VARCHAR(64),
    "old_value"       VARCHAR,
    "new_value"       VARCHAR,
    constraint "fk_user_db_audit_log_item"
        foreign key ("db_audit_log_id") references "user_db_audit_log" ("id")
);

comment on table "user_db_audit_log_item" is '用户数据库操作审计日志明细';

comment on column "user_db_audit_log_item"."db_audit_log_id" is '外键，数据库操作审计日志id，user_db_audit_log表主键';

comment on column "user_db_audit_log_item"."record_id" is '记录的主键值';

comment on column "user_db_audit_log_item"."column_name" is 'update的列名';

comment on column "user_db_audit_log_item"."old_value" is 'update前的值';

comment on column "user_db_audit_log_item"."new_value" is 'update后的值';

create table "user_biz_audit_log"
(
    "id"                    CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_account_id"       CHAR(36)                       not null,
    "identity_type__code"   VARCHAR(16)                    not null,
    "identity_type"         VARCHAR(32)                    not null,
    "identifier"            VARCHAR(64)                    not null,
    "sub_sys__code"         VARCHAR(32),
    "sub_sys"               VARCHAR(64),
    "module"                VARCHAR(64),
    "operate_time"          TIMESTAMP                      not null,
    "client_ip"             CHAR(39),
    "client_ip_isp"         VARCHAR(128),
    "client_region_code"    VARCHAR(12),
    "client_region_name"    VARCHAR(128),
    "client_terminal__code" VARCHAR(16),
    "client_terminal"       VARCHAR(32),
    "client_os"             VARCHAR(64),
    "client_browser"        VARCHAR(64),
    constraint "fk_user_biz_audit_log"
        foreign key ("user_account_id") references "user_account" ("id")
);

comment on table "user_biz_audit_log" is '用户审计日志';

comment on column "user_biz_audit_log"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "user_biz_audit_log"."identity_type__code" is '身份类型代码';

comment on column "user_biz_audit_log"."identity_type" is '身份类型';

comment on column "user_biz_audit_log"."identifier" is '唯一身份标识';

comment on column "user_biz_audit_log"."sub_sys__code" is '子系统代码';

comment on column "user_biz_audit_log"."sub_sys" is '子系统';

comment on column "user_biz_audit_log"."module" is '模块';

comment on column "user_biz_audit_log"."operate_time" is '操作时间';

comment on column "user_biz_audit_log"."client_ip" is '客户端ip，标准全格式ipv6';

comment on column "user_biz_audit_log"."client_ip_isp" is '客户端ip的isp';

comment on column "user_biz_audit_log"."client_region_code" is '客户端区域编码';

comment on column "user_biz_audit_log"."client_region_name" is '客户端区域';

comment on column "user_biz_audit_log"."client_terminal__code" is '客户端终端类型代码';

comment on column "user_biz_audit_log"."client_terminal" is '客户端终端类型';

comment on column "user_biz_audit_log"."client_os" is '客户端操作系统';

comment on column "user_biz_audit_log"."client_browser" is '客户端浏览器';

create table "user_personal_info"
(
    "id"                        CHAR(36)              not null
        primary key,
    "real_name"                 VARCHAR(64),
    "nickname"                  VARCHAR(64),
    "sex__code"                 CHAR(1) default '9'   not null,
    "birthday"                  DATE,
    "id_card_no"                VARCHAR(32),
    "constellation__code"       VARCHAR(11),
    "country_id"                CHAR(3),
    "nation__code"              VARCHAR(3),
    "region_code"               VARCHAR(12),
    "user_status__code"         CHAR(2),
    "user_status_reason"        VARCHAR(128),
    "user_type__code"           CHAR(2),
    "avatar_url"                VARCHAR(128),
    "sub_sys__code"             VARCHAR(32),
    "freeze_time_start"         TIMESTAMP,
    "freeze_time_end"           TIMESTAMP,
    "last_login_time"           TIMESTAMP,
    "last_logout_time"          TIMESTAMP,
    "last_login_ip"             CHAR(39),
    "last_login_terminal__code" VARCHAR(16),
    "total_online_time"         REAL,
    "register_ip"               CHAR(39),
    "register_url"              VARCHAR(128),
    "dynamic_auth_key"          VARCHAR(64),
    "remark"                    VARCHAR(128),
    "is_active"                 BOOLEAN default TRUE  not null,
    "is_built_in"               BOOLEAN default FALSE not null,
    "create_user"               VARCHAR(36),
    "create_time"               TIMESTAMP,
    "update_user"               VARCHAR(36),
    "update_time"               TIMESTAMP
);

comment on table "user_personal_info" is '用户个人信息';

comment on column "user_personal_info"."id" is '主键，与user_account共用主键';

comment on column "user_personal_info"."real_name" is '真实姓名';

comment on column "user_personal_info"."nickname" is '昵称';

comment on column "user_personal_info"."sex__code" is '性别代码';

comment on column "user_personal_info"."birthday" is '生日';

comment on column "user_personal_info"."id_card_no" is '身份证号';

comment on column "user_personal_info"."constellation__code" is '星座代码';

comment on column "user_personal_info"."country_id" is '国家id';

comment on column "user_personal_info"."nation__code" is '民族代码';

comment on column "user_personal_info"."region_code" is '地区编码';

comment on column "user_personal_info"."avatar_url" is '头像url';

comment on column "user_personal_info"."remark" is '备注，或其国际化key';

comment on column "user_personal_info"."is_active" is '是否启用';

comment on column "user_personal_info"."is_built_in" is '是否内置';

comment on column "user_personal_info"."create_user" is '创建用户';

comment on column "user_personal_info"."create_time" is '创建时间';

comment on column "user_personal_info"."update_user" is '更新用户';

comment on column "user_personal_info"."update_time" is '更新时间';

create table "user_contact_way"
(
    "id"                       CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_id"                  CHAR(36)                       not null,
    "contact_way__code"        CHAR(3)                        not null,
    "contact_way_value"        VARCHAR(128)                   not null,
    "contact_way_status__code" CHAR(2)  default '00'          not null,
    "priority"                 TINYINT,
    "remark"                   VARCHAR(128),
    "is_active"                BOOLEAN  default TRUE          not null,
    "is_built_in"              BOOLEAN  default FALSE         not null,
    "create_user"              VARCHAR(36),
    "create_time"              TIMESTAMP,
    "update_user"              VARCHAR(36),
    "update_time"              TIMESTAMP,
    constraint "fk_user_contact_way"
        foreign key ("user_id") references "user_account" ("id")
);

comment on table "user_contact_way" is '用户联系方式';

comment on column "user_contact_way"."user_id" is '外键，用户账号id，user_account表主键';

comment on column "user_contact_way"."contact_way__code" is '联系方式代码';

comment on column "user_contact_way"."contact_way_value" is '联系方式值';

comment on column "user_contact_way"."contact_way_status__code" is '联系方式状态代码';

comment on column "user_contact_way"."priority" is '优先级';

comment on column "user_contact_way"."remark" is '备注，或其国际化key';

comment on column "user_contact_way"."is_active" is '是否启用';

comment on column "user_contact_way"."is_built_in" is '是否内置';

comment on column "user_contact_way"."create_user" is '创建用户';

comment on column "user_contact_way"."create_time" is '创建时间';

comment on column "user_contact_way"."update_user" is '更新用户';

comment on column "user_contact_way"."update_time" is '更新时间';

create unique index "uq_user_contact_way__user_id_code"
    on "user_contact_way" ("user_id", "contact_way__code");

create table "user_account_protection"
(
    "id"                   CHAR(36)              not null
        primary key,
    "question1"            VARCHAR(64)           not null,
    "answer1"              VARCHAR(64)           not null,
    "question2"            VARCHAR(64),
    "answer2"              VARCHAR(64),
    "question3"            VARCHAR(64),
    "answer3"              VARCHAR(64),
    "safe_contact_way_id"  CHAR(36),
    "total_validate_count" INT4    default 0     not null,
    "match_question_count" INT4    default 1     not null,
    "error_times"          INT4    default 0     not null,
    "remark"               VARCHAR(128),
    "is_active"            BOOLEAN default TRUE  not null,
    "is_built_in"          BOOLEAN default FALSE not null,
    "create_user"          VARCHAR(36),
    "create_time"          TIMESTAMP,
    "update_user"          VARCHAR(36),
    "update_time"          TIMESTAMP
);

comment on table "user_account_protection" is '用户账号保护';

comment on column "user_account_protection"."id" is '主键, 与user_account共用主键';

comment on column "user_account_protection"."question1" is '问题１';

comment on column "user_account_protection"."answer1" is '答案1';

comment on column "user_account_protection"."question2" is '问题2';

comment on column "user_account_protection"."answer2" is '答案2';

comment on column "user_account_protection"."question3" is '问题3';

comment on column "user_account_protection"."answer3" is '答案3';

comment on column "user_account_protection"."safe_contact_way_id" is '安全的联系方式id';

comment on column "user_account_protection"."total_validate_count" is '总的找回密码次数';

comment on column "user_account_protection"."match_question_count" is '必须答对的问题数';

comment on column "user_account_protection"."error_times" is '错误次数';

comment on column "user_account_protection"."remark" is '备注，或其国际化key';

comment on column "user_account_protection"."is_active" is '是否启用';

comment on column "user_account_protection"."is_built_in" is '是否内置';

comment on column "user_account_protection"."create_user" is '创建用户';

comment on column "user_account_protection"."create_time" is '创建时间';

comment on column "user_account_protection"."update_user" is '更新用户';

comment on column "user_account_protection"."update_time" is '更新时间';

create table "geo_ip_library"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "ip_start"    CHAR(39)                       not null
        constraint "uq_geo_ip_library__ip_start"
            unique,
    "ip_end"      CHAR(39)                       not null,
    "country_id"  CHAR(3)                        not null,
    "region_code" VARCHAR(12),
    "isp_name"    VARCHAR(128),
    "is_revised"  BOOLEAN  default FALSE         not null,
    "remark"      VARCHAR(128),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP
);

comment on table "geo_ip_library" is 'ip库';

comment on column "geo_ip_library"."id" is '主键';

comment on column "geo_ip_library"."ip_start" is 'ip段起，标准ipv6全格式';

comment on column "geo_ip_library"."ip_end" is 'ip段止，标准ipv6全格式';

comment on column "geo_ip_library"."country_id" is '国家ip，外键，geo_country表的主键';

comment on column "geo_ip_library"."region_code" is '地区编码';

comment on column "geo_ip_library"."isp_name" is 'isp名称，或其国际化key';

comment on column "geo_ip_library"."is_revised" is '该IP是否是用户修正过';

comment on column "geo_ip_library"."remark" is '备注，或其国际化key';

comment on column "geo_ip_library"."is_active" is '是否启用';

comment on column "geo_ip_library"."is_built_in" is '是否内置';

comment on column "geo_ip_library"."create_user" is '创建用户';

comment on column "geo_ip_library"."create_time" is '创建时间';

comment on column "geo_ip_library"."update_user" is '更新用户';

comment on column "geo_ip_library"."update_time" is '更新时间';

create table "geo_country"
(
    "id"                      CHAR(3)               not null
        primary key,
    "parent_id"               CHAR(3),
    "digital_code"            CHAR(3),
    "letter_code"             CHAR(2)               not null,
    "gec_code"                CHAR(2)               not null,
    "name"                    VARCHAR(32)           not null,
    "english_name"            VARCHAR(64),
    "full_name"               VARCHAR(64),
    "english_full_name"       VARCHAR(64),
    "domain_suffix"           CHAR(2),
    "flag_url"                VARCHAR(128),
    "capital"                 VARCHAR(64),
    "capital_latitude"        VARCHAR(7),
    "capital_longitude"       VARCHAR(7),
    "locale__code" VARCHAR(5),
    "continent_ocean__code"   VARCHAR(16),
    "currency__code"          CHAR(3),
    "calling_code"            VARCHAR(5),
    "timezone_utc"            VARCHAR(32),
    "date_format"             VARCHAR(16),
    "founding_day"            DATE,
    "driving_side__code"      VARCHAR(16),
    "remark"                  VARCHAR(128),
    "is_active"               BOOLEAN default TRUE  not null,
    "is_built_in"             BOOLEAN default FALSE not null,
    "create_user"             VARCHAR(36),
    "create_time"             TIMESTAMP,
    "update_user"             VARCHAR(36),
    "update_time"             TIMESTAMP
);

comment on table "geo_country" is '国家|地区';

comment on column "geo_country"."id" is '主键，国家地区3位字母编码，ISO 3166-1';

comment on column "geo_country"."parent_id" is '从属国家';

comment on column "geo_country"."digital_code" is '国家地区3位数字编码，ISO 3166-1';

comment on column "geo_country"."letter_code" is '国家地区2位字母编码(存在重复项)，ISO 3166-1';

comment on column "geo_country"."gec_code" is '国家地区GEC编码，2位字母编码(存在重复项)';

comment on column "geo_country"."name" is '国家地区名称，或其国际化key';

comment on column "geo_country"."english_name" is '国家地区英文名称';

comment on column "geo_country"."full_name" is '全名，或其国际化key';

comment on column "geo_country"."english_full_name" is '国家地区英文全称';

comment on column "geo_country"."domain_suffix" is '互联网域名后缀';

comment on column "geo_country"."flag_url" is '旗帜url';

comment on column "geo_country"."capital" is '首都/行政中心名称，或其国际化key';

comment on column "geo_country"."capital_latitude" is '首府纬度';

comment on column "geo_country"."capital_longitude" is '首府经度';

comment on column "geo_country"."locale__code" is '官方语言代码';

comment on column "geo_country"."continent_ocean__code" is '所属大洲大洋代码';

comment on column "geo_country"."currency__code" is '币种代码';

comment on column "geo_country"."calling_code" is '国际电话区号';

comment on column "geo_country"."timezone_utc" is 'UTC时区，多个以半角逗号分隔';

comment on column "geo_country"."date_format" is '日期格式';

comment on column "geo_country"."founding_day" is '建国日';

comment on column "geo_country"."driving_side__code" is '驾驶方向代码';

comment on column "geo_country"."remark" is '备注，或其国际化key';

comment on column "geo_country"."is_active" is '是否启用';

comment on column "geo_country"."is_built_in" is '是否内置';

comment on column "geo_country"."create_user" is '创建用户';

comment on column "geo_country"."create_time" is '创建时间';

comment on column "geo_country"."update_user" is '更新用户';

comment on column "geo_country"."update_time" is '更新时间';

create unique index "uq_geo_country__name"
    on "geo_country" ("name");

create table "geo_region"
(
    "id"                      CHAR(36) default RANDOM_UUID() not null
        primary key,
    "country_id"              CHAR(3)                        not null,
    "code"                    VARCHAR(12)                    not null,
    "name"                    VARCHAR(128)                   not null,
    "parent_code"             VARCHAR(12),
    "hierarchy"               CHAR(1),
    "short_name"              VARCHAR(32),
    "alias_name"              VARCHAR(128),
    "postcode"                VARCHAR(6),
    "longitude"               VARCHAR(7),
    "latitude"                VARCHAR(7),
    "calling_code"            VARCHAR(5),
    "license_plate_no_prefix" VARCHAR(5),
    "airport_code"            CHAR(3),
    "remark"                  VARCHAR(128),
    "is_active"               BOOLEAN  default TRUE          not null,
    "is_built_in"             BOOLEAN  default FALSE         not null,
    "create_user"             VARCHAR(36),
    "create_time"             TIMESTAMP,
    "update_user"             VARCHAR(36),
    "update_time"             TIMESTAMP
);

comment on table "geo_region" is '地区';

comment on column "geo_region"."id" is '主键';

comment on column "geo_region"."country_id" is '外键，国家或地区id, geo_country表主键';

comment on column "geo_region"."code" is '区域数字编码，2位省编码+2位市编码+2位县编码';

comment on column "geo_region"."name" is '区域名称，或其国际化key';

comment on column "geo_region"."parent_code" is '父编码';

comment on column "geo_region"."hierarchy" is '层级';

comment on column "geo_region"."short_name" is '区域简称，或其国际化key';

comment on column "geo_region"."alias_name" is '区域别名，或其国际化key';

comment on column "geo_region"."postcode" is '邮政编码';

comment on column "geo_region"."longitude" is '经度';

comment on column "geo_region"."latitude" is '纬度';

comment on column "geo_region"."calling_code" is '电话区号';

comment on column "geo_region"."license_plate_no_prefix" is '车牌号前缀';

comment on column "geo_region"."airport_code" is '机场3位编码';

comment on column "geo_region"."remark" is '备注，或其国际化key';

comment on column "geo_region"."is_active" is '是否启用';

comment on column "geo_region"."is_built_in" is '是否内置';

comment on column "geo_region"."create_user" is '创建用户';

comment on column "geo_region"."create_time" is '创建时间';

comment on column "geo_region"."update_user" is '更新用户';

comment on column "geo_region"."update_time" is '更新时间';

create unique index "uq_geo_region__code_country_id"
    on "geo_region" ("code", "country_id");

create index IDX_GEO_REGION__NAME
    on "geo_region" ("name");

create table "auth_user_group"
(
    "id"          CHAR(36) default RANDOM_UUID() not null
        primary key,
    "group_name"  VARCHAR(64)                    not null,
    "sub_sys__code"       VARCHAR(32),
    "remark"      VARCHAR(128),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    "owner_id" VARCHAR(36)
);

comment on table "auth_user_group" is '用户组';

comment on column "auth_user_group"."id" is '主键';

comment on column "auth_user_group"."group_name" is '用户组名';

comment on column "auth_user_group"."sub_sys__code" is '子系统代码';

comment on column "auth_user_group"."remark" is '备注，或其国际化key';

comment on column "auth_user_group"."is_active" is '是否启用';

comment on column "auth_user_group"."is_built_in" is '是否内置';

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
    "role_name"   VARCHAR(64)                    not null,
    "sub_sys__code"       VARCHAR(32),
    "remark"      VARCHAR(128),
    "is_active"   BOOLEAN  default TRUE          not null,
    "is_built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP,
    "update_user" VARCHAR(36),
    "update_time" TIMESTAMP,
    "owner_id" VARCHAR(36)
);

comment on table "auth_role" is '角色';

comment on column "auth_role"."id" is '主键';

comment on column "auth_role"."role_name" is '角色名';

comment on column "auth_role"."sub_sys__code" is '子系统代码';

comment on column "auth_role"."remark" is '备注，或其国际化key';

comment on column "auth_role"."is_active" is '是否启用';

comment on column "auth_role"."is_built_in" is '是否内置';

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




CREATE TABLE "msg_template" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "send_type__code" varchar(6) NOT NULL,
  "event_type__code" varchar(32) NOT NULL,
  "msg_type__code" varchar(16) NOT NULL,
  "group_code" char(36),
  "locale__code" varchar(5),
  "title" varchar(128),
  "content" varchar,
  "is_default_active" bool NOT NULL DEFAULT false,
  "default_title" varchar(128),
  "default_content" varchar,
  "owner_id" VARCHAR(36)
);

COMMENT ON TABLE "msg_template" IS '消息模板';
COMMENT ON COLUMN "msg_template"."id" IS '主键';
COMMENT ON COLUMN "msg_template"."msg_type__code" IS '发送类型代码';
COMMENT ON COLUMN "msg_template"."event_type__code" IS '事件类型代码。send_type__code为auto时，字典类型为auto_event_type;为manual时，则为manual_event_type';
COMMENT ON COLUMN "msg_template"."msg_type__code" IS '消息类型代码';
COMMENT ON COLUMN "msg_template"."group_code" IS '模板分组编码,uuid,用于区分同一事件下不同操作原因的多套模板';
COMMENT ON COLUMN "msg_template"."locale__code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_template"."title" IS '模板标题';
COMMENT ON COLUMN "msg_template"."content" IS '模板内容';
COMMENT ON COLUMN "msg_template"."is_default_active" IS '是否启用默认值';
COMMENT ON COLUMN "msg_template"."default_title" IS '模板标题默认值';
COMMENT ON COLUMN "msg_template"."default_content" IS '模板内容默认值';
COMMENT ON COLUMN "msg_template"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "msg_instance" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "locale__code" varchar(5),
  "title" varchar(128),
  "content" varchar,
  "template_id" CHAR(36),
  "send_type__code" varchar(6),
  "event_type__code" varchar(32),
  "msg_type__code" varchar(16),
  "valid_time_start" TIMESTAMP default now() not null,
  "valid_time_end" TIMESTAMP default (now()+99999) not null,
  "owner_id" VARCHAR(36),
  constraint "fk_msg_instance"
          foreign key ("template_id") references "msg_template" ("id")
);

COMMENT ON TABLE "msg_instance" IS '消息实例';
COMMENT ON COLUMN "msg_instance"."id" IS '主键';
COMMENT ON COLUMN "msg_instance"."locale__code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_instance"."title" IS '标题，可能还含有用户名等实际要发送时才能确定的模板变量';
COMMENT ON COLUMN "msg_instance"."content" IS '通知内容，可能还含有用户名等实际要发送时才能确定的模板变量';
COMMENT ON COLUMN "msg_instance"."template_id" IS '消息模板id，为null时表示没有依赖静态模板，可能是依赖动态模板或无模板';
COMMENT ON COLUMN "msg_instance"."send_type__code" IS '发送类型代码';
COMMENT ON COLUMN "msg_instance"."event_type__code" IS '事件类型代码';
COMMENT ON COLUMN "msg_instance"."msg_type__code" IS '消息类型代码';
COMMENT ON COLUMN "msg_instance"."valid_time_start" IS '有效期起';
COMMENT ON COLUMN "msg_instance"."valid_time_end" IS '有效期止';
COMMENT ON COLUMN "msg_instance"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "msg_receiver_group" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "receiver_group_type__code" varchar(16) not null,
  "define_table" varchar(64) not null,
  "name_column" varchar(64) not null,
  "remark"      VARCHAR(128),
  "is_active"   BOOLEAN  default TRUE          not null,
  "is_built_in" BOOLEAN  default FALSE         not null,
  "create_user" VARCHAR(36),
  "create_time" TIMESTAMP,
  "update_user" VARCHAR(36),
  "update_time" TIMESTAMP
);

create unique index "uq_msg_receiver_group__type_code" on "msg_receiver_group" ("receiver_group_type__code");

COMMENT ON TABLE "msg_receiver_group" IS '消息接收者群组';
COMMENT ON COLUMN "msg_receiver_group"."id" IS '主键';
COMMENT ON COLUMN "msg_receiver_group"."receiver_group_type__code" IS '接收者群组类型代码';
COMMENT ON COLUMN "msg_receiver_group"."define_table" IS '群组定义的表';
COMMENT ON COLUMN "msg_receiver_group"."name_column" IS '群组名称在具体群组表中的字段名';
comment on column "msg_receiver_group"."remark" is '备注，或其国际化key';
comment on column "msg_receiver_group"."is_active" is '是否启用';
comment on column "msg_receiver_group"."is_built_in" is '是否内置';
comment on column "msg_receiver_group"."create_user" is '创建用户';
comment on column "msg_receiver_group"."create_time" is '创建时间';
comment on column "msg_receiver_group"."update_user" is '更新用户';
comment on column "msg_receiver_group"."update_time" is '更新时间';


CREATE TABLE "msg_send" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "receiver_group_type__code" varchar(16) NOT NULL,
  "receiver_group_id" VARCHAR(36),
  "instance_id" CHAR(36) NOT NULL,
  "msg_type__code" varchar(16) NOT NULL,
  "locale__code" varchar(5),
  "send_status__code" varchar(2) NOT NULL,
  "create_time" timestamp NOT NULL,
  "update_time" timestamp,
  "success_count" int4 DEFAULT 0,
  "fail_count" int4 DEFAULT 0,
  "job_id" varchar(36),
  "owner_id" VARCHAR(36),
  constraint "fk_msg_send"
            foreign key ("instance_id") references "msg_instance" ("id")
);

COMMENT ON TABLE "msg_send" IS '消息发送';
COMMENT ON COLUMN "msg_send"."id" IS '主键';
COMMENT ON COLUMN "msg_send"."receiver_group_type__code" IS '接收者群组类型代码';
COMMENT ON COLUMN "msg_send"."receiver_group_id" IS '接收者群组id';
COMMENT ON COLUMN "msg_send"."instance_id" IS '消息实例id';
COMMENT ON COLUMN "msg_send"."msg_type__code" IS '消息类型代码';
COMMENT ON COLUMN "msg_send"."locale__code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_send"."send_status__code" IS '发送状态代码';
COMMENT ON COLUMN "msg_send"."create_time" IS '创建时间';
COMMENT ON COLUMN "msg_send"."update_time" IS '更新时间';
COMMENT ON COLUMN "msg_send"."success_count" IS '发送成功数量';
COMMENT ON COLUMN "msg_send"."fail_count" IS '发送失败数量';
COMMENT ON COLUMN "msg_send"."job_id" IS '定时任务id';
COMMENT ON COLUMN "msg_send"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "msg_site_msg_receive" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "receiver_id" CHAR(36) NOT NULL,
  "send_id" CHAR(36) NOT NULL,
  "receive_status__code" varchar(2) NOT NULL,
  "create_time" timestamp NOT NULL,
  "update_time" timestamp,
  "owner_id" VARCHAR(36),
  constraint "fk_msg_site_msg_receive"
              foreign key ("send_id") references "msg_send" ("id")
);
COMMENT ON TABLE "msg_site_msg_receive" IS '消息接收';
COMMENT ON COLUMN "msg_site_msg_receive"."id" IS '主键';
COMMENT ON COLUMN "msg_site_msg_receive"."receiver_id" IS '接收者id';
COMMENT ON COLUMN "msg_site_msg_receive"."send_id" IS '发送id';
COMMENT ON COLUMN "msg_site_msg_receive"."receive_status__code" IS '接收状态代码';
COMMENT ON COLUMN "msg_site_msg_receive"."create_time" IS '创建时间';
COMMENT ON COLUMN "msg_site_msg_receive"."update_time" IS '更新时间';
COMMENT ON COLUMN "msg_site_msg_receive"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "code_gen_file" (
  "id" CHAR(36) default RANDOM_UUID() not null primary key,
  "filename" varchar(64) NOT NULL,
  "object_name" varchar(64) NOT NULL
);
COMMENT ON TABLE "code_gen_file" IS '代码生成-文件信息';
COMMENT ON COLUMN "code_gen_file"."id" IS '主键';
COMMENT ON COLUMN "code_gen_file"."filename" IS '文件名';
COMMENT ON COLUMN "code_gen_file"."object_name" IS '对象名';

CREATE TABLE "code_gen_object" (
  "id" CHAR(36) default RANDOM_UUID() not null primary key,
  "name" varchar(64) NOT NULL,
  "comment" varchar(128),
  "create_time" timestamp default now() NOT NULL,
  "create_user" varchar(36) NOT NULL,
  "update_time" timestamp,
  "update_user" varchar(36),
  "gen_count" int4 NOT NULL
);
COMMENT ON TABLE "code_gen_object" IS '代码生成-对象信息';
COMMENT ON COLUMN "code_gen_object"."id" IS '主键';
COMMENT ON COLUMN "code_gen_object"."name" IS '对象名称';
COMMENT ON COLUMN "code_gen_object"."comment" IS '注释';
COMMENT ON COLUMN "code_gen_object"."create_time" IS '创建时间';
COMMENT ON COLUMN "code_gen_object"."create_user" IS '创建用户';
COMMENT ON COLUMN "code_gen_object"."update_time" IS '更新时间';
COMMENT ON COLUMN "code_gen_object"."update_user" IS '更新用户';
COMMENT ON COLUMN "code_gen_object"."gen_count" IS '生成次数';


CREATE TABLE "code_gen_column" (
  "id" CHAR(36) default RANDOM_UUID() not null primary key,
  "name" varchar(64) NOT NULL,
  "object_name" varchar(64) NOT NULL,
  "comment" varchar(128),
  "is_searchable" bool NOT NULL DEFAULT false,
  "is_sortable" bool NOT NULL DEFAULT false,
  "order_in_list" tinyint,
  "default_order" tinyint,
  "order_in_edit" tinyint,
  "order_in_view" tinyint
);
COMMENT ON TABLE "code_gen_column" IS '代码生成-列信息';
COMMENT ON COLUMN "code_gen_column"."id" IS '主键';
COMMENT ON COLUMN "code_gen_column"."name" IS '字段名';
COMMENT ON COLUMN "code_gen_column"."object_name" IS '对象名称';
COMMENT ON COLUMN "code_gen_column"."comment" IS '注释';
COMMENT ON COLUMN "code_gen_column"."is_searchable" IS '是否可查询';
COMMENT ON COLUMN "code_gen_column"."is_sortable" IS '是否列表中可排序';
COMMENT ON COLUMN "code_gen_column"."order_in_list" IS '列表中列序';
COMMENT ON COLUMN "code_gen_column"."default_order" IS '列表默认排序';
COMMENT ON COLUMN "code_gen_column"."order_in_edit" IS '编辑页中的顺序';
COMMENT ON COLUMN "code_gen_column"."order_in_view" IS '详情页中的顺序';


