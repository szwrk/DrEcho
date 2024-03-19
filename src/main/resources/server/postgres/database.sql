-- Database: postgres

-- DROP DATABASE IF EXISTS postgres;

CREATE DATABASE postgres
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE postgres
    IS 'default administrative connection database';

GRANT TEMPORARY, CONNECT ON DATABASE postgres TO PUBLIC;

GRANT ALL ON DATABASE postgres TO arkwil;

GRANT ALL ON DATABASE postgres TO postgres;