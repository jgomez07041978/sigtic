# Despliegue: cartas responsivas (Correos > Personal, vista previa, logo y formato nuevo)

Rama: `david_garcia`. Este documento es para quien despliegue en otra base/servidor.
**Git solo lleva el codigo. Los cambios de base de datos y el archivo del logo se aplican a mano, con estos scripts.**

## 0. Que cambia (resumen)
| Parte | Cambio |
|---|---|
| Reporte (`carta_responsiva_aceptada_2.jrxml` + `.jasper`) | Logo arriba y mas pequeño; titulo/folio/fecha debajo; marca "EN CAPTURA"; el 4.º firmante / 4.ª fila solo se imprimen si existen; encabezados de la tabla `[[TABLA]]` salen de la plantilla |
| Servicio (`CartaResponsivaServiceImp`) | Variables nuevas en el texto: superior, cargo del superior, unidad, ente, CURP, RFC, correo, telefono, extension; vista previa (ojito) con elegir plantilla |
| Controlador + 2 `.js` | Boton "ver carta" (ojito) en Servicios TIC y en Mis Cartas |
| Base de datos | Plantillas de Correos > Personal (autografa + e.firma), logo oficial en las plantillas |
| Archivos del servidor | `oficial.png` en `/archivos_sigtic/logos/` |

## 1. Orden de despliegue
1. Respaldar la base (o al menos correr `00_respaldo.sql`).
2. Correr `01_precheck_estructura.sql` y comparar con la seccion 2. **Si falta alguna columna, parar y avisar** (no continuar).
3. Desplegar el codigo (build + reinicio de la aplicacion) con la rama.
4. Subir `oficial.png` desde la pantalla de logos de la aplicacion y confirmar el archivo en el servidor (seccion 5).
5. Correr `02_plantillas_correo_personal.sql`.
6. Correr `03_logo_plantillas.sql`.
7. Verificar (seccion 6).

Cada script corre con F5 (Run Script) en SQL Developer, en la conexion donde viven las tablas de plantillas. Todos se detienen con un mensaje, sin cambiar nada, si algo no cuadra.

## 2. Estructura que debe existir (resultado esperado del precheck)
- `CAT_PLANTILLA_REPORTE`: `FIRMADA`, `ID_LOGO`, `TITULO_REPORTE`, `NOMBRE_REPORTE`, `ID_ENTE_PUBLICO`, `ID_SUBTIPO_SERVICIO`, `CVE_ESTATUS` (7)
- `CAT_PLANTILLA_REPORTE_PARRAFO`: `ORDEN`, `PARRAFO`, `CVE_ESTATUS` (3)
- `CARTA_RESPONSIVA`: `FIRMADA`, `ID_LOGO`, `NOMBRE_IMAGEN`, `RUTA_IMAGEN`, `NOMBRE_REPORTE`, `TITULO_REPORTE`, `ID_PLANTILLA_REPORTE` (7)
- Secuencias `SEQ_PLANTILLA_REPORTE` y `SEQ_PLANTILLA_REPORTE_PARRAFO`.
- Una plantilla autografa (`FIRMADA='S'`) vigente para ente 21 / subtipo 40.

**Importante:** en la base `10.2.8.54` (`ORCLPDB1`) se detecto antes que NO existia la columna `FIRMADA`. Si el destino es esa base, falta primero el cambio de estructura que agrego `FIRMADA` y el resto de las columnas de logo; ese cambio no esta en estos scripts.
Los numeros de ente (21 = Secretaria de Administracion y Finanzas) y subtipo (40 = Personal) deben coincidir en el destino; el precheck (c) lo muestra.

## 3. Reglas que el sistema exige (por eso los scripts validan)
- Para cada ente + subtipo + tipo de firma debe haber **una sola** plantilla vigente; si hay mas, la carta marca "Existe mas de una plantilla disponible".
- Tipo de firma: `S` = autografa (`[[FIRMAS]]`), `N` = e.firma (`[[EFIRMA]]`).

## 4. Compilar el reporte (solo si se vuelve a editar el `.jrxml`)
El `.jasper` compilado ya va en el repositorio. Si se modifica el `.jrxml`, hay que recompilarlo con JasperReports **6.20.x** (la misma version que el proyecto):
`java -cp ".:<classpath>" Compile carta_responsiva_aceptada_2.jrxml carta_responsiva_aceptada.jasper`

## 5. Logo en el servidor
- Subir `oficial.png` desde la pantalla de logos. El archivo queda en `/archivos_sigtic/logos/<nombre numerico>.png`.
- Verificar: `ls -la /archivos_sigtic/logos/` y que el usuario que ejecuta la aplicacion pueda leerlo.
- En `CAT_IMAGENES`, `RUTA_IMAGEN` debe ser `/archivos_sigtic/logos/<archivo>`. Si trae una ruta de Windows (`C:\...`), el logo no sale en el PDF y hay que corregirla a la ruta de Linux.

## 6. Verificacion despues de desplegar
1. Servicios TIC > Carta Responsiva: abrir una carta de **Correos > Personal** y usar el ojito. Debe verse: titulo "Correo Electronico Institucional", logo arriba, tabla Solicitud / Servicio, datos reales del usuario en el primer parrafo y marca "EN CAPTURA".
2. En **e.firma**: tabla de firmantes con 3 filas. En **autografa**: 3 firmas.
3. Una carta de **Acceso a Base de Datos**: debe verse como antes (4 firmas / 4 filas, tabla Solicitud / Base de Datos).
4. Una carta ya aceptada: el PDF de "carta responsiva aceptada" se genera sin error.
5. Si algun usuario no tiene titular de unidad registrado, el PDF muestra `##NombreSuperior##` / `##CargoSuperior##` tal cual (comportamiento acordado); lo mismo con `##ExtTelefono##` si no tiene extension.

## 7. Pendientes conocidos (no son parte de este despliegue)
- Las plantillas de Acceso a Base de Datos traen `##NoSolicitud##` y `##NombreBaseDatos##`; el codigo todavia no las sustituye (ya era asi).
- Las cartas **ya aceptadas** conservan el formato con el que se aceptaron (guardan una copia del texto, titulo y logo). Cambiarlas requiere autorizacion y un script aparte.
- Hay dos plantillas vigentes para el subtipo 248 (44 y 48, ambas autografa). Conviene dar de baja una.

## 8. Marcha atras
- Plantillas: restaurar desde `BK_PLANT_REP_<fecha>` (plantillas) y `BK_PLANT_REP_PARR_<fecha>` (parrafos), los respaldos de `00_respaldo.sql`.
- Codigo: revertir el commit.
