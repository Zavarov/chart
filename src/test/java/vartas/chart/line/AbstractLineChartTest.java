package vartas.chart.line;

import org.junit.Test;
import vartas.chart.AbstractChartTest;
import vartas.chart.Interval;

import java.time.temporal.ChronoUnit;

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
public abstract class AbstractLineChartTest <S extends Comparable<S>, T> extends AbstractChartTest <T>{
    private AbstractLineChart<S,T> chart;

    protected void init(AbstractLineChart<S,T> chart){
        super.init(chart);
        this.chart = chart;
    }

    @Test
    public void testXAxisLabel(){
        assertNull(chart.getXAxisLabel());
        chart.setXAxisLabel("x-axis");
        assertEquals(chart.getXAxisLabel(),"x-axis");
    }
    @Test
    public void testYAxisLabel(){
        assertNull(chart.getYAxisLabel());
        chart.setYAxisLabel("y-axis");
        assertEquals(chart.getYAxisLabel(),"y-axis");
    }
    @Test
    public void testInterval(){
        assertNull(chart.getInterval());
        chart.setInterval(Interval.DAY);
        assertEquals(chart.getInterval(),Interval.DAY);
    }
    @Test
    public void testGetGranularity(){
        assertEquals(chart.getGranularity(), ChronoUnit.DAYS);
    }
    @Test
    public void testSetGranularity(){
        assertEquals(chart.getGranularity(), ChronoUnit.DAYS);
        chart.setGranularity(ChronoUnit.MONTHS);
        assertEquals(chart.getGranularity(), ChronoUnit.MONTHS);
    }
}
