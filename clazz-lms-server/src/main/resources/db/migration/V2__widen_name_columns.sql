-- V2: Widen person-name columns for internationalization.
-- The original VARCHAR(10) assumed short (Chinese) names and truncates
-- or rejects realistic international full names. Widen to VARCHAR(100).

ALTER TABLE student MODIFY COLUMN name VARCHAR(100) NOT NULL COMMENT 'name';
ALTER TABLE emp     MODIFY COLUMN name VARCHAR(100) NOT NULL COMMENT 'name';
