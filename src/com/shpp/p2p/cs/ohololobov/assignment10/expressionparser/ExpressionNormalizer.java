package com.shpp.p2p.cs.ohololobov.assignment10.expressionparser;

import com.shpp.p2p.cs.ohololobov.assignment10.common.CharUtils;
import com.shpp.p2p.cs.ohololobov.assignment10.token.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * The class contains logic of normalization of string expression before parsing to tokens
 */
public class ExpressionNormalizer {
    /**
     * private variable for SingleTone pattern implementation
     */
    private static ExpressionNormalizer instance;
    /**
     * Instance of Logger
     */
    private static final Logger log = LoggerFactory.getLogger(ExpressionNormalizer.class);

    /**
     * Implementation of SingleTone
     *
     * @return instance of ExpressionNormalizer as SingleTone
     */
    public static ExpressionNormalizer getInstance() {
        if (instance == null) {
            instance = new ExpressionNormalizer();
        }

        return instance;
    }

    /**
     * The methods formates raw input string element from String[] args of main(),
     * removes spaces converts to lowercase and inserts missing multiplication signs into a mathematical expression;
     *
     * @param rowExpression string first element from String[] args of main()
     * @return formated string without spaces and in lowercase
     */

    public String normalize(String rowExpression) {
        String stringWithoutSpacesInLowerCase = rowExpression.replace(" ", "");
        return addMultiplicationSing(stringWithoutSpacesInLowerCase);
    }

    /**
     * The method inserts missing multiplication signs into a mathematical expression where they are absent,
     * specifically where the syntax of mathematical notation requires them—such as between a number and a variable.
     *
     * @param expression linear representation of expression
     * @return formated expression with inserted missing multiplication sign
     */
    private String addMultiplicationSing(String expression) {
        StringBuilder sb = new StringBuilder();
        int expressionLength = expression.length();
        int previousMissedMultipleSignIndex = 0;
        for (int i = 0; i < expressionLength; i++) {
            char currentChar = expression.charAt(i);
            int nextIndex = i + 1;
            if (i < expressionLength - 1) {
                char nextChar = expression.charAt(nextIndex);
                if (isLetterOrOpeningBracketAfterDecimal(currentChar, nextChar, nextIndex, expression)
                        || isOpeningBracketAfterClosingBracket(currentChar, nextChar)
                        || (i > 0 && isOpeningBracketAfterVariableInsideTheExpression(currentChar, nextChar, expression.charAt(i - 1)))
                        || (i == 0 && isOpeningBracketAfterVariableAtStartTheExpression(currentChar, nextChar))) {
                    sb.append(expression, previousMissedMultipleSignIndex, nextIndex).append("*");
                    previousMissedMultipleSignIndex = nextIndex;
                }
            }
        }
        sb.append(expression, previousMissedMultipleSignIndex, expressionLength);
        String normalizedExpression = sb.toString();
        log.info("Normalized Expression: {}", normalizedExpression);
        return normalizedExpression;
    }

    /**
     * method check presents of letter or '(' after decimal
     *
     * @param currentChar number char in expression
     * @param nextChar    checked char, next after current in expression
     * @param nextIndex   index of checked char
     * @param expression  linear representation of expression
     * @return true, if checked char letter or '('
     */
    private boolean isLetterOrOpeningBracketAfterDecimal(char currentChar, char nextChar, int nextIndex, String expression) {
        return (CharUtils.isDigit(currentChar) && (CharUtils.isLetter(nextChar) || nextChar == '(')
                && !expression.substring(0, nextIndex).toLowerCase(Locale.ROOT).matches(".*" + Function.mathFunctionsToStringWithOrSeparatorRegEx()));
    }

    /**
     * method check presents of '(' after ')'
     *
     * @param currentChar ')'
     * @param nextChar    checked char, next after current in expression
     * @return true, if checked char '('
     */
    private boolean isOpeningBracketAfterClosingBracket(char currentChar, char nextChar) {
        return currentChar == ')' && nextChar == '(';
    }

    /**
     * method check presents of '(' after variables at any position in expression except first position
     *
     * @param currentChar  a letter
     * @param nextChar     checked char, next after current in expression
     * @param previousChar checked char, previous char to current in expression
     * @return true, if next checked char is '(' and precious is not a letter
     */
    private boolean isOpeningBracketAfterVariableInsideTheExpression(char currentChar, char nextChar, char previousChar) {
        return !CharUtils.isLetter(previousChar) && CharUtils.isLetter(currentChar) && nextChar == '(';
    }

    /**
     * method check presents of '(' after variables at first position in expression
     *
     * @param currentChar a letter
     * @param nextChar    checked char, next after current in expression
     * @return true, if next checked char is '('
     */
    private boolean isOpeningBracketAfterVariableAtStartTheExpression(char currentChar, char nextChar) {
        return CharUtils.isLetter(currentChar) && nextChar == '(';
    }
}
