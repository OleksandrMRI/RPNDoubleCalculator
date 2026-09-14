package com.shpp.p2p.cs.ohololobov.assignment10.expressionparser;

import com.shpp.p2p.cs.ohololobov.assignment10.token.Token;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;

import java.util.List;

/**
 * DTO for gets data from tokenize() of Lexer. Its data used in parse() of RPNExpressionParser and in methods
 * that works with arrays of variables values (MutableDoubleList[])
 *
 * @param tokens                list of tokens in postfix notation
 * @param variablesInExpression map, that describes connection between variables names
 *                              and positions of their values in array of variables values,
 *                              can be null, if there is no variable in expression present
 */
public record LexerContext(List<Token> tokens, MutableCharIntMap variablesInExpression) {
}
