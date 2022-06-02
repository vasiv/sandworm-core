package pl.kielce.tu.sandworm.core.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.model.Options;
import pl.kielce.tu.sandworm.core.model.Pattern;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.util.*;
import java.util.regex.Matcher;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OptionsParser {

    private static final Logger logger = LoggerFactory.getLogger(OptionsParser.class);
    private static final String RULE_OPTIONS_REGEX = "\\((.*?)\\)";
    private static final String SEMICOLON = ";";
    private static final String COLON = ":";
    private static final String QUOTE_REGEX = "^\"|\"$";
    private final ModifierParser modifierParser;

    public OptionsParser(ModifierParser modifierParser) {
        this.modifierParser = modifierParser;
    }

    public Options parse(String rule) {
        List<String> optionsSeparated = getOptionsSeparated(rule);
        Set<Pattern> options = getOptions(optionsSeparated);
        return new Options(options);
    }

    private Set<Pattern> getOptions(List<String> optionsSeparated) {
        Set<Pattern> options = new HashSet<>();
        for (String element : optionsSeparated) {
            String[] nameAndMaybeValue = element.split(COLON);
            if (isNotModifier(nameAndMaybeValue)) {
                Pattern e = getOption(optionsSeparated, optionsSeparated.indexOf(element), nameAndMaybeValue);
                options.add(e);
            }
        }
        return options;
    }

    private List<String> getOptionsSeparated(String line) {
        Matcher matcher = java.util.regex.Pattern.compile(RULE_OPTIONS_REGEX).matcher(line);
        if (matcher.find()) {
            return separateOptions(matcher);
        } else {
            //todo throw exception
            return new ArrayList<>();
        }
    }

    private List<String> separateOptions(Matcher matcher) {
        String optionsGroup = matcher.group(1);
        return Arrays.stream(optionsGroup.split(SEMICOLON))
                .map(String::trim)
                .toList();
    }


    private Pattern getOption(List<String> optionsSeparated, int currentIndex, String[] nameAndMaybeValue) {
        Set<Modifier> modifiers = getModifiers(optionsSeparated.subList(currentIndex + 1, optionsSeparated.size()));
        String optionName = nameAndMaybeValue[0].trim();
        String optionValue = nameAndMaybeValue[1].trim().replaceAll(QUOTE_REGEX, EMPTY);
        return new Pattern(optionName, optionValue, modifiers);
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

}
