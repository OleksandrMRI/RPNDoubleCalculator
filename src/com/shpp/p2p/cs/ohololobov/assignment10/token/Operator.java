package com.shpp.p2p.cs.ohololobov.assignment10.token;

import com.shpp.p2p.cs.ohololobov.assignment10.exception.MissingOperandException;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ENUM contains used in program mathematical operators with their linear values,
 * ranks in mathematical execution priority and lambda functions of executable operation.
 * It although contains logic of handling of ENUM`s instances
 */
public enum Operator implements OperatorToken, OperatorCalculated, RPNToken {
    PLUS('+', 1, Double::sum),
    MULTIPLICATION('*', 2, (a, b) -> a * b),
    DIVISION('/', 3, (a, b) -> a / b),
    POW('^', 4, Math::pow),
    //minus must always at last position be, for correct using line of operators in regular variableValue
    SUBTRACTION('-', 1, (a, b) -> a - b);
    /**
     * constant definite minimal valid number of operand before operator in list with RPNToken in RPN order
     */
    public static final int VALID_PRECEDING_OPERANDS_NUMBER = 2;
    /**
     * Map of SimpleMathOperators, where string values of operator are keys and SimpleMathOperators as values
     */
    private static Map<Character, Operator> operatorsHashMap;

    /**
     * rank of SimpleMathOperator in mathematical execution priority
     */
    private final int rank;

    /**
     * string values of SimpleMathOperator
     */
    private final char keyValue;

    /**
     * functional interface as key of hird parameter of SimpleMathOperator
     * represents lambda functions of executable operation
     */
    private final OperatorCalculated operatorCalculated;

    /**
     * Constructor of SimpleMathOperator
     *
     * @param keyValue           string values of SimpleMathOperator
     * @param rank               rank of SimpleMathOperator in mathematical execution priority
     * @param operatorCalculated functional interface as key of hird parameter of SimpleMathOperator
     *                           represents lambda functions of executable operation
     */
    Operator(char keyValue, int rank, OperatorCalculated operatorCalculated) {
        this.keyValue = keyValue;
        this.rank = rank;
        this.operatorCalculated = operatorCalculated;
    }

    /**
     * method creates anf fills Map of SimpleMathOperators,
     * where string values of operator are keys and SimpleMathOperators as values
     *
     * @return Map of string values of and SimpleMathOperators
     */
    public static Map<Character, Operator> asMap() {
        if (operatorsHashMap == null) {
            operatorsHashMap = new HashMap<>();
            for (Operator operator : Operator.values()) {
                operatorsHashMap.put(operator.keyValue, operator);
            }
        }

        return operatorsHashMap;
    }

    /**
     * The method is uses to getting rank key of SimpleMathOperator
     *
     * @return string values of SimpleMathOperator
     */
    @Override
    public int rank() {
        return this.rank;
    }

    /**
     * The method calls lambda function from third parameter of SimpleMathOperator
     *
     * @param arg1 first operation argument
     * @param arg2 second operation argument
     * @return double result of calculation
     */
    @Override
    public double calculate(double arg1, double arg2) {
        validateByZeroDivision(arg2);
        return this.operatorCalculated.calculate(arg1, arg2);
    }

    /**
     * method throw exception if operator is DIVISION and second argument is zero
     *
     * @param arg2 checked second argument of operation
     */
    private void validateByZeroDivision(double arg2) {
        if (this == DIVISION && arg2 == 0)
            throw new ArithmeticException("Division by zero");
    }

    /**
     * the method check number of operand before token Operator in list of RPNTokens in RPN order,
     * if number of operands less as 2, method throw exception
     *
     * @param stackSize size of supporting stack with numbers, used during calculating RPN
     */
    public void validatePrecedingOperandsNumber(int stackSize) {
        if (stackSize < VALID_PRECEDING_OPERANDS_NUMBER)
            throw new MissingOperandException("\"" + this.keyValue + "\" expects two arguments but there is only " + stackSize + " arguments");
    }

    /**
     * method describes behavior of instance of Operator during calculating expression
     *
     * @param stack            stack of decimal
     * @param variablesContext array of values of variables for calculating expression,
     *                         this parameter is used only with Variable Token
     */
    @Override
    public void executeAction(MutableDoubleList stack, double[] variablesContext) {
        validatePrecedingOperandsNumber(stack.size());
        double arg2 = stack.removeAtIndex(stack.size() - 1);
        double arg1 = stack.removeAtIndex(stack.size() - 1);
        stack.add(this.calculate(arg1, arg2));
    }

    /**
     * The method adds operand in list of tokens in postfix notation
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