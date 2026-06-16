-- Allow multiple course topics per date (remove unique constraint on date)
ALTER TABLE courses DROP CONSTRAINT IF EXISTS courses_date_key;
