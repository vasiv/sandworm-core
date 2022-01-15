package pl.kielce.tu.sandworm.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kielce.tu.sandworm.core.controller.AnalysisController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SandwormCoreApplicationTests {

    @Autowired
    private AnalysisController analysisController;

    @Test
    void contextLoads() {
        assertNotNull(analysisController);
    }

}
