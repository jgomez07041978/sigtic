package mx.gob.tabasco.cmaig.sigtic.sas.pojo;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;

import java.io.Serializable;
import java.util.List;

@Data
public class SegMenuUsuarioBean implements Serializable {
    private SegMenu menuPadre;
    private List<SegMenu> menuHijo;
}