package com.shpp.p2p.cs.ohololobov.assignment10.token;

import org.eclipse.collections.api.list.primitive.MutableDoubleList;

/**
 * this  interface extends contract of interface Token for Operator, Operand and Function
 * with contract of executing action during calculating of expression. Base interface for calculate expression
 */
public sealed interface RPNToken extends Token permits Function, Operand, Operator {

    void executeAction(MutableDoubleList stack, double[] variable);
}
