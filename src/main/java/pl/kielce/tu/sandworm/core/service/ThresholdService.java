package pl.kielce.tu.sandworm.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kielce.tu.sandworm.core.model.Threat;
import pl.kielce.tu.sandworm.core.model.Threshold;

@Service
public class ThresholdService {

    private static final Logger logger = LoggerFactory.getLogger(ThresholdService.class);
    private final ThreatService threatService;

    @Autowired
    public ThresholdService(ThreatService threatService) {
        this.threatService = threatService;
    }

    public boolean isThresholdReached(Threat threat) {
        Threshold threshold = threat.getRuleThreshold();
        switch (threshold.getType()) {
            case NONE -> {
                logger.debug("Threshold Type is None. Threshold reached.");
                return true;
            }
            case THRESHOLD -> {
                logger.debug("Threshold Type is Threshold. Checking...");
                return isThresholdReached(threat, threshold);
            }
            case LIMIT -> {
                logger.debug("Threshold Type is Limit. Checking...");
                return isLimitNotExceeded(threat, threshold);
            }
            default -> {
                logger.error("Threshold Type not recognized. Throwing an Exception.");
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isThresholdReached(Threat threat, Threshold threshold) {
        int threatsOccurred = threatService.countThreatsOccurredInRangeOfTime(threat, threshold.getSeconds());
        return threatsOccurred >= threshold.getCount();
    }

    private boolean isLimitNotExceeded(Threat threat, Threshold threshold) {
        int threatsOccurred = threatService.countThreatsOccurredInRangeOfTime(threat, threshold.getSeconds());
        return threatsOccurred <= threshold.getCount();
    }

}
