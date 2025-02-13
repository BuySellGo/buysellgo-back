-- INSERT INTO tbl_faq_group (faq_group_title, created_at) VALUES ("교환반품", current_timestamp);
-- INSERT INTO tbl_faq_group (faq_group_title, created_at) VALUES ("이용방법", current_timestamp);
-- INSERT INTO tbl_faq_group (faq_group_title, created_at) VALUES ("계정", current_timestamp);
-- INSERT INTO tbl_faq_group (faq_group_title, created_at) VALUES ("배송", current_timestamp);
-- "교환반품"이 없으면 삽입
INSERT INTO tbl_faq_group (faq_group_title, created_at)
SELECT '교환반품', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM tbl_faq_group WHERE faq_group_title = '교환반품');

-- "이용방법"이 없으면 삽입
INSERT INTO tbl_faq_group (faq_group_title, created_at)
SELECT '이용방법', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM tbl_faq_group WHERE faq_group_title = '이용방법');

-- "계정"이 없으면 삽입
INSERT INTO tbl_faq_group (faq_group_title, created_at)
SELECT '계정', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM tbl_faq_group WHERE faq_group_title = '계정');

-- "배송"이 없으면 삽입
INSERT INTO tbl_faq_group (faq_group_title, created_at)
SELECT '배송', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM tbl_faq_group WHERE faq_group_title = '배송');
