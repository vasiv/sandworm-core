package pl.kielce.tu.sandworm.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.repository.TriggeredRuleRepository;
import pl.kielce.tu.sandworm.core.rule.RulesLoader;
import pl.kielce.tu.sandworm.core.service.analysis.HttpAnalysisService;
import pl.kielce.tu.sandworm.core.service.analysis.StandardHttpAnalysisService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.kielce.tu.sandworm.core.model.enumeration.Protocol.HTTP;

@Configuration
public class SandwormCoreConfiguration {

    @Value("${rules.set}")
    private String ruleSetResourceName;

    @Bean
    public HttpAnalysisService analysisService(TriggeredRuleRepository triggeredRuleRepository)
            throws URISyntaxException, IOException {
        return new StandardHttpAnalysisService(getHttpRules(), triggeredRuleRepository);
    }

    private URI getResourceUri(String resourceName) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        return resource != null ? resource.toURI() : URI.create(EMPTY);
    }

    private Set<Rule> getHttpRules() throws URISyntaxException, IOException {
        Path ruleSetPath = Paths.get(getResourceUri(ruleSetResourceName));
        RulesLoader rulesLoader = new RulesLoader(ruleSetPath);
        return rulesLoader.generate()
                .stream()
                .filter(this::isHttpProtocolUsed)
                .collect(Collectors.toSet());
    }

    private boolean isHttpProtocolUsed(Rule rule) {
        return HTTP.equals(rule.getProtocol());
    }
}
