package com.shpp.p2p.cs.ohololobov.assignment10.cli;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.ResultsDTO;
import org.eclipse.collections.api.iterator.CharIterator;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;

/**
 * This class outputs the report about calculation result to the console
 */
public class ConsoleOutputHandler implements OutputHandler {
    /**
     * template for report of calculated expression
     */
    private static final String EXPRESSION_TEMPLATE = "Expression: ";
    /**
     * template for report of calculated all variables in calculation
     */
    private static final String VARIABLES_TEMPLATE = "variable: ";
    /**
     * template for report of result of calculation
     */
    private static final String RESULT_TEMPLATE = "result: ";
    /**
     * instance of class ConsoleOutputHandler
     */
    private static ConsoleOutputHandler instance;

    /**
     * singleton for creating instance of class ConsoleOutputHandler as Singleton
     *
     * @return instance of class ConsoleOutputHandler
     */
    public static ConsoleOutputHandler getInstance() {
        if (instance == null) {
            instance = new ConsoleOutputHandler();
        }

        return instance;
    }

    /**
     * The method print report of calculating in console
     *
     * @param result DTO with fields expression, variables names and double result
     */
    @Override
    public void send(ResultsDTO result) {
        String expression = result.expression();
        System.out.println(EXPRESSION_TEMPLATE + expression);
        MutableCharIntMap variablesPositionsInExpression = result.variablesSlots();

        char[] variableNames;
        double[] results = result.calculationResults();
        if (variablesPositionsInExpression != null) {
            variableNames = new char[variablesPositionsInExpression.size()];
            CharIterator iterator = variablesPositionsInExpression.keysView().charIterator();
            while (iterator.hasNext()) {
                char key = iterator.next();
                variableNames[variablesPositionsInExpression.get(key)] = key;
            }

            MutableDoubleList[] variablesValues = result.variablesValues();
            for (int i = 0; i < variablesValues[0].size(); i++) {
                for (int j = 0; j < variablesValues.length; j++) {
                    System.out.println(VARIABLES_TEMPLATE + variableNames[j] + " = " + variablesValues[j].get(i));
                }
                System.out.println(RESULT_TEMPLATE + results[i]);
                System.out.println();
            }
        } else {
            System.out.println(RESULT_TEMPLATE + results[0]);
        }
    }
}
