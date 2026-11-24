-- 02_plantillas_correo_personal.sql
-- Correos Electronicos > Personal (ente 21, subtipo 40).
-- 1) Si no existe la plantilla e.firma (N), la crea copiando la autografa (S).
-- 2) Fija el titulo "Correo Electrónico Institucional".
-- 3) Reemplaza TODOS los parrafos de las dos plantillas por el texto del documento "CARTA RESPONSIVA DE SERVICIO":
--    tabla Solicitud = nombre del solicitante / Servicio = Correo Electrónico Institucional Personal;
--    firmantes: solicitante, quien entrega y quien autoriza (3 firmas). Todo en una sola transaccion (COMMIT al final).
-- Antes de correr: 00_respaldo.sql y 01_precheck_estructura.sql. Ejecutar con F5 (Run Script).
-- Si algo no cuadra, el script se detiene con un mensaje y NO cambia nada.
SET SERVEROUTPUT ON
DECLARE
  v_s NUMBER; v_n NUMBER; v_max NUMBER; v_seq NUMBER;
  v_firmas VARCHAR2(4000);
  r CAT_PLANTILLA_REPORTE%ROWTYPE;

  PROCEDURE add_par(p_id NUMBER, p_orden NUMBER, p_txt VARCHAR2) IS
  BEGIN
    INSERT INTO CAT_PLANTILLA_REPORTE_PARRAFO
      (ID_PLANTILLA_REPORTE_PARRAFO, ID_PLANTILLA_REPORTE, PARRAFO, ORDEN, FECHA_REGISTRO, CVE_ESTATUS, FECHA_ULTIMO_CAMBIO)
    VALUES
      (SEQ_PLANTILLA_REPORTE_PARRAFO.NEXTVAL, p_id, p_txt, p_orden, SYSDATE, 'VIG', SYSDATE);
  END;
