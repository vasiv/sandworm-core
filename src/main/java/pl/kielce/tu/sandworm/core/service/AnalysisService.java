package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import pl.kielce.tu.sandworm.core.analysis.matcher.RuleMatcher;
import pl.kielce.tu.sandworm.core.analysis.result.AnalysisResult;
import pl.kielce.tu.sandworm.core.analysis.result.AnalysisResultHandler;
import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.repository.ThreatRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class AnalysisService {

    Logger logger = LoggerFactory.getLogger(AnalysisService.class);
    private final ThreatRepository threatRepository;
    private final ThresholdService thresholdService;
    private final Set<Rule> dropRules;
    private final Set<Rule> nonDropRules;
    @Value("${alert.directory}")
    private String alertDirectory;

    public AnalysisService(Set<Rule> rules, ThreatRepository threatRepository, ThresholdService thresholdService) {
        this.dropRules = getDropRules(rules);
        this.nonDropRules = getNonDropRules(rules);
        this.threatRepository = threatRepository;
        this.thresholdService = thresholdService;
    }

    public void performNonDropAnalysis(HttpRequest requestData) {
        Runnable runnable = () -> {
            AnalysisResult analysisResult = analyzeRules(requestData, nonDropRules);
            handleResult(analysisResult);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public boolean performDropAnalysis(HttpRequest requestData) {
        AnalysisResult analysisResult = analyzeRules(requestData, dropRules);
        handleResult(analysisResult);
        return analysisResult.isDropNeeded();
    }

    private AnalysisResult analyzeRules(HttpRequest requestData, Set<Rule> rules) {
        Set<Rule> rulesTriggered = getTriggeredRules(requestData, rules);
        return new AnalysisResult(requestData, rulesTriggered);
    }

    private void handleResult(AnalysisResult analysisResult) {
        AnalysisResultHandler handler =
                new AnalysisResultHandler(analysisResult, threatRepository, alertDirectory, thresholdService);
        handler.start();
    }

    private Set<Rule> getTriggeredRules(HttpRequest requestData, Set<Rule> rules) {
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
        return Action.DROP.equals(rule.getAction());
    }

}
