package pl.kielce.tu.sandworm.core.rule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.rule.parser.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RulesLoaderTest {

    private RuleParser ruleParser;

    @BeforeEach
    void setUp() {
        ruleParser = new RuleParser(
                new ActionParser(),
                new HeaderParser(),
                new OptionsParser(new ModifierParser()));
    }

    @Test
    void shouldThrowExceptionWhenFileUnableToOpen() {
        Path path = Paths.get("notExistingDirectory/notExistingRuleSet.rules");
        RulesLoader generator = new RulesLoader(path, ruleParser);

        Assertions.assertThrows(IOException.class, generator::generate);
    }

    @Test
    void shouldReturnEmptyRuleSetWhenNoRulesDefinedInFile() throws URISyntaxException, IOException {
        Path path = Paths.get(getResourceUri("rules/emptySet.rules"));
        RulesLoader generator = new RulesLoader(path, ruleParser);

        Set<Rule> rules = generator.generate();

        assertEquals(0, rules.size());
    }

    @Test
    void shouldReturnDefinedRules() throws URISyntaxException, IOException {
        Path path = Paths.get(getResourceUri("rules/twoRulesSet.rules"));
        RulesLoader generator = new RulesLoader(path, ruleParser);

        Set<Rule> rules = generator.generate();

        assertEquals(2, rules.size());
    }

    @Test
    void shouldSkipCommentedOutLinesInRuleSetFile() throws URISyntaxException, IOException {
        Path path = Paths.get(getResourceUri("rules/twoRulesAndOneCommentedOutSet.rules"));
        RulesLoader generator = new RulesLoader(path, ruleParser);

        Set<Rule> rules = generator.generate();

        assertEquals(1, rules.size());
    }

    private URI getResourceUri(String resourceName) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        return resource != null ? resource.toURI() : URI.create(EMPTY);
    }

}