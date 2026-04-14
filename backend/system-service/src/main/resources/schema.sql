CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(32) DEFAULT 'STUDENT'
);

MERGE INTO sys_user (username, password, role)
KEY (username)
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN');
