package com.shpp.p2p.cs.ohololobov.assignment10.token;

import com.shpp.p2p.cs.ohololobov.assignment10.AppConfig;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;

import java.util.List;

/**
 * The record contains logic of token that represent decimal in Token Wrapper
 *
 * @param value numeric value of decimal
 */
public record Decimal(double value) implements Operand {
    public static final String INVALID_DECIMAL_SIGNATURE_TEMPLATE = "Invalid decimal in expression \"%s\" at position %d";

    /**
     * The method adds decimal in list of tokens in postfix notation
     *
     * @param expressionToPars            mathematical expression
     * @param currentIndex                index of current char in expression
     * @param currentChar                 current char
     * @param tokensListInPostfixNotation list of tokens in postfix notation
     * @return index of next char
     */
    public static int addToken(String expressionToPars, int currentIndex, char currentChar, List<Token> tokensListInPostfixNotation) {
        int startDecimalIndex = currentIndex;
        int decimalSeparatorCounter = 0;
        char lastChar;

        do {
            if (currentChar == AppConfig.DECIMAL_SEPARATOR) {
                validateDecimalSeparatorNumber(++decimalSeparatorCounter, expressionToPars, startDecimalIndex);
            }
            lastChar = currentChar;
            currentIndex++;
        } while (currentIndex < expressionToPars.length()
                && (Character.isDigit(currentChar = expressionToPars.charAt(currentIndex)) || currentChar == AppConfig.DECIMAL_SEPARATOR));

        validateDecimalLastChar(lastChar, expressionToPars, startDecimalIndex);

        if (currentIndex < expressionToPars.length())
            Operand.validateNextChar(expressionToPars, currentIndex);

        String stringValue = expressionToPars.substring(startDecimalIndex, currentIndex);

        double value = Double.parseDouble(stringValue);
        tokensListInPostfixNotation.add(new Decimal(value));

        return currentIndex;
    }

    /**
     * the method validate if last char at decimal string digit ist
     *
     * @param lastChar          last char in string representation of decimal value
     * @param expressionToPars  mathematical expression
     * @param startDecimalIndex index of the first char of decimal in mathematical expression
     */
    private static void validateDecimalLastChar(char lastChar, String expressionToPars, int startDecimalIndex) {
        if (lastChar == '.')
            throw new IllegalArgumentException(String.format(INVALID_DECIMAL_SIGNATURE_TEMPLATE, expressionToPars, startDecimalIndex));
    }

    /**
     * the method validate if number of decimal separator in decimal
     *
     * @param decimalSeparatorCounter value of decimal separator counter
     * @param expressionToPars        mathematical expression
     * @param startDecimalIndex       index of the first char of decimal in mathematical expression
     */
    private static void validateDecimalSeparatorNumber(int decimalSeparatorCounter, String expressionToPars, int startDecimalIndex) {
        if (decimalSeparatorCounter > 1)
            throw new IllegalArgumentException(String.format(INVALID_DECIMAL_SIGNATURE_TEMPLATE, expressionToPars, startDecimalIndex));
    }

    /**
     * method describes behavior of instance of Decimal during calculating expression
     *
     * @param stack            stack of decimal
     * @param variablesContext array of values of variables for calculating expression,
     *                         this parameter is used only with Variable Token
     */
    @Override
    public void executeAction(MutableDoubleList stack, double[] variablesContext) {
        stack.add(this.value);
    }
}
