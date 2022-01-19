package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.OptionModifier;

import java.util.Set;

public class Option {

    public Option(String name, String value, Set<OptionModifier> modifiers) {
        this.name = name;
        this.value = value;
        this.modifiers = modifiers;
    }

    private String name;
    private String value;
    private Set<OptionModifier> modifiers;

}
