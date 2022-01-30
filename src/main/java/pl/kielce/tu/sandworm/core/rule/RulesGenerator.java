package pl.kielce.tu.sandworm.core.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.rule.parser.RuleParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class RulesGenerator {

    Logger logger = LoggerFactory.getLogger(RulesGenerator.class);

    private static final String COMMENTED_OUT_PREFIX = "#";
    private final RuleParser parser = new RuleParser();
    private final Path ruleSetPath;

    public RulesGenerator(Path ruleSetPath) {
        this.ruleSetPath = ruleSetPath;
    }

    public Set<Rule> generate() throws IOException {
        try (Stream<String> lines = Files.lines(ruleSetPath)) {
            return lines
                    .filter(this::isNotCommentedOut)
                    .map(this::parse)
                    .filter(this::isNotNull)
                    .collect(toSet());
        }
    }

    private boolean isNotCommentedOut(String line) {
        return !line.startsWith(COMMENTED_OUT_PREFIX);
    }

    private Rule parse(String line) {
        try {
            return parser.parse(line);
        } catch (RuleSyntaxException e) {
            logger.error("Cannot generate Rule due to parsing error: {}. Rule skipped: {}", e, line);
            return null;
        }
    }

    private boolean isNotNull(Rule rule) {
        return rule != null;
    }
}
