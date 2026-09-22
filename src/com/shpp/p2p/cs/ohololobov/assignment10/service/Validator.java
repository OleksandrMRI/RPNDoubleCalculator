package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.AppConfig;
import com.shpp.p2p.cs.ohololobov.assignment10.exception.MissingVariableException;
import org.eclipse.collections.api.iterator.CharIterator;
import org.eclipse.collections.api.list.primitive.MutableCharList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList;

import java.io.IOException;
import java.util.List;

import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.OPENING_BRACKET;

/**
 * The class contains validation of expression and variables and their connection
 */
public class Validator {
    public static final String VARIABLE_DECIMAL_REG_EX = "-?[0-9]+(\\" + AppConfig.DECIMAL_SEPARATOR + "[0-9]+(E(-)?[0-9]+)?)?";
    public static final int VALID_EQUALITY_ARRAY_LENGTH = 2;
    public static final String START_END_ILLEGAL_ARGUMENT_TEMPLATE = "Illegal argument \"%s\" at %s of expression \"%s\"";
    public static final String NO_ARGUMENTS_IN_MAIN_ARGS_MSG = "There is no arguments in main()";
    public static final String INCORRECT_VARIABLE_EQUALITY_MSG = "Incorrect variable equality";
    public static final int VARIABLE_NAME_INDEX = 0;

    public static final int VARIABLE_VALUE_INDEX = 1;
    public static final String ILLEGAL_NUMBER_OF_BRACKETS_MSG = "Illegal number of brackets";
    public static final String MISSING_VARIABLE_MSG = "Missing variable(s): %s. Enter correct program arguments.";
    public static final String NO_VARIABLES_FOR_SUBSTITUTION_EXCEPTION_TEMPLATE = "Expected %s variables for substitution, but there are none";
    public static final int VALID_VARIABLE_NAME_LENGTH = 1;
    public static final String INVALID_NUMBER_OF_BRACKETS = "Invalid number of brackets";


    public static void isArgsEmpty(String[] arr) throws IOException {
        if (arr.length == 0) {
            throw new IOException(NO_ARGUMENTS_IN_MAIN_ARGS_MSG);
        }
    }

    public static void validateStartOfExpression(String expression) {
        char firstChar = expression.charAt(0);
        String partOfExpression = "start";
        if (!expression.startsWith("-") && !Character.isLetter(firstChar) && !Character.isDigit(firstChar) && !expression.startsWith("("))
            throw new IllegalArgumentException(
                    String.format(START_END_ILLEGAL_ARGUMENT_TEMPLATE, firstChar, partOfExpression, expression)
            );
    }

    public static void validateEndOfExpression(String expression) {
        char lastChar = expression.charAt(expression.length() - VARIABLE_VALUE_INDEX);
        String partOfExpression = "end";
        if (!expression.endsWith(")") && !Character.isLetter(lastChar) && !Character.isDigit(lastChar))
            throw new IllegalArgumentException(
                    String.format(START_END_ILLEGAL_ARGUMENT_TEMPLATE, lastChar, partOfExpression, expression)
            );
    }

    public static void checkAllVariablesPresence(MutableCharIntMap variablesInArray, MutableDoubleList[] variablesValues) throws MissingVariableException {
        MutableCharList missedVariables = new CharArrayList();
        CharIterator iterator = variablesInArray.keysView().charIterator();
        while (iterator.hasNext()) {
            char key = iterator.next();
            if (variablesValues[variablesInArray.get(key)] == null) {
                missedVariables.add(key);
            }
        }

        if (!missedVariables.isEmpty())
            throw new MissingVariableException(String.format(MISSING_VARIABLE_MSG, missedVariables));
    }

    public static void validateVariableEquality(String[] variableEqualityArray) {
        if (variableEqualityArray.length != VALID_EQUALITY_ARRAY_LENGTH
                || variableEqualityArray[VARIABLE_NAME_INDEX].length() != VALID_VARIABLE_NAME_LENGTH
                || !Character.isLetter(variableEqualityArray[VARIABLE_NAME_INDEX].charAt(0))
                || !variableEqualityArray[VARIABLE_VALUE_INDEX].matches(Validator.VARIABLE_DECIMAL_REG_EX))
            throw new IllegalArgumentException(INCORRECT_VARIABLE_EQUALITY_MSG);
    }

    public static void isValidBracketsNumber(int rankOfToken) {
        if (rankOfToken != OPENING_BRACKET.rank()) {
            throw new RuntimeException(ILLEGAL_NUMBER_OF_BRACKETS_MSG);
        }
    }

    public static void validateVariablesAbsence(MutableCharIntMap variablesInExpression, List<String> rawVariablesEqualities) {
        if (rawVariablesEqualities == null && variablesInExpression != null) {
            throw new IllegalArgumentException(String.format(NO_VARIABLES_FOR_SUBSTITUTION_EXCEPTION_TEMPLATE, variablesInExpression.size()));
        }
    }

    public static void validateBrackets(int bracketCounter, boolean isCheckingAfterParsing) {
        if ((isCheckingAfterParsing && bracketCounter > 0) || bracketCounter < 0)
            throw new IllegalArgumentException(INVALID_NUMBER_OF_BRACKETS);

    }
}
