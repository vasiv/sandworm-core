package pl.kielce.tu.sandworm.core.rule;

import pl.kielce.tu.sandworm.core.RuleParser;
import pl.kielce.tu.sandworm.core.model.Rule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RulesGenerator {

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
                    .map(parser::parse).collect(Collectors.toSet());
        }
    }

    private boolean isNotCommentedOut(String line) {
        return !line.startsWith(COMMENTED_OUT_PREFIX);
    }
}
