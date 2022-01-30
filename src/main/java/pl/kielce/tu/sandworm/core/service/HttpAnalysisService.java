package pl.kielce.tu.sandworm.core.service;

import pl.kielce.tu.sandworm.core.model.RequestData;

public interface HttpAnalysisService {

    void performNonDropAnalysis(RequestData requestData);

    boolean performDropAnalysis(RequestData requestData);

}
