package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

public class Header {

    private final Protocol protocol;
    private final String sourceAddress;
    private final String sourcePort;
    private final Direction direction;
    private final String destinationAddress;
    private final String destinationPort;

    private Header(Builder builder) {
        protocol = builder.protocol;
        sourceAddress = builder.sourceAddress;
        sourcePort = builder.sourcePort;
        direction = builder.direction;
        destinationAddress = builder.destinationAddress;
        destinationPort = builder.destinationPort;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public static class Builder {

        private Protocol protocol;
        private String sourceAddress;
        private String sourcePort;
        private Direction direction;
        private String destinationAddress;
        private String destinationPort;

        public Builder withProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder withSourceAddress(String sourceAddress) {
            this.sourceAddress = sourceAddress;
            return this;
        }

        public Builder withSourcePort(String sourcePort) {
            this.sourcePort = sourcePort;
            return this;
        }

        public Builder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder withDestinationAddress(String destinationAddress) {
            this.destinationAddress = destinationAddress;
            return this;
        }

        public Builder withDestinationPort(String destinationPort) {
            this.destinationPort = destinationPort;
            return this;
        }

        public Header build() {
            return new Header(this);
        }
    }

}
