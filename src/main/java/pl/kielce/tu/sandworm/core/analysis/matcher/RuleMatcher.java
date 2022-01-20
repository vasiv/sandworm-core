package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.RequestData;
import pl.kielce.tu.sandworm.core.model.Rule;

public class RuleMatcher {

    private final RuleHeaderMatcher headerMatcher;
    private final RuleOptionsMatcher optionsMatcher;

    public RuleMatcher(RequestData requestData) {
        headerMatcher = new RuleHeaderMatcher(requestData);
        optionsMatcher = new RuleOptionsMatcher(requestData);
    }

    public boolean doesHeaderMatch(Rule rule) {
        return headerMatcher.doesHeaderMatch(rule);
    }

    public boolean doOptionsMatch(Rule rule) {
        return optionsMatcher.doOptionsMatch(rule);
    }
}
