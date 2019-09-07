/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.trainer;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;

public class RealtimeChart {

    private final XYChart chart;
    private final ArrayList<ArrayList<Double>> input;
    private final SwingWrapper<XYChart> sw;

    public RealtimeChart() {
       input = new ArrayList<ArrayList<Double>>();
       input.add(new ArrayList<>());
       input.add(new ArrayList<>());
       input.get(0).add(0.0);
       input.get(1).add(0.0);
        // Create Chart
        chart = QuickChart.getChart("Gameplay", "Epochs", "Score", "Score", input.get(0), input.get(1));

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();



    }

    public void updateChart(double epoch, double score) {

        this.input.get(0).add(epoch);
        this.input.get(1).add(score);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                chart.updateXYSeries("Score", input.get(0), input.get(1), null);
                sw.repaintChart();
            }
        });
    }

}




