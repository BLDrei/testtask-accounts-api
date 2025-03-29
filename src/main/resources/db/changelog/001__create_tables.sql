
CREATE TABLE account_type(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name VARCHAR2(32) NOT NULL UNIQUE,
    description VARCHAR2(256) NOT NULL,

    created_at DATE DEFAULT SYSDATE NOT NULL,
    created_by VARCHAR2(64) DEFAULT USER NOT NULL,
    updated_at DATE,
    updated_by VARCHAR2(64),
    deleted_at DATE,
    deleted_by VARCHAR2(64)
);


CREATE TABLE account_status(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name VARCHAR2(32) NOT NULL UNIQUE,
    description VARCHAR2(256) NOT NULL,

    created_at DATE DEFAULT SYSDATE NOT NULL,
    created_by VARCHAR2(64) DEFAULT USER NOT NULL,
    updated_at DATE,
    updated_by VARCHAR2(64),
    deleted_at DATE,
    deleted_by VARCHAR2(64)
);


CREATE TABLE currency(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name VARCHAR2(128) NOT NULL UNIQUE,
    code VARCHAR2(3) NOT NULL UNIQUE,
    sign VARCHAR2(8) NOT NULL UNIQUE,

    created_at DATE DEFAULT SYSDATE NOT NULL,
    created_by VARCHAR2(64) DEFAULT USER NOT NULL,
    updated_at DATE,
    updated_by VARCHAR2(64),
    deleted_at DATE,
    deleted_by VARCHAR2(64)
);


CREATE TABLE account(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name VARCHAR2(128) NOT NULL UNIQUE,
    account_number VARCHAR2(32) NOT NULL UNIQUE,

    account_type_id INTEGER,
    CONSTRAINT fk_account_account_type_id
        FOREIGN KEY (account_type_id)
            REFERENCES account_type(id),

    account_status_id INTEGER,
    CONSTRAINT fk_account_account_status_id
        FOREIGN KEY (account_status_id)
            REFERENCES account_status(id),

    created_at DATE DEFAULT SYSDATE NOT NULL,
    created_by VARCHAR2(64) DEFAULT USER NOT NULL,
    updated_at DATE,
    updated_by VARCHAR2(64),
    deleted_at DATE,
    deleted_by VARCHAR2(64)
);


CREATE TABLE account_balance(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    balance NUMBER(11,2) NOT NULL CHECK (balance >= 0.00),

    account_id INTEGER,
    CONSTRAINT fk_account_balance_account_id
        FOREIGN KEY (account_id)
            REFERENCES account(id),

    currency_id INTEGER,
    CONSTRAINT fk_account_balance_currency_id
        FOREIGN KEY (currency_id)
            REFERENCES currency(id),

    created_at DATE DEFAULT SYSDATE NOT NULL,
    created_by VARCHAR2(64) DEFAULT USER NOT NULL,
    updated_at DATE,
    updated_by VARCHAR2(64),
    deleted_at DATE,
    deleted_by VARCHAR2(64)
);


