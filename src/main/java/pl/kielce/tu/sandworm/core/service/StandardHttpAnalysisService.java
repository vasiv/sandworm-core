package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import pl.kielce.tu.sandworm.core.analysis.matcher.RuleMatcher;
import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResult;
import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResultHandler;
import pl.kielce.tu.sandworm.core.model.RequestData;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.repository.TriggeredRuleRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static pl.kielce.tu.sandworm.core.model.enumeration.Action.DROP;

public class StandardHttpAnalysisService implements HttpAnalysisService {

    Logger logger = LoggerFactory.getLogger(StandardHttpAnalysisService.class);

    @Value("${alert.directory}")
    private String alertDirectory;
    private final TriggeredRuleRepository triggeredRuleRepository;
    private final Set<Rule> dropRules;
    private final Set<Rule> nonDropRules;

    public StandardHttpAnalysisService(Set<Rule> rules, TriggeredRuleRepository triggeredRuleRepository) {
        this.dropRules = getDropRules(rules);
        this.nonDropRules = getNonDropRules(rules);
        this.triggeredRuleRepository = triggeredRuleRepository;
    }

    @Override
    public void performNonDropAnalysis(RequestData requestData) {
        Runnable runnable = () -> {
            HttpAnalysisResult analysisResult = analyzeRules(requestData, nonDropRules);
            handleResult(analysisResult);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean performDropAnalysis(RequestData requestData) {
        HttpAnalysisResult analysisResult = analyzeRules(requestData, dropRules);
        handleResult(analysisResult);
        return analysisResult.isDropNeeded();
    }

    private HttpAnalysisResult analyzeRules(RequestData requestData, Set<Rule> rules) {
        Set<Rule> rulesTriggered = getTriggeredRules(requestData, rules);
        return new HttpAnalysisResult(requestData, rulesTriggered);
    }

    private void handleResult(HttpAnalysisResult analysisResult) {
        HttpAnalysisResultHandler handler =
                new HttpAnalysisResultHandler(analysisResult, triggeredRuleRepository, alertDirectory);
        handler.start();
    }

    private Set<Rule> getTriggeredRules(RequestData requestData, Set<Rule> rules) {
        RuleMatcher ruleMatcher = new RuleMatcher(requestData);
        return getMatchedRules(ruleMatcher, rules);
    }

    private Set<Rule> getMatchedRules(RuleMatcher ruleMatcher, Set<Rule> rules) {
        return rules.stream()
                .filter(ruleMatcher::doesHeaderMatch)
                .filter(ruleMatcher::doOptionsMatch)
                .collect(Collectors.toSet());
    }

    private Set<Rule> getDropRules(Set<Rule> rules) {
        return rules.stream()
                .filter(this::isDropRule)
                .collect(Collectors.toSet());
    }

    private Set<Rule> getNonDropRules(Set<Rule> rules) {
        return rules.stream()
                .filter(this::isNonDropRule)
                .collect(Collectors.toSet());
    }

    private boolean isNonDropRule(Rule rule) {
        return !isDropRule(rule);
    }

    private boolean isDropRule(Rule rule) {
        return DROP.equals(rule.getAction());
    }

}
