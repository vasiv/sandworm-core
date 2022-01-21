package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResult;
import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResultHandler;
import pl.kielce.tu.sandworm.core.model.RequestData;
import pl.kielce.tu.sandworm.core.analysis.matcher.RuleMatcher;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.repository.TriggeredRuleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardHttpAnalysisService implements HttpAnalysisService {

    Logger logger = LoggerFactory.getLogger(StandardHttpAnalysisService.class);

    @Value("${alert.directory}")
    private String alertDirectory;
    private final TriggeredRuleRepository triggeredRuleRepository;
    private final Set<Rule> rules;

    public StandardHttpAnalysisService(Set<Rule> rules, TriggeredRuleRepository triggeredRuleRepository) {
        this.rules = rules;
        this.triggeredRuleRepository = triggeredRuleRepository;
    }

    @Override
    public HttpAnalysisResult analyze(HttpServletRequest request, String body) {
        RequestData requestData = new RequestData(request, body);
        Set<Rule> rulesTriggered = getTriggeredRules(requestData);
        return new HttpAnalysisResult(requestData, rulesTriggered);
    }

    @Override
    public void handleResult(HttpAnalysisResult analysisResult) {
        HttpAnalysisResultHandler analysisResultHandler =
                new HttpAnalysisResultHandler(analysisResult, triggeredRuleRepository, alertDirectory);
        analysisResultHandler.start();
    }

    private Set<Rule> getTriggeredRules(RequestData requestData) {
        RuleMatcher ruleMatcher = new RuleMatcher(requestData);
        return getMatchedRules(ruleMatcher);
    }

    private Set<Rule> getMatchedRules(RuleMatcher ruleMatcher) {
        return rules.stream()
                .filter(ruleMatcher::doesHeaderMatch)
                .filter(ruleMatcher::doOptionsMatch)
                .collect(Collectors.toSet());
    }

}
