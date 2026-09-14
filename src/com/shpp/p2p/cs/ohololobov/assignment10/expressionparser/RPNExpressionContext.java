package com.shpp.p2p.cs.ohololobov.assignment10.expressionparser;

import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;

import java.util.List;

/**
 * DTO contains result of work of parsEspression() returns this data
 * to the calling method parsData() ExpressionAndVariableParserFacade for further processing
 *
 * @param normalizedExpression  linear representation of expression after normalization
 * @param rpnTokens             list of tokens in RPN
 * @param variablesInExpression map, that describes connection between variables names
 *                              and positions of their values in array of variables values,
 *                              can be null, if there is no variable in expression present
 */
public record RPNExpressionContext(String normalizedExpression, List<RPNToken> rpnTokens,
                                   org.eclipse.collections.api.map.primitive.MutableCharIntMap variablesInExpression) {
}
