package pl.kielce.tu.sandworm.core.analysis.result;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kielce.tu.sandworm.core.SandwormCoreApplication;
import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.repository.ThreatRepository;
import pl.kielce.tu.sandworm.core.service.ThresholdService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SandwormCoreApplication.class)
class AnalysisResultHandlerTest {

    @Autowired
    private ThreatRepository threatRepository;

    @Autowired
    private ThresholdService thresholdService;

    @Test
    void shouldSaveThreatInDatabaseWhenRuleTriggered() {
        //given
        Rule triggeredRule = new Rule("0", Action.PASS);
        Set<Rule> triggeredRules = new HashSet<>(List.of(triggeredRule));
        AnalysisResult analysisResult = new AnalysisResult(new HttpRequest(), triggeredRules);
        AnalysisResultHandler analysisResultHandler =
                new AnalysisResultHandler(analysisResult, threatRepository, StringUtils.EMPTY, thresholdService);

        //when
        analysisResultHandler.run();

        //then
        assertEquals(1, threatRepository.count());
    }
}