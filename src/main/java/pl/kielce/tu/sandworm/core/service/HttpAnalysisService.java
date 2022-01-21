package pl.kielce.tu.sandworm.core.service;

import pl.kielce.tu.sandworm.core.analysis.result.HttpAnalysisResult;

import javax.servlet.http.HttpServletRequest;

public interface HttpAnalysisService {

    HttpAnalysisResult analyze(HttpServletRequest request, String body);

    void handleResult(HttpAnalysisResult analysisResult);
}
