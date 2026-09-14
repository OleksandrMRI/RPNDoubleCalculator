package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.service.VectorEvaluator;
import com.shpp.p2p.cs.ohololobov.assignment10.token.*;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VectorEvaluatorTest {

    @ParameterizedTest
    @MethodSource("preparingValuesForCalculateWithoutFunctionTest")
    void calculateWithoutFunctionTest(double[] expected, List<RPNToken> rpnTokensList, MutableDoubleList[] variablesValuesDataBase) {
        VectorEvaluator vectorEvaluator = VectorEvaluator.getInstance();

        double[] results = vectorEvaluator.evaluate(rpnTokensList, variablesValuesDataBase);

        assertEquals(Arrays.toString(expected), Arrays.toString(results));
    }

    @ParameterizedTest
    @MethodSource("preparingValuesForCalculateWithFunctionTest")
    void calculateWithFunctionTest(
            double[] expected,
            List<RPNToken> rpnTokensList,
            MutableDoubleList[] variablesValuesDataBase) {

        VectorEvaluator vectorEvaluator = VectorEvaluator.getInstance();

        double[] results = vectorEvaluator.evaluate(rpnTokensList, variablesValuesDataBase);
        for (int i = 0; i < results.length; i++) {
            results[i] = Math.round(results[i] * 10) / 10.0;
        }
        assertEquals(Arrays.toString(expected), Arrays.toString(results));
    }

    private static Stream<Arguments> preparingValuesForCalculateWithoutFunctionTest() {
        return Stream.of(
                Arguments.of(
                        new double[]{5.0, 20},
                        getRpnTokensList(
                                new RPNToken[]{
                                        new Variable('a', 0),
                                        new Variable('a', 0),
                                        new Decimal(5),
                                        new Decimal(4),
                                        Operator.MULTIPLICATION,
                                        Operator.DIVISION,
                                        Operator.MULTIPLICATION
                                }),
                        variablesValues(
                                new double[]{
                                        10, 20
                                },
                                1)
                ),
                Arguments.of(
                        new double[]{25, 4},
                        getRpnTokensList(
                                new RPNToken[]{
                                        new Decimal(100),
                                        new Variable('a', 0),
                                        new Variable('b', 1),
                                        Operator.MULTIPLICATION,
                                        Operator.DIVISION
                                }),
                        variablesValues(
                                new double[]{
                                        2, 5, 2, 5
                                },
                                2)

                )
        );
    }

    private static Stream<Arguments> preparingValuesForCalculateWithFunctionTest() {
        return Stream.of(

                Arguments.of(
                        new double[]{10},
                        getRpnTokensList(
                                new RPNToken[]{
                                        new Decimal(10),
                                        new Variable('b', 0),
                                        Function.TAN,
                                        Operator.MULTIPLICATION
                                }),
                        variablesValues(
                                new double[]{
                                        Math.toRadians(45)
                                },
                                1
                        )),
                Arguments.of(
                        new double[]{0.5, 0.5},
                        getRpnTokensList(
                                new RPNToken[]{
                                        new Variable('a', 0),
                                        new Variable('b', 1),
                                        Operator.PLUS,
                                        Function.SIN
                                }),
                        variablesValues(
                                new double[]{
                                        Math.toRadians(15),
                                        Math.toRadians(20),
                                        Math.toRadians(15),
                                        Math.toRadians(10),
                                },
                                2)

                )
        );
    }

    private static Object variablesValues(double[] variablesValues, int numberOfVariables) {
        MutableDoubleList[] variablesValuesArray = new DoubleArrayList[numberOfVariables];
        int variableValuesListSize = variablesValues.length / numberOfVariables;
        for (int i = 0; i < numberOfVariables; i++) {
            variablesValuesArray[i] = new DoubleArrayList();
            for (int j = 0; j < variableValuesListSize; j++) {
                variablesValuesArray[i].add(variablesValues[i * variableValuesListSize + j]);
            }
        }
        return variablesValuesArray;
    }


    static List<RPNToken> getRpnTokensList(RPNToken[] tokens) {

        return Arrays.asList(tokens);
    }
}