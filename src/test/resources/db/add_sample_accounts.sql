INSERT INTO account (name, account_number, account_type_id, account_status_id) VALUES
('Test account', '1234567890', (SELECT id FROM account_type WHERE name = 'DEBIT'), (SELECT id FROM account_status WHERE name = 'ACTIVE')),
('Blocked test account', 'XXXXXXXXXX', (SELECT id FROM account_type WHERE name = 'DEBIT'), (SELECT id FROM account_status WHERE name = 'BLOCKED'));

INSERT INTO account_balance (amount, account_id, currency_id) VALUES
(1500, (SELECT id FROM account WHERE account_number = '1234567890'), (SELECT id FROM currency WHERE code = 'EUR')),
(80.12, (SELECT id FROM account WHERE account_number = '1234567890'), (SELECT id FROM currency WHERE code = 'DKK')),

(350.50, (SELECT id FROM account WHERE account_number = 'XXXXXXXXXX'), (SELECT id FROM currency WHERE code = 'EUR'));
