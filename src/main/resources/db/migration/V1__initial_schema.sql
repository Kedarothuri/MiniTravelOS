CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         phone VARCHAR(50),
                         country VARCHAR(100),
                         notes TEXT,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE travelers (
                           id BIGSERIAL PRIMARY KEY,
                           client_id BIGINT NOT NULL REFERENCES clients(id),
                           name VARCHAR(255) NOT NULL,
                           date_of_birth DATE,
                           nationality VARCHAR(100),
                           passport_number VARCHAR(100),
                           special_requirements TEXT
);

CREATE TABLE suppliers (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           type VARCHAR(50) NOT NULL,
                           contact_person VARCHAR(255),
                           email VARCHAR(255),
                           phone VARCHAR(50),
                           city VARCHAR(100),
                           is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE travel_requests (
                                 id BIGSERIAL PRIMARY KEY,
                                 client_id BIGINT NOT NULL REFERENCES clients(id),
                                 destination VARCHAR(255) NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL,
                                 traveler_count INT NOT NULL,
                                 budget NUMERIC(12, 2) NOT NULL,
                                 currency VARCHAR(10) NOT NULL,
                                 notes TEXT,
                                 assigned_employee VARCHAR(255),
                                 status VARCHAR(50) NOT NULL DEFAULT 'NEW'
);

CREATE TABLE quotations (
                            id BIGSERIAL PRIMARY KEY,
                            request_id BIGINT NOT NULL REFERENCES travel_requests(id),
                            title VARCHAR(255) NOT NULL,
                            description TEXT,
                            currency VARCHAR(10) NOT NULL,
                            total_amount NUMERIC(12, 2) NOT NULL,
                            validity_date DATE NOT NULL,
                            notes TEXT,
                            status VARCHAR(50) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE trips (
                       id BIGSERIAL PRIMARY KEY,
                       client_id BIGINT NOT NULL REFERENCES clients(id),
                       quotation_id BIGINT UNIQUE NOT NULL REFERENCES quotations(id),
                       trip_name VARCHAR(255) NOT NULL,
                       start_date DATE NOT NULL,
                       end_date DATE NOT NULL,
                       status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
                       notes TEXT
);

CREATE TABLE trip_costs (
                            id BIGSERIAL PRIMARY KEY,
                            trip_id BIGINT NOT NULL REFERENCES trips(id),
                            supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
                            description TEXT NOT NULL,
                            amount NUMERIC(12, 2) NOT NULL,
                            currency VARCHAR(10) NOT NULL,
                            due_date DATE NOT NULL,
                            payment_status VARCHAR(50) NOT NULL DEFAULT 'UNPAID'
);