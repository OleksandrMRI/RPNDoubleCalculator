package com.shpp.p2p.cs.ohololobov.assignment10.cli;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * This class paint graph of formula at canvas
 */
public class GraphPainter {
    /**
     * The method receives two double[] of the same length
     * and draws graph using par of arrays with same indexes as coordinates.
     *
     * @param xData   array of x values
     * @param yData   array of y values
     * @param formula lineal representation of expression
     */
    public static void paint(double[] xData, double[] yData, String formula) {

        XYChart chart = QuickChart.getChart(
                "Mathematical expression graph",
                "X",
                "Y",
                formula,
                xData,
                yData
        );

        // Показываем окно с графиком
        new SwingWrapper<>(chart).displayChart();
    }
}

