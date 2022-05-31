package pl.kielce.tu.sandworm.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kielce.tu.sandworm.core.model.Threat;
import pl.kielce.tu.sandworm.core.repository.ThreatRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ThreatService {

    private final ThreatRepository threatRepository;

    @Autowired
    public ThreatService(ThreatRepository threatRepository) {
        this.threatRepository = threatRepository;
    }

    public int countThreatsOccurredInRangeOfTime(Threat threat, int seconds) {
        String ruleId = threat.getRule().getId();
        LocalDateTime triggeredAt = new Timestamp(threat.getTriggeredAt()).toLocalDateTime();
        long startRange = toEpochMilliseconds(triggeredAt.minus(Duration.ofSeconds(seconds)));
        long endRange = toEpochMilliseconds(triggeredAt);
        return threatRepository.countThreatsByRuleIdAndTriggeredAtBetween(ruleId, startRange, endRange);
    }

    private long toEpochMilliseconds(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }

}
