CREATE OR REPLACE PACKAGE pkg_transaction AS

    PROCEDURE create_transaction (
        p_account_from IN transactions.account_from%TYPE,
        p_account_to   IN transactions.account_to%TYPE,
        p_amount       IN transactions.amount%TYPE,
        p_transaction_id OUT transactions.id%TYPE
    );

    PROCEDURE update_transaction_status (
        p_transaction_id IN transactions.id%TYPE,
        p_status         IN transactions.status%TYPE
    );

END pkg_transaction;
/