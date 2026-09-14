package com.shpp.p2p.cs.ohololobov.assignment10.exception;

/**
 * RunTimeException throws if missing operand during RPN parsing is detected
 */
public class MissingOperandException extends IllegalArgumentException {
    public MissingOperandException(String message) {
        super(message);
    }
}
