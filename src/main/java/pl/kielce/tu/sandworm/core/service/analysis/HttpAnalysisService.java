package pl.kielce.tu.sandworm.core.service.analysis;

import pl.kielce.tu.sandworm.core.model.HttpRequest;

public interface HttpAnalysisService {

    void performNonDropAnalysis(HttpRequest requestData);

    boolean performDropAnalysis(HttpRequest requestData);

}
