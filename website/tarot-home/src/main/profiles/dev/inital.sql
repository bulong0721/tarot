
-- 初始化mysql的假数据 --


INSERT INTO `tarot`.`C_MERCHANT` (`MERCHANT_ID`, `BUSINESS_TYPE`, `CUISINE_TYPE`, `DESCRIPTION`, `LOGO_URL`, `NAME`) VALUES ('36', 'MARKET', '22223', '22223', NULL, '22223');
INSERT INTO `tarot`.`C_MERCHANT` (`MERCHANT_ID`, `BUSINESS_TYPE`, `CUISINE_TYPE`, `DESCRIPTION`, `LOGO_URL`, `NAME`) VALUES ('37', 'OTHER', '4', '4', NULL, '4');
INSERT INTO `tarot`.`C_MERCHANT` (`MERCHANT_ID`, `BUSINESS_TYPE`, `CUISINE_TYPE`, `DESCRIPTION`, `LOGO_URL`, `NAME`) VALUES ('100', 'FOOD', 'tttt2', '1232344124', NULL, '门店100');



INSERT INTO `tarot`.`C_MERCHANT_STORE` (`STORE_ID`, `STORE_CODE`, `STORE_CPP`, `DESCRIPTION`, `EXPERIENCE`, `NAME`, `PHONE`, `POSTAL_CODE`, `RATINGS`, `STORE_ADDRESS`, `MERCHANT_ID`, `SCORE`, `STORE_TYPE`, `TIME_CLOSE`, `TIME_OPEN`) VALUES ('100', '23423432', NULL, NULL, '\0', '商户100', NULL, NULL, NULL, NULL, '100', NULL, NULL, NULL, NULL);
INSERT INTO `tarot`.`C_MERCHANT_STORE` (`STORE_ID`, `STORE_CODE`, `STORE_CPP`, `DESCRIPTION`, `EXPERIENCE`, `NAME`, `PHONE`, `POSTAL_CODE`, `RATINGS`, `STORE_ADDRESS`, `MERCHANT_ID`, `SCORE`, `STORE_TYPE`, `TIME_CLOSE`, `TIME_OPEN`) VALUES ('34', '6456', NULL, NULL, '\0', 'test1', '645', NULL, NULL, NULL, '100', NULL, NULL, NULL, NULL);
INSERT INTO `tarot`.`C_MERCHANT_STORE` (`STORE_ID`, `STORE_CODE`, `STORE_CPP`, `DESCRIPTION`, `EXPERIENCE`, `NAME`, `PHONE`, `POSTAL_CODE`, `RATINGS`, `STORE_ADDRESS`, `MERCHANT_ID`, `SCORE`, `STORE_TYPE`, `TIME_CLOSE`, `TIME_OPEN`) VALUES ('35', '3322', NULL, NULL, '\0', 'test2', '333', NULL, NULL, NULL, '36', NULL, NULL, NULL, NULL);
INSERT INTO `tarot`.`C_MERCHANT_STORE` (`STORE_ID`, `STORE_CODE`, `STORE_CPP`, `DESCRIPTION`, `EXPERIENCE`, `NAME`, `PHONE`, `POSTAL_CODE`, `RATINGS`, `STORE_ADDRESS`, `MERCHANT_ID`, `SCORE`, `STORE_TYPE`, `TIME_CLOSE`, `TIME_OPEN`) VALUES ('36', '64561', NULL, NULL, '\0', 'test3', '645', NULL, NULL, NULL, '36', NULL, NULL, NULL, NULL);
INSERT INTO `tarot`.`C_MERCHANT_STORE` (`STORE_ID`, `STORE_CODE`, `STORE_CPP`, `DESCRIPTION`, `EXPERIENCE`, `NAME`, `PHONE`, `POSTAL_CODE`, `RATINGS`, `STORE_ADDRESS`, `MERCHANT_ID`, `SCORE`, `STORE_TYPE`, `TIME_CLOSE`, `TIME_OPEN`) VALUES ('37', '33222', NULL, NULL, '\0', 'test4', '333', NULL, NULL, NULL, '37', NULL, NULL, NULL, NULL);

INSERT INTO `tarot`.`C_ADMIN_USER` (`ADMIN_USER_ID`, `ACTIVE_STATUS_FLAG`, `ADMIN_EMAIL`, `LOGIN`, `NAME`, `PASSWORD`, `PHONE_NUMBER`, `LOGIN_ACCESS`, `STORE_ID`, `LOGIN_IP`) VALUES ('5', 1, 'bulong0721@163.com', 'bulong0721', 'bulong0721', '123456', '13818771662', '2016-07-15 19:08:50', '100', '127.0.0.1');