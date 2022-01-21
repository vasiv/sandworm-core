package pl.kielce.tu.sandworm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResult;
import pl.kielce.tu.sandworm.core.service.HttpAnalysisService;
import pl.kielce.tu.sandworm.core.service.ProxyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.REQUEST_DROPPED_RESPONSE_MESSAGE;

@RestController
public class AnalysisController {

    private final ProxyService proxyService;
    private final HttpAnalysisService analysisService;

    @Autowired
    public AnalysisController(ProxyService proxyService, HttpAnalysisService httpAnalysisService) {
        this.proxyService = proxyService;
        this.analysisService = httpAnalysisService;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> analyzeRequest(@RequestBody(required = false) String body, HttpMethod method,
                                                 HttpServletRequest request, HttpServletResponse response) throws
            URISyntaxException {
        HttpAnalysisResult analysisResult = analysisService.analyze(request, body);
        analysisService.handleResult(analysisResult);
        if (analysisResult.isDropNeeded()) {
            return getResponseEntityForDroppedRequest();
        }
        return proxyService.proxyRequest(body, method, request, response);
    }

    private ResponseEntity<String> getResponseEntityForDroppedRequest() {
        return new ResponseEntity<>(REQUEST_DROPPED_RESPONSE_MESSAGE, FORBIDDEN);
    }
}
