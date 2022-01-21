package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import java.util.Set;

public class Rule {

    private final Action action;
    private final Protocol protocol;
    private final String sourceAddress;
    private final String sourcePort;
    private final Direction direction;
    private final String destinationAddress;
    private final String destinationPort;
    private final Threshold threshold;
    private final Set<Option> options;

    private Rule(RuleBuilder builder) {
        this.action = builder.action;
        this.protocol = builder.protocol;
        this.sourceAddress = builder.sourceAddress;
        this.sourcePort = builder.sourcePort;
        this.direction = builder.direction;
        this.destinationAddress = builder.destinationAddress;
        this.destinationPort = builder.destinationPort;
        this.threshold = builder.threshold;
        this.options = builder.options;
    }

    public Action getAction() {
        return action;
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

    public Threshold getThreshold() {
        return threshold;
    }

    public Set<Option> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "action=" + action +
                ", protocol=" + protocol +
                ", sourceAddress='" + sourceAddress + '\'' +
                ", sourcePort=" + sourcePort +
                ", direction=" + direction +
                ", destinationAddress='" + destinationAddress + '\'' +
                ", destinationPort=" + destinationPort +
                ", threshold=" + threshold +
                ", options=" + options +
                '}';
    }

    public static class RuleBuilder {

        private final Action action;
        private final Protocol protocol;
        private final String sourceAddress;
        private final String sourcePort;
        private final Direction direction;
        private final String destinationAddress;
        private final String destinationPort;
        private Threshold threshold;
        private Set<Option> options;

        public RuleBuilder(Action action, Protocol protocol, String sourceAddress, String sourcePort,
                           Direction direction, String destinationAddress, String destinationPort) {
            this.action = action;
            this.protocol = protocol;
            this.sourceAddress = sourceAddress;
            this.sourcePort = sourcePort;
            this.direction = direction;
            this.destinationAddress = destinationAddress;
            this.destinationPort = destinationPort;
        }

        public RuleBuilder withOptions(Set<Option> options) {
            this.options = options;
            return this;
        }

        public RuleBuilder withThreshold(Threshold threshold) {
            this.threshold = threshold;
            return this;
        }

        public Rule build() {
            return new Rule(this);
        }
    }
}
