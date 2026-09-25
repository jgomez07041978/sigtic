package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrganigramaBean implements Serializable {

    private String name;
    private String title;
    private Long id;
    private Long idPadre;
    private List<OrganigramaBean> children;
}
