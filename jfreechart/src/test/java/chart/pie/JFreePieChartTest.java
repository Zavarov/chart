package chart.pie;

import chart.AbstractChartTest;
import org.junit.jupiter.api.Test;
import vartas.chart.pie.$factory.PieChartFactory;
import vartas.chart.pie.PieChart;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JFreePieChartTest extends AbstractChartTest<PieChart> {
    @Test
    public void testCreate(){
        chart = PieChartFactory.create(JFreePieChart::new, "Test Pie Chart");

        String text = "How Much Wood Would A Woodchuck Chuck If A Woodchuck Could Chuck Wood ?";
        Map<String, Long> words = Stream.of(text.split(" ")).collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        words.forEach((key, value) -> chart.putEntries(key, value));

        save("PieChart");
    }
    @Test
    public void testCreateColor(){
        chart = PieChartFactory.create(JFreePieChart::new, "Test Pie Chart Color");

        String text = "How Much Wood Would A Woodchuck Chuck If A Woodchuck Could Chuck Wood ?";
        Map<String, Long> words = Stream.of(text.split(" ")).collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        words.forEach((key, value) -> chart.putEntries(key, value));

        save("PieChartColor");
    }
}
