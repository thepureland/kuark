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

create unique index "uq_sys_param__param_name_module" on "sys_param" ("param_name", "module");

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

create table "sys_datasource"
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



create table "user_account"
(
    "id"                        CHAR(36) default RANDOM_UUID() not null primary key,
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
    "update_time"               TIMESTAMP
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


create table "user_account_auth"
(
    "id"                        CHAR(36) default RANDOM_UUID() not null primary key,
    "user_account_id"           CHAR(36) not null,
    "identity_type__code"       VARCHAR(16) not null,
    "identifier"                VARCHAR(64) not null,
    "sub_sys__code"             VARCHAR(32),
    "credential"                VARCHAR(64) not null,
    "is_verified"               BOOLEAN  default FALSE         not null,
    "remark"                    VARCHAR(128),
    "is_active"                 BOOLEAN  default TRUE          not null,
    "is_built_in"               BOOLEAN  default FALSE         not null,
    "create_user"               VARCHAR(36),
    "create_time"               TIMESTAMP,
    "update_user"               VARCHAR(36),
    "update_time"               TIMESTAMP,
    constraint "fk_user_account_auth"
        foreign key ("user_account_id") references "user_account_auth" ("id")
);

create unique index "uq_u_a_a__identifier_identity_type_sub_sys" on "user_account_auth" ("identifier", "identity_type__code", "sub_sys__code");

comment on table "user_account_auth" is '用户账号授权';
comment on column "user_account_auth"."user_account_id" is '外键，用户账号id，user_account表主键';
comment on column "user_account_auth"."identity_type__code" is '登陆类型代码';
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


create table "user_personal_info"
(
    "id"                        CHAR(36) not null primary key,
    "real_name"                 VARCHAR(64),
    "nickname"                  VARCHAR(64),
    "sex__code"                 CHAR(1)  default '9'           not null,
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
    "is_active"                 BOOLEAN  default TRUE          not null,
    "is_built_in"               BOOLEAN  default FALSE         not null,
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
    "id"                      CHAR(3)               not null        primary key,
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
    "official_language__code" VARCHAR(5),
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

create unique index "uq_geo_country__name" on "geo_country" ("name");

comment on table "geo_country" is '国家|地区';

comment on column "geo_country"."id" is '主键，国家地区3位字母编码，ISO 3166-1';

comment on column "geo_country"."parent_id" is '从属国家';

comment on column "geo_country"."digital_code" is '国家地区3位数字编码，ISO 3166-1';

comment on column "geo_country"."letter_code" is '国家地区2位字母编码(存在重复项)，ISO 3166-1';

comment on column "geo_country"."gec_code" is '国家地区GEC编码，2位字母编码(存在重复项)';

comment on column "geo_country"."name" is '国家地区名称，或其国际化key';

comment on column "geo_country"."english_name" is '国家地区英文名称';

comment on column "geo_country"."english_full_name" is '国家地区英文全称';

comment on column "geo_country"."full_name" is '全名，或其国际化key';

comment on column "geo_country"."domain_suffix" is '互联网域名后缀';

comment on column "geo_country"."flag_url" is '旗帜url';

comment on column "geo_country"."capital" is '首都/行政中心名称，或其国际化key';

comment on column "geo_country"."capital_latitude" is '首府纬度';

comment on column "geo_country"."capital_longitude" is '首府经度';

comment on column "geo_country"."official_language__code" is '官方语言代码';

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

create table "geo_region"
(
    "id"           CHAR(36) default RANDOM_UUID() not null        primary key,
    "country_id"   CHAR(3)                        not null,
    "code"         VARCHAR(12)                        not null,
    "name"         VARCHAR(128)                    not null,
    "parent_code"  VARCHAR(12),
    "hierarchy"    char(1),
    "short_name"   VARCHAR(32),
    "alias_name"   VARCHAR(128),
    "postcode"     VARCHAR(6),
    "longitude"    VARCHAR(7),
    "latitude"     VARCHAR(7),
    "calling_code" VARCHAR(5),
    "license_plate_no_prefix" VARCHAR(5),
    "airport_code"  CHAR(3),
    "remark"       VARCHAR(128),
    "is_active"    BOOLEAN  default TRUE          not null,
    "is_built_in"  BOOLEAN  default FALSE         not null,
    "create_user"  VARCHAR(36),
    "create_time"  TIMESTAMP,
    "update_user"  VARCHAR(36),
    "update_time"  TIMESTAMP
);

create unique index "uq_geo_region__code_country_id" on "geo_region" ("code", "country_id");
create index idx_geo_region__name on "geo_region" ("name");

comment on table "geo_region" is '地区';

comment on column "geo_region"."id" is '主键';

comment on column "geo_region"."country_id" is '外键，国家或地区id, geo_country表主键';

comment on column "geo_region"."code" is '区域数字编码，2位省编码+2位市编码+2位县编码';

comment on column "geo_region"."parent_code" is '父编码';

comment on column "geo_region"."hierarchy" is '层级';

comment on column "geo_region"."airport_code" is '机场3位编码';

comment on column "geo_region"."name" is '区域名称，或其国际化key';

comment on column "geo_region"."short_name" is '区域简称，或其国际化key';

comment on column "geo_region"."alias_name" is '区域别名，或其国际化key';

comment on column "geo_region"."postcode" is '邮政编码';

comment on column "geo_region"."latitude" is '纬度';

comment on column "geo_region"."longitude" is '经度';

comment on column "geo_region"."calling_code" is '电话区号';

comment on column "geo_region"."license_plate_no_prefix" is '车牌号前缀';

comment on column "geo_region"."remark" is '备注，或其国际化key';

comment on column "geo_region"."is_active" is '是否启用';

comment on column "geo_region"."is_built_in" is '是否内置';

comment on column "geo_region"."create_user" is '创建用户';

comment on column "geo_region"."create_time" is '创建时间';

comment on column "geo_region"."update_user" is '更新用户';

comment on column "geo_region"."update_time" is '更新时间';

