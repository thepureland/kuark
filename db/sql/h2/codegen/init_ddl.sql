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
  "search_item" bool NOT NULL DEFAULT false,
  "list_item" bool NOT NULL DEFAULT false,
  "edit_item" bool NOT NULL DEFAULT false,
  "detail_item" bool NOT NULL DEFAULT false
);
COMMENT ON TABLE "code_gen_column" IS '代码生成-列信息';
COMMENT ON COLUMN "code_gen_column"."id" IS '主键';
COMMENT ON COLUMN "code_gen_column"."name" IS '字段名';
COMMENT ON COLUMN "code_gen_column"."object_name" IS '对象名称';
COMMENT ON COLUMN "code_gen_column"."comment" IS '注释';
COMMENT ON COLUMN "code_gen_column"."search_item" IS '是否查询项';
COMMENT ON COLUMN "code_gen_column"."list_item" IS '是否列表项';
COMMENT ON COLUMN "code_gen_column"."edit_item" IS '是否编辑项';
COMMENT ON COLUMN "code_gen_column"."detail_item" IS '是否详情项';
