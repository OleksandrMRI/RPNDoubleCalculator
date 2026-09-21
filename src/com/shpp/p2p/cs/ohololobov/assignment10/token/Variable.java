package com.shpp.p2p.cs.ohololobov.assignment10.token;

import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record Variable(char value, int slot) implements Operand {
    private static final Logger log = LoggerFactory.getLogger(Variable.class);

    /**
     * The method adds variable in list of tokens in postfix notation
     *
     * @param expressionToPars            mathematical expression
     * @param currentIndex                index of current char in expression
     * @param currentChar                 current char
     * @param tokensListInPostfixNotation list of tokens in postfix notation
     * @return index of next char
     */
    public static int addToken(String expressionToPars, int currentIndex, char currentChar, List<Token> tokensListInPostfixNotation, int slotIndex) {
        int nextIndex = ++currentIndex;
        if (nextIndex < expressionToPars.length())
            Operand.validateNextChar(expressionToPars, nextIndex);
        tokensListInPostfixNotation.add(new Variable(currentChar, slotIndex));
        log.info("slotIndex in Variable: {}", slotIndex);
        return nextIndex;
    }

    @Override
    public void executeAction(MutableDoubleList stack, double[] variablesContext) {
        stack.add(variablesContext[this.slot]);
    }
}
