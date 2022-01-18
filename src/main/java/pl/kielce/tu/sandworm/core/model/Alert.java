package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.analysis.RequestData;

public class Alert {

    private final RequestData requestData;
    private final Rule rule;

    public Alert(RequestData requestData, Rule rule) {
        this.requestData = requestData;
        this.rule = rule;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public Rule getRule() {
        return rule;
    }

}
