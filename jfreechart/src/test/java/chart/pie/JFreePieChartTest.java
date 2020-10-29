package chart.pie;

import chart.AbstractChartTest;
import org.junit.jupiter.api.Test;
import vartas.chart.pie.PieChart;
import vartas.chart.pie.factory.NumberDatasetFactory;
import vartas.chart.pie.factory.PieChartFactory;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JFreePieChartTest extends AbstractChartTest<PieChart> {
    @Test
    public void testCreate(){
        chart = PieChartFactory.create(JFreePieChart::new, "Test Pie Chart");

        String text = "How Much Wood Would A Woodchuck Chuck If A Woodchuck Could Chuck Wood ?";
        Map<String, Long> words = Stream.of(text.split(" ")).collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        words.forEach((key, value) -> chart.putEntries(key, NumberDatasetFactory.create(value.doubleValue())));

        save("PieChart");
    }
    @Test
    public void testCreateColor(){
        chart = PieChartFactory.create(JFreePieChart::new, "Test Pie Chart Color");

        String text = "How Much Wood Would A Woodchuck Chuck If A Woodchuck Could Chuck Wood ?";
        Map<String, Long> words = Stream.of(text.split(" ")).collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        words.forEach((key, value) -> chart.putEntries(key, NumberDatasetFactory.create(value.doubleValue(), Optional.of(new Color(key.hashCode())))));

        save("PieChartColor");
    }
}
