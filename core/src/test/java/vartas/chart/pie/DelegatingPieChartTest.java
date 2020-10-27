package vartas.chart.pie;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class DelegatingPieChartTest extends AbstractPieChartTest<String>{
    private DelegatingPieChart<String> chart;
    @Before
    public void setUp(){
        BiFunction<String, Collection<? extends String>, Long> mapper;

        mapper = (k,v) -> (long)v.size();
        chart = new DelegatingPieChart<>(mapper);

        super.init(chart);
    }

    @Test
    public void testCreate(){
        chart.setTitle("Test Pie Chart");
        chart.setAlpha(0.66);

        String text = "How Much Wood Would A Woodchuck Chuck If A Woodchuck Could Chuck Wood ?";
        Map<String, Long> words = Stream.of(text.split(" ")).collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        words.forEach((k,v) -> chart.addAll(k, Collections.nCopies(v.intValue(), k)));

        assertNotNull(chart.create());

        save("PieChart");
    }
}
