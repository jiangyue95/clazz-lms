-- =====================================================================
-- V1: Baseline schema for clazz-lms.
--
-- This is a faithful snapshot of the existing production database, used
-- as the Flyway baseline. On an existing database it is NOT re-executed
-- (baseline-on-migrate marks it as already applied); on a fresh/empty
-- environment it rebuilds the schema from scratch.
--
-- No foreign-key constraints are declared here: the current database has
-- none (relations are logical only). Adding FKs is deferred to a later
-- migration, after verifying existing data satisfies them.
-- =====================================================================

CREATE TABLE dept (
                      id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                      name        VARCHAR(10) NOT NULL COMMENT 'department name',
                      create_time DATETIME    NULL COMMENT 'created at',
                      update_time DATETIME    NULL COMMENT 'updated at',
                      CONSTRAINT name UNIQUE (name)
) COMMENT 'department';

CREATE TABLE emp (
                     id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                     username    VARCHAR(20)      NOT NULL COMMENT 'login username',
                     password    VARCHAR(72)      NOT NULL COMMENT 'password (BCrypt hash)',
                     name        VARCHAR(10)      NOT NULL COMMENT 'name',
                     gender      TINYINT UNSIGNED NOT NULL COMMENT 'gender, 1:male, 2:female',
                     phone       CHAR(11)         NOT NULL COMMENT 'phone',
                     job         TINYINT UNSIGNED NULL COMMENT 'job, 1:head teacher, 2:lecturer, 3:student affairs, 4:research supervisor, 5:counsellor',
                     salary      INT UNSIGNED     NULL COMMENT 'salary',
                     image       VARCHAR(255)     NULL COMMENT 'avatar url',
                     entry_date  DATE             NULL COMMENT 'entry date',
                     dept_id     INT UNSIGNED     NULL COMMENT 'department ID',
                     create_time DATETIME         NULL COMMENT 'created at',
                     update_time DATETIME         NULL COMMENT 'updated at',
                     CONSTRAINT phone UNIQUE (phone),
                     CONSTRAINT username UNIQUE (username)
) COMMENT 'employee';

CREATE TABLE clazz (
                       id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                       name        VARCHAR(30)      NOT NULL COMMENT 'class name',
                       room        VARCHAR(20)      NULL COMMENT 'classroom',
                       begin_date  DATE             NOT NULL COMMENT 'start date',
                       end_date    DATE             NOT NULL COMMENT 'end date',
                       master_id   INT UNSIGNED     NULL COMMENT 'head teacher ID, references emp.id',
                       subject     TINYINT UNSIGNED NOT NULL COMMENT 'subject, 1:java, 2:frontend, 3:big data, 4:python, 5:go, 6:embedded',
                       create_time DATETIME         NULL COMMENT 'created at',
                       update_time DATETIME         NULL COMMENT 'updated at',
                       CONSTRAINT name UNIQUE (name)
) COMMENT 'class';

CREATE TABLE student (
                         id              INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                         name            VARCHAR(10)                  NOT NULL COMMENT 'name',
                         no              CHAR(10)                     NOT NULL COMMENT 'student number',
                         gender          TINYINT UNSIGNED             NOT NULL COMMENT 'gender, 1:male, 2:female',
                         phone           VARCHAR(11)                  NOT NULL COMMENT 'phone',
                         id_card         CHAR(18)                     NOT NULL COMMENT 'id card number',
                         is_college      TINYINT UNSIGNED             NOT NULL COMMENT 'from college, 1:yes, 0:no',
                         address         VARCHAR(100)                 NULL COMMENT 'address',
                         degree          TINYINT UNSIGNED             NULL COMMENT 'highest degree, 1:junior, 2:senior, 3:college, 4:bachelor, 5:master, 6:phd',
                         graduation_date DATE                         NULL COMMENT 'graduation date',
                         clazz_id        INT UNSIGNED                 NOT NULL COMMENT 'class ID, references clazz.id',
                         violation_count TINYINT UNSIGNED DEFAULT '0' NOT NULL COMMENT 'violation count',
                         violation_score TINYINT UNSIGNED DEFAULT '0' NOT NULL COMMENT 'violation score',
                         create_time     DATETIME                     NULL COMMENT 'created at',
                         update_time     DATETIME                     NULL COMMENT 'updated at',
                         CONSTRAINT id_card UNIQUE (id_card),
                         CONSTRAINT no UNIQUE (no),
                         CONSTRAINT phone UNIQUE (phone)
) COMMENT 'student';

CREATE TABLE emp_expr (
                          id      INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                          emp_id  INT UNSIGNED NULL COMMENT 'employee ID',
                          begin   DATE         NULL COMMENT 'start date',
                          end     DATE         NULL COMMENT 'end date',
                          company VARCHAR(50)  NULL COMMENT 'company name',
                          job     VARCHAR(50)  NULL COMMENT 'job title'
) COMMENT 'employee work experience';

CREATE TABLE emp_log (
                         id           INT UNSIGNED AUTO_INCREMENT COMMENT 'ID, primary key' PRIMARY KEY,
                         operate_time DATETIME      NULL COMMENT 'operation time',
                         info         VARCHAR(2000) NULL COMMENT 'log info'
) COMMENT 'employee log';

CREATE TABLE operate_log (
                             id             INT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
                             operate_emp_id INT UNSIGNED    NULL COMMENT 'operator ID',
                             operate_time   DATETIME        NULL COMMENT 'operation time',
                             class_name     VARCHAR(100)    NULL COMMENT 'class name',
                             method_name    VARCHAR(100)    NULL COMMENT 'method name',
                             method_params  VARCHAR(2000)   NULL COMMENT 'method params',
                             return_value   VARCHAR(2000)   NULL COMMENT 'return value',
                             cost_time      BIGINT UNSIGNED NULL COMMENT 'execution time (ms)'
) COMMENT 'operation log';