package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Pattern;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;

import static pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword.EMPTY;

public class PatternMatcher {

    private static final String CONTENT = "content";
    private final HttpRequest request;

    public PatternMatcher(HttpRequest request) {
        this.request = request;
    }

    public boolean doPatternsMatch(Rule rule) {
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

    private String getRequestPart(HttpRequest request, HttpKeyword modifier) {
        return switch (modifier) {
            case HTTP_URI -> request.getUri();
            case HTTP_HEADER -> request.getHeadersValues();
            case HTTP_METHOD -> request.getMethod();
            case HTTP_REQUEST_BODY -> request.getBody();
            case EMPTY -> getAllRequestParts(request);
        };
    }

    private String getAllRequestParts(HttpRequest request) {
        return request.getUri() + request.getHeadersValues() + request.getMethod() + request.getBody();
    }

    private HttpKeyword getHttpKeywordModifier(Pattern pattern) {
        return (HttpKeyword) pattern.modifiers()
                .stream()
                .filter(HttpKeyword.class::isInstance)
                .findFirst()
                .orElse(EMPTY);
    }

    private boolean isPatternNotPresent(String data, String pattern) {
        return !data.contains(pattern);
    }

}
