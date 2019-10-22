package vartas.chart.pie;

import com.google.common.base.Preconditions;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import vartas.chart.AbstractChart;

import java.awt.*;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/*
 * Copyright (C) 2019 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public abstract class AbstractPieChart <T> extends AbstractChart <T> {
    private double alpha = 0.0;
    private Collection<T> dataCollection = new ArrayList<>();

    /**
     * Overwrites the current alpha value. <br>
     * The value should be between 0 and 1, where 0 represents transparent and 1 opaque.
     * @param alpha the new alpha value of the plot.
     */
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }

    /**
     * @return the current alpha value of the plot.
     */
    public double getAlpha(){
        return alpha;
    }

    /**
     * Adds a single element to the underlying list.
     * @param data the new element.
     */
    public void add(T data){
        dataCollection.add(data);
    }

    /**
     * Adds all elements in the collection to the underlying list.
     * @param dataCollection all elements that are added to the underlying list.
     */
    public void addAll(Collection<? extends T> dataCollection){
        this.dataCollection.addAll(dataCollection);
    }

    /**
     * @param data all elements that have been added up to this point.
     * @return a map containing all distinct entries in the data set and how often they appeared.
     */
    public abstract Map<String,Long> count(Collection<? extends T> data);

    @Override
    public JFreeChart create(){
        Preconditions.checkNotNull(getTitle());

        Map<String, Long> data = count(dataCollection);

        DefaultPieDataset dataset = createDataSet(data);

        JFreeChart chart = ChartFactory.createPieChart3D(getTitle(), dataset, false, false, Locale.ENGLISH);

        PiePlot plot = (PiePlot)chart.getPlot();

        setPlotLabel(plot);
        assignColorToPlotLabel(data, plot);

        return chart;
    }

    private void setPlotLabel(PiePlot plot){
        PieSectionLabelGenerator label = new StandardPieSectionLabelGenerator("{0} = {1} ({2})");
        plot.setLabelGenerator(label);
    }

    private void assignColorToPlotLabel(Map<String, Long> data, PiePlot plot){
        data.keySet().forEach(key -> {
            int hash = key.hashCode();
            int r = hash & 0xFF;
            int g = (hash>>8) & 0xFF;
            int b = (hash>>16) & 0xFF;
            plot.setSectionPaint(key, new Color(r,g,b,(int)(alpha*255)));
        });
    }

    private DefaultPieDataset createDataSet(Map<String, Long> data){
        DefaultPieDataset dataset = new DefaultPieDataset();

        data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));

        return dataset;
    }
}
