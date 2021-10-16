INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('5d11f010-6d5b-4ead-b20e-a9f815bb946e', 'kuark:msg', 'send_type', '消息发送类型', '消息发送类型', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('9f9bbfd1-9d99-4fd6-9f32-08825ec835ab', '5d11f010-6d5b-4ead-b20e-a9f815bb946e', 'auto', null, '自动', 1, '自动', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('eca543b4-37cd-4945-ac7b-4574167ed8ba', '5d11f010-6d5b-4ead-b20e-a9f815bb946e', 'manual', null, '手动', 2, '手动', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('9b370a9c-bf07-43db-a560-001bbe8be555', 'kuark:msg', 'manual_event_type', '手动事件类型', '手动事件类型', true, true, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('e824cada-52d5-4d6c-bd1c-b47974674e73', 'kuark:msg', 'msg_type', '消息类型', '消息类型', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('fe5074e9-14f3-40d0-80d5-7735e0d57022', 'e824cada-52d5-4d6c-bd1c-b47974674e73', 'sms', null, '手机短信', null, '手机短信', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('244c464a-7b06-45a4-9943-a3ac73da329a', 'e824cada-52d5-4d6c-bd1c-b47974674e73', 'email', null, '电子邮箱', null, '电子邮箱', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b28d97c9-8479-4a64-8489-16049d95c81e', 'e824cada-52d5-4d6c-bd1c-b47974674e73', 'siteMsg', null, '站内信', null, '站内信', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('8188c833-04c6-4cee-ba1c-d8a6c7cdbb1a', 'kuark:msg', 'auto_event_type', '自动事件类型', '自动事件类型', true, true, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'kuark:msg', 'receiver_group_type', '消息接收者群组类型', '消息接收者群组类型', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('cffdf111-e0d3-4b0e-aa7c-7503706a020b', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'all_front', null, '所有前台用户', 1, '所有前台用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('f7b1ce80-1544-414f-9007-a047221858a1', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'all_back', null, '所有后台用户', 2, '所有后台用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('8b7b429d-2a64-4e06-ac88-ae38fb66b8dc', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'online_front', null, '前台在线用户', 3, '前台在线用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('fac8e7c4-54a4-44de-9fcf-f08a75cd4479', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'online_back', null, '后台在线用户', 4, '后台在线用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c2a9cc1a-6211-4282-b52c-2630076953e6', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'offline_front', null, '前台不在线用户', 5, '前台不在线用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('4c1edd65-0cd7-4360-ae17-a871fcfbe94a', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'offline_back', null, '后台不在线用户', 6, '后台不在线用户', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('d90d4cad-4132-42e6-b238-c0f0cc428d19', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'guest', null, '游客', 7, '游客', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('a8821f8e-88ad-4ad7-884e-464a79266867', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'rank', null, '层级', 8, '层级', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('5266ba2c-695d-4ce7-8db6-a6b96b6f3cd0', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'tag', null, '标签', 9, '标签', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('50efef9d-6b7a-4671-95b9-4910174c1d9e', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'level', null, '等级', 10, '等级', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('548d2f2f-e58a-4049-8a6c-15d68bec9af3', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'role', null, '角色', 11, '角色', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('bc183757-f687-4c2e-95cf-d68cabd0d13b', '8d0f6049-2642-4b0f-90f6-cd52333e0d6a', 'user', null, '具体用户', 12, '具体用户', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('1b4563f8-56db-4f5e-8173-111d0ee433f4', 'kuark:msg', 'send_status', '发送状态', '发送状态', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b3dba5d7-efe1-44de-89ee-9c6d08269c41', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '00', null, '等待发送', 1, '等待发送', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('01ff8e5a-8545-488e-b767-92335c829a58', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '01', null, '取消发送', 2, '取消发送', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('53b32d0b-705e-4d22-88ec-73f866a84bcd', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '11', null, '已发送给消息队列', 3, '已发送给消息队列', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('718f783e-0655-413e-9917-c44a86b5bf5d', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '22', null, '最终发送失败', 4, '最终发送失败', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('e522a64e-739d-417d-8b12-f98456d82f93', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '31', null, '已从消息队列消费', 5, '已从消息队列消费', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('71b075ce-591f-40f7-a94c-7acf040b0443', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '32', null, '发送完成，但是部分用户发送失败', 6, '发送完成，但是部分用户发送失败', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "active", "built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('424ceca5-127d-437c-844d-93bdbddcabff', '1b4563f8-56db-4f5e-8173-111d0ee433f4', '33', null, '发送成功', 7, '发送成功', true, false, null, null, null, null);
