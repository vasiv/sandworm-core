package pl.kielce.tu.sandworm.core.rule.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;

public class ActionParser {

    private static final Logger logger = LoggerFactory.getLogger(ActionParser.class);
    private static final int ACTION_INDEX = 0;

    public Rule.Action parse(String[] splitRule) throws RuleSyntaxException {
        Rule.Action action;
        try {
            action = getAction(splitRule);
        } catch (IllegalArgumentException e) {
            throw new RuleSyntaxException("Action not recognized.", e);
        }
        logger.debug("For {} parsed Action is: {}", splitRule, action);
        return action;
    }

    private Rule.Action getAction(String[] splitRule) {
        String actionChunk = getActionChunk(splitRule);
        return Rule.Action.valueOf(actionChunk);
    }

    private String getActionChunk(String[] splitRule) {
        String action = splitRule[ACTION_INDEX];
        return action.toUpperCase();
    }

}
