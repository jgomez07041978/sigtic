-- 00_respaldo.sql
-- Respalda las tablas de plantillas ANTES de cambiar nada. Se puede correr varias veces:
-- si el respaldo de hoy ya existe, lo deja como esta.
-- Ejecutar con F5 (Run Script) en la conexion donde viven CAT_PLANTILLA_REPORTE y CAT_PLANTILLA_REPORTE_PARRAFO.
SET SERVEROUTPUT ON
DECLARE
  v_fecha VARCHAR2(8) := TO_CHAR(SYSDATE, 'YYYYMMDD');
  PROCEDURE respaldar(p_tabla VARCHAR2, p_respaldo VARCHAR2) IS
  BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE ' || p_respaldo || '_' || v_fecha || ' AS SELECT * FROM ' || p_tabla;
    DBMS_OUTPUT.PUT_LINE('Respaldo creado: ' || p_respaldo || '_' || v_fecha);
  EXCEPTION WHEN OTHERS THEN
    IF SQLCODE = -955 THEN
      DBMS_OUTPUT.PUT_LINE('Ya existia el respaldo de hoy: ' || p_respaldo || '_' || v_fecha || ' (no se toca).');
    ELSE
      RAISE;
    END IF;
  END;
BEGIN
  respaldar('CAT_PLANTILLA_REPORTE',         'BK_PLANT_REP');
  respaldar('CAT_PLANTILLA_REPORTE_PARRAFO', 'BK_PLANT_REP_PARR');
END;
/
