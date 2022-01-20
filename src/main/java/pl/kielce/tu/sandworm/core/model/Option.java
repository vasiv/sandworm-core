package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.util.Set;

public class Option {

    private String name;
    private String value;
    private Set<Modifier> modifiers;

    public Option(String name, String value, Set<Modifier> modifiers) {
        this.name = name;
        this.value = value;
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }
}
