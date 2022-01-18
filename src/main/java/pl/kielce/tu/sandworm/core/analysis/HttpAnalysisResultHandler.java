package pl.kielce.tu.sandworm.core.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.model.Alert;
import pl.kielce.tu.sandworm.core.model.Rule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.FILEPATH;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.JSON_EXTENSION;
import static pl.kielce.tu.sandworm.core.model.enumeration.Action.ALERT;

public class HttpAnalysisResultHandler extends Thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpAnalysisResultHandler.class);
    private final Set<Rule> triggeredRules;
    private final RequestData requestData;
    private final ObjectWriter jsonWriter;

    public HttpAnalysisResultHandler(HttpAnalysisResult analysisResult) {
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
            if (ALERT.equals(rule.getAction())) {
                storeAlertAsJson(requestData, rule);
            }
        }
    }

    private void storeAlertAsJson(RequestData requestData, Rule rule) throws IOException {
        Alert alert = new Alert(requestData, rule);
        File file = new File(FILEPATH + LocalDateTime.now() + JSON_EXTENSION);
        jsonWriter.writeValue(file, alert);
    }
}
