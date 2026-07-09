-- V3: Add avatar column to student.
-- Students previously had no avatar. This column stores the S3 object key
-- (not a URL); a presigned URL is generated on read. Nullable because
-- existing students have no avatar and it's optional going forward.

ALTER TABLE student ADD COLUMN image VARCHAR(255) NULL COMMENT 'avatar S3 object key';