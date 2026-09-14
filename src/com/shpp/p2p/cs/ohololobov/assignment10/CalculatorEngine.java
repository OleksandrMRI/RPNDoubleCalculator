package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.ResultsDTO;

import java.io.IOException;

public interface CalculatorEngine {
    ResultsDTO runCalculator(String[] args) throws IOException;
}
