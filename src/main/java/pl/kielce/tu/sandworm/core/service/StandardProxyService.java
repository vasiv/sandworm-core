package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import static org.apache.http.HttpHost.DEFAULT_SCHEME_NAME;
import static org.springframework.http.HttpHeaders.ACCEPT_ENCODING;

@Service
public class StandardProxyService implements ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(StandardProxyService.class);

    @Value("${service.proxyService.domain}")
    private String domain;

    @Value("${service.proxyService.port}")
    private int port;

    @Override
    public ResponseEntity<String> proxyRequest(String body, HttpMethod method, HttpServletRequest request,
                                               HttpServletResponse response) throws URISyntaxException {
        URI uri = generateDestinationUri(request);
        HttpHeaders headers = prepareHeaders(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        return proxyRequest(method, uri, httpEntity);
    }

    private URI generateDestinationUri(HttpServletRequest request) throws URISyntaxException {
        String requestUri = request.getRequestURI();
        URI destinationUri = new URI(DEFAULT_SCHEME_NAME, null, domain, port, null, null, null);
        return UriComponentsBuilder.fromUri(destinationUri)
                .path(requestUri)
                .query(request.getQueryString())
                .build(true).toUri();
    }

    private HttpHeaders prepareHeaders(HttpServletRequest request) {
        HttpHeaders headers = copyHeadersFrom(request);
        headers.remove(ACCEPT_ENCODING);
        return headers;
    }

    private HttpHeaders copyHeadersFrom(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private ResponseEntity<String> proxyRequest(HttpMethod method, URI uri, HttpEntity<String> httpEntity) {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            return restTemplate.exchange(uri, method, httpEntity, String.class);
        } catch (HttpStatusCodeException e) {
            logger.error("Cannot proxy request due to: ", e);
            return ResponseEntity.status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }
}
