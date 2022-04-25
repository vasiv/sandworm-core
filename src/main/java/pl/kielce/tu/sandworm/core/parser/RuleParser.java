package pl.kielce.tu.sandworm.core.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;

import static org.apache.commons.lang3.StringUtils.SPACE;

public class RuleParser {

    private static final Logger logger = LoggerFactory.getLogger(RuleParser.class);
    private final ActionParser actionParser;
    private final HeaderParser headerParser;
    private final OptionsParser optionsParser;

    public RuleParser(ActionParser actionParser, HeaderParser headerParser, OptionsParser optionsParser) {
        this.actionParser = actionParser;
        this.headerParser = headerParser;
        this.optionsParser = optionsParser;
    }

    public Rule parse(String rule) throws RuleSyntaxException {
        logger.debug("Parsing of {} started...", rule);
        Rule parsedRule = parseToRule(rule);
        logger.debug("Parsing finished. Parsed Rule is: {}", parsedRule);
        return parsedRule;
    }

    private Rule parseToRule(String rule) throws RuleSyntaxException {
        String[] splitRule = rule.split(SPACE);
        Rule.Action action = actionParser.parse(splitRule);
        Rule.Header header = headerParser.parse(splitRule);
        Rule.Options options = optionsParser.parse(rule);
        return new Rule(action, header, options);
    }

}
