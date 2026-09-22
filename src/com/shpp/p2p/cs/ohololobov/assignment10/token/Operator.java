package com.shpp.p2p.cs.ohololobov.assignment10.token;

import com.shpp.p2p.cs.ohololobov.assignment10.exception.MissingOperandException;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.OPENING_BRACKET;

/**
 * ENUM contains used in program mathematical operators with their linear values,
 * ranks in mathematical execution priority and lambda functions of executable operation.
 * It although contains logic of handling of ENUM`s instances
 */
public enum Operator implements OperatorToken, OperatorCalculated, RPNToken {
    PLUS('+', Rank.PLUS.rank(), true, Double::sum),
    MULTIPLICATION('*', Rank.MULTIPLICATOR.rank(), true, (a, b) -> a * b),
    DIVISION('/', Rank.DIVISION.rank(), true, (a, b) -> a / b),
    POW('^', Rank.POW.rank(), false, Math::pow),
    UNARY_MINUS('-', Rank.UNARY_MINUS.rank(), true, (a, b) -> -1 + b),
    //minus must always at last position be, for correct using line of operators in regular variableValue
    SUBTRACTION('-', Rank.MINUS.rank(), true, (a, b) -> a - b);
    /**
     * constant definite minimal valid number of operand before unary minus in list with RPNToken in RPN order
     */
    public static final int VALID_PRECEDING_OPERANDS_NUMBER_BEFORE_UNARY_MINUS = 1;
    /**
     * constant definite minimal valid number of operand before operator in list with RPNToken in RPN order
     */
    public static final int VALID_PRECEDING_OPERANDS_NUMBER_BEFORE_BINARY_OPERATORS = 2;
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
     * boolean shows operator associativity: left-associative - true and right-associative - false
     */
    private final boolean isLeftAssociative;

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
     * @param isLeftAssociative  associativity value left-associative or right-associative
     * @param operatorCalculated functional interface as key of hird parameter of SimpleMathOperator
     *                           represents lambda functions of executable operation
     */
    Operator(char keyValue, int rank, boolean isLeftAssociative, OperatorCalculated operatorCalculated) {
        this.keyValue = keyValue;
        this.rank = rank;
        this.isLeftAssociative = isLeftAssociative;
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
     * The method is uses to getting rank key of SimpleMathOperator
     *
     * @return string values of SimpleMathOperator
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
        return this.isLeftAssociative;
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
     * @param stackSize    size of supporting stack with numbers, used during calculating RPN
     * @param isUnaryMinus boolean value ob numbers of operands before unary minus cheches
     */
    public void validatePrecedingOperandsNumber(int stackSize, boolean isUnaryMinus) {
        if (isUnaryMinus) {
            if (stackSize < VALID_PRECEDING_OPERANDS_NUMBER_BEFORE_UNARY_MINUS)
                throw new MissingOperandException("\"" + this.keyValue + "\" expects one arguments but there is only " + stackSize + " arguments");

        } else {
            if (stackSize < VALID_PRECEDING_OPERANDS_NUMBER_BEFORE_BINARY_OPERATORS)
                throw new MissingOperandException("\"" + this.keyValue + "\" expects two arguments but there is only " + stackSize + " arguments");
        }
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
        if (this != UNARY_MINUS) {
            boolean isUnaryMinus = false;
            validatePrecedingOperandsNumber(stack.size(), isUnaryMinus);
            double arg2 = stack.removeAtIndex(stack.size() - 1);
            double arg1 = stack.removeAtIndex(stack.size() - 1);
            stack.add(this.calculate(arg1, arg2));
        } else {
            boolean isUnaryMinus = true;
            validatePrecedingOperandsNumber(stack.size(), isUnaryMinus);
            double arg2 = stack.removeAtIndex(stack.size() - 1);
            stack.add(this.calculate(Double.NaN, arg2));
        }
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