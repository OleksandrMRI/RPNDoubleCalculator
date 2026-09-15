package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The class calculate expression with vector of variables values
 */
public class VectorEvaluator {
    Logger log = LoggerFactory.getLogger(VectorEvaluator.class);
    /**
     * empty array of batch of variables with length 0 for calculating expression if expression contains no ariables
     */
    private static final double[] EMPTY_BATCH = new double[0];
    /**
     * array of batch of variables with length 1 to calculate expression that contains only one variable
     */
    private static final double[] ONE_VARIABLE_BATCH = new double[1];
    private final Calculator calculator;

    /**
     * constructor of VectorEvaluator
     *
     * @param calculator instance of Calculator
     */
    private VectorEvaluator(Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     * instance of VectorEvaluator
     */
    private static VectorEvaluator instance;

    /**
     * Implementation of SingleTone
     *
     * @return instance of VectorEvaluator as SingleTone
     */
    public static VectorEvaluator getInstance() {
        if (instance == null) {
            instance = new VectorEvaluator(
                    Calculator.getInstance()
            );
        }
        return instance;
    }

    /**
     * the method prepare batches of variables from vector and calls Calculator to calculate expression with each batches
     *
     * @param rpnTokensList           list of RPNTokens in RPN order
     * @param variablesValuesDataBase array of MutableDoubleLists with variables values
     * @return array of results
     */
    public double[] evaluate(List<RPNToken> rpnTokensList, MutableDoubleList[] variablesValuesDataBase) {
        double[] results;
        if (variablesValuesDataBase == null) {
            log.debug("In empty variables");
            results = new double[1];
            results[0] = calculator.evaluateBatch(rpnTokensList, EMPTY_BATCH);
        } else {
            int numberOfVariableBatches = variablesValuesDataBase[0].size();
            results = new double[numberOfVariableBatches];
            int numberOfVariables = variablesValuesDataBase.length;
            double[] variablesBatch;
            if (numberOfVariables == 1) {
                variablesBatch = ONE_VARIABLE_BATCH;
                MutableDoubleList variableValues = variablesValuesDataBase[0];

                for (int i = 0; i < numberOfVariableBatches; i++) {
                    variablesBatch[0] = variableValues.get(i);
                    results[i] = calculator.evaluateBatch(rpnTokensList, variablesBatch);
                }
            } else {
                variablesBatch = new double[numberOfVariables];
                for (int i = 1; i < variablesValuesDataBase.length; i++) {
                    numberOfVariableBatches = Math.min(numberOfVariableBatches, variablesValuesDataBase[i].size());
                }

                for (int i = 0; i < numberOfVariableBatches; i++) {
                    for (int j = 0; j < variablesBatch.length; j++) {
                        variablesBatch[j] = variablesValuesDataBase[j].get(i);
                    }
                    results[i] = calculator.evaluateBatch(rpnTokensList, variablesBatch);
                }


            }
        }
        return results;
    }

    /**
     * overloaded method for using in 2D graph for calculating x and y coordinates of points
     *
     * @param rpnTokensList         list of RPNTokens in RPN order
     * @param offsetXValues         array of x coordinates of points
     * @param variablesInExpression map, that describes connection between variables names
     *                              and positions of their values in array of variables values,
     *                              can be null, if there is no variable in expression present
     * @return array of y coordinates of points
     */
    public double[] evaluate(List<RPNToken> rpnTokensList, double[] offsetXValues, MutableCharIntMap variablesInExpression) {
        double[] results = new double[offsetXValues.length];
        double[] variablesBatch = ONE_VARIABLE_BATCH;
        if (variablesInExpression == null) {
            results[0] = calculator.evaluateBatch(rpnTokensList, variablesBatch);
            for (int i = 1; i < offsetXValues.length; i++) {
                results[i] = results[0];
            }
        } else {
            for (int i = 0; i < offsetXValues.length; i++) {
                variablesBatch[0] = offsetXValues[i];
                results[i] = calculator.evaluateBatch(rpnTokensList, variablesBatch);
            }
        }
        return results;
    }
}
