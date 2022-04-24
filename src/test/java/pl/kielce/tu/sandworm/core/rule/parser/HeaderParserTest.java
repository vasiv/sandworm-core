package pl.kielce.tu.sandworm.core.rule.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kielce.tu.sandworm.core.exception.RuleSyntaxException;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeaderParserTest {

    private HeaderParser parser;

    @BeforeEach
    void setUp() {
        parser = new HeaderParser();
    }

    @Test
    void shouldThrowAnExceptionWhenChunkNotFound() {
        String rule = "none " +
                "http any -> any any " +
                "(msg:”Incoming HTTP request.”; sid:00001; rev:1;)";


        RuleSyntaxException exception = assertThrows(RuleSyntaxException.class, () -> parser.parse(rule.split(SPACE)));
    }

}