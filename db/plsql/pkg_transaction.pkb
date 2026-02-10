CREATE OR REPLACE PACKAGE BODY pkg_transaction AS

    PROCEDURE create_transaction (
        p_account_from IN transactions.account_from%TYPE,
        p_account_to   IN transactions.account_to%TYPE,
        p_amount       IN transactions.amount%TYPE,
        p_transaction_id OUT transactions.id%TYPE
    ) IS
    BEGIN
        INSERT INTO transactions (
            id,
            account_from,
            account_to,
            amount,
            status,
            created_at
        ) VALUES (
            transactions_seq.NEXTVAL,
            p_account_from,
            p_account_to,
            p_amount,
            'PENDING',
            SYSTIMESTAMP
        )
        RETURNING id INTO p_transaction_id;

        COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END create_transaction;

    PROCEDURE update_transaction_status (
        p_transaction_id IN transactions.id%TYPE,
        p_status         IN transactions.status%TYPE
    ) IS
    BEGIN
        UPDATE transactions
        SET status = p_status
        WHERE id = p_transaction_id;

        COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END update_transaction_status;

END pkg_transaction;
/