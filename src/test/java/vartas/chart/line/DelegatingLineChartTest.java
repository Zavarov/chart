package vartas.chart.line;

import org.junit.Before;
import org.junit.Test;
import vartas.chart.Interval;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.Assert.*;

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
public class DelegatingLineChartTest extends AbstractLineChartTest<String, String>{
    private DelegatingLineChart<String, String> chart;
    @Before
    public void setUp(){
        chart = new DelegatingLineChart<>(col -> (long)col.size());
        super.init(chart);
    }

    @Test
    public void testEvicting(){
        chart = new DelegatingLineChart<>(col -> (long)col.size(), Duration.ZERO);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Entry", Instant.now(), "event");
        }

        assertTrue(chart.cache.asMap().isEmpty());
    }

    @Test
    public void testCreateMinute(){
        chart.setTitle("Test Line Chart (Minute)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.MINUTE);
        chart.setGranularity(ChronoUnit.MINUTES);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Minute", start.plusMinutes(2*i).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartMinute");
    }

    @Test
    public void testCreateHour(){
        chart.setTitle("Test Line Chart (Hour)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.HOUR);
        chart.setGranularity(ChronoUnit.HOURS);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Entry", start.plusHours(2*i).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartHour");
    }
    @Test
    public void testCreateDay(){
        chart.setTitle("Test Line Chart (Day)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.DAY);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Day", start.plus(Period.ofDays(i*2)).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartDay");
    }

    @Test
    public void testCreateWeek(){
        chart.setTitle("Test Line Chart (Week)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.WEEK);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Week", start.plus(Period.ofWeeks(i*2)).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartWeek");
    }

    @Test
    public void testCreateMonth(){
        chart.setTitle("Test Line Chart (Month)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.MONTH);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Month", start.plus(Period.ofMonths(i*2)).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartMonth");
    }

    @Test
    public void testCreateYear(){
        chart.setTitle("Test Line Chart (Year)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.YEAR);

        OffsetDateTime start = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Year", start.plus(Period.ofYears(i*2)).toInstant(), "event");
        }

        assertNotNull(chart.create());

        save("LineChartYear");
    }
    @Test
    public void testSet(){
        OffsetDateTime date = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        assertTrue(chart.cache.asMap().isEmpty());
        chart.add("Label", date.toInstant(), "add");
        assertEquals(chart.cache.asMap().get(date).get("Label"), Collections.singletonList("add"));
        chart.set("Label", date.toInstant(), Collections.singleton("set"));
        assertEquals(chart.cache.asMap().get(date).get("Label"), Collections.singletonList("set"));
    }
    @Test
    public void testGet(){
        OffsetDateTime date = Instant.now().atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        assertTrue(chart.cache.asMap().isEmpty());
        chart.add("Label", date.toInstant(), "add");
        assertEquals(chart.get("Label", date.toInstant()), Collections.singletonList("add"));
    }
}
