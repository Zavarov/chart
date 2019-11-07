package vartas.chart.line;

import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Function;

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
public class DelegatingLineChart <T> extends AbstractLineChart <T>{
    private Function<Collection<? extends T>, Long> delegator;

    public DelegatingLineChart(Function<Collection<? extends T>, Long> delegator){
        this.delegator = delegator;
    }

    public DelegatingLineChart(Function<Collection<? extends T>, Long> delegator, Duration lifetime){
        super(lifetime);
        this.delegator = delegator;
    }

    @Override
    protected long count(Collection<? extends T> data) {
        return delegator.apply(data);
    }
}
