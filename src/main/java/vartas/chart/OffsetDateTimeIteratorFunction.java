package vartas.chart;

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

import com.google.common.collect.AbstractIterator;

import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This function creates an iterator that visits all time steps in the given interval.
 */
public class OffsetDateTimeIteratorFunction implements BiFunction<OffsetDateTime,OffsetDateTime, Iterator<OffsetDateTime>> {
    /**
     * The function that returns the next timestamp that follows from the current one.
     */
    protected final Function<OffsetDateTime,OffsetDateTime> next;
    /**
     * @param next The function that returns the next timestamp that follows from the current one.
     */
    public OffsetDateTimeIteratorFunction(Function<OffsetDateTime,OffsetDateTime> next){
        this.next = next;
    }
    /**
     * @param before the (inclusive) time stamp of the newest submission.
     * @param after the (exclusive) time stamp of the oldest submission.
     * @return an iterator that visits all time stamps in between.
     */
    @Override
    public Iterator<OffsetDateTime> apply(OffsetDateTime before, OffsetDateTime after) {
        return new DateIterator(before, after, next);
    }
    /**
     * This class implements the iterator that will visit all timestamps in a given interval.
     */
    private static class DateIterator extends AbstractIterator<OffsetDateTime> {
        /**
         * The function that returns the next timestamp that follows from the current one.
         */
        protected final Function<OffsetDateTime, OffsetDateTime> next;
        /**
         * The time stamp of the newest submission.
         */
        protected final OffsetDateTime before;
        /**
         * The current time stamp.
         */
        protected OffsetDateTime current;

        /**
         * @param before the time stamp of the newest submission.
         * @param after  the time stamp of the oldest submission.
         * @param next   the function that returns the next timestamp that follows from the current one.
         */
        public DateIterator(OffsetDateTime before, OffsetDateTime after, Function<OffsetDateTime, OffsetDateTime> next) {
            this.next = next;
            this.before = before;
            this.current = after;
        }

        /**
         * @return the next timestep or endOfData(), if we are already at the end.
         */
        @Override
        protected OffsetDateTime computeNext() {
            //If next returns a value after 'before', 'before' will be assigned to it
            if (current.isEqual(before)) {
                return endOfData();
            }
            current = next.apply(current).isAfter(before) ? before : next.apply(current);
            return current;
        }
    }
}