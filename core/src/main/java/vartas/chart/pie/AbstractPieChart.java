package vartas.chart.pie;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import vartas.chart.AbstractChart;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

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
    private Multimap<String, T> data = LinkedListMultimap.create();

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
     * Adds a single key-value pair to the chart.<br>
     * If the key is already used, the new element is simply appended to the existing data.
     * @param key the unique key for this value.
     * @param value the value associated with the key.
     */
    public void add(String key, T value){
        addAll(key, Collections.singleton(value));
    }

    /**
     * Adds all elements in the collection to the underlying list.
     * If the key is already used, the new elements are simply appended to the existing data.
     * @param key the unique key for the values.
     * @param values the values associated with the key.
     */
    public void addAll(String key, Collection<? extends T> values){
        this.data.putAll(key, values);
    }

    /**
     * @param key the unique key associated with the values.
     * @param values the values associated with the key.
     * @return a value associated with the key-value pair.
     */
    public abstract Long count(String key, Collection<? extends T> values);

    @Override
    public JFreeChart create(){
        Preconditions.checkNotNull(getTitle());

        Map<String, Long> data = Maps.transformEntries(this.data.asMap(), this::count);

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
