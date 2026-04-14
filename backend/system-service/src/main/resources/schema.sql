CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    phone VARCHAR(32),
    email VARCHAR(128),
    role VARCHAR(32) DEFAULT 'STUDENT'
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_sys_user_phone ON sys_user(phone);
CREATE UNIQUE INDEX IF NOT EXISTS ux_sys_user_email ON sys_user(email);

MERGE INTO sys_user (username, password, phone, email, role)
KEY (username)
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', '13800000000', 'admin@example.com', 'ADMIN');
