package pl.kielce.tu.sandworm.core.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import pl.kielce.tu.sandworm.core.model.TriggeredRule;

@Repository
public interface TriggeredRuleRepository extends ElasticsearchRepository<TriggeredRule, String> {

}
