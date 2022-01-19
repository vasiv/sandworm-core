package pl.kielce.tu.sandworm.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kielce.tu.sandworm.core.analysis.HttpAnalysisResult;
import pl.kielce.tu.sandworm.core.analysis.HttpAnalysisResultHandler;
import pl.kielce.tu.sandworm.core.repository.TriggeredRuleRepository;

@Service
public class StandardHttpAnalysisResultService implements HttpAnalysisResultService {

    @Value("${alert.directory}")
    private String alertDirectory;

    private final TriggeredRuleRepository triggeredRuleRepository;

    @Autowired
    public StandardHttpAnalysisResultService(TriggeredRuleRepository triggeredRuleRepository) {
        this.triggeredRuleRepository = triggeredRuleRepository;
    }

    @Override
    public void handleResult(HttpAnalysisResult analysisResult) {
        HttpAnalysisResultHandler analysisResultHandler =
                new HttpAnalysisResultHandler(analysisResult, triggeredRuleRepository, alertDirectory);
        analysisResultHandler.start();
    }
}
