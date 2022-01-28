package pl.kielce.tu.sandworm.core.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;

import java.util.Arrays;

import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.COLON;

@Configuration
@EnableElasticsearchRepositories(basePackages = "pl.kielce.tu.sandworm.core.repository")
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${elasticsearch.port}")
    private String elasticsearchPort;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(elasticsearchHost + COLON + elasticsearchPort)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(Arrays.asList(
                HttpKeyword.ModifierToStringConverter.INSTANCE,
                HttpKeyword.StringToModifierConverter.INSTANCE)
        );
    }
}
