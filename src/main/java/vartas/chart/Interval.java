package vartas.chart;

import org.jfree.data.time.*;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Function;

/**
 * All intervals that are supported by the chart.
 */
public enum Interval{
    /**
     * Step size of a minute.
     */
    MINUTE(d -> d.plusMinutes(1), t -> new Minute(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of an hour.
     */
    HOUR(d -> d.plusHours(1), t -> new Hour(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a day.
     */
    DAY(d -> d.plusDays(1), t -> new Day(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a week.
     */
    WEEK(d -> d.plusWeeks(1), t -> new Week(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a month.
     */
    MONTH(d -> d.plusMonths(1).withDayOfMonth(1), t -> new Month(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH)),
    /**
     * Step size of a year.
     */
    YEAR(d -> d.plusYears(1).withDayOfYear(1), t -> new Year(t, TimeZone.getTimeZone("UTC"), Locale.ENGLISH));
    /**
     * The iterator over all elements in the given interval with respect to the step size.
     */
    private final OffsetDateTimeIteratorFunction iteratorFunction;
    /**
     * Maps the timestamp to the respective day, week, month or year.
     */
    private final Function<Date,RegularTimePeriod> period;
    /**
     * @return the function that creates the iterator.
     */
    public OffsetDateTimeIteratorFunction getIteratorFunction(){
        return iteratorFunction;
    }
    /**
     * @param time the current time stamp.
     * @return the time period the current instance is in as used in the JFreeChart.
     */
    public RegularTimePeriod getTimePeriod(OffsetDateTime time){
        return period.apply(Date.from(time.toInstant()));
    }
    /**
     * @param next the function that returns the next timestamp that follows from the current one.
     * @param period the function that creates the time periods from each date.
     */
    Interval(Function<OffsetDateTime,OffsetDateTime> next, Function<Date,RegularTimePeriod> period){
        this.iteratorFunction = new OffsetDateTimeIteratorFunction(next);
        this.period = period;
    }
}