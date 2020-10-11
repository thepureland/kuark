create table "user_db_audit_log"
(
    "id"                 CHAR(36) default RANDOM_UUID() not null
        primary key,
    "user_account_id"    CHAR(36)                       not null,
    "table_name"         VARCHAR(63)                    not null,
    "operate_time"       TIMESTAMP                      not null,
    "operate_type_dict_code" CHAR(1)                        not null,
    constraint "fk_user_db_audit_log"
        foreign key ("user_account_id") references "auth_user_account" ("id")
);

comment on table "user_db_audit_log" is '用户数据库操作审计日志';

comment on column "user_db_audit_log"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "user_db_audit_log"."table_name" is '表名';

comment on column "user_db_audit_log"."operate_time" is '操作时间';

comment on column "user_db_audit_log"."operate_type_dict_code" is '操作类型代码';

create table "user_db_audit_log_item"
(
    "id"              CHAR(36) default RANDOM_UUID() not null
        primary key,
    "db_audit_log_id" CHAR(36)                       not null,
    "record_id"       VARCHAR(36)                    not null,
    "column_name"     VARCHAR(63),
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
    "identity_type_dict_code"   VARCHAR(15)                    not null,
    "identity_type"         VARCHAR(31)                    not null,
    "identifier"            VARCHAR(63)                    not null,
    "sub_sys_dict_code"         VARCHAR(31),
    "sub_sys"               VARCHAR(63),
    "module"                VARCHAR(63),
    "operate_time"          TIMESTAMP                      not null,
    "client_ip"             CHAR(39),
    "client_ip_isp"         VARCHAR(127),
    "client_region_code"    VARCHAR(12),
    "client_region_name"    VARCHAR(127),
    "client_terminal_dict_code" VARCHAR(15),
    "client_terminal"       VARCHAR(31),
    "client_os"             VARCHAR(63),
    "client_browser"        VARCHAR(63),
    constraint "fk_user_biz_audit_log"
        foreign key ("user_account_id") references "auth_user_account" ("id")
);

comment on table "user_biz_audit_log" is '用户审计日志';

comment on column "user_biz_audit_log"."user_account_id" is '外键，用户账号id，user_account表主键';

comment on column "user_biz_audit_log"."identity_type_dict_code" is '身份类型代码';

comment on column "user_biz_audit_log"."identity_type" is '身份类型';

comment on column "user_biz_audit_log"."identifier" is '唯一身份标识';

comment on column "user_biz_audit_log"."sub_sys_dict_code" is '子系统代码';

comment on column "user_biz_audit_log"."sub_sys" is '子系统';

comment on column "user_biz_audit_log"."module" is '模块';

comment on column "user_biz_audit_log"."operate_time" is '操作时间';

comment on column "user_biz_audit_log"."client_ip" is '客户端ip，标准全格式ipv6';

comment on column "user_biz_audit_log"."client_ip_isp" is '客户端ip的isp';

comment on column "user_biz_audit_log"."client_region_code" is '客户端区域编码';

comment on column "user_biz_audit_log"."client_region_name" is '客户端区域';

comment on column "user_biz_audit_log"."client_terminal_dict_code" is '客户端终端类型代码';

comment on column "user_biz_audit_log"."client_terminal" is '客户端终端类型';

comment on column "user_biz_audit_log"."client_os" is '客户端操作系统';

comment on column "user_biz_audit_log"."client_browser" is '客户端浏览器';

