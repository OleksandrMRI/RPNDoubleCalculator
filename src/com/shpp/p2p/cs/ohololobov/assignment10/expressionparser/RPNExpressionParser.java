package com.shpp.p2p.cs.ohololobov.assignment10.expressionparser;

import com.shpp.p2p.cs.ohololobov.assignment10.service.Validator;
import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.CLOSING_BRACKET;
import static com.shpp.p2p.cs.ohololobov.assignment10.token.Bracket.OPENING_BRACKET;
import static com.shpp.p2p.cs.ohololobov.assignment10.token.Operator.MULTIPLICATION;
import static com.shpp.p2p.cs.ohololobov.assignment10.token.Operator.POW;

/**
 * The class contains logic of positioning tokens in the list in RPN order
 */
public class RPNExpressionParser {
    /**
     * logger instance
     */
    public static final Logger log = LoggerFactory.getLogger(RPNExpressionParser.class);
    /**
     * instance of class RPNExpressionParser
     */
    private static RPNExpressionParser instance;

    /**
     * singleton for creating instance of class RPNExpressionParser as Singleton
     *
     * @return instance of class RPNExpressionParser
     */
    public static RPNExpressionParser getInstance() {
        if (instance == null) {
            instance = new RPNExpressionParser();
        }
        return instance;
    }

    /**
     * The method pars list of tokens in postfix notation to list in RPN
     *
     * @param postfixTokens list of tokens in postfix notation
     * @return list of tokens in RPN order
     */
    public List<RPNToken> parse(List<Token> postfixTokens) {
        List<RPNToken> tokensInRPNNotation = new ArrayList<>();
        Deque<Token> stackBuffer = new ArrayDeque<>();
        Token currentToken;
        int currentRank;
        int previousTokenRank = Integer.MIN_VALUE;
        for (int i = 0; i < postfixTokens.size(); i++) {
            currentToken = postfixTokens.get(i);
            currentRank = currentToken.rank();
            if (currentRank == UnaryMinus.getRank()) {
                tokensInRPNNotation.add(new Decimal(UnaryMinus.getMultiplicator()));
                stackBuffer.offerLast(MULTIPLICATION);
                previousTokenRank = MULTIPLICATION.rank();
                log.debug("tokensInRPNNotation -1 unary minus: {}", tokensInRPNNotation);
            } else if (currentToken instanceof Operand operand) {
                tokensInRPNNotation.add(operand);
                log.debug("tokensInRPNNotation operand: {}", tokensInRPNNotation);
            } else if (currentRank == CLOSING_BRACKET.rank()) {
                previousTokenRank = transferTokens(tokensInRPNNotation, stackBuffer, currentToken, previousTokenRank);
                log.debug("tokensInRPNNotation after bracket transfer: {}", tokensInRPNNotation);
            } else if (currentRank > previousTokenRank
                    || currentToken == POW
                    || previousTokenRank == OPENING_BRACKET.rank()) {
                stackBuffer.offerLast(currentToken);
                previousTokenRank = currentRank;
                log.debug("tokensInRPNNotation after major operator, pow or opening bracket: {}", tokensInRPNNotation);
            } else {
                log.debug("tokensInRPNNotation after minor operator: {}", tokensInRPNNotation);
                previousTokenRank = transferTokens(tokensInRPNNotation, stackBuffer, currentToken, previousTokenRank);
            }
            log.debug("stack : {}", stackBuffer);
        }
        while (!stackBuffer.isEmpty()) {
            transferToken(stackBuffer, tokensInRPNNotation);
        }

        return tokensInRPNNotation;
    }

    /**
     * The method transfer tokens from stackBuffer to list of RPNTokens in RPN order
     *
     * @param tokensInRPNNotation list of RPNTokens in RPN order
     * @param stackBuffer         supporting buffer of operation and brackets for parsing in RPN
     * @param currentToken        token to pars
     * @param previousTokenRank   priority rank of operation in mathematical notation
     * @return rank of currentToken
     */
    private int transferTokens(List<RPNToken> tokensInRPNNotation, Deque<Token> stackBuffer, Token currentToken, int previousTokenRank) {
        int currentRank = currentToken.rank();
        log.info("currentToken: {}", currentToken);
        if (currentToken instanceof RPNToken rpnToken) {
            transferIfMathematicalOperation(tokensInRPNNotation, stackBuffer, rpnToken, currentRank);

        } else if (currentRank == Bracket.CLOSING_BRACKET.rank()) {
            previousTokenRank = transferIfBrackets(tokensInRPNNotation, stackBuffer, previousTokenRank);
            Validator.isValidBracketsNumber(previousTokenRank);
            log.info("Removing token: {}", stackBuffer.peekLast());
            stackBuffer.removeLast();
            currentRank = transferFunction(tokensInRPNNotation, stackBuffer, currentRank);
        }

        return currentRank;
    }

