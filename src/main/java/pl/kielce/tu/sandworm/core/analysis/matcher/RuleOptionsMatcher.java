package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.PayloadPattern;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.option.HttpKeyword;

public class RuleOptionsMatcher {

    private static final String CONTENT = "content";
    private final HttpRequest request;

    public RuleOptionsMatcher(HttpRequest request) {
        this.request = request;
    }

    public boolean doOptionsMatch(Rule rule) {
//        Set<Option> options = rule.getOptions();
//        for (Option option : options) {
//            if(CONTENT.equals(option.name())) {
//                if (doesContentNotMatch(option)) {
//                    return false;
//                }
//            }
//        }
        return true;
    }

    private boolean doesContentNotMatch(PayloadPattern payloadPattern) {
        String requestPart = getRequestPart(request, getHttpKeywordModifier(payloadPattern));
        return isPatternNotPresent(requestPart, payloadPattern.value());
    }

    private HttpKeyword getHttpKeywordModifier(PayloadPattern payloadPattern) {
        return (HttpKeyword) payloadPattern.modifiers()
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
