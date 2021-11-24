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
    "isp_name"    VARCHAR(127),
    "revised"  BOOLEAN  default FALSE         not null,
    "remark"      VARCHAR(127),
    "active"   BOOLEAN  default TRUE          not null,
    "built_in" BOOLEAN  default FALSE         not null,
    "create_user" VARCHAR(36),
    "create_time" TIMESTAMP  default now() not null,
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

comment on column "geo_ip_library"."revised" is '该IP是否是用户修正过';

comment on column "geo_ip_library"."remark" is '备注，或其国际化key';

comment on column "geo_ip_library"."active" is '是否启用';

comment on column "geo_ip_library"."built_in" is '是否内置';

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
    "name"                    VARCHAR(31)           not null,
    "english_name"            VARCHAR(63),
    "full_name"               VARCHAR(63),
    "english_full_name"       VARCHAR(63),
    "domain_suffix"           CHAR(2),
    "flag_url"                VARCHAR(127),
    "capital"                 VARCHAR(63),
    "capital_latitude"        VARCHAR(7),
    "capital_longitude"       VARCHAR(7),
    "locale_dict_code" VARCHAR(5),
    "continent_ocean_dict_code"   VARCHAR(15),
    "currency_dict_code"          CHAR(3),
    "calling_code"            VARCHAR(5),
    "timezone_utc"            VARCHAR(31),
    "date_format"             VARCHAR(15),
    "founding_day"            DATE,
    "driving_side_dict_code"      VARCHAR(15),
    "remark"                  VARCHAR(127),
    "active"               BOOLEAN default TRUE  not null,
    "built_in"             BOOLEAN default FALSE not null,
    "create_user"             VARCHAR(36),
    "create_time"             TIMESTAMP  default now(),
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

comment on column "geo_country"."locale_dict_code" is '官方语言代码';

comment on column "geo_country"."continent_ocean_dict_code" is '所属大洲大洋代码';

comment on column "geo_country"."currency_dict_code" is '币种代码';

comment on column "geo_country"."calling_code" is '国际电话区号';

comment on column "geo_country"."timezone_utc" is 'UTC时区，多个以半角逗号分隔';

comment on column "geo_country"."date_format" is '日期格式';

comment on column "geo_country"."founding_day" is '建国日';

comment on column "geo_country"."driving_side_dict_code" is '驾驶方向代码';

comment on column "geo_country"."remark" is '备注，或其国际化key';

comment on column "geo_country"."active" is '是否启用';

comment on column "geo_country"."built_in" is '是否内置';

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
    "name"                    VARCHAR(127)                   not null,
    "parent_code"             VARCHAR(12),
    "hierarchy"               CHAR(1),
    "short_name"              VARCHAR(31),
    "alias_name"              VARCHAR(127),
    "postcode"                VARCHAR(6),
    "longitude"               VARCHAR(7),
    "latitude"                VARCHAR(7),
    "calling_code"            VARCHAR(5),
    "license_plate_no_prefix" VARCHAR(5),
    "airport_code"            CHAR(3),
    "remark"                  VARCHAR(127),
    "active"               BOOLEAN  default TRUE          not null,
    "built_in"             BOOLEAN  default FALSE         not null,
    "create_user"             VARCHAR(36),
    "create_time"             TIMESTAMP  default now() not null,
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

comment on column "geo_region"."active" is '是否启用';

comment on column "geo_region"."built_in" is '是否内置';

comment on column "geo_region"."create_user" is '创建用户';

comment on column "geo_region"."create_time" is '创建时间';

comment on column "geo_region"."update_user" is '更新用户';

comment on column "geo_region"."update_time" is '更新时间';

create unique index "uq_geo_region_dict_code_country_id"
    on "geo_region" ("code", "country_id");

create index IDX_GEO_REGION__NAME
    on "geo_region" ("name");