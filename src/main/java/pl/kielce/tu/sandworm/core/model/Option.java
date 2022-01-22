package pl.kielce.tu.sandworm.core.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.util.Set;

public class Option {

    private final String name;
    private final String value;
    @Field(type = FieldType.Keyword)
    private final Set<Modifier> modifiers;

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
