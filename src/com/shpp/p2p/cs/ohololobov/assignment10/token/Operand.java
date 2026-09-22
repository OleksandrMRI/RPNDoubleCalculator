package com.shpp.p2p.cs.ohololobov.assignment10.token;

/**
 * This interface extends contains contract of token interface for operand tokens - Decimal and Variable
 */
public sealed interface Operand extends RPNToken permits Decimal, Variable {
    /**
     * value of rank of operands
     */
    int rank = Rank.OPERAND.rank();

    /**
     * getter for rank instance of operand
     *
     * @return rank value
     */
    @Override
    default int rank() {
        return rank;
    }


    @Override
    default boolean isLeftAssociative() {
        return false;
    }

    /**
     * the method validate next char after Operand
     *
     * @param expression   linear representation of char
     * @param charPosition index of next char in expression
     */
    static void validateNextChar(String expression, int charPosition) {
        char nextChar = expression.charAt(charPosition);
        if (!Operator.asMap().containsKey(nextChar) && nextChar != ')')
            Token.throwInvalidNextCharException(nextChar, expression, charPosition);
    }
}
