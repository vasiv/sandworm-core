package pl.kielce.tu.sandworm.core.exception;

public class RuleSyntaxException extends Exception {

    public RuleSyntaxException(String message) {
        super(message);
    }

    public RuleSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

}
