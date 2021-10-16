drop TABLE IF EXISTS "test_table";
CREATE TABLE IF NOT EXISTS "test_table"
(
    "id"            int2 not null,
    "name"          varchar(255) NOT NULL,
    "birthday"      timestamp(6),
    "active" bool,
    "weight"        float8,
    "height"        int2,
    CONSTRAINT "pk_test_table" PRIMARY KEY ("id")
);

comment on table "test_table" is '测试表';
COMMENT ON COLUMN "test_table"."id" IS '主键';
COMMENT ON COLUMN "test_table"."name" IS '名字';
COMMENT ON COLUMN "test_table"."birthday" IS '生日';
COMMENT ON COLUMN "test_table"."active" IS '是否生效';
COMMENT ON COLUMN "test_table"."weight" IS '体重';
COMMENT ON COLUMN "test_table"."height" IS '身高';