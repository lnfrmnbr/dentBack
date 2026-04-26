CREATE TABLE IF NOT EXISTS clinics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    logo TEXT,
    org_name TEXT NOT NULL,
    short_name TEXT,
    contacts TEXT,
    address TEXT,
    email TEXT
);

CREATE TABLE IF NOT EXISTS doctors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    clinic_id UUID REFERENCES clinics(id) ON DELETE SET NULL,
    last_name TEXT NOT NULL,
    first_name TEXT,
    patronymic TEXT,
    role TEXT NOT NULL,
    birth_date DATE NOT NULL,
    sex CHAR(1) CHECK (sex IN ('M', 'F'))
);

CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    clinic_id UUID REFERENCES clinics(id) ON DELETE SET NULL,
    full_name TEXT NOT NULL,
    birth_date DATE NOT NULL,
    sex CHAR(1) CHECK (sex IN ('M', 'F')),
    email TEXT,
    phone_number TEXT
);

CREATE TABLE IF NOT EXISTS appointments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE SET NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE SET NULL,
    date TIMESTAMP NOT NULL DEFAULT NOW(),
    tags text,
    diagnosis text,
    complaints text,
    comments text,
    plan text,
    is_first BOOLEAN ,
    indexes JSONB
    chart JSONB
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    email TEXT UNIQUE NOT NULL,
    password_hash TEXT,
    salt TEXT,

    doctor_id UUID UNIQUE REFERENCES doctors(id) ON DELETE CASCADE,

    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS auth_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    token TEXT NOT NULL,

    token_type TEXT NOT NULL DEFAULT 'refresh' CHECK (token_type IN ('access', 'refresh')),

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP DEFAULT NOW(),
    revoked_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_doctor_id
    ON users (doctor_id);

CREATE INDEX IF NOT EXISTS idx_doctors_clinic_id
    ON doctors (clinic_id);

CREATE INDEX IF NOT EXISTS idx_patients_clinic_id
    ON patients (clinic_id);

