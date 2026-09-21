package com.shpp.p2p.cs.ohololobov.assignment10.dto;

import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;

/**
 * DTO contains results of job of run():
 * - result of normalizing input raw expression,
 * - result of calculating of expression or results of calculating of expression
 * with all presenting batches of variables
 * - all variables with collections of their values
 * - map that connects position variable in array of variables values with names of variables
 * for creation batches to calculating.
 * Using for transfer data from calculator to outputHandler
 *
 * @param expression         normalized expression
 * @param calculationResults array with results of calculating
 * @param variablesValues    array with collections of variables values
 * @param variablesSlots     describes the association between the variables names
 *                           and collections positions in the variables values array.
 */
public record ResultsDTO(String expression, double[] calculationResults,
                         MutableDoubleList[] variablesValues,
                         MutableCharIntMap variablesSlots) {
}