BEGIN
  -- Debe existir exactamente 1 plantilla autografa (S) vigente; la e.firma (N) se crea si falta.
  SELECT COUNT(*) INTO v_s FROM CAT_PLANTILLA_REPORTE
   WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG' AND FIRMADA = 'S';
  SELECT COUNT(*) INTO v_n FROM CAT_PLANTILLA_REPORTE
   WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG' AND FIRMADA = 'N';
  IF v_s <> 1 THEN
    RAISE_APPLICATION_ERROR(-20010, 'Se esperaba 1 plantilla autografa (S) vigente para Correos > Personal (ente 21, subtipo 40). Hay ' || v_s || '. No se cambio nada.');
  END IF;
  IF v_n > 1 THEN
    RAISE_APPLICATION_ERROR(-20011, 'Hay ' || v_n || ' plantillas e.firma (N) vigentes; debe haber maximo 1. No se cambio nada.');
  END IF;

  IF v_n = 0 THEN
    SELECT NVL(MAX(ID_PLANTILLA_REPORTE), 0) INTO v_max FROM CAT_PLANTILLA_REPORTE;
    SELECT SEQ_PLANTILLA_REPORTE.NEXTVAL INTO v_seq FROM DUAL;
    IF v_seq <= v_max THEN
      RAISE_APPLICATION_ERROR(-20013, 'La secuencia de plantillas (' || v_seq || ') va por detras del maximo ID (' || v_max || '). No se cambio nada.');
    END IF;
    -- La e.firma se crea como copia de la autografa (mismo logo, firmantes, etc.), cambiando solo el ID y el tipo de firma.
    SELECT * INTO r FROM CAT_PLANTILLA_REPORTE
     WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG' AND FIRMADA = 'S';
    r.ID_PLANTILLA_REPORTE := v_seq;
    r.FIRMADA := 'N';
    INSERT INTO CAT_PLANTILLA_REPORTE VALUES r;
  END IF;

  -- Titulo que muestra el reporte (si esta vacio usaria el nombre del reporte)
  UPDATE CAT_PLANTILLA_REPORTE SET TITULO_REPORTE = 'Correo Electrónico Institucional'
   WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG';

  SELECT NVL(MAX(ID_PLANTILLA_REPORTE_PARRAFO), 0) INTO v_max FROM CAT_PLANTILLA_REPORTE_PARRAFO;
  SELECT SEQ_PLANTILLA_REPORTE_PARRAFO.NEXTVAL INTO v_seq FROM DUAL;
  IF v_seq <= v_max THEN
    RAISE_APPLICATION_ERROR(-20012, 'La secuencia de párrafos (' || v_seq || ') va por detrás del máximo ID (' || v_max || '). No se cambió nada.');
  END IF;

  v_firmas := q'~|##NombreResponsable##|##CargoResponsable##|Persona Servidora Pública que Acepta y Recibe|Ervey Ramón Ábalos|Director de Infraestructura Tecnológica|Persona Servidora Pública que Entrega|Juan Roberto Rosales Vargas|Director General de Enlace Institucional de Tecnologías de la Información y Comunicaciones|Persona Servidora Pública que Autoriza~';

  -- Se retiran los párrafos actuales de las dos plantillas (respaldo: el CSV que exportaste de la #1)
  DELETE FROM CAT_PLANTILLA_REPORTE_PARRAFO
   WHERE ID_PLANTILLA_REPORTE IN (SELECT ID_PLANTILLA_REPORTE FROM CAT_PLANTILLA_REPORTE
                                   WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG');

  FOR t IN (SELECT ID_PLANTILLA_REPORTE AS ID, FIRMADA AS TIPO
              FROM CAT_PLANTILLA_REPORTE
             WHERE ID_ENTE_PUBLICO = 21 AND ID_SUBTIPO_SERVICIO = 40 AND CVE_ESTATUS = 'VIG') LOOP

    add_par(t.ID, 1,  q'~<p>Por este medio la Persona Servidora Pública que suscribe <b>##NombreResponsable##</b>, <b>##CargoResponsable##</b>, bajo la supervisión directa de <b>##NombreSuperior##</b>, <b>##CargoSuperior##</b> adscrito a la <b>##UnidadAdministrativa##</b> perteneciente a la <b>##NombreEnte##</b> del <b>Gobierno del Estado de Tabasco</b>; con Clave Única de Registro de Población (CURP) <b>##CURP##</b>, Registro Federal de Contribuyentes (RFC) <b>##RFC##</b>, con correo institucional <b>##CorreoInstitucional##</b> y teléfono institucional <b>##TelefonoInstitucional##</b> con ext. <b>##ExtTelefono##</b>; declaro que asumo la responsabilidad que conlleva el servicio solicitado, con efecto inmediato tras la firma de la presente.</p>~');
    add_par(t.ID, 2,  q'~[[TABLA]]|Solicitud|Servicio|##NombreResponsable##|Correo Electrónico Institucional Personal~');
    add_par(t.ID, 3,  q'~<p>Estoy informado y acepto que las credenciales de acceso proporcionadas, son estrictamente para uso personal e intransferible, por lo que me veo obligado a protegerlas, reservarlas, resguardarlas y no divulgarlas.</p>~');
    add_par(t.ID, 4,  q'~<p>Me comprometo a informar a la Persona Servidora Pública Titular del servidor donde se aloje la base de datos, en caso de robo o extravío de las credenciales de acceso bajo mi resguardo o en cualquier otro hecho relacionado con las mismas, que pudiera constituirse como delito. Seguidamente deberé presentar la denuncia correspondiente ante las autoridades competentes. Así como también, informar sobre cualquier incidente, vulneración o problemática relacionada, para ejecutar las acciones de seguridad pertinentes, mediante un oficio donde se informe de los hechos con una breve descripción y evidencias recabadas.</p>~');
    add_par(t.ID, 5,  q'~<p><b>Compromiso de Confidencialidad, Responsabilidad de Gestión y Uso</b></p>~');
    add_par(t.ID, 6,  q'~<p>Declaro que tengo el pleno conocimiento que, en caso de tener acceso a datos personales que obren en la información alojada en la base de datos, que sea responsabilidad de la Agencia Estatal de Transformación Digital; y derivado del reconocimiento expreso que, este servicio es únicamente para uso exclusivo de la Dependencia o Institución descrita en la presente; estoy obligado a cumplir con las disposiciones que establece la Ley General de Transparencia y Acceso a la Información Pública, la Ley General de Protección de Datos Personales en Posesión de Sujetos Obligados, Ley de Transparencia y Acceso a la Información Pública del Estado de Tabasco, Ley de Protección de Datos Personales en Posesión de Sujetos Obligados del Estado de Tabasco y demás disposiciones que resulten aplicables.</p>~');
    add_par(t.ID, 7,  q'~<p>Por lo tanto, estoy obligado a informar a la Persona Servidora Pública que Entrega, cuando se detecte alguna vulneración en los servicios de infraestructura y/o cómputo que puedan comprometer las credenciales de acceso, ocasionando cualquier alteración, sustracción o pérdida de datos. Las obligaciones contempladas en este documento permanecerán vigentes y serán exigibles aún en el caso de que se dé por terminada la operación y/o administración de las credenciales de acceso.</p>~');
    add_par(t.ID, 8,  q'~<p>En virtud de lo anterior, la presente Carta Responsiva deberá actualizarse anualmente, por cambio de administración o cuando por alguna causa la Persona Servidora Pública responsable de las credenciales de acceso cambie de adscripción y/o deje de prestar sus servicios en la dependencia a la que pertenece, conforme a lo establecido en los Artículos 2, 3 y demás aplicables de la Ley que Establece los Procedimientos de Entrega y Recepción en los Poderes Públicos, Ayuntamientos y Órganos Constitucionales Autónomos del Estado de Tabasco vigente.</p>~');
    add_par(t.ID, 9,  q'~<p><b>Suspensión del Servicio</b></p>~');
    add_par(t.ID, 10, q'~<p>El incumplimiento de las obligaciones previstas en la presente y/o la detección de cualquier actividad inusual con el uso y/o administración de las credenciales de acceso asignadas, será causal de la inhabilitación de dichas credenciales, la cual se notificará a través del correo electrónico institucional proporcionado por la Persona Servidora Pública que suscribe.</p>~');
    add_par(t.ID, 11, q'~<p>Si la actividad inusual se originó por una vulneración en la infraestructura o en los servicios informáticos, se asumirá que no hubo intención dolosa en el uso anómalo. No obstante, las credenciales permanecerán inhabilitadas hasta que la Persona Servidora Pública responsable demuestre satisfactoriamente a la Agencia Estatal de Transformación Digital que ha resuelto la vulnerabilidad. Además, deberá acreditar que ha implementado medidas de seguridad técnicas, físicas o administrativas reforzadas para prevenir cualquier daño, sustracción, robo o uso indebido futuro.</p>~');
    add_par(t.ID, 12, q'~<p><b>Causales de terminación de la operación de los servicios, de manera enunciativa más no limitativa:</b></p>~');
    add_par(t.ID, 13, q'~<ol><li>La notificación de forma expresa y voluntaria de concluir la operación del servicio.</li><li>La divulgación de credenciales de acceso a través de cualquier medio.</li><li>El uso de credenciales de acceso para cualquier beneficio económico que implique lucro.</li><li>El uso de credenciales de acceso distinto al autorizado.</li><li>El incumplimiento con la obligación de mantener la confidencialidad, reserva y no difusión de información, conforme a las disposiciones legales aplicables.</li></ol>~');
    add_par(t.ID, 14, q'~<p>En caso de acreditarse la participación, autorización, realización, ejecución o comisión por parte de la Persona Servidora Pública responsable de las credenciales de acceso en alguna de las causales 2, 3, 4 y 5 antes descritas, la inhabilitación de sus credenciales será permanente, lo cual se notificará a través del correo electrónico institucional señalado en la presente.</p>~');
    add_par(t.ID, 15, q'~<p>Enterado del contenido de este documento, y en este acto, asumo el compromiso y la responsabilidad de conducirme bajo los principios éticos que rigen el servicio público de conformidad con los Artículos 7, 16, 49 fracción V, 51, 55, 56 y demás aplicables de la Ley General de Responsabilidades Administrativas en vigor; hacer buen uso de las credenciales de acceso que me han sido asignadas, con mi firma autógrafa y/o Firma Electrónica Avanzada en los términos previstos en los Artículos 1, fracciones I, IV, V; 48, 49 fracción I de la Ley de Gobierno Digital y Firma Electrónica para el Estado de Tabasco y sus Municipios vigente.</p>~');

    IF t.TIPO = 'N' THEN
      add_par(t.ID, 16, '[[EFIRMA]]' || v_firmas);
    ELSE
      add_par(t.ID, 16, '[[FIRMAS]]' || v_firmas);
    END IF;
  END LOOP;

  COMMIT;
END;
/

-- Verificacion: 16 parrafos por plantilla (la S termina en [[FIRMAS]], la N en [[EFIRMA]])
SELECT t.ID_PLANTILLA_REPORTE, t.FIRMADA, t.TITULO_REPORTE, p.ORDEN, SUBSTR(p.PARRAFO, 1, 70) AS PARRAFO
  FROM CAT_PLANTILLA_REPORTE t
  JOIN CAT_PLANTILLA_REPORTE_PARRAFO p ON p.ID_PLANTILLA_REPORTE = t.ID_PLANTILLA_REPORTE
 WHERE t.ID_ENTE_PUBLICO = 21 AND t.ID_SUBTIPO_SERVICIO = 40 AND t.CVE_ESTATUS = 'VIG'
 ORDER BY t.FIRMADA, p.ORDEN;
