package pl.kielce.tu.sandworm.core.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import static pl.kielce.tu.sandworm.core.model.enumeration.Direction.BOTH_WAYS;
import static pl.kielce.tu.sandworm.core.model.enumeration.Direction.ONE_WAY;

public class HeaderParser {

    private static final Logger logger = LoggerFactory.getLogger(HeaderParser.class);
    private static final int PROTOCOL_INDEX = 1;
    private static final int SOURCE_ADDRESS_INDEX = 2;
    private static final int SOURCE_PORT_INDEX = 3;
    private static final int DIRECTION_INDEX = 4;
    private static final int DESTINATION_ADDRESS_INDEX = 5;
    private static final int DESTINATION_PORT_INDEX = 6;
    private static final String ONE_WAY_SIGN = "->";
    private static final String BOTH_WAYS_SIGN = "<>";

    public Rule.Header parse(String[] splitRule) throws RuleSyntaxException {
        Rule.Header header;
        header = getHeader(splitRule);
        logger.debug("For {} parsed Header is: {}", splitRule, header);
        return header;
    }

    private Rule.Header getHeader(String[] splitRule) throws RuleSyntaxException {
        return new Rule.Header.Builder()
                .withProtocol(getProtocol(splitRule))
                .withSourceAddress(getRuleChunk(splitRule, SOURCE_ADDRESS_INDEX))
                .withSourcePort(getRuleChunk(splitRule, SOURCE_PORT_INDEX))
                .withDirection(getDirection(splitRule))
                .withDestinationAddress(getRuleChunk(splitRule, DESTINATION_ADDRESS_INDEX))
                .withDestinationPort(getRuleChunk(splitRule, DESTINATION_PORT_INDEX))
                .build();
    }

    private Protocol getProtocol(String[] splitLine) {
        String protocol = splitLine[PROTOCOL_INDEX];
        return Protocol.valueOf(protocol.toUpperCase());
    }

    private Direction getDirection(String[] splitLine) throws RuleSyntaxException {
        String direction = splitLine[DIRECTION_INDEX];
        return switch (direction) {
            case ONE_WAY_SIGN -> ONE_WAY;
            case BOTH_WAYS_SIGN -> BOTH_WAYS;
            default -> throw new RuleSyntaxException("Direction sign not recognized.");
        };
    }

    private String getRuleChunk(String[] splitLine, int index) {
        return splitLine[index].toUpperCase();
    }
}
