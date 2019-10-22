package vartas.chart.pie;

import org.junit.Test;
import vartas.chart.AbstractChartTest;
import vartas.chart.line.AbstractLineChart;

import static org.junit.Assert.assertEquals;
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
public abstract class AbstractPieChartTest <T> extends AbstractChartTest<T> {
    private AbstractPieChart<T> chart;

    protected void init(AbstractPieChart<T> chart){
        super.init(chart);
        this.chart = chart;
    }

    @Test
    public void testAlpha(){
        assertEquals(chart.getAlpha(),0.0, 1e-10);
        chart.setAlpha(0.5);
        assertEquals(chart.getAlpha(),0.5, 1e-10);
    }
}
