package pl.kielce.tu.sandworm.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import java.util.Objects;
import java.util.Set;

@Document(indexName = "rule")
public class Rule {

    @Id
    private String id;
    private Action action;
    private Protocol protocol;
    private String sourceAddress;
    private String sourcePort;
    private Direction direction;
    private String destinationAddress;
    private String destinationPort;
    private String message;
    @Field(type = FieldType.Nested, name = "threshold")
    private Threshold threshold;
    private Set<Pattern> patterns;

    public Rule(Action action, Header header, Options options) {
        id = options.getId();
        this.action = action;
        protocol = header.getProtocol();
        sourceAddress = header.getSourceAddress();
        sourcePort = header.getSourcePort();
        direction = header.getDirection();
        destinationAddress = header.getDestinationAddress();
        destinationPort = header.getDestinationPort();
        message = options.getMessage();
        patterns = options.getPatterns();
    }

    public Rule(String id, Action action, Threshold threshold) {
        this.id = id;
        this.action = action;
        this.threshold = threshold;
    }

    public Rule(String id, Action action) {
        this.id = id;
        this.action = action;
    }

    public Rule() {
    }

    public String getId() {
        return id;
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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public String getMessage() {
        return message;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public Set<Pattern> getPatterns() {
        return patterns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id) && action == rule.action && Objects.equals(sourceAddress, rule.sourceAddress) && Objects.equals(sourcePort, rule.sourcePort) && Objects.equals(destinationAddress, rule.destinationAddress) && Objects.equals(destinationPort, rule.destinationPort) && Objects.equals(message, rule.message) && Objects.equals(threshold, rule.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, sourceAddress, sourcePort, destinationAddress, destinationPort, message, threshold);
    }
}
