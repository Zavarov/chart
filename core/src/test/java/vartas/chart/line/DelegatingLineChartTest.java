package vartas.chart.line;

import org.junit.Before;
import org.junit.Test;
import vartas.chart.Interval;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
public class DelegatingLineChartTest extends AbstractLineChartTest<String>{
    private DelegatingLineChart<String> chart;
    @Before
    public void setUp(){
        chart = new DelegatingLineChart<>(col -> (long)col.size());
        super.init(chart);
    }

    @Test
    public void testEvicting(){
        chart = new DelegatingLineChart<>(col -> (long)col.size(), Duration.ZERO);

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Entry", LocalDateTime.now(), "event");
        }

        assertTrue(chart.cache.asMap().isEmpty());
    }

    @Test
    public void testCreateMultipleLabels(){
        chart.setTitle("Test Line Chart (Multiple labels)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.DAY);
        chart.setGranularity(ChronoUnit.DAYS);

        LocalDateTime first = LocalDateTime.now();
        LocalDateTime second = first.minus(1, ChronoUnit.DAYS);

        chart.set("A", first, Collections.singletonList("A"));
        chart.set("B", first, Arrays.asList("B","B"));
        chart.set("C", first, Arrays.asList("C","C","C"));
        chart.set("D", first, Arrays.asList("D","D","D","D"));

        chart.set("A", second, Collections.singletonList("A"));
        chart.set("B", second, Arrays.asList("B","B"));
        chart.set("C", second, Arrays.asList("C","C","C"));
        chart.set("D", second, Arrays.asList("D","D","D","D"));

        assertNotNull(chart.create());

        save("LineChartMultipleLabels");
    }

    @Test
    public void testCreateStepsize(){
        chart.setTitle("Test Line Chart (15 Minutes)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.MINUTE);
        chart.setGranularity(ChronoUnit.MINUTES);
        chart.setStepSize(15);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Minute", start.plusMinutes(2*i*15), "event");
        }

        assertNotNull(chart.create());

        save("LineChartStepsize");
    }

    @Test
    public void testCreateMinute(){
        chart.setTitle("Test Line Chart (Minute)");
        chart.setXAxisLabel("Time (UTC)");
        chart.setYAxisLabel("Count");
        chart.setInterval(Interval.MINUTE);
        chart.setGranularity(ChronoUnit.MINUTES);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Minute", start.plusMinutes(2*i), "event");
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

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Entry", start.plusHours(2*i), "event");
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

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Day", start.plus(Period.ofDays(i*2)), "event");
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

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Week", start.plus(Period.ofWeeks(i*2)), "event");
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

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Month", start.plus(Period.ofMonths(i*2)), "event");
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

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.add("Year", start.plus(Period.ofYears(i*2)), "event");
        }

        assertNotNull(chart.create());

        save("LineChartYear");
    }
    @Test
    public void testSet(){
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        assertTrue(chart.cache.asMap().isEmpty());
        chart.add("Label", date, "add");
        assertEquals(chart.cache.asMap().get(date).get("Label"), Collections.singletonList("add"));
        chart.set("Label", date, Collections.singleton("set"));
        assertEquals(chart.cache.asMap().get(date).get("Label"), Collections.singletonList("set"));
    }
    @Test
    public void testGet(){
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        assertTrue(chart.cache.asMap().isEmpty());
        chart.add("Label", date, "add");
        assertEquals(chart.get("Label", date), Collections.singletonList("add"));
    }
    @Test
    public void testUpdate(){
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        assertTrue(chart.cache.asMap().isEmpty());
        chart.add("Label", date, "add");
        assertEquals(chart.get("Label", date), Collections.singletonList("add"));
        chart.update("Label", date, s -> s+s);
        assertEquals(chart.get("Label", date), Collections.singletonList("addadd"));
    }
}
