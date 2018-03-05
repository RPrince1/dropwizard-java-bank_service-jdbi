--Drop Tables if Exist
DROP TYPE IF EXISTS customer_transactions;

-- Create Table:
CREATE TABLE customer_transactions
(
  transaction_id	    UUID PRIMARY KEY,
  account_id            CHARACTER VARYING NOT NULL,
  amount                DECIMAL
)
WITH (
OIDS = FALSE
);