-- ========================================
-- Test seed data for clazz-lms
-- Each test starts from this baseline state.
-- ========================================

-- Departments
INSERT INTO dept
    (id, name, create_time, update_time)
VALUES
    (1, '研发部',  '2026-01-01 10:00:00', '2026-01-01 10:00:00'),
    (2, '市场部',  '2026-01-01 10:00:00', '2026-01-01 10:00:00'),
    (3, '财务部',  '2026-01-01 10:00:00', '2026-01-01 10:00:00');

-- Employees: 1 employee assigned to dept #1
-- This lets us test:
--   - DELETE /depts/1 → 409 Conflict (has employees)
--   - DELETE /depts/2 → 204 No Content (no employees)
INSERT INTO emp
    (id, username, name, gender, phone, dept_id, create_time, update_time)
VALUES
    (1, 'alice', 'Alice', 1, '13800000001', 1, '2026-01-01 10:00:00', '2026-01-01 10:00:00');

-- Reset auto-increment counters so new INSERT statements
-- get id values that don't conflict with seeded data above.
-- Without this, H2 starts auto-increment from 1, colliding with seeded ids.
ALTER TABLE dept ALTER COLUMN id RESTART WITH 100;
ALTER TABLE emp ALTER COLUMN id RESTART WITH 100;