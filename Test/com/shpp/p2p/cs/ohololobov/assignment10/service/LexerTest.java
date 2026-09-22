package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.Lexer;
import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LexerTest {
    static Stream<Arguments> stringsToTokensListMatches() {
        return Stream.of(
                Arguments.of("3+2",
                        List.of(
                                new Decimal(3),
                                Operator.PLUS,
                                new Decimal(2)
                        )
                ),
                Arguments.of("3.2",
                        List.of(
                                new Decimal(3.2)
                        )
                ),
                Arguments.of("log10(a)",
                        List.of(
                                Function.LOG10,
                                Bracket.OPENING_BRACKET,
                                new Variable('a', 0),
                                Bracket.CLOSING_BRACKET
                        )
                ),
                Arguments.of("-(30/5)",
                        List.of(
                                Operator.UNARY_MINUS,
                                Bracket.OPENING_BRACKET,
                                new Decimal(30),
                                Operator.DIVISION,
                                new Decimal(5),
                                Bracket.CLOSING_BRACKET
                        )
                ),
                Arguments.of("-(3*a/(5+cos(60)))",
                        List.of(
                                Operator.UNARY_MINUS,
                                Bracket.OPENING_BRACKET,
                                new Decimal(3),
                                Operator.MULTIPLICATION,
                                new Variable('a', 0),
                                Operator.DIVISION,
                                Bracket.OPENING_BRACKET,
                                new Decimal(5),
                                Operator.PLUS,
                                Function.COS,
                                Bracket.OPENING_BRACKET,
                                new Decimal(60),
                                Bracket.CLOSING_BRACKET,
                                Bracket.CLOSING_BRACKET,
                                Bracket.CLOSING_BRACKET
                        )
                )
        );
    }

    static Stream<Arguments> stringsWithExceptionMatches() {
        return Stream.of(
                Arguments.of("2+#/7",
                        "Unexpected token in expression \"2+#/7\" at position 2"

                ),
                Arguments.of("3a",
                        "Illegal argument \"a\" in expression \"3a\" at position 1"

                ),
                Arguments.of("3(+4",
                        "Illegal argument \"(\" in expression \"3(+4\" at position 1"

                ),
                Arguments.of(")-",
                        "Illegal argument \")\" at start of expression \")-\""
                ),
                Arguments.of("+0",
                        "Illegal argument \"+\" at start of expression \"+0\""
                ),
                Arguments.of("-3(",
                        "Illegal argument \"(\" at end of expression \"-3(\""
                ),
                Arguments.of("-3.(+3",
                        "Invalid decimal in expression \"-3.(+3\" at position 1"
                ),
                Arguments.of("-.3.(+3",
                        "Unexpected token in expression \"-.3.(+3\" at position 1"
                ),
                Arguments.of("-3/()+1",
                        "Illegal argument \")\" in expression \"-3/()+1\" at position 4"
                ),
                Arguments.of("-3/(+1",
                        "Illegal argument \"+\" in expression \"-3/(+1\" at position 4"
                ),
                Arguments.of("-3/(sin+1",
                        "Illegal argument \"+\" in expression \"-3/(sin+1\" at position 7"
                ),
                Arguments.of("-3/(sin)1",
                        "Illegal argument \")\" in expression \"-3/(sin)1\" at position 7"
                ),
                Arguments.of("-3/(sin1",
                        "Unexpected token \"sin1\" in expression \"-3/(sin1\" at position 4"
                ),
                Arguments.of("-3/(sinf",
                        "Unexpected token \"sinf\" in expression \"-3/(sinf\" at position 4"
                ),
                Arguments.of("-3/)+1",
                        "Illegal argument \")\" in expression \"-3/)+1\" at position 3"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("stringsToTokensListMatches")
    void parseTest(String expression, List<Token> expectedTokens) {
        List<Token> resultTokens = Lexer.getInstance().tokenize(expression).tokens();
        assertEquals(expectedTokens, resultTokens);
    }

    @ParameterizedTest
    @MethodSource("stringsWithExceptionMatches")
    void parseExceptionTest(String expression, String exceptionMSG) {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Lexer.getInstance().tokenize(expression));
        assertEquals(exceptionMSG, exception.getMessage());
    }

}