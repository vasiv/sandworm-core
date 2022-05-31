package pl.kielce.tu.sandworm.core.model;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.SPACE;

public class HttpRequest {

    private static final String SOURCE_ADDRESS_HEADER_NAME = "X-Forwarded-For";
    private static final String SOURCE_PORT_HEADER_NAME = "X-Forwarded-Port";
    private static final String HOST_HEADER_NAME = "host";
    private final String sourceAddress;
    private final String sourcePort;
    private final String destinationAddress;
    private final String destinationPort;
    private final String uri;
    private final Map<String, String> headers;
    private final String method;
    private final String body;
    private final long createdAt;

    public HttpRequest(HttpServletRequest request, String body) {
        createdAt = System.currentTimeMillis();
        sourceAddress = request.getHeader(SOURCE_ADDRESS_HEADER_NAME);
        sourcePort = request.getHeader(SOURCE_PORT_HEADER_NAME);
        destinationAddress = request.getHeader(HOST_HEADER_NAME);
        destinationPort = String.valueOf(request.getServerPort());
        uri = request.getRequestURI();
        headers = new HashMap<>();
        initHeaders(request);
        method = request.getMethod();
        this.body = body;
    }

    private void initHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getHeadersValues() {
        return String.join(SPACE, headers.values());
    }
}
