package com.shpp.p2p.cs.ohololobov.assignment10.token;

import org.eclipse.collections.api.list.primitive.MutableDoubleList;

import java.util.List;
import java.util.Objects;

public record Variable(char value, int slot) implements Operand {
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
        return nextIndex;
    }

    @Override
    public void executeAction(MutableDoubleList stack, double[] variablesContext) {
        stack.add(variablesContext[this.slot]);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
