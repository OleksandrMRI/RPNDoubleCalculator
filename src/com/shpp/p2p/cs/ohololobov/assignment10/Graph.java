package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.cli.GraphPainter;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.ExpressionNormalizer;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.Lexer;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.LexerContext;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.RPNExpressionParser;
import com.shpp.p2p.cs.ohololobov.assignment10.service.VectorEvaluator;
import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class get expression with one variable from user
 * and calculate a predetermined number of values for the variable (coordinate x)
 * within a specified range.
 * It then calculates the result (coordinate y) for each variable value and generates points.
 * A graph is formed from these points.
 * In this program a
 */
public class Graph {
    public static final Map<String, double[]> expressionsDataBase = new HashMap<>();
    private static final String VARIABLE_NAME = "a";
    private static final String EXPRESSION = String.format("sin(%s/50)*100", VARIABLE_NAME);
    private static final int POINTS_NUMBER = 1000;
    private static final double START_POINT_OFFSET_X = -300;
    private static final double END_POINT_OFFSET_X = 300;
    private static final String REQUEST_TO_USER = "Enter mathematical expression: ";
    private static final String INVALID_EXPRESSION_MSG = "Invalid expression: \"%s\". Program starts with default expression \n";
    public static final String INVALID_VALUE_OF_VARIABLES_MSG = "Expression for graph must contains only one variable";
    private static double[] defaultValue;
    private static double[] variableValues;
    private static Graph instance;

    static void main() {
        Graph graph = Graph.getInstance();
        graph.run();
    }

    public static Graph getInstance() {
        if (instance == null) {
            instance = new Graph();
        }

        return instance;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String rowExpression = getExpression(scanner);
            double[] offsetXValues = generateOffsetXValuesArray();
            String normalizedExpression = ExpressionNormalizer.getInstance().normalize(rowExpression);

            double[] offsetYValues = evaluateOrDefault(normalizedExpression, offsetXValues);
            GraphPainter.paint(offsetXValues, offsetYValues, GraphContext.normalizedExpression);
        }
    }

    private static String getExpression(Scanner scanner) {
        System.out.println(REQUEST_TO_USER);

        return scanner.nextLine();
    }

    private double[] generateOffsetXValuesArray() {
        if (variableValues == null) {
            variableValues = new double[POINTS_NUMBER];
            double step = (END_POINT_OFFSET_X - START_POINT_OFFSET_X) / (POINTS_NUMBER - 1);
            double offsetX = START_POINT_OFFSET_X;

            for (int i = 0; i < variableValues.length; i++) {
                variableValues[i] = offsetX;
                offsetX += step;
            }
        }

        return variableValues;
    }

    /**
     * The method parses expression string to List of tokens or receives List of tokens from database
     *
     * @param expression String, that is linear representation of a mathematical expression
     * @return array of token after parsing
     */
    private double[] getOffsetYValuesArray(String expression, double[] offsetXValues) {
        double[] offsetYValues;
        if (!expressionsDataBase.containsKey(expression)) {
            LexerContext lexerContext = parsExpressionToPostfixNotation(expression);

            List<RPNToken> rpnTokens = RPNExpressionParser.getInstance().parse(lexerContext.tokens());
            offsetYValues = VectorEvaluator.getInstance().evaluate(rpnTokens, offsetXValues, lexerContext.variablesInExpression());
            expressionsDataBase.put(expression, offsetYValues);
        } else {
            offsetYValues = expressionsDataBase.get(expression);
        }
        return offsetYValues;
    }

    private double[] evaluateOrDefault(String normalizedExpression, double[] offsetXValues) {
        double[] offsetYValues;
        try {
            offsetYValues = getOffsetYValuesArray(normalizedExpression, offsetXValues);
            GraphContext.normalizedExpression =EXPRESSION;
        } catch (Exception e) {
            System.out.printf(INVALID_EXPRESSION_MSG, normalizedExpression);
            if (defaultValue == null) {
                defaultValue = getOffsetYValuesArray(EXPRESSION, offsetXValues);
            }
            offsetYValues = defaultValue;
            GraphContext.normalizedExpression =EXPRESSION;
        }
        return offsetYValues;
    }

    private LexerContext parsExpressionToPostfixNotation(String normalizedExpression) {
        LexerContext lexerContext = Lexer.getInstance().tokenize(normalizedExpression);
        if (lexerContext.variablesInExpression() != null && lexerContext.variablesInExpression().size() > 1) {
            throw new IllegalArgumentException(
                    INVALID_VALUE_OF_VARIABLES_MSG + String.format(INVALID_EXPRESSION_MSG, normalizedExpression));
        }

        return lexerContext;
    }
    private static class GraphContext{
        private static String normalizedExpression;


    }
}
