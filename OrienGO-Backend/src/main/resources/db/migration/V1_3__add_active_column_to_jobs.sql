-- Add active column to jobs table
ALTER TABLE jobs ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE; 