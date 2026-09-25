package mx.gob.tabasco.cmaig.sigtic.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private Date timestamp;
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }
}