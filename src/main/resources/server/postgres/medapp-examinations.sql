CREATE TABLE IF NOT EXISTS medapp.examinations
(
    exam_id
    integer
    NOT
    NULL,
    exam_type
    character
    varying
(
    10
) COLLATE pg_catalog."default" NOT NULL,
    patient_id integer NOT NULL,
    visit_id integer,
    start_time timestamp with time zone,
    created_at timestamp with time zone,
                             created_by integer,
                             CONSTRAINT examination_pkey PRIMARY KEY (exam_id)
    )
    TABLESPACE medapp_tables;

ALTER TABLE IF EXISTS medapp.examinations
    OWNER to postgres;

COMMENT
ON TABLE medapp.examinations
    IS 'Examinations';