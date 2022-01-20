package pl.kielce.tu.sandworm.core.rule.parser;

import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

public class ModifierParser {

    private static final String HTTP = "http";

    public Modifier getModifier(String value) {
        if (value.startsWith(HTTP)) {
            return getHttpKeyword(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private HttpKeyword getHttpKeyword(String label) {
        for (HttpKeyword modifier : HttpKeyword.values())
            if (modifier.getLabel().equalsIgnoreCase(label)) {
                return modifier;
            }
        throw new IllegalArgumentException();
    }
}
