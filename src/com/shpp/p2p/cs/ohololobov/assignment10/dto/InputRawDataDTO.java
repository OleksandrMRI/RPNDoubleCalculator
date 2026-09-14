package com.shpp.p2p.cs.ohololobov.assignment10.dto;

import java.util.List;

/**
 * DTO contains result of routing of args[] from main() in route() of ArgsDispatcher
 * in two type of parameters input lineal representation of formula
 * and variables. Used for transfer this data to parsdata() of ExpressionAndVariableParserFacade
 *
 * @param rawExpression input raw lineal representation of mathematical expression
 * @param rawEqualities list of lineal representation of variable equalities,
 *                      may contain null if no args vor variable present
 */
public record InputRawDataDTO(String rawExpression, List<String> rawEqualities) {
}
