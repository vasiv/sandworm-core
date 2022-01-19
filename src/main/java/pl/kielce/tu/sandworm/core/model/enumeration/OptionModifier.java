package pl.kielce.tu.sandworm.core.model.enumeration;

public enum OptionModifier {

    HTTP_URI("http.uri"),
    HTTP_METHOD("http.method"),
    HTTP_HEADER("http.header");

    private final String label;

    OptionModifier(String label) {
        this.label = label;
    }

    public static OptionModifier getModifier(String value) {
        for (OptionModifier modifier : values())
            if (modifier.label.equalsIgnoreCase(value)) {
                return modifier;
            }
        throw new IllegalArgumentException();
    }

}
