package pl.kielce.tu.sandworm.core.analysis.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.model.RequestData;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.TriggeredRule;
import pl.kielce.tu.sandworm.core.repository.TriggeredRuleRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.JSON_EXTENSION;
import static pl.kielce.tu.sandworm.core.model.enumeration.Action.ALERT;

public class HttpAnalysisResultHandler extends Thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpAnalysisResultHandler.class);
    private final TriggeredRuleRepository triggeredRuleRepository;
    private final Set<Rule> triggeredRules;
    private final RequestData requestData;
    private final ObjectWriter jsonWriter;
    private final String alertDirectory;

    public HttpAnalysisResultHandler(HttpAnalysisResult analysisResult,
                                     TriggeredRuleRepository triggeredRuleRepository, String alertDirectory) {
        this.triggeredRuleRepository = triggeredRuleRepository;
        this.alertDirectory = alertDirectory;
        triggeredRules = analysisResult.getRulesTriggered();
        requestData = analysisResult.getRequest();
        jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @Override
    public void run() {
        try {
            handleTriggeredRules();
        } catch (IOException e) {
            logger.error("Cannot handle Analysis Result due to: ", e);
        }
    }

    private void handleTriggeredRules() throws IOException {
        for (Rule rule : triggeredRules) {
            TriggeredRule triggeredRule = new TriggeredRule(requestData, rule);
            saveLog(triggeredRule);
            if (ALERT.equals(rule.getAction())) {
                saveAlertAsJson(triggeredRule);
            }
        }
    }

    private void saveLog(TriggeredRule triggeredRule) {
        triggeredRuleRepository.save(triggeredRule);
    }

    private void saveAlertAsJson(TriggeredRule alert) throws IOException {
        String alertFilepath = generateAlertFilepath();
        File file = new File(alertFilepath);
        jsonWriter.writeValue(file, alert);
    }

    private String generateAlertFilepath() {
        return alertDirectory + LocalDateTime.now() + JSON_EXTENSION;
    }
}
