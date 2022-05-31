package pl.kielce.tu.sandworm.core.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActionParserTest {

    private ActionParser parser;

    @BeforeEach
    void setUp() {
        parser = new ActionParser();
    }

    @ParameterizedTest
    @MethodSource("rulesWithActions")
    void shouldSetCorrectActionWhenParsing(String[] rule, Action expectedAction) throws RuleSyntaxException {
        Action action = parser.parse(rule);

        assertEquals(expectedAction, action);
    }

    @Test
    void shouldThrowAnExceptionWhenActionNotRecognized() {
        String rule = "invalidAction " +
                "http any any -> any any " +
                "(msg:”Incoming HTTP request.”; sid:00001; rev:1;)";

        RuleSyntaxException exception = assertThrows(RuleSyntaxException.class, () -> parser.parse(rule.split(SPACE)));
        assertEquals("Action not recognized.", exception.getMessage());
    }

    private static Stream<Arguments> rulesWithActions() {
        return Stream.of(
                Arguments.of(("none " +
                                "http any any -> any any " +
                                "(msg:”Incoming HTTP request.”; sid:00001; rev:1;)").split(SPACE),
                        Action.PASS),
                Arguments.of(("drop " +
                                "http 192.168.0.13 any -> any any " +
                                "(msg:”Incoming HTTP request from malicious host.”; sid:00002; rev:1;)").split(SPACE),
                        Action.DROP),
                Arguments.of(("alert http 192.168.0.15 any -> any any " +
                                "(msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)").split(SPACE),
                        Action.ALERT)
        );
    }

}