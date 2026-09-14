package com.shpp.p2p.cs.ohololobov.assignment10.token;

/**
 * this  interface extends contract of interface Token for Operator and UnaryMinus
 * with logic of validation next char, that common for Operator and UnaryMinus
 */
public sealed interface OperatorToken extends Token permits Operator, UnaryMinus {
    default void validateNextChar(String expression, int charPosition) {
        char nextChar = expression.charAt(charPosition);
        if (Operator.asMap().containsKey("" + nextChar) || nextChar == ')' || nextChar == ',')
            Token.throwInvalidNextCharException(nextChar, expression, charPosition);
    }
}
