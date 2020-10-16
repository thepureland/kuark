INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7ea61fa6-0013-47fb-9a4c-2052deecd371', 'kuark:user', 'sex', '性别', '性别', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('29aa96fb-df5d-4d39-8d77-377af16d0dd2', '7ea61fa6-0013-47fb-9a4c-2052deecd371', '0', null, '女', 2, '女', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('2c1fc71b-518e-4d6a-8d87-5c87da07b9c0', '7ea61fa6-0013-47fb-9a4c-2052deecd371', '1', null, '男', 1, '男', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('d381cb8e-4742-400c-874a-6f49986a7416', '7ea61fa6-0013-47fb-9a4c-2052deecd371', '9', null, '保密', 9, '保密', true, true, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('21118c66-2a0e-4b35-8e82-694387d7ef1d', 'kuark:user', 'user_status', '用户状态', '用户状态', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('165e7f79-ec57-45e9-bda4-8276b150ade0', '21118c66-2a0e-4b35-8e82-694387d7ef1d', '00', null, '注销', null, '注销', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c386f18a-14dd-4de5-bc1e-8dab439dbde3', '21118c66-2a0e-4b35-8e82-694387d7ef1d', '10', null, '正常', null, '正常', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('55158637-c92e-47f7-997a-08b01511866b', '21118c66-2a0e-4b35-8e82-694387d7ef1d', '20', null, '冻结', null, '冻结', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('8588a39c-ad84-40e6-b0b8-e6c4682593a0', '21118c66-2a0e-4b35-8e82-694387d7ef1d', '30', null, '过期', null, '过期', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('ef94a826-bd3a-4230-86e1-c4e4f7e4dd06', 'kuark:user', 'user_type', '用户类型', '用户类型', true, true, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('74931d7e-dd97-4378-bc35-aebf076b193c', 'kuark:user', 'constellation', '十二星座', '十二星座', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('735df7ff-2bef-4bf2-a857-4e51f6c2791d', '74931d7e-dd97-4378-bc35-aebf076b193c', 'aquarius', null, '水瓶座', 1, '水瓶座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('2d7df45a-8ca2-424f-b7bf-956506a3d5c0', '74931d7e-dd97-4378-bc35-aebf076b193c', 'pisces', null, '双鱼座', 2, '双鱼座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('5a63ff93-2240-4f28-95b0-6f0d211e9e03', '74931d7e-dd97-4378-bc35-aebf076b193c', 'aries', null, '白羊座', 3, '白羊座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('4ae46518-197b-4d55-8493-c6cfc418543f', '74931d7e-dd97-4378-bc35-aebf076b193c', 'taurus', null, '金牛座', 4, '金牛座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('6454a092-245f-4cdd-9596-5870d2f6fc66', '74931d7e-dd97-4378-bc35-aebf076b193c', 'gemini', null, '双子座', 5, '双子座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('201d79da-58a5-46dc-9ed9-76346eb82474', '74931d7e-dd97-4378-bc35-aebf076b193c', 'cancer', null, '巨蟹座', 6, '巨蟹座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c749ab38-6291-492e-b580-001e159a7a11', '74931d7e-dd97-4378-bc35-aebf076b193c', 'leo', null, '狮子座', 7, '狮子座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('5c9e6144-0c94-4a1e-b221-2e6e7d375d56', '74931d7e-dd97-4378-bc35-aebf076b193c', 'virgo', null, '处女座', 8, '处女座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('3e09c335-4477-4a99-ab71-1fcac952ef34', '74931d7e-dd97-4378-bc35-aebf076b193c', 'libra', null, '天秤座', 9, '天秤座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7c9940c4-23a4-4a21-932c-f6083f84fe80', '74931d7e-dd97-4378-bc35-aebf076b193c', 'scorpio', null, '天蝎座', 10, '天蝎座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('67b3ee90-0c98-428b-a50f-28747cc49998', '74931d7e-dd97-4378-bc35-aebf076b193c', 'sagittarius', null, '射手座', 11, '射手座', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7e822a9d-5a1a-4269-837b-11d87a54a19d', '74931d7e-dd97-4378-bc35-aebf076b193c', 'capricorn', null, '摩羯座', 12, '摩羯座', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('72d81bef-431c-494d-9bbf-9dc52de7a22b', 'kuark:user', 'CHN-nation', '中国-民族', '中国-民族', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('a96c0110-538d-476f-abb4-47a89953aeb2', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '01', null, '汉族', null, '汉族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c242f1f0-7743-46c6-960c-3e71f03eb298', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '02', null, '蒙古族', null, '蒙古族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('6ba11206-af81-42ab-b63a-016b7b28024b', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '03', null, '回族', null, '回族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('63fbaf1a-29d9-4cd2-9d69-5e8fe5b0cfec', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '04', null, '藏族', null, '藏族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('22aeda4d-90e8-458d-9789-894d93e382ce', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '05', null, '维吾尔族', null, '维吾尔族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('3217dc44-0e82-4fb1-92ce-cb4c4e225727', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '06', null, '苗族', null, '苗族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('844c9eb6-9838-4eb2-8ef7-ca7e98c0ea36', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '07', null, '彝族', null, '彝族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('6e1ac5f8-a9a9-4a80-a0df-3345352b6446', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '08', null, '壮族', null, '壮族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('1fba8fc5-8646-4fe2-81f8-68c731bfe587', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '09', null, '布依族', null, '布依族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('df94b0dc-234f-4ac0-af75-c3d764b10e86', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '10', null, '朝鲜族', null, '朝鲜族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('e8885b0d-5466-4bb5-9477-5fb9a0606923', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '11', null, '满族', null, '满族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('bd0dbb14-94d4-4d58-8420-985b884833ef', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '12', null, '侗族', null, '侗族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b4c3e0ab-7f08-4329-9cb9-789bdeba0044', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '13', null, '瑶族', null, '瑶族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b3e3198c-21f8-41cd-8148-e48313bde6f1', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '14', null, '白族', null, '白族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('29be51bf-6508-47b8-ab7e-5e708c0e9553', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '15', null, '土家族', null, '土家族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('52496cc3-b0b5-4562-af4a-6957deef23c6', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '16', null, '哈尼族', null, '哈尼族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('76decafa-7997-417d-8bf4-15f3bf6111b4', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '17', null, '哈萨克族', null, '哈萨克族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('936ea31b-fbd4-4461-879d-9047e60642a8', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '18', null, '傣族', null, '傣族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('f849f61e-a22b-4386-8dd4-ed9d26d2228f', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '19', null, '黎族', null, '黎族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7b9ec097-dc8b-4480-8999-9800628a62d7', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '20', null, '傈僳族', null, '傈僳族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b74027e5-aa7e-4c51-9ce3-8b2b5e167b4c', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '21', null, '佤族', null, '佤族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('ea858e77-9b04-475c-8f33-321d9a5ca752', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '22', null, '畲族', null, '畲族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('09e7d789-520d-4e61-aa9e-35ff9447568b', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '23', null, '高山族', null, '高山族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('bd0d3e8b-77a9-4da2-af76-dbf3869f697f', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '24', null, '拉祜族', null, '拉祜族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b41c6279-6d37-4189-b882-16bf9b283f7e', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '25', null, '水族', null, '水族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('704ea22f-8d2f-4af2-a262-a3c604f7ce38', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '26', null, '东乡族', null, '东乡族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('71407c7e-aa66-4ce2-b563-ec0541d29357', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '27', null, '纳西族', null, '纳西族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('9246b9c0-48d4-4edc-a906-33e3a1d333c5', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '28', null, '景颇族', null, '景颇族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('4f1d793a-eb03-4aac-9179-2996d6f39003', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '29', null, '柯尔克孜族', null, '柯尔克孜族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('9ed236a1-dbd4-48cb-967a-e2343c3e78e0', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '30', null, '土族', null, '土族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('134dae7c-9837-479a-a4de-80cd893bf8dc', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '31', null, '达斡尔族', null, '达斡尔族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('f5343318-923a-41f0-9a3d-67519f5849f8', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '32', null, '仫佬族', null, '仫佬族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('069ce071-ac2a-42aa-8e92-97ca316da8d4', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '33', null, '羌族', null, '羌族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('03a4a9e0-714d-4aeb-bbed-13b5fb88ae9d', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '34', null, '布朗族', null, '布朗族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('37d75a7e-a8cf-45a2-a6b7-a9a655436a20', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '35', null, '撒拉族', null, '撒拉族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c324c791-8a28-4c98-8234-410ab81972f0', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '36', null, '毛难族', null, '毛难族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('85abb303-b9b8-486d-b4b7-6a23b8760630', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '37', null, '仡佬族', null, '仡佬族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c517ad7d-102a-418b-ba0e-8545b2f662e0', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '38', null, '锡伯族', null, '锡伯族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('68d22ae1-837a-4f41-8c35-a118a7826475', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '39', null, '阿昌族', null, '阿昌族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('9e6be36b-ec25-438f-b30a-cc3ad7883fa7', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '40', null, '普米族', null, '普米族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('5a4eea72-67d4-4f1e-860b-4ba3639cb8cd', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '41', null, '塔吉克族', null, '塔吉克族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('d33aa591-2699-4700-82b2-8068336fccb6', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '42', null, '怒族', null, '怒族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7a937b72-ae0e-4a1f-943e-58916322a2eb', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '43', null, '乌孜别克族', null, '乌孜别克族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b16fa2b7-c3ec-4050-ac81-96375a4634bf', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '44', null, '俄罗斯族', null, '俄罗斯族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('fcf99128-1ff3-408c-b142-7600b304988c', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '45', null, '鄂温克族', null, '鄂温克族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('32b2962e-1c4c-4c04-9693-40556859f5bf', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '46', null, '崩龙族', null, '崩龙族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('3c9f757d-5bd9-440d-8711-f5a57335249c', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '47', null, '保安族', null, '保安族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('00a674ee-a34b-471e-a7df-6947f4707959', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '48', null, '裕固族', null, '裕固族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('42b469c6-dcb0-459c-9255-01ecca8fc94d', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '49', null, '京族', null, '京族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('319fc918-41fd-4c45-97e0-56e95e959e2d', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '50', null, '塔塔尔族', null, '塔塔尔族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('b4264557-c28c-478a-83ab-d9ff0c1b8c15', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '51', null, '独龙族', null, '独龙族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('d5fab68f-e1ab-4f45-a967-e33a657d9f1a', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '52', null, '鄂伦春族', null, '鄂伦春族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('067031c2-a3db-431f-8686-7bffd71b4d44', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '53', null, '赫哲族', null, '赫哲族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('1f509bd9-99d6-438a-a3a1-d6628b5e8dfc', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '54', null, '门巴族', null, '门巴族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('f297a601-724a-4ebf-9e85-4a1f215d3e5b', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '55', null, '珞巴族', null, '珞巴族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('ad738e15-0b2c-4532-a65f-b773dea7ee99', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '56', null, '基诺族', null, '基诺族', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('900665fe-78f6-4c0c-80c9-da830b57a9bf', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '97', null, '其他', null, '其他', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('faa8f10b-f5f1-49f2-808a-cc3a6f4a26fc', '72d81bef-431c-494d-9bbf-9dc52de7a22b', '98', null, '外国血统', null, '外国血统', true, false, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('88fc3696-f96e-48e8-a86b-b13b8481b998', 'kuark:user', 'user_terminal', '用户终端', '用户终端', true, true, null, null, null, null);


INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', 'kuark:user', 'identity_type', '身份类型', '身份类型', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('3cdebd8d-2a60-496f-b64d-95f949902174', '1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', '1-sys', null, '系统账号', null, '系统账号', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7952c2ee-39b8-4820-872e-80cecea41758', '1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', '2-mobile', null, '手机账号', null, '手机账号', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('11057d92-e0df-4df3-876a-06637479cbb6', '1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', '2-email', null, '邮箱账号', null, '邮箱账号', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('90a33752-14db-413f-acd9-2f3de0a2bedd', '1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', '3-wechat', null, '微信账号', null, '微信账号', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('62786283-0a11-4e93-b5d4-769452376119', '1739ad5e-6a99-41e1-a0b9-74dff7b52cc8', '3-qq', null, 'QQ账号', null, 'QQ账号', true, false, null, null, null, null);



INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('ccffbb81-3ee9-4c04-b99e-4b2164513912', 'kuark:user', 'contact_way', '联系方式', '联系方式', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('ec58525f-799f-4b96-b92c-de32fc147a60', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '101', null, '手机', null, '手机', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('823c58b3-daf3-4d77-94d8-a95273948f8f', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '201', null, '电子邮箱', null, '电子邮箱', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('019c2d12-1606-4a8b-948b-4979c2b655a7', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '301', null, '微信', null, '微信', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('773f030f-3fa3-44f9-a1b4-ed7929579a85', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '302', null, 'QQ', null, 'QQ', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('58e68b1d-885b-4867-b9b4-8ae48f083ae2', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '102', null, '固定电话', null, '固定电话', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('65665f8b-8a58-456c-9bec-358a8e226b3f', 'ccffbb81-3ee9-4c04-b99e-4b2164513912', '401', null, '微博', null, '微博', true, false, null, null, null, null);



INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('18093541-0fee-4ae1-bb0c-28112d8ccbb8', 'kuark:user', 'contact_way_status', '联系方式状态', '联系方式状态', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('852dc24a-ab87-42e3-be53-f79ebd1fe7e8', '18093541-0fee-4ae1-bb0c-28112d8ccbb8', '00', null, '未验证', null, '未验证', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('cfd43b9a-fcde-43bd-a156-4fb96f9a89c2', '18093541-0fee-4ae1-bb0c-28112d8ccbb8', '10', null, '正常', null, '正常', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('7036a848-89df-43c2-8dc8-edf3226af05e', '18093541-0fee-4ae1-bb0c-28112d8ccbb8', '20', null, '无法联系', null, '无法联系', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('c5b9ec48-377c-4573-90e1-b9d001420d3e', '18093541-0fee-4ae1-bb0c-28112d8ccbb8', '30', null, '非本人联系方式', null, '非本人联系方式', true, false, null, null, null, null);



INSERT INTO "sys_dict" ("id", "module", "dict_type", "dict_name", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('de59617e-b79d-4b25-9aea-d2ec03299c2e', 'kuark:user', 'db_audit_operate_type', '数据库审批操作类型', '数据库审批操作类型', true, true, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('6144b3ed-0d9b-48c1-a33a-e31b06dc6f49', 'de59617e-b79d-4b25-9aea-d2ec03299c2e', 'A', null, '新增', null, '新增', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('37189bae-7fd8-4e78-9045-e1ca735abb0d', 'de59617e-b79d-4b25-9aea-d2ec03299c2e', 'D', null, '删除', null, '删除', true, false, null, null, null, null);
INSERT INTO "sys_dict_item" ("id", "dict_id", "item_code", "parent_code", "item_name", "seq_no", "remark", "is_active", "is_built_in", "create_user", "create_time", "update_user", "update_time") VALUES ('e3029d4d-d6bf-4509-b3c8-471aaac9489c', 'de59617e-b79d-4b25-9aea-d2ec03299c2e', 'U', null, '更新', null, '更新', true, false, null, null, null, null);

