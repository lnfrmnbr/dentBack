CREATE TABLE IF NOT EXISTS doctors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    last_name TEXT NOT NULL,
    first_name TEXT,
    patronymic TEXT,
    role TEXT NOT NULL,
    birth_date DATE NOT NULL,
    sex CHAR(1) CHECK (sex IN ('M', 'F'))
);

CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name TEXT NOT NULL,
    birth_date DATE NOT NULL,
    sex CHAR(1) CHECK (sex IN ('M', 'F')),
    email TEXT,
    phone_number TEXT
);

CREATE TABLE IF NOT EXISTS doctor_patient (
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    PRIMARY KEY (doctor_id, patient_id)
);

CREATE TABLE IF NOT EXISTS appointments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE SET NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE SET NULL,
    date TIMESTAMP NOT NULL DEFAULT NOW(),
    tags text,
    bop NUMERIC(5,2),
    russel NUMERIC(5,2),
    api NUMERIC(5,2),
    chart JSONB,
    file_url TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    email TEXT UNIQUE NOT NULL,
    password_hash TEXT,
    salt TEXT,
    provider TEXT NOT NULL DEFAULT 'local' CHECK (provider IN ('local', 'google')),
    provider_id TEXT,

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

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email_provider
    ON users (email, provider);

CREATE INDEX IF NOT EXISTS idx_users_doctor_id
    ON users (doctor_id);

