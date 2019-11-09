package vartas.chart.line;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import vartas.chart.AbstractChart;
import vartas.chart.Interval;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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

/**
 * This class implements a basic line chart. <br>
 * It is possible to create plots with both a single line and multiple lines, as long as each line has a unique label.
 * @param <S> the label of the events.
 * @param <T> the type of events that are stored in the underlying map.
 */
public abstract class
AbstractLineChart <S extends Comparable<? super S>,T> extends AbstractChart <T>{
    protected LoadingCache<OffsetDateTime, Multimap<S, T>> cache;
    protected String xAxisLabel;
    protected String yAxisLabel;
    protected Interval interval;
    protected ChronoUnit granularity = ChronoUnit.DAYS;

    protected AbstractLineChart(CacheBuilder<Object, Object> builder){
        cache = builder.concurrencyLevel(1).build(CacheLoader.from(key -> ArrayListMultimap.create()));
    }

    protected AbstractLineChart(){
        this(CacheBuilder.newBuilder());
    }

    protected AbstractLineChart(Duration lifetime){
        this(CacheBuilder.newBuilder().expireAfterWrite(lifetime.toNanos(), TimeUnit.NANOSECONDS));
    }

    public String getXAxisLabel(){
        return xAxisLabel;
    }

    public String getYAxisLabel(){
        return yAxisLabel;
    }

    /**
     * @return the current granularity of the time axis.
     */
    public Interval getInterval(){
        return interval;
    }

    public void setXAxisLabel(String xAxisLabel){
        this.xAxisLabel = xAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel){
        this.yAxisLabel = yAxisLabel;
    }

    /**
     * @param interval the new granularity of the time axis.
     */
    public void setInterval(Interval interval){
        this.interval = interval;
    }

    /**
     * @param label the label of the line the event belongs to.
     * @param instant the time the event occurred.
     * @param data the value of the event.
     */
    public void add(S label, Instant instant, T data){
        OffsetDateTime sanitized = instant.atOffset(ZoneOffset.UTC).truncatedTo(granularity);
        cache.getUnchecked(sanitized).put(label, data);
    }

    /**
     *  Overwrites the previous values with the new entries.
     * @param label the label of the line the event belongs to.
     * @param instant the time the event occurred.
     * @param data the new values of the event.
     */
    public void set(S label, Instant instant, Collection<T> data){
        OffsetDateTime sanitized = instant.atOffset(ZoneOffset.UTC).truncatedTo(granularity);
        cache.getUnchecked(sanitized).replaceValues(label, data);
    }

    /**
     * @param label the label of the line the event belongs to.
     * @param instant the time the event occurred.
     * @return all events that happened during that time.
     */
    public Collection<T> get(S label, Instant instant){
        OffsetDateTime sanitized = instant.atOffset(ZoneOffset.UTC).truncatedTo(granularity);
        return cache.getUnchecked(sanitized).get(label);
    }

    /**
     * @param label the label of the line the event belongs to.
     * @param instant the time the event occurred.
     * @param update the update function applied to all events.
     */
    public void update(S label, Instant instant, Function<T, T> update){
        Collection<T> updated = get(label, instant).stream().map(update).collect(Collectors.toList());
        set(label, instant, updated);
    }

    /**
     * The dates on the time axis will be normalized to UTC and use their English names. <br>
     * Additionally, the chart will always start from 0 on the Y axis.
     * @return the chart containing all entries that have been added up to this point.
     */
    @Override
    public JFreeChart create(){
        return create(cache.asMap().values().stream().map(Multimap::keySet).flatMap(Collection::stream).collect(Collectors.toSet()));
    }


    /**
     * The dates on the time axis will be normalized to UTC and use their English names. <br>
     * Additionally, the chart will always start from 0 on the Y axis.
     * @param labels all valid chart labels.
     * @return the chart containing all entries with the specified labels.
     */
    public JFreeChart create(Collection<S> labels){
        Preconditions.checkNotNull(getTitle());
        Preconditions.checkNotNull(getXAxisLabel());
        Preconditions.checkNotNull(getYAxisLabel());
        Preconditions.checkNotNull(getInterval());

        TimeSeriesCollection dataset = createTimeSeriesCollection(labels);

        JFreeChart chart =  ChartFactory.createTimeSeriesChart(getTitle(), getXAxisLabel(), getYAxisLabel(), dataset,true,false,false);
        //Set the default language of the months to English
        ((DateAxis)chart.getXYPlot().getDomainAxis()).setLocale(Locale.ENGLISH);
        //Normalize the time zone
        ((DateAxis)chart.getXYPlot().getDomainAxis()).setTimeZone(TimeZone.getTimeZone("UTC"));
        //Normalize the values
        chart.getXYPlot().getRangeAxis().setLowerBound(0);
        return chart;
    }
    /**
     * @param data all elements in the given interval.
     * @return the number representing those elements.
     */
    protected abstract long count(Collection<? extends T> data);

    /**
     * The granularity defaults to {@link ChronoUnit#DAYS}
     * @return the current granularity.
     */
    public ChronoUnit getGranularity(){
        return granularity;
    }

    /**
     * Overwrites the current granularity;
     * @param granularity the new granularity
     */
    public void setGranularity(ChronoUnit granularity){
        this.granularity = granularity;
    }

    private TimeSeriesCollection createTimeSeriesCollection(Collection<S> lables){
        TimeSeriesCollection dataset = new TimeSeriesCollection(TimeZone.getTimeZone("UTC"));

        cache.asMap()
             .values()
             .stream()
             .map(Multimap::keySet)
             .flatMap(Collection::stream)
             .distinct()
             .filter(lables::contains)
             .map(this::createTimeSeries)
             .forEach(dataset::addSeries);

        return dataset;
    }

    private TimeSeries createTimeSeries(S label){
        TimeSeries series = new TimeSeries(label);

        //Increase 'before' by a little so that the newest entry won't be skipped
        OffsetDateTime before = getNewestTimeStamp(label).plusMinutes(1);
        OffsetDateTime after = getOldestTimeStamp(label);

        //Add all timestamps to the series
        Iterator<OffsetDateTime> iterator = interval.getIteratorFunction().apply(before, after);
        OffsetDateTime current = after, next;
        while(iterator.hasNext()){
            next = iterator.next();
            series.add(interval.getTimePeriod(current), collect(label, next, current));
            current = next;
        }
        return series;
    }

    private long collect(S label, OffsetDateTime end, OffsetDateTime start){
        return count(accumulate(label, end, start));
    }

    private Collection<T> accumulate(S label, OffsetDateTime end, OffsetDateTime start){
        return Maps.filterValues(cache.asMap(), value -> value.containsKey(label))
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isBefore(end))
                .filter(entry -> !entry.getKey().isBefore(start))
                .map(Map.Entry::getValue)
                .map(Multimap::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private OffsetDateTime getNewestTimeStamp(S label){
        return Maps.filterValues(cache.asMap(), value -> value.containsKey(label))
                .keySet()
                .stream()
                .max(OffsetDateTime::compareTo)
                .orElseThrow(() -> new IllegalStateException("The data set for the label "+label+" is empty."));
    }

    private OffsetDateTime getOldestTimeStamp(S label){
        return Maps.filterValues(cache.asMap(), value -> value.containsKey(label))
                .keySet()
                .stream()
                .min(OffsetDateTime::compareTo)
                .orElseThrow(() -> new IllegalStateException("The data set for the label "+label+" is empty."));
    }
}
