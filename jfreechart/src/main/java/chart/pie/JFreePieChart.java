package chart.pie;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import vartas.chart.pie.Dataset;
import vartas.chart.pie.PieChart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Map;

public class JFreePieChart extends PieChart {
    @Override
    public BufferedImage create(int width, int height) {
        Preconditions.checkNotNull(getLabel());
        DefaultPieDataset dataset = createDataSet();
        JFreeChart chart = ChartFactory.createPieChart3D(getLabel(), dataset, false, false, Locale.ENGLISH);

        PiePlot plot = (PiePlot)chart.getPlot();
        setLabel(plot);
        assignColors(plot);

        return chart.createBufferedImage(width, height);

    }

    private DefaultPieDataset createDataSet(){
        Map<String, Double> data = Maps.transformValues(getEntries(), Dataset::getCount);
        DefaultPieDataset dataset = new DefaultPieDataset();

        data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));

        return dataset;
    }

    private void setLabel(PiePlot plot){
        PieSectionLabelGenerator label = new StandardPieSectionLabelGenerator("{0} = {1} ({2})");
        plot.setLabelGenerator(label);
    }

    private void assignColors(PiePlot plot){
        forEachEntries((key, value) ->
            plot.setSectionPaint(key, getColor(key, value))
        );
    }

    private Color getColor(String label, Dataset dataset){
        if(dataset.isPresentColor()){
            return dataset.getColor().orElseThrow();
        }else{
            int hash = label.hashCode();
            return new Color(hash & 0xFFFFFF);
        }
    }
}
