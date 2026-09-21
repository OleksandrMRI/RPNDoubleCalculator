package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.LexerContext;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.Lexer;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.RPNExpressionParser;
import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RPNExpressionParserTest {
    static Stream<Arguments> stringsToTokensListMatches() {
        return Stream.of(
                Arguments.of("3+2",
                        List.of(
                                new Decimal(3),
                                new Decimal(2),
                                Operator.PLUS
                        )
                ),
                Arguments.of("3.2",
                        List.of(
                                new Decimal(3.2)
                        )
                ),
                Arguments.of("3.2^cos(60)",
                        List.of(
                                new Decimal(3.2),
                                new Decimal(60),
                                Function.COS,
                                Operator.POW

                        )
                ),
                Arguments.of("cos(60^2)",
                        List.of(
                                new Decimal(60),
                                new Decimal(2),
                                Operator.POW,
                                Function.COS

                        )
                ),
                Arguments.of("log10(a)",
                        List.of(
                                new Variable('a', 0),
                                Function.LOG10
                        )
                ),
                Arguments.of("2^3.2^2",
                        List.of(
                                new Decimal(2),
                                new Decimal(3.2),
                                new Decimal(2),
                                Operator.POW,
                                Operator.POW
                        )
                ),
                Arguments.of("-(30/5)",
                        List.of(
                                new Decimal(-1),
                                new Decimal(30),
                                new Decimal(5),
                                Operator.DIVISION,
                                Operator.MULTIPLICATION
                        )
                ),
                Arguments.of("-(3*a/(5+cos(60)))",
                        List.of(
                                new Decimal(-1),
                                new Decimal(3),
                                new Variable('a', 0),
                                new Decimal(5),
                                new Decimal(60),
                                Function.COS,
                                Operator.PLUS,
                                Operator.DIVISION,
                                Operator.MULTIPLICATION,
                                Operator.MULTIPLICATION
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("stringsToTokensListMatches")
    void parseTest(String expression, List<Token> tokens) {
        LexerContext lexerContext = Lexer.getInstance().tokenize(expression);
        List<RPNToken> expectedListToken = RPNExpressionParser.getInstance().parse(lexerContext.tokens());
        assertEquals(tokens, expectedListToken);
    }
}