create table "user_personal_info"
(
    "id"                        CHAR(36)              not null
        primary key,
    "real_name"                 VARCHAR(63),
    "nickname"                  VARCHAR(63),
    "sex_dict_code"                 CHAR(1) default '9'   not null,
    "birthday"                  DATE,
    "id_card_no"                VARCHAR(31),
    "constellation_dict_code"       VARCHAR(11),
    "country_id"                CHAR(3),
    "nation_dict_code"              VARCHAR(3),
    "region_code"               VARCHAR(12),
    "user_status_dict_code"         CHAR(2),
    "user_status_reason"        VARCHAR(127),
    "user_type_dict_code"           CHAR(2),
    "avatar_url"                VARCHAR(127),
    "sub_sys_dict_code"             VARCHAR(31),
    "freeze_time_start"         TIMESTAMP,
    "freeze_time_end"           TIMESTAMP,
    "last_login_time"           TIMESTAMP,
    "last_logout_time"          TIMESTAMP,
    "last_login_ip"             CHAR(39),
    "last_login_terminal_dict_code" VARCHAR(15),
    "total_online_time"         REAL,
    "register_ip"               CHAR(39),
    "register_url"              VARCHAR(127),
    "dynamic_auth_key"          VARCHAR(63),
    "remark"                    VARCHAR(127),
    "is_active"                 BOOLEAN default TRUE  not null,
    "is_built_in"               BOOLEAN default FALSE not null,
    "create_user"               VARCHAR(36),
    "create_time"               TIMESTAMP  default now() not null,
    "update_user"               VARCHAR(36),
    "update_time"               TIMESTAMP
);

comment on table "user_personal_info" is '用户个人信息';

comment on column "user_personal_info"."id" is '主键，与user_account共用主键';

comment on column "user_personal_info"."real_name" is '真实姓名';

comment on column "user_personal_info"."nickname" is '昵称';

comment on column "user_personal_info"."sex_dict_code" is '性别代码';

comment on column "user_personal_info"."birthday" is '生日';

comment on column "user_personal_info"."id_card_no" is '身份证号';

comment on column "user_personal_info"."constellation_dict_code" is '星座代码';

comment on column "user_personal_info"."country_id" is '国家id';

comment on column "user_personal_info"."nation_dict_code" is '民族代码';

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
    "contact_way_dict_code"        CHAR(3)                        not null,
    "contact_way_value"        VARCHAR(127)                   not null,
    "contact_way_status_dict_code" CHAR(2)  default '00'          not null,
    "priority"                 TINYINT,
    "remark"                   VARCHAR(127),
    "is_active"                BOOLEAN  default TRUE          not null,
    "is_built_in"              BOOLEAN  default FALSE         not null,
    "create_user"              VARCHAR(36),
    "create_time"              TIMESTAMP  default now() not null,
    "update_user"              VARCHAR(36),
    "update_time"              TIMESTAMP,
    constraint "fk_user_contact_way"
        foreign key ("user_id") references "auth_user_account" ("id")
);

comment on table "user_contact_way" is '用户联系方式';

comment on column "user_contact_way"."user_id" is '外键，用户账号id，user_account表主键';

comment on column "user_contact_way"."contact_way_dict_code" is '联系方式代码';

comment on column "user_contact_way"."contact_way_value" is '联系方式值';

comment on column "user_contact_way"."contact_way_status_dict_code" is '联系方式状态代码';

comment on column "user_contact_way"."priority" is '优先级';

comment on column "user_contact_way"."remark" is '备注，或其国际化key';

comment on column "user_contact_way"."is_active" is '是否启用';

comment on column "user_contact_way"."is_built_in" is '是否内置';

comment on column "user_contact_way"."create_user" is '创建用户';

comment on column "user_contact_way"."create_time" is '创建时间';

comment on column "user_contact_way"."update_user" is '更新用户';

comment on column "user_contact_way"."update_time" is '更新时间';

create unique index "uq_user_contact_way__user_id_code"
    on "user_contact_way" ("user_id", "contact_way_dict_code");

create table "user_account_protection"
(
    "id"                   CHAR(36)              not null
        primary key,
    "question1"            VARCHAR(63)           not null,
    "answer1"              VARCHAR(63)           not null,
    "question2"            VARCHAR(63),
    "answer2"              VARCHAR(63),
    "question3"            VARCHAR(63),
    "answer3"              VARCHAR(63),
    "safe_contact_way_id"  CHAR(36),
    "total_validate_count" INT4    default 0     not null,
    "match_question_count" INT4    default 1     not null,
    "error_times"          INT4    default 0     not null,
    "remark"               VARCHAR(127),
    "is_active"            BOOLEAN default TRUE  not null,
    "is_built_in"          BOOLEAN default FALSE not null,
    "create_user"          VARCHAR(36),
    "create_time"          TIMESTAMP  default now() not null,
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