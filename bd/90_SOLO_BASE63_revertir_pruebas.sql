-- 90_SOLO_BASE63_revertir_pruebas.sql
-- *** NO SE CORRE EN PRODUCCION. Solo en la base .63 (10.2.8.63 / SIGGET11) del desarrollador. ***
-- Durante las pruebas se paso a historica la carta responsiva 21-000016-25 (id 23) de Fabiola Garcia Leon,
-- para poder agregar su correo a una carta nueva. Este script la deja vigente otra vez.
-- Nota: al agregar el servicio a la carta nueva, USUARIOS_SERVICIOS_TIC.ID_CARTA_RESPONSIVA_DETALLE (servicio 112164, usuario-servicio 14829)
-- apunta al detalle nuevo; si hace falta devolverlo al detalle 9, descomentar el ultimo UPDATE.
UPDATE SIGGET_SAI_SIGTIC.CARTA_RESPONSIVA_DETALLE SET CVE_ESTATUS = 'VIG' WHERE ID_CARTA_RESPONSIVA_DETALLE = 9;
UPDATE SIGGET_SAI_SIGTIC.CARTA_RESPONSIVA SET CVE_ESTATUS = 'VIG' WHERE ID_CARTA_RESPONSIVA = 23;
-- UPDATE SIGGET_SAI_SIGTIC.USUARIOS_SERVICIOS_TIC SET ID_CARTA_RESPONSIVA_DETALLE = 9 WHERE ID_USUARIO_SERVICIO_TIC = 14829;
COMMIT;
