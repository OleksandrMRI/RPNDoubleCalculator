package com.shpp.p2p.cs.ohololobov.assignment10.token;

import java.util.List;

/**
 * ENUM contains opening and closing brackets although logic of handling of ENUM`s objects
 */
public enum Bracket implements Token {
    OPENING_BRACKET('(',Rank.OPENING_BRACKET.rank()) {
        /**
         * the method validate next char after opening bracket
         * @param expression     linear representation of char
         * @param charPosition     index of next char in expression
         */
        @Override
        public void validateNextChar(String expression, int charPosition) {
            char nextChar = expression.charAt(charPosition);
            if (nextChar != '-' && !Character.isLetter(nextChar) && !Character.isDigit(nextChar) && nextChar != '(')
                Token.throwInvalidNextCharException(nextChar, expression, charPosition);
        }


    },
    CLOSING_BRACKET(')',Rank.CLOSING_BRACKET.rank()) {
        /**
         * the method validate next char after closing bracket
         * @param expression     linear representation of char
         * @param charPosition     index of next char in expression
         */
        @Override
        public void validateNextChar(String expression, int charPosition) {
            char nextChar = expression.charAt(charPosition);
            if (Character.isLetter(nextChar) || Character.isDigit(nextChar) || nextChar == OPENING_BRACKET.value)
                Token.throwInvalidNextCharException(nextChar, expression, charPosition);
        }
    };

    /**
     * string key of Bracket
     */
    private final char value;

    /**
     * Value of priority rank during calculating the mathematical expression
     */
    private final int rank;

    /**
     * constructor of instance of Bracket
     *
     * @param value string key of Bracket
     */
    Bracket(char value, int rank) {
        this.value = value;
        this.rank = rank;
    }

    /**
     * getting string key of Bracket instance
     *
     * @return string key of Bracket instance
     */
    public char value() {
        return this.value;
    }

    /**
     * getter for rank priority of token in math expression
     *
     * @return value of pri
     */
    @Override
    public int rank() {
        return this.rank;
    }

    /**
     * The method is uses to getting association of SimpleMathOperator
     *
     * @return true if operator is left-associative
     */
    @Override
    public boolean isLeftAssociative() {
        return true;
    }
    /**
     * The method validate next char in expression and throw expression if structure of expression is invalid.
     *
     * @param expressionToPars linear representation of expression
     * @param nextPosition     index of next char in expression
     */
    public abstract void validateNextChar(String expressionToPars, int nextPosition);

    /**
     * The method adds bracket in list of tokens in postfix notation
     *
     * @param expressionToPars            mathematical expression
     * @param currentIndex                index of current char in expression
     * @param tokensListInPostfixNotation list of tokens in postfix notation
     * @return index of next char
     */
    public int addToken(String expressionToPars, int currentIndex, List<Token> tokensListInPostfixNotation) {
        int nextIndex = ++currentIndex;
        if (nextIndex < expressionToPars.length())
            this.validateNextChar(expressionToPars, nextIndex);
        tokensListInPostfixNotation.add(this);

        return nextIndex;
    }
}