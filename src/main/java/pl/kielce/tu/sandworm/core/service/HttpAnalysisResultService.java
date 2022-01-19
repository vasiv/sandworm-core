package pl.kielce.tu.sandworm.core.service;

import pl.kielce.tu.sandworm.core.analysis.HttpAnalysisResult;

public interface HttpAnalysisResultService {

    void handleResult(HttpAnalysisResult analysisResult);
}
