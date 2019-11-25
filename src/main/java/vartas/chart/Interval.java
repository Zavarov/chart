package vartas.chart;

import org.jfree.data.time.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * All intervals that are supported by the chart.
 */
public enum Interval{
    /**
     * Step size of a minute.
     */
    MINUTE(LocalDateTime::plusMinutes, t -> new Minute(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of an hour.
     */
    HOUR(LocalDateTime::plusHours, t -> new Hour(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a day.
     */
    DAY(LocalDateTime::plusDays, t -> new Day(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a week.
     */
    WEEK(LocalDateTime::plusWeeks, t -> new Week(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a month.
     */
    MONTH((d,i) -> d.plusMonths(i).withDayOfMonth(1), t -> new Month(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a year.
     */
    YEAR((d,i) -> d.plusYears(i).withDayOfYear(1), t -> new Year(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
    /**
     * The step function of the interval.
     */
    private final BiFunction<LocalDateTime, Integer, LocalDateTime> step;
    /**
     * Maps the timestamp to the respective day, week, month, etc.
     */
    private final Function<Date,RegularTimePeriod> period;
    /**
     * @param start the (inclusive) minimum age of the entries.
     * @param end the (inclusive) maximum age of the entries.
     * @param stepsize the distance between two entries with respect to the interval.
     * @return all dates in the specified interval.
     */
    public Stream<LocalDateTime> getDates(LocalDateTime start, LocalDateTime end, int stepsize){
        return Stream.iterate(start, d -> !d.isAfter(end), d -> step.apply(d, stepsize));
    }
    /**
     * @param time the current time stamp.
     * @return the time period the current instance is in as used in the JFreeChart.
     */
    public RegularTimePeriod getTimePeriod(LocalDateTime time){
        return period.apply(Date.from(time.toInstant(ZoneOffset.UTC)));
    }
    /**
     * @param step the function that returns the next timestamp that follows from the current one.
     * @param period the function that creates the time periods from each date.
     */
    Interval(BiFunction<LocalDateTime, Integer, LocalDateTime> step, Function<Date,RegularTimePeriod> period){
        this.step = step;
        this.period = period;
    }
}