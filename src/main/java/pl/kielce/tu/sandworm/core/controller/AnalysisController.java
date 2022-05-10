package pl.kielce.tu.sandworm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.service.analysis.HttpAnalysisService;
import pl.kielce.tu.sandworm.core.service.proxy.ProxyService;

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
                                                 HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {
        HttpRequest requestData = new HttpRequest(request, body);
        analysisService.performNonDropAnalysis(requestData);
        boolean isDropNeeded = analysisService.performDropAnalysis(requestData);
        if (isDropNeeded) {
            return getResponseEntityForDroppedRequest();
        }
        return proxyService.proxyRequest(body, method, request, response);
    }

    private ResponseEntity<String> getResponseEntityForDroppedRequest() {
        return new ResponseEntity<>(REQUEST_DROPPED_RESPONSE_MESSAGE, FORBIDDEN);
    }
}
