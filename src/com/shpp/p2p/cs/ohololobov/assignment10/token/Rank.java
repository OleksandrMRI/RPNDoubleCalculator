package com.shpp.p2p.cs.ohololobov.assignment10.token;

public enum Rank {
    PLUS(1),MINUS(1),
    MULTIPLICATOR(2), DIVISION(2),
    UNARY_MINUS(3),
    POW(4),
    FUNCTION(5),
    OPENING_BRACKET(0),
    CLOSING_BRACKET(6),
    OPERAND(7);

    private final int rank;

    Rank(int rank){
        this.rank = rank;
    }

    int rank(){
        return this.rank;
    }
}
