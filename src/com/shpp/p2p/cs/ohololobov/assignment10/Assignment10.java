package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.cli.ConsoleOutputHandler;
import com.shpp.p2p.cs.ohololobov.assignment10.cli.OutputHandler;
import com.shpp.p2p.cs.ohololobov.assignment10.dto.ResultsDTO;

import java.io.IOException;

/**
 * This is main class of program that calculate result of lineal representation of mathematical expression
 * and variable values if any are present in the expression.
 * in this implementation:
 * Program pars expression to reverse Polish notation (RPN),
 * and form from presenting variables a collection (array of MutableDoubleLists).
 * During calculation the program converted all presenting in collections variables values in single vector
 * for batch parallel processing within a single calculating loop.
 * Program return result output vector as double[]
 */
public class Assignment10 {
    /**
     * Application entry point.Instantiates the calculator runner with raw arguments
     * trigger execution, and routs the generated results to output
     *
     * @param args command-line arguments containing line representation of expression and variable equalities
     * @throws IOException if args[] is empty
     */
    static void main(String[] args) throws IOException {
        CalculatorEngine doubleCalculator = DoubleCalculator.getInstance();
        ResultsDTO results = doubleCalculator.runCalculator(args);

        OutputHandler consoleOutputHandler = ConsoleOutputHandler.getInstance();
        consoleOutputHandler.send(results);
        System.out.println(results.calculationResults()[0]);
    }

}
