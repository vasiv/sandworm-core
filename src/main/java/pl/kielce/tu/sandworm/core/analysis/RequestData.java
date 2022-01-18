package pl.kielce.tu.sandworm.core.analysis;

import javax.servlet.http.HttpServletRequest;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.*;

public class RequestData {

    private final String sourceAddress;
    private final String sourcePort;
    private final String destinationAddress;
    private final String destinationPort;
    private final String body;

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
