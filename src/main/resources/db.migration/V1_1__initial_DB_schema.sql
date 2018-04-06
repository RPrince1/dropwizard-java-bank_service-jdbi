--Drop Tables if Exist
DROP TYPE IF EXISTS bank;

-- Create Table:
CREATE TABLE bank
(
  name	                CHARACTER VARYING NOT NULL,
  address               CHARACTER VARYING NOT NULL,
  sortcode              INT PRIMARY KEY,
  branch_number         INT

)
WITH (
OIDS = FALSE
);