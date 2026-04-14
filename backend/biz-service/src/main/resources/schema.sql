CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_no VARCHAR(32),
    name VARCHAR(64),
    age INT,
    gender VARCHAR(16)
);

CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(32),
    course_name VARCHAR(64),
    credit INT
);

CREATE TABLE IF NOT EXISTS score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT,
    course_id BIGINT,
    score DOUBLE
);

MERGE INTO student (id, student_no, name, age, gender) KEY (id) VALUES (1, '2026001', '张三', 20, '男');
MERGE INTO course (id, course_code, course_name, credit) KEY (id) VALUES (1, 'CS101', 'Java程序设计', 4);
MERGE INTO score (id, student_id, course_id, score) KEY (id) VALUES (1, 1, 1, 89.5);
