package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Option;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;

import java.util.Set;

public class RuleOptionsMatcher {

    private static final String CONTENT = "content";
    private final HttpRequest request;

    public RuleOptionsMatcher(HttpRequest request) {
        this.request = request;
    }

    public boolean doOptionsMatch(Rule rule) {
        Set<Option> options = rule.getOptions();
        for (Option option : options) {
            if(CONTENT.equals(option.name())) {
                if (doesContentNotMatch(option)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean doesContentNotMatch(Option option) {
        String requestPart = getRequestPart(request, getHttpKeywordModifier(option));
        return isPatternNotPresent(requestPart, option.value());
    }

    private HttpKeyword getHttpKeywordModifier(Option option) {
        return (HttpKeyword) option.modifiers()
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
