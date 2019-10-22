package vartas.chart.line;

import org.junit.Before;
import org.junit.Test;
import vartas.chart.Interval;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertNotNull;

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
}
