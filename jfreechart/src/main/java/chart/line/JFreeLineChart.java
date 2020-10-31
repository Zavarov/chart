package chart.line;

import com.google.common.base.Preconditions;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import vartas.chart.line.Dataset;
import vartas.chart.line.LineChart;
import vartas.chart.line.Position;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JFreeLineChart extends LineChart {
    private static final Map<TemporalUnit, Function<LocalDateTime, RegularTimePeriod>> TIME = new HashMap<>();
    private static final Map<TemporalUnit, Function<LocalDateTime, LocalDateTime>> TRUNCATE = new HashMap<>();

    static{
        TIME.put(ChronoUnit.MINUTES, date -> new Minute(toDate(date), TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
        TIME.put(ChronoUnit.HOURS, date -> new Hour(toDate(date), TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
        TIME.put(ChronoUnit.DAYS, date -> new Day(toDate(date), TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
        TIME.put(ChronoUnit.MONTHS, date -> new Month(toDate(date), TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
        TIME.put(ChronoUnit.YEARS, date -> new Year(toDate(date), TimeZone.getTimeZone("UTC"), Locale.ENGLISH));

        TRUNCATE.put(ChronoUnit.MINUTES, date -> date.truncatedTo(ChronoUnit.MINUTES));
        TRUNCATE.put(ChronoUnit.HOURS, date -> date.truncatedTo(ChronoUnit.HOURS));
        TRUNCATE.put(ChronoUnit.DAYS, date -> date.truncatedTo(ChronoUnit.DAYS));
        TRUNCATE.put(ChronoUnit.MONTHS, date -> date.toLocalDate().withDayOfMonth(1).atStartOfDay());
        TRUNCATE.put(ChronoUnit.YEARS, date -> date.toLocalDate().withDayOfYear(1).atStartOfDay());
    }

    private static Date toDate(LocalDateTime date){
        return Date.from(date.toInstant(ZoneOffset.UTC));
    }

    @Override
    public BufferedImage create(int width, int height) {
        Preconditions.checkArgument(TIME.containsKey(getGranularity()));
        Preconditions.checkArgument(TRUNCATE.containsKey(getGranularity()));

        JFreeChart chart =  ChartFactory.createTimeSeriesChart(getLabel(), getDomainLabel(), getRangeLabel(), new TimeSeriesCollection(),true,false,false);

        buildDomainAxis(chart.getXYPlot());
        buildLeftRangeAxis(chart.getXYPlot());
        buildRightRangeAxis(chart.getXYPlot());
        buildRenderer(chart.getXYPlot());

        return chart.createBufferedImage(width, height);
    }

    private void buildLeftRangeAxis(XYPlot plot){
        TimeSeriesCollection data = createTimeSeriesCollection(Position.LEFT);
        NumberAxis axis = new NumberAxis(getRangeLabel());
        axis.setAutoRangeIncludesZero(true);

        plot.setRangeAxis(0, axis, false);
        plot.setDataset(0, data);
        plot.mapDatasetToRangeAxis(0, 0);
    }

    private void buildRightRangeAxis(XYPlot plot){
        if(isEmptySecondaryRangeLabel())
            return;

        TimeSeriesCollection data = createTimeSeriesCollection(Position.RIGHT);
        NumberAxis axis = new NumberAxis(getSecondaryRangeLabel().orElseThrow());
        axis.setAutoRangeIncludesZero(true);

        plot.setRangeAxis(1, axis, false);
        plot.setDataset(1, data);
        plot.mapDatasetToRangeAxis(1, 1);
    }

    private void buildRenderer(XYPlot plot){

        for(int i = 0 ; i < plot.getDatasetCount() ; ++i){
            XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
            plot.setRenderer(i, renderer);
        }

    }

    private void buildDomainAxis(XYPlot plot){
        DateAxis axis = new DateAxis(getDomainLabel());

        //Set the default language of the months to English
        axis.setLocale(Locale.ENGLISH);
        //Normalize the time zone instead of using the local time zone
        axis.setTimeZone(TimeZone.getTimeZone("UTC"));

        plot.setDomainAxis(axis);
    }

    private TimeSeriesCollection createTimeSeriesCollection(Position position){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        Map<String, List<Dataset>> data = getEntries().stream().filter(entry -> entry.getPosition().equals(position)).collect(Collectors.groupingBy(Dataset::getLabel));
        //Sort the entries by label to map a unique color to each label
        data.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> dataset.addSeries(createTimeSeries(entry.getKey(), entry.getValue())));

        return dataset;
    }

    private TimeSeries createTimeSeries(String label, Collection<Dataset> data){
        TimeSeries series = new TimeSeries(label);

        //Count the elements for each local date time.
        //In order to accumulate the data, the creation date is truncated with respect to the granularity
        Map<LocalDateTime, Double> entries = new HashMap<>();
        for(Dataset entry : data) {
            entries.merge(TRUNCATE.get(getGranularity()).apply(entry.getCreated()), entry.getCount().doubleValue(), Double::sum);
        }

        //Fill the time series with the accumulated data
        entries.forEach((key, value) ->
            series.add(TIME.get(getGranularity()).apply(key), value)
        );

        return series;
    }
}
