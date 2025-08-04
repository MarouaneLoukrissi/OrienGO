-- Add RIASEC columns to jobs table
ALTER TABLE jobs ADD COLUMN riasec_realistic DECIMAL(5,2);
ALTER TABLE jobs ADD COLUMN riasec_investigative DECIMAL(5,2);
ALTER TABLE jobs ADD COLUMN riasec_artistic DECIMAL(5,2);
ALTER TABLE jobs ADD COLUMN riasec_social DECIMAL(5,2);
ALTER TABLE jobs ADD COLUMN riasec_enterprising DECIMAL(5,2);
ALTER TABLE jobs ADD COLUMN riasec_conventional DECIMAL(5,2); 