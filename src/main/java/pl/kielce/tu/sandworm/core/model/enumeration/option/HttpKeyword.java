package pl.kielce.tu.sandworm.core.model.enumeration.option;

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
}
