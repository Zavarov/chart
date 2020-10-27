package vartas.chart;

import org.jfree.chart.JFreeChart;

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

/**
 * @param <T> the type of the values that are stored in the chart.
 */
public abstract class AbstractChart <T> {
    private String title;

    /**
     * @param title the new title of the chart.
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * @return the current title of the chart.
     */
    public String getTitle(){
        return title;
    }

    /**
     * @return a chart plotting all values that have been added so far.
     */
    public abstract JFreeChart create();
}
