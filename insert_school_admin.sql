-- First, drop the old check constraint
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_role_check;

-- Add new check constraint with all valid roles
ALTER TABLE users ADD CONSTRAINT users_role_check 
  CHECK (role IN ('USER', 'SUPER_ADMIN', 'SCHOOL_ADMIN', 'STUDENT'));

-- Now insert the school admin user
INSERT INTO users (
  first_name, last_name, email, password, role, active, 
  auth_provider, school_id, user_type, 
  welcome_completed, onboarding_completed,
  created_at, updated_at
) VALUES (
  'Kaustubh', 
  'Salunkhe', 
  'kaustubhsalunkhe1012@gmail.com', 
  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
  'SCHOOL_ADMIN', 
  true, 
  'LOCAL', 
  1, 
  'Teacher',
  false,
  false,
  NOW(), 
  NOW()
);
