CREATE TABLE "msg_template" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "send_type_dict_code" varchar(6) NOT NULL,
  "event_type_dict_code" VARCHAR(31) NOT NULL,
  "msg_type_dict_code" VARCHAR(15) NOT NULL,
  "group_code" char(36),
  "locale_dict_code" varchar(5),
  "title" VARCHAR(127),
  "content" varchar,
  "is_default_active" bool NOT NULL DEFAULT false,
  "default_title" VARCHAR(127),
  "default_content" varchar,
  "owner_id" VARCHAR(36)
);

COMMENT ON TABLE "msg_template" IS '消息模板';
COMMENT ON COLUMN "msg_template"."id" IS '主键';
COMMENT ON COLUMN "msg_template"."msg_type_dict_code" IS '发送类型代码';
COMMENT ON COLUMN "msg_template"."event_type_dict_code" IS '事件类型代码。send_type_dict_code为auto时，字典类型为auto_event_type;为manual时，则为manual_event_type';
COMMENT ON COLUMN "msg_template"."msg_type_dict_code" IS '消息类型代码';
COMMENT ON COLUMN "msg_template"."group_code" IS '模板分组编码,uuid,用于区分同一事件下不同操作原因的多套模板';
COMMENT ON COLUMN "msg_template"."locale_dict_code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_template"."title" IS '模板标题';
COMMENT ON COLUMN "msg_template"."content" IS '模板内容';
COMMENT ON COLUMN "msg_template"."is_default_active" IS '是否启用默认值';
COMMENT ON COLUMN "msg_template"."default_title" IS '模板标题默认值';
COMMENT ON COLUMN "msg_template"."default_content" IS '模板内容默认值';
COMMENT ON COLUMN "msg_template"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "msg_instance" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "locale_dict_code" varchar(5),
  "title" VARCHAR(127),
  "content" varchar,
  "template_id" CHAR(36),
  "send_type_dict_code" varchar(6),
  "event_type_dict_code" VARCHAR(31),
  "msg_type_dict_code" VARCHAR(15),
  "valid_time_start" TIMESTAMP default now() not null,
  "valid_time_end" TIMESTAMP default (now()+99999) not null,
  "owner_id" VARCHAR(36),
  constraint "fk_msg_instance"
          foreign key ("template_id") references "msg_template" ("id")
);

COMMENT ON TABLE "msg_instance" IS '消息实例';
COMMENT ON COLUMN "msg_instance"."id" IS '主键';
COMMENT ON COLUMN "msg_instance"."locale_dict_code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_instance"."title" IS '标题，可能还含有用户名等实际要发送时才能确定的模板变量';
COMMENT ON COLUMN "msg_instance"."content" IS '通知内容，可能还含有用户名等实际要发送时才能确定的模板变量';
COMMENT ON COLUMN "msg_instance"."template_id" IS '消息模板id，为null时表示没有依赖静态模板，可能是依赖动态模板或无模板';
COMMENT ON COLUMN "msg_instance"."send_type_dict_code" IS '发送类型代码';
COMMENT ON COLUMN "msg_instance"."event_type_dict_code" IS '事件类型代码';
COMMENT ON COLUMN "msg_instance"."msg_type_dict_code" IS '消息类型代码';
COMMENT ON COLUMN "msg_instance"."valid_time_start" IS '有效期起';
COMMENT ON COLUMN "msg_instance"."valid_time_end" IS '有效期止';
COMMENT ON COLUMN "msg_instance"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';



CREATE TABLE "msg_receiver_group" (
  "id"       CHAR(36) default RANDOM_UUID() not null primary key,
  "receiver_group_type_dict_code" VARCHAR(15) not null,
  "define_table" VARCHAR(63) not null,
  "name_column" VARCHAR(63) not null,
  "remark"      VARCHAR(127),
  "is_active"   BOOLEAN  default TRUE          not null,
  "is_built_in" BOOLEAN  default FALSE         not null,
  "create_user" VARCHAR(36),
  "create_time" TIMESTAMP  default now() not null,
  "update_user" VARCHAR(36),
  "update_time" TIMESTAMP
);

create unique index "uq_msg_receiver_group__type_code" on "msg_receiver_group" ("receiver_group_type_dict_code");

COMMENT ON TABLE "msg_receiver_group" IS '消息接收者群组';
COMMENT ON COLUMN "msg_receiver_group"."id" IS '主键';
COMMENT ON COLUMN "msg_receiver_group"."receiver_group_type_dict_code" IS '接收者群组类型代码';
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
  "receiver_group_type_dict_code" VARCHAR(15) NOT NULL,
  "receiver_group_id" VARCHAR(36),
  "instance_id" CHAR(36) NOT NULL,
  "msg_type_dict_code" VARCHAR(15) NOT NULL,
  "locale_dict_code" varchar(5),
  "send_status_dict_code" varchar(2) NOT NULL,
  "create_time" timestamp default now() NOT NULL,
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
COMMENT ON COLUMN "msg_send"."receiver_group_type_dict_code" IS '接收者群组类型代码';
COMMENT ON COLUMN "msg_send"."receiver_group_id" IS '接收者群组id';
COMMENT ON COLUMN "msg_send"."instance_id" IS '消息实例id';
COMMENT ON COLUMN "msg_send"."msg_type_dict_code" IS '消息类型代码';
COMMENT ON COLUMN "msg_send"."locale_dict_code" IS '国家-语言代码';
COMMENT ON COLUMN "msg_send"."send_status_dict_code" IS '发送状态代码';
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
  "receive_status_dict_code" varchar(2) NOT NULL,
  "create_time" timestamp default now() NOT NULL,
  "update_time" timestamp,
  "owner_id" VARCHAR(36),
  constraint "fk_msg_site_msg_receive"
              foreign key ("send_id") references "msg_send" ("id")
);
COMMENT ON TABLE "msg_site_msg_receive" IS '消息接收';
COMMENT ON COLUMN "msg_site_msg_receive"."id" IS '主键';
COMMENT ON COLUMN "msg_site_msg_receive"."receiver_id" IS '接收者id';
COMMENT ON COLUMN "msg_site_msg_receive"."send_id" IS '发送id';
COMMENT ON COLUMN "msg_site_msg_receive"."receive_status_dict_code" IS '接收状态代码';
COMMENT ON COLUMN "msg_site_msg_receive"."create_time" IS '创建时间';
COMMENT ON COLUMN "msg_site_msg_receive"."update_time" IS '更新时间';
COMMENT ON COLUMN "msg_site_msg_receive"."owner_id" IS '所有者id，依业务可以是店铺id、站点id、商户id等';