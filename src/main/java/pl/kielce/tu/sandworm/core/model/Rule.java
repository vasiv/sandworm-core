package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.COMMA;

public record Rule(Action action, Header header, Options options) {

    public Action getAction() {
        return action;
    }

    public Protocol getProtocol() {
        return header.protocol;
    }

    public String getSourceAddress() {
        return header.sourceAddress;
    }

    public String getSourcePort() {
        return header.sourcePort;
    }

    public Direction getDirection() {
        return header.direction;
    }

    public String getDestinationAddress() {
        return header.destinationAddress;
    }

    public String getDestinationPort() {
        return header.destinationPort;
    }

    public Threshold getThreshold() {
        return options.threshold;
    }

    public Set<Option> getOptions() {
        return options.payloadMatchers;
    }

    public enum Action {
        PASS, ALERT, DROP
    }

    public static class Header {

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

    public static class Options {

        private static final String SID = "sid";
        private static final String REV = "rev";
        private static final String MSG = "msg";
        private static final String THRESHOLD = "threshold";
        private static final String CONTENT = "content";

        private int id;
        private int revision;
        private String message;
        private Threshold threshold;
        private Set<Option> payloadMatchers;


        public Options(Set<Option> options) {
            this.payloadMatchers = new HashSet<>();
            options.forEach(option -> {
                switch (option.name()) {
                    case SID -> id = Integer.parseInt(option.value());
                    case REV -> revision = Integer.parseInt(option.value());
                    case MSG -> message = option.value();
                    case THRESHOLD -> threshold = getThreshold(option.value());
                    case CONTENT -> payloadMatchers.add(option);
                    default -> throw new IllegalStateException("Unexpected value: " + option.name());
                    //todo maybe throw custom one
                }
            });
        }

        private Threshold getThreshold(String thresholdOption) {
            Map<String, String> settings = transferToMap(thresholdOption);
            return new Threshold(settings);
        }

        private Map<String, String> transferToMap(String thresholdSettings) {
            Map<String, String> map = new HashMap<>();
            Stream.of(thresholdSettings.split(COMMA))
                    .map(String::trim)
                    .map(setting -> setting.split(SPACE))
                    .forEach(array -> map.put(array[0], array[1]));
            return map;
        }
    }
}
