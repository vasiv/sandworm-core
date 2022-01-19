package pl.kielce.tu.sandworm.core.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.rule.RulesGenerator;
import pl.kielce.tu.sandworm.core.service.HttpAnalysisService;
import pl.kielce.tu.sandworm.core.service.StandardHttpAnalysisService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.COLON;
import static pl.kielce.tu.sandworm.core.model.enumeration.Protocol.HTTP;

@Configuration
@EnableElasticsearchRepositories(basePackages = "pl.kielce.tu.sandworm.core.repository")
public class SandwormCoreConfiguration {

    @Value("${rules.set}")
    private String ruleSetResourceName;
    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${elasticsearch.port}")
    private String elasticsearchPort;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(elasticsearchHost + COLON + elasticsearchPort)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

    @Bean
    public HttpAnalysisService analysisService() throws URISyntaxException, IOException {
        return new StandardHttpAnalysisService(getHttpRules());
    }

    private URI getResourceUri(String resourceName) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        return resource != null ? resource.toURI() : URI.create(EMPTY);
    }

    private Set<Rule> getHttpRules() throws URISyntaxException, IOException {
        Path ruleSetPath = Paths.get(getResourceUri(ruleSetResourceName));
        RulesGenerator rulesGenerator = new RulesGenerator(ruleSetPath);
        return rulesGenerator.generate()
                .stream()
                .filter(this::isHttpProtocolUsed)
                .collect(Collectors.toSet());
    }

    private boolean isHttpProtocolUsed(Rule rule) {
        return HTTP.equals(rule.getProtocol());
    }

}
