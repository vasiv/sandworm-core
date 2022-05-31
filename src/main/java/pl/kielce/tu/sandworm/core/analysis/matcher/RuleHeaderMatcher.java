package pl.kielce.tu.sandworm.core.analysis.matcher;

import pl.kielce.tu.sandworm.core.model.HttpRequest;
import pl.kielce.tu.sandworm.core.model.Rule;

public class RuleHeaderMatcher {

    private static final String ANY = "any";
    private final HttpRequest request;

    public RuleHeaderMatcher(HttpRequest request) {
        this.request = request;
    }

    public boolean doesHeaderMatch(Rule rule) {
        return doesSourceAddressMatch(rule.getSourceAddress(), request.getSourceAddress())
                && doesSourcePortMatch(rule.getSourcePort(), request.getSourcePort())
                && doesDestinationAddressMatch(rule.getDestinationAddress(), request.getDestinationAddress())
                && doesDestinationPortMatch(rule.getDestinationPort(), request.getDestinationPort());
    }

    private boolean doesSourceAddressMatch(String ruleSourceAddress, String requestSourceAddress) {
        return isAnySetInRule(ruleSourceAddress) || ruleSourceAddress.equals(requestSourceAddress);
    }

    private boolean doesSourcePortMatch(String ruleSourcePort, String requestSourcePort) {
        return isAnySetInRule(ruleSourcePort) || ruleSourcePort.equals(requestSourcePort);
    }

    private boolean doesDestinationAddressMatch(String ruleDestinationAddress, String requestDestinationAddress) {
        return isAnySetInRule(ruleDestinationAddress) || ruleDestinationAddress.equals(requestDestinationAddress);
    }

    private boolean doesDestinationPortMatch(String ruleDestinationPort, String requestDestinationPort) {
        return isAnySetInRule(ruleDestinationPort) || ruleDestinationPort.equals(requestDestinationPort);
    }

    private boolean isAnySetInRule(String ruleValue) {
        return ANY.equalsIgnoreCase(ruleValue);
    }

}
