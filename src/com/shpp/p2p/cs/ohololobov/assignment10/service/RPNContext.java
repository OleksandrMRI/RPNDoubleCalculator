package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;

import java.util.List;

/**
 * DTO for contains result of work of parsData() ExpressionAndVariableParserFacade
 * for transfer this data to VectorEvaluator
 *
 * @param rpnTokensList         list of tokens in RPN
 * @param variablesValues       array of collections with variables values
 * @param variablesInExpression map, that describes connection between variables names
 *                              and positions of their values in array of variables values,
 *                              can be null, if there is no variable in expression present
 * @param normalizedExpression  linear representation of expression after normalization
 */
public record RPNContext(List<RPNToken> rpnTokensList, MutableDoubleList[] variablesValues,
                         org.eclipse.collections.api.map.primitive.MutableCharIntMap variablesInExpression,
                         String normalizedExpression) {
}
