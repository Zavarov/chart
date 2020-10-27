package vartas.chart.pie;

import java.util.Collection;
import java.util.function.BiFunction;

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
public class DelegatingPieChart <T> extends AbstractPieChart <T>{
    private BiFunction<String, Collection<? extends T>, Long> delegator;

    public DelegatingPieChart(BiFunction<String, Collection<? extends T>, Long> delegator){
        this.delegator = delegator;
    }
    @Override
    public Long count(String key, Collection<? extends T> value) {
        return delegator.apply(key, value);
    }
}
