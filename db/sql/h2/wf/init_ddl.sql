create table "flow_form"
(
    "id"                 CHAR(36)  default RANDOM_UUID() not null primary key,
    "key"                VARCHAR(64)                     not null,
    "version_"           SMALLINT                        not null,
    "category_dict_code" VARCHAR(64)                     not null,
    "name"               VARCHAR(64)                     not null,
    "path"               VARCHAR(255)                    not null,
    "content"            CLOB                            not null,
    "remark"             VARCHAR(127),
    "built_in"        BOOLEAN   default FALSE         not null,
    "create_user"        VARCHAR(36),
    "create_time"        TIMESTAMP default now(),
    "update_user"        VARCHAR(36),
    "update_time"        TIMESTAMP
);

comment on table "flow_form" is '工作流表单';

comment on column "flow_form"."id" is '主键';

comment on column "flow_form"."key" is '表单key';

comment on column "flow_form"."version_" is '版本';

comment on column "flow_form"."category_dict_code" is '分类字典代码';

comment on column "flow_form"."name" is '名称';

comment on column "flow_form"."path" is '路径';

comment on column "flow_form"."content" is '内容';

comment on column "flow_form"."remark" is '备注';

comment on column "flow_form"."built_in" is '是否内置';

comment on column "flow_form"."create_user" is '创建用户';

comment on column "flow_form"."create_time" is '创建时间';

comment on column "flow_form"."update_user" is '更新用户';

comment on column "flow_form"."update_time" is '更新时间';
