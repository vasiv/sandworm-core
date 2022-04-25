package pl.kielce.tu.sandworm.core.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;
import pl.kielce.tu.sandworm.core.model.Rule;
import pl.kielce.tu.sandworm.core.model.enumeration.Action;
import pl.kielce.tu.sandworm.core.model.enumeration.Direction;
import pl.kielce.tu.sandworm.core.model.enumeration.Protocol;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleParserTest {

    private final RuleParserOld parser = new RuleParserOld();

    @ParameterizedTest
    @MethodSource("rulesWithActions")
    void shouldSetCorrectActionWhenParsing(String rule, Action expectedAction) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedAction, parsedRule.getAction());
    }

    @ParameterizedTest
    @MethodSource("rulesWithProtocols")
    void shouldSetCorrectProtocolWhenParsing(String rule, Protocol expectedProtocol) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedProtocol, parsedRule.getProtocol());
    }

    @ParameterizedTest
    @MethodSource("rulesWithSourceAddresses")
    void shouldSetCorrectSourceAddressWhenParsing(String rule, String expectedSourceAddress) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedSourceAddress, parsedRule.getSourceAddress());
    }

    @ParameterizedTest
    @MethodSource("rulesWithSourcePorts")
    void shouldSetCorrectSourcePortWhenParsing(String rule, String expectedSourcePort) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedSourcePort, parsedRule.getSourcePort());
    }

    @ParameterizedTest
    @MethodSource("rulesWithDirections")
    void shouldSetDirectionPortWhenParsing(String rule, Direction expectedDirection) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedDirection, parsedRule.getDirection());
    }

    @Test
    void shouldThrowAnExceptionWhenInvalidDirection() {
        String rule = "alert http 192.168.0.15 any <- " +
                "any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)";

        RuleSyntaxException exception = assertThrows(RuleSyntaxException.class, () -> parser.parse(rule));
        assertEquals("Cannot parse direction sign - non of expected ones.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("rulesWithDestinationAddresses")
    void shouldSetCorrectDestinationAddressWhenParsing(String rule, String expectedDestinationAddress) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedDestinationAddress, parsedRule.getDestinationAddress());
    }

    @ParameterizedTest
    @MethodSource("rulesWithDestinationPorts")
    void shouldSetCorrectDestinationPortWhenParsing(String rule, String expectedDestinationPort) throws RuleSyntaxException {
        Rule parsedRule = parser.parse(rule);

        assertEquals(expectedDestinationPort, parsedRule.getDestinationPort());
    }

    private static Stream<Arguments> rulesWithActions() {
        return Stream.of(
                Arguments.of("none http any any -> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", Action.NONE),
                Arguments.of("drop http 192.168.0.13 any -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00002; rev:1;)", Action.DROP),
                Arguments.of("alert http 192.168.0.15 any -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", Action.ALERT)
        );
    }

    private static Stream<Arguments> rulesWithProtocols() {
        return Stream.of(
                Arguments.of("none http any any -> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", Protocol.HTTP)
        );
    }

    private static Stream<Arguments> rulesWithSourceAddresses() {
        return Stream.of(
                Arguments.of("alert http 192.168.0.15 any -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", "192.168.0.15"),
                Arguments.of("none http any 8080 -> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", "ANY")
        );
    }

    private static Stream<Arguments> rulesWithSourcePorts() {
        return Stream.of(
                Arguments.of("alert http 192.168.0.15 any -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", "ANY"),
                Arguments.of("none http any 8080 -> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", "8080")
        );
    }

    private static Stream<Arguments> rulesWithDirections() {
        return Stream.of(
                Arguments.of("alert http 192.168.0.15 any -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", Direction.ONE_WAY),
                Arguments.of("none http any 8080 <> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", Direction.BOTH_WAYS)
        );
    }

    private static Stream<Arguments> rulesWithDestinationAddresses() {
        return Stream.of(
                Arguments.of("alert http 192.168.0.15 any -> 192.168.0.16 any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", "192.168.0.16"),
                Arguments.of("none http 192.168.0.15 8080 -> any any (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", "ANY")
        );
    }

    private static Stream<Arguments> rulesWithDestinationPorts() {
        return Stream.of(
                Arguments.of("alert http 192.168.0.15 8080 -> any any (msg:”Incoming HTTP request from malicious host.”; sid:00003; rev:1;)", "ANY"),
                Arguments.of("none http any 8080 -> any 8080 (msg:”Incoming HTTP request.”; sid:00001; rev:1;)", "8080")
        );
    }
}