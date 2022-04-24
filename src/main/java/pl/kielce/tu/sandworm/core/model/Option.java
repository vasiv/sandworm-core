package pl.kielce.tu.sandworm.core.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pl.kielce.tu.sandworm.core.model.enumeration.option.Modifier;

import java.util.Set;

public record Option(String name,
                     String value,
                     @Field(type = FieldType.Keyword) Set<Modifier> modifiers) {
}
