package com.shpp.p2p.cs.ohololobov.assignment10.token;

import java.util.List;

import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.OPENING_BRACKET;

/**
 * this record contains logic of token unary minus. Unary minus is a "-" element of expression
 * which is located either at the very beginning of the sentence
 * or immediately after the opening parenthesis
 *
 * @param key                     field string value of unary minus
 * @param rank                    priority rank of token by calculating
 * @param unaryMinusMultiplicator the number to multiply by to obtain the equivalent of a unary minus
 */
public record UnaryMinus(String key, int rank, double unaryMinusMultiplicator) implements OperatorToken {
    private static final int rankValue = -1;
    private static final double multiplicator = -1;
    private static UnaryMinus instance;

    /**
     * constructor of unary minus
     */
    private UnaryMinus() {
        this("-", rankValue, multiplicator);
    }

    public static UnaryMinus getInstance() {
        if (instance == null) {
            instance = new UnaryMinus();
        }
        return instance;
    }

    /**
     * priority rank of token by calculating
     *
     * @return value of rank
     */
    public static int getRank() {
        return rankValue;
    }

    /**
     * getter to return the number to multiply by to obtain the equivalent of a unary minus
     *
     * @return unary minus multiplicator
     */
    public static double getMultiplicator() {
        return multiplicator;
    }

    /**
     * the method checks if current char in expression corresponds to unary minus
     *
     * @param expressionToPars linear representation of mathematical expression
     * @param currentPosition  position of current checked char
     * @return true if checked char is a "-"
     * which is located either at the very beginning of the sentence
     */
    public static boolean isUnaryMinus(String expressionToPars, int currentPosition) {
        return (currentPosition == 0 || expressionToPars.charAt(currentPosition - 1) == OPENING_BRACKET.value());
    }

    /**
     * The method adds unary minus in list of tokens in postfix notation
     *
     * @param expressionToPars            mathematical expression
     * @param currentIndex                index of current char in expression
     * @param tokensListInPostfixNotation list of tokens in postfix notation
     * @return index of next char
     */
    public static int addToken(String expressionToPars, int currentIndex, List<Token> tokensListInPostfixNotation) {
        int nextIndex = ++currentIndex;
        UnaryMinus unaryMinus = UnaryMinus.getInstance();
        if (nextIndex < expressionToPars.length())
            unaryMinus.validateNextChar(expressionToPars, nextIndex);

        tokensListInPostfixNotation.add(unaryMinus);

        return nextIndex;
    }
}
