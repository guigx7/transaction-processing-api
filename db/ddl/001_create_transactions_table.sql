CREATE TABLE transactions (
    id            NUMBER PRIMARY KEY,
    account_from  VARCHAR2(50) NOT NULL,
    account_to    VARCHAR2(50) NOT NULL,
    amount        NUMBER(15,2) NOT NULL,
    status        VARCHAR2(20) NOT NULL,
    created_at    TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);
