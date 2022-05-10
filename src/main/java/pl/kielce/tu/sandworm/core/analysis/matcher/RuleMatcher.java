package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;

public class RuleMatcher {

    private final RuleHeaderMatcher headerMatcher;
    private final RuleOptionsMatcher optionsMatcher;

    public RuleMatcher(HttpRequest httpRequest) {
        headerMatcher = new RuleHeaderMatcher(httpRequest);
        optionsMatcher = new RuleOptionsMatcher(httpRequest);
    }

    public boolean doesHeaderMatch(Rule rule) {
        return headerMatcher.doesHeaderMatch(rule);
    }

    public boolean doOptionsMatch(Rule rule) {
        return optionsMatcher.doOptionsMatch(rule);
    }
}
