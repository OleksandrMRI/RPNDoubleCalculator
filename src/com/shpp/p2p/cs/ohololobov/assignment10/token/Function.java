package com.shpp.p2p.cs.ohololobov.assignment10.token;

import com.shpp.p2p.cs.ohololobov.assignment10.common.CharUtils;
import com.shpp.p2p.cs.ohololobov.assignment10.exception.MissingOperandException;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This enum contains instances corresponding to mathematical functions processing with one operand,
 * as well as their values priority rank, logic of their executing and using
 */
public enum Function implements FunctionCalculated, RPNToken {
    SIN("sin", Math::sin),
    COS("cos", Math::cos),
    TAN("tan", Math::tan),
    CTAN("ctan", arg -> Math.tan(90 - arg)),
    ATAN("atan", Math::atan),
    ACTAN("actan", arg -> Math.PI - Math.atan(arg)),
    ASIN("asin", Math::asin),
    SQRT("sqrt", Math::sqrt),
    LOG10("log10", Math::log10),
    LOG2("log2", arg -> Math.log10(arg) / Math.log10(2.0));
    /**
     * minimal number of operand in stack for function during calculating expression
     */
    public static final int VALID_PRECEDING_OPERANDS_NUMBER = 1;
    public static final String INVALID_TAN_OR_CTAN_VALUE_MSG = "Invalid key of argument of tan or ctan";
    public static final String ILLEGAL_ARGUMENT_OF_LOGARITHM_MSG = "Illegal argument of logarithm";
    public static final String UNEXPECTED_TOKEN_IN_EXPRESSION_MSG = "Unexpected token \"%s\" in expression \"%s\" at position %s";
    private static final Logger log = LoggerFactory.getLogger(Function.class);

    /**
     * String key of OneArgumentFunction instance
     */
    private final String key;
    /**
     * Value of priority rank during calculating the mathematical expression
     */
    private static final int rank = 6;
    /**
     * functional interface for definition of executable operation of OneArgumentFunction instance
     */
    private final FunctionCalculated functionCalculated;
    /**
     * HashMap with values of OneArgumentFunction instances as keys
     * and corresponding OneArgumentFunction instance as values
     */
    private static HashMap<String, Function> mathFunctionHashMap;

    /**
     * constructor of OneArgumentFunction
     *
     * @param key                key of priority rank during calculating the mathematical expression
     * @param functionCalculated functional interface for definition of executable operation of OneArgumentFunction instance
     */
    Function(String key, FunctionCalculated functionCalculated) {
        this.key = key;
        this.functionCalculated = functionCalculated;
    }

    /**
     * Method create string of values of operation with "logic OR" -  "|", as separator
     *
     * @return String of values of operation
     */
    public static String mathFunctionsToStringWithOrSeparatorRegEx() {
        StringBuilder sb = new StringBuilder("(");
        int counter = 0;
        for (Function function : Function.values()) {
            if (counter++ == Function.values().length - VALID_PRECEDING_OPERANDS_NUMBER) {
                sb.append(function.key).append(")");
            } else {
                sb.append(function.key).append("|");
            }
        }
        return sb.toString();
    }

    /**
     * The method adds function in list of tokens in postfix notation
     *
     * @param expressionToPars            mathematical expression
     * @param currentIndex                index of current char in expression
     * @param tokensListInPostfixNotation list of tokens in postfix notation
     * @return index of next char
     */
    public static int addToken(String expressionToPars, int currentIndex, List<Token> tokensListInPostfixNotation) {
        int startFunctionIndex = currentIndex;

        do {
            currentIndex++;
        } while (currentIndex < expressionToPars.length()
                && (CharUtils.isLetterIgnoreCase(expressionToPars.charAt(currentIndex)) || (CharUtils.isDigit(expressionToPars.charAt(currentIndex)))));
        if (currentIndex < expressionToPars.length())
            Function.validateNextChar(expressionToPars, currentIndex);

        String functionName = expressionToPars.substring(startFunctionIndex, currentIndex).toLowerCase();
        Map<String, Function> functionCash = Function.asMap();
        log.debug("functionCash: {}", functionCash);
        Function function = functionCash.get(functionName);
        log.debug("function: {}", function);
        if (function != null) {
            tokensListInPostfixNotation.add(function);
        } else {
            throwUnexpectedTokenException(expressionToPars, startFunctionIndex, functionName);
        }
        log.debug("currentIndex: {}", currentIndex);
        return currentIndex;
    }

