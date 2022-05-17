CREATE SCHEMA course_management;
CREATE SCHEMA user_management;

CREATE SEQUENCE user_management.seq_t_user
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE user_management.t_user
(
    user_id         BIGINT DEFAULT nextval('user_management.seq_t_user') PRIMARY KEY,
    user_first_name VARCHAR NOT NULL,
    user_last_name  VARCHAR,
    user_email      VARCHAR NOT NULL UNIQUE,
    user_password   VARCHAR NOT NULL
);

CREATE SEQUENCE course_management.seq_t_course_status
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE course_management.t_course_status
(
    course_status_id    INTEGER DEFAULT nextval('course_management.seq_t_course_status') PRIMARY KEY,
    course_status_title VARCHAR(25) NOT NULL
);


CREATE SEQUENCE course_management.seq_t_course
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE course_management.t_course
(
    course_id             BIGINT DEFAULT nextval('course_management.seq_t_course') PRIMARY KEY,
    course_title          VARCHAR NOT NULL,
    course_description    VARCHAR     NOT NULL,
    course_thumbnail_path VARCHAR,
    last_update_date      DATE, --define trigger or java code
    created_date          DATE, --same as for previous
    author_id             BIGINT,
    course_status_id      INTEGER,
    CONSTRAINT fk_t_course_to_author FOREIGN KEY (author_id) REFERENCES user_management.t_user (user_id),
    CONSTRAINT fk_t_course_to_course_status FOREIGN KEY (course_status_id) REFERENCES course_management.t_course_status (course_status_id)
);

CREATE SEQUENCE course_management.seq_t_course_chapter
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE course_management.t_course_chapter
(
    chapter_id          BIGINT DEFAULT nextval('course_management.seq_t_course_chapter') PRIMARY KEY,
    chapter_description VARCHAR,
    course_id           BIGINT,
    chapter_title       VARCHAR
);

CREATE SEQUENCE course_management.seq_t_attachment
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE course_management.t_attachment
(
    attachment_id   BIGINT DEFAULT nextval('course_management.seq_t_attachment') PRIMARY KEY,
    attachment_path VARCHAR,
    chapter_id      BIGINT,
    CONSTRAINT fk_t_attachment_to_chapter FOREIGN KEY (chapter_id) REFERENCES course_management.t_course_chapter (chapter_id)
);

CREATE SEQUENCE course_management.seq_t_progress_status
    INCREMENT BY 1
    MINVALUE 1
    NO CYCLE;

CREATE TABLE course_management.t_progress_status
(
    progress_status_id    INTEGER DEFAULT nextval('course_management.seq_t_progress_status') PRIMARY KEY,
    progress_status_title VARCHAR
);

CREATE TABLE course_management.t_user_to_course
(
    course_id          BIGINT,
    user_id            BIGINT,
    progress_status_id INTEGER,
    CONSTRAINT pk_t_user_to_course PRIMARY KEY (course_id, user_id),
    CONSTRAINT fk_t_user_to_course_to_t_course FOREIGN KEY (course_id) REFERENCES course_management.t_course (course_id),
    CONSTRAINT fk_t_user_to_course_to_t_user FOREIGN KEY (user_id) REFERENCES user_management.t_user (user_id),
    CONSTRAINT fk_t_user_to_course_to_t_progress_status FOREIGN KEY (progress_status_id) REFERENCES course_management.t_progress_status (progress_status_id)
);