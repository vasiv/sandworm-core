package pl.kielce.tu.sandworm.core.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import pl.kielce.tu.sandworm.core.model.Threat;

@Repository
public interface ThreatRepository extends ElasticsearchRepository<Threat, String> {

    int countThreatsByRuleIdAndTriggeredAtBetween(String ruleId, long start, long end);

}