    /**
     * The method throws IllegalArgumentException exception about unexpected token
     *
     * @param expressionToPars linear representation of expression
     * @param currentIndex     position of first char as number
     * @param tokenValue       unexpected key string of Token from expression
     */
    private static void throwUnexpectedTokenException(String expressionToPars, int currentIndex, String tokenValue) {
        throw new IllegalArgumentException(String.format(UNEXPECTED_TOKEN_IN_EXPRESSION_MSG, tokenValue, expressionToPars, currentIndex));
    }


    /**
     * getter for OneArgumentFunction instance priority rank field
     *
     * @return int key of priority rank
     */
    @Override
    public int rank() {
        return rank;
    }

    /**
     * getter of static field rank of enum Function
     *
     * @return value of rank
     */
    public static int getRank() {
        return rank;
    }

    /**
     * the method check number of operand before token Function in list of RPNTokens in RPN order,
     * if number of operands less as 1 method throw exception
     *
     * @param stackSize size of supporting stack with numbers, used during calculating RPN
     */
    public void validatePrecedingOperandsNumber(int stackSize) {
        if (stackSize < VALID_PRECEDING_OPERANDS_NUMBER)
            throw new MissingOperandException("\"" + this.name() + "\" expects one argument but there is no arguments");
    }

    /**
     * the method validate next char after Function
     *
     * @param expression   linear representation of char
     * @param charPosition index of next char in expression
     */
    public static void validateNextChar(String expression, int charPosition) {
        char nextChar = expression.charAt(charPosition);
        if (nextChar != '(')
            Token.throwInvalidNextCharException(nextChar, expression, charPosition);
    }

    /**
     * Method create HashMap with values of keys fields of functions instances as keys
     * and corresponding functions instances as values, with pattern Singleton
     *
     * @return map that connects keys of Functions instances and their instances
     */
    public static Map<String, Function> asMap() {
        if (mathFunctionHashMap == null) {
            mathFunctionHashMap = new HashMap<>();
            for (Function function : Function.values()) {
                mathFunctionHashMap.put(function.key, function);
            }
        }
        return mathFunctionHashMap;
    }

    /**
     * Definition of calculate method of interface Calculated,
     * method calls lambda from field Calculated<SingleDoubleRecord> of OneArgumentFunction instance
     *
     * @param arg argument of function
     * @return result of calculation
     */
    @Override
    public double calculate(double arg) {
        validateTanAndCtanArgument(this, arg);
        validateLog2AndLog10Argument(this, arg);
        return this.functionCalculated.calculate(arg);
    }

    /**
     * The method checks values of argument of tan and ctan Functions and throw exception if argument invalid ist
     *
     * @param function TAN or CTAN
     * @param arg      argument of function
     */
    public void validateTanAndCtanArgument(Function function, double arg) {
        if (function == TAN && arg % Math.PI == Math.PI / 2
                || function == CTAN && arg % Math.PI == 0) {
            throw new IllegalArgumentException(INVALID_TAN_OR_CTAN_VALUE_MSG);
        }
    }

    /**
     * The method checks values of argument of tan and ctan Functions and throw exception if argument invalid ist
     *
     * @param function TAN or CTAN
     * @param arg      argument of function
     */
    public void validateLog2AndLog10Argument(Function function, double arg) {
        if ((function == LOG2 || function == LOG10) && arg < 0) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_OF_LOGARITHM_MSG);
        }
    }

    /**
     * method describes behavior of instance of Function during calculating expression
     *
     * @param stack            stack of decimal
     * @param variablesContext array of values of variables for calculating expression,
     *                         this parameter is used only with Variable Token
     */
    @Override
    public void executeAction(MutableDoubleList stack, double[] variablesContext) {
        int stackSize = stack.size();
        validatePrecedingOperandsNumber(stackSize);
        double arg = stack.removeAtIndex(stackSize - 1);
        stack.add(this.calculate(arg));
    }
}
