-- Insert account types
INSERT INTO account_type (name, description) VALUES
    ('DEBIT', 'Debit account for transactions'),
    ('CREDIT', 'Credit account with borrowing limit'),
    ('INVEST', 'Investment account for financial assets');

-- Insert account statuses
INSERT INTO account_status (name, description) VALUES
    ('ACTIVE', 'Account is fully operational'),
    ('CLOSED', 'Account has been permanently closed'),
    ('BLOCKED', 'Account is temporarily restricted'),
    ('FROZEN', 'Account is restricted due to security concerns'),
    ('PENDING', 'Account is awaiting verification');

-- Insert currencies
INSERT INTO currency (name, code, sign) VALUES
    ('Euro', 'EUR', '€'),
    ('US Dollar', 'USD', '$'),
    ('Danish Krone', 'DKK', 'kr'),
    ('British Pound', 'GBP', '£'),
    ('Japanese Yen', 'JPY', '¥'),
    ('Swiss Franc', 'CHF', 'Fr');

-- Insert accounts
INSERT INTO account (name, account_number, account_type_id, account_status_id) VALUES
    ('John Doe Savings', 'DEB123456', (SELECT id FROM account_type WHERE name = 'DEBIT'), (SELECT id FROM account_status WHERE name = 'ACTIVE')),
    ('Jane Smith Credit', 'CRED654321', (SELECT id FROM account_type WHERE name = 'CREDIT'), (SELECT id FROM account_status WHERE name = 'ACTIVE')),
    ('Global Investments', 'INV789101', (SELECT id FROM account_type WHERE name = 'INVEST'), (SELECT id FROM account_status WHERE name = 'BLOCKED')),
    ('Company Payroll', 'DEB112233', (SELECT id FROM account_type WHERE name = 'DEBIT'), (SELECT id FROM account_status WHERE name = 'FROZEN')),
    ('Startup Business', 'CRED445566', (SELECT id FROM account_type WHERE name = 'CREDIT'), (SELECT id FROM account_status WHERE name = 'PENDING'));

-- Insert account balances (Fixed FK references)
INSERT INTO account_balance (balance, account_id, currency_id) VALUES
    (1500.75, (SELECT id FROM account WHERE account_number = 'DEB123456'), (SELECT id FROM currency WHERE code = 'EUR')),
    (2500.00, (SELECT id FROM account WHERE account_number = 'CRED654321'), (SELECT id FROM currency WHERE code = 'USD')),
    (350.50, (SELECT id FROM account WHERE account_number = 'INV789101'), (SELECT id FROM currency WHERE code = 'DKK')),
    (10000.00, (SELECT id FROM account WHERE account_number = 'DEB112233'), (SELECT id FROM currency WHERE code = 'GBP')),
    (500.25, (SELECT id FROM account WHERE account_number = 'CRED445566'), (SELECT id FROM currency WHERE code = 'JPY'));
