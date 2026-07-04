-- V2: Widen person-name columns for internationalization.
-- The original VARCHAR(10) assumed short (Chinese) names and truncates2026-07-04 15:02:27.179 [main] INFO  org.flywaydb.core.FlywayExecutor - Database: jdbc:mysql://localhost:3306/tlias (MySQL 8.4)
2026-07-04 15:02:27.188 [main] WARN  org.flywaydb.core.internal.database.base.Database - Flyway upgrade recommended: MySQL 8.4 is newer than this version of Flyway and support has not been tested. The latest supported version of MySQL is 8.1.
2026-07-04 15:02:27.209 [main] INFO  org.flywaydb.core.internal.command.DbValidate - Successfully validated 3 migrations (execution time 00:00.015s)
2026-07-04 15:02:27.221 [main] INFO  org.flywaydb.core.internal.command.DbMigrate - Current version of schema `tlias`: 1
2026-07-04 15:02:27.228 [main] INFO  org.flywaydb.core.internal.command.DbMigrate - Migrating schema `tlias` to version "2 - widen name columns"
2026-07-04 15:02:27.292 [main] INFO  org.flywaydb.core.internal.command.DbMigrate - Successfully applied 1 migration to schema `tlias`, now at version v2 (execution time 00:00.050s)
Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImpl' adapter.
-- or rejects realistic international full names. Widen to VARCHAR(100).

ALTER TABLE student MODIFY COLUMN name VARCHAR(100) NOT NULL COMMENT 'name';
ALTER TABLE emp     MODIFY COLUMN name VARCHAR(100) NOT NULL COMMENT 'name';
