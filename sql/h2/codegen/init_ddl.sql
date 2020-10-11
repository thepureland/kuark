CREATE TABLE "code_gen_file" (
  "id" CHAR(36) default RANDOM_UUID() not null primary key,
  "filename" VARCHAR(63) NOT NULL,
  "object_name" VARCHAR(63) NOT NULL
);
COMMENT ON TABLE "code_gen_file" IS '代码生成-文件信息';
COMMENT ON COLUMN "code_gen_file"."id" IS '主键';
COMMENT ON COLUMN "code_gen_file"."filename" IS '文件名';
COMMENT ON COLUMN "code_gen_file"."object_name" IS '对象名';


CREATE TABLE "code_gen_object" (
  "id" CHAR(36) default RANDOM_UUID() not null primary key,
  "name" VARCHAR(63) NOT NULL,
  "comment" VARCHAR(127),
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
  "name" VARCHAR(63) NOT NULL,
  "object_name" VARCHAR(63) NOT NULL,
  "comment" VARCHAR(127),
  "is_searchable" bool NOT NULL DEFAULT false,
  "is_sortable" bool NOT NULL DEFAULT false,
  "order_in_list" tinyint,
  "default_order" VARCHAR(7),
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