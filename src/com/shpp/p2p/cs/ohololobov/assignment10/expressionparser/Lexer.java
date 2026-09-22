package com.shpp.p2p.cs.ohololobov.assignment10.expressionparser;

import com.shpp.p2p.cs.ohololobov.assignment10.service.Validator;
import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;
import org.eclipse.collections.impl.map.mutable.primitive.CharIntHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.CLOSING_BRACKET;
import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.OPENING_BRACKET;
import static com.shpp.p2p.cs.ohololobov.assignment10.token.Operator.*;

/**
 * This class contain a logic of parsing of normalized linear representation
 * of mathematical expression tao postfix notation
 */
public class Lexer {
    public static final String UNEXPECTED_TOKEN_IN_EXPRESSION_MSG = "Unexpected token in expression \"%s\" at position %s";
    /**
     * instance of Lexer
     */
    private static Lexer instance;

    private Lexer() {
    }

    /**
     * instance of logger
     */
    static Logger log = LoggerFactory.getLogger(Lexer.class);

    /**
     * singleton for creating instance of class Lexer as Singleton
     *
     * @return instance of class Lexer
     */
    public static Lexer getInstance() {
        if (instance == null) {
            instance = new Lexer();
        }
        return instance;
    }

    /**
     * The method pars linear representation of expression to List of tokens in postfix notation
     * and form map of connection of variables names from expression with their positions
     * in arrays of variables values.
     *
     * @param expressionToPars linear representation of expression
     * @return list of tokens in postfix notation order
     */
    public LexerContext tokenize(String expressionToPars) {
        Validator.validateStartOfExpression(expressionToPars);
        Validator.validateEndOfExpression(expressionToPars);
        List<Token> tokensListInInfixNotation = new ArrayList<>();
        MutableCharIntMap variablesInExpression = null;
        int currentIndex = 0;
        char currentChar;
        int bracketCounter = 0;
        boolean isCheckingAfterParsing = false;
        int slotIndex = 0;
        while (currentIndex < expressionToPars.length()) {
            currentChar = expressionToPars.charAt(currentIndex);
            int nextIndex = currentIndex + 1;
            log.debug("currentChar: {}", currentChar);
            if (Character.isDigit(currentChar)) {
                log.debug("currentChar in decimal: {}", currentChar);
                log.debug("currentIndex in decimal: {}", currentIndex);
                currentIndex = Decimal.addToken(expressionToPars, currentIndex, currentChar, tokensListInInfixNotation);
            } else if (Character.isLetter(currentChar)) {
                if (nextIndex == expressionToPars.length() || !Character.isLetter(expressionToPars.charAt(nextIndex))) {
                    log.info("variable = currentChar: {}", currentChar);
                    if (variablesInExpression == null) {
                        variablesInExpression = new CharIntHashMap();
                    }
                    boolean isVariableAdded = variablesInExpression.containsKey(currentChar);
                    if (!isVariableAdded && !variablesInExpression.isEmpty()) slotIndex++;
                    if (!isVariableAdded) {
                        variablesInExpression.put(currentChar, slotIndex);
                    }
                    log.info("variableInExpression: {}", variablesInExpression);
                    slotIndex = variablesInExpression.get(currentChar);
                    currentIndex = Variable.addToken(expressionToPars, currentIndex, currentChar, tokensListInInfixNotation, slotIndex);
                    log.info("slotIndex in Lexer: {}", slotIndex);
                } else {
                    currentIndex = Function.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                }
            } else {
                switch (currentChar) {
                    case '+' ->
                            currentIndex = PLUS.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    case '-' -> {
                        if (Operator.isUnaryMinus(expressionToPars, currentIndex)) {
                            currentIndex = UNARY_MINUS.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                        } else {
                            currentIndex = SUBTRACTION.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                        }
                    }
                    case '/' ->
                            currentIndex = DIVISION.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    case '*' ->
                            currentIndex = MULTIPLICATION.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    case '^' ->
                            currentIndex = POW.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    case '(' -> {
                        bracketCounter++;
                        currentIndex = OPENING_BRACKET.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    }
                    case ')' -> {
                        bracketCounter--;
                        currentIndex = CLOSING_BRACKET.addToken(expressionToPars, currentIndex, tokensListInInfixNotation);
                    }
                    default -> {

                        throwUnexpectedTokenException(expressionToPars, currentIndex);
                    }
                }
            }

            log.debug("tokensListInInfixNotation: {}", tokensListInInfixNotation);
            log.debug("currentIndex: {}", currentIndex);
            log.info("bracketCounter inside: {}", bracketCounter);
            Validator.validateBrackets(bracketCounter, isCheckingAfterParsing);
        }
        log.debug("variableInExpression: {}", variablesInExpression);
        isCheckingAfterParsing = true;
        log.info("bracketCounter outside: {}", bracketCounter);
        Validator.validateBrackets(bracketCounter, isCheckingAfterParsing);
        log.debug("tokensListInInfixNotation: {}", tokensListInInfixNotation);
        return new LexerContext(tokensListInInfixNotation, variablesInExpression);
    }

    /**
     * The method throws IllegalArgumentException exception about unexpected token
     *
     * @param expressionToPars linear representation of expression
     * @param currentIndex     position of first char as number
     */
    private static void throwUnexpectedTokenException(String expressionToPars, int currentIndex) {
        throw new IllegalArgumentException(String.format(UNEXPECTED_TOKEN_IN_EXPRESSION_MSG, expressionToPars, currentIndex));
    }
}
