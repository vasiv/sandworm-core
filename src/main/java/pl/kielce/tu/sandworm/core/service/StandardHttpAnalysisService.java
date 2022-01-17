package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.analysis.HttpAnalysisResult;
import pl.kielce.tu.sandworm.core.analysis.RequestData;
import pl.kielce.tu.sandworm.core.analysis.RuleHeaderMatcher;
import pl.kielce.tu.sandworm.core.model.Rule;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardHttpAnalysisService implements HttpAnalysisService {

    Logger logger = LoggerFactory.getLogger(StandardHttpAnalysisService.class);

    private final Set<Rule> rules;

    public StandardHttpAnalysisService(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public HttpAnalysisResult analyze(HttpServletRequest request, String body) {
        RequestData requestData = new RequestData(request, body);
        Set<Rule> rulesTriggered = getTriggeredRules(requestData);
        return new HttpAnalysisResult(requestData, rulesTriggered);
    }

    private Set<Rule> getTriggeredRules(RequestData requestData) {
        RuleHeaderMatcher headerMatcher = new RuleHeaderMatcher(requestData);
        return getMatchedRules(headerMatcher);
    }

    private Set<Rule> getMatchedRules(RuleHeaderMatcher headerMatcher) {
        return rules.stream()
                .filter(headerMatcher::doesHeaderMatch)
                .collect(Collectors.toSet());
    }

}
