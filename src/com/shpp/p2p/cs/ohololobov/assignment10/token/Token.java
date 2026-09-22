package com.shpp.p2p.cs.ohololobov.assignment10.token;

/**
 * interface with contract for oll wrappers for elements that contains in expression.
 */
public sealed interface Token permits Bracket, OperatorToken, RPNToken {

    int rank();

    static void throwInvalidNextCharException(char nextChar, String expression, int charPosition) throws IllegalArgumentException {
        throw new IllegalArgumentException("Illegal argument \"" + nextChar + "\" in expression \"" + expression + "\" at position " + charPosition);
    }

    boolean isLeftAssociative();
}
