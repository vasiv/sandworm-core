package pl.kielce.tu.sandworm.core.analysis.result;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;

import java.util.Set;
import java.util.function.Predicate;

import static pl.kielce.tu.sandworm.core.model.enumeration.Action.DROP;

public class HttpAnalysisResult {

    private HttpRequest request;
    private Set<Rule> rulesTriggered;
    private boolean isDropNeeded;

    public HttpAnalysisResult(HttpRequest request, Set<Rule> rulesTriggered) {
        this.request = request;
        this.rulesTriggered = rulesTriggered;
        isDropNeeded = isDropNeeded(rulesTriggered);
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

    public Set<Rule> getRulesTriggered() {
        return rulesTriggered;
    }

    public boolean isDropNeeded() {
        return isDropNeeded;
    }
}
