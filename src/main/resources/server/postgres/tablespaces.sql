CREATE TABLESPACE medapp_tables
  OWNER postgres
  LOCATION '/var/lib/postgresql/data/pg_data_volume';

ALTER TABLESPACE medapp_tables
  OWNER TO postgres;