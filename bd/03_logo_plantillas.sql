-- 03_logo_plantillas.sql
-- Asigna el logo oficial (oficial.png) a las plantillas de carta responsiva.
-- ANTES de correr este script:
--   1) Subir oficial.png desde la pantalla de logos de la aplicacion (la misma que se uso en la base .63).
--      La aplicacion guarda el archivo en /archivos_sigtic/logos/ del servidor con un nombre numerico (ej. 1795117245360.png).
--   2) Confirmar que ese archivo existe en el servidor:  ls -la /archivos_sigtic/logos/
-- Ejecutar con F5 (Run Script). Si no hay exactamente 1 logo llamado oficial.png, se detiene sin cambiar nada.
SET SERVEROUTPUT ON
DECLARE
  v_n NUMBER; v_logo NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_n FROM CAT_IMAGENES WHERE NOMBRE_IMAGEN = 'oficial.png';
  IF v_n <> 1 THEN
    RAISE_APPLICATION_ERROR(-20020, 'Se esperaba exactamente 1 imagen llamada oficial.png en CAT_IMAGENES y hay ' || v_n || '. Subala desde la aplicacion y vuelve a correr. No se cambio nada.');
  END IF;
  SELECT ID_IMAGEN INTO v_logo FROM CAT_IMAGENES WHERE NOMBRE_IMAGEN = 'oficial.png';

  -- Decision tomada en la base .63: el mismo logo en TODAS las plantillas. Si en destino solo debe ir en algunas,
  -- agregar aqui un WHERE (por ejemplo: WHERE ID_ENTE_PUBLICO = 21).
  UPDATE CAT_PLANTILLA_REPORTE SET ID_LOGO = v_logo;
  DBMS_OUTPUT.PUT_LINE('Logo ' || v_logo || ' asignado a ' || SQL%ROWCOUNT || ' plantillas.');
  COMMIT;
END;
/

-- Verificacion: RUTA_IMAGEN debe ser una ruta de Linux del servidor, del tipo /archivos_sigtic/logos/<archivo>
-- (si trae una ruta de Windows, tipo C:\..., el logo NO saldra en el PDF; ver LEEME_DESPLIEGUE.md, seccion 5).
SELECT * FROM CAT_IMAGENES WHERE NOMBRE_IMAGEN = 'oficial.png';
SELECT ID_LOGO, COUNT(*) AS PLANTILLAS FROM CAT_PLANTILLA_REPORTE GROUP BY ID_LOGO;
