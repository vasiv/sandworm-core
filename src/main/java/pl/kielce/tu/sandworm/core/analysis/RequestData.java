package pl.kielce.tu.sandworm.core.analysis;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.HOST_HEADER_NAME;

@Component
public class RequestData {

    private static final String SOURCE_ADDRESS_HEADER_NAME = "X-Forwarded-For";
    private static final String SOURCE_PORT_HEADER_NAME = "X-Forwarded-Port";
    private String sourceAddress;
    private String sourcePort;
    private String destinationAddress;
    private String destinationPort;
    private String body;

    public RequestData() {
    }

    public RequestData(HttpServletRequest request, String body) {
        sourceAddress = request.getHeader(SOURCE_ADDRESS_HEADER_NAME);
        sourcePort = request.getHeader(SOURCE_PORT_HEADER_NAME);
        destinationAddress = request.getHeader(HOST_HEADER_NAME);
        destinationPort = String.valueOf(request.getServerPort());
        this.body = body;
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

    public String getBody() {
        return body;
    }
}
