package pl.kielce.tu.sandworm.core.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.sandworm.core.model.Option;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.kielce.tu.sandworm.core.constants.SandwormCoreConstants.*;

public class OptionsParser {

    private static final Logger logger = LoggerFactory.getLogger(OptionsParser.class);
    private static final String QUOTE_REGEX = "^\"|\"$";
    private final ModifierParser modifierParser;

    public OptionsParser(ModifierParser modifierParser) {
        this.modifierParser = modifierParser;
    }

    public Rule.Options parse(String rule) {
        List<String> optionsSeparated = getOptionsSeparated(rule);
        Set<Option> options = getOptions(optionsSeparated);
        return new Rule.Options(options);
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

    private List<String> getOptionsSeparated(String line) {
        Matcher matcher = Pattern.compile(RULE_OPTIONS_REGEX).matcher(line);
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


    private Option getOption(List<String> optionsSeparated, int currentIndex, String[] nameAndMaybeValue) {
        Set<Modifier> modifiers = getModifiers(optionsSeparated.subList(currentIndex + 1, optionsSeparated.size()));
        String optionName = nameAndMaybeValue[0].trim();
        String optionValue = nameAndMaybeValue[1].trim().replaceAll(QUOTE_REGEX, EMPTY);
        return new Option(optionName, optionValue, modifiers);
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
