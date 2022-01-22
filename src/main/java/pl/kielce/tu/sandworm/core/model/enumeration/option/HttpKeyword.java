package pl.kielce.tu.sandworm.core.model.enumeration.option;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public enum HttpKeyword implements Modifier {

    HTTP_URI("http.uri"),
    HTTP_METHOD("http.method"),
    HTTP_HEADER("http.header"),
    HTTP_REQUEST_BODY("http.request_body");

    private final String label;

    HttpKeyword(String label) {
        this.label = label;
    }

    public static HttpKeyword get(String label) {
        for (HttpKeyword modifier : HttpKeyword.values())
            if (modifier.label.equalsIgnoreCase(label)) {
                return modifier;
            }
        throw new IllegalArgumentException();
    }

    @WritingConverter
    public enum ModifierToStringConverter implements Converter<Modifier, String> {

        INSTANCE;

        @Override
        public String convert(Modifier source) {
            return ((HttpKeyword) source).label;
        }
    }

    @ReadingConverter
    public enum StringToModifierConverter implements Converter<String, Modifier> {

        INSTANCE;

        @Override
        public Modifier convert(String source) {
            return get(source);
        }
    }
}
