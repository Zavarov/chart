package vartas.chart.pie;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Function<Collection<? extends String>, Map<String,Long>> mapper;

        mapper = col -> col.stream().collect(Collectors.groupingBy(l -> l, Collectors.counting()));
        chart = new DelegatingPieChart<>(mapper);

        super.init(chart);
    }

    @Test
    public void testCreate(){
        chart.setTitle("Test Pie Chart");
        chart.setAlpha(0.66);

        chart.add("How");
        chart.add("Much");
        chart.add("Wood");
        chart.add("Would");
        chart.add("A");
        chart.add("Woodchuck");
        chart.add("Chuck");
        chart.add("If");
        chart.add("A");
        chart.add("Woodchuck");
        chart.addAll(Arrays.asList("Could","Chuck","Wood","?"));

        assertNotNull(chart.create());

        save("PieChart");
    }
}
