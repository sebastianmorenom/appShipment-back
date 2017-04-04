/*
CREATE USER AS_ADMIN IDENTIFIED BY oracle12c;
GRANT ALL PRIVILEGES TO AS_ADMIN;
*/ 

CREATE TABLE persona (
  id_persona GENERATED ALWAYS AS IDENTITY (START WITH 1001 INCREMENT BY 1) PRIMARY KEY,
  tipo_id VARCHAR2(2),
  nm_id NUMERIC,
  nm_celular NUMERIC,
  nombre VARCHAR2(50),
  apellido VARCHAR2(50),
  email VARCHAR2(50),
  password VARCHAR2(50),
  fecha_registro DATE,
  calificacion NUMERIC(3,2),
  activo BOOLEAN DEFAULT false
);

CREATE TABLE localizacion (
  
);