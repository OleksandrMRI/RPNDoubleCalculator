package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;

import java.util.List;

/**
 * Class calculator contains logic of calculating result
 * from list of token in RPN with batch of variables values
 */
public class Calculator {
    /**
     * instance of Calculator
     */
    private static Calculator instance;

    /**
     * singleton for creating instance of class Calculator as Singleton
     *
     * @return instance of class Calculator
     */
    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        return instance;
    }

    /**
     * method calculates result using list of token in RPN amd batch of variable values
     *
     * @param rpnTokensList        list of RPNTokens in RPN order
     * @param variablesValuesBatch array of variables values that are present in expression
     * @return double result
     */
    public double evaluateBatch(List<RPNToken> rpnTokensList, double[] variablesValuesBatch) {
        MutableDoubleList stackBuffer = new DoubleArrayList();
        RPNToken rpnToken;
        for (RPNToken token : rpnTokensList) {
            rpnToken = token;
            rpnToken.executeAction(stackBuffer, variablesValuesBatch);
        }
        return stackBuffer.getFirst();
    }
}
