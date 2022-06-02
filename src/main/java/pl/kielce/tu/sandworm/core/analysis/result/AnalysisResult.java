package pl.kielce.tu.sandworm.core.analysis.result;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;

import java.util.Set;
import java.util.function.Predicate;

import static pl.kielce.tu.sandworm.core.model.enumeration.Action.DROP;

public class AnalysisResult {

    private final HttpRequest request;
    private final Set<Rule> triggeredRules;
    private final boolean isDropNeeded;

    public AnalysisResult(HttpRequest request, Set<Rule> triggeredRules) {
        this.request = request;
        this.triggeredRules = triggeredRules;
        isDropNeeded = isDropNeeded(triggeredRules);
    }

    private boolean isDropNeeded(Set<Rule> rulesTriggered) {
        return rulesTriggered.stream().anyMatch(isDropActionSet());
    }

    private Predicate<Rule> isDropActionSet() {
        return rule -> DROP.equals(rule.getAction());
    }

    public HttpRequest getRequest() {
        return request;
    }

    public Set<Rule> getTriggeredRules() {
        return triggeredRules;
    }

    public boolean isDropNeeded() {
        return isDropNeeded;
    }
}
