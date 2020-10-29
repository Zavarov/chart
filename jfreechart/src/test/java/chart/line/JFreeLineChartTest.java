package chart.line;

import chart.AbstractChartTest;
import org.junit.jupiter.api.Test;
import vartas.chart.line.Dataset;
import vartas.chart.line.LineChart;
import vartas.chart.line.Position;
import vartas.chart.line.factory.LineChartFactory;
import vartas.chart.line.factory.NumberDatasetFactory;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Optional;

public class JFreeLineChartTest extends AbstractChartTest<LineChart> {
    @Test
    public void testCreateMultipleLabels(){
        chart = createChart("Test Line Chart (Multiple Labels)", ChronoUnit.DAYS);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i){
            chart.addEntries(createEntry(2 + i%2, start.plusDays(i), "A"));
            chart.addEntries(createEntry(4 + i%2, start.plusDays(i), "B"));
            chart.addEntries(createEntry(6 + i%2, start.plusDays(i), "C"));
            chart.addEntries(createEntry(8 + i%2, start.plusDays(i), "D"));
        }

        save("LineChartMultipleLabels");
    }

    @Test
    public void testCreateDual(){
        chart = createChart("Test Line Chart (Dual)", ChronoUnit.DAYS);
        chart.setSecondaryRangeLabel("Dual");

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(2, start.plusYears(2*i), "A", Position.LEFT));
            chart.addEntries(createEntry(1, start.plusYears(2*i + 1), "A", Position.LEFT));
            chart.addEntries(createEntry(4, start.plusYears(2*i), "B", Position.RIGHT));
            chart.addEntries(createEntry(3, start.plusYears(2*i + 1), "B", Position.RIGHT));
        }

        save("LineChartDual");
    }

    @Test
    public void testCreateMinute(){
        chart = createChart("Test Line Chart (Minute)", ChronoUnit.MINUTES);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(1, start.plusMinutes(2*i)));
            chart.addEntries(createEntry(0, start.plusMinutes(2*i + 1)));
        }

        save("LineChartMinute");
    }

    @Test
    public void testCreateHour(){
        chart = createChart("Test Line Chart (Hour)", ChronoUnit.HOURS);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(1, start.plusHours(2*i)));
            chart.addEntries(createEntry(0, start.plusHours(2*i + 1)));
        }

        save("LineChartHour");
    }
    @Test
    public void testCreateDay(){
        chart = createChart("Test Line Chart (DAY)", ChronoUnit.DAYS);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(1, start.plusDays(2*i)));
            chart.addEntries(createEntry(0, start.plusDays(2*i + 1)));
        }

        save("LineChartDay");
    }

    @Test
    public void testCreateMonth(){
        chart = createChart("Test Line Chart (Month)", ChronoUnit.MONTHS);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(1, start.plusMonths(2*i)));
            chart.addEntries(createEntry(0, start.plusMonths(2*i + 1)));
        }

        save("LineChartMonth");
    }

    @Test
    public void testCreateYear(){
        chart = createChart("Test Line Chart (Year)", ChronoUnit.YEARS);

        LocalDateTime start = LocalDateTime.now();

        for(int i = 0 ; i < 7 ; ++i) {
            chart.addEntries(createEntry(1, start.plusYears(2*i)));
            chart.addEntries(createEntry(0, start.plusYears(2*i + 1)));
        }

        save("LineChartYear");
    }

    // --------------------------------------------------------------- //
    //
    //  Helper
    //
    // --------------------------------------------------------------- //

    private LineChart createChart(String label, TemporalUnit granularity){
        return LineChartFactory.create(
                JFreeLineChart::new,
                granularity,
                new ArrayList<>(),
                "Time (UTC)",
                "Count",
                Optional.empty(),
                label
        );
    }

    private Dataset createEntry(int count, LocalDateTime date){
        return createEntry(count, date, "A");
    }

    private Dataset createEntry(int count, LocalDateTime date, String label){
        return createEntry(count, date, label, Position.LEFT);
    }

    private Dataset createEntry(double count, LocalDateTime date, String label, Position position){
        return NumberDatasetFactory.create(
                count,
                date,
                label,
                position
        );
    }
}
