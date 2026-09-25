package mx.gob.tabasco.cmaig.sigtic.sas.service;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

public interface RequestService {
    String getClientIp(HttpServletRequest request);
    String getClientHost(HttpServletRequest request) throws UnknownHostException;
}