    /**
     * the method transfers function token from stackBuffer to list of RPNTokens in RPN order,
     * if function token stays for opening bracket, else return it rank of previous for opening bracket token
     * or Integer.MIN_VALUE if stackBuffer is empty
     *
     * @param tokensInRPNNotation list of RPNTokens in RPN order
     * @param stackBuffer         supporting buffer of operation and brackets for parsing in RPN
     * @param currentRank         rank of token to pars
     * @return rank of previous token or Integer.MIN_VALUE
     */
    private static int transferFunction(List<RPNToken> tokensInRPNNotation, Deque<Token> stackBuffer, int currentRank) {
        if (!stackBuffer.isEmpty() && stackBuffer.getLast().rank() == Function.getRank()) {
            transferToken(stackBuffer, tokensInRPNNotation);
        } else if (!stackBuffer.isEmpty()) {
            currentRank = stackBuffer.getLast().rank();
        } else {
            currentRank = Integer.MIN_VALUE;
        }
        log.debug("tokensInRPNNotation : {}", tokensInRPNNotation);
        return currentRank;
    }

    /**
     * Method includes logic of transfer tokens from stackBuffer to list of RPNTokens in RPN order.
     * The parsing process continues until the opening parenthesis is the last token remaining on the stack.
     * Neither the opening nor the closing brackets is transferred to the list of RPN tokens.
     * Afterward, the method checks whether a function preceded the opening parenthesis;
     * if so, that function is also transferred to the list of RPN tokens.
     *
     * @param tokensInRPNNotation list of RPNTokens in RPN order
     * @param stackBuffer         supporting buffer of operation and brackets for parsing in RPN
     * @param previousTokenRank   rank of last token in stackBuffer
     * @return rank of last remaining token in stackBuffer
     */
    private static int transferIfBrackets(List<RPNToken> tokensInRPNNotation, Deque<Token> stackBuffer, int previousTokenRank) {
        log.debug("stack: {}", stackBuffer);
        while (previousTokenRank < OPENING_BRACKET.rank()) {
            log.info("previousToken: {}", stackBuffer.peekLast());
            transferToken(stackBuffer, tokensInRPNNotation);
            if (!stackBuffer.isEmpty()) {
                previousTokenRank = stackBuffer.peekLast().rank();
            } else {
                previousTokenRank = Integer.MAX_VALUE;
            }
            log.debug("tokensInRPNNotation: {}", tokensInRPNNotation);
        }

        return previousTokenRank;
    }

    /**
     * Method includes logic of transfer tokens from stackBuffer to list of RPNTokens in RPN order.
     * The transfer continues as long as the rank of the current token is less than
     * or equal to the rank of the last token in the stack.
     *
     * @param tokensInRPNNotation list of RPNTokens in RPN order
     * @param stackBuffer         supporting buffer of operation and brackets for parsing in RPN
     * @param currentToken        token to pars
     * @param currentRank         priority rank of current token
     */
    private static void transferIfMathematicalOperation(List<RPNToken> tokensInRPNNotation, Deque<Token> stackBuffer, RPNToken currentToken, int currentRank) {
        log.debug("stack: {}", stackBuffer);
        transferToken(stackBuffer, tokensInRPNNotation);
        while (!stackBuffer.isEmpty() && stackBuffer.peekLast().rank() >= currentRank) {
            transferToken(stackBuffer, tokensInRPNNotation);
            log.debug("tokensInRPNNotation  {}", tokensInRPNNotation);
        }
        stackBuffer.add(currentToken);
    }

    /**
     * the method transfer tokens from stackBuffer to list of tokens in RPN order
     * during transfer is cast Token to RPNToken.
     *
     * @param stackBuffer         supporting buffer of operation and brackets for parsing in RPN
     * @param tokensInRPNNotation list of RPNTokens in RPN order
     */
    private static void transferToken(Deque<Token> stackBuffer, List<RPNToken> tokensInRPNNotation) {
        if (stackBuffer.removeLast() instanceof RPNToken rpnToken)
            tokensInRPNNotation.add(rpnToken);
    }
}
