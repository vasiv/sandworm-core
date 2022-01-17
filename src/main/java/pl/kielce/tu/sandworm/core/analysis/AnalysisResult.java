package pl.kielce.tu.sandworm.core.analysis;

import pl.kielce.tu.sandworm.core.model.Rule;

import java.util.Set;

public class AnalysisResult {

    private RequestData request;
    private Set<Rule> rulesTriggered;

    public AnalysisResult(RequestData request, Set<Rule> rulesTriggered) {
        this.request = request;
        this.rulesTriggered = rulesTriggered;
    }
}
