package pl.kielce.tu.sandworm.core.service;

import pl.kielce.tu.sandworm.core.analysis.AnalysisResult;

import javax.servlet.http.HttpServletRequest;

public interface HttpAnalysisService {

    AnalysisResult analyze(HttpServletRequest request, String body);
}
