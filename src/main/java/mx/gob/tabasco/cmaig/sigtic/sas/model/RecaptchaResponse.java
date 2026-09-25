package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class RecaptchaResponse implements Serializable {

    private boolean success;
    private String challege_ts;
    private String hostname;
    private Double score;
    private String action;

}
