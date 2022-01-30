package pl.kielce.tu.sandworm.core.service.proxy;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

public interface ProxyService {

    ResponseEntity<String> proxyRequest(String body, HttpMethod method, HttpServletRequest request,
                                        HttpServletResponse response) throws URISyntaxException;
}
