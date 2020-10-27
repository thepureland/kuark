drop TABLE IF EXISTS "test_table";
CREATE TABLE IF NOT EXISTS "test_table"
(
    "id"            int2 not null primary key,
    "balance"       float8 NOT NULL
    CONSTRAINT "pk_test_table" PRIMARY KEY ("id")
);

comment on table "test_table" is '测试表';
COMMENT ON COLUMN "test_table"."id" IS '主键';
COMMENT ON COLUMN "test_table"."balance" IS '余额';