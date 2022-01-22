package pl.kielce.tu.sandworm.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(indexName = "triggered_rule")
public class TriggeredRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private final RequestData requestData;
    private final Rule rule;
    private long triggeredAt;

    public TriggeredRule(RequestData requestData, Rule rule) {
        this.requestData = requestData;
        this.rule = rule;
        triggeredAt = requestData.getCreatedAt();
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public Rule getRule() {
        return rule;
    }

    public Threshold getThreshold() {
        return rule.getThreshold();
    }

    public long getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(long triggeredAt) {
        this.triggeredAt = triggeredAt;
    }
}
