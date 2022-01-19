package pl.kielce.tu.sandworm.core.constants;

public abstract class SandwormCoreConstants {

    private SandwormCoreConstants() {
    }

    public static final String REQUEST_DROPPED_RESPONSE_MESSAGE = "Request has been dropped due to security violation.";
    public static final String SOURCE_ADDRESS_HEADER_NAME = "X-Forwarded-For";
    public static final String SOURCE_PORT_HEADER_NAME = "X-Forwarded-Port";
    public static final String HOST_HEADER_NAME = "host";
    public static final String ANY = "any";
    public static final String JSON_EXTENSION = ".json";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String RULE_OPTIONS_REGEX = "\\((.*?)\\)";


}
