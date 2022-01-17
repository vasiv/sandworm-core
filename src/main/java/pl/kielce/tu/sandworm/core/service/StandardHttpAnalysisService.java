package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.analysis.AnalysisResult;
import pl.kielce.tu.sandworm.core.model.Rule;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class StandardHttpAnalysisService implements HttpAnalysisService {

    Logger logger = LoggerFactory.getLogger(StandardHttpAnalysisService.class);

    private Set<Rule> rules;

    public StandardHttpAnalysisService(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public AnalysisResult analyze(HttpServletRequest request, String body) {
        return null;
    }
}
