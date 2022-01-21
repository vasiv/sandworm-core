package pl.kielce.tu.sandworm.core.rule.parser;

import pl.kielce.tu.sandworm.core.model.Option;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.Threshold;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.*;
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
    private static final String BOTH_WAYS_SIGN = "<>";
    private static final ModifierParser modifierParser = new ModifierParser();
    private static final String QUOTE_REGEX = "^\"|\"$";
    private static final String THRESHOLD_PREFIX = "threshold:";

    public Rule parse(String line) throws ParseException {
        String[] splitLine = line.split(SPACE);

        Action action = getAction(splitLine);
        Protocol protocol = getProtocol(splitLine);
        String sourceAddress = getRuleChunk(splitLine, SOURCE_ADDRESS_INDEX);
        String sourcePort = getRuleChunk(splitLine, SOURCE_PORT_INDEX);
        Direction direction = getDirection(splitLine);
        String destinationAddress = getRuleChunk(splitLine, DESTINATION_ADDRESS_INDEX);
        String destinationPort = getRuleChunk(splitLine, DESTINATION_PORT_INDEX);

        List<String> optionsSeparated = getOptionsSeparated(line);
        return new Rule.RuleBuilder(action, protocol, sourceAddress, sourcePort,
                direction, destinationAddress, destinationPort)
                .withOptions(getOptions(optionsSeparated))
                .withThreshold(getThreshold(optionsSeparated))
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

    private Set<Option> getOptions(List<String> optionsSeparated) {
        Set<Option> options = new HashSet<>();
        for (String element : optionsSeparated) {
            String[] nameAndMaybeValue = element.split(COLON);
            if (isNotModifier(nameAndMaybeValue)) {
                Option e = getOption(optionsSeparated, optionsSeparated.indexOf(element), nameAndMaybeValue);
                options.add(e);
            }
        }
        return options;
    }

    private Option getOption(List<String> optionsSeparated, int currentIndex, String[] nameAndMaybeValue) {
        Set<Modifier> modifiers = getModifiers(optionsSeparated.subList(currentIndex + 1, optionsSeparated.size()));
        String optionName = nameAndMaybeValue[0].trim();
        String optionValue = nameAndMaybeValue[1].trim().replaceAll(QUOTE_REGEX, EMPTY);
        return new Option(optionName, optionValue, modifiers);
    }

    private List<String> getOptionsSeparated(String line) {
        Matcher m = Pattern.compile(RULE_OPTIONS_REGEX).matcher(line);
        m.find();
        String optionsGroup = m.group(1);
        String[] splitArray = optionsGroup.split(SEMICOLON);
        return Arrays.asList(splitArray);
    }

    private boolean isNotModifier(String[] nameAndMaybeValue) {
        return nameAndMaybeValue.length == 2;
    }

    private Set<Modifier> getModifiers(List<String> optionsGroup) {
        Set<Modifier> modifiers = new HashSet<>();
        for (String maybeModifier : optionsGroup) {
            try {
                Modifier modifier = modifierParser.getModifier(maybeModifier.trim());
                modifiers.add(modifier);
            } catch (IllegalArgumentException e) {
                break;
            }
        }
        return modifiers;
    }

    private Threshold getThreshold(List<String> optionsSeparated) {
        return optionsSeparated.stream()
                .filter(option -> option.trim().startsWith(THRESHOLD_PREFIX))
                .findFirst()
                .map(this::getThreshold)
                .orElse(new Threshold());
    }

    private Threshold getThreshold(String thresholdOption) {
        String thresholdSettings = thresholdOption.split(COLON)[1];
        final Map<String, String> settings = transferToMap(thresholdSettings);
        return new Threshold(settings);
    }

    private Map<String, String> transferToMap(String thresholdSettings) {
        final Map<String, String> map = new HashMap<>();
        Stream.of(thresholdSettings.split(COMMA))
                .map(String::trim)
                .map(setting -> setting.split(SPACE))
                .forEach(array -> map.put(array[0], array[1]));
        return map;
    }

}
