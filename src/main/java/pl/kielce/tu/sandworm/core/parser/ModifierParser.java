package pl.kielce.tu.sandworm.core.parser;

import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

public class ModifierParser {

    private static final String HTTP = "http";

    public Modifier getModifier(String value) {
        if (value.startsWith(HTTP)) {
            return HttpKeyword.get(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
