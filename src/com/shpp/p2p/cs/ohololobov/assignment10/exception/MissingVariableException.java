package com.shpp.p2p.cs.ohololobov.assignment10.exception;

/**
 * RunTimeException throws if expression contains variable but such variable is not exist in variables Array
 */
public class MissingVariableException extends IllegalArgumentException {
    public MissingVariableException(String s) {
        super(s);
    }
}
