package mx.gob.tabasco.cmaig.sigtic.sai.service;

public interface BitacoraMovimientoService {
    void guardarBitacoraMovimiento(Long idMovimiento, String tablaMovimiento, String descripcionMovimiento);

    void guardarBitacoraMovimiento(Long idMovimiento, String tablaMovimiento, String descripcionMovimiento, Long idUsuarioEntePublico);
}
