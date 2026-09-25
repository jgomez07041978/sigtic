package mx.gob.tabasco.cmaig.sigtic.exception;

import java.io.Serializable;

public final class ReCaptchaInvalidException extends RuntimeException implements Serializable{

    private static final long serialVersionUID = 5496362961143827874L;

    public ReCaptchaInvalidException() {
        super();
    }

    public ReCaptchaInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaInvalidException(final String message) {
        super(message);
    }

    public ReCaptchaInvalidException(final Throwable cause) {
        super(cause);
    }
}
