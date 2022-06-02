package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Pattern;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;

public class PatternMatcher {

    private static final String CONTENT = "content";
    private final HttpRequest request;

    public PatternMatcher(HttpRequest request) {
        this.request = request;
    }

    public boolean doOptionsMatch(Rule rule) {
        for (Pattern pattern : rule.getPatterns()) {
            if (CONTENT.equals(pattern.name()) && isPatternNotMatched(pattern)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPatternNotMatched(Pattern pattern) {
        String requestPart = getRequestPart(request, getHttpKeywordModifier(pattern));
        return isPatternNotPresent(requestPart, pattern.value());
    }

    private HttpKeyword getHttpKeywordModifier(Pattern pattern) {
        return (HttpKeyword) pattern.modifiers()
                .stream()
                .filter(HttpKeyword.class::isInstance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private String getRequestPart(HttpRequest request, HttpKeyword modifier) {
        return switch (modifier) {
            case HTTP_URI -> request.getUri();
            case HTTP_HEADER -> request.getHeadersValues();
            case HTTP_METHOD -> request.getMethod();
            case HTTP_REQUEST_BODY -> request.getBody();
        };
    }

    private boolean isPatternNotPresent(String data, String pattern) {
        return !data.contains(pattern);
    }

}
