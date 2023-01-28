INSERT INTO users (user_id, username, password)
VALUES (1, 'hikingcarrot7',
        '$2a$10$W3J4dsdxKS.vWkdwsnYTMeMTXH8nFPB12ETrxdgYMwLYC7eA8vSIG');

INSERT INTO piggy_banks (piggy_bank_id, owner_user_id, name, borrowedAmount,
                         initialAmount, startSavings, endSavings, createdAt)
VALUES (1, 1, 'myPiggy', 0.00, 0.00, '2023-01-01', '2023-01-31',
        '2023-01-23 22:19:24.012000');

INSERT INTO daily_savings (piggy_bank_id, amount, date, description)
VALUES (1, 100.00, '2023-01-01', 'Sample description'),
       (1, 100.00, '2023-01-02', 'Sample description'),
       (1, 100.00, '2023-01-03', 'Sample description'),
       (1, 100.00, '2023-01-04', 'Sample description'),
       (1, 100.00, '2023-01-05', 'Sample description'),
       (1, 100.00, '2023-01-06', 'Sample description'),
       (1, 100.00, '2023-01-07', 'Sample description'),
       (1, 100.00, '2023-01-08', 'Sample description'),
       (1, 100.00, '2023-01-09', 'Sample description'),
       (1, 100.00, '2023-01-10', 'Sample description'),
       (1, 100.00, '2023-01-11', 'Sample description'),
       (1, 100.00, '2023-01-12', 'Sample description'),
       (1, 100.00, '2023-01-13', 'Sample description'),
       (1, 100.00, '2023-01-14', 'Sample description'),
       (1, 100.00, '2023-01-15', 'Sample description'),
       (1, 100.00, '2023-01-16', 'Sample description'),
       (1, 100.00, '2023-01-17', 'Sample description'),
       (1, 100.00, '2023-01-18', 'Sample description'),
       (1, 100.00, '2023-01-19', 'Sample description'),
       (1, 100.00, '2023-01-20', 'Sample description'),
       (1, 100.00, '2023-01-21', 'Sample description'),
       (1, 100.00, '2023-01-22', 'Sample description'),
       (1, 100.00, '2023-01-23', 'Sample description'),
       (1, 100.00, '2023-01-24', 'Sample description'),
       (1, 100.00, '2023-01-25', 'Sample description'),
       (1, 100.00, '2023-01-26', 'Sample description'),
       (1, 100.00, '2023-01-27', 'Sample description'),
       (1, 100.00, '2023-01-28', 'Sample description'),
       (1, 100.00, '2023-01-29', 'Sample description'),
       (1, 100.00, '2023-01-30', 'Sample description');
