package com.shpp.p2p.cs.ohololobov.assignment10.service;

import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.LexerContext;
import com.shpp.p2p.cs.ohololobov.assignment10.dto.InputRawDataDTO;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.Lexer;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.ExpressionNormalizer;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.RPNExpressionContext;
import com.shpp.p2p.cs.ohololobov.assignment10.expressionparser.RPNExpressionParser;
import com.shpp.p2p.cs.ohololobov.assignment10.token.RPNToken;
import com.shpp.p2p.cs.ohololobov.assignment10.variableparser.VariableEqualityNormalizer;
import com.shpp.p2p.cs.ohololobov.assignment10.variableparser.VariableEqualityParser;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * the class manages parsing of expression and variables
 */
public class ExpressionAndVariableParserFacade {
    private static final Logger log = LoggerFactory.getLogger(ExpressionAndVariableParserFacade.class);
    /**
     * instance of  ExpressionAndVariableParserFacade
     */
    private static ExpressionAndVariableParserFacade instance;

    private final ExpressionNormalizer expressionNormalizer;
    private final Lexer lexer;
    private final RPNExpressionParser rpnExpressionParser;
    private final VariableEqualityNormalizer variableEqualityNormalizer;
    private final VariableEqualityParser variableEqualityParser;

    /**
     * constructor of class ExpressionAndVariableParserFacade
     *
     * @param expressionNormalizer       instance of ExpressionNormalizer
     * @param lexer                      instance of Lexer
     * @param rpnExpressionParser        instance of RPNExpressionParser
     * @param variableEqualityNormalizer instance of VariableEqualityNormalizer
     * @param variableEqualityParser     instance of VariableEqualityParser
     */
    private ExpressionAndVariableParserFacade(
            ExpressionNormalizer expressionNormalizer,
            Lexer lexer,
            RPNExpressionParser rpnExpressionParser,
            VariableEqualityNormalizer variableEqualityNormalizer,
            VariableEqualityParser variableEqualityParser) {
        this.expressionNormalizer = expressionNormalizer;
        this.lexer = lexer;
        this.rpnExpressionParser = rpnExpressionParser;
        this.variableEqualityNormalizer = variableEqualityNormalizer;
        this.variableEqualityParser = variableEqualityParser;
    }

    /**
     * singleton for creating instance of class ExpressionAndVariableParserFacade as Singleton
     *
     * @return instance of class ExpressionAndVariableParserFacade
     */
    public static ExpressionAndVariableParserFacade getInstance() {
        if (instance == null) {
            instance = new ExpressionAndVariableParserFacade(
                    ExpressionNormalizer.getInstance(),
                    Lexer.getInstance(),
                    RPNExpressionParser.getInstance(),
                    VariableEqualityNormalizer.getInstance(),
                    VariableEqualityParser.getInstance()
            );
        }
        return instance;
    }

    /**
     * the method manages of parsing expression to list of RPNTokens and
     * variables values to array of MutableDoubleList of variable values, and contains logic
     * of connection of position of variable in expression with its index in array of MutableDoubleList
     *
     * @param inputRawDataDTO DTO contains result of routing of args[] from main() in route() of ArgsDispatcher
     * @return RPNContext DTO for contains result of work of parsData() ExpressionAndVariableParserFacade
     * for transfer this data to VectorEvaluator
     */
    public RPNContext parsData(InputRawDataDTO inputRawDataDTO) {
        RPNExpressionContext rpnExpressionContext = parsExpression(inputRawDataDTO.rawExpression());

        MutableCharIntMap variablesInExpression = rpnExpressionContext.variablesInExpression();
        List<String> rawVariablesEqualities = inputRawDataDTO.rawEqualities();

        MutableDoubleList[] variablesValues = null;
        Validator.validateVariablesAbsence(variablesInExpression, rawVariablesEqualities);
        if (rawVariablesEqualities != null) {
            log.debug("rawVariablesEqualities: {}", rawVariablesEqualities);
            variablesValues = parsVariables(inputRawDataDTO.rawEqualities(), variablesInExpression);
            log.debug("variablesValues: {}", variablesValues);
        }
        List<RPNToken> rpnTokens1 = rpnExpressionContext.rpnTokens();
        String normalizedExpression = rpnExpressionContext.normalizedExpression();

        return new RPNContext(rpnTokens1,
                variablesValues,
                variablesInExpression,
                normalizedExpression
        );
    }

    /**
     * method contains logic of parsing  of presenting in expression variables equalities from list of raw variables
     * to array of collections variables values according their position in expression
     *
     * @param rawVariablesEqualities list of raw variables received from args[] of main after routing in ArgsDispatcher
     * @param variablesInExpression  describes the association between the variables names
     *                               and collections positions in the variables values array.
     * @return array of MutableDoubleLists of variables values
     */
    private MutableDoubleList[] parsVariables(List<String> rawVariablesEqualities, MutableCharIntMap variablesInExpression) {
        List<String> normalizedVariablesEqualities
                = variableEqualityNormalizer.normalizeVariablesEqualities(
                rawVariablesEqualities
        );

        log.debug("normalizedVariablesEqualities: {}", normalizedVariablesEqualities);
        MutableDoubleList[] variablesValuesArray = variableEqualityParser.parse(
                normalizedVariablesEqualities,
                variablesInExpression
        );
        return variablesValuesArray;
    }

    /**
     * the method manage order of parsing of expression
     *
     * @param rowExpression row linear representation of expression from args[] main after routing in ArgsDispatcher
     * @return DTO contains result of work of parsEspression() returns this data
     * * to the calling method parsData() ExpressionAndVariableParserFacade for further processing
     */
    private RPNExpressionContext parsExpression(String rowExpression) {
        String normalizedExpression = expressionNormalizer.normalize(rowExpression);
        LexerContext lexerContext = lexer.tokenize(normalizedExpression);
        List<RPNToken> rpnTokens = rpnExpressionParser.parse(lexerContext.tokens());
        return new RPNExpressionContext(normalizedExpression, rpnTokens, lexerContext.variablesInExpression());
    }
}
