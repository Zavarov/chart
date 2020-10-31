/*
 * Copyright (C) 2020  Zavarov
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

package chart.pie;

import chart.AbstractChartTest;
import org.junit.jupiter.api.Test;
import vartas.chart.pie.$factory.PieChartFactory;
import vartas.chart.pie.PieChart;

import java.awt.*;
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

        words.forEach((key, value) -> chart.putEntries(key, value, Color.BLACK));

        save("PieChartColor");
    }
}
