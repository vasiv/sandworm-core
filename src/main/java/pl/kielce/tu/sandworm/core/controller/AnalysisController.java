package pl.kielce.tu.sandworm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.service.AnalysisService;
import pl.kielce.tu.sandworm.core.service.ProxyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
public class AnalysisController {

    private static final String REQUEST_DROPPED_RESPONSE_MESSAGE = "Request has been dropped due to security violation.";
    private final ProxyService proxyService;
    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(ProxyService proxyService, AnalysisService analysisService) {
        this.proxyService = proxyService;
        this.analysisService = analysisService;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> analyzeRequest(@RequestBody(required = false) String body,
                                                 HttpMethod method, HttpServletRequest request)
            throws URISyntaxException {
        HttpRequest requestData = new HttpRequest(request, body);
        analysisService.performNonDropAnalysis(requestData);
        boolean isDropNeeded = analysisService.performDropAnalysis(requestData);
        if (isDropNeeded) {
            return getResponseEntityForDroppedRequest();
        }
        return proxyService.proxyRequest(body, method, request);
    }

    private ResponseEntity<String> getResponseEntityForDroppedRequest() {
        return new ResponseEntity<>(REQUEST_DROPPED_RESPONSE_MESSAGE, FORBIDDEN);
    }
}
