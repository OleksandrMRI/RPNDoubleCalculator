package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.InputRawDataDTO;
import com.shpp.p2p.cs.ohololobov.assignment10.dto.ResultsDTO;
import com.shpp.p2p.cs.ohololobov.assignment10.service.ExpressionAndVariableParserFacade;
import com.shpp.p2p.cs.ohololobov.assignment10.service.RPNContext;
import com.shpp.p2p.cs.ohololobov.assignment10.service.VectorEvaluator;
import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;

import java.io.IOException;
import java.util.List;

/**
 * This class orchestrator for performing calculations of a mathematical formula presented as a string,
 * in which calculations are performed on double numbers
 */
public class DoubleCalculator implements CalculatorEngine {
    private static DoubleCalculator instance;

    private DoubleCalculator() {
    }

    public static DoubleCalculator getInstance() {
        if (instance == null) {
            instance = new DoubleCalculator();
        }
        return instance;
    }

    /**
     * The method manages calculating pipeline sequencies from row strings
     * of expression and variable equalities from args[] to vector of results as double[]:
     * 1. Routing input data
     * 2. Expression processing
     * - normalization
     * - parsing in postfix notation
     * - parsing in RPN
     * 3. Variables processing
     * - normalization
     * - choosing necessary variables
     * - parsing in vector of variables values in form of array of MutableDoubleList
     * 4. Batch calculating
     * - batching data
     * - calculating batch
     * - building output result vector as double[]
     *
     * @param args command-line arguments containing line representation of expression and variable equalities
     * @return output result vector as double[]
     * @throws IOException if args[] length equals 0
     */
    @Override
    public ResultsDTO runCalculator(String[] args) throws IOException {

        InputRawDataDTO inputRawDataDTO = new ArgsDispatcher().route(args);
        RPNContext rpnContext = ExpressionAndVariableParserFacade.getInstance().parsData(inputRawDataDTO);

        List<RPNToken> rpnTokensList = rpnContext.rpnTokensList();
        MutableDoubleList[] variablesValues = rpnContext.variablesValues();
        MutableCharIntMap variablesInExpression = rpnContext.variablesInExpression();
        double[] results = VectorEvaluator.getInstance().evaluate(
                rpnTokensList,
                variablesValues
        );
        return new ResultsDTO(rpnContext.normalizedExpression(), results, variablesValues, variablesInExpression);
    }
}
