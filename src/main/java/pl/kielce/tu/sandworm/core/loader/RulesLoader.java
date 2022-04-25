package pl.kielce.tu.sandworm.core.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.parser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class RulesLoader {

    private static final Logger logger = LoggerFactory.getLogger(RulesLoader.class);
    private static final String COMMENTED_OUT_PREFIX = "#";
    private final RuleParser parser;
    private final Path ruleSetPath;

    public RulesLoader(Path ruleSetPath, RuleParser parser) {
        this.ruleSetPath = ruleSetPath;
        this.parser = parser;
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
