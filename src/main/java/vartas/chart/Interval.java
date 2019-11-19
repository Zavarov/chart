package vartas.chart;

import org.jfree.data.time.*;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * All intervals that are supported by the chart.
 */
public enum Interval{
    /**
     * Step size of a minute.
     */
    MINUTE(OffsetDateTime::plusMinutes, t -> new Minute(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of an hour.
     */
    HOUR(OffsetDateTime::plusHours, t -> new Hour(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a day.
     */
    DAY(OffsetDateTime::plusDays, t -> new Day(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a week.
     */
    WEEK(OffsetDateTime::plusWeeks, t -> new Week(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
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
    private final BiFunction<OffsetDateTime, Integer, OffsetDateTime> step;
    /**
     * Maps the timestamp to the respective day, week, month, etc.
     */
    private final Function<Date,RegularTimePeriod> period;
    /**
     * @return the function that creates the iterator.
     */
    public OffsetDateTimeIteratorFunction getIteratorFunction(int stepsize){
        return new OffsetDateTimeIteratorFunction(u -> step.apply(u, stepsize));
    }
    /**
     * @param time the current time stamp.
     * @return the time period the current instance is in as used in the JFreeChart.
     */
    public RegularTimePeriod getTimePeriod(OffsetDateTime time){
        return period.apply(Date.from(time.toInstant()));
    }
    /**
     * @param step the function that returns the next timestamp that follows from the current one.
     * @param period the function that creates the time periods from each date.
     */
    Interval(BiFunction<OffsetDateTime, Integer,OffsetDateTime> step, Function<Date,RegularTimePeriod> period){
        this.step = step;
        this.period = period;
    }
}