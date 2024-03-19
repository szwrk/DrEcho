-- Table: medapp.examination_types

-- DROP TABLE IF EXISTS medapp.examination_types;

CREATE TABLE IF NOT EXISTS medapp.examination_types
(
    id_exam_type character varying(10) COLLATE pg_catalog."default",
    exam_type_name character varying(100) COLLATE pg_catalog."default"
    )

    TABLESPACE medapp_tables;

ALTER TABLE IF EXISTS medapp.examination_types
    OWNER to postgres;