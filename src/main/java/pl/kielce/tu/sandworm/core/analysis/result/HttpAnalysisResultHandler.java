package pl.kielce.tu.sandworm.core.analysis.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.model.RequestData;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.Threshold;
import pl.kielce.tu.sandworm.core.model.Threat;
import pl.kielce.tu.sandworm.core.repository.ThreatRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.JSON_EXTENSION;
import static pl.kielce.tu.sandworm.core.model.enumeration.ThresholdType.NONE;

public class HttpAnalysisResultHandler extends Thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpAnalysisResultHandler.class);
    private final ThreatRepository triggeredRuleRepository;
    private final Set<Rule> triggeredRules;
    private final RequestData requestData;
    private final ObjectWriter jsonWriter;
    private final String alertDirectory;

    public HttpAnalysisResultHandler(HttpAnalysisResult analysisResult,
                                     ThreatRepository threatRepository, String alertDirectory) {
        this.triggeredRuleRepository = threatRepository;
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
            Threat threat = new Threat(requestData, rule);
            saveLog(threat);
            if (isActionAlert(rule)) {
                handleAlertAction(threat);
            }
        }
    }

    private void saveLog(Threat threat) {
        triggeredRuleRepository.save(threat);
    }

    private boolean isActionAlert(Rule rule) {
        return Rule.Action.ALERT.equals(rule.getAction());
    }

    private void handleAlertAction(Threat threat) throws IOException {
        if (isThresholdReached(threat)) {
            saveAlertAsJson(threat);
        }
    }

    private boolean isThresholdReached(Threat threat) {
        Threshold threshold = threat.getRuleThreshold();
        if (threshold == null || isTypeNone(threshold)) {
            logger.debug("Threshold is not defined or type is None. Threshold reached.");
            return true;
        }
        return true;
    }

    private boolean isTypeNone(Threshold threshold) {
        return NONE.equals(threshold.getType());
    }

    private void saveAlertAsJson(Threat alert) throws IOException {
        String alertFilepath = generateAlertFilepath();
        File file = new File(alertFilepath);
        jsonWriter.writeValue(file, alert);
    }

    private String generateAlertFilepath() {
        return alertDirectory + LocalDateTime.now() + JSON_EXTENSION;
    }
}
