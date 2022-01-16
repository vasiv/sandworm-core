package pl.kielce.tu.sandworm.core.rule;

import pl.kielce.tu.sandworm.core.model.Options;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import java.text.ParseException;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static pl.kielce.tu.sandworm.core.model.enumeration.Direction.BOTH_WAYS;
import static pl.kielce.tu.sandworm.core.model.enumeration.Direction.ONE_WAY;

public class RuleParser {

    private static final int ACTION_INDEX = 0;
    private static final int PROTOCOL_INDEX = 1;
    private static final int SOURCE_ADDRESS_INDEX = 2;
    private static final int SOURCE_PORT_INDEX = 3;
    private static final int DIRECTION_INDEX = 4;
    private static final int DESTINATION_ADDRESS_INDEX = 5;
    private static final int DESTINATION_PORT_INDEX = 6;
    private static final String ONE_WAY_SIGN = "->";
    private static final String BOTH_WAYS_SIGN = "<->";

    public Rule parse(String line) throws ParseException {
        String[] splitLine = line.split(SPACE);

        Action action = getAction(splitLine);
        Protocol protocol = getProtocol(splitLine);
        String sourceAddress = getRuleChunk(splitLine, SOURCE_ADDRESS_INDEX);
        String sourcePort = getRuleChunk(splitLine, SOURCE_PORT_INDEX);
        Direction direction = getDirection(splitLine);
        String destinationAddress = getRuleChunk(splitLine, DESTINATION_ADDRESS_INDEX);
        String destinationPort = getRuleChunk(splitLine, DESTINATION_PORT_INDEX);

        return new Rule.RuleBuilder(action, protocol, sourceAddress, sourcePort,
                direction, destinationAddress, destinationPort)
                .withOptions(getOptions(line))
                .build();
    }

    private Action getAction(String[] splitLine) {
        String action = splitLine[ACTION_INDEX];
        return Action.valueOf(action.toUpperCase());
    }

    private Protocol getProtocol(String[] splitLine) {
        String protocol = splitLine[PROTOCOL_INDEX];
        return Protocol.valueOf(protocol.toUpperCase());
    }

    private Direction getDirection(String[] splitLine) throws ParseException {
        String direction = splitLine[DIRECTION_INDEX];
        switch (direction) {
            case ONE_WAY_SIGN:
                return ONE_WAY;
            case BOTH_WAYS_SIGN:
                return BOTH_WAYS;
            default:
                throw new ParseException("Cannot parse direction sign - non of expected ones.", 0);
        }
    }

    private String getRuleChunk(String[] splitLine, int index) {
        return splitLine[index].toUpperCase();
    }

    private Options getOptions(String line) {
        return new Options();
    }
}